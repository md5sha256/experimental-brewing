package com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.lsd;

import com.github.md5sha256.experiementalbrewing.api.drugs.ISynthetic;
import com.github.md5sha256.experiementalbrewing.api.util.AbstractDrug;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.lsd.components.Chloroform;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.lsd.components.Ethanol;
import com.github.md5sha256.experimentalbrewing.implementation.drugs.synthetics.lsd.components.LysergicAcid;
import com.github.md5sha256.experimentalbrewing.util.AdventureUtils;
import com.github.md5sha256.experimentalbrewing.util.Utils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
public final class DrugLSD extends AbstractDrug implements ISynthetic {

    private final Recipe recipe;

    @Inject
    DrugLSD(@NotNull Plugin plugin,
            @NotNull ItemFactory itemFactory,
            @NotNull LysergicAcid lysergicAcid,
            @NotNull Ethanol ethanol,
            @NotNull Chloroform chloroform
    ) {
        super(itemFactory,
              Utils.internalKey("lsd"),
              "LSD",
              Material.PAPER,
              "addictiveexperience.consumelsd");
        this.recipe = createRecipe(plugin, lysergicAcid, ethanol, chloroform);
    }

    private Recipe createRecipe(@NotNull Plugin plugin,
                                @NotNull LysergicAcid lysergicAcid,
                                @NotNull Ethanol ethanol,
                                @NotNull Chloroform chloroform
    ) {
        final NamespacedKey key = new NamespacedKey(plugin, "lsd");
        final ShapelessRecipe recipe = new ShapelessRecipe(key, this.asItem());
        recipe.addIngredient(new RecipeChoice.ExactChoice(lysergicAcid.asItem()));
        recipe.addIngredient(new RecipeChoice.ExactChoice(ethanol.asItem()));
        recipe.addIngredient(new RecipeChoice.ExactChoice(chloroform.asItem()));
        return recipe;
    }

    protected final @NotNull ItemMeta meta() {
        final ItemMeta meta = this.itemFactory.getItemMeta(Material.PAPER);
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        AdventureUtils.setDisplayName(meta, Component.text("LSD", NamedTextColor.AQUA));
        final List<Component> lore = Arrays.asList(
                Component.text("Lysergic acid diethylamide, also known", NamedTextColor.WHITE),
                Component.text("as acid, is a hallucinogenic drug.", NamedTextColor.WHITE),
                Component.text("Effects typically include altered", NamedTextColor.WHITE),
                Component.text("thoughts, feelings, and", NamedTextColor.WHITE),
                Component.text("awareness of one's surroundings.", NamedTextColor.WHITE),
                Component.text("Many users see or hear things", NamedTextColor.WHITE),
                Component.text("that do not exist", NamedTextColor.WHITE),
                Component.empty()
        );
        AdventureUtils.setLore(meta, lore);
        return meta;
    }

    @Override
    public @NotNull Optional<@NotNull Recipe> recipe() {
        return Optional.of(this.recipe);
    }

}
