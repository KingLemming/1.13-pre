package cofh.core.item;

import cofh.lib.util.Utils;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

import static cofh.core.util.CoreUtils.configDir;
import static cofh.lib.util.Constants.GROUP_RAILS;

public class ItemBlockRail extends ItemBlockCoFH {

	public ItemBlockRail(Block block) {

		this(block, GROUP_RAILS);
	}

	public ItemBlockRail(Block block, String group) {

		super(block, group);
	}

	// region IModelRegister
	@Override
	@SideOnly (Side.CLIENT)
	public void generateModelFiles() {

		String domain = getRegistryName().getResourceDomain();
		String path = getRegistryName().getResourcePath();

		try {
			File blockState = new File(configDir, "/dev/" + domain + "/blockstates/" + path + ".json");
			FileUtils.writeStringToFile(blockState, Utils.createPrettyJSON(getBlockstateString()), Charset.forName("UTF-8"));
		} catch (Throwable t) {
			// pokemon!
		}
	}

	@SideOnly (Side.CLIENT)
	public String getBlockstateString() {

		String group = getGroup().isEmpty() ? "" : getGroup() + "/";
		return "{\"forge_marker\":1,\"variants\":{\"shape\":{\"north_south\":{\"model\":\"rail_flat\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"}},\"east_west\":{\"model\":\"rail_flat\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"},\"y\":90},\"ascending_east\":{\"model\":\"rail_raised_ne\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"},\"y\":90},\"ascending_west\":{\"model\":\"rail_raised_sw\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"},\"y\":90},\"ascending_north\":{\"model\":\"rail_raised_ne\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"}},\"ascending_south\":{\"model\":\"rail_raised_sw\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"}},\"south_east\":{\"model\":\"rail_curved\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "_corner\"}},\"south_west\":{\"model\":\"rail_curved\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "_corner\"},\"y\":90},\"north_west\":{\"model\":\"rail_curved\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "_corner\"},\"y\":180},\"north_east\":{\"model\":\"rail_curved\",\"textures\":{\"rail\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "_corner\"},\"y\":270}},\"inventory\":[{\"model\":\"builtin/generated\",\"textures\":{\"layer0\":\"" + getRegistryName().getResourceDomain() + ":blocks/" + group + getRegistryName().getResourcePath() + "\"},\"transform\":\"forge:default-item\"}]}}";
	}
	// endregion
}
