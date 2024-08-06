package liedge.limatech.entity.effect;

import liedge.limatech.LimaTechConstants;
import liedge.limatech.registry.LimaTechParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FreezingMobEffect extends MobEffect
{
    public FreezingMobEffect(ResourceLocation id)
    {
        super(MobEffectCategory.HARMFUL, LimaTechConstants.FREEZE_LIGHT_BLUE.packedRGB());
        addAttributeModifier(Attributes.ATTACK_SPEED, id.withSuffix(".attack_speed"), -0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, id.withSuffix(".move_speed"), -0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.FLYING_SPEED, id.withSuffix(".fly_speed"), -0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, id.withSuffix(".dig_speed"), -0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier)
    {
        if (livingEntity.canFreeze())
        {
            int ticksFrozen = livingEntity.getTicksRequiredToFreeze() + 10;
            livingEntity.setTicksFrozen(ticksFrozen);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier)
    {
        return true;
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect)
    {
        return LimaTechParticles.FREEZE_SNOWFLAKE.get();
    }
}