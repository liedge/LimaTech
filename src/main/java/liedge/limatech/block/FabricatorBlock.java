package liedge.limatech.block;

import liedge.limacore.block.LimaEntityBlock;
import liedge.limacore.blockentity.LimaBlockEntityType;
import liedge.limacore.util.LimaCollectionsUtil;
import liedge.limatech.registry.LimaTechBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static liedge.limacore.util.LimaBlockUtil.rotateYClockwise;
import static liedge.limacore.util.LimaBlockUtil.rotationYFromDirection;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class FabricatorBlock extends LimaEntityBlock
{
    private static final VoxelShape REFERENCE_SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 0, 0, 1, 16, 1),
            Block.box(15, 0, 0, 16, 16, 1),
            Block.box(0, 0, 15, 16, 16, 16),
            Block.box(0, 15, 0, 16, 16, 1),
            Block.box(0, 15, 0, 1, 16, 16),
            Block.box(15, 15, 0, 16, 16, 16),
            Block.box(9, 1, 0, 15, 6, 1));

    private static final Map<Direction, VoxelShape> SHAPE_MAP = Direction.Plane.HORIZONTAL.stream().collect(LimaCollectionsUtil.toUnmodifiableEnumMap(Direction.class, side -> rotateYClockwise(REFERENCE_SHAPE, rotationYFromDirection(side))));

    public FabricatorBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public @Nullable LimaBlockEntityType<?> getBlockEntityType(BlockState blockState)
    {
        return LimaTechBlockEntities.FABRICATOR.get();
    }

    @Override
    public boolean shouldTickServer(BlockState state)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx)
    {
        return SHAPE_MAP.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return defaultBlockState().setValue(HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(HORIZONTAL_FACING);
    }
}