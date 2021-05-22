package com.github.md5sha256.experiementalbrewing.api.drugs.impl;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugMeta;
import com.github.md5sha256.experiementalbrewing.api.slur.ISlurEffect;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DrugMetaImpl implements DrugMeta {

    private final Set<PotionEffect> effects;
    private final boolean enabled;
    private final ISlurEffect effect;
    private final long slurDurationMillis;
    private final int overdoseThreshold;

    DrugMetaImpl(@NotNull Set<@NotNull PotionEffect> effects,
                 boolean enabled,
                 @Nullable ISlurEffect effect,
                 long slurDurationMillis,
                 int overdoseThreshold) {
        this.effects = effects;
        this.enabled = enabled;
        this.effect = effect;
        this.slurDurationMillis = slurDurationMillis;
        this.overdoseThreshold = overdoseThreshold;
    }

    @Override
    public boolean drugEnabled() {
        return this.enabled;
    }

    @Override
    public @NotNull Optional<@NotNull ISlurEffect> effect() {
        return Optional.ofNullable(this.effect);
    }

    @Override
    public long slurDuration(@NotNull TimeUnit timeUnit) {
        return timeUnit.convert(this.slurDurationMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public long slurDurationMillis() {
        return this.slurDurationMillis;
    }

    @Override
    public @NotNull Set<@NotNull PotionEffect> effects() {
        return this.effects;
    }

    @Override
    public int overdoseThreshold() {
        return this.overdoseThreshold;
    }
}
