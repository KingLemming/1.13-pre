package cofh.thermal.expansion.block.machine.world;

import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.block.machine.TileMachineWorld;
import cofh.thermal.expansion.init.MachinesTE;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.TANK_MEDIUM;
import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.lib.util.helpers.FluidHelper.isWater;
import static cofh.thermal.core.ThermalSeries.config;

public class TileMachineWaterGen extends TileMachineWorld {

	private static boolean infiniteSource = false;
	private static boolean passiveGeneration = false;
	private static int waterRate = TANK_MEDIUM;
	private static int waterRatePassive = waterRate / 50;
	private static final int DEFAULT_TIME_CONSTANT = 40;

	private static FluidStack water = new FluidStack(FluidRegistry.WATER, waterRate);
	private static FluidStack waterPassive = new FluidStack(FluidRegistry.WATER, waterRatePassive);

	public static void config() {

		String category = "Machines.WaterGen";

		String comment = "If TRUE, the Aqueous Accumulator will act as an Infinite Source and will also function in the Nether.";
		infiniteSource = config.getBoolean("Infinite Source", category, infiniteSource, comment);

		comment = "If TRUE, the Aqueous Accumulator will produce water very slowly even without adjacent source blocks.";
		passiveGeneration = config.getBoolean("Passive Generation", category, passiveGeneration, comment);
	}

	protected FluidStorageCoFH outputTank = new FluidStorageCoFH(TANK_MEDIUM);

	protected boolean inNether;
	protected int targetWater;

	public TileMachineWaterGen() {

		super(MachinesTE.WATER_GEN);

		tankInv.addTank(outputTank, OUTPUT);

		timeConstant = DEFAULT_TIME_CONSTANT;
		timeOffset = MathHelper.RANDOM.nextInt(timeConstant);
	}

	// region PROCESS
	@Override
	protected boolean canProcess() {

		return super.canProcess() && !inNether;
	}

	@Override
	protected int processTick() {

		if (!infiniteSource) {
			if (targetWater >= 2 || (world.isRaining() && world.canSeeSky(pos))) {
				return outputTank.fill(water, true);
			} else if (passiveGeneration) {
				return outputTank.fill(waterPassive, true);
			}
		}
		return 0;
	}
	// endregion

	// region HELPERS
	@Override
	protected void updateValidity() {

		inNether = BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.NETHER) && !infiniteSource;
		targetWater = 0;

		if (isWater(world.getBlockState(pos.down()))) {
			targetWater++;
		}
		if (isWater(world.getBlockState(pos.up()))) {
			targetWater++;
		}
		if (isWater(world.getBlockState(pos.west()))) {
			targetWater++;
		}
		if (isWater(world.getBlockState(pos.east()))) {
			targetWater++;
		}
		if (isWater(world.getBlockState(pos.north()))) {
			targetWater++;
		}
		if (isWater(world.getBlockState(pos.south()))) {
			targetWater++;
		}
	}
	// endregion
}
