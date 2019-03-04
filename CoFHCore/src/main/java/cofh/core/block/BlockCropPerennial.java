package cofh.core.block;

import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import static cofh.lib.util.Constants.AGE_PERENNIAL;

public class BlockCropPerennial extends BlockCrop implements IGrowable, IPlantable {

	public BlockCropPerennial() {

		super(Material.PLANTS, EnumPlantType.Crop);
	}

	public BlockCropPerennial(Material blockMaterialIn, EnumPlantType type) {

		super(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), type);
	}

	public BlockCropPerennial(Material blockMaterialIn, MapColor blockMapColorIn, EnumPlantType type) {

		super(blockMaterialIn, blockMapColorIn, type);
	}

	@Override
	protected PropertyInteger getAgeProperty() {

		return AGE_PERENNIAL;
	}

	@Override
	public int getMaximumAge() {

		return 10;
	}

	@Override
	public int getPostHarvestAge() {

		return 8;
	}

}
