package liedge.limatech.menu;

import liedge.limacore.inventory.menu.LimaMenu;
import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limacore.registry.LimaCoreNetworkSerializers;
import liedge.limacore.util.LimaBlockUtil;
import liedge.limatech.blockentity.base.IOController;
import liedge.limatech.blockentity.base.BlockEntityInputType;
import liedge.limatech.blockentity.base.SidedAccessBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.Objects;

public class IOControlMenu extends LimaMenu<IOControlMenu.MenuContext>
{
    private IOControlMenu(LimaMenuType<MenuContext, ?> type, int containerId, Inventory inventory, MenuContext menuContext)
    {
        super(type, containerId, inventory, menuContext);

        addPlayerInventory(DEFAULT_INV_X, DEFAULT_INV_Y);
        addPlayerHotbar(DEFAULT_INV_X, DEFAULT_HOTBAR_Y);
    }

    public IOController getIOControl()
    {
        return menuContext.holder.getIOController(menuContext.inputType);
    }

    @Override
    protected boolean quickMoveInternal(int index, ItemStack stack)
    {
        return false;
    }

    @Override
    public void defineDataWatchers(DataWatcherCollector collector)
    {
        collector.register(getIOControl().getOrCreateDataWatcher());
    }

    @Override
    protected void defineButtonEventHandlers(EventHandlerBuilder builder)
    {
        IOController ioControl = getIOControl();
        builder.handleUnitAction(0, menuContext.holder::returnToPrimaryMenuScreen);
        builder.handleAction(1, LimaCoreNetworkSerializers.DIRECTION, (sender, side) -> ioControl.cycleSideIO(side, true));
        builder.handleAction(2, LimaCoreNetworkSerializers.DIRECTION, (sender, side) -> ioControl.cycleSideIO(side, false));
        builder.handleUnitAction(3, sender -> ioControl.toggleAutoInput());
        builder.handleUnitAction(4, sender -> ioControl.toggleAutoOutput());
    }

    @Override
    protected IItemHandlerModifiable menuContainer()
    {
        throw new UnsupportedOperationException();
    }

    public static class MenuType extends LimaMenuType<MenuContext, IOControlMenu>
    {
        public MenuType(ResourceLocation registryId)
        {
            super(registryId, MenuContext.class, IOControlMenu::new);
        }

        @Override
        public MutableComponent getMenuTitle(Object uncheckedContext)
        {
            return checkContext(uncheckedContext).inputType.translate();
        }

        @Override
        public void encodeContext(MenuContext menuContext, RegistryFriendlyByteBuf net)
        {
            net.writeBlockPos(menuContext.holder.getAsLimaBlockEntity().getBlockPos());
            BlockEntityInputType.STREAM_CODEC.encode(net, menuContext.inputType);
        }

        @Override
        protected MenuContext decodeContext(RegistryFriendlyByteBuf net, Inventory inventory)
        {
            BlockPos pos = net.readBlockPos();
            SidedAccessBlockEntity holder = Objects.requireNonNull(LimaBlockUtil.getSafeBlockEntity(inventory.player.level(), pos, SidedAccessBlockEntity.class));
            BlockEntityInputType inputType = BlockEntityInputType.STREAM_CODEC.decode(net);

            return new MenuContext(holder, inputType);
        }

        @Override
        public boolean canPlayerKeepUsing(MenuContext menuContext, Player player)
        {
            return menuContext.holder.getAsLimaBlockEntity().canPlayerUse(player);
        }
    }

    public record MenuContext(SidedAccessBlockEntity holder, BlockEntityInputType inputType) {}
}