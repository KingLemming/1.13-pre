package cofh.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;

/**
 * Wraps standard packet buffer to provide additional read/write options.
 *
 * @author covers1624
 */
public class PacketBufferCoFH extends PacketBuffer {

	public PacketBufferCoFH(ByteBuf wrapped) {

		super(wrapped);
	}

	public void writeFluidStack(FluidStack stack) {

		NBTTagCompound tag = null;

		if (stack != null && FluidRegistry.getFluidName(stack) != null) {
			tag = stack.writeToNBT(new NBTTagCompound());
		}
		writeCompoundTag(tag);
	}

	public FluidStack readFluidStack() {

		NBTTagCompound tag;

		try {
			tag = readCompoundTag();
		} catch (IOException e) {
			throw new EncoderException(e);
		}
		if (tag != null) {
			return FluidStack.loadFluidStackFromNBT(tag);
		}
		return null;
	}

}
