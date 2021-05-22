package com.github.md5sha256.experimentalbrewing.implementation.drugs.organics.marijuana.components;

import com.github.md5sha256.experiementalbrewing.api.drugs.IDrugComponent;
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
public final class PlantMarijuana extends AbstractDrugComponent implements IDrugComponent {

    @Inject
    PlantMarijuana(@NotNull ItemFactory itemFactory) {
        super(itemFactory,
              Utils.internalKey("plant_marijuana"),
              "Marijuana Plant",
              Material.LARGE_FERN);
    }

    protected final @NotNull ItemMeta meta() {
        ItemMeta meta = this.itemFactory.getItemMeta(Material.LARGE_FERN);
        final Component displayName = Component.text("Marijuana Plant", NamedTextColor.GREEN);
        AdventureUtils.setDisplayName(meta, displayName);
        final List<Component> lore = Arrays.asList(
                Component.text("Used to harvest", NamedTextColor.WHITE),
                Component.text("marijuana", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.empty();
    }
}
