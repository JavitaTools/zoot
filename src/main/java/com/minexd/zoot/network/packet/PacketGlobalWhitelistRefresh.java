package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketGlobalWhitelistRefresh implements Packet {

	@Override
	public int id() {
		return 10;
	}

	@Override
	public JsonObject serialize() {
		return new JsonObject();
	}

	@Override
	public void deserialize(JsonObject object) {

	}

}
