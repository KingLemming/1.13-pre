package cofh.core.block.crops;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraftforge.common.EnumPlantType;

import static cofh.lib.util.Constants.AGE_TALL_PERENNIAL;

public class BlockCropTallPerennial extends BlockCropTall {

	protected int splitAge = 4;

	public BlockCropTallPerennial() {

		super();
	}

	public BlockCropTallPerennial(Material blockMaterialIn, EnumPlantType type) {

		super(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), type);
	}

	public BlockCropTallPerennial(Material blockMaterialIn, MapColor blockMapColorIn, EnumPlantType type) {

		super(blockMaterialIn, blockMapColorIn, type);
	}

	@Override
	protected PropertyInteger getAgeProperty() {

		return AGE_TALL_PERENNIAL;
	}

	@Override
	public int getMaximumAge() {

		return 9;
	}

	@Override
	public int getPostHarvestAge() {

		return 8;
	}

}
