package cofh.thermal.expansion.gui.client.machine;

import cofh.core.gui.GuiHelper;
import cofh.core.gui.element.ElementButton;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.block.machine.TileMachine;
import cofh.thermal.expansion.gui.container.machine.ContainerMachineExtruder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.StringHelper.generateTabInfo;

public class GuiMachineExtruder extends GuiMachine {

	public static final String TEX_PATH = ID_THERMAL_SERIES + ":textures/gui/machine/extruder.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	private ElementButton prevOutput;
	private ElementButton nextOutput;

	public GuiMachineExtruder(InventoryPlayer inventory, TileEntity tile) {

		super(new ContainerMachineExtruder(inventory, tile), (TileMachine) tile, generateTabInfo("tab.thermal.machine_extruder"));
		name = StringHelper.localize("tile.thermal.machine_extruder.name");
		texture = TEXTURE;
	}

	@Override
	public void initGui() {

		super.initGui();

		addElement(GuiHelper.createSmallFluidStorage(this, 32, 19, tile.getTank(0)));
		addElement(GuiHelper.createSmallFluidStorage(this, 56, 19, tile.getTank(1)));

		prevOutput = new ElementButton(this, 72, 54, "PrevOutput", 176, 0, 176, 14, 176, 28, 14, 14, TEX_PATH);
		nextOutput = new ElementButton(this, 108, 54, "NextOutput", 190, 0, 190, 14, 190, 28, 14, 14, TEX_PATH);

		addElement(prevOutput);
		addElement(nextOutput);
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();
	}
}

// TODO: Finish

//package cofh.thermalexpansion.gui.client.machine;
//
//		import cofh.core.gui.element.*;
//		import cofh.thermalexpansion.block.machine.TileExtruder;
//		import cofh.thermalexpansion.gui.client.GuiPoweredBase;
//		import cofh.thermalexpansion.gui.container.machine.ContainerExtruder;
//		import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
//		import cofh.thermalexpansion.gui.element.ElementSlotOverlay.SlotColor;
//		import cofh.thermalexpansion.gui.element.ElementSlotOverlay.SlotRender;
//		import cofh.thermalexpansion.gui.element.ElementSlotOverlay.SlotType;
//		import cofh.thermalexpansion.init.TEProps;
//		import net.minecraft.entity.player.InventoryPlayer;
//		import net.minecraft.tileentity.TileEntity;
//		import net.minecraft.util.ResourceLocation;
//		import net.minecraftforge.fluids.FluidRegistry;
//
//public class GuiExtruder extends GuiPoweredBase {
//
//	public static final String TEX_PATH = TEProps.PATH_GUI_MACHINE + "extruder.png";
//	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);
//
//	private TileExtruder myTile;
//
//	private ElementBase slotInputHot;
//	private ElementBase slotInputCold;
//	private ElementBase slotOutput;
//	private ElementFluid progressHot;
//	private ElementFluid progressCold;
//	private ElementDualScaled progressOverlay;
//	private ElementDualScaled speed;
//
//	private ElementFluidTank hotTank;
//	private ElementFluidTank coldTank;
//
//	private ElementButton prevOutput;
//	private ElementButton nextOutput;
//
//	public GuiExtruder(InventoryPlayer inventory, TileEntity tile) {
//
//		super(new ContainerExtruder(inventory, tile), tile, inventory.player, TEXTURE);
//
//		generateInfo("tab.thermalexpansion.machine.extruder");
//
//		myTile = (TileExtruder) tile;
//	}
//
//	@Override
//	public void initGui() {
//
//		super.initGui();
//
//		slotInputHot = addElement(new ElementSlotOverlay(this, 32, 19).setSlotInfo(SlotColor.BLUE, SlotType.TANK_SHORT, SlotRender.FULL));
//		slotInputCold = addElement(new ElementSlotOverlay(this, 56, 19).setSlotInfo(SlotColor.BLUE, SlotType.TANK_SHORT, SlotRender.FULL));
//		slotOutput = addElement(new ElementSlotOverlay(this, 130, 22).setSlotInfo(SlotColor.ORANGE, SlotType.OUTPUT, SlotRender.FULL));
//
//		if (!myTile.smallStorage()) {
//			addElement(new ElementEnergyStored(this, 8, 8, myTile.getEnergyStorage()));
//		}
//		hotTank = (ElementFluidTank) addElement(new ElementFluidTank(this, 32, 19, myTile.getTank(0)).setAlwaysShow(true).setSmall());
//		coldTank = (ElementFluidTank) addElement(new ElementFluidTank(this, 56, 19, myTile.getTank(1)).setAlwaysShow(true).setSmall());
//
//		progressHot = (ElementFluid) addElement(new ElementFluid(this, 85, 26).setFluid(FluidRegistry.LAVA).setSize(24, 8));
//		progressCold = (ElementFluid) addElement(new ElementFluid(this, 85, 34).setFluid(FluidRegistry.WATER).setSize(24, 8));
//		progressOverlay = (ElementDualScaled) addElement(new ElementDualScaled(this, 85, 26).setMode(1).setBackground(false).setSize(24, 16).setTexture(TEX_DROP_RIGHT, 64, 16));
//		speed = (ElementDualScaled) addElement(new ElementDualScaled(this, 44, 53).setSize(16, 16).setTexture(TEX_COMPACT, 32, 16));
//
//		prevOutput = new ElementButton(this, 72, 54, "PrevOutput", 176, 0, 176, 14, 176, 28, 14, 14, TEX_PATH).setToolTipLocalized(true);
//		nextOutput = new ElementButton(this, 108, 54, "NextOutput", 190, 0, 190, 14, 190, 28, 14, 14, TEX_PATH).setToolTipLocalized(true);
//
//		addElement(prevOutput);
//		addElement(nextOutput);
//	}
//
//	@Override
//	protected void updateElementInformation() {
//
//		super.updateElementInformation();
//
//		slotInputHot.setVisible(myTile.hasSideType(INPUT_ALL) || baseTile.hasSideType(OMNI));
//		slotInputCold.setVisible(myTile.hasSideType(INPUT_ALL) || baseTile.hasSideType(OMNI));
//		slotOutput.setVisible(myTile.hasSideType(OUTPUT_ALL) || baseTile.hasSideType(OMNI));
//
//		progressHot.setSize(myTile.getScaledProgress(PROGRESS), myTile.augmentNoWater() ? 16 : 8);
//		progressCold.setSize(myTile.getScaledProgress(PROGRESS), myTile.augmentNoWater() ? 0 : 8);
//		progressOverlay.setQuantity(myTile.getScaledProgress(PROGRESS));
//		speed.setQuantity(myTile.getScaledSpeed(SPEED));
//	}
//
//	@Override
//	public void handleElementButtonClick(String buttonName, int mouseButton) {
//
//		byte direction = 0;
//		float pitch = 0.7F;
//
//		if (buttonName.equalsIgnoreCase("PrevOutput")) {
//			direction -= 1;
//			pitch -= 0.1F;
//		} else if (buttonName.equalsIgnoreCase("NextOutput")) {
//			pitch += 0.1F;
//			direction += 1;
//		}
//		playClickSound(pitch);
//		myTile.setMode(direction);
//	}
//
//}
