package cofh.enstoragement.block;

import cofh.core.block.TileCoFH;
import cofh.enstoragement.gui.client.GuiStrongbox;
import cofh.enstoragement.gui.container.ContainerStrongbox;
import cofh.lib.inventory.InventoryCoFH;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class TileStrongbox extends TileCoFH implements ITickable {

	protected static final int TIME_CONSTANT = 200;

	protected EnumFacing facing = EnumFacing.SOUTH;
	protected InventoryCoFH inventory;

	protected int enchantHolding;
	protected int offset;

	/* CLIENT */
	protected float prevLidAngle;
	protected float lidAngle;
	protected int numUsingPlayers;

	public TileStrongbox(int size) {

		inventory = new InventoryCoFH(this, size);
		offset = MathHelper.RANDOM.nextInt(TIME_CONSTANT);
	}

	@Override
	public boolean hasClientUpdate() {

		return true;
	}

	//	@Override
	//	public boolean onWrench(EntityPlayer player, EnumFacing side) {
	//
	//		return rotateBlock();
	//	}

	@Override
	public boolean receiveClientEvent(int id, int type) {

		if (id == 1) {
			numUsingPlayers = type;
			return true;
		}
		return false;
	}

	@Override
	public void update() {

		getNumPlayers();

		if (numUsingPlayers > 0 && !world.isRemote && world.getTotalWorldTime() % 200 == 0) {
			world.addBlockEvent(pos, getBlockType(), 1, numUsingPlayers);
		}
		prevLidAngle = lidAngle;
		lidAngle = MathHelper.approachLinear(lidAngle, numUsingPlayers > 0 ? 1F : 0F, 0.1F);

		if (prevLidAngle >= 0.5 && lidAngle < 0.5) {
			world.playSound(null, pos, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		} else if (prevLidAngle == 0 && lidAngle > 0) {
			world.playSound(null, pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}

	// region HELPERS
	public InventoryCoFH getInventory() {

		return inventory;
	}

	public double getRadianLidAngle(float f) {

		float angle = MathHelper.interpolate(prevLidAngle, lidAngle, f);
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		return angle * Math.PI * -0.5;
	}

	public void getNumPlayers() {

		if (Utils.isClientWorld(world)) {
			return;
		}
		if (numUsingPlayers != 0 && (world.getTotalWorldTime() + offset) % TIME_CONSTANT == 0) {
			numUsingPlayers = 0;
			float dist = 5.0F;
			for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX() - dist, pos.getY() - dist, pos.getZ() - dist, pos.getX() + 1 + dist, pos.getY() + 1 + dist, pos.getZ() + 1 + dist))) {
				if (player.openContainer instanceof ContainerStrongbox) {
					if (((ContainerStrongbox) player.openContainer).getTile() == this) {
						numUsingPlayers++;
					}
				}
			}
		}
	}
	// endregion

	// region GUI
	@Override
	public Object getGuiClient(InventoryPlayer inventory) {

		return new GuiStrongbox(inventory, this);
	}

	@Override
	public Object getGuiServer(InventoryPlayer inventory) {

		return new ContainerStrongbox(inventory, this);
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);
		inventory.readFromNBT(nbt);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);
		inventory.writeToNBT(nbt);
		return nbt;
	}
	// endregion
}
