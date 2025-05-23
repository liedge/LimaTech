package liedge.limatech.item.tool;

import liedge.limatech.item.LimaTechItemAbilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EnergyWrenchItem extends EnergyBaseToolItem
{
    public EnergyWrenchItem(Properties properties)
    {
        super(properties, 0f);
    }

    @Override
    protected Set<ItemAbility> getAvailableAbilities()
    {
        return LimaTechItemAbilities.WRENCH_ABILITIES;
    }

    @Override
    protected InteractionResult useToolOn(UseOnContext context, Level level, BlockPos pos, BlockState state, @Nullable Player player, ItemStack stack)
    {
        // Determine tool action
        boolean isSneaking = player != null && player.isShiftKeyDown();
        ItemAbility ability = isSneaking ? LimaTechItemAbilities.WRENCH_DISMANTLE : LimaTechItemAbilities.WRENCH_ROTATE;

        // See if we actually modified the state
        BlockState modified = state.getToolModifiedState(context, ability, false);
        if (modified == null) return InteractionResult.PASS;

        Holder<GameEvent> gameEvent = isSneaking ? GameEvent.BLOCK_DESTROY : GameEvent.BLOCK_CHANGE;

        if (!level.isClientSide())
        {
            level.setBlock(pos, modified, Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(gameEvent, pos, GameEvent.Context.of(player, modified));

            if (isSneaking) consumeActionEnergy(player, stack); // Use energy for dismantling only
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}