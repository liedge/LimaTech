package liedge.limatech.integration.jei;

import liedge.limacore.recipe.LimaRecipeType;
import liedge.limacore.recipe.LimaSimpleSizedIngredientRecipe;
import liedge.limatech.client.gui.screen.SingleItemRecipeScreen;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.function.Supplier;

public abstract class SingleItemRecipeJEICategory<R extends LimaSimpleSizedIngredientRecipe<?>> extends LimaJEICategory<R>
{
    private final IDrawableStatic progressBackground;
    private final IDrawableAnimated progressForeground;

    public SingleItemRecipeJEICategory(IGuiHelper helper, Supplier<? extends LimaRecipeType<R>> gameRecipeType)
    {
        super(helper, gameRecipeType, SingleItemRecipeScreen.SCREEN_TEXTURE, 51, 29, 76, 26);
        this.progressBackground = widgetDrawable(helper, "crafting_progress_bg", 24, 6).build();
        this.progressForeground = widgetDrawable(helper, "crafting_progress", 22, 4).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void draw(RecipeHolder<R> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        progressBackground.draw(guiGraphics, 24, 10);
        progressForeground.draw(guiGraphics, 25, 11);
    }

    @Override
    protected void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<R> holder, R recipe, IFocusGroup focuses, RegistryAccess registries)
    {
        sizedIngredientsSlot(builder, recipe, 0, 3, 5);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 5).addItemStack(recipe.getResultItem(registries));
    }
}