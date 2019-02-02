package cofh.thermal.core.init;

import cofh.core.fluid.BlockFluidCoFH;
import cofh.core.fluid.FluidCoFH;
import cofh.core.fluid.FluidPotion;
import cofh.thermal.core.fluid.BlockFluidEnder;
import cofh.thermal.core.fluid.BlockFluidGlowstone;
import cofh.thermal.core.fluid.BlockFluidRedstone;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.thermal.core.ThermalSeries.registerBlock;

public class FluidsTSeries {

	private FluidsTSeries() {

	}

	// region REGISTRATION
	public static void registerFluids() {

		createFluids();
		createFluidBlocks();
		createBuckets();
		refreshReferences();
	}

	public static void createFluids() {

		fluidSteam = new FluidCoFH("steam", ID_THERMAL_SERIES).setDensity(-1000).setViscosity(200).setTemperature(750).setGaseous(true);
		fluidExperience = new FluidCoFH("experience", ID_THERMAL_SERIES).setLuminosity(12).setDensity(-200).setViscosity(200).setGaseous(true).setRarity(EnumRarity.UNCOMMON);

		fluidMilk = new FluidCoFH("milk", ID_THERMAL_SERIES).setDensity(1000).setViscosity(1000);//.setColor(0xffededed);
		fluidBeetrootSoup = new FluidCoFH("beetroot_soup", ID_THERMAL_SERIES).setDensity(1500).setViscosity(1500);//.setColor(0xffbd282e);
		fluidMushroomStew = new FluidCoFH("mushroom_stew", ID_THERMAL_SERIES).setDensity(2000).setViscosity(2000);//.setColor(0xffb89371);

		fluidPotion = new FluidPotion("potion", ID_THERMAL_SERIES).setLuminosity(3).setDensity(500).setViscosity(1500).setRarity(EnumRarity.UNCOMMON);

		fluidRedstone = new FluidCoFH("redstone", ID_THERMAL_SERIES).setLuminosity(7).setDensity(1200).setViscosity(1500).setRarity(EnumRarity.UNCOMMON);//.setColor(0xff7d0404);
		fluidGlowstone = new FluidCoFH("glowstone", ID_THERMAL_SERIES).setLuminosity(15).setDensity(-500).setViscosity(100).setGaseous(true).setRarity(EnumRarity.UNCOMMON);//.setColor(0xfffdd404);
		fluidEnder = new FluidCoFH("ender", ID_THERMAL_SERIES).setLuminosity(3).setDensity(4000).setViscosity(2500).setRarity(EnumRarity.UNCOMMON);//.setColor(0xff083c3c);

		FluidRegistry.registerFluid(fluidSteam);
		FluidRegistry.registerFluid(fluidExperience);

		FluidRegistry.registerFluid(fluidMilk);
		FluidRegistry.registerFluid(fluidBeetrootSoup);
		FluidRegistry.registerFluid(fluidMushroomStew);

		FluidRegistry.registerFluid(fluidPotion);

		FluidRegistry.registerFluid(fluidRedstone);
		FluidRegistry.registerFluid(fluidGlowstone);
		FluidRegistry.registerFluid(fluidEnder);
	}

	public static void createFluidBlocks() {

		blockFluidRedstone = new BlockFluidRedstone(fluidRedstone);
		blockFluidGlowstone = new BlockFluidGlowstone(fluidGlowstone);
		blockFluidEnder = new BlockFluidEnder(fluidEnder);

		registerBlock("fluid_redstone", blockFluidRedstone);
		registerBlock("fluid_glowstone", blockFluidGlowstone);
		registerBlock("fluid_ender", blockFluidEnder);
	}

	public static void createBuckets() {

		FluidRegistry.addBucketForFluid(fluidSteam);
		FluidRegistry.addBucketForFluid(fluidExperience);

		//		FluidRegistry.addBucketForFluid(fluidMilk);
		//		FluidRegistry.addBucketForFluid(fluidBeetrootSoup);
		//		FluidRegistry.addBucketForFluid(fluidMushroomStew);

		//		FluidRegistry.addBucketForFluid(fluidPotion);

		FluidRegistry.addBucketForFluid(fluidRedstone);
		FluidRegistry.addBucketForFluid(fluidGlowstone);
		FluidRegistry.addBucketForFluid(fluidEnder);
	}

	public static void refreshReferences() {

		fluidSteam = FluidRegistry.getFluid("steam");
		fluidExperience = FluidRegistry.getFluid("experience");

		fluidMilk = FluidRegistry.getFluid("milk");
		fluidBeetrootSoup = FluidRegistry.getFluid("beetroot_soup");
		fluidMushroomStew = FluidRegistry.getFluid("mushroom_stew");

		fluidPotion = FluidRegistry.getFluid("potion");

		fluidRedstone = FluidRegistry.getFluid("redstone");
		fluidGlowstone = FluidRegistry.getFluid("glowstone");
		fluidEnder = FluidRegistry.getFluid("ender");
	}
	// endregion

	// region HELPERS
	public static boolean isPotion(FluidStack stack) {

		return stack != null && stack.getFluid().getName().equals(fluidPotion.getName());
	}

	public static FluidStack getPotion(int amount, PotionType type) {

		if (type == null || type == PotionTypes.EMPTY) {
			return null;
		}
		if (type == PotionTypes.WATER) {
			return new FluidStack(FluidRegistry.WATER, amount);
		}
		return addPotionToFluidStack(new FluidStack(fluidPotion, amount), type);
	}

	public static FluidStack addPotionToFluidStack(FluidStack stack, PotionType type) {

		ResourceLocation resourcelocation = PotionType.REGISTRY.getNameForObject(type);

		// NOTE: This can actually happen.
		if (resourcelocation == null) {
			return null;
		}
		if (type == PotionTypes.EMPTY) {
			if (stack.tag != null) {
				stack.tag.removeTag("Potion");
				if (stack.tag.hasNoTags()) {
					stack.tag = null;
				}
			}
		} else {
			if (stack.tag == null) {
				stack.tag = new NBTTagCompound();
			}
			stack.tag.setString("Potion", resourcelocation.toString());
		}
		return stack;
	}

	public static FluidStack getPotionFluid(int amount, ItemStack stack) {

		Item item = stack.getItem();

		if (item.equals(Items.POTIONITEM)) {
			return getPotion(amount, PotionUtils.getPotionFromItem(stack));
		}
		return null;
	}
	// endregion

	// region REFERENCES
	public static Fluid fluidSteam;
	public static Fluid fluidExperience;

	public static Fluid fluidMilk;
	public static Fluid fluidBeetrootSoup;
	public static Fluid fluidMushroomStew;

	public static Fluid fluidPotion;

	public static Fluid fluidRedstone;
	public static Fluid fluidGlowstone;
	public static Fluid fluidEnder;

	public static BlockFluidCoFH blockFluidRedstone;
	public static BlockFluidCoFH blockFluidGlowstone;
	public static BlockFluidCoFH blockFluidEnder;
	// endregion
}
