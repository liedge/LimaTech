package liedge.limatech.item;

import liedge.limacore.lib.Translatable;
import liedge.limatech.client.LimaTechLang;
import liedge.limatech.lib.upgradesystem.machine.MachineUpgrade;
import liedge.limatech.lib.upgradesystem.machine.MachineUpgradeEntry;
import liedge.limatech.registry.LimaTechCreativeTabs;
import liedge.limatech.registry.LimaTechItems;
import liedge.limatech.registry.LimaTechRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static liedge.limatech.registry.LimaTechDataComponents.MACHINE_UPGRADE_ENTRY;

public class MachineUpgradeModuleItem extends UpgradeModuleItem<MachineUpgrade, MachineUpgradeEntry>
{
    private static final Style STYLE = Style.EMPTY.withColor(0x4cc9bf);

    public static ItemStack createStack(HolderLookup.Provider registries, ResourceKey<MachineUpgrade> upgradeKey, int upgradeRank)
    {
        ItemStack stack = new ItemStack(LimaTechItems.MACHINE_UPGRADE_MODULE.get());
        Holder<MachineUpgrade> holder = registries.holderOrThrow(upgradeKey);
        stack.set(MACHINE_UPGRADE_ENTRY, new MachineUpgradeEntry(holder, upgradeRank));
        return stack;
    }

    public static ItemStack createStack(HolderLookup.Provider registries, ResourceKey<MachineUpgrade> upgradeKey)
    {
        return createStack(registries, upgradeKey, 1);
    }

    public MachineUpgradeModuleItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public DataComponentType<MachineUpgradeEntry> entryComponentType()
    {
        return MACHINE_UPGRADE_ENTRY.get();
    }

    @Override
    protected ResourceKey<Registry<MachineUpgrade>> upgradeRegistryKey()
    {
        return LimaTechRegistries.MACHINE_UPGRADES_KEY;
    }

    @Override
    protected ResourceLocation creativeTabId()
    {
        return LimaTechCreativeTabs.MACHINE_MODULES_TAB.getId();
    }

    @Override
    protected MachineUpgradeEntry createUpgradeEntry(Holder<MachineUpgrade> upgradeHolder, int upgradeRank)
    {
        return new MachineUpgradeEntry(upgradeHolder, upgradeRank);
    }

    @Override
    protected Translatable moduleTypeTooltip()
    {
        return LimaTechLang.MACHINE_UPGRADE_MODULE_TOOLTIP;
    }

    @Override
    protected Style moduleTypeStyle()
    {
        return STYLE;
    }

    @Override
    protected List<ItemStack> getAllCompatibleItems(MachineUpgrade upgrade)
    {
        return upgrade.supportedSet().stream().flatMap(type -> type.value().getValidBlocks().stream()).map(block -> block.asItem().getDefaultInstance()).toList();
    }
}