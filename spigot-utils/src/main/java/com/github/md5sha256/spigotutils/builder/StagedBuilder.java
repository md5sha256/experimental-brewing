package com.github.md5sha256.spigotutils.builder;

import com.github.md5sha256.spigotutils.Common;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;

public class StagedBuilder<T> {

    private static final Object EMPTY_OBJECT = new Object();
    private static final Prompt INCOMPLETE_EXIT_PROMPT = new Prompt() {

        @Override
        public boolean blocksForInput(@NotNull final ConversationContext context) {
            return false;
        }

        @Override
        public @NotNull String getPromptText(@NotNull final ConversationContext context) {
            return Common
                    .legacyColorise("&aYou have exited the builder! All your progress has been saved.");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull final ConversationContext context,
                                            @Nullable final String input) {
            return Prompt.END_OF_CONVERSATION;
        }
    };

    private static final Prompt BUILD_SUCCESS_PROMPT = new Prompt() {

        @Override
        public boolean blocksForInput(@NotNull final ConversationContext context) {
            return false;
        }

        @Override
        public @NotNull String getPromptText(@NotNull final ConversationContext context) {
            return Common.legacyColorise("&aBuild success!");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull final ConversationContext context,
                                            @Nullable final String input) {
            return Prompt.END_OF_CONVERSATION;
        }
    };
    private final BuilderManager builderManager;
    private final Class<T> type;
    private final Object invokingObject;
    private final Executable executable;
    private final List<String> paramNames = new LinkedList<>();
    private final List<Class<?>> paramTypes = new LinkedList<>();
    private InputData inputData;
    private Prompt firstPrompt;
    private String displayName;
    private List<String> notes;
    private Consumer<T> onBuildSuccess = (unused) -> {
    };


    public StagedBuilder(@NotNull final Class<T> clazz, @Nullable final Object invokingObj,
                         @NotNull final Method method, @NotNull final BuilderManager builderManager) {
        this.type = clazz;
        this.displayName = type.getSimpleName();
        this.builderManager = builderManager;
        this.invokingObject = invokingObj;
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        if (method.getReturnType() != clazz) {
            throw new IllegalArgumentException("Method does not have the correct return type!");
        }
        for (final Parameter parameter : method.getParameters()) {
            if (!builderManager.canResolve(parameter.getType())) {
                throw new IllegalStateException(
                        "No resolver found for param type: " + parameter.getType().getCanonicalName());
            }
            paramNames.add(parameter.getName());
            paramTypes.add(parameter.getType());
        }
        this.executable = method;
        withNotes(new ArrayList<>());
        this.inputData = new InputData(this);
        this.firstPrompt = buildPrompt();
        builderManager.registerBuilder(this);
    }

    public StagedBuilder(@NotNull final Class<T> clazz, @NotNull final Constructor<T> constructor,
                         @NotNull final BuilderManager builderManager) {
        this.type = clazz;
        this.displayName = type.getSimpleName();
        this.invokingObject = null;
        this.builderManager = builderManager;
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        for (final Parameter parameter : constructor.getParameters()) {
            if (!builderManager.canResolve(parameter.getType())) {
                throw new IllegalStateException(
                        "No resolver found for param type: " + parameter.getType().getCanonicalName());
            }
            paramNames.add(parameter.getName());
            paramTypes.add(parameter.getType());
        }
        this.executable = constructor;
        withNotes(new ArrayList<>());
        this.inputData = new InputData(this);
        this.firstPrompt = buildPrompt();
        builderManager.registerBuilder(this);
    }

    public StagedBuilder<T> withInputData(@NotNull final InputData inputData) {
        inputData.clearResolved(); // Force clear
        inputData.resolveInput(this); // Try resolving.
        this.inputData = inputData;
        return this;
    }

    public StagedBuilder<T> withNotes(final List<String> notes) {
        if (notes.size() < paramTypes.size()) {
            this.notes = new ArrayList<>(notes);
            final int diff = paramTypes.size() - notes.size();
            this.notes.addAll(Collections.nCopies(diff, ""));
        } else {
            this.notes = new ArrayList<>(notes);
        }
        return this;
    }

    @NotNull
    public InputData getInputData() {
        return inputData;
    }

    public StagedBuilder<T> withActionOnComplete(@Nullable final Consumer<T> onComplete) {
        this.onBuildSuccess = onComplete == null ? (unused) -> {
        } : onComplete;
        return this;
    }

    public StagedBuilder<T> withDisplayName(@NotNull final String name) {
        this.displayName = name;
        this.firstPrompt = buildPrompt();
        return this;
    }


    public StagedBuilder<T> withFreshInput() {
        final StagedBuilder<T> builder;
        if (executable instanceof Constructor) {
            builder = new StagedBuilder<>(this.type, Common.unsafeCast(this.executable),
                    this.builderManager);
        } else {
            builder = new StagedBuilder<>(this.type, invokingObject, (Method) this.executable,
                    this.builderManager);
        }
        builder.inputData = new InputData(this.inputData);
        return builder;
    }

    public Class<?> getRequiredType(final int paramIndex) {
        return paramTypes.get(paramIndex);
    }

    public String getParamName(final int paramIndex) {
        return paramNames.get(paramIndex);
    }

    public StagedBuilder<T> setValue(final int index, final String value) {
        inputData.appendInput(index, value, this);
        return this;
    }

    public Class<T> getGenericType() {
        return type;
    }

    public List<Class<?>> getParamTypes() {
        return Collections.unmodifiableList(paramTypes);
    }

    public List<String> getParamNames() {
        return Collections.unmodifiableList(paramNames);
    }

    public List<Object> getResolved() {
        return new ArrayList<>(inputData.getResolvedInput(this));
    }

    public Prompt getFirstPrompt() {
        return firstPrompt;
    }

    private Prompt buildFieldSetterPrompt(final int paramIndex) {
        final Class<?> requiredType = getRequiredType(paramIndex);
        final String typeName = requiredType.getSimpleName();
        final String notes = this.notes.get(paramIndex);
        final StagedBuilder<?> builder = this;
        return new StringPrompt() {

            @Override
            public @NotNull String getPromptText(@NotNull final ConversationContext context) {
                String value =
                        "&7Please enter " + (typeName.indexOf("aeiou") == 0 ? "an" : "a") + " "
                                + typeName + ".";
                if (notes != null && !notes.isEmpty()) {
                    value = value + " Notes: " + notes;
                }
                return Common.legacyColorise(value);
            }

            @Override
            public @Nullable Prompt acceptInput(@NotNull final ConversationContext context,
                                                @Nullable final String input) {
                try {
                    inputData.appendInput(paramIndex, input == null ? "" : input, builder);
                    return buildPrompt();
                } catch (final Exception e) { // If resolver failed means invalid.
                    context.getForWhom().sendRawMessage(Common.legacyColorise(
                            "&cAn error occurred when trying to set the value for " + paramNames
                                    .get(paramIndex) + "! Reason: " + e.getMessage()));
                    return buildFieldSetterPrompt(paramIndex);
                }
            }
        };
    }

    private boolean isComplete() {
        if (getResolved().isEmpty()) {
            return false;
        }
        return !getResolved().contains(EMPTY_OBJECT);
    }

    private Prompt buildPrompt() {
        final List<String> strings = new LinkedList<>();
        strings.add(" ");
        final String header = "&2--- " + this.displayName + " Builder ---";
        strings.add(header);
        strings.add(" ");

        for (int index = 0; index < paramTypes.size(); index++) {
            String field_name = getParamName(index);
            final String notes = this.notes.get(index);
            final Class<?> type = getRequiredType(index);
            final Object current = getResolved().get(index);
            final boolean hasValue;
            if (current instanceof String) {
                hasValue = !((String) current).trim().isEmpty();
            } else {
                hasValue = current != EMPTY_OBJECT;
            }
            field_name = hasValue ? "&a" + field_name : "&c" + field_name;
            String message =
                    "&7" + index + ") &fName: " + field_name + " &f| &5Type: " + type.getSimpleName() + " ";
            if (notes != null && !notes.isEmpty()) {
                message = message + "(" + notes + ") ";
            }
            message =
                    message + "&f| &7Current Value: " + (hasValue ? String.valueOf(current) : "unset");
            strings.add(message);
        }
        strings.add(" ");
        strings.add("&7To exit this builder at any moment, type &fexit &7or &fquit&7.");
        strings.add("&7To force a progress save, type &fsave&7.");
        strings.add("&7To build the finished product, type &fbuild&7.");
        return new StringPrompt() {

            @Override
            public @NotNull String getPromptText(@NotNull final ConversationContext context) {
                final Conversable conversable = context.getForWhom();
                for (final String s : strings) {
                    conversable.sendRawMessage(Common.legacyColorise(s));
                }
                return "";
            }

            @Override
            public @Nullable Prompt acceptInput(@NotNull final ConversationContext context,
                                                @Nullable final String input) {
                if (input == null) {
                    return this;
                }
                switch (ChatColor.stripColor(input)) {
                    case "save":
                    case "exit":
                    case "quit":
                        return INCOMPLETE_EXIT_PROMPT;
                    case "build":
                        final String failureMessage = Common
                                .legacyColorise("&cBuild Failure. Reason: Parameters are still missing!");
                        if (!isComplete()) {
                            context.getForWhom().sendRawMessage(failureMessage);
                            return this;
                        }
                        try {
                            final T product = build();
                            onBuildSuccess.accept(product);
                            return BUILD_SUCCESS_PROMPT;
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                            context.getForWhom().sendRawMessage(Common
                                    .legacyColorise("&cBuilder Failure! Reason: Unknown error occurred."));
                        }
                    default:
                        break;
                }
                try {
                    final int paramIndex = Integer.parseInt(input);
                    if (paramIndex < 0 || paramIndex > paramTypes.size() - 1) {
                        throw new NumberFormatException();
                    }
                    return buildFieldSetterPrompt(paramIndex);
                } catch (final NumberFormatException ex) {
                    final int size = paramTypes.size() - 1;
                    final String message;
                    if (size < 1) {
                        message = "&7Invalid Input! Expected 0";
                    } else {
                        message = "&7Invalid Input! Expected a number between 0 and " + size + ".";
                    }
                    context.getForWhom().sendRawMessage(Common.legacyColorise(message));
                    return this;
                }
            }
        };
    }

    public T build() throws ReflectiveOperationException {
        if (executable instanceof Constructor) {
            final Constructor<T> ctor = Common.unsafeCast(executable);
            final int len = ctor.getParameters().length;
            Object[] arr = getResolved().toArray(new Object[0]);
            if (arr.length > len) {
                arr = Arrays.copyOfRange(arr, 0, len);
            }
            return ctor.newInstance(arr);
        } else {
            final Method method = Common.unsafeCast(executable);
            return type.cast(method.invoke(invokingObject, getResolved().toArray(new Object[0])));
        }
    }

    public static class InputData implements ConfigurationSerializable {

        private static final String SIZE_KEY = "Size";

        private final List<String> rawInput = new LinkedList<>();
        private final List<Object> resolvedInput = new LinkedList<>();

        InputData(@NotNull final StagedBuilder<?> builder) {
            resolveInput(builder);
        }

        public InputData(final Map<String, Object> serial) {
            if (!serial.containsKey(SIZE_KEY)) {
                throw new IllegalArgumentException("Invalid serial! Missing keys!");
            }
            final int max = Common.unsafeCast(serial.get(SIZE_KEY));
            for (int index = 0; index < max; index++) {
                rawInput.add((String) serial.get(String.valueOf(index)));
            }
        }

        public InputData(@NotNull final InputData other) {
            this.rawInput.addAll(other.rawInput);
            this.resolvedInput.addAll(other.resolvedInput);
        }

        public void appendInput(final int index, @NotNull final String input,
                                @NotNull final StagedBuilder<?> builder) {
            if (index > resolvedInput.size() - 1) {
                final int diff = index - resolvedInput.size() - 1;
                final Collection<String> strings = Collections.nCopies(diff, " ");
                rawInput.addAll(strings);
            }
            rawInput.set(index, input);
            resolveInput(builder, index);
        }

        public void resolveInput(@NotNull final StagedBuilder<?> builder, final int paramIndex) {
            final Class<?> clazz = builder.getRequiredType(paramIndex);
            try {
                String input = rawInput.get(paramIndex);
                if (input.trim().isEmpty()) {
                    return;
                }
                final Object resolved =
                        builder.builderManager.getResolver(clazz).apply(input);
                resolvedInput.set(paramIndex, resolved);
            } catch (Exception ignored) { // Ignore failed resolution.
            }
        }


        public void resolveInput(@NotNull final StagedBuilder<?> builder) {
            for (int paramIndex = 0; paramIndex < rawInput.size(); paramIndex++) {
                resolveInput(builder, paramIndex);
            }
            if (rawInput.size() < builder.paramTypes.size()) { // Fill with empty input.
                final int diff = builder.paramTypes.size() - rawInput.size();
                rawInput.addAll(Collections.nCopies(diff, " "));
                resolvedInput.addAll(Collections.nCopies(diff, EMPTY_OBJECT));
            }
        }

        public List<String> getRawInput() {
            return rawInput;
        }

        public List<Object> getResolvedInput() {
            return resolvedInput;
        }

        public List<Object> getResolvedInput(@NotNull final StagedBuilder<?> builder) {
            resolveInput(builder);
            return resolvedInput;
        }

        public void clearRaw() {
            clearResolved();
            rawInput.clear();
        }

        public void clearResolved() {
            resolvedInput.clear();
        }

        @Override
        public @NotNull Map<String, Object> serialize() {
            final Map<String, Object> map = new HashMap<>();
            map.put(SIZE_KEY, rawInput.size());
            int index = 0;
            for (final String s : rawInput) {
                map.put(String.valueOf(index++), s);
            }
            return map;
        }
    }

}
