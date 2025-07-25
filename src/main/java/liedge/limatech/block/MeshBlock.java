package liedge.limatech.block;

import liedge.limacore.blockentity.LimaBlockEntityType;
import liedge.limacore.inventory.menu.LimaMenuProvider;
import liedge.limacore.util.LimaBlockUtil;
import liedge.limatech.blockentity.MeshBlockEntity;
import liedge.limatech.registry.game.LimaTechBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MeshBlock extends BaseMeshBlock
{
    public MeshBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        // Mesh blocks aren't directly placeable
        return null;
    }

    @Override
    public @Nullable LimaBlockEntityType<?> getBlockEntityType(BlockState state)
    {
        return LimaTechBlockEntities.MESH_BLOCK.get();
    }

    @Override
    public @Nullable LimaMenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        BlockPos primaryPos = getPrimaryPos(level, pos, state);
        return primaryPos != null ? LimaBlockUtil.getSafeBlockEntity(level, primaryPos, LimaMenuProvider.class) : null;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
    {
        BlockPos primaryPos = getPrimaryPos(level, pos, state);

        if (primaryPos != null)
        {
            BlockState primaryState = level.getBlockState(primaryPos);
            if (primaryState.getBlock() instanceof PrimaryMeshBlock primaryMeshBlock)
                return primaryMeshBlock.getCloneItemStack(state, target, level, pos, player);
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
    {
        if (!state.is(newState.getBlock()))
        {
            BlockPos primaryPos = getPrimaryPos(level, pos, state);
            if (primaryPos != null)
            {
                BlockState primaryState = level.getBlockState(primaryPos);
                if (primaryState.getBlock() instanceof PrimaryMeshBlock)
                {
                    level.removeBlock(primaryPos, false);
                }
            }

            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        BlockPos primaryPos = getPrimaryPos(level, pos, state);
        if (primaryPos != null)
        {
            BlockState primaryState = level.getBlockState(primaryPos);
            if (primaryState.getBlock() instanceof PrimaryMeshBlock)
            {
                primaryState.getBlock().playerWillDestroy(level, primaryPos, primaryState, player);
                return state;
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid)
    {
        if (willHarvest) return true; // What does this even do?

        BlockPos primaryPos = getPrimaryPos(level, pos, state);
        if (primaryPos != null)
        {
            BlockState primaryState = level.getBlockState(primaryPos);
            if (primaryState.getBlock() instanceof PrimaryMeshBlock)
            {
                primaryState.onDestroyedByPlayer(level, primaryPos, player, false, fluid);
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, false, fluid);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool)
    {
        BlockPos primaryPos = getPrimaryPos(level, pos, state);

        if (primaryPos != null)
        {
            BlockState primaryState = level.getBlockState(primaryPos);
            if (primaryState.getBlock() instanceof PrimaryMeshBlock)
            {
                primaryState.getBlock().playerDestroy(level, player, primaryPos, primaryState, level.getBlockEntity(primaryPos), tool);
            }
        }
        else
        {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
        }

        level.removeBlock(pos, false);
    }

    @Override
    protected BlockState handleWrenchDismantle(Level level, BlockPos pos, BlockState state, @Nullable Player player, ItemStack tool, boolean simulate)
    {
        BlockPos primaryPos = getPrimaryPos(level, pos, state);
        if (primaryPos != null)
        {
            BlockState primaryState = level.getBlockState(primaryPos);
            if (primaryState.getBlock() instanceof PrimaryMeshBlock primaryMeshBlock)
            {
                primaryMeshBlock.handleWrenchDismantle(level, primaryPos, primaryState, player, tool, simulate);
                return null; // Block removal will be handled by primary block wrench removal
            }
        }

        return getFluidState(state).createLegacyBlock();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        VoxelShape shape = null;

        if (level instanceof LevelReader levelReader)
        {
            MeshBlockEntity blockEntity = LimaBlockUtil.getSafeBlockEntity(levelReader, pos, MeshBlockEntity.class);
            if (blockEntity != null) shape = blockEntity.getMeshShape(levelReader, state, pos);
        }

        return shape != null ? shape : Shapes.empty();
    }

    private @Nullable BlockPos getPrimaryPos(LevelReader level, BlockPos pos, BlockState state)
    {
        MeshBlockEntity blockEntity = LimaBlockUtil.getSafeBlockEntity(level, pos, MeshBlockEntity.class);
        return blockEntity != null ? blockEntity.getPrimaryPos(pos, state) : null;
    }
}