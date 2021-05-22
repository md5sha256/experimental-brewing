package com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.heroin.components;

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
public final class PlantOpium extends AbstractDrugComponent {

    @Inject
    PlantOpium(@NotNull ItemFactory itemFactory) {
        super(itemFactory,
              Utils.internalKey("plant_opium"),
              "Opium Poppy",
              Material.ALLIUM);
    }

    @Override
    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.ALLIUM);
        final Component displayName = Component.text("Opium Poppy", NamedTextColor.RED);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Used to plant an Opium Poppy", NamedTextColor.WHITE),
                Component.text("which you can harvest to get", NamedTextColor.WHITE),
                Component.text("Opium Poppy Seed Pods!", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }
}
