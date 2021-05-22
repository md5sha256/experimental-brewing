package com.github.md5sha256.experiementalbrewing.api;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugHandler;
import com.github.md5sha256.experiementalbrewing.api.drugs.DrugRegistry;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForms;
import com.github.md5sha256.experiementalbrewing.api.slur.SlurEffectState;
import org.jetbrains.annotations.NotNull;

public interface ExperimentalBrewing {

    @NotNull DrugRegistry drugRegistry();

    @NotNull IDrugForms drugForms();

    @NotNull SlurEffectState slurEffectState();

    @NotNull DrugHandler drugHandler();

}
