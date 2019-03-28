package cofh.thermal.expansion.block.machine.world;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.Utils;
import cofh.lib.util.comparison.BlockWrapper;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.block.AbstractTileType;
import cofh.thermal.core.block.machine.TileMachineWorld;
import cofh.thermal.core.init.FluidsTSeries;
import cofh.thermal.expansion.util.managers.machine.TapperManager;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.Set;

import static cofh.lib.util.Constants.TANK_MEDIUM;
import static cofh.lib.util.StorageGroup.INPUT;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.thermal.core.ThermalSeries.config;

public class TileMachineTapper extends TileMachineWorld {

	private static boolean requireFertilizer = false;
	private static int boostCycles = 8;
	private static int numLeaves = 3;
	private static final int DEFAULT_TIME_CONSTANT = 500;

	public static void config() {

		String category = "Device.Tapper";

		String comment = "If TRUE, the Arboreal Extractor will REQUIRE Phyto-Gro to operate.";
		requireFertilizer = config.getBoolean("Require Fertilizer", category, requireFertilizer, comment);

		comment = "Adjust this value to set the number of cycles Phyto-Gro lasts.";
		boostCycles = config.getInt("Fertilizer Duration", category, boostCycles, 2, 64, comment);
	}

	protected ItemStorageCoFH inputSlot = new ItemStorageCoFH();
	protected FluidStorageCoFH outputTank = new FluidStorageCoFH(TANK_MEDIUM);

	private FluidStack genFluid = new FluidStack(FluidsTSeries.fluidResin, 50);

	private boolean validTree;
	private BlockPos trunkPos;
	private BlockPos[] leafPos = new BlockPos[numLeaves];

	private int boostMult;
	private int boostTime;

	public TileMachineTapper(AbstractTileType type) {

		super(type);

		inventory.addSlot(inputSlot, INPUT);
		tankInv.addTank(outputTank, OUTPUT);

		timeConstant = DEFAULT_TIME_CONSTANT;
		timeOffset = MathHelper.RANDOM.nextInt(timeConstant);

		trunkPos = new BlockPos(pos);
		for (int i = 0; i < numLeaves; i++) {
			leafPos[i] = new BlockPos(pos);
		}
	}

	// region PROCESS
	@Override
	protected boolean canProcess() {

		return super.canProcess() && validTree;
	}

	@Override
	protected int processTick() {

		int ret = 0;
		genFluid = TapperManager.getFluid(world.getBlockState(trunkPos));
		if (boostTime > 0) {
			ret = outputTank.fill(new FluidStack(genFluid, genFluid.amount * boostMult), true);
			boostTime--;
		} else {
			boostMult = TapperManager.getFertilizerMultiplier(inputSlot.getItemStack());
			if (boostMult > 0) {
				ret = outputTank.fill(new FluidStack(genFluid, genFluid.amount * boostMult), true);
				boostTime = boostCycles - 1;
				inputSlot.modify(-1);
			} else if (!requireFertilizer) {
				ret = outputTank.fill(genFluid, true);
			}
		}
		updateValidity();
		return ret;
	}
	// endregion

	// region HELPERS
	@Override
	protected void updateValidity() {

		if (Utils.isClientWorld(world)) {
			return;
		}
		timeConstant = updateTimeConstant();
		if (validTree) {
			if (isTrunkBase(trunkPos)) {
				Set<BlockWrapper> leafSet = TapperManager.getLeaf(world.getBlockState(trunkPos));
				int leafCount = 0;
				for (int i = 0; i < numLeaves; i++) {
					IBlockState state = world.getBlockState(leafPos[i]);
					BlockWrapper target = new BlockWrapper(state.getBlock(), state.getBlock().getMetaFromState(state));

					if (leafSet.contains(target)) {
						leafCount++;
					}
				}
				if (leafCount >= numLeaves) {
					Iterable<MutableBlockPos> scanArea = BlockPos.getAllInBoxMutable(trunkPos, trunkPos.add(0, leafPos[0].getY() - trunkPos.getY(), 0));
					for (BlockPos scan : scanArea) {
						IBlockState state = world.getBlockState(scan);
						Material material = state.getMaterial();
						if (material == Material.GRASS || material == Material.GROUND || material == Material.ROCK) {
							validTree = false;
							cached = true;
							return;
						}
					}
					scanArea = BlockPos.getAllInBoxMutable(pos.add(0, 1, 0), pos.add(0, leafPos[0].getY() - pos.getY(), 0));
					for (BlockPos scan : scanArea) {
						if (isTapper(world.getBlockState(scan))) {
							validTree = false;
							cached = true;
							return;
						}
					}
					cached = true;
					genFluid = TapperManager.getFluid(world.getBlockState(trunkPos));
					return;
				}
			}
			validTree = false;
		}
		if (isTrunkBase(pos.west())) {
			trunkPos = pos.west();
		} else if (isTrunkBase(pos.east())) {
			trunkPos = pos.east();
		} else if (isTrunkBase(pos.north())) {
			trunkPos = pos.north();
		} else if (isTrunkBase(pos.south())) {
			trunkPos = pos.south();
		}
		if (!isTrunkBase(trunkPos)) {
			validTree = false;
			cached = true;
			return;
		}
		Iterable<MutableBlockPos> area = BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, Math.min(256 - pos.getY(), 40), 1));

		Set<BlockWrapper> leafSet = TapperManager.getLeaf(world.getBlockState(trunkPos));
		int leafCount = 0;

		for (BlockPos scan : area) {
			IBlockState state = world.getBlockState(scan);
			BlockWrapper target = new BlockWrapper(state.getBlock(), state.getBlock().getMetaFromState(state));

			if (leafSet.contains(target)) {
				leafPos[leafCount] = new BlockPos(scan);
				leafCount++;
				if (leafCount >= numLeaves) {
					break;
				}
			}
		}
		if (leafCount >= numLeaves) {
			Iterable<MutableBlockPos> scanArea = BlockPos.getAllInBoxMutable(trunkPos, trunkPos.add(0, leafPos[0].getY() - trunkPos.getY(), 0));

			for (BlockPos scan : scanArea) {
				IBlockState state = world.getBlockState(scan);
				Material material = state.getMaterial();

				if (material == Material.GRASS || material == Material.GROUND || material == Material.ROCK) {
					validTree = false;
					cached = true;
					return;
				}
			}
			scanArea = BlockPos.getAllInBoxMutable(pos.add(0, 1, 0), pos.add(0, leafPos[0].getY() - pos.getY(), 0));

			for (BlockPos scan : scanArea) {
				if (isTapper(world.getBlockState(scan))) {
					validTree = false;
					cached = true;
					return;
				}
			}
			validTree = true;
			genFluid = TapperManager.getFluid(world.getBlockState(trunkPos));
		}
		cached = true;
	}

	protected boolean isTrunkBase(BlockPos checkPos) {

		IBlockState state = world.getBlockState(checkPos.down());
		Material material = state.getMaterial();

		if (material != Material.GRASS && material != Material.GROUND && material != Material.ROCK) {
			return false;
		}
		return TapperManager.mappingExists(world.getBlockState(checkPos)) && TapperManager.mappingExists(world.getBlockState(checkPos.up())) && TapperManager.mappingExists(world.getBlockState(checkPos.up(2)));
	}

	protected int updateTimeConstant() {

		int constant = DEFAULT_TIME_CONSTANT / 2;
		Iterable<BlockPos.MutableBlockPos> area = BlockPos.getAllInBoxMutable(trunkPos.add(-1, 0, -1), trunkPos.add(1, 0, 1));

		for (BlockPos scan : area) {
			if (isTapper(world.getBlockState(scan))) {
				constant += DEFAULT_TIME_CONSTANT / 2;
			}
		}
		return MathHelper.clamp(constant, DEFAULT_TIME_CONSTANT, DEFAULT_TIME_CONSTANT * 2);
	}

	public boolean isTapper(IBlockState state) {

		return state.getBlock() == this.getBlockType();
	}
	// endregion

	// region NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);

		validTree = nbt.getBoolean("Tree");
		boostMult = nbt.getInteger("BoostMult");
		boostTime = nbt.getInteger("BoostTime");
		timeConstant = nbt.getInteger("TimeConstant");

		if (timeConstant <= 0) {
			timeConstant = DEFAULT_TIME_CONSTANT;
		}
		for (int i = 0; i < numLeaves; i++) {
			leafPos[i] = new BlockPos(nbt.getInteger("LeafX" + i), nbt.getInteger("LeafY" + i), nbt.getInteger("LeafZ" + i));
		}
		trunkPos = new BlockPos(nbt.getInteger("TrunkX"), nbt.getInteger("TrunkY"), nbt.getInteger("TrunkZ"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setBoolean("Tree", validTree);
		nbt.setInteger("BoostMult", boostMult);
		nbt.setInteger("BoostTime", boostTime);
		nbt.setInteger("TimeConstant", timeConstant);

		for (int i = 0; i < numLeaves; i++) {
			nbt.setInteger("LeafX" + i, leafPos[i].getX());
			nbt.setInteger("LeafY" + i, leafPos[i].getY());
			nbt.setInteger("LeafZ" + i, leafPos[i].getZ());
		}
		nbt.setInteger("TrunkX", trunkPos.getX());
		nbt.setInteger("TrunkY", trunkPos.getY());
		nbt.setInteger("TrunkZ", trunkPos.getZ());

		return nbt;
	}
	// endregion
}
