package cofh.lib.util.control;

import cofh.core.network.packet.PacketTransferControl;
import cofh.lib.util.Utils;
import net.minecraft.nbt.NBTTagCompound;

public class TransferControlModule implements ITransferControllable {

	protected ITransferControllableTile tile;

	protected boolean hasAutoInput;
	protected boolean hasAutoOutput;

	protected boolean enableAutoInput;
	protected boolean enableAutoOutput;

	public TransferControlModule(ITransferControllableTile tile) {

		this(tile, true, true);
	}

	public TransferControlModule(ITransferControllableTile tile, boolean hasAutoInput, boolean hasAutoOutput) {

		this.tile = tile;
		this.hasAutoInput = hasAutoInput;
		this.hasAutoOutput = hasAutoOutput;
	}

	// region NBT
	public TransferControlModule readFromNBT(NBTTagCompound nbt) {

		enableAutoInput = nbt.getBoolean("EnableIn");
		enableAutoOutput = nbt.getBoolean("EnableOut");

		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		nbt.setBoolean("EnableIn", enableAutoInput);
		nbt.setBoolean("EnableOut", enableAutoOutput);

		return nbt;
	}
	// endregion

	// region ITransferControl
	@Override
	public boolean hasTransferIn() {

		return hasAutoInput;
	}

	@Override
	public boolean hasTransferOut() {

		return hasAutoOutput;
	}

	@Override
	public boolean getTransferIn() {

		return hasTransferIn() && enableAutoInput;
	}

	@Override
	public boolean getTransferOut() {

		return hasTransferOut() && enableAutoOutput;
	}

	@Override
	public void setControl(boolean input, boolean output) {

		if (hasTransferIn()) {
			enableAutoInput = input;
		}
		if (hasTransferOut()) {
			enableAutoOutput = output;
		}
		if (Utils.isClientWorld(tile.world())) {
			PacketTransferControl.sendToServer(this.tile);
		} else {
			tile.onControlUpdate();
		}
	}
	// endregion
}
