package com.github.md5sha256.experimentalbrewing.implementation;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugBloodData;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugCooldownData;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugHandler;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugMeta;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugRegistry;
import com.github.md5sha256.experiementalbrewing.api.drugs.IDrug;
import com.github.md5sha256.experiementalbrewing.api.drugs.impl.DrugCooldownDataImpl;
import com.github.md5sha256.experimentalbrewing.util.Utils;
import com.google.inject.Inject;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DrugHandlerImpl implements DrugHandler {

    @Inject
    private DrugRegistry drugRegistry;

    private final Map<UUID, DrugBloodData> bloodDataMap = new HashMap<>();
    private final DrugCooldownData cooldownData = new DrugCooldownDataImpl();

    public DrugHandlerImpl() {
    }

    @Override
    public @NotNull Optional<@NotNull DrugBloodData> bloodData(@NotNull UUID player) {
        return Optional.ofNullable(this.bloodDataMap.get(player));
    }

    public @NotNull DrugBloodData getOrCreateBloodData(@NotNull UUID player) {
        return this.bloodDataMap.computeIfAbsent(player, DrugBloodData::create);
    }

    @Override
    public @NotNull DrugCooldownData cooldownData() {
        return this.cooldownData;
    }

    @Override
    public void clearData(@NotNull UUID player) {
        this.cooldownData.clear(player);
        this.bloodDataMap.remove(player);
    }

    @Override
    public boolean overdose(@NotNull LivingEntity player, @NotNull IDrug drug) {
        final DrugBloodData drugBloodData = this.bloodDataMap.get(player.getUniqueId());
        final Optional<@NotNull DrugMeta> optional = drugRegistry.metaData(drug, DrugMeta.KEY);
        return optional.filter(meta -> drugBloodData.getLevel(drug) >= meta.overdoseThreshold()).isPresent();
    }

    @Override
    public void notifyIfOverdosed(@NotNull LivingEntity player, @NotNull IDrug drug) {
        if (!overdose(player, drug)) {
            return;
        }
        // FIXME prefix and overdoseMsg
        player.sendMessage("prefix" + Utils.legacyColorize("overdose message"));
    }

}
