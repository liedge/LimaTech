package liedge.limatech.util.datagen;

import liedge.limacore.data.generation.LimaDatagenBootstrapBuilder;
import liedge.limatech.LimaTechTags;
import liedge.limatech.lib.LimaTechDeathMessageTypes;
import liedge.limatech.lib.weapons.GrenadeType;
import liedge.limatech.lib.weapons.WeaponAmmoSource;
import liedge.limatech.lib.weapons.WeaponAttribute;
import liedge.limatech.lib.weapons.WeaponContextCalculation;
import liedge.limatech.registry.LimaTechBlocks;
import liedge.limatech.registry.LimaTechItems;
import liedge.limatech.registry.LimaTechRegistries;
import liedge.limatech.upgradesystem.EquipmentUpgrade;
import liedge.limatech.upgradesystem.effect.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers.AddFeaturesBiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static liedge.limatech.LimaTech.RESOURCES;
import static liedge.limatech.registry.LimaTechDamageTypes.*;
import static liedge.limatech.registry.LimaTechEnchantments.AMMO_SCAVENGER;
import static liedge.limatech.registry.LimaTechEnchantments.RAZOR;
import static liedge.limatech.registry.LimaTechEquipmentUpgrades.*;
import static liedge.limatech.registry.LimaTechWorldGen.*;

class DataRegistriesEntries extends LimaDatagenBootstrapBuilder
{
    @Override
    protected void buildDataRegistryEntries(RegistrySetBuilder builder)
    {
        builder.add(Registries.DAMAGE_TYPE, this::createDamageTypes);
        builder.add(Registries.CONFIGURED_FEATURE, this::createConfiguredFeatures);
        builder.add(Registries.PLACED_FEATURE, this::createPlacedFeatures);
        builder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, this::createBiomeModifiers);
        builder.add(Registries.ENCHANTMENT, this::createEnchantments);

        builder.add(LimaTechRegistries.EQUIPMENT_UPGRADES_KEY, this::createUpgradeEffects);
    }

    private void createDamageTypes(BootstrapContext<DamageType> ctx)
    {
        DeathMessageType weaponMsgType = LimaTechDeathMessageTypes.WEAPON_DEATH_MESSAGE_TYPE.getValue();
        DeathMessageType traceableMsgType = LimaTechDeathMessageTypes.TRACEABLE_PROJECTILE_MESSAGE_TYPE.getValue();

        registerDamageType(ctx, LIGHTFRAG, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);
        registerDamageType(ctx, MAGNUM_LIGHTFRAG, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);
        registerDamageType(ctx, EXPLOSIVE_GRENADE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);
        registerDamageType(ctx, FLAME_GRENADE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.BURNING, weaponMsgType);
        registerDamageType(ctx, FREEZE_GRENADE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.FREEZING, weaponMsgType);
        registerDamageType(ctx, ELECTRIC_GRENADE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);
        registerDamageType(ctx, ACID_GRENADE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);
        registerDamageType(ctx, NEURO_GRENADE, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);
        registerDamageType(ctx, ROCKET_LAUNCHER, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, weaponMsgType);

        registerDamageType(ctx, STICKY_FLAME, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, traceableMsgType);
        registerDamageType(ctx, TURRET_ROCKET, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT, traceableMsgType);
    }

    private void createConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx)
    {
        ConfiguredFeature<?, ?> titaniumOre = new ConfiguredFeature<>(Feature.ORE, oreConfig(10, tagMatchOreTarget(BlockTags.STONE_ORE_REPLACEABLES, LimaTechBlocks.TITANIUM_ORE), tagMatchOreTarget(BlockTags.DEEPSLATE_ORE_REPLACEABLES, LimaTechBlocks.DEEPSLATE_TITANIUM_ORE)));
        ConfiguredFeature<?, ?> niobiumOre = new ConfiguredFeature<>(Feature.ORE, oreConfig(3, singleBlockOreTarget(Blocks.END_STONE, LimaTechBlocks.NIOBIUM_ORE)));

        ctx.register(TITANIUM_ORE_CONFIG, titaniumOre);
        ctx.register(NIOBIUM_ORE_CONFIG, niobiumOre);
    }

    private void createPlacedFeatures(BootstrapContext<PlacedFeature> ctx)
    {
        HolderGetter<ConfiguredFeature<?, ?>> configs = ctx.lookup(Registries.CONFIGURED_FEATURE);

        PlacedFeature titaniumOre = orePlacement(configs.getOrThrow(TITANIUM_ORE_CONFIG), 10,
                HeightRangePlacement.triangle(VerticalAnchor.BOTTOM, VerticalAnchor.absolute(60)));

        PlacedFeature niobiumOre = orePlacement(configs.getOrThrow(NIOBIUM_ORE_CONFIG), 2, HeightRangePlacement.uniform(VerticalAnchor.BOTTOM, VerticalAnchor.TOP));


        ctx.register(TITANIUM_ORE_PLACEMENT, titaniumOre);
        ctx.register(NIOBIUM_ORE_PLACEMENT, niobiumOre);
    }

    private void createBiomeModifiers(BootstrapContext<BiomeModifier> ctx)
    {
        HolderGetter<PlacedFeature> placements = ctx.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = ctx.lookup(Registries.BIOME);

        BiomeModifier titaniumOre = new AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placements.getOrThrow(TITANIUM_ORE_PLACEMENT)),
                GenerationStep.Decoration.UNDERGROUND_ORES);

        BiomeModifier niobiumOre = new AddFeaturesBiomeModifier(
                HolderSet.direct(biomes::getOrThrow, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.END_BARRENS, Biomes.SMALL_END_ISLANDS),
                HolderSet.direct(placements.getOrThrow(NIOBIUM_ORE_PLACEMENT)),
                GenerationStep.Decoration.UNDERGROUND_ORES);

        ctx.register(TITANIUM_ORE_BIOMES, titaniumOre);
        ctx.register(NIOBIUM_ORE_BIOMES, niobiumOre);
    }

    private void createEnchantments(BootstrapContext<Enchantment> ctx)
    {
        HolderGetter<Item> items = ctx.lookup(Registries.ITEM);

        Enchantment.Builder razor = Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                1,
                3,
                Enchantment.dynamicCost(10, 9),
                Enchantment.dynamicCost(60, 9),
                3,
                EquipmentSlotGroup.MAINHAND));

        Enchantment.Builder ammoScavenger = Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                1,
                3,
                Enchantment.dynamicCost(10, 9),
                Enchantment.dynamicCost(60, 9),
                2,
                EquipmentSlotGroup.MAINHAND));

        registerEnchantment(ctx, RAZOR, razor);
        registerEnchantment(ctx, AMMO_SCAVENGER, ammoScavenger);
    }

    private void createUpgradeEffects(BootstrapContext<EquipmentUpgrade> ctx)
    {
        HolderGetter<Item> items = ctx.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = ctx.lookup(Registries.ENCHANTMENT);
        HolderGetter<EquipmentUpgrade> holders = ctx.lookup(LimaTechRegistries.EQUIPMENT_UPGRADES_KEY);

        // Weapon-specific upgrades
        EquipmentUpgrade.builder(SMG_BUILT_IN)
                .supports(HolderSet.direct(LimaTechItems.SUBMACHINE_GUN))
                .withEffect(NoVibrationUpgradeEffect.NO_VIBRATIONS_EFFECT)
                .withEffect(NoAngerUpgradeEffect.NO_ANGER_EFFECT)
                .withEffect(new KnockbackModifierUpgradeEffect(false, WeaponContextCalculation.overrideBase(0f)))
                .withEffect(new BypassArmorUpgradeEffect(LevelBasedValue.constant(0)))
                .buildAndRegister(ctx);

        ItemAttributeModifiersUpgradeEffect.Builder shotgunAttributes = ItemAttributeModifiersUpgradeEffect.builder()
                .add(Attributes.MOVEMENT_SPEED, RESOURCES.location("shotgun_speed_boost"), LevelBasedValue.constant(0.25f), AttributeModifier.Operation.ADD_MULTIPLIED_BASE, EquipmentSlotGroup.MAINHAND)
                .add(Attributes.STEP_HEIGHT, RESOURCES.location("shotgun_step_boost"), LevelBasedValue.constant(1), AttributeModifier.Operation.ADD_VALUE, EquipmentSlotGroup.MAINHAND);

        EquipmentUpgrade.builder(SHOTGUN_BUILT_IN)
                .supports(LimaTechItems.SHOTGUN)
                .withEffect(shotgunAttributes.build())
                .withEffect(new BypassArmorUpgradeEffect(LevelBasedValue.constant(0.15f)))
                .buildAndRegister(ctx);

        EquipmentUpgrade.builder(HIGH_IMPACT_ROUNDS)
                .supports(LimaTechItems.SHOTGUN, LimaTechItems.MAGNUM)
                .withEffect(new KnockbackModifierUpgradeEffect(true, WeaponContextCalculation.flatAddition(1.5f)))
                .effectIcon("armor_bypass")
                .buildAndRegister(ctx);

        EquipmentUpgrade.builder(MAGNUM_SCALING_ROUNDS)
                .supports(LimaTechItems.MAGNUM)
                .withEffect(WeaponAttributesUpgradeEffect.modifyAttribute(WeaponAttribute.DAMAGE, WeaponContextCalculation.multiplyByAttribute(0.20f, Attributes.MAX_HEALTH)))
                .withEffect(new BypassArmorUpgradeEffect(LevelBasedValue.constant(1f)))
                .buildAndRegister(ctx);

        EquipmentUpgrade.builder(GRENADE_LAUNCHER_PROJECTILE_SPEED)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .setMaxRank(2)
                .withEffect(WeaponAttributesUpgradeEffect.modifyAttribute(WeaponAttribute.PROJECTILE_SPEED, WeaponContextCalculation.flatAddition(LevelBasedValue.perLevel(0.5f))))
                .effectIcon("grenade_speed_boost")
                .buildAndRegister(ctx);

        // Universal upgrades
        EquipmentUpgrade.builder(UNIVERSAL_ANTI_VIBRATION)
                .supportsAllWeapons(items)
                .withEffect(NoVibrationUpgradeEffect.NO_VIBRATIONS_EFFECT)
                .effectIcon("no_vibration")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(UNIVERSAL_STEALTH_DAMAGE)
                .supportsAllWeapons(items)
                .withEffect(NoAngerUpgradeEffect.NO_ANGER_EFFECT)
                .effectIcon("stealth_damage")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(UNIVERSAL_ENERGY_AMMO)
                .supportsAllWeapons(items)
                .exclusiveWith(holders, LimaTechTags.EquipmentUpgrades.AMMO_SOURCE_MODIFIERS)
                .withEffect(new SetAmmoSourceUpgradeEffect(WeaponAmmoSource.COMMON_ENERGY_UNIT))
                .effectIcon("energy_ammo")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(UNIVERSAL_INFINITE_AMMO)
                .supportsAllWeapons(items)
                .exclusiveWith(holders, LimaTechTags.EquipmentUpgrades.AMMO_SOURCE_MODIFIERS)
                .withEffect(new SetAmmoSourceUpgradeEffect(WeaponAmmoSource.INFINITE))
                .effectIcon("infinite_ammo")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(UNIVERSAL_ARMOR_PIERCE)
                .supportsAllWeapons(items)
                .setMaxRank(3)
                .withEffect(new BypassArmorUpgradeEffect(LevelBasedValue.perLevel(0.1f)))
                .effectIcon("armor_bypass")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(UNIVERSAL_SHIELD_REGEN)
                .supportsAllWeapons(items)
                .setMaxRank(3)
                .withEffect(new BubbleShieldUpgradeEffect(LevelBasedValue.constant(4), LevelBasedValue.perLevel(10)))
                .effectIcon("shield_regen")
                .buildAndRegister(ctx);

        // Enchantments
        EquipmentUpgrade.builder(LOOTING_ENCHANTMENT)
                .supportsAllWeapons(items)
                .setMaxRank(5)
                .withEffect(new EnchantmentLevelUpgradeEffect(enchantments.getOrThrow(Enchantments.LOOTING)))
                .effectIcon("looting_enchant")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(AMMO_SCAVENGER_ENCHANTMENT)
                .supportsAllWeapons(items)
                .setMaxRank(5)
                .withEffect(new EnchantmentLevelUpgradeEffect(enchantments.getOrThrow(AMMO_SCAVENGER)))
                .effectIcon("ammo_scavenger")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(RAZOR_ENCHANTMENT)
                .supportsAllWeapons(items)
                .setMaxRank(5)
                .withEffect(new EnchantmentLevelUpgradeEffect(enchantments.getOrThrow(RAZOR)))
                .effectIcon("razor_enchant")
                .buildAndRegister(ctx);

        // Hanabi grenade cores
        EquipmentUpgrade.builder(FLAME_GRENADE_CORE)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .withEffect(GrenadeTypeSelectionUpgradeEffect.unlockSingle(GrenadeType.FLAME))
                .effectIcon("flame_grenade_core")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(FREEZE_GRENADE_CORE)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .withEffect(GrenadeTypeSelectionUpgradeEffect.unlockMultiple(GrenadeType.FREEZE))
                .effectIcon("freeze_grenade_core")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(ELECTRIC_GRENADE_CORE)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .withEffect(GrenadeTypeSelectionUpgradeEffect.unlockSingle(GrenadeType.ELECTRIC))
                .effectIcon("electric_grenade_core")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(ACID_GRENADE_CORE)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .withEffect(GrenadeTypeSelectionUpgradeEffect.unlockSingle(GrenadeType.ACID))
                .effectIcon("acid_grenade_core")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(NEURO_GRENADE_CORE)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .withEffect(GrenadeTypeSelectionUpgradeEffect.unlockSingle(GrenadeType.NEURO))
                .effectIcon("neuro_grenade_core")
                .buildAndRegister(ctx);
        EquipmentUpgrade.builder(OMNI_GRENADE_CORE)
                .supports(LimaTechItems.GRENADE_LAUNCHER)
                .withEffect(GrenadeTypeSelectionUpgradeEffect.unlockMultiple(GrenadeType.FLAME, GrenadeType.FREEZE, GrenadeType.ELECTRIC, GrenadeType.ACID, GrenadeType.NEURO))
                .effectIcon("omni_grenade_core")
                .buildAndRegister(ctx);
    }
}