package liedge.limatech.lib.upgrades.effect.equipment;

import com.mojang.serialization.MapCodec;
import liedge.limacore.lib.damage.LimaCoreDamageComponents;
import liedge.limatech.client.LimaTechLang;
import liedge.limatech.registry.game.LimaTechEquipmentUpgradeEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Arrays;
import java.util.List;

public record DynamicDamageTagUpgradeEffect(List<TagKey<DamageType>> tags) implements EquipmentUpgradeEffect
{
    public static final MapCodec<DynamicDamageTagUpgradeEffect> CODEC = TagKey.codec(Registries.DAMAGE_TYPE).listOf().fieldOf("tags").xmap(DynamicDamageTagUpgradeEffect::new, DynamicDamageTagUpgradeEffect::tags);

    public static DynamicDamageTagUpgradeEffect of(TagKey<DamageType> key)
    {
        return new DynamicDamageTagUpgradeEffect(List.of(key));
    }

    @SafeVarargs
    public static DynamicDamageTagUpgradeEffect of(TagKey<DamageType>... keys)
    {
        return new DynamicDamageTagUpgradeEffect(Arrays.asList(keys));
    }

    @Override
    public void applyEquipmentEffect(ServerLevel level, Entity entity, int upgradeRank, LootContext context)
    {
        DamageSource damageSource = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        if (damageSource != null) damageSource.mergeSet(LimaCoreDamageComponents.DYNAMIC_TAGS, tags);
    }

    @Override
    public MapCodec<? extends EquipmentUpgradeEffect> codec()
    {
        return LimaTechEquipmentUpgradeEffects.DYNAMIC_DAMAGE_TAG_EQUIPMENT_EFFECT.get();
    }

    @Override
    public Component getEffectTooltip(int upgradeRank)
    {
        Component tagsComponent = ComponentUtils.formatList(tags, o -> Component.translatable(LimaTechLang.namedDamageTagKey(o)));
        return LimaTechLang.DYNAMIC_DAMAGE_TAG_EFFECT.translateArgs(tagsComponent).withStyle(ChatFormatting.AQUA);
    }
}