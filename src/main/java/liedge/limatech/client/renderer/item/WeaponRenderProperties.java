package liedge.limatech.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import liedge.limacore.client.LimaCoreClientUtil;
import liedge.limacore.client.LimaSpecialItemRenderer;
import liedge.limacore.client.model.BakedQuadGroup;
import liedge.limacore.lib.LimaColor;
import liedge.limacore.util.LimaCoreUtil;
import liedge.limacore.util.LimaMathUtil;
import liedge.limatech.client.model.baked.DynamicModularItemBakedModel;
import liedge.limatech.client.model.custom.TranslucentFillModel;
import liedge.limatech.client.renderer.LimaTechArmPoses;
import liedge.limatech.client.renderer.LimaTechRenderTypes;
import liedge.limatech.item.weapon.WeaponItem;
import liedge.limatech.lib.weapons.ClientWeaponControls;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public abstract class WeaponRenderProperties<T extends WeaponItem> extends LimaSpecialItemRenderer<T> implements IClientItemExtensions
{
    public static WeaponRenderProperties<?> fromItem(WeaponItem weaponItem)
    {
        return LimaCoreUtil.castOrThrow(WeaponRenderProperties.class, IClientItemExtensions.of(weaponItem));
    }

    private RenderType nonEmissiveRenderType;
    private RenderType emissiveRenderType;
    protected BakedQuadGroup mainSubmodel;

    @Override
    protected void onResourceManagerReload(ResourceManager manager, T item)
    {
        DynamicModularItemBakedModel model = LimaCoreClientUtil.getCustomBakedModel(LimaCoreClientUtil.inventoryModelPath(item), DynamicModularItemBakedModel.class);
        this.mainSubmodel = model.getSubmodel("main");
        this.nonEmissiveRenderType = model.getNonEmissiveRenderType();
        this.emissiveRenderType = model.getEmissiveRenderType();
        loadWeaponModelParts(item, model);
    }

    @Override
    protected void renderCustomItem(ItemStack stack, T item, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int light, int overlay)
    {
        if (isFirstPersonMainHand(displayContext) && Minecraft.getInstance().player != null)
        {
            ClientWeaponControls controls = ClientWeaponControls.of(Minecraft.getInstance().player);
            renderWeaponFirstPerson(stack, item, displayContext, poseStack, bufferSource, light, overlay, partialTick, controls);
        }
        else
        {
            renderStaticWeapon(stack, item, displayContext, poseStack, bufferSource, light, overlay);
        }
    }

    @Override
    public final BlockEntityWithoutLevelRenderer getCustomRenderer()
    {
        return this;
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess)
    {
        if (player.isUsingItem())
        {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate(i * 0.56f, -0.52f, -0.72f);

            return true;
        }

        return false;
    }

    @Override
    public @Nullable HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack heldItem)
    {
        return LimaTechArmPoses.TWO_HANDED_WEAPON.getValue();
    }

    public abstract void renderCrosshair(LocalPlayer player, WeaponItem weaponItem, ClientWeaponControls controls, GuiGraphics graphics, float partialTicks, int screenWidth, int screenHeight, LimaColor crosshairColor);

    public abstract void onWeaponFired(ItemStack stack, WeaponItem weaponItem, ClientWeaponControls controls);

    protected abstract void loadWeaponModelParts(T item, DynamicModularItemBakedModel model);

    protected abstract void renderStaticWeapon(ItemStack stack, T item, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay);

    protected abstract void renderWeaponFirstPerson(ItemStack stack, T item, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, float partialTick, ClientWeaponControls controls);

    protected void renderSubModel(BakedQuadGroup submodel, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        submodel.putItemQuadsInBuffer(poseStack, bufferSource, nonEmissiveRenderType, emissiveRenderType, LimaColor.WHITE, LimaColor.WHITE, packedLight);
    }

    protected void renderSubModel(BakedQuadGroup submodel, PoseStack poseStack, MultiBufferSource bufferSource, LimaColor nonEmissiveTint, LimaColor emissiveTint, int packedLight)
    {
        submodel.putItemQuadsInBuffer(poseStack, bufferSource, nonEmissiveRenderType, emissiveRenderType, nonEmissiveTint, emissiveTint, packedLight);
    }

    protected void renderStaticMagazineFill(T item, ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, TranslucentFillModel fillModel, LimaColor color)
    {
        float fill = Math.min(LimaMathUtil.divideFloat(item.getAmmoLoaded(stack), item.getAmmoCapacity(stack)), 1f);
        if (fill > 0)
        {
            VertexConsumer buffer = bufferSource.getBuffer(LimaTechRenderTypes.POSITION_COLOR_TRANSLUCENT);
            fillModel.renderRotated(buffer, poseStack, color, fill);
        }
    }

    protected void renderAnimatedMagazineFill(T item, ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, TranslucentFillModel fillModel, LimaColor color, float partialTick, ClientWeaponControls controls)
    {
        if (!controls.getReloadTimer().isRunningClient())
        {
            renderStaticMagazineFill(item, stack, poseStack, bufferSource, fillModel, color);
        }
        else
        {
            float mul = controls.getReloadTimer().lerpProgressNotPaused(partialTick);

            VertexConsumer buffer = bufferSource.getBuffer(LimaTechRenderTypes.POSITION_COLOR_TRANSLUCENT);
            fillModel.renderRotated(buffer, poseStack, color, mul);
        }
    }
}