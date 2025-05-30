package liedge.limatech.blockentity;

import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limacore.recipe.LimaRecipeInput;
import liedge.limatech.recipe.RecomposingRecipe;
import liedge.limatech.registry.game.LimaTechBlockEntities;
import liedge.limatech.registry.game.LimaTechMenus;
import liedge.limatech.registry.game.LimaTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static liedge.limatech.util.config.LimaTechMachinesConfig.*;

public class RecomposerBlockEntity extends SimpleRecipeMachineBlockEntity<LimaRecipeInput, RecomposingRecipe>
{
    public RecomposerBlockEntity(BlockPos pos, BlockState state)
    {
        super(LimaTechBlockEntities.RECOMPOSER.get(), LimaTechRecipeTypes.RECOMPOSING.get(), pos, state, 3);
    }

    @Override
    public int getBaseEnergyCapacity()
    {
        return RECOMPOSER_ENERGY_CAPACITY.getAsInt();
    }

    @Override
    public int getBaseEnergyUsage()
    {
        return RECOMPOSER_ENERGY_USAGE.getAsInt();
    }

    @Override
    public int getBaseTicksPerOperation()
    {
        return RECOMPOSER_CRAFTING_TIME.getAsInt();
    }

    @Override
    protected LimaRecipeInput getRecipeInput(Level level)
    {
        return LimaRecipeInput.createSingleSlot(getItemHandler(), 1);
    }

    @Override
    public boolean isInputSlot(int slot)
    {
        return slot == 1;
    }

    @Override
    public int getOutputSlot()
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
}
