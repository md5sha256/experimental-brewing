package com.github.md5sha256.experiementalbrewing.api.drugs.impl;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugPlantMeta;
import com.github.md5sha256.experiementalbrewing.api.drugs.IDrugComponent;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class DrugPlantMetaBuilder {

    private long growthTimeMillis;
    private double seedDropProbability;
    private int seedDropAmount;
    private double harvestProbability;
    private int harvestAmount;
    private IDrugComponent seed;

    public DrugPlantMetaBuilder() {
    }

    public DrugPlantMetaBuilder(@NotNull DrugPlantMetaBuilder builder) {
        this.growthTimeMillis = builder.growthTimeMillis;
        this.seedDropProbability = builder.seedDropProbability;
        this.seedDropAmount = builder.seedDropAmount;
        this.harvestProbability = builder.harvestProbability;
        this.harvestAmount = builder.harvestAmount;
        this.seed = builder.seed;
    }

    public DrugPlantMetaBuilder(@NotNull DrugPlantMeta meta) {
        this.growthTimeMillis = meta.growthTimeMillis();
        this.seedDropProbability = meta.seedDropProbability();
        this.seedDropAmount = meta.seedDropAmount();
        this.harvestProbability = meta.harvestSuccessProbability();
        this.harvestAmount = meta.harvestAmount();
        this.seed = meta.seed().orElse(null);
    }

    public DrugPlantMetaBuilder growthTimeMillis(long growthTimeMillis) {
        this.growthTimeMillis = growthTimeMillis;
        return this;
    }

    public DrugPlantMetaBuilder growthTime(long growthTime, @NotNull TimeUnit timeUnit) {
        this.growthTimeMillis = timeUnit.toMillis(growthTime);
        return this;
    }

    public DrugPlantMetaBuilder seedDropProbability(double seedDropProbability) {
        this.seedDropProbability = seedDropProbability;
        return this;
    }

    public DrugPlantMetaBuilder seedDropAmount(int seedDropAmount) {
        this.seedDropAmount = seedDropAmount;
        return this;
    }

    public DrugPlantMetaBuilder harvestProbability(double harvestProbability) {
        this.harvestProbability = harvestProbability;
        return this;
    }

    public DrugPlantMetaBuilder harvestAmount(int harvestAmount) {
        this.harvestAmount = harvestAmount;
        return this;
    }

    public DrugPlantMetaBuilder seed(IDrugComponent seed) {
        this.seed = seed;
        return this;
    }

    private void validate() {
        Validate.isTrue(growthTimeMillis > 0, "Invalid growth time: ", growthTimeMillis);
        Validate.isTrue(seedDropAmount >= 0, "Invalid seed drop amount: ", seedDropAmount);
        Validate.isTrue(seedDropProbability >= 0,
                        "Invalid seed drop probability: ",
                        seedDropProbability);
        Validate.isTrue(harvestProbability >= 0,
                        "Invalid harvest probability: ",
                        harvestProbability);
        Validate.isTrue(harvestAmount >= 0, "Invalid harvest amount: ", harvestAmount);
    }

    public DrugPlantMetaImpl build() {
        validate();
        return new DrugPlantMetaImpl(growthTimeMillis,
                                     seedDropProbability,
                                     seedDropAmount,
                                     harvestProbability,
                                     harvestAmount,
                                     seed);
    }

}
