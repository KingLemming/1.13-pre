package cofh.lib.util;

import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Map;

/**
 * Created by covers1624 on 21/01/19.
 */
@Deprecated//Will be removed in 1.13 update, this system has been changed.
public class UnlistedMapProperty<K, V> implements IUnlistedProperty<Map<K, V>> {

	private final String name;

	public UnlistedMapProperty(String name) {

		this.name = name;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public boolean isValid(Map<K, V> value) {

		return true;
	}

	@Override
	@SuppressWarnings ("unchecked")
	public Class<Map<K, V>> getType() {

		Class<?> clazz = Map.class;//Java! wheeeee ;_;
		return (Class<Map<K, V>>) clazz;
	}

	@Override
	public String valueToString(Map<K, V> value) {

		StringBuilder builder = new StringBuilder("Map [ ");
		for (Map.Entry<K, V> entry : value.entrySet()) {
			builder.append(entry.getKey());
			builder.append(": ");
			builder.append(entry.getValue());
			builder.append(" ");
		}
		builder.append("]");
		return builder.toString();
	}
}
