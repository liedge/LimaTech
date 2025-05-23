package liedge.limatech.blockentity;

import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limatech.registry.game.LimaTechBlockEntities;
import liedge.limatech.registry.game.LimaTechMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static liedge.limatech.util.config.LimaTechMachinesConfig.*;

public class DigitalFurnaceBlockEntity extends SimpleRecipeMachineBlockEntity<SingleRecipeInput, SmeltingRecipe>
{
    public DigitalFurnaceBlockEntity(BlockPos pos, BlockState state)
    {
        super(LimaTechBlockEntities.DIGITAL_FURNACE.get(), RecipeType.SMELTING, pos, state, DIGITAL_FURNACE_ENERGY_CAPACITY.getAsInt(), 3);
    }

    @Override
    public int getBaseEnergyUsage()
    {
        return DIGITAL_FURNACE_ENERGY_USAGE.getAsInt();
    }

    @Override
    public int getBaseTicksPerOperation()
    {
        return DIGITAL_FURNACE_CRAFTING_TIME.getAsInt();
    }

    @Override
    protected SingleRecipeInput getRecipeInput(Level level)
    {
        return new SingleRecipeInput(getItemHandler().getStackInSlot(1));
    }

    @Override
    protected void consumeIngredients(SingleRecipeInput recipeInput, SmeltingRecipe recipe, Level level)
    {
        getItemHandler().extractItem(1, 1, false);
    }

    @Override
    public LimaMenuType<?, ?> getMenuType()
    {
        return LimaTechMenus.DIGITAL_FURNACE.get();
    }

    @Override
    public boolean isInputSlot(int index)
    {
        return index == 1;
    }

    @Override
    public int getOutputSlot()
    {
        return 2;
    }
}