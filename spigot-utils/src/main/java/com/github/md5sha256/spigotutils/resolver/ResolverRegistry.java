package com.github.md5sha256.spigotutils.resolver;

import com.github.md5sha256.spigotutils.Common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ResolverRegistry {

    public Map<Class<?>, ResolverMeta<?>> metaMap = new ConcurrentHashMap<>();

    public <T> ResolverMeta<T> registerResolver(final Class<T> clazz) throws ReflectiveOperationException {
        if (!metaMap.containsKey(clazz)) {
            final ResolverMeta<T> meta = new ResolverMeta<>(clazz);
            metaMap.put(clazz, meta);
            return meta;
        }
        return Common.unsafeCast(metaMap.get(clazz));
    }

    public <T> Optional<Resolver<T>> getResolver(final Class<T> clazz, final String context) {
        Optional<ResolverMeta<T>> optional = Optional.ofNullable(Common.unsafeCast(metaMap.get(clazz)));
        if (optional.isPresent()) {
            return optional.get().getResolver(context);
        }
        return Optional.empty();
    }

    private static class ResolverMeta<T> {

        private final Map<String, Resolver<T>> resolvers;

        public ResolverMeta(final Class<T> clazz) throws ReflectiveOperationException {
            this.resolvers = Collections.unmodifiableMap(resolve(clazz));
        }

        private Map<String, Resolver<T>> resolve(final Class<T> clazz) {
            final Map<String, Resolver<T>> resolvers = new HashMap<>();
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (Resolver.isEligible(clazz, constructor)) {
                    final Resolver<T> resolver = new Resolver<>((Constructor<T>) constructor);
                    resolvers.put(resolver.getContext(), resolver);
                }
            }
            for (Method method : clazz.getMethods()) {
                if (Resolver.isEligible(clazz, method)) {
                    final Resolver<T> resolver = new Resolver<>(clazz, method);
                    resolvers.put(resolver.getContext(), resolver);
                }
            }
            return resolvers;
        }

        public Optional<Resolver<T>> getResolver(final String context) {
            return Optional.ofNullable(resolvers.get(context));
        }

        public Map<String, Resolver<T>> getResolvers() {
            return resolvers;
        }
    }

}
