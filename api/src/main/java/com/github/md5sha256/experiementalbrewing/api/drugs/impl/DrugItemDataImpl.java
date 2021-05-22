package com.github.md5sha256.experiementalbrewing.api.drugs.impl;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugItemData;
import com.github.md5sha256.experiementalbrewing.api.drugs.IDrug;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DrugItemDataImpl implements DrugItemData {

    private final IDrug drug;
    private final IDrugForm drugForm;

    public DrugItemDataImpl(@NotNull IDrug drug, @NotNull IDrugForm form) {
        this.drug = Objects.requireNonNull(drug);
        this.drugForm = Objects.requireNonNull(form);
    }

    @NotNull
    @Override
    public IDrug drug() {
        return this.drug;
    }

    @Override
    public @NotNull IDrugForm form() {
        return this.drugForm;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugItemDataImpl that = (DrugItemDataImpl) o;

        if (!drug.equals(that.drug)) return false;
        return drugForm.equals(that.drugForm);
    }

    @Override
    public int hashCode() {
        int result = drug.hashCode();
        result = 31 * result + drugForm.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DrugItemData{" +
                "drug=" + drug +
                ", drugForm=" + drugForm +
                '}';
    }
}
