package liedge.limatech.util.datagen;

import liedge.limacore.LimaCoreTags;
import liedge.limacore.data.generation.LimaLanguageProvider;
import liedge.limatech.LimaTech;
import liedge.limatech.LimaTechTags;
import liedge.limatech.blockentity.io.MachineInputType;
import liedge.limatech.client.LimaTechKeyMappings;
import liedge.limatech.client.LimaTechLang;
import liedge.limatech.item.SimpleHintItem;
import liedge.limatech.item.TooltipShiftHintItem;
import liedge.limatech.item.weapon.GrenadeLauncherWeaponItem;
import liedge.limatech.item.weapon.WeaponItem;
import liedge.limatech.lib.math.CompoundOperation;
import liedge.limatech.lib.upgrades.equipment.EquipmentUpgrade;
import liedge.limatech.lib.upgrades.machine.MachineUpgrade;
import liedge.limatech.lib.weapons.GrenadeType;
import liedge.limatech.lib.weapons.WeaponAmmoSource;
import liedge.limatech.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.function.Supplier;

import static liedge.limatech.client.LimaTechLang.*;
import static liedge.limatech.registry.LimaTechBlocks.*;
import static liedge.limatech.registry.LimaTechCreativeTabs.*;
import static liedge.limatech.registry.LimaTechItems.*;
import static liedge.limatech.registry.LimaTechRecipeTypes.*;
import static liedge.limatech.registry.LimaTechSounds.*;

class LanguageGen extends LimaLanguageProvider
{
    LanguageGen(PackOutput output)
    {
        super(output, LimaTech.RESOURCES);
    }

    @Override
    protected void addTranslations()
    {
        // Attributes
        add(LimaTechAttributes.UNIVERSAL_STRENGTH.get().getDescriptionId(), "Universal Attack Strength");

        //#region Blocks
        addBlock(TITANIUM_ORE, "Titanium Ore");
        addBlock(DEEPSLATE_TITANIUM_ORE, "Deepslate Titanium Ore");
        addBlock(NIOBIUM_ORE, "Niobium Ore");
        addBlock(RAW_TITANIUM_BLOCK, "Block of Raw Titanium");
        addBlock(RAW_NIOBIUM_BLOCK, "Block of Raw Niobium");
        addBlock(TITANIUM_BLOCK, "Block of Titanium");
        addBlock(NIOBIUM_BLOCK, "Block of Niobium");
        addBlock(SLATE_ALLOY_BLOCK, "Block of Slate Alloy");

        GLOW_BLOCKS.forEach((color, deferredBlock) -> addBlock(deferredBlock, localizeSimpleName(color) + " Glow Block"));

        addBlock(ENERGY_STORAGE_ARRAY, "Energy Storage Array");
        addBlock(INFINITE_ENERGY_STORAGE_ARRAY, "Energy Storage Array (∞)");
        addBlock(DIGITAL_FURNACE, "Digital Furnace");
        addBlock(GRINDER, "Grinder");
        addBlock(RECOMPOSER, "Recomposer");
        addBlock(MATERIAL_FUSING_CHAMBER, "Material Fusing Chamber");
        addBlock(FABRICATOR, "Fabricator");
        addBlock(EQUIPMENT_UPGRADE_STATION, "Equipment Upgrade Station");

        addBlock(ROCKET_TURRET, "Anti-Air Rocket Turret");
        //#endregion

        //#region Items
        addItem(RAW_TITANIUM, "Raw Titanium");
        addItem(TITANIUM_INGOT, "Titanium Ingot");
        addItem(TITANIUM_NUGGET, "Titanium Nugget");
        addItem(RAW_NIOBIUM, "Raw Niobium");
        addItem(NIOBIUM_INGOT, "Niobium Ingot");
        addItem(NIOBIUM_NUGGET, "Niobium Nugget");

        addItem(WHITE_PIGMENT, "Titanium White Pigment");
        addItem(LIME_PIGMENT, "Lime Pigment");

        addItem(TITANIUM_SWORD, "Titanium Sword");
        addItem(TITANIUM_SHOVEL, "Titanium Shovel");
        addItem(TITANIUM_PICKAXE, "Titanium Pickaxe");
        addItem(TITANIUM_AXE, "Titanium Axe");
        addItem(TITANIUM_HOE, "Titanium Hoe");
        addItem(TITANIUM_SHEARS, "Titanium Shears");

        addItem(DEEPSLATE_POWDER, "Deepslate Powder");
        addItem(SLATE_ALLOY_INGOT, "Slate Alloy Ingot");
        addItem(BEDROCK_ALLOY_INGOT, "Bedrock Alloy Ingot");
        addItem(SLATE_ALLOY_NUGGET, "Slate Alloy Nugget");
        addItem(COPPER_CIRCUIT, "Copper Circuit");
        addItem(GOLD_CIRCUIT, "Gold Circuit");
        addItem(NIOBIUM_CIRCUIT, "Niobium Circuit");

        addItem(COAL_ORE_PEBBLES, "Coal Ore Pebbles");
        addItem(COPPER_ORE_PEBBLES, "Copper Ore Pebbles");
        addItem(IRON_ORE_PEBBLES, "Iron Ore Pebbles");
        addItem(LAPIS_ORE_PEBBLES, "Lapis Lazuli Ore Pebbles");
        addItem(REDSTONE_ORE_PEBBLES, "Redstone Ore Pebbles");
        addItem(GOLD_ORE_PEBBLES, "Gold Ore Pebbles");
        addItem(DIAMOND_ORE_PEBBLES, "Diamond Ore Pebbles");
        addItem(EMERALD_ORE_PEBBLES, "Emerald Ore Pebbles");
        addItem(QUARTZ_ORE_PEBBLES, "Nether Quartz Ore Pebbles");
        addItem(NETHERITE_ORE_PEBBLES, "Netherite Scrap Ore Pebbles");
        addItem(TITANIUM_ORE_PEBBLES, "Titanium Ore Pebbles");
        addItem(NIOBIUM_ORE_PEBBLES, "Niobium Ore Pebbles");
        addItem(MACHINE_WRENCH, "Machine Wrench");
        addItem(EMPTY_UPGRADE_MODULE, "Empty Upgrade Module");

        simpleHintItem(EXPLOSIVES_WEAPON_TECH_SALVAGE, "Salvaged Tech: Explosive Weapon Systems", "Broken components from an explosives handling device. Might be useful in reconstructing explosive weaponry.");
        simpleHintItem(TARGETING_TECH_SALVAGE, "Salvaged Tech: Auto-Targeting Systems", "Broken electronics from a targeting computer. Might be useful in reconstructing guidance systems for weaponry.");

        addWeaponItem(SUBMACHINE_GUN, "Serenity", "LTX-07/SD");
        addWeaponItem(SHOTGUN, "Aurora", "LTX-19/SG");
        addWeaponItem(GRENADE_LAUNCHER, "Hanabi", "LTX-33/GL");
        addWeaponItem(ROCKET_LAUNCHER, "Daybreak", "LTX-42/RL");
        addWeaponItem(MAGNUM, "Nova", "LTX-77/HX");

        simpleHintItem(AUTO_AMMO_CANISTER, "Automatics Ammo Canister", "Stabilized energy suitable for use in low-power high frequency projectile synthesis.");
        simpleHintItem(SPECIALIST_AMMO_CANISTER, "Specialist Ammo Canister", "Concentrated energy suitable for use in medium-power projectile synthesis.");
        simpleHintItem(EXPLOSIVES_AMMO_CANISTER, "Explosives Ammo Canister", "Volatile energy suitable for use in explosive weaponry.");
        simpleHintItem(ROCKET_LAUNCHER_AMMO, "Daybreak Mini Missiles", "Programmable, compact rockets for use in the Daybreak launcher.");
        simpleHintItem(MAGNUM_AMMO_CANISTER, "Magnum Ammo Canister", "Highly concentrated, compact energy capable of handling the energy spikes of the Nova magnum.");
        //#endregion

        //#region Equipment upgrades
        equipmentUpgrade(LimaTechEquipmentUpgrades.LIGHTFRAG_BASE_ARMOR_BYPASS, "Lightfrag Weaponry", "Lightning-fast LightFragments pierce the natural armor-like skin of certain enemies such as undead.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.SMG_BUILT_IN, "Serenity Intrinsics", "Serenity's small light-frags zip right through targets without a trace.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.SHOTGUN_BUILT_IN, "Aurora Intrinsics", "Aurora's combat precepts, specialized in fast assault and scout operations.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.HIGH_IMPACT_ROUNDS, "High Impact Rounds", "Light-frags with a punch! Send targets flying back regardless of their knockback resistances.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.MAGNUM_SCALING_ROUNDS, "Stellar Reality Disruptor", "Rip through reality itself with this Nova upgrade. Ensures swift defeat of even the strongest enemies.");

        equipmentUpgrade(LimaTechEquipmentUpgrades.UNIVERSAL_ANTI_VIBRATION, "Acoustic Attenuation", "Eliminates Sculk sound vibrations from weapons and their projectiles.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.UNIVERSAL_STEALTH_DAMAGE, "Biometric Obfuscation", "Hey, wasn't me.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.UNIVERSAL_ENERGY_AMMO, "Energy Ammo Synthesis", "Modifies the weapon's magazine system to reload from an internal Common Energy reserve instead of ammo canister items.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.UNIVERSAL_INFINITE_AMMO, "Infinite Ammo", "Weapon ignores ammo and can fire infinitely.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.UNIVERSAL_ARMOR_PIERCE, "Armor-Piercing Rounds", "Weapon ignores 10% of armor per upgrade rank.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.UNIVERSAL_SHIELD_REGEN, "Shield Siphon", "Restores 4 points of bubble shield per kill, for a maximum of the upgrade rank times 10.");

        equipmentUpgrade(LimaTechEquipmentUpgrades.LOOTING_ENCHANTMENT, "Loot Booster", "Earn more loot drops from defeated enemies, affected by rank.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.AMMO_SCAVENGER_ENCHANTMENT, "Ammunition Finder", "Increases the chance to find rarer weapon ammo types.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.RAZOR_ENCHANTMENT, "Razor's Edge", "Defeated enemies can drop their heads, affected by rank.");

        equipmentUpgrade(LimaTechEquipmentUpgrades.GRENADE_LAUNCHER_PROJECTILE_SPEED, "Hanabi Launch Boost", "Increases the velocity of the Hanabi grenades.");

        equipmentUpgrade(LimaTechEquipmentUpgrades.FLAME_GRENADE_CORE, "Hanabi Core/Flame", "Grenades are loaded with a concentrated fuel that creates powerful flames.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.FREEZE_GRENADE_CORE, "Hanabi Core/Freeze", "Grenades contain a cryogenic compound that freezes a large area.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.ELECTRIC_GRENADE_CORE, "Hanabi Core/Electric", "Grenades create a burst of electrical energy. Recommended for use in humid/aquatic environments.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.ACID_GRENADE_CORE, "Hanabi Core/Acid", "Grenades contain a highly corrosive acid that reduces target armor strength.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.NEURO_GRENADE_CORE, "Hanabi Core/Neuro", "Grenades contain a powerful neuro-suppressant agent that highly reduces target attack strength.");
        equipmentUpgrade(LimaTechEquipmentUpgrades.OMNI_GRENADE_CORE, "Hanabi Core/ARCOIRIS", "Full spectrum adaptable core for the Hanabi. Allows the use of any of grenade types.");
        //#endregion

        //#region Machine upgrades
        machineUpgrade(LimaTechMachineUpgrades.ESA_CAPACITY_UPGRADE, "Auxiliary Energy Cells", "Increases the energy capacity and transfer rate of the Energy Storage Array.");
        machineUpgrade(LimaTechMachineUpgrades.GENERAL_PROCESSING_UPGRADE, "Upgraded Internal Parts", "General purpose upgrade for basic machines. Increases stats all around as well as processing speed (at the cost of increased energy consumption).");
        machineUpgrade(LimaTechMachineUpgrades.FABRICATOR_UPGRADE, "Enhanced Tool Head", "Elevate your Fabricator's manufacturing capabilities with superior internal components.");
        //#endregion

        // Creative tabs
        creativeTab(MAIN_TAB, "LimaTech");
        creativeTab(EQUIPMENT_MODULES_TAB, "LimaTech: Equipment Upgrades");
        creativeTab(MACHINE_MODULES_TAB, "LimaTech: Machine Upgrades");

        //#region Menu titles
        add(LimaTechMenus.MACHINE_UPGRADES, "Machine Upgrades");
        add(LimaTechMenus.ENERGY_STORAGE_ARRAY, "Energy Storage Array");
        add(LimaTechMenus.DIGITAL_FURNACE, "Digital Furnace");
        add(LimaTechMenus.GRINDER, "Grinder");
        add(LimaTechMenus.RECOMPOSER, "Recomposer");
        add(LimaTechMenus.MATERIAL_FUSING_CHAMBER, "Material Fusing Chamber");
        add(LimaTechMenus.FABRICATOR, "Fabricator");
        add(LimaTechMenus.EQUIPMENT_UPGRADE_STATION, "Equipment Upgrade Station");
        //#endregion

        // Machine input types
        add(MachineInputType.ITEMS, "Items IO Control");
        add(MachineInputType.ENERGY, "Energy IO Control");
        add(MachineInputType.FLUIDS, "Fluids IO Control");

        // Recipe types
        add(GRINDING, "Grinding");
        add(RECOMPOSING, "Recomposing");
        add(MATERIAL_FUSING, "Material Fusing");
        add(FABRICATING, "Fabricating");

        // Entity type names
        addEntityType(LimaTechEntities.ORB_GRENADE, "Orb Grenade");
        addEntityType(LimaTechEntities.ROCKET_LAUNCHER_MISSILE, "Rocket Launcher Missile");
        addEntityType(LimaTechEntities.TURRET_MISSILE, "Turret Missile");
        addEntityType(LimaTechEntities.STICKY_FLAME, "Sticky Flame");

        // Mob effects
        addEffect(LimaTechMobEffects.FREEZING, "Freezing");
        addEffect(LimaTechMobEffects.CORROSIVE, "Corroding");
        addEffect(LimaTechMobEffects.NEURO, "Neuro-Suppressed");

        // Enchantments
        enchantment(LimaTechEnchantments.RAZOR, "Razor");
        enchantment(LimaTechEnchantments.AMMO_SCAVENGER, "Ammo Scavenger");

        //#region Tooltips
        add(INLINE_ENERGY_STORED, "Energy: %s");
        add(INLINE_ENERGY_CAPACITY, "Capacity: %s");
        add(INLINE_ENERGY_TRANSFER_RATE, "I/O: %s/t");
        add(ENERGY_OVERCHARGE_TOOLTIP, "Energy Overcharged! Your energy stored is more than your current capacity.");

        add(TOP_IO_LABEL, "Top (%s)");
        add(BOTTOM_IO_LABEL, "Bottom (%s)");
        add(FRONT_IO_LABEL, "Front (%s)");
        add(REAR_IO_LABEL, "Rear (%s)");
        add(LEFT_IO_LABEL, "Left (%s)");
        add(RIGHT_IO_LABEL, "Right (%s)");
        add(BACK_BUTTON_LABEL, "Back");
        add(AUTO_OUTPUT_OFF_TOOLTIP, "Auto Output Disabled");
        add(AUTO_OUTPUT_ON_TOOLTIP, "Auto Output Enabled");
        add(AUTO_INPUT_OFF_TOOLTIP, "Auto Input Disabled");
        add(AUTO_INPUT_ON_TOOLTIP, "Auto Input Enabled");

        add(EMPTY_ITEM_INVENTORY_TOOLTIP, "No items stored");
        add(ITEM_INVENTORY_TOOLTIP, "Stored Items");
        add(FABRICATOR_CLICK_TO_CRAFT_TOOLTIP, "Click again to craft");
        add(FABRICATOR_ENERGY_REQUIRED_TOOLTIP, "Energy required: %s");
        add(CRAFTING_PROGRESS_TOOLTIP, "Crafting: %s%%");
        add(UPGRADE_RANK_TOOLTIP, "Rank %s/%s");
        add(UPGRADE_REMOVE_HINT, "Shift + left click to remove upgrade. Must have space in your inventory.");
        add(UPGRADE_COMPATIBILITY_TOOLTIP, "Compatible with:");
        add(EQUIPMENT_UPGRADE_MODULE_TOOLTIP, "Equipment Upgrade Module");
        add(MACHINE_UPGRADE_MODULE_TOOLTIP, "Machine Upgrade Module");

        add(CompoundOperation.SET, "Sets %s as new base");
        add(CompoundOperation.FLAT_ADDITION, "%s");
        add(CompoundOperation.ADD_MULTIPLIED_BASE, "%s base");
        add(CompoundOperation.ADD_MULTIPLIED_TOTAL, "%s total");
        add(CompoundOperation.MULTIPLY, "%sx");

        add(INVALID_UPGRADE_HINT, "This upgrade data in this module is invalid or corrupted. Shift+Right Click to revert to an empty module.");

        add(LimaTechUpgradeDataTypes.WEAPON_DAMAGE, "%s weapon damage");
        add(LimaTechUpgradeDataTypes.WEAPON_PROJECTILE_SPEED, "%s projectile speed");
        add(LimaTechUpgradeDataTypes.ENERGY_CAPACITY, "%s CE capacity");
        add(LimaTechUpgradeDataTypes.ENERGY_TRANSFER_RATE, "%s CE transfer rate/tick");
        add(LimaTechUpgradeDataTypes.MACHINE_ENERGY_USAGE, "%s CE usage");
        add(LimaTechUpgradeDataTypes.TICKS_PER_OPERATION, "%s ticks/operation");

        add(TARGET_ATTRIBUTE_VALUE_EFFECT, "%s of enemy %s");
        add(PLAYER_ATTRIBUTE_VALUE_EFFECT, "%s of player %s");
        add(WEAPON_KNOCKBACK_EFFECT, "%s knockback power");
        add(DYNAMIC_DAMAGE_TAG_EFFECT, "Adds damage type tag: %s");
        add(WEAPON_DAMAGE_EFFECT, "%s bonus damage");
        add(PROJECTILE_SPEED_EFFECT, "%s projectile speed");
        add(NO_SCULK_VIBRATIONS_EFFECT, "No sculk vibrations");
        add(ENERGY_AMMO_EFFECT, "Unlocks CE ammunition synthesis");
        add(INFINITE_AMMO_EFFECT, "Unlocks infinite ammo");
        add(ENERGY_CAPACITY_UPGRADE, "Energy Capacity: %s");
        add(ENERGY_TRANSFER_RATE_UPGRADE, "Energy Transfer Rate: %s");
        add(SHIELD_UPGRADE_EFFECT, "%s shield per kill, (max %s)");
        add(ENCHANTMENT_UPGRADE_EFFECT, "%s %s Enchantment");
        add(ARMOR_BYPASS_EFFECT, "Equipment bypasses %s armor");
        add(GRENADE_UNLOCK_EFFECT, "Unlocks %s grenades");

        add(TooltipShiftHintItem.HINT_HOVER_TOOLTIP, "Hold SHIFT for extra info");
        add(WeaponAmmoSource.NORMAL.getItemTooltip(), "Reloads with %s");
        add(WeaponAmmoSource.COMMON_ENERGY_UNIT.getItemTooltip(), "Synthesizes ammo from Common Energy Units");
        add(WeaponAmmoSource.INFINITE.getItemTooltip(), "This weapon has infinite ammo!");

        add(WeaponItem.AMMO_LOADED_TOOLTIP, "Ammo: %s/%s");
        add(WeaponItem.ENERGY_AMMO_COST_TOOLTIP, "Ammo Synth Cost: %s");
        add(GrenadeLauncherWeaponItem.GRENADE_TYPE_TOOLTIP, "%s rounds equipped");
        //#endregion

        // Sound subtitles
        soundEvent(WEAPON_MODE_SWITCH, "Weapon mode switched");
        soundEvent(SUBMACHINE_GUN_LOOP, "Submachine gun firing");
        soundEvent(SHOTGUN_FIRE, "Shotgun fires");
        soundEvent(GRENADE_LAUNCHER_FIRE, "Grenade launched");
        soundEvent(ROCKET_LAUNCHER_FIRE, "Rocket launched");
        soundEvent(MAGNUM_FIRE, "Magnum fires");
        soundEvent(MISSILE_EXPLODE, "Missile explodes");
        GRENADE_SOUNDS.forEach((element, holder) -> soundEvent(holder, localizeSimpleName(element) + " grenade explodes"));

        // Orb grenade elements
        for (GrenadeType element : GrenadeType.values())
        {
            add(element, localizeSimpleName(element));
        }

        // Damage types
        add(INVALID_WEAPON_DEATH_MESSAGE, "%s was killed by an invalid LTX weapon");
        add(STRAY_PROJECTILE_DEATH_MESSAGE, "%s was killed by a stray %s");
        damageType(LimaTechDamageTypes.LIGHTFRAG, "%2$s shot %1$s with %3$s");
        damageType(LimaTechDamageTypes.MAGNUM_LIGHTFRAG, "%s was erased by %s's %s");
        damageType(LimaTechDamageTypes.EXPLOSIVE_GRENADE, "%s was blown away by %s's %s");
        damageType(LimaTechDamageTypes.FLAME_GRENADE, "%s was incinerated by %s's %s");
        damageType(LimaTechDamageTypes.FREEZE_GRENADE, "%s was frozen solid by %s's %s");
        damageType(LimaTechDamageTypes.ELECTRIC_GRENADE, "%s was electrocuted by %s's %s");
        damageType(LimaTechDamageTypes.ACID_GRENADE, "%s was dissolved by %s's %s");
        damageType(LimaTechDamageTypes.NEURO_GRENADE, "%s was decayed by %s's %s");
        damageType(LimaTechDamageTypes.ROCKET_LAUNCHER, "%s was blown up by %s's %s");

        traceableDamageSourceMessage(LimaTechDamageTypes.STICKY_FLAME, "%s was cooked well-done", "%s was cooked well-done by %s");
        traceableDamageSourceMessage(LimaTechDamageTypes.TURRET_ROCKET, "%s was shot out of the sky by a rocket turret", "s was shot out of the sky by %s's rocket turret");

        //#region Advancements
        //#endregion

        // Named tags
        add(LimaTechTags.Items.LTX_WEAPONS, "All LTX Weapons");

        namedDamageTag(LimaCoreTags.DamageTypes.IGNORES_KNOCKBACK_RESISTANCE, "Ignores Knockback Resistance");
        namedDamageTag(DamageTypeTags.NO_ANGER, "No Anger");
        namedDamageTag(DamageTypeTags.NO_KNOCKBACK, "No Knockback");

        // Patchouli compatibility
        add("item.limatech.guidebook", "LimaTech Guidebook");
        add("limatech.guidebook.landing_text", "Welcome to LimaTech! This guidebook contains hints, info, and instructions on the basic mechanics of the mod.");

        // Key mapping
        add(LimaTechKeyMappings.CATEGORY_LIMATECH, "LimaTech");
        add(LimaTechKeyMappings.RELOAD_KEY_SUBTITLE, "Reload Weapon");
    }

    private void simpleHintItem(DeferredItem<SimpleHintItem> item, String name, String hint)
    {
        addItem(item, name);
        add(item.get().getShiftHint(), hint);
    }

    private void addWeaponItem(Supplier<? extends WeaponItem> supplier, String name, String designation)
    {
        addItem(supplier, name);
        add(supplier.get().getDesignationId(), designation);
    }

    private void traceableDamageSourceMessage(ResourceKey<DamageType> damageTypeKey, String translation, String entityTranslation)
    {
        damageTypeAndVariants(damageTypeKey, translation, collector -> collector.accept("entity", entityTranslation));
    }

    private void equipmentUpgrade(ResourceKey<EquipmentUpgrade> key, String title, String description)
    {
        add(EquipmentUpgrade.defaultTitleTranslationKey(key), title);
        add(EquipmentUpgrade.defaultDescriptionTranslationKey(key), description);
    }

    private void machineUpgrade(ResourceKey<MachineUpgrade> key, String title, String description)
    {
        add(MachineUpgrade.defaultTitleTranslationKey(key), title);
        add(MachineUpgrade.defaultDescriptionTranslationKey(key), description);
    }

    private void namedDamageTag(TagKey<DamageType> tagKey, String value)
    {
        add(LimaTechLang.namedDamageTagKey(tagKey), value);
    }
}