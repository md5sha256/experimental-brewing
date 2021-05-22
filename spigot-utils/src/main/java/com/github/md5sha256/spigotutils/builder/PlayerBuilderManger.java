package com.github.md5sha256.spigotutils.builder;

import com.github.md5sha256.spigotutils.logging.ILogger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class PlayerBuilderManger {

    private static final String SECTION_KEY = "Player_Builder_Data", SIZE_KEY = "Size";

    private final BuilderManager builderManager;

    private final Map<UUID, List<StagedBuilder<?>>> builderMap = new HashMap<>();
    private transient final Map<UUID, Conversation> conversationMap = new HashMap<>();

    @Inject
    private ILogger logger;

    @Inject
    private Plugin plugin;

    @Inject
    public PlayerBuilderManger(@NotNull final BuilderManager builderManager) {
        this.builderManager = builderManager;
    }

    public void load(@NotNull final ConfigurationSection section) {
        this.builderMap.clear();
        this.conversationMap.clear();
        final ConfigurationSection actual = section.getConfigurationSection(SECTION_KEY);
        if (actual==null) {
            return;
        }
        for (final String s : actual.getKeys(false)) {
            final UUID uuid = UUID.fromString(s);
            final ConfigurationSection playerSection = actual.getConfigurationSection(s);
            assert playerSection!=null;
            final List<String> rawClasses = new LinkedList<>();
            final List<StagedBuilder.InputData> inputData = new LinkedList<>();
            for (final String rawIndex : playerSection.getKeys(false)) {
                if (s.equalsIgnoreCase(SIZE_KEY)) {
                    final int size = Integer.parseInt(s);
                    rawClasses.addAll(Collections.nCopies(size, null));
                    inputData.addAll(Collections.nCopies(size, null));
                    continue;
                }
                final int index = Integer.parseInt(rawIndex);
                ConfigurationSection indexSection = playerSection.getConfigurationSection(rawIndex);
                assert indexSection!=null;
                rawClasses.add(index, indexSection.getString("Class"));
                inputData.add(index,
                              indexSection.getSerializable("Data", StagedBuilder.InputData.class));
            }
            rawClasses.removeIf(Objects::isNull); // Remove null objects.
            final List<StagedBuilder<?>> builders = rawClasses.stream().map((value) -> {
                try {
                    return Class.forName(value);
                } catch (final ClassNotFoundException ex) {
                    logger.error("Generic class not found! Class: " + value);
                }
                return null;
            }).filter(Objects::nonNull).map((clazz) -> {
                final Optional<? extends StagedBuilder<?>> optional =
                        builderManager.getCachedBuilder(clazz);
                if (!optional.isPresent()) {
                    final String message =
                            "Builder not found when deserializing! Missing builder for generic type: "
                                    + clazz.getCanonicalName();
                    logger.error(message);
                    return null;
                }
                return optional.get();
            }).filter(Objects::nonNull).collect(Collectors.toList());
            builderMap.put(uuid, builders);
        }

    }

    public void save(@NotNull final ConfigurationSection section) {
        final ConfigurationSection actual = section.createSection(SECTION_KEY);
        for (final Map.Entry<UUID, List<StagedBuilder<?>>> entry : builderMap.entrySet()) {
            final ConfigurationSection playerSection =
                    actual.createSection(entry.getKey().toString());
            final int index = 0;
            for (final StagedBuilder<?> builder : entry.getValue()) {
                ConfigurationSection indexSection =
                        playerSection.createSection(String.valueOf(index));
                indexSection.set("Class", builder.getGenericType().getCanonicalName());
                indexSection.set("Data", builder.getInputData());
                playerSection
                        .set(String.valueOf(index), builder.getGenericType().getCanonicalName());
            }
        }
    }

    public Optional<StagedBuilder<?>> getLastBuilder(@NotNull final UUID player) {
        return Optional.ofNullable(builderMap.get(player))
                       .map(builders -> builders.isEmpty() ? null:builders
                               .get(builders.size() - 1));
    }

    public void addBuilder(@NotNull final UUID player, @NotNull final StagedBuilder<?> builder) {
        final List<StagedBuilder<?>> builders =
                builderMap.computeIfAbsent(player, (unused) -> new LinkedList<>());
    }

    public void flagUsed(@NotNull final StagedBuilder<?> builder) {
        for (final List<StagedBuilder<?>> list : builderMap.values()) {
            if (list.contains(builder)) {
                list.remove(builder);
                list.add(builder); // Reset index
            }
        }
    }

    /***
     * Get the current list of builders a player has accessed.
     * The lowest index indicates the most stale builder and the highest index
     * indicates the most recently accessed builder.
     * @param player The UUID of the player.
     * @return Returns a copy of the list of builders a player has accessed.
     */
    public List<StagedBuilder<?>> getAllBuilders(@NotNull final UUID player) {
        if (!builderMap.containsKey(player)) {
            return new LinkedList<>();
        }
        return builderMap.get(player);
    }

    public boolean isBuilding(@NotNull final Player player) {
        return conversationMap.containsKey(player.getUniqueId());
    }

    public void clearConversation(@NotNull final Player player) {
        conversationMap.remove(player.getUniqueId());
    }

    public Optional<Conversation> getCurrentConversation(@NotNull final Player player) {
        return Optional.ofNullable(conversationMap.get(player.getUniqueId()));
    }

    public boolean startBuildProcess(@NotNull final Player player,
                                     @NotNull final Conversation conversation) {
        if (isBuilding(player)) {
            return false;
        }
        conversationMap.put(player.getUniqueId(), conversation);
        player.beginConversation(conversation);
        return true;
    }

    public Conversation buildConversation(@NotNull final Player player,
                                          @NotNull final StagedBuilder<?> builder) {
        final ConversationFactory factory = new ConversationFactory(plugin);
        factory.addConversationAbandonedListener((conversation) -> {
            final Conversable conversable = conversation.getContext().getForWhom();
            if (conversable instanceof Player) {
                final Player playerObject = ((Player) conversable);
                conversationMap.remove(
                        playerObject.getUniqueId()); // Send empty message to break from the builder
            }
        });

        final Conversation conversation = factory.withModality(false).withLocalEcho(false)
                                                 .withFirstPrompt(builder.getFirstPrompt())
                                                 .addConversationAbandonedListener(
                                                         (ctx) -> conversationMap
                                                                 .remove(((Player) ctx.getContext()
                                                                                      .getForWhom())
                                                                                 .getUniqueId()))
                                                 .buildConversation(player);
        builderMap.computeIfAbsent(player.getUniqueId(), (unused) -> new LinkedList<>())
                  .add(builder);

        return conversation;
    }


}
