package com.github.md5sha256.experimentalbrewing.module;

import com.github.md5sha256.experiementalbrewing.api.ExperimentalBrewing;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugHandler;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForms;
import com.github.md5sha256.experiementalbrewing.api.slur.SlurEffectState;
import com.github.md5sha256.experimentalbrewing.implementation.DrugHandlerImpl;
import com.github.md5sha256.experimentalbrewing.implementation.SlurEffectStateImpl;
import com.github.md5sha256.experimentalbrewing.implementation.forms.DrugForms;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;

public final class ExperimentalBrewingModule extends AbstractModule {

    private final ExperimentalBrewing api;

    public ExperimentalBrewingModule(@NotNull ExperimentalBrewing api) {
        this.api = api;
    }

    @Override
    protected void configure() {
        // Drugs Implementation
        install(new DrugsModule());

        // Implementation
        bind(DrugHandler.class).to(DrugHandlerImpl.class).asEagerSingleton();
        bind(SlurEffectState.class).to(SlurEffectStateImpl.class).asEagerSingleton();

        // API bindings
        bind(IDrugForms.class).to(DrugForms.class).in(Singleton.class);
        bind(ExperimentalBrewing.class).toInstance(this.api);
    }
}
