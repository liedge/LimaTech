package liedge.limatech.blockentity.base;

import com.google.common.collect.ImmutableMap;
import liedge.limacore.blockentity.LimaBlockEntity;
import liedge.limacore.blockentity.LimaBlockEntityType;
import liedge.limacore.util.LimaCollectionsUtil;
import liedge.limacore.util.LimaRegistryUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.UnaryOperator;

public final class SidedAccessBlockEntityType<BE extends LimaBlockEntity> extends LimaBlockEntityType<BE>
{
    private final Map<BlockEntityInputType, SidedAccessRules> accessRules;
    private final Set<BlockEntityInputType> validInputTypes;

    public SidedAccessBlockEntityType(BlockEntitySupplier<BE> factory, Set<Block> validBlocks, Map<BlockEntityInputType, SidedAccessRules> accessRules)
    {
        super(factory, validBlocks);
        this.accessRules = accessRules;
        this.validInputTypes = Collections.unmodifiableSet(accessRules.keySet());
    }

    public Collection<BlockEntityInputType> getValidInputTypes()
    {
        return validInputTypes;
    }

    public SidedAccessRules getSideAccessRules(BlockEntityInputType inputType)
    {
        return Objects.requireNonNull(accessRules.get(inputType), () -> String.format("Block entity type %s does not support input type %s", LimaRegistryUtil.getNonNullRegistryId(this, BuiltInRegistries.BLOCK_ENTITY_TYPE), inputType));
    }

    public static class Builder<BE extends LimaBlockEntity> extends AbstractBuilder<BE, SidedAccessBlockEntityType<BE>, Builder<BE>>
    {
        public static <BE extends LimaBlockEntity> Builder<BE> builder(BlockEntitySupplier<BE> factory)
        {
            return new Builder<>(factory);
        }

        private final Map<BlockEntityInputType, SidedAccessRules> ruleMap = new EnumMap<>(BlockEntityInputType.class);

        private Builder(BlockEntitySupplier<BE> factory)
        {
            super(factory);
        }

        public Builder<BE> withSideRules(BlockEntityInputType inputType, SidedAccessRules rules)
        {
            LimaCollectionsUtil.putNoDuplicates(ruleMap, inputType, rules);
            return this;
        }

        public Builder<BE> withSideRules(BlockEntityInputType inputType, UnaryOperator<SidedAccessRules.Builder> builder)
        {
            return withSideRules(inputType, builder.apply(SidedAccessRules.builder()).build());
        }

        @Override
        protected SidedAccessBlockEntityType<BE> build(BlockEntitySupplier<BE> factory, Set<Block> validBlocks)
        {
            return new SidedAccessBlockEntityType<>(factory, validBlocks, ImmutableMap.copyOf(ruleMap));
        }
    }
}