package liedge.limatech.blockentity;

import liedge.limacore.blockentity.IOAccess;
import liedge.limacore.blockentity.LimaBlockEntityType;
import liedge.limacore.capability.energy.LimaBlockEntityEnergyStorage;
import liedge.limacore.capability.energy.LimaEnergyStorage;
import liedge.limacore.capability.energy.LimaEnergyUtil;
import liedge.limacore.capability.itemhandler.LimaBlockEntityItemHandler;
import liedge.limacore.capability.itemhandler.LimaItemHandlerBase;
import liedge.limacore.capability.itemhandler.LimaItemHandlerUtil;
import liedge.limacore.util.LimaItemUtil;
import liedge.limatech.LimaTech;
import liedge.limatech.blockentity.io.MachineIOControl;
import liedge.limatech.blockentity.io.MachineInputType;
import liedge.limatech.blockentity.io.SidedMachineIOHolder;
import liedge.limatech.lib.machinetiers.MachineTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static liedge.limatech.block.LimaTechBlockProperties.MACHINE_WORKING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class SimpleRecipeMachineBlockEntity<I extends RecipeInput, R extends Recipe<I>> extends TieredMachineBlockEntity implements TimedProcessMachineBlockEntity, SidedMachineIOHolder
{
    private final LimaBlockEntityEnergyStorage machineEnergy;
    private final LimaBlockEntityItemHandler machineItems;
    private final int baseEnergyCapacity;
    private final MachineIOControl itemIOControl;
    private final Map<Direction, BlockCapabilityCache<IItemHandler, Direction>> itemConnections = new EnumMap<>(Direction.class);

    private int craftingProgress;
    private boolean shouldCheckRecipe;
    private boolean crafting;

    private int autoOutputTimer = 0;

    private @Nullable RecipeHolder<R> lastUsedRecipe;

    protected SimpleRecipeMachineBlockEntity(LimaBlockEntityType<?> type, BlockPos pos, BlockState state, int baseEnergyCapacity, int inventorySize)
    {
        super(type, pos, state);
        this.machineEnergy = new LimaBlockEntityEnergyStorage(this, baseEnergyCapacity, baseEnergyCapacity / 16);
        this.machineItems = new LimaBlockEntityItemHandler(this, inventorySize);
        this.baseEnergyCapacity = baseEnergyCapacity;

        Direction front = state.getValue(HORIZONTAL_FACING);
        this.itemIOControl = new MachineIOControl(this, MachineInputType.ITEMS, IOAccess.INPUT_OR_OUTPUT_ONLY_AND_DISABLED, IOAccess.DISABLED, front, false, false);
    }

    @Override
    public LimaBlockEntityEnergyStorage getEnergyStorage()
    {
        return machineEnergy;
    }

    @Override
    public LimaBlockEntityItemHandler getItemHandler()
    {
        return machineItems;
    }

    @Override
    protected void applyMachineTier(MachineTier tier)
    {
        int newCapacity = baseEnergyCapacity + getTotalEnergyUsage() * 8;
        getEnergyStorage().setMaxEnergyStored(newCapacity);
        getEnergyStorage().setTransferRate(getTotalEnergyUsage() * 2);
        LimaTech.LOGGER.debug("Applying machine tier {}", tier.getTierLevel());
    }

    @Override
    public void defineDataWatchers(DataWatcherCollector collector) {}

    @Override
    public int getCurrentProcessTime()
    {
        return craftingProgress;
    }

    @Override
    public void setCurrentProcessTime(int currentProcessTime)
    {
        this.craftingProgress = currentProcessTime;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return slot != 0 || LimaItemUtil.hasEnergyCapability(stack);
    }

    @Override
    public IOAccess getItemIOForSide(Direction side)
    {
        return itemIOControl.getSideIO(side);
    }

    @Override
    public @Nullable MachineIOControl getIOControls(MachineInputType inputType)
    {
        return inputType == MachineInputType.ITEMS ? itemIOControl : null;
    }

    @Override
    public void onIOControlsChanged(MachineInputType inputType)
    {
        invalidateCapabilities();
        setChanged();
    }

    @Override
    public boolean allowsAutoInput(MachineInputType inputType)
    {
        return false;
    }

    @Override
    public boolean allowsAutoOutput(MachineInputType inputType)
    {
        return inputType == MachineInputType.ITEMS;
    }

    @Override
    public int getTotalProcessDuration()
    {
        return baseCraftingTime() / getTier().getSpeedModifier();
    }

    public int getTotalEnergyUsage()
    {
        return baseEnergyUsage() * getTier().getEnergyMultiplier();
    }

    public abstract RecipeType<R> machineRecipeType();

    public abstract int baseEnergyUsage();

    public abstract int baseCraftingTime();

    protected abstract I getRecipeInput(Level level);

    protected abstract boolean isInputSlot(int slot);

    protected abstract int outputSlotIndex();

    protected abstract void consumeIngredients(I recipeInput, R recipe, Level level);

    private boolean checkRecipe(Level level, I recipeInput)
    {
        shouldCheckRecipe = false;
        Optional<RecipeHolder<R>> holder = level.getRecipeManager().getRecipeFor(machineRecipeType(), recipeInput, level, lastUsedRecipe);

        if (holder.isPresent())
        {
            lastUsedRecipe = holder.get();
            return true;
        }

        return false;
    }

    private void updateCraftingState(Level level, boolean crafting)
    {
        if (this.crafting != crafting)
        {
            this.crafting = crafting;
            setChanged();

            BlockState state = getBlockState();
            if (state.hasProperty(MACHINE_WORKING))
            {
                state = state.setValue(MACHINE_WORKING, crafting);
                level.setBlockAndUpdate(getBlockPos(), state);
            }
        }
    }

    private boolean canInsertResultItem(Level level, R recipe)
    {
        return LimaItemUtil.canMergeItemStacks(getItemHandler().getStackInSlot(outputSlotIndex()), recipe.getResultItem(level.registryAccess()));
    }

    @Override
    public IOAccess getExternalItemSlotIO(int slot)
    {
        if (slot == outputSlotIndex())
        {
            return IOAccess.OUTPUT_ONLY;
        }
        else if (isInputSlot(slot))
        {
            return IOAccess.INPUT_ONLY;
        }
        else
        {
            return IOAccess.DISABLED;
        }
    }

    @Override
    public void onItemSlotChanged(int slot)
    {
        super.onItemSlotChanged(slot);
        if (slot != 0) shouldCheckRecipe = true;
    }

    @Override
    protected void tickServer(Level level, BlockPos pos, BlockState state)
    {
        LimaEnergyStorage energyStorage = getEnergyStorage();
        LimaItemHandlerBase inventory = getItemHandler();

        // Fill internal energy buffer from energy item slot
        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored())
        {
            IEnergyStorage itemEnergy = inventory.getStackInSlot(0).getCapability(Capabilities.EnergyStorage.ITEM);
            if (itemEnergy != null)
            {
                LimaEnergyUtil.transferEnergyBetween(itemEnergy, energyStorage, energyStorage.getTransferRate(), false);
            }
        }

        // Perform recipe check if required - set crafting state accordingly
        I recipeInput = getRecipeInput(level);
        if (shouldCheckRecipe)
        {
            updateCraftingState(level, checkRecipe(level, recipeInput) && lastUsedRecipe != null && canInsertResultItem(level, lastUsedRecipe.value()));
        }

        // Tick recipe progress
        if (crafting && lastUsedRecipe != null && canInsertResultItem(level, lastUsedRecipe.value()))
        {
            if (craftingProgress >= getTotalProcessDuration())
            {
                ItemStack craftedItem = lastUsedRecipe.value().assemble(recipeInput, level.registryAccess());
                getItemHandler().insertItem(outputSlotIndex(), craftedItem, false);
                consumeIngredients(recipeInput, lastUsedRecipe.value(), level);
                craftingProgress = 0;
                shouldCheckRecipe = true; // Check state of recipe after every successful craft.
            }
            else if (LimaEnergyUtil.consumeEnergy(energyStorage, getTotalEnergyUsage(), false))
            {
                craftingProgress++; // setChanged already called by energy storage extraction
            }
        }
        else
        {
            craftingProgress = 0;
        }

        // Auto output item if option available
        if (itemIOControl.isAutoOutput())
        {
            if (autoOutputTimer >= 20)
            {
                for (Direction side : Direction.values())
                {
                    if (itemIOControl.getSideIO(side).allowsOutput())
                    {
                        IItemHandler adjacentInventory = itemConnections.get(side).getCapability();
                        if (adjacentInventory != null)
                        {
                            ItemStack outputItem = getItemHandler().getStackInSlot(outputSlotIndex());
                            ItemStack insertResult = LimaItemHandlerUtil.insertIntoAnySlot(adjacentInventory, outputItem, true);

                            // Insert the extracted stack into adjacent inventory
                            if (outputItem != insertResult)
                            {
                                int inserted = outputItem.getCount() - insertResult.getCount();
                                LimaItemHandlerUtil.insertIntoAnySlot(adjacentInventory, getItemHandler().extractItem(outputSlotIndex(), inserted, false), false);
                            }
                        }
                    }
                }

                autoOutputTimer = 0;
            }
            else
            {
                autoOutputTimer++;
            }
        }
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        this.shouldCheckRecipe = true;

        itemIOControl.setFacing(getBlockState().getValue(HORIZONTAL_FACING));
        if (level instanceof ServerLevel serverLevel)
        {
            for (Direction side : Direction.values())
            {
                itemConnections.put(side, createCapabilityCache(Capabilities.ItemHandler.BLOCK, serverLevel, side));
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        craftingProgress = tag.getInt("progress");
        itemIOControl.deserializeNBT(registries, tag.getCompound("item_io"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        tag.putInt("progress", craftingProgress);
        tag.put("item_io", itemIOControl.serializeNBT(registries));
    }
}