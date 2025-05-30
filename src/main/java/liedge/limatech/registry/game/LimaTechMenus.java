package liedge.limatech.registry.game;

import liedge.limacore.inventory.menu.BlockEntityAccessMenuType;
import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limatech.LimaTech;
import liedge.limatech.LimaTechIds;
import liedge.limatech.blockentity.*;
import liedge.limatech.blockentity.base.UpgradableMachineBlockEntity;
import liedge.limatech.menu.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class LimaTechMenus
{
    private LimaTechMenus() {}

    private static final DeferredRegister<MenuType<?>> TYPES = LimaTech.RESOURCES.deferredRegister(Registries.MENU);

    public static void register(IEventBus bus)
    {
        TYPES.register(bus);
    }

    public static final DeferredHolder<MenuType<?>, IOControllerMenu.MenuType> MACHINE_IO_CONTROL = TYPES.register("machine_io_control", IOControllerMenu.MenuType::new);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<UpgradableMachineBlockEntity, MachineUpgradeMenu>> MACHINE_UPGRADES = TYPES.register("machine_upgrades", id -> BlockEntityAccessMenuType.create(id, UpgradableMachineBlockEntity.class, MachineUpgradeMenu::new));

    public static final DeferredHolder<MenuType<?>, LimaMenuType<BaseESABlockEntity, EnergyStorageArrayMenu>> ENERGY_STORAGE_ARRAY = TYPES.register(LimaTechIds.ID_ENERGY_STORAGE_ARRAY, id -> BlockEntityAccessMenuType.create(id, BaseESABlockEntity.class, EnergyStorageArrayMenu::new));
    public static final DeferredHolder<MenuType<?>, LimaMenuType<DigitalFurnaceBlockEntity, SingleItemRecipeMenu<DigitalFurnaceBlockEntity>>> DIGITAL_FURNACE = registerSingleItemRecipe(LimaTechIds.ID_DIGITAL_FURNACE, DigitalFurnaceBlockEntity.class);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<DigitalSmokerBlockEntity, SingleItemRecipeMenu<DigitalSmokerBlockEntity>>> DIGITAL_SMOKER = registerSingleItemRecipe(LimaTechIds.ID_DIGITAL_SMOKER, DigitalSmokerBlockEntity.class);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<DigitalBlastFurnaceBlockEntity, SingleItemRecipeMenu<DigitalBlastFurnaceBlockEntity>>> DIGITAL_BLAST_FURNACE = registerSingleItemRecipe(LimaTechIds.ID_DIGITAL_BLAST_FURNACE, DigitalBlastFurnaceBlockEntity.class);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<GrinderBlockEntity, SingleItemRecipeMenu<GrinderBlockEntity>>> GRINDER = registerSingleItemRecipe(LimaTechIds.ID_GRINDER, GrinderBlockEntity.class);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<RecomposerBlockEntity, SingleItemRecipeMenu<RecomposerBlockEntity>>> RECOMPOSER = registerSingleItemRecipe(LimaTechIds.ID_RECOMPOSER, RecomposerBlockEntity.class);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<MaterialFusingChamberBlockEntity, MaterialFusingChamberMenu>> MATERIAL_FUSING_CHAMBER = TYPES.register(LimaTechIds.ID_MATERIAL_FUSING_CHAMBER, id -> BlockEntityAccessMenuType.create(id, MaterialFusingChamberBlockEntity.class, MaterialFusingChamberMenu::new));
    public static final DeferredHolder<MenuType<?>, LimaMenuType<FabricatorBlockEntity, FabricatorMenu>> FABRICATOR = TYPES.register(LimaTechIds.ID_FABRICATOR, id -> BlockEntityAccessMenuType.create(id, FabricatorBlockEntity.class, FabricatorMenu::new));
    public static final DeferredHolder<MenuType<?>, LimaMenuType<AutoFabricatorBlockEntity, AutoFabricatorMenu>> AUTO_FABRICATOR = TYPES.register(LimaTechIds.ID_AUTO_FABRICATOR, id -> BlockEntityAccessMenuType.create(id, AutoFabricatorBlockEntity.class, AutoFabricatorMenu::new));

    public static final DeferredHolder<MenuType<?>, LimaMenuType<MolecularReconstructorBlockEntity, MolecularReconstructorMenu>> MOLECULAR_RECONSTRUCTOR = TYPES.register(LimaTechIds.ID_MOLECULAR_RECONSTRUCTOR, id -> BlockEntityAccessMenuType.create(id, MolecularReconstructorBlockEntity.class, MolecularReconstructorMenu::new));

    public static final DeferredHolder<MenuType<?>, LimaMenuType<EquipmentUpgradeStationBlockEntity, EquipmentUpgradeStationMenu>> EQUIPMENT_UPGRADE_STATION = TYPES.register(LimaTechIds.ID_EQUIPMENT_UPGRADE_STATION, id -> BlockEntityAccessMenuType.create(id, EquipmentUpgradeStationBlockEntity.class, EquipmentUpgradeStationMenu::new));

    public static final DeferredHolder<MenuType<?>, LimaMenuType<RocketTurretBlockEntity, TurretMenu<RocketTurretBlockEntity>>> ROCKET_TURRET = registerTurret(LimaTechIds.ID_ROCKET_TURRET, RocketTurretBlockEntity.class);
    public static final DeferredHolder<MenuType<?>, LimaMenuType<RailgunTurretBlockEntity, TurretMenu<RailgunTurretBlockEntity>>> RAILGUN_TURRET = registerTurret(LimaTechIds.ID_RAILGUN_TURRET, RailgunTurretBlockEntity.class);

    private static <BE extends SimpleRecipeMachineBlockEntity<?, ?>> DeferredHolder<MenuType<?>, LimaMenuType<BE, SingleItemRecipeMenu<BE>>> registerSingleItemRecipe(String name, Class<BE> beClass)
    {
        return TYPES.register(name, id -> BlockEntityAccessMenuType.<BE, SingleItemRecipeMenu<BE>>create(id, beClass, SingleItemRecipeMenu::new));
    }

    private static <BE extends BaseTurretBlockEntity> DeferredHolder<MenuType<?>, LimaMenuType<BE, TurretMenu<BE>>> registerTurret(String name, Class<BE> beClass)
    {
        return TYPES.register(name, id -> BlockEntityAccessMenuType.<BE, TurretMenu<BE>>create(id, beClass, TurretMenu::new));
    }
}