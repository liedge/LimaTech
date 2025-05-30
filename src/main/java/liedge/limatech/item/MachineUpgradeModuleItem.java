package liedge.limatech.item;

import liedge.limacore.lib.Translatable;
import liedge.limacore.util.LimaBlockUtil;
import liedge.limatech.LimaTechConstants;
import liedge.limatech.blockentity.base.UpgradableMachineBlockEntity;
import liedge.limatech.client.LimaTechLang;
import liedge.limatech.lib.upgrades.machine.MachineUpgrade;
import liedge.limatech.lib.upgrades.machine.MachineUpgradeEntry;
import liedge.limatech.lib.upgrades.machine.MachineUpgrades;
import liedge.limatech.registry.LimaTechRegistries;
import liedge.limatech.registry.game.LimaTechCreativeTabs;
import liedge.limatech.registry.game.LimaTechItems;
import liedge.limatech.registry.game.LimaTechSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.List;

import static liedge.limatech.registry.game.LimaTechDataComponents.MACHINE_UPGRADE_ENTRY;

public class MachineUpgradeModuleItem extends UpgradeModuleItem<MachineUpgrade, MachineUpgradeEntry>
{
    private static final Style STYLE = Style.EMPTY.withColor(0x4cc9bf);

    public static ItemStack createStack(Holder<MachineUpgrade> upgradeHolder, int upgradeRank)
    {
        ItemStack stack = new ItemStack(LimaTechItems.MACHINE_UPGRADE_MODULE.get());
        stack.set(MACHINE_UPGRADE_ENTRY, new MachineUpgradeEntry(upgradeHolder, upgradeRank));
        return stack;
    }

    public MachineUpgradeModuleItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        MachineUpgradeEntry entry = stack.get(entryComponentType());
        UpgradableMachineBlockEntity blockEntity = LimaBlockUtil.getSafeBlockEntity(level, context.getClickedPos(), UpgradableMachineBlockEntity.class);

        if (entry != null && blockEntity != null && player != null)
        {
            if (!level.isClientSide())
            {
                if (blockEntity.getUpgrades().canInstallUpgrade(blockEntity, entry))
                {
                    int previousRank = blockEntity.getUpgrades().getUpgradeRank(entry.upgrade());

                    MachineUpgrades newUpgrades = blockEntity.getUpgrades().toMutableContainer().set(entry).toImmutable();
                    blockEntity.setUpgrades(newUpgrades);

                    if (!player.getAbilities().instabuild)
                    {
                        stack.shrink(1);
                        if (previousRank > 0) ItemHandlerHelper.giveItemToPlayer(player, createStack(entry.upgrade(), previousRank));
                    }

                    player.displayClientMessage(LimaTechLang.UPGRADE_INSTALL_SUCCESS.translate().withStyle(moduleTypeStyle()), true);
                    level.playSound(null, context.getClickedPos(), LimaTechSounds.UPGRADE_INSTALL.get(), SoundSource.PLAYERS, 1f, 1f);
                }
                else
                {
                    player.displayClientMessage(LimaTechLang.UPGRADE_INSTALL_FAIL.translate().withStyle(LimaTechConstants.HOSTILE_ORANGE.chatStyle()), true);
                }
            }

            return InteractionResult.sidedSuccess(!level.isClientSide());
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public DataComponentType<MachineUpgradeEntry> entryComponentType()
    {
        return MACHINE_UPGRADE_ENTRY.get();
    }

    @Override
    protected ResourceKey<Registry<MachineUpgrade>> upgradeRegistryKey()
    {
        return LimaTechRegistries.Keys.MACHINE_UPGRADES;
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