package cofh.thermal.core.fluid;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.lib.util.Utils;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;

public class BlockFluidEnder extends BlockFluidCoFH {

	private static boolean effect = true;

	public static void config() {

		String category = "Fluids.Ender";
		String comment = "If TRUE, Resonant Ender will randomly teleport entities on contact.";
		effect = ThermalSeries.config.getBoolean("Effect", category, effect, comment);
	}

	public BlockFluidEnder(Fluid fluid) {

		super(fluid, new MaterialLiquid(MapColor.GREEN), fluid.getName(), ID_THERMAL_SERIES);

		setQuantaPerBlock(4);
		setTickRate(20);

		setHardness(2000F);
		setLightOpacity(7);
		setParticleColor(0.05F, 0.2F, 0.2F);

		config();
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {

		if (!effect || Utils.isClientWorld(world)) {
			return;
		}
		if (entity instanceof EntityItem || entity instanceof EntityXPOrb) {
			return;
		}
		if (world.getTotalWorldTime() % 8 == 0) {
			BlockPos randPos = pos.add(-8 + world.rand.nextInt(17), world.rand.nextInt(8), -8 + world.rand.nextInt(17));

			if (!world.getBlockState(randPos).getMaterial().isSolid()) {
				if (entity instanceof EntityLivingBase) {
					Utils.teleportEntityTo(entity, randPos);
				} else {
					entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
					entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
				}
			}
		}
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

		return definedFluid.getLuminosity();
	}

}
