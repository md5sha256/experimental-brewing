package com.github.md5sha256.experiementalbrewing.api.drugs;

import com.github.md5sha256.experiementalbrewing.api.drugs.impl.DrugItemDataImpl;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForm;
import com.github.md5sha256.experiementalbrewing.api.util.SimilarLike;
import org.jetbrains.annotations.NotNull;

public interface DrugItemData extends SimilarLike<DrugItemData> {

    @NotNull
    static DrugItemData of(@NotNull IDrug drug, @NotNull IDrugForm drugForm) {
        return new DrugItemDataImpl(drug, drugForm);
    }

    @NotNull IDrug drug();

    @NotNull IDrugForm form();

    @Override
    default boolean isSimilar(@NotNull DrugItemData other) {
        return this.drug().equals(other.drug()) && this.form().equals(other.form());
    }
}
