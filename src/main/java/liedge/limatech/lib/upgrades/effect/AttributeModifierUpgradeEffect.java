package liedge.limatech.lib.upgrades.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record AttributeModifierUpgradeEffect(Holder<Attribute> attribute, ResourceLocation modifierId, LevelBasedValue amount, AttributeModifier.Operation operation, EquipmentSlotGroup slotGroup) implements EffectTooltipProvider
{
    public static final Codec<AttributeModifierUpgradeEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Attribute.CODEC.fieldOf("attribute").forGetter(o -> o.attribute),
                    ResourceLocation.CODEC.fieldOf("modifier_id").forGetter(o -> o.modifierId),
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(o -> o.amount),
                    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(o -> o.operation),
                    EquipmentSlotGroup.CODEC.optionalFieldOf("slot_group", EquipmentSlotGroup.MAINHAND).forGetter(o -> o.slotGroup))
            .apply(instance, AttributeModifierUpgradeEffect::new));

    public static AttributeModifierUpgradeEffect constantMainHand(Holder<Attribute> attribute, ResourceLocation modifierId, float value, AttributeModifier.Operation operation)
    {
        return new AttributeModifierUpgradeEffect(attribute, modifierId, LevelBasedValue.constant(value), operation, EquipmentSlotGroup.MAINHAND);
    }

    public ItemAttributeModifiers.Entry makeModifierEntry(int upgradeRank)
    {
        return new ItemAttributeModifiers.Entry(attribute, makeModifier(upgradeRank), slotGroup);
    }

    private AttributeModifier makeModifier(int upgradeRank)
    {
        return new AttributeModifier(modifierId, amount.calculate(upgradeRank), operation);
    }

    @Override
    public Component getEffectTooltip(int upgradeRank)
    {
        return attribute.value().toComponent(makeModifier(upgradeRank), TooltipFlag.NORMAL).withStyle(ChatFormatting.DARK_GREEN);
    }
}