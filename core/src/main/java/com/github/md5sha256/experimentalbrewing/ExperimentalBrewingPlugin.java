package com.github.md5sha256.experimentalbrewing;

import com.github.md5sha256.experiementalbrewing.api.ExperimentalBrewing;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugHandler;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugRegistry;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForms;
import com.github.md5sha256.experiementalbrewing.api.slur.SlurEffectState;
import com.github.md5sha256.experimentalbrewing.configuration.ExperimentalBrewingPluginConfiguration;
import com.github.md5sha256.experimentalbrewing.module.ExperimentalBrewingModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ExperimentalBrewingPlugin extends JavaPlugin implements ExperimentalBrewing, SlimefunAddon {

    private Injector injector;
    private DrugRegistry drugRegistry;
    private IDrugForms drugForms;
    private SlurEffectState slurEffectState;
    private DrugHandler drugHandler;
    private ExperimentalBrewingPluginConfiguration experimentalBrewingPluginConfiguration;

    @Override
    public @NotNull DrugRegistry drugRegistry() {
        return this.drugRegistry;
    }

    @Override
    public @NotNull IDrugForms drugForms() {
        return this.drugForms;
    }

    @Override
    public @NotNull SlurEffectState slurEffectState() {
        return this.slurEffectState;
    }

    @Override
    public @NotNull DrugHandler drugHandler() {
        return this.drugHandler;
    }

    public @NotNull ExperimentalBrewingPluginConfiguration addictiveExperienceConfiguration() {
        return this.experimentalBrewingPluginConfiguration;
    }

    private void initializeInjector() {
        final Injector injector = Guice
                .createInjector(Stage.PRODUCTION, new ExperimentalBrewingModule(this));
        this.drugForms = injector.getInstance(IDrugForms.class);
        this.slurEffectState = injector.getInstance(SlurEffectState.class);
        this.drugHandler = injector.getInstance(DrugHandler.class);
        // Missing bindings/implementations
        this.experimentalBrewingPluginConfiguration = injector.getInstance(
                ExperimentalBrewingPluginConfiguration.class);
        this.drugRegistry = injector.getInstance(DrugRegistry.class);
        this.injector = injector;
    }

    private void shutdownInjector() {
        this.injector = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        shutdownInjector();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        initializeInjector();
    }


    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public @Nullable String getBugTrackerURL() {
        return null;
    }

}
