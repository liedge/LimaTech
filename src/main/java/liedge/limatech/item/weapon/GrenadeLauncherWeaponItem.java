package liedge.limatech.item.weapon;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import liedge.limacore.client.gui.TooltipLineConsumer;
import liedge.limacore.lib.OrderedEnum;
import liedge.limacore.lib.Translatable;
import liedge.limatech.LimaTech;
import liedge.limatech.entity.OrbGrenadeEntity;
import liedge.limatech.item.ScrollModeSwitchItem;
import liedge.limatech.lib.upgrades.equipment.EquipmentUpgrades;
import liedge.limatech.lib.weapons.AbstractWeaponControls;
import liedge.limatech.lib.weapons.GrenadeType;
import liedge.limatech.registry.*;
import liedge.limatech.registry.bootstrap.LimaTechEquipmentUpgrades;
import liedge.limatech.util.config.LimaTechWeaponsConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static liedge.limatech.registry.LimaTechDataComponents.GRENADE_TYPE;

public class GrenadeLauncherWeaponItem extends SemiAutoWeaponItem implements ScrollModeSwitchItem
{
    public static final Translatable GRENADE_TYPE_TOOLTIP = LimaTech.RESOURCES.translationHolder("tooltip.{}.selected_grenade_type");

    public static GrenadeType getGrenadeTypeFromItem(ItemStack stack)
    {
        return stack.getOrDefault(GRENADE_TYPE, GrenadeType.EXPLOSIVE);
    }

    public GrenadeLauncherWeaponItem(Properties properties)
    {
        super(properties);
    }

    public void setGrenadeType(ItemStack stack, GrenadeType grenadeType)
    {
        stack.set(GRENADE_TYPE, grenadeType);
    }

    public ItemStack createDefaultStack(@Nullable HolderLookup.Provider registries, boolean fullMagazine, GrenadeType grenadeType)
    {
        ItemStack stack = createDefaultStack(registries, fullMagazine);
        setGrenadeType(stack, grenadeType);
        return stack;
    }

    @Override
    public boolean canFocusReticle(ItemStack heldItem, Player player, AbstractWeaponControls controls)
    {
        return false;
    }

    @Override
    public void refreshEquipmentUpgrades(ItemStack stack, EquipmentUpgrades upgrades)
    {
        super.refreshEquipmentUpgrades(stack, upgrades);

        GrenadeType currentlyEquipped = getGrenadeTypeFromItem(stack);
        boolean shouldReset = upgrades.effectFlatStream(LimaTechUpgradeEffectComponents.GRENADE_UNLOCK.get()).noneMatch(currentlyEquipped::equals);
        if (shouldReset) setGrenadeType(stack, GrenadeType.EXPLOSIVE);
    }

    @Override
    public int getEnergyCapacity(ItemStack stack)
    {
        return LimaTechWeaponsConfig.GRENADE_LAUNCHER_ENERGY_CAPACITY.getAsInt();
    }

    @Override
    public int getEnergyReloadCost(ItemStack stack)
    {
        return LimaTechWeaponsConfig.GRENADE_LAUNCHER_ENERGY_AMMO_COST.getAsInt();
    }

    @Override
    public void weaponFired(ItemStack heldItem, Player player, Level level, AbstractWeaponControls controls)
    {
        if (!level.isClientSide())
        {
            EquipmentUpgrades upgrades = getUpgrades(heldItem);

            OrbGrenadeEntity grenade = new OrbGrenadeEntity(level, getGrenadeTypeFromItem(heldItem), upgrades);
            grenade.setOwner(player);
            grenade.aimAndSetPosFromShooter(player, calculateProjectileSpeed(upgrades, 1.5d), 0.35d);
            level.addFreshEntity(grenade);

            postWeaponFiredGameEvent(upgrades, level, player);
        }

        level.playSound(player, player, LimaTechSounds.GRENADE_LAUNCHER_FIRE.get(), SoundSource.PLAYERS, 2f, Mth.randomBetween(level.random, 0.9f, 1.1f));
    }

    @Override
    public Item getAmmoItem(ItemStack stack)
    {
        return LimaTechItems.EXPLOSIVES_AMMO_CANISTER.asItem();
    }

    @Override
    public int getAmmoCapacity(ItemStack stack)
    {
        return 6;
    }

    @Override
    public int getFireRate(ItemStack stack)
    {
        return 15;
    }

    @Override
    public int getReloadSpeed(ItemStack stack)
    {
        return 40;
    }

    @Override
    public void switchItemMode(ItemStack stack, Player player, int delta)
    {
        final boolean forward = delta == 1;

        EquipmentUpgrades upgrades = getUpgrades(stack);
        Set<GrenadeType> availableTypes = new ObjectOpenHashSet<>();
        availableTypes.add(GrenadeType.EXPLOSIVE); // Always allow equipping explosive shells
        upgrades.forEachEffect(LimaTechUpgradeEffectComponents.GRENADE_UNLOCK, (effect, rank) -> availableTypes.add(effect));

        GrenadeType currentType = GrenadeLauncherWeaponItem.getGrenadeTypeFromItem(stack);
        GrenadeType toSwitch = forward ? OrderedEnum.nextAvailable(availableTypes, currentType) : OrderedEnum.previousAvailable(availableTypes, currentType);
        if (!currentType.equals(toSwitch))
        {
            setGrenadeType(stack, toSwitch);
            player.level().playSound(null, player, LimaTechSounds.WEAPON_MODE_SWITCH.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }

    @Override
    public void addAdditionalToCreativeTab(ResourceLocation tabId, CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility)
    {
        HolderLookup.Provider registries = parameters.holders();
        ItemStack stack = createDefaultStack(registries, true);
        stack.set(LimaTechDataComponents.EQUIPMENT_UPGRADES, EquipmentUpgrades.builder()
                .set(registries.holderOrThrow(LimaTechEquipmentUpgrades.OMNI_GRENADE_CORE))
                .build());
        output.accept(stack, tabVisibility);
    }

    @Override
    public void appendTooltipHintComponents(@Nullable Level level, ItemStack stack, TooltipLineConsumer consumer)
    {
        consumer.accept(GRENADE_TYPE_TOOLTIP.translateArgs(getGrenadeTypeFromItem(stack).translate()));
        super.appendTooltipHintComponents(level, stack, consumer);
    }
}