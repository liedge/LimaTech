package liedge.limatech.util.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class LimaTechWeaponsConfig
{
    private LimaTechWeaponsConfig() {}

    public static final float MIN_BASE_DAMAGE = 0.5f;
    public static final float MAX_BASE_DAMAGE = 4096f;

    // Submachine gun
    public static final ModConfigSpec.DoubleValue SMG_BASE_DAMAGE;
    public static final ModConfigSpec.IntValue SMG_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue SMG_ENERGY_AMMO_COST;

    // Shotgun
    public static final ModConfigSpec.DoubleValue SHOTGUN_BASE_PELLET_DAMAGE;
    public static final ModConfigSpec.IntValue SHOTGUN_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue SHOTGUN_ENERGY_AMMO_COST;

    // Grenade Launcher
    public static final ModConfigSpec.IntValue GRENADE_LAUNCHER_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue GRENADE_LAUNCHER_ENERGY_AMMO_COST;
    public static final ModConfigSpec.DoubleValue EXPLOSIVE_GRENADE_BASE_DAMAGE;
    public static final ModConfigSpec.DoubleValue FLAME_GRENADE_BASE_DAMAGE;
    public static final ModConfigSpec.DoubleValue FLAME_GRENADE_DAMAGE_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue CRYO_GRENADE_BASE_DAMAGE;
    public static final ModConfigSpec.DoubleValue CRYO_GRENADE_DAMAGE_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue ELECTRIC_GRENADE_BASE_DAMAGE;
    public static final ModConfigSpec.DoubleValue ELECTRIC_GRENADE_DAMAGE_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue ACID_GRENADE_BASE_DAMAGE;
    public static final ModConfigSpec.DoubleValue NEURO_GRENADE_BASE_DAMAGE;

    // LFR
    public static final ModConfigSpec.DoubleValue LFR_BASE_DAMAGE;
    public static final ModConfigSpec.IntValue LFR_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue LFR_ENERGY_AMMO_COST;

    // Rocket Launcher
    public static final ModConfigSpec.DoubleValue ROCKET_LAUNCHER_BASE_DAMAGE;
    public static final ModConfigSpec.IntValue ROCKET_LAUNCHER_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue ROCKET_LAUNCHER_ENERGY_AMMO_COST;

    // Magnum
    public static final ModConfigSpec.DoubleValue MAGNUM_BASE_DAMAGE;
    public static final ModConfigSpec.IntValue MAGNUM_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue MAGNUM_ENERGY_AMMO_COST;

    public static final ModConfigSpec WEAPONS_CONFIG_SPEC;

    static
    {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        // Common messages
        final String baseDamageMsg = "Weapon base damage (each full number is half a heart).";
        final String ceuCapacityMsg = "Common Energy Unit capacity when using applicable upgrades.";
        final String ceuAmmoCostMsg = "Ammo synthesis cost (in Common Energy Units) when using applicable upgrades.";

        // SMG
        builder.push("submachine_gun");
        SMG_BASE_DAMAGE = builder.comment(baseDamageMsg).defineInRange("base_damage", 3.0, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        SMG_ENERGY_CAPACITY = builder.comment(ceuCapacityMsg).defineInRange("energy_capacity", 1_000_000, 1, Integer.MAX_VALUE);
        SMG_ENERGY_AMMO_COST = builder.comment(ceuAmmoCostMsg).defineInRange("energy_ammo_cost", 80_000, 1, Integer.MAX_VALUE);
        builder.pop();

        // Shotgun
        builder.push("shotgun");
        SHOTGUN_BASE_PELLET_DAMAGE = builder.comment("Base damage PER PELLET of the shotgun. The shotgun fires 7 pellets.").defineInRange("base_damage", 9.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        SHOTGUN_ENERGY_CAPACITY = builder.comment(ceuCapacityMsg).defineInRange("energy_capacity", 5_000_000, 1, Integer.MAX_VALUE);
        SHOTGUN_ENERGY_AMMO_COST = builder.comment(ceuAmmoCostMsg).defineInRange("energy_ammo_cost", 200_000, 1, Integer.MAX_VALUE);
        builder.pop();

        // Grenade Launcher
        builder.push("grenade_launcher");
        GRENADE_LAUNCHER_ENERGY_CAPACITY = builder.comment(ceuCapacityMsg).defineInRange("energy_capacity", 10_000_000, 1, Integer.MAX_VALUE);
        GRENADE_LAUNCHER_ENERGY_AMMO_COST = builder.comment(ceuAmmoCostMsg).defineInRange("energy_ammo_cost", 1_000_000, 1, Integer.MAX_VALUE);
        EXPLOSIVE_GRENADE_BASE_DAMAGE = builder.comment("Explosive grenades base damage").defineInRange("explosive_base_damage", 30.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        FLAME_GRENADE_BASE_DAMAGE = builder.comment("Flame grenades base damage").defineInRange("flame_base_damage", 10.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        FLAME_GRENADE_DAMAGE_MULTIPLIER = builder.comment("Flame grenades damage multiplier to mobs that have the dataTag limatech:weak_to_flame").defineInRange("flame_multiplier", 3.0d, 0d, Double.MAX_VALUE);
        CRYO_GRENADE_BASE_DAMAGE = builder.comment("Cryo grenades base damage").defineInRange("cryo_base_damage", 4.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        CRYO_GRENADE_DAMAGE_MULTIPLIER = builder.comment("Cryo grenades damage multiplier to mobs that have the dataTag limatech:weak_to_cryo").defineInRange("cryo_multiplier", 8.0d, 0d, Double.MAX_VALUE);
        ELECTRIC_GRENADE_BASE_DAMAGE = builder.comment("Electric grenades base damage").defineInRange("electric_base_damage", 30.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        ELECTRIC_GRENADE_DAMAGE_MULTIPLIER = builder.comment("Electric grenades damage multiplier to mobs in rain/water or that have the entity type tag limatech:weak_to_electric").defineInRange("electric_multiplier", 3.0d, 0d, Double.MAX_VALUE);
        ACID_GRENADE_BASE_DAMAGE = builder.comment("Acid grenades base damage").defineInRange("acid_base_damage", 50.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        NEURO_GRENADE_BASE_DAMAGE = builder.comment("Neuro grenades base damage").defineInRange("neuro_base_damage", 4.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        builder.pop();

        builder.push("linear_fusion_rifle");
        LFR_BASE_DAMAGE = builder.comment(baseDamageMsg).defineInRange("base_damage", 45d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        LFR_ENERGY_CAPACITY = builder.comment(ceuCapacityMsg).defineInRange("energy_capacity", 10_000_000, 1, Integer.MAX_VALUE);
        LFR_ENERGY_AMMO_COST = builder.comment(ceuAmmoCostMsg).defineInRange("energy_ammo_cost", 1_000_000, 1, Integer.MAX_VALUE);
        builder.pop();

        // Rocket Launcher
        builder.push("rocket_launcher");
        ROCKET_LAUNCHER_BASE_DAMAGE = builder.comment(baseDamageMsg).defineInRange("base_damage", 80.0d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        ROCKET_LAUNCHER_ENERGY_CAPACITY = builder.comment(ceuCapacityMsg).defineInRange("energy_capacity", 20_000_00, 1, Integer.MAX_VALUE);
        ROCKET_LAUNCHER_ENERGY_AMMO_COST = builder.comment(ceuAmmoCostMsg).defineInRange("energy_ammo_cost", 1_000_000, 1, Integer.MAX_VALUE);
        builder.pop();

        // Magnum
        builder.push("magnum");
        MAGNUM_BASE_DAMAGE = builder.comment(baseDamageMsg).defineInRange("base_damage", 75d, MIN_BASE_DAMAGE, MAX_BASE_DAMAGE);
        MAGNUM_ENERGY_CAPACITY = builder.comment(ceuCapacityMsg).defineInRange("energy_capacity", 20_000_000, 1, Integer.MAX_VALUE);
        MAGNUM_ENERGY_AMMO_COST = builder.comment(ceuAmmoCostMsg).defineInRange("energy_ammo_cost", 20_000_000, 1, Integer.MAX_VALUE);
        builder.pop();

        WEAPONS_CONFIG_SPEC = builder.build();
    }
}