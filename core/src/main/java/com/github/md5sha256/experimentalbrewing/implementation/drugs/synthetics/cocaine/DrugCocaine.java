package com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.cocaine;

import com.github.md5sha256.experiementalbrewing.api.drugs.ISynthetic;
import com.github.md5sha256.experiementalbrewing.api.util.AbstractDrug;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.cocaine.components.LeafCocaine;
import com.github.md5sha256.experimentalbrewing.util.AdventureUtils;
import com.github.md5sha256.experimentalbrewing.util.Utils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class DrugCocaine extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;

    @Inject
    DrugCocaine(@NotNull Plugin plugin,
                @NotNull ItemFactory itemFactory,
                @NotNull LeafCocaine leafCocaine
    ) {
        super(itemFactory,
              Utils.internalKey("cocaine"),
              "Cocaine",
              Material.SUGAR,
              "addictiveexperience.consumecocaine");
        this.recipe = createRecipe(plugin, leafCocaine);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin, @NotNull LeafCocaine leafCocaine) {
        final NamespacedKey key = new NamespacedKey(plugin, "Cocaine");
        final ShapedRecipe recipe = new ShapedRecipe(key, asItem(1));
        recipe.shape(" $ ", "$ $", " $ ");
        recipe.setIngredient('$', leafCocaine.asItem(1));
        return recipe;
    }


    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.SUGAR);
        final Component displayName = Component.text("Cocaine", NamedTextColor.WHITE);
        final List<Component> lore = Arrays.asList(
                Component.text("Cocaine, also known as coke is a", NamedTextColor.WHITE),
                Component.text("strong stimulant most frequently", NamedTextColor.WHITE),
                Component.text("used as a recreational drug.", NamedTextColor.WHITE)
        );
        AdventureUtils.setDisplayName(meta, displayName);
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

}
