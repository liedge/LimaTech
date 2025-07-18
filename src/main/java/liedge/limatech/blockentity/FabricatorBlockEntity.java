package liedge.limatech.blockentity;

import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limacore.recipe.LimaRecipeInput;
import liedge.limatech.recipe.FabricatingRecipe;
import liedge.limatech.registry.game.LimaTechBlockEntities;
import liedge.limatech.registry.game.LimaTechItems;
import liedge.limatech.registry.game.LimaTechMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FabricatorBlockEntity extends BaseFabricatorBlockEntity
{
    public FabricatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(LimaTechBlockEntities.FABRICATOR.get(), pos, state, 3);
    }

    @Override
    public boolean isInputSlot(int index)
    {
        return false;
    }

    public void startCrafting(Level level, RecipeHolder<FabricatingRecipe> holder, LimaRecipeInput input, boolean forceStart)
    {
        FabricatingRecipe recipe = holder.value();

        if (!isCrafting() && canInsertRecipeResult(level, recipe))
        {
            if (forceStart)
            {
                setCrafting(true);
                getRecipeCheck().setLastUsedRecipe(holder);
            }
            else if (recipe.matches(input, level))
            {
                recipe.consumeIngredientsLenientSlots(input, false);
                setCrafting(true);
                getRecipeCheck().setLastUsedRecipe(holder);
            }
        }
    }

    private void stopCrafting(boolean insertResult, Level level)
    {
        getRecipeCheck().getLastUsedRecipe(level).ifPresent(holder -> {
            FabricatingRecipe recipe = holder.value();

            if (insertResult) getItemHandler().insertItem(OUTPUT_SLOT, recipe.assemble(null, level.registryAccess()), false);

            energyCraftProgress = 0;
            setCrafting(false);
        });
    }

    @Override
    protected void tickServerFabricator(ServerLevel level, BlockPos pos, BlockState state)
    {
        // Try progressing crafting recipe
        FabricatingRecipe recipe = getRecipeCheck().getLastUsedRecipe(level).map(RecipeHolder::value).orElse(null);
        if (isCrafting() && recipe != null)
        {
            if (energyCraftProgress < recipe.getEnergyRequired())
            {
                int toExtract = Math.min(getEnergyUsage(), recipe.getEnergyRequired() - energyCraftProgress);
                energyCraftProgress += getEnergyStorage().extractEnergy(toExtract, false);
            }
            else
            {
                stopCrafting(true, level);
            }
        }
        else
        {
            stopCrafting(false, level);
        }
    }

    @Override
    protected ItemLike getValidBlueprintItem()
    {
        return LimaTechItems.EMPTY_FABRICATION_BLUEPRINT;
    }

    @Override
    public LimaMenuType<?, ?> getMenuType()
    {
        return LimaTechMenus.FABRICATOR.get();
    }
}