package com.github.md5sha256.experimentalbrewing.configuration;

import org.jetbrains.annotations.NotNull;

public interface ExperimentalBrewingPluginConfiguration {

    @NotNull MessageRegistry messages();

    @NotNull DrugConfiguration drugConfiguration();

    @NotNull ShopConfiguration shopConfiguration();

}
