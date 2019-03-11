package cofh.core.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.EnumPlantType;

public class BlockCropTall extends BlockCrop {

	public BlockCropTall() {

		super();
	}

	public BlockCropTall(Material blockMaterialIn, EnumPlantType type) {

		super(blockMaterialIn, blockMaterialIn.getMaterialMapColor(), type);
	}

	public BlockCropTall(Material blockMaterialIn, MapColor blockMapColorIn, EnumPlantType type) {

		super(blockMaterialIn, blockMapColorIn, type);
	}

}
