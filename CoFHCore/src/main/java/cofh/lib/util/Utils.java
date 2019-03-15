package cofh.lib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class Utils {

	private Utils() {

	}

	public static boolean isClientWorld(World world) {

		return world.isRemote;
	}

	public static boolean isServerWorld(World world) {

		return !world.isRemote;
	}

	public static boolean isFakePlayer(Entity player) {

		return player instanceof FakePlayer;
	}

	public static String createPrettyJSON(String jsonString) {

		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.toJson(json);
	}

	// region CHAT UTILS

	// endregion

	// region ENTITY UTILS
	public static boolean dropItemStackIntoWorld(ItemStack stack, World world, Vec3d pos) {

		return dropItemStackIntoWorld(stack, world, pos, false);
	}

	public static boolean dropItemStackIntoWorldWithVelocity(ItemStack stack, World world, BlockPos pos) {

		return dropItemStackIntoWorld(stack, world, new Vec3d(pos), true);
	}

	public static boolean dropItemStackIntoWorldWithVelocity(ItemStack stack, World world, Vec3d pos) {

		return dropItemStackIntoWorld(stack, world, pos, true);
	}

	public static boolean dropItemStackIntoWorld(ItemStack stack, World world, Vec3d pos, boolean velocity) {

		if (stack.isEmpty()) {
			return false;
		}
		float x2 = 0.5F;
		float y2 = 0.0F;
		float z2 = 0.5F;

		if (velocity) {
			x2 = world.rand.nextFloat() * 0.8F + 0.1F;
			y2 = world.rand.nextFloat() * 0.8F + 0.1F;
			z2 = world.rand.nextFloat() * 0.8F + 0.1F;
		}
		EntityItem entity = new EntityItem(world, pos.x + x2, pos.y + y2, pos.z + z2, stack.copy());

		if (velocity) {
			entity.motionX = (float) world.rand.nextGaussian() * 0.05F;
			entity.motionY = (float) world.rand.nextGaussian() * 0.05F + 0.2F;
			entity.motionZ = (float) world.rand.nextGaussian() * 0.05F;
		} else {
			entity.motionY = -0.05F;
			entity.motionX = 0;
			entity.motionZ = 0;
		}
		world.spawnEntity(entity);

		return true;
	}

	public static boolean dropDismantleStackIntoWorld(ItemStack stack, World world, BlockPos pos) {

		if (stack.isEmpty()) {
			return false;
		}
		float f = 0.3F;
		double x2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double y2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double z2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		EntityItem dropEntity = new EntityItem(world, pos.getX() + x2, pos.getY() + y2, pos.getZ() + z2, stack);
		dropEntity.setPickupDelay(10);
		world.spawnEntity(dropEntity);

		return true;
	}

	public static boolean teleportEntityTo(Entity entity, BlockPos pos) {

		return teleportEntityTo(entity, pos.getX(), pos.getY(), pos.getZ());
	}

	public static boolean teleportEntityTo(Entity entity, double x, double y, double z) {

		if (entity instanceof EntityLivingBase) {
			return teleportEntityTo((EntityLivingBase) entity, x, y, z);
		} else {
			entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
			entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}
		return true;
	}

	public static boolean teleportEntityTo(EntityLivingBase entity, double x, double y, double z) {

		EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return false;
		}
		entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

		return true;
	}
	// endregion
}
