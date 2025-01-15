package liedge.limatech.menu;

import liedge.limacore.inventory.menu.LimaMenuType;
import liedge.limacore.util.LimaItemUtil;
import liedge.limatech.blockentity.SimpleRecipeMachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public abstract class SingleItemRecipeMenu<CTX extends SimpleRecipeMachineBlockEntity<?, ?>> extends SidedUpgradableMachineMenu<CTX>
{
    protected SingleItemRecipeMenu(LimaMenuType<CTX, ?> type, int containerId, Inventory inventory, CTX menuContext)
    {
        super(type, containerId, inventory, menuContext);

        addSlot(0, 8, 62);
        addSlot(1, 54, 34);
        addRecipeResultSlot(2, 106, 34, menuContext.machineRecipeType());

        addDefaultPlayerInventoryAndHotbar();
    }

    @Override
    public void defineDataWatchers(DataWatcherCollector collector)
    {
        menuContext.getEnergyStorage().keepAllPropertiesSynced(collector);
        menuContext.keepProcessAndDurationSynced(collector);
    }

    @Override
    protected boolean quickMoveInternal(int index, ItemStack stack)
    {
        if (index < 2)
        {
            return quickMoveToAllInventory(stack, false);
        }
        else if (index == 2)
        {
            return quickMoveToAllInventory(stack, true);
        }
        else
        {
            if (LimaItemUtil.hasEnergyCapability(stack))
            {
                return quickMoveToContainerSlot(stack, 0);
            }
            else
            {
                return quickMoveToContainerSlot(stack, 1);
            }
        }
    }
}