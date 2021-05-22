package com.github.md5sha256.experimentalbrewing.implementation.forms;

import com.github.md5sha256.experiementalbrewing.api.drugs.IDrug;
import com.github.md5sha256.experiementalbrewing.api.forms.IDrugForm;
import com.github.md5sha256.experiementalbrewing.api.util.AbstractDrugForm;
import com.github.md5sha256.experimentalbrewing.util.Utils;
import com.google.inject.Singleton;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Singleton
public final class FormDefault extends AbstractDrugForm implements IDrugForm {

    FormDefault(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("default"), "Default Form");
    }

    @Override
    public @NotNull Optional<@NotNull ItemStack> asItem() {
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack asItem(@NotNull IDrug drug) {
        return drug.asItem();
    }

    @Override
    public @NotNull ItemMeta asMeta(@NotNull IDrug drug) {
        return drug.asMeta();
    }

    @Override
    public @NotNull Optional<@NotNull ItemMeta> asMeta() {
        return Optional.empty();
    }

}
