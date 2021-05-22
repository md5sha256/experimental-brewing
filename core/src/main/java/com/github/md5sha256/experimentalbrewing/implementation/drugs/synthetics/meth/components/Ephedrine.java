package com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.meth.components;

import com.github.md5sha256.experiementalbrewing.api.util.AbstractDrugComponent;
import com.github.md5sha256.experimentalbrewing.util.AdventureUtils;
import com.github.md5sha256.experimentalbrewing.util.Utils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class Ephedrine extends AbstractDrugComponent {

    @Inject
    Ephedrine(@NotNull ItemFactory itemFactory) {
        super(itemFactory, Utils.internalKey("ephedrine"), "Ephedrine", Material.BLUE_DYE);
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.BLUE_DYE);
        AdventureUtils.setDisplayName(meta, Component.text("Ephedrine", NamedTextColor.BLUE));
        final List<Component> lore = Arrays.asList(
                Component.text("Used for the production", NamedTextColor.WHITE),
                Component.text("of Methamphetamine!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<Recipe> recipe() {
        return Optional.empty();
    }
}
