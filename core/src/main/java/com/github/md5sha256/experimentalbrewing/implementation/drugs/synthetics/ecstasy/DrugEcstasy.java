package com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.ecstasy;

import com.github.md5sha256.experiementalbrewing.api.util.AbstractDrug;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.ecstasy.components.Mercury;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.ecstasy.components.MethylChloride;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.ecstasy.components.PlantSafrole;
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
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public final class DrugEcstasy extends AbstractDrug {

    private final Recipe recipe;

    @Inject
    DrugEcstasy(@NotNull Plugin plugin,
                @NotNull ItemFactory itemFactory,
                @NotNull PlantSafrole safrole,
                @NotNull MethylChloride mcl,
                @NotNull Mercury mercury
    ) {
        super(itemFactory,
              Utils.internalKey("ecstasy"),
              "Ecstasy", Material.IRON_NUGGET, "addictiveexperience.consumeecstasy");
        this.recipe = createRecipe(plugin, safrole, mcl, mercury);
    }

    private @NotNull Recipe createRecipe(@NotNull Plugin plugin,
                                         @NotNull PlantSafrole safrole,
                                         @NotNull MethylChloride mcl,
                                         @NotNull Mercury mercury
    ) {
        final NamespacedKey key = new NamespacedKey(plugin, "ecstasy");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, this.asItem());
        recipe.addIngredient(new RecipeChoice.ExactChoice(safrole.asItem()));
        recipe.addIngredient(new RecipeChoice.ExactChoice(mcl.asItem()));
        recipe.addIngredient(new RecipeChoice.ExactChoice(mercury.asItem()));
        recipe.addIngredient(Material.IRON_INGOT);
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
