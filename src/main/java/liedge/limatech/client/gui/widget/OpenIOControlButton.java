package liedge.limatech.client.gui.widget;

import liedge.limacore.client.gui.LimaMenuScreen;
import liedge.limatech.LimaTech;
import liedge.limatech.blockentity.base.BlockEntityInputType;
import liedge.limatech.registry.game.LimaTechNetworkSerializers;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.resources.ResourceLocation;

public class OpenIOControlButton extends LimaSidebarButton
{
    private static final ResourceLocation ITEMS_SPRITE = LimaTech.RESOURCES.location("sidebar_item_io");
    private static final ResourceLocation ENERGY_SPRITE = LimaTech.RESOURCES.location("sidebar_energy_io");
    private static final ResourceLocation FLUIDS_SPRITE = LimaTech.RESOURCES.location("sidebar_fluid_io");

    private static ResourceLocation spriteLocationForType(BlockEntityInputType type)
    {
        return switch (type)
        {
            case ITEMS -> ITEMS_SPRITE;
            case ENERGY -> ENERGY_SPRITE;
            case FLUIDS -> FLUIDS_SPRITE;
        };
    }

    private final LimaMenuScreen<?> parent;
    private final int buttonId;
    private final BlockEntityInputType inputType;

    public OpenIOControlButton(int x, int y, LimaMenuScreen<?> parent, int buttonId, BlockEntityInputType inputType)
    {
        super(x, y, inputType.translate(), spriteLocationForType(inputType));
        this.parent = parent;
        this.buttonId = buttonId;
        this.inputType = inputType;
        setTooltip(Tooltip.create(getMessage()));
    }

    @Override
    public void onPress(int button)
    {
        parent.sendCustomButtonData(buttonId, inputType, LimaTechNetworkSerializers.MACHINE_INPUT_TYPE);
    }
}