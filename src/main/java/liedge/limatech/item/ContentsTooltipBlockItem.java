package liedge.limatech.item;

import liedge.limacore.client.gui.TooltipLineConsumer;
import liedge.limacore.registry.game.LimaCoreDataComponents;
import liedge.limatech.util.LimaTechTooltipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class ContentsTooltipBlockItem extends BlockItem implements TooltipShiftHintItem
{
    private final boolean showEnergy;
    private final boolean showItems;
    private final boolean showOwner;

    public ContentsTooltipBlockItem(Block block, Properties properties, boolean showEnergy, boolean showItems, boolean showOwner)
    {
        super(block, properties);
        this.showEnergy = showEnergy;
        this.showItems = showItems;
        this.showOwner = showOwner;
    }

    public ContentsTooltipBlockItem(Block block, Properties properties, boolean showEnergy, boolean showItems)
    {
        this(block, properties, showEnergy, showItems, false);
    }

    @Override
    public void appendTooltipHintComponents(@Nullable Level level, ItemStack stack, TooltipLineConsumer consumer)
    {
        if (showEnergy) LimaTechTooltipUtil.appendEnergyOnlyTooltip(consumer, stack.getOrDefault(LimaCoreDataComponents.ENERGY, 0));

        if (showItems) LimaTechTooltipUtil.appendInventoryPreviewTooltip(consumer, stack);

        if (showOwner) consumer.accept(LimaTechTooltipUtil.makeOwnerComponent(stack.get(LimaCoreDataComponents.OWNER)).withStyle(ChatFormatting.GRAY));
    }
}