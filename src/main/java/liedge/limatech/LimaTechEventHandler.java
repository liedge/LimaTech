package liedge.limatech;

import liedge.limacore.capability.itemhandler.LimaItemHandlerUtil;
import liedge.limacore.util.LimaCoreUtil;
import liedge.limatech.blockentity.RocketTurretBlockEntity;
import liedge.limatech.entity.BubbleShieldUser;
import liedge.limatech.item.weapon.WeaponItem;
import liedge.limatech.lib.TurretDamageSource;
import liedge.limatech.lib.weapons.WeaponDamageSource;
import liedge.limatech.network.packet.ClientboundEntityShieldPacket;
import liedge.limatech.network.packet.ClientboundPlayerShieldPacket;
import liedge.limatech.registry.LimaTechAttachmentTypes;
import liedge.limatech.registry.LimaTechAttributes;
import liedge.limatech.registry.LimaTechUpgradeEffectComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Iterator;

@EventBusSubscriber(modid = LimaTech.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class LimaTechEventHandler
{
    private LimaTechEventHandler() {}

    @SubscribeEvent
    public static void onPlayerTick(final PlayerTickEvent.Pre event)
    {
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        WeaponItem weaponItem = LimaCoreUtil.castOrNull(WeaponItem.class, heldItem.getItem());
        player.getData(LimaTechAttachmentTypes.WEAPON_CONTROLS).tickInput(player, heldItem, weaponItem);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(final PlayerEvent.PlayerChangedDimensionEvent event)
    {
        ClientboundPlayerShieldPacket.sendPacketToPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onStartTrackingEntity(final PlayerEvent.StartTracking event)
    {
        Entity entity = event.getTarget();

        if (!entity.level().isClientSide())
        {
            BubbleShieldUser shield = entity.getCapability(LimaTechCapabilities.ENTITY_BUBBLE_SHIELD);
            if (shield != null)
            {
                ClientboundEntityShieldPacket packet = new ClientboundEntityShieldPacket(entity.getId(), shield.getShieldHealth());
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event)
    {
        ClientboundPlayerShieldPacket.sendPacketToPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void gatherSkippedAttributeTooltips(final GatherSkippedAttributeTooltipsEvent event)
    {
        if (event.getStack().getItem() instanceof WeaponItem)
        {
            event.setSkipAll(true);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(final EntityTickEvent.Pre event)
    {
        if (event.getEntity() instanceof LivingEntity livingEntity)
        {
            livingEntity.getExistingData(LimaTechAttachmentTypes.BUBBLE_SHIELD).ifPresent(shield -> shield.tickShield(livingEntity));
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(final LivingDropsEvent event)
    {
        if (event.getSource() instanceof TurretDamageSource damageSource)
        {
            // Check if upgrade effect is active
            RocketTurretBlockEntity be = damageSource.getBlockEntity();
            if (be.getUpgrades().upgradeEffectTypePresent(LimaTechUpgradeEffectComponents.DIRECT_ITEM_TELEPORT.get()))
            {
                Iterator<ItemEntity> iterator = event.getDrops().iterator();

                while (iterator.hasNext())
                {
                    ItemEntity itemEntity = iterator.next();

                    ItemStack original = itemEntity.getItem();
                    ItemStack insertRemainder = LimaItemHandlerUtil.insertItemIntoSlots(be.getItemHandler(), original, 1, 21, false);

                    if (insertRemainder.isEmpty()) iterator.remove(); // Entity drop removed if fully inserted
                    if (original.getCount() != insertRemainder.getCount()) itemEntity.setItem(insertRemainder); // Partial remaining stack still drops in world
                }

                if (event.getDrops().isEmpty()) event.setCanceled(true); // No point in continuing event if iterator was emptied
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingIncomingDamage(final LivingIncomingDamageEvent event)
    {
        LivingEntity hurtEntity = event.getEntity();
        DamageSource source = event.getSource();

        // Check for universal attack strength
        if (source.getEntity() instanceof LivingEntity attacker)
        {
            double universalAttack = attacker.getAttributeValue(LimaTechAttributes.UNIVERSAL_STRENGTH);
            if (universalAttack != 1.0)
            {
                float newDamage = event.getAmount() * (float) universalAttack;
                event.setAmount(newDamage);
            }
        }

        // Check for bubble shield
        if (!hurtEntity.level().isClientSide())
        {
            BubbleShieldUser shieldUser = hurtEntity.getCapability(LimaTechCapabilities.ENTITY_BUBBLE_SHIELD);
            if (shieldUser != null)
            {
                if (shieldUser.blockDamage(hurtEntity.level(), source, event.getAmount())) event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(final LivingDeathEvent event)
    {
        if (event.getSource() instanceof WeaponDamageSource damageSource && damageSource.getEntity() instanceof Player player)
        {
            LivingEntity targetEntity = event.getEntity();
            damageSource.weaponItem().onPlayerKill(damageSource, player, targetEntity);
        }
    }
}