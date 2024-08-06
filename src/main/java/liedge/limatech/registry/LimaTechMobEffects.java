package liedge.limatech.registry;

import liedge.limacore.lib.SimpleMobEffect;
import liedge.limatech.LimaTech;
import liedge.limatech.LimaTechConstants;
import liedge.limatech.entity.effect.CorrosiveMobEffect;
import liedge.limatech.entity.effect.FreezingMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class LimaTechMobEffects
{
    private LimaTechMobEffects() {}

    private static final DeferredRegister<MobEffect> MOB_EFFECTS = LimaTech.RESOURCES.deferredRegister(Registries.MOB_EFFECT);

    public static void initRegister(IEventBus bus)
    {
        MOB_EFFECTS.register(bus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> FREEZING = MOB_EFFECTS.register("freezing", FreezingMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> CORROSIVE = MOB_EFFECTS.register("corrosive", CorrosiveMobEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> NEURO = MOB_EFFECTS.register("neuro", id -> new SimpleMobEffect(MobEffectCategory.HARMFUL, LimaTechConstants.NEURO_BLUE.packedRGB())
            .addAttributeModifier(LimaTechAttributes.UNIVERSAL_STRENGTH, id.withSuffix(".universal_attack"), -0.33d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
}