package liedge.limatech.item.weapon;

import liedge.limacore.util.LimaEntityUtil;
import liedge.limacore.util.LimaNetworkUtil;
import liedge.limatech.entity.CompoundHitResult;
import liedge.limatech.lib.weapons.AbstractWeaponControls;
import liedge.limatech.registry.*;
import liedge.limatech.lib.upgrades.equipment.EquipmentUpgrades;
import liedge.limatech.registry.bootstrap.LimaTechDamageTypes;
import liedge.limatech.registry.bootstrap.LimaTechEquipmentUpgrades;
import liedge.limatech.util.config.LimaTechWeaponsConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagnumWeaponItem extends SemiAutoWeaponItem
{
    public MagnumWeaponItem(Properties properties)
    {
        super(properties);
    }

    @Override
    protected EquipmentUpgrades getDefaultUpgrades(HolderLookup.Provider registries)
    {
        return EquipmentUpgrades.builder()
                .set(registries.holderOrThrow(LimaTechEquipmentUpgrades.LIGHTFRAG_BASE_ARMOR_BYPASS))
                .build();
    }

    @Override
    public void weaponFired(ItemStack heldItem, Player player, Level level, AbstractWeaponControls controls)
    {
        player.getUsedItemHand();

        if (!level.isClientSide())
        {
            double inaccuracy = LimaEntityUtil.isEntityUsingItem(player, InteractionHand.MAIN_HAND) ? 0.15d : 2d;
            CompoundHitResult hitResult = CompoundHitResult.tracePath(level, player, 25d, inaccuracy, 0.25d, 1000);
            EquipmentUpgrades upgrades = getUpgrades(heldItem);

            hitResult.entityHits().forEach(hit -> causeInstantDamage(upgrades, player, LimaTechDamageTypes.MAGNUM_LIGHTFRAG, hit.getEntity(), LimaTechWeaponsConfig.MAGNUM_BASE_DAMAGE.getAsDouble()));
            postWeaponFiredGameEvent(upgrades, level, player);

            LimaNetworkUtil.spawnAlwaysVisibleParticle(level, LimaTechParticles.LIGHTFRAG_TRACER, hitResult.origin(), hitResult.impact().getLocation());
        }

        level.playSound(player, player, LimaTechSounds.MAGNUM_FIRE.get(), SoundSource.PLAYERS, 1.0f, 0.75f + (player.getRandom().nextFloat() * 0.2f));
    }

    @Override
    public int getEnergyCapacity(ItemStack stack)
    {
        return LimaTechWeaponsConfig.MAGNUM_ENERGY_CAPACITY.getAsInt();
    }

    @Override
    public int getEnergyReloadCost(ItemStack stack)
    {
        return LimaTechWeaponsConfig.MAGNUM_ENERGY_AMMO_COST.getAsInt();
    }

    @Override
    public Item getAmmoItem(ItemStack stack)
    {
        return LimaTechItems.MAGNUM_AMMO_CANISTER.asItem();
    }

    @Override
    public int getAmmoCapacity(ItemStack stack)
    {
        return 7;
    }

    @Override
    public int getFireRate(ItemStack stack)
    {
        return 13;
    }

    @Override
    public int getReloadSpeed(ItemStack stack)
    {
        return 30;
    }
}