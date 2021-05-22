package com.github.md5sha256.experiementalbrewing.api.drugs;

import com.github.md5sha256.experiementalbrewing.api.drugs.impl.DrugCooldownDataImpl;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForm;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DrugCooldownData {

    static @NotNull DrugCooldownData create() {
        return new DrugCooldownDataImpl();
    }

    void clear();

    void clear(@NotNull UUID player);

    void clear(@NotNull UUID player, @NotNull IDrug drug);

    void clear(@NotNull UUID player, @NotNull IDrugForm drugForm);

    boolean isBlocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm);

    void setBlocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm);

    void setUnblocked(@NotNull UUID player, @NotNull IDrug drug, @NotNull IDrugForm drugForm);
}
