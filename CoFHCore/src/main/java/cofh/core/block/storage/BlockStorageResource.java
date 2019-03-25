package cofh.core.block.storage;

import cofh.core.block.BlockCoFH;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import static cofh.lib.util.Constants.TOOL_PICKAXE;

public class BlockStorageResource extends BlockCoFH {

	public BlockStorageResource() {

		this(Material.ROCK.getMaterialMapColor());
	}

	public BlockStorageResource(MapColor color) {

		super(Material.ROCK, color);

		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);

		setHarvestParams(TOOL_PICKAXE, 0);
	}

}
