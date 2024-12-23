package liedge.limatech.util.datagen;

import liedge.limacore.data.generation.LimaRecipeProvider;
import liedge.limacore.data.generation.recipe.LimaCustomRecipeBuilder;
import liedge.limacore.data.generation.recipe.LimaSimpleRecipeBuilder;
import liedge.limacore.lib.ModResources;
import liedge.limatech.LimaTech;
import liedge.limatech.LimaTechTags;
import liedge.limatech.item.EquipmentUpgradeItem;
import liedge.limatech.item.weapon.WeaponItem;
import liedge.limatech.recipe.*;
import liedge.limatech.registry.LimaTechDataComponents;
import liedge.limatech.upgradesystem.EquipmentUpgrade;
import liedge.limatech.upgradesystem.EquipmentUpgradeEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static liedge.limatech.registry.LimaTechBlocks.*;
import static liedge.limatech.registry.LimaTechEquipmentUpgrades.*;
import static liedge.limatech.registry.LimaTechItems.*;
import static net.minecraft.world.item.Items.*;
import static net.neoforged.neoforge.common.Tags.Items.DYES_LIME;

class RecipesGen extends LimaRecipeProvider
{
    RecipesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, LimaTech.RESOURCES);
    }

    @Override
    protected void buildRecipes(RecipeOutput output, HolderLookup.Provider registries)
    {
        //#region Crafting table recipes
        nineStorageRecipes(output, RAW_TITANIUM, RAW_TITANIUM_BLOCK);
        nineStorageRecipes(output, RAW_NIOBIUM, RAW_NIOBIUM_BLOCK);

        nuggetIngotBlockRecipes(output, "titanium", TITANIUM_NUGGET, TITANIUM_INGOT, TITANIUM_BLOCK);
        nuggetIngotBlockRecipes(output, "niobium", NIOBIUM_NUGGET, NIOBIUM_INGOT, NIOBIUM_BLOCK);
        nuggetIngotBlockRecipes(output, "slate_alloy", SLATE_ALLOY_NUGGET, SLATE_ALLOY_INGOT, SLATE_ALLOY_BLOCK);

        titaniumTool(output, TITANIUM_SWORD, "t", "t", "s");
        titaniumTool(output, TITANIUM_SHOVEL, "t", "s", "s");
        titaniumTool(output, TITANIUM_PICKAXE, "ttt", " s ", " s ");
        titaniumTool(output, TITANIUM_AXE, "tt", "ts", " s");
        titaniumTool(output, TITANIUM_HOE, "tt", " s", " s");
        shaped(TITANIUM_SHEARS).input('t', TITANIUM_INGOT).patterns(" t", "t ").save(output);

        shaped(COPPER_CIRCUIT).input('r', REDSTONE).input('m', COPPER_INGOT).input('t', TITANIUM_NUGGET).patterns("rrr", "mmm", "ttt").save(output);
        shaped(GOLD_CIRCUIT).input('r', REDSTONE).input('m', GOLD_INGOT).input('t', TITANIUM_NUGGET).patterns("rrr", "mmm", "ttt").save(output);
        shaped(NIOBIUM_CIRCUIT).input('r', REDSTONE).input('m', NIOBIUM_INGOT).input('t', TITANIUM_NUGGET).patterns("rrr", "mmm", "ttt").save(output);

        shaped(MACHINE_WRENCH).input('t', TITANIUM_INGOT).input('l', DYES_LIME).patterns("t t", " l ", " t ").save(output);

        shaped(TIERED_ENERGY_STORAGE_ARRAY).input('t', TITANIUM_INGOT).input('c', GOLD_CIRCUIT).input('l', DYES_LIME).input('b', COPPER_BLOCK).patterns("tlt", "cbc", "tlt").save(output);
        shaped(DIGITAL_FURNACE).input('t', TITANIUM_INGOT).input('c', COPPER_CIRCUIT).input('l', DYES_LIME).input('a', FURNACE).patterns("tlt", "cac", "ttt").save(output);
        shaped(GRINDER).input('t', TITANIUM_INGOT).input('c', COPPER_CIRCUIT).input('l', DYES_LIME).input('a', GRINDSTONE).patterns("tlt", "cac", "ttt").save(output);
        shaped(RECOMPOSER).input('t', TITANIUM_INGOT).input('c', GOLD_CIRCUIT).input('l', GLOW_BLOCKS.get(DyeColor.LIME)).patterns("ttt", "lcl", "tct").save(output);
        shaped(MATERIAL_FUSING_CHAMBER).input('t', TITANIUM_INGOT).input('c', COPPER_CIRCUIT).input('l', DYES_LIME).input('a', BLAST_FURNACE).patterns("tlt", "cac", "ttt").save(output);
        shaped(FABRICATOR).input('t', TITANIUM_INGOT).input('c', GOLD_CIRCUIT).input('l', DYES_LIME).input('a', CRAFTER).patterns("tlt", "cac", "ttt").save(output);
        shaped(EQUIPMENT_MOD_TABLE).input('t', TITANIUM_INGOT).input('a', ANVIL).input('l', DYES_LIME).patterns("ttt",  "lal", "ttt").save(output);

        GLOW_BLOCKS.forEach((color, deferredBlock) -> {
            String path = deferredBlock.getId().getPath();
            shaped(deferredBlock, 4).input('d', color.getTag()).input('g', GLOWSTONE).patterns("dg", "gd").save(output, path + "_a");
            shaped(deferredBlock, 8).input('d', color.getTag()).input('g', GLOW_INK_SAC).patterns("dg", "gd").save(output, path + "_b");
        });
        //#endregion

        // Smelting/cooking recipes
        oreSmeltBlast(output, "smelt_raw_titanium", RAW_TITANIUM, stackOf(TITANIUM_INGOT));
        oreSmeltBlast(output, "smelt_stone_titanium", TITANIUM_ORE, stackOf(TITANIUM_INGOT));
        oreSmeltBlast(output, "smelt_deepslate_titanium", DEEPSLATE_TITANIUM_ORE, stackOf(TITANIUM_INGOT));
        oreSmeltBlast(output, "smelt_raw_niobium", RAW_NIOBIUM, stackOf(NIOBIUM_INGOT));
        oreSmeltBlast(output, "smelt_niobium_ore", NIOBIUM_ORE, stackOf(NIOBIUM_INGOT));

        orePebblesCooking(COAL_ORE_PEBBLES, COAL, 2, output);
        orePebblesCooking(COPPER_ORE_PEBBLES, COPPER_INGOT, 1, output);
        orePebblesCooking(IRON_ORE_PEBBLES, IRON_INGOT, 1, output);
        orePebblesCooking(LAPIS_ORE_PEBBLES, LAPIS_LAZULI, 6, output);
        orePebblesCooking(REDSTONE_ORE_PEBBLES, REDSTONE, 8, output);
        orePebblesCooking(GOLD_ORE_PEBBLES, GOLD_INGOT, 1, output);
        orePebblesCooking(DIAMOND_ORE_PEBBLES, DIAMOND, 1, output);
        orePebblesCooking(EMERALD_ORE_PEBBLES, EMERALD, 1, output);
        orePebblesCooking(QUARTZ_ORE_PEBBLES, QUARTZ, 4, output);
        orePebblesCooking(NETHERITE_ORE_PEBBLES, NETHERITE_SCRAP, 1, output);
        orePebblesCooking(TITANIUM_ORE_PEBBLES, TITANIUM_INGOT, 1, output);
        orePebblesCooking(NIOBIUM_ORE_PEBBLES, NIOBIUM_INGOT, 1, output);

        // Grinding recipes
        grinding(stackOf(DEEPSLATE_POWDER))
                .input(LimaTechTags.Items.DEEPSLATE_GRINDABLES)
                .save(output, "grind_deepslate");

        orePebbleGrinding(COAL_ORE_PEBBLES, Tags.Items.ORES_COAL, null, "coal", output);
        orePebbleGrinding(COPPER_ORE_PEBBLES, Tags.Items.ORES_COPPER, Tags.Items.RAW_MATERIALS_COPPER, "copper", output);
        orePebbleGrinding(IRON_ORE_PEBBLES, Tags.Items.ORES_IRON, Tags.Items.RAW_MATERIALS_IRON, "iron", output);
        orePebbleGrinding(LAPIS_ORE_PEBBLES, Tags.Items.ORES_LAPIS, null, "lapis", output);
        orePebbleGrinding(REDSTONE_ORE_PEBBLES, Tags.Items.ORES_REDSTONE, null, "redstone", output);
        orePebbleGrinding(GOLD_ORE_PEBBLES, Tags.Items.ORES_GOLD, Tags.Items.RAW_MATERIALS_GOLD, "gold", output);
        orePebbleGrinding(DIAMOND_ORE_PEBBLES, Tags.Items.ORES_DIAMOND, null, "diamond", output);
        orePebbleGrinding(EMERALD_ORE_PEBBLES, Tags.Items.ORES_EMERALD, null, "emerald", output);
        orePebbleGrinding(QUARTZ_ORE_PEBBLES, Tags.Items.ORES_QUARTZ, null, "quartz", output);
        orePebbleGrinding(NETHERITE_ORE_PEBBLES, Tags.Items.ORES_NETHERITE_SCRAP, null, "netherite", output);
        orePebbleGrinding(TITANIUM_ORE_PEBBLES, LimaTechTags.Items.TITANIUM_ORES, LimaTechTags.Items.RAW_TITANIUM_MATERIALS, "titanium", output);
        orePebbleGrinding(NIOBIUM_ORE_PEBBLES, LimaTechTags.Items.NIOBIUM_ORES, LimaTechTags.Items.RAW_NIOBIUM_MATERIALS, "niobium", output);

        // Recomposing Recipes
        recomposing(stackOf(LIME_PIGMENT, 2))
                .input(ItemTags.LEAVES, 8)
                .save(output, "extract_dye_from_leaves");
        recomposing(stackOf(LIME_PIGMENT))
                .input(Tags.Items.SEEDS, 8)
                .save(output, "extract_dye_from_seeds");
        recomposing(stackOf(WHITE_PIGMENT, 12))
                .input(TITANIUM_INGOT)
                .save(output, "extract_titanium_white");

        // Material fusing recipes
        fusing(stackOf(SLATE_ALLOY_INGOT))
                .input(DEEPSLATE_POWDER, 4)
                .input(NETHERITE_INGOT)
                .save(output, "slate_alloy_from_netherite_ingot");
        fusing(stackOf(SLATE_ALLOY_INGOT))
                .input(DEEPSLATE_POWDER, 4)
                .input(NETHERITE_SCRAP, 2)
                .input(GOLD_INGOT)
                .save(output, "slate_alloy_from_netherite_alloying");
        fusing(stackOf(NETHERITE_INGOT))
                .input(NETHERITE_SCRAP, 4)
                .input(GOLD_INGOT)
                .save(output);

        // Fabricating recipes
        fabricating(ROCKET_TURRET, 1_000_000)
                .input(TARGETING_TECH_SALVAGE)
                .input(TITANIUM_INGOT, 32)
                .input(NIOBIUM_CIRCUIT, 4)
                .input(GOLD_CIRCUIT, 8)
                .group("turrets").save(output);
        weaponFabricating(SUBMACHINE_GUN, 400_000)
                .input(TITANIUM_INGOT, 16)
                .input(COPPER_CIRCUIT, 8)
                .input(GOLD_CIRCUIT, 4)
                .group("weapons").save(output);
        weaponFabricating(SHOTGUN, 1_000_000)
                .input(TITANIUM_INGOT, 32)
                .input(COPPER_CIRCUIT, 16)
                .input(GOLD_CIRCUIT, 8)
                .group("weapons").save(output);
        weaponFabricating(GRENADE_LAUNCHER, 20_000_000)
                .input(EXPLOSIVES_WEAPON_TECH_SALVAGE)
                .input(TITANIUM_INGOT, 32)
                .input(NIOBIUM_INGOT, 16)
                .input(GOLD_CIRCUIT, 16)
                .group("weapons").save(output);
        weaponFabricating(ROCKET_LAUNCHER, 30_000_000)
                .input(EXPLOSIVES_WEAPON_TECH_SALVAGE)
                .input(TITANIUM_INGOT, 48)
                .input(NIOBIUM_INGOT, 16)
                .input(GOLD_CIRCUIT, 16)
                .group("weapons").save(output);
        weaponFabricating(MAGNUM, 50_000_000)
                .input(TITANIUM_INGOT, 32)
                .input(NIOBIUM_INGOT, 24)
                .input(SLATE_ALLOY_INGOT, 4)
                .input(GOLD_CIRCUIT, 16)
                .input(NIOBIUM_CIRCUIT, 8)
                .group("weapons").save(output);

        upgradeFabricating(output, registries, "stealth_upgrades", UNIVERSAL_ANTI_VIBRATION, 500_000, builder -> builder
                .input(GOLD_CIRCUIT, 4)
                .input(TITANIUM_INGOT, 16)
                .input(ECHO_SHARD, 1)
                .input(ItemTags.WOOL, 16));
        upgradeFabricating(output, registries, "stealth_upgrades", UNIVERSAL_STEALTH_DAMAGE, 750_000, builder -> builder
                .input(GOLD_CIRCUIT, 6)
                .input(TITANIUM_INGOT, 16)
                .input(PHANTOM_MEMBRANE, 8)
                .input(DataComponentIngredient.of(false, DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.INVISIBILITY)).build(), POTION)));

        upgradeFabricating(output, registries, "looting_upgrades", LOOTING_ENCHANTMENT, 1, 125_000, builder -> builder
                .input(COPPER_CIRCUIT, 8)
                .input(GOLD_CIRCUIT, 2)
                .input(TITANIUM_INGOT, 8)
                .input(RABBIT_FOOT, 1)
                .input(LAPIS_LAZULI, 8));
        upgradeFabricating(output, registries, "looting_upgrades", LOOTING_ENCHANTMENT, 2, 250_000, builder -> builder
                .input(GOLD_CIRCUIT, 4)
                .input(EMERALD, 4)
                .input(DIAMOND, 4)
                .input(LAPIS_LAZULI, 32));
        upgradeFabricating(output, registries, "looting_upgrades", LOOTING_ENCHANTMENT, 3, 500_000, builder -> builder
                .input(ZOMBIE_HEAD)
                .input(CREEPER_HEAD)
                .input(SKELETON_SKULL)
                .input(WITHER_SKELETON_SKULL)
                .input(LAPIS_LAZULI, 48));
        upgradeFabricating(output, registries, "looting_upgrades", LOOTING_ENCHANTMENT, 4, 1_000_000, builder -> builder
                .input(NIOBIUM_CIRCUIT, 4)
                .input(EMERALD_BLOCK, 4)
                .input(NETHER_STAR, 1)
                .input(LAPIS_BLOCK, 16));
        upgradeFabricating(output, registries, "looting_upgrades", LOOTING_ENCHANTMENT, 5, 10_000_000, builder -> builder
                .input(NIOBIUM_CIRCUIT, 8)
                .input(SLATE_ALLOY_INGOT, 16)
                .input(ECHO_SHARD, 2)
                .input(DRAGON_HEAD, 1)
                .input(NETHER_STAR, 4)
                .input(LAPIS_BLOCK, 64));

        upgradeFabricating(output, registries, "ammo_scavenger_upgrades", AMMO_SCAVENGER_ENCHANTMENT, 1, 300_000, builder -> builder
                .input(GOLD_CIRCUIT, 2)
                .input(TITANIUM_INGOT, 8)
                .input(GUNPOWDER, 8));
        upgradeFabricating(output, registries, "ammo_scavenger_upgrades", AMMO_SCAVENGER_ENCHANTMENT, 2, 600_000, builder -> builder
                .input(GOLD_CIRCUIT, 4)
                .input(TITANIUM_INGOT, 12)
                .input(GUNPOWDER, 12)
                .input(LAPIS_LAZULI, 16));
        upgradeFabricating(output, registries, "ammo_scavenger_upgrades", AMMO_SCAVENGER_ENCHANTMENT, 3, 900_000, builder -> builder
                .input(GOLD_CIRCUIT, 6)
                .input(AUTO_AMMO_CANISTER, 2)
                .input(TITANIUM_INGOT, 16));
        upgradeFabricating(output, registries, "ammo_scavenger_upgrades", AMMO_SCAVENGER_ENCHANTMENT, 4, 1_200_000, builder -> builder
                .input(GOLD_CIRCUIT, 8)
                .input(NIOBIUM_CIRCUIT, 1)
                .input(AUTO_AMMO_CANISTER, 4)
                .input(SPECIALIST_AMMO_CANISTER, 1));
        upgradeFabricating(output, registries, "ammo_scavenger_upgrades", AMMO_SCAVENGER_ENCHANTMENT, 5, 1_500_000, builder -> builder
                .input(NIOBIUM_CIRCUIT, 2)
                .input(SLATE_ALLOY_INGOT, 2)
                .input(AUTO_AMMO_CANISTER, 6)
                .input(SPECIALIST_AMMO_CANISTER, 2)
                .input(EXPLOSIVES_AMMO_CANISTER, 1));

        upgradeFabricating(output, registries, "grenade_cores", FLAME_GRENADE_CORE, 250_000, builder -> builder
                .input(COPPER_CIRCUIT, 16)
                .input(TITANIUM_INGOT, 8)
                .input(FIRE_CHARGE, 21));
        upgradeFabricating(output, registries, "grenade_cores", FREEZE_GRENADE_CORE, 250_000, builder -> builder
                .input(COPPER_CIRCUIT, 16)
                .input(TITANIUM_INGOT, 8)
                .input(SNOW_BLOCK, 16)
                .input(DataComponentIngredient.of(false, DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_SLOWNESS)).build(), POTION)));
        upgradeFabricating(output, registries, "grenade_cores", ELECTRIC_GRENADE_CORE, 500_000, builder -> builder
                .input(COPPER_CIRCUIT, 8)
                .input(GOLD_CIRCUIT, 8)
                .input(TITANIUM_INGOT, 8)
                .input(LIGHTNING_ROD, 4)
                .input(BREEZE_ROD, 1));
        upgradeFabricating(output, registries, "grenade_cores", ACID_GRENADE_CORE, 1_000_000, builder -> builder
                .input(GOLD_CIRCUIT, 16)
                .input(NIOBIUM_CIRCUIT, 4)
                .input(TITANIUM_INGOT, 32)
                .input(DataComponentIngredient.of(false, DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_POISON)).build(), POTION)));
        upgradeFabricating(output, registries, "grenade_cores", NEURO_GRENADE_CORE, 5_000_000, builder -> builder
                .input(GOLD_CIRCUIT, 16)
                .input(NIOBIUM_CIRCUIT, 16)
                .input(TITANIUM_INGOT, 64)
                .input(NETHER_STAR)
                .input(DataComponentIngredient.of(false, DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS, new PotionContents(Potions.LONG_WEAKNESS)).build(), POTION)));
    }

    private void orePebblesCooking(ItemLike orePebble, ItemLike resultItem, int resultCount, RecipeOutput output)
    {
        String name = getItemName(orePebble);
        smelting(stackOf(resultItem, resultCount)).input(orePebble).xp(0.5f).save(output, "smelt_" + name);
        blasting(stackOf(resultItem, resultCount)).input(orePebble).xp(0.5f).save(output, "blast_" + name);
    }

    private LimaSimpleRecipeBuilder<GrindingRecipe, ?> grinding(ItemStack result)
    {
        return LimaSimpleRecipeBuilder.simpleBuilder(modResources, result, GrindingRecipe::new);
    }

    private void orePebbleGrinding(ItemLike orePebble, TagKey<Item> oreTag, @Nullable TagKey<Item> rawOreTag, String name, RecipeOutput output)
    {
        grinding(stackOf(orePebble, 3)).input(oreTag).save(output, "grind_" + name + "_ores");
        if (rawOreTag != null) grinding(stackOf(orePebble, 2)).input(rawOreTag).save(output, "grind_raw_" + name + "_materials");
    }

    private LimaSimpleRecipeBuilder<RecomposingRecipe, ?> recomposing(ItemStack result)
    {
        return LimaSimpleRecipeBuilder.simpleBuilder(modResources, result, RecomposingRecipe::new);
    }

    private LimaSimpleRecipeBuilder<MaterialFusingRecipe, ?> fusing(ItemStack result)
    {
        return LimaSimpleRecipeBuilder.simpleBuilder(modResources, result, MaterialFusingRecipe::new);
    }

    private FabricatingBuilder fabricating(ItemLike result, int energyRequired)
    {
        return fabricating(stackOf(result), energyRequired);
    }

    private FabricatingBuilder fabricating(ItemStack result, int energyRequired)
    {
        return new FabricatingBuilder(modResources, result, energyRequired);
    }

    private WeaponFabricatingBuilder weaponFabricating(Supplier<? extends WeaponItem> itemSupplier, int energyRequired)
    {
        return new WeaponFabricatingBuilder(modResources, itemSupplier.get(), energyRequired);
    }

    private void upgradeFabricating(RecipeOutput output, HolderLookup.Provider registries, String group, ResourceKey<EquipmentUpgrade> upgradeKey, int energyRequired, UnaryOperator<FabricatingBuilder> op)
    {
        op.apply(fabricating(EquipmentUpgradeItem.createStack(registries, upgradeKey), energyRequired)).group(group).save(output, "equipment_upgrades/" + upgradeKey.location().getPath());
    }

    private void upgradeFabricating(RecipeOutput output, HolderLookup.Provider registries, String group, ResourceKey<EquipmentUpgrade> upgradeKey, int upgradeRank, int energyRequired, UnaryOperator<FabricatingBuilder> op)
    {
        FabricatingBuilder builder = fabricating(EquipmentUpgradeItem.createStack(registries, upgradeKey, upgradeRank), energyRequired);

        if (upgradeRank > 1)
        {
            Holder<EquipmentUpgrade> upgradeHolder = registries.holderOrThrow(upgradeKey);
            Ingredient previousRankUpgrade = DataComponentIngredient.of(true, LimaTechDataComponents.UPGRADE_ITEM_DATA, new EquipmentUpgradeEntry(upgradeHolder, upgradeRank - 1), EQUIPMENT_UPGRADE_ITEM);
            builder.input(previousRankUpgrade);
        }

        op.apply(builder).group(group).save(output, "equipment_upgrades/" + upgradeKey.location().getPath() + "_" + upgradeRank);
    }

    private void titaniumTool(RecipeOutput output, ItemLike tool, String p1, String p2, String p3)
    {
        shaped(stackOf(tool)).input('t', TITANIUM_INGOT).input('s', Tags.Items.RODS_WOODEN).patterns(p1, p2, p3).save(output);
    }

    private static class FabricatingBuilder extends LimaSimpleRecipeBuilder<FabricatingRecipe, FabricatingBuilder>
    {
        private final int energyRequired;

        FabricatingBuilder(ModResources resources, ItemStack result, int energyRequired)
        {
            super(resources, result);
            this.energyRequired = energyRequired;
        }

        @Override
        protected String defaultFolderPrefix(FabricatingRecipe recipe, ResourceLocation recipeId)
        {
            return makeTypePrefix(recipe);
        }

        @Override
        protected FabricatingRecipe buildRecipe()
        {
            return new FabricatingRecipe(NonNullList.copyOf(ingredients), resultItem, energyRequired, getGroupOrBlank());
        }
    }

    private static class WeaponFabricatingBuilder extends LimaCustomRecipeBuilder<WeaponFabricatingRecipe, WeaponFabricatingBuilder>
    {
        private final WeaponItem weaponItem;
        private final int energyRequired;

        protected WeaponFabricatingBuilder(ModResources modResources, WeaponItem weaponItem, int energyRequired)
        {
            super(modResources);
            this.weaponItem = weaponItem;
            this.energyRequired = energyRequired;
        }

        @Override
        protected String defaultFolderPrefix(WeaponFabricatingRecipe recipe, ResourceLocation recipeId)
        {
            return makeTypePrefix(recipe) + "/weapons/";
        }

        @Override
        protected void validate(ResourceLocation id) { }

        @Override
        protected WeaponFabricatingRecipe buildRecipe()
        {
            return new WeaponFabricatingRecipe(buildIngredients(), energyRequired, weaponItem);
        }

        @Override
        protected String getDefaultRecipeName()
        {
            return getItemName(weaponItem);
        }
    }
}