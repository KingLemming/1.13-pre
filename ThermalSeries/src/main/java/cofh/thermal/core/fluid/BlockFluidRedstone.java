package cofh.thermal.core.fluid;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class BlockFluidRedstone extends BlockFluidCoFH {

	private static boolean effect = true;

	public static void config() {

		String category = "Fluids.Redstone";
		String comment = "If TRUE, Destabilized Redstone will emit a redstone signal proportional to its quanta (level).";
		effect = ThermalSeries.config.getBoolean("Effect", category, effect, comment);
	}

	public BlockFluidRedstone(Fluid fluid) {

		super(fluid, new MaterialLiquid(MapColor.RED), fluid.getName(), ID_THERMAL_SERIES);

		setQuantaPerBlock(8);
		setTickRate(5);

		setHardness(100F);
		setLightOpacity(2);
		setParticleColor(0.4F, 0.0F, 0.0F);

		config();
	}

	@Override
	public boolean canProvidePower(IBlockState state) {

		return effect;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

		return effect ? (int) (getQuantaPercentage(blockAccess, pos) * 15) : 0;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

		return definedFluid.getLuminosity();
	}

}
