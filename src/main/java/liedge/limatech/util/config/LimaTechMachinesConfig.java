package liedge.limatech.util.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class LimaTechMachinesConfig
{
    private LimaTechMachinesConfig() {}

    public static final ModConfigSpec.IntValue ESA_BASE_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue ESA_BASE_TRANSFER_RATE;

    public static final ModConfigSpec.IntValue DIGITAL_FURNACE_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue DIGITAL_FURNACE_ENERGY_USAGE;
    public static final ModConfigSpec.IntValue DIGITAL_FURNACE_CRAFTING_TIME;

    public static final ModConfigSpec.IntValue GRINDER_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue GRINDER_ENERGY_USAGE;
    public static final ModConfigSpec.IntValue GRINDER_CRAFTING_TIME;

    public static final ModConfigSpec.IntValue RECOMPOSER_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue RECOMPOSER_ENERGY_USAGE;
    public static final ModConfigSpec.IntValue RECOMPOSER_CRAFTING_TIME;

    public static final ModConfigSpec.IntValue MFC_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue MFC_ENERGY_USAGE;
    public static final ModConfigSpec.IntValue MFC_CRAFTING_TIME;

    public static final ModConfigSpec.IntValue FABRICATOR_ENERGY_CAPACITY;
    public static final ModConfigSpec.IntValue FABRICATOR_ENERGY_IO_RATE;

    public static final ModConfigSpec.DoubleValue ROCKET_TURRET_DAMAGE;

    public static final ModConfigSpec MACHINES_CONFIG_SPEC;

    static
    {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        // Energy Storage Array (ESA)
        builder.push("energy_storage_array");
        ESA_BASE_ENERGY_CAPACITY = builder.comment("Base energy capacity of the Energy Storage Array")
                        .defineInRange("energy_capacity", 5_000_000, 1, Integer.MAX_VALUE);
        ESA_BASE_TRANSFER_RATE = builder.comment("Base transfer rate (per tick) of the Energy Storage Array")
                        .defineInRange("transfer_rate", 50_000, 1, Integer.MAX_VALUE);
        builder.pop();

        // Digital Furnace
        builder.push("digital_furnace");
        DIGITAL_FURNACE_ENERGY_CAPACITY = builder.comment("Base energy capacity of the Digital Furnace")
                .defineInRange("energy_capacity", 250_000, 1, Integer.MAX_VALUE);
        DIGITAL_FURNACE_ENERGY_USAGE = builder.comment("Base energy usage per operation tick of the Digital Furnace")
                .defineInRange("energy_usage", 60, 1, Integer.MAX_VALUE);
        DIGITAL_FURNACE_CRAFTING_TIME = builder.comment("Base crafting time (in ticks) of the Digital Furnace")
                .defineInRange("crafting_time", 160, 1, Integer.MAX_VALUE);
        builder.pop();

        // Grinder
        builder.push("grinder");
        GRINDER_ENERGY_CAPACITY = builder.comment("Base energy capacity of the Grinder")
                .defineInRange("energy_capacity", 250_000, 1, Integer.MAX_VALUE);
        GRINDER_ENERGY_USAGE = builder.comment("Base energy usage per operation tick of the Grinder")
                .defineInRange("energy_usage", 80, 1, Integer.MAX_VALUE);
        GRINDER_CRAFTING_TIME = builder.comment("Base crafting time (in ticks) of the Grinder")
                .defineInRange("crafting_time", 160, 1, Integer.MAX_VALUE);
        builder.pop();

        // Recomposer
        builder.push("recomposer");
        RECOMPOSER_ENERGY_CAPACITY = builder.comment("Base energy capacity of the Recomposer")
                .defineInRange("energy_capacity", 250_000, 1, Integer.MAX_VALUE);
        RECOMPOSER_ENERGY_USAGE = builder.comment("Base energy usage per operation tick of the Recomposer")
                .defineInRange("energy_usage", 120, 1, Integer.MAX_VALUE);
        RECOMPOSER_CRAFTING_TIME = builder.comment("Base crafting time (in ticks) of the Recomposer")
                .defineInRange("crafting_time", 160, 1, Integer.MAX_VALUE);
        builder.pop();

        // Material Fusing Chamber
        builder.push("material_fusing_chamber");
        MFC_ENERGY_CAPACITY = builder.comment("Base energy capacity of the Material Fusing Chamber")
                .defineInRange("energy_capacity", 250_000, 1, Integer.MAX_VALUE);
        MFC_ENERGY_USAGE = builder.comment("Base energy usage per operation tick of the Material Fusing Chamber")
                .defineInRange("energy_usage", 120, 1, Integer.MAX_VALUE);
        MFC_CRAFTING_TIME = builder.comment("Base crafting time (in ticks) of the Material Fusing Chamber")
                .defineInRange("crafting_time", 160, 1, Integer.MAX_VALUE);
        builder.pop();

        // Fabricator
        builder.push("fabricator");
        FABRICATOR_ENERGY_CAPACITY = builder.comment("Energy capacity of the Fabricator.")
                .defineInRange("energy_capacity", 5_000_000, 1, Integer.MAX_VALUE);
        FABRICATOR_ENERGY_IO_RATE = builder.comment(
                        "Energy IO rate of the Fabricator.",
                        "This is also the crafting 'speed' of the fabricator since the recipe crafting times are based on energy consumption.")
                .defineInRange("energy_io_rate", 100_000, 1, Integer.MAX_VALUE);
        builder.pop();

        // Rocket turret
        builder.push("rocket_turret");
        ROCKET_TURRET_DAMAGE = builder.comment("Damage dealt by rockets from the rocket turret")
                .defineInRange("turret_damage", 40d, 1d, Double.MAX_VALUE);
        builder.pop();

        MACHINES_CONFIG_SPEC = builder.build();
    }
}