package liedge.limatech.blockentity;

import liedge.limacore.blockentity.LimaBlockEntityType;
import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limacore.recipe.LimaRecipeInput;
import liedge.limatech.recipe.RecomposingRecipe;
import liedge.limatech.registry.LimaTechMenus;
import liedge.limatech.registry.LimaTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static liedge.limatech.util.config.LimaTechMachinesConfig.*;

public class RecomposerBlockEntity extends SimpleRecipeMachineBlockEntity<LimaRecipeInput, RecomposingRecipe>
{
    public RecomposerBlockEntity(LimaBlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state, RECOMPOSER_ENERGY_CAPACITY.getAsInt(), 3);
    }

    @Override
    public RecipeType<RecomposingRecipe> machineRecipeType()
    {
        return LimaTechRecipeTypes.RECOMPOSING.get();
    }

    @Override
    public int machineEnergyUse()
    {
        return RECOMPOSER_ENERGY_USAGE.getAsInt();
    }

    @Override
    protected LimaRecipeInput getRecipeInput(Level level)
    {
        return new LimaRecipeInput(getItemHandler(), 1, 1);
    }

    @Override
    protected boolean isInputSlot(int slot)
    {
        return slot == 1;
    }

    @Override
    protected int outputSlotIndex()
    {
        return 2;
    }

    @Override
    protected void consumeIngredients(LimaRecipeInput recipeInput, RecomposingRecipe recipe, Level level)
    {
        recipe.consumeIngredientsStrictSlots(recipeInput, false, false);
    }

    @Override
    public LimaMenuType<?, ?> getMenuType()
    {
        return LimaTechMenus.RECOMPOSER.get();
    }

    @Override
    public int getTotalProcessDuration()
    {
        return RECOMPOSER_CRAFTING_TIME.getAsInt();
    }
}