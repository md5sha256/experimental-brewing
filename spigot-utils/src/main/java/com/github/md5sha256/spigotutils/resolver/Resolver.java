package com.github.md5sha256.spigotutils.resolver;

import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.reflect.*;
import java.util.*;

public class Resolver<T> {

    @Inject
    private Injector injector;
    @Inject
    private ResolverRegistry resolverManager;
    private final Class<T> clazz;
    private final boolean ctor;
    private final Executable executable;
    private final Collection<Parameter> parameters;
    private final String context;

    public Resolver(Constructor<T> executable) {
        this.clazz = executable.getDeclaringClass();
        this.executable = executable;
        if (!isEligible(clazz, executable)) {
            throw new IllegalArgumentException("Invalid Constructor passed! Non-default constructor not annotated with @PaginationObjectCreator class found!");
        }
        if (executable.getParameters().length == 0) {
            this.context = null;
            this.parameters = Collections.emptyList();
        } else {
            this.context = executable.getAnnotation(ObjectCreator.class).context();
            this.parameters = Arrays.asList(executable.getParameters());
        }
        this.ctor = true;
    }

    public Resolver(final Class<T> clazz, Method method) {
        this.clazz = clazz;
        this.executable = method;
        if (!isEligible(clazz, method)) {
            throw new IllegalArgumentException("Invalid Constructor passed! Non-default constructor not annotated with @PaginationObjectCreator class found!");
        }
        if (executable.getParameters().length == 0) {
            this.context = null;
            this.parameters = Collections.emptyList();
        } else {
            this.context = executable.getAnnotation(ObjectCreator.class).context();
            this.parameters = Arrays.asList(executable.getParameters());
        }
        this.ctor = true;
    }

    public static <T> boolean isEligible(Class<T> clazz, Executable executable) {
        boolean val;
        if (executable instanceof Method) {
            val = clazz.isAssignableFrom(((Method) executable).getReturnType());
        } else {
            val = clazz.isAssignableFrom(((Constructor<T>) executable).getDeclaringClass());
        }
        return val && (executable.getParameters().length == 0 || executable.isAnnotationPresent(ObjectCreator.class));
    }

    public String getContext() {
        return context;
    }

    protected T resolve(InputData<T> inputData) throws ReflectiveOperationException {

        final List<Object> resolved = new ArrayList<>();
        for (Parameter parameter : executable.getParameters()) {

        }
        if (this.ctor) {
            return clazz.cast(((Constructor<?>) executable).newInstance(resolved));
        } else {
            return clazz.cast(((Method) executable).invoke(null, resolved));
        }
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }
}
