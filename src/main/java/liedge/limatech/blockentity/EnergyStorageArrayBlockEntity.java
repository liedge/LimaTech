package liedge.limatech.blockentity;

import liedge.limacore.capability.energy.LimaBlockEntityEnergyStorage;
import liedge.limacore.capability.energy.LimaEnergyUtil;
import liedge.limacore.lib.LimaColor;
import liedge.limacore.network.sync.AutomaticDataWatcher;
import liedge.limacore.registry.LimaCoreNetworkSerializers;
import liedge.limatech.LimaTechConstants;
import liedge.limatech.registry.LimaTechBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import static liedge.limatech.util.config.LimaTechMachinesConfig.ESA_BASE_ENERGY_CAPACITY;
import static liedge.limatech.util.config.LimaTechMachinesConfig.ESA_BASE_TRANSFER_RATE;

public class EnergyStorageArrayBlockEntity extends BaseESABlockEntity
{
    private final LimaBlockEntityEnergyStorage energyStorage;
    private int remoteEnergyFill;

    public EnergyStorageArrayBlockEntity(BlockPos pos, BlockState state)
    {
        super(LimaTechBlockEntities.ENERGY_STORAGE_ARRAY.get(), pos, state);
        this.energyStorage = new LimaBlockEntityEnergyStorage(this);
    }

    @Override
    public LimaColor getRemoteEnergyFillColor()
    {
        return LimaTechConstants.REM_BLUE;
    }

    @Override
    public float getRemoteEnergyFill()
    {
        return remoteEnergyFill / 20f;
    }

    @Override
    public int getBaseEnergyCapacity()
    {
        return ESA_BASE_ENERGY_CAPACITY.getAsInt();
    }

    @Override
    public int getBaseEnergyTransferRate()
    {
        return ESA_BASE_TRANSFER_RATE.getAsInt();
    }

    @Override
    public LimaBlockEntityEnergyStorage getEnergyStorage()
    {
        return energyStorage;
    }

    @Override
    public void defineDataWatchers(DataWatcherCollector collector)
    {
        collector.register(AutomaticDataWatcher.keepSynced(LimaCoreNetworkSerializers.VAR_INT, () -> Mth.floor(LimaEnergyUtil.getClampedFillPercentage(getEnergyStorage()) * 20f), i -> this.remoteEnergyFill = i));
    }
}