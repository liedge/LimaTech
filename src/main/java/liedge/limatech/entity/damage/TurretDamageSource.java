package liedge.limatech.entity.damage;

import com.google.common.base.Preconditions;
import liedge.limacore.capability.itemhandler.LimaItemHandlerUtil;
import liedge.limatech.blockentity.BaseTurretBlockEntity;
import liedge.limatech.lib.upgrades.UpgradesContainerBase;
import liedge.limatech.lib.upgrades.effect.equipment.DirectDropsUpgradeEffect;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class TurretDamageSource extends UpgradableDamageSource
{
    public static TurretDamageSource create(Level level, ResourceKey<DamageType> damageTypeKey, BaseTurretBlockEntity blockEntity, @Nullable Entity directEntity, @Nullable Entity owner, @Nullable Vec3 location)
    {
        Preconditions.checkArgument(!(directEntity == null && location == null), "Turret damage must have either a direct projectile entity or a location");
        return new TurretDamageSource(level.registryAccess().holderOrThrow(damageTypeKey), blockEntity, directEntity, owner, location);
    }

    private final BaseTurretBlockEntity blockEntity;

    private TurretDamageSource(Holder<DamageType> type, BaseTurretBlockEntity blockEntity, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 location)
    {
        super(type, directEntity, causingEntity, location);
        this.blockEntity = blockEntity;
    }

    @Override
    public UpgradesContainerBase<?, ?> getUpgrades()
    {
        return blockEntity.getUpgrades();
    }

    @Override
    public @Nullable DropsRedirect createDropsRedirect()
    {
        IItemHandler inventory = LimaItemHandlerUtil.sizedRangedWrapper(blockEntity.getItemHandler(), BaseTurretBlockEntity.DROPS_INVENTORY_SLOT_START, BaseTurretBlockEntity.DROPS_INVENTORY_SIZE);
        return DropsRedirect.create(inventory, blockEntity.getProjectileStart(), getUpgrades(), DirectDropsUpgradeEffect.Type.ENTITY_DROPS);
    }
}