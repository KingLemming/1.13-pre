package cofh.core.network.packet;

import cofh.core.network.PacketHandler;

/**
 * Created by covers1624 on 21/09/18.
 */
public abstract class PacketBase<P extends PacketBase<P>> implements IPacket<P> {

	private final int id;
	private final PacketHandler handler;
	private boolean shouldCompress = false;

	protected PacketBase(int id, PacketHandler handler) {

		this.id = id;
		this.handler = handler;
	}

	@Override
	public byte getId() {

		return (byte) id;
	}

	@Override
	public PacketHandler getHandler() {

		return handler;
	}

	@Override
	public boolean shouldCompress() {

		return shouldCompress;
	}

	@Override
	@SuppressWarnings ("unchecked")
	public P compress() {

		shouldCompress = true;
		return (P) this;
	}

}
