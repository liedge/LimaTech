package liedge.limatech.compat.jei;

import liedge.limacore.client.gui.UnmanagedSprite;
import liedge.limacore.recipe.LimaRecipeType;
import liedge.limacore.recipe.LimaSizedIngredientRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class LimaJEICategory<R extends LimaSizedIngredientRecipe<?>> implements IRecipeCategory<RecipeHolder<R>>
{
    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    protected LimaJEICategory(IGuiHelper helper, LimaRecipeType<R> gameRecipeType, ResourceLocation backgroundTexture, int backgroundU, int backgroundV, int width, int height)
    {
        this.background = helper.createDrawable(backgroundTexture, backgroundU, backgroundV, width, height);
        this.icon = helper.createDrawableItemStack(categoryIconItemStack());
        this.title = gameRecipeType.translate();
    }

    protected LimaJEICategory(IGuiHelper helper, Supplier<? extends LimaRecipeType<R>> typeSupplier, ResourceLocation backgroundTexture, int backgroundU, int backgroundV, int width, int height)
    {
        this(helper, typeSupplier.get(), backgroundTexture, backgroundU, backgroundV, width, height);
    }

    protected abstract ItemStack categoryIconItemStack();

    protected abstract void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<R> holder, R recipe, IFocusGroup focuses, RegistryAccess registries);

    protected final RegistryAccess localRegistryAccess()
    {
        return Objects.requireNonNull(Minecraft.getInstance().level, "Minecraft local level is null").registryAccess();
    }

    protected IDrawableBuilder unmanagedSpriteDrawable(IGuiHelper helper, UnmanagedSprite sprite)
    {
        return helper.drawableBuilder(sprite.textureSheet(), sprite.u(), sprite.v(), sprite.width(), sprite.height());
    }

    protected void sizedIngredientsSlot(IRecipeLayoutBuilder builder, R recipe, int ingredientIndex, int x, int y)
    {
        builder.addSlot(RecipeIngredientRole.INPUT, x, y).addItemStacks(List.of(recipe.getRecipeIngredient(ingredientIndex).getItems()));
    }

    @Override
    public final void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<R> recipe, IFocusGroup focuses)
    {
        setRecipe(builder, recipe, recipe.value(), focuses, localRegistryAccess());
    }

    @Override
    public Component getTitle()
    {
        return title;
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }
}