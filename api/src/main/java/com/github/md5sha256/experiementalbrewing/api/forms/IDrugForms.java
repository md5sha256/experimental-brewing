package com.github.md5sha256.experiementalbrewing.api.forms;

import org.jetbrains.annotations.NotNull;

public interface IDrugForms {

    @NotNull IDrugForm defaultForm();

    @NotNull IDrugForm powder();

    @NotNull IDrugForm syringe();

    @NotNull IBlunts blunt();

}
