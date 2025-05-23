package liedge.limatech.lib.upgrades.effect.equipment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import liedge.limacore.util.LimaMathUtil;
import liedge.limatech.client.LimaTechLang;
import liedge.limatech.lib.upgrades.effect.EffectTooltipProvider;
import liedge.limatech.util.LimaTechTooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public record MiningRuleUpgradeEffect(Optional<HolderSet<Block>> effectiveBlocks, Optional<HolderSet<Block>> deniedBlocks, Optional<Float> miningSpeed, int priority) implements EffectTooltipProvider, Comparable<MiningRuleUpgradeEffect>
{
    public static final Codec<MiningRuleUpgradeEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("effective_blocks").forGetter(MiningRuleUpgradeEffect::effectiveBlocks),
            RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("denied_blocks").forGetter(MiningRuleUpgradeEffect::deniedBlocks),
            Codec.floatRange(1f, 99f).optionalFieldOf("mining_speed").forGetter(MiningRuleUpgradeEffect::miningSpeed),
            Codec.intRange(1, 99).fieldOf("priority").forGetter(MiningRuleUpgradeEffect::priority))
            .apply(instance, MiningRuleUpgradeEffect::new));

    public static MiningRuleUpgradeEffect miningLevelAndSpeed(HolderSet<Block> deniedBlocks, float miningSpeed, int priority)
    {
        return new MiningRuleUpgradeEffect(Optional.empty(), Optional.of(deniedBlocks), Optional.of(miningSpeed), priority);
    }

    @Override
    public int compareTo(@NotNull MiningRuleUpgradeEffect o)
    {
        return Integer.compare(o.priority, this.priority);
    }

    @Override
    public void appendEffectLines(int upgradeRank, Consumer<Component> linesConsumer)
    {
        effectiveBlocks.ifPresent(set -> linesConsumer.accept(LimaTechLang.MINING_EFFECTIVE_BLOCKS_EFFECT.translateArgs(LimaTechTooltipUtil.translateHolderSet(set))));
        miningSpeed.ifPresent(speed -> linesConsumer.accept(LimaTechLang.MINING_BASE_SPEED_EFFECT.translateArgs(Component.literal(LimaMathUtil.FORMAT_2_ROUND_FLOOR.format(speed)).withStyle(ChatFormatting.AQUA))));
    }
}