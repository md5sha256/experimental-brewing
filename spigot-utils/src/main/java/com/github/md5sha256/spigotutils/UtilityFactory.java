package com.github.md5sha256.spigotutils;

import com.google.inject.assistedinject.Assisted;
import org.jetbrains.annotations.NotNull;

public interface UtilityFactory {

    @NotNull <T> TimedObjectManager<T> newObjectManager(@Assisted("maxTimeMillis") final long maxTimeMillis,
                                                        @Assisted("updateIntervalTicks") final long updateIntervalTicks);

    @NotNull <T> TimedObjectManager<T> newObjectManager(@Assisted("maxTimeMillis") final long maxTimeMillis,
                                                        @Assisted("updateIntervalTicks") final long updateIntervalTicks,
                                                        boolean async);

}
