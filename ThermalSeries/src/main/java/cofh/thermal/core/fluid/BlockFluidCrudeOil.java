package cofh.thermal.core.fluid;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class BlockFluidCrudeOil extends BlockFluidCoFH {

	private static boolean effect = true;

	public static void config() {

		String category = "Fluid.CrudeOil";
		String comment = "If TRUE, Crude Oil will be flammable.";
		effect = ThermalSeries.config.getBoolean("Flammable", category, effect, comment);
	}

	public BlockFluidCrudeOil(Fluid fluid) {

		super(fluid, new MaterialLiquid(MapColor.BLACK), fluid.getName(), ID_THERMAL_SERIES);

		setQuantaPerBlock(6);
		setTickRate(10);

		setHardness(100F);
		setLightOpacity(7);
		setParticleColor(0.2F, 0.2F, 0.2F);

		config();
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {

		return effect ? 300 : 0;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {

		return 25;
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {

		return effect;
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing face) {

		return effect;
	}

}
