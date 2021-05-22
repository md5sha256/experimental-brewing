package com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.meth;

import com.github.md5sha256.experiementalbrewing.api.drugs.ISynthetic;
import com.github.md5sha256.experiementalbrewing.api.util.AbstractDrug;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.meth.components.Ephedrine;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.meth.components.HydrochloricAcid;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.meth.components.Iodine;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.meth.components.Phosphorus;
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
public final class DrugMethamphetamine extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;

    @Inject
    DrugMethamphetamine(@NotNull Plugin plugin,
                        @NotNull ItemFactory itemFactory,
                        @NotNull HydrochloricAcid hcl,
                        @NotNull Phosphorus phosphorus,
                        @NotNull Iodine iodine,
                        @NotNull Ephedrine ephedrine
    ) {
        super(itemFactory,
              Utils.internalKey("meth"),
              "Meth",
              Material.CYAN_DYE,
              "addictiveexperience.consumemeth");
        this.recipe = createRecipe(plugin, hcl, phosphorus, iodine, ephedrine);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin,
                                         @NotNull HydrochloricAcid hcl,
                                         @NotNull Phosphorus phosphorus,
                                         @NotNull Iodine iodine,
                                         @NotNull Ephedrine ephedrine
    ) {
        final NamespacedKey key = new NamespacedKey(plugin, "Meth");
        final ShapedRecipe recipe = new ShapedRecipe(key, this.asItem(1));
        recipe.shape(" % ", "$ £", " * ");
        recipe.setIngredient('%', hcl.asItem(1));
        recipe.setIngredient('$', phosphorus.asItem(1));
        recipe.setIngredient('£', iodine.asItem(1));
        recipe.setIngredient('*', ephedrine.asItem(1));
        return recipe;
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.CYAN_DYE);
        AdventureUtils.setDisplayName(meta, Component.text("Methamphetamine", NamedTextColor.BLUE));
        final List<Component> lore = Arrays.asList(
                Component.text("Methamphetamine is a strong", NamedTextColor.WHITE),
                Component.text("and highly addictive drug that", NamedTextColor.WHITE),
                Component.text("affects the central nervous system", NamedTextColor.WHITE)
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

}
