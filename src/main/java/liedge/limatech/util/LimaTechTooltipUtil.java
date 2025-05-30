package liedge.limatech.util;

import liedge.limacore.client.gui.TooltipLineConsumer;
import liedge.limacore.lib.Translatable;
import liedge.limatech.menu.tooltip.ItemGridTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.UsernameCache;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static liedge.limacore.capability.energy.LimaEnergyUtil.*;
import static liedge.limacore.registry.game.LimaCoreDataComponents.ITEM_CONTAINER;
import static liedge.limacore.util.LimaMathUtil.*;
import static liedge.limatech.LimaTech.RESOURCES;
import static liedge.limatech.LimaTechConstants.REM_BLUE;
import static liedge.limatech.client.LimaTechLang.*;

public final class LimaTechTooltipUtil
{
    public static final Translatable ALL_HOLDER_SET = RESOURCES.translationHolder("{}.any_holder_set");
    public static final Translatable AMBIGUOUS_HOLDER_SET = RESOURCES.translationHolder("{}.ambiguous_holder_set");

    private LimaTechTooltipUtil() {}

    public static void appendEnergyOnlyTooltip(TooltipLineConsumer consumer, int energy)
    {
        consumer.accept(INLINE_ENERGY.translateArgs(toEnergyString(energy)).withStyle(REM_BLUE.chatStyle()));
    }

    public static void appendEnergyWithCapacityTooltip(TooltipLineConsumer consumer, int energy, int capacity)
    {
        consumer.accept(INLINE_ENERGY.translateArgs(toEnergyStoredString(energy, capacity)).withStyle(REM_BLUE.chatStyle()));
    }

    public static void appendEnergyUsageTooltip(TooltipLineConsumer consumer, int energyUsage)
    {
        consumer.accept(INLINE_ENERGY_USAGE.translateArgs(toEnergyString(energyUsage)).withStyle(REM_BLUE.chatStyle()));
    }

    public static void appendEnergyUsagePerTickTooltip(TooltipLineConsumer consumer, int energyUsage)
    {
        consumer.accept(INLINE_ENERGY_USAGE.translateArgs(toEnergyPerTickString(energyUsage)).withStyle(REM_BLUE.chatStyle()));
    }

    public static void appendStorageEnergyTooltip(TooltipLineConsumer consumer, int energy, int capacity, int transferRate)
    {
        appendEnergyWithCapacityTooltip(consumer, energy, capacity);
        consumer.accept(INLINE_ENERGY_TRANSFER_RATE.translateArgs(toEnergyPerTickString(transferRate)).withStyle(REM_BLUE.chatStyle()));
    }

    public static void appendInventoryPreviewTooltip(TooltipLineConsumer consumer, ItemStack stack)
    {
        List<ItemStack> inventory = stack.getOrDefault(ITEM_CONTAINER, ItemContainerContents.EMPTY).nonEmptyStream().toList();
        if (!inventory.isEmpty())
        {
            consumer.accept(ITEM_INVENTORY_TOOLTIP.translate().withStyle(ChatFormatting.GRAY));
            consumer.accept(new ItemGridTooltip(inventory, 6, 1, true));
        }
        else
        {
            consumer.accept(EMPTY_ITEM_INVENTORY_TOOLTIP.translate().withStyle(ChatFormatting.GRAY));
        }
    }

    public static String formatFlatNumber(double value)
    {
        return value < 1000 ? FORMAT_2_ROUND_FLOOR.format(value) : FORMAT_COMMA_INT.format(value);
    }

    public static MutableComponent flatNumberWithSign(double value)
    {
        String formattedValue = formatFlatNumber(value);
        if (value >= 0) formattedValue = "+" + formattedValue;
        return Component.literal(formattedValue);
    }

    public static MutableComponent flatNumberWithoutSign(double value)
    {
        return Component.literal(formatFlatNumber(value));
    }

    public static MutableComponent percentageWithSign(double value)
    {
        String formattedValue = FORMAT_PERCENTAGE.format(value);
        if (value >= 0) formattedValue = "+" + formattedValue;
        return Component.literal(formattedValue);
    }

    public static MutableComponent percentageWithoutSign(double value)
    {
        return Component.literal(FORMAT_PERCENTAGE.format(value));
    }

    public static MutableComponent makeOwnerComponent(@Nullable UUID uuid)
    {
        if (uuid == null)
        {
            return INLINE_NO_OWNER_TOOLTIP.translate();
        }
        else
        {
            String username = UsernameCache.getLastKnownUsername(uuid);
            Component nameComponent = username != null ? Component.literal(username) : ComponentUtils.wrapInSquareBrackets(Component.literal(uuid.toString()));
            return INLINE_OWNER_TOOLTIP.translateArgs(nameComponent);
        }
    }

    public static MutableComponent translateHolderSet(HolderSet<?> set)
    {
        if (set instanceof AnyHolderSet<?>)
        {
            return ALL_HOLDER_SET.translate();
        }
        else if (set instanceof HolderSet.Named<?> named)
        {
            return Component.translatable(Tags.getTagTranslationKey(named.key()));
        }
        else
        {
            return AMBIGUOUS_HOLDER_SET.translate();
        }
    }
}