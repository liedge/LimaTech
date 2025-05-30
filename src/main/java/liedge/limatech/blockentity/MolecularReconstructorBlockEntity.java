package liedge.limatech.blockentity;

import liedge.limacore.blockentity.IOAccess;
import liedge.limacore.capability.energy.LimaEnergyUtil;
import liedge.limacore.capability.itemhandler.LimaItemHandlerBase;
import liedge.limacore.client.gui.TooltipLineConsumer;
import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limacore.util.LimaItemUtil;
import liedge.limatech.LimaTechTags;
import liedge.limatech.blockentity.base.EnergyConsumerBlockEntity;
import liedge.limatech.blockentity.base.TimedProcessMachineBlockEntity;
import liedge.limatech.client.LimaTechLang;
import liedge.limatech.lib.upgrades.machine.MachineUpgrades;
import liedge.limatech.registry.game.LimaTechBlockEntities;
import liedge.limatech.registry.game.LimaTechMenus;
import liedge.limatech.util.LimaTechTooltipUtil;
import liedge.limatech.util.config.LimaTechMachinesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

public class MolecularReconstructorBlockEntity extends SidedItemEnergyMachineBlockEntity implements EnergyConsumerBlockEntity, TimedProcessMachineBlockEntity
{
    public static final int REPAIR_INPUT_SLOT = 1;
    public static final int REPAIR_OUTPUT_SLOT = 2;

    private int energyUsage = getBaseEnergyUsage();
    private int machineSpeed = getBaseTicksPerOperation();
    private int currentProcessTime;

    public MolecularReconstructorBlockEntity(BlockPos pos, BlockState state)
    {
        super(LimaTechBlockEntities.MOLECULAR_RECONSTRUCTOR.get(), pos, state, 3);
    }

    @Override
    public int getBaseEnergyCapacity()
    {
        return LimaTechMachinesConfig.REPAIRER_ENERGY_CAPACITY.getAsInt();
    }

    @Override
    public LimaMenuType<?, ?> getMenuType()
    {
        return LimaTechMenus.MOLECULAR_RECONSTRUCTOR.get();
    }

    @Override
    public int getBaseEnergyUsage()
    {
        return LimaTechMachinesConfig.REPAIRER_ENERGY_USAGE.getAsInt();
    }

    @Override
    public int getEnergyUsage()
    {
        return energyUsage;
    }

    @Override
    public void setEnergyUsage(int energyUsage)
    {
        this.energyUsage = energyUsage;
    }

    @Override
    public IOAccess getPrimaryHandlerItemSlotIO(int slot)
    {
        if (slot == REPAIR_INPUT_SLOT) return IOAccess.INPUT_ONLY;
        else if (slot == REPAIR_OUTPUT_SLOT) return IOAccess.OUTPUT_ONLY;
        else return IOAccess.DISABLED;
    }

    @Override
    protected boolean isItemValidForPrimaryHandler(int slot, ItemStack stack)
    {
        return switch (slot)
        {
            case ENERGY_ITEM_SLOT -> LimaItemUtil.hasEnergyCapability(stack);
            case REPAIR_INPUT_SLOT -> stack.isDamageableItem() && !stack.is(LimaTechTags.Items.REPAIR_BLACKLIST);
            default -> true;
        };
    }

    @Override
    public void appendStatsTooltips(TooltipLineConsumer consumer)
    {
        consumer.accept(LimaTechLang.MACHINE_TICKS_PER_OP_TOOLTIP.translateArgs(getTicksPerOperation()));
        LimaTechTooltipUtil.appendEnergyUsagePerTickTooltip(consumer, getEnergyUsage());
    }

    @Override
    protected void tickServer(ServerLevel level, BlockPos pos, BlockState state)
    {
        LimaItemHandlerBase inventory = getItemHandler();

        // Fill internal energy buffer
        fillEnergyBuffer();

        // Try repairing item
        ItemStack inputItem = inventory.getStackInSlot(REPAIR_INPUT_SLOT);
        if (inputItem.isDamaged() && !inputItem.is(LimaTechTags.Items.REPAIR_BLACKLIST))
        {
            if (LimaEnergyUtil.consumeEnergy(getEnergyStorage(), getEnergyUsage(), false))
            {
                currentProcessTime++;

                if (currentProcessTime >= getTicksPerOperation())
                {
                    ItemStack stack = inputItem.copy();

                    int oldDamage = stack.getOrDefault(DataComponents.DAMAGE, 0);
                    int newDamage = Math.max(0, oldDamage - 1);

                    stack.set(DataComponents.DAMAGE, newDamage);
                    inventory.setStackInSlot(REPAIR_INPUT_SLOT, stack);

                    currentProcessTime = 0;
                }
            }
        }
        else
        {
            currentProcessTime = 0;

            if (!inputItem.isEmpty() && inventory.getStackInSlot(REPAIR_OUTPUT_SLOT).isEmpty())
            {
                ItemStack extracted = inventory.extractItem(REPAIR_INPUT_SLOT, inventory.getSlotLimit(REPAIR_OUTPUT_SLOT), false);
                inventory.insertItem(REPAIR_OUTPUT_SLOT, extracted, false);
            }
        }

        // Auto output item
        autoOutputItems(40, REPAIR_OUTPUT_SLOT, 1);
    }

    @Override
    public void defineDataWatchers(DataWatcherCollector collector) {}

    @Override
    public int getCurrentProcessTime()
    {
        return currentProcessTime;
    }

    @Override
    public void setCurrentProcessTime(int currentProcessTime)
    {
        this.currentProcessTime = currentProcessTime;
    }

    @Override
    public int getBaseTicksPerOperation()
    {
        return LimaTechMachinesConfig.REPAIRER_OPERATION_TIME.getAsInt();
    }

    @Override
    public int getTicksPerOperation()
    {
        return machineSpeed;
    }

    @Override
    public void setTicksPerOperation(int ticksPerOperation)
    {
        this.machineSpeed = ticksPerOperation;
    }

    @Override
    public void onUpgradeRefresh(LootContext context, MachineUpgrades upgrades)
    {
        super.onUpgradeRefresh(context, upgrades);
        TimedProcessMachineBlockEntity.applyUpgrades(this, context, upgrades);
        EnergyConsumerBlockEntity.applyUpgrades(this, context, upgrades);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        tag.putInt(TAG_KEY_PROGRESS, currentProcessTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        this.currentProcessTime = tag.getInt(TAG_KEY_PROGRESS);
    }
}