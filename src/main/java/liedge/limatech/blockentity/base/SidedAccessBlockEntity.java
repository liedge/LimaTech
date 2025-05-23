package liedge.limatech.blockentity.base;

import liedge.limacore.inventory.menu.LimaMenuProvider;
import liedge.limatech.menu.IOControllerMenu;
import liedge.limatech.registry.game.LimaTechMenus;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

public interface SidedAccessBlockEntity extends SubMenuProviderBlockEntity
{
    /**
     * Retrieves the {@link IOController} for the given {@code inputType}. Implementations of this interface must
     * provide a valid controller for all {@link BlockEntityInputType} in {@link SidedAccessBlockEntityType#getValidInputTypes()}.
     * @param inputType The input type
     * @throws IllegalArgumentException If the block entity does not support the provided input type
     * @return The IO controller
     */
    IOController getIOController(BlockEntityInputType inputType) throws IllegalArgumentException;

    Direction getFacing();

    SidedAccessBlockEntityType<?> getType();

    void onIOControlsChanged(BlockEntityInputType inputType);

    default void openIOControlMenuScreen(Player player, BlockEntityInputType inputType)
    {
        IOControllerMenu.MenuContext context = new IOControllerMenu.MenuContext(this, inputType);
        LimaMenuProvider.openStandaloneMenu(player, LimaTechMenus.MACHINE_IO_CONTROL.get(), context);
    }
}