package com.minexd.zoot.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import com.minexd.zoot.util.json.JsonChain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketStaffRequest implements Packet {

	private String sentBy;
	private String reason;
	private String serverId;
	private String serverName;

	@Override
	public int id() {
		return 12;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("sentBy", sentBy)
				.addProperty("reason", reason)
				.addProperty("serverId", serverId)
				.addProperty("serverName", serverName)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		sentBy = object.get("sentBy").getAsString();
		reason = object.get("reason").getAsString();
		serverId = object.get("serverId").getAsString();
		serverName = object.get("serverName").getAsString();
	}

}
