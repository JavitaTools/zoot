package com.minexd.zoot.profile.grant;

import com.google.gson.JsonObject;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.json.JsonDeserializer;
import java.util.UUID;

public class GrantJsonDeserializer implements JsonDeserializer<Grant> {

	@Override
	public Grant deserialize(JsonObject object) {
		Rank rank = Rank.getRankByUuid(UUID.fromString(object.get("rank").getAsString()));

		if (rank == null) {
			return null;
		}

		Grant grant = new Grant(
				UUID.fromString(object.get("uuid").getAsString()),
				rank,
				null,
				object.get("addedAt").getAsLong(),
				object.get("addedReason").getAsString(),
				object.get("duration").getAsLong()
		);

		if (!object.get("addedBy").isJsonNull()) {
			grant.setAddedBy(UUID.fromString(object.get("addedBy").getAsString()));
		}

		if (!object.get("removedBy").isJsonNull()) {
			grant.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
		}

		if (!object.get("removedAt").isJsonNull()) {
			grant.setRemovedAt(object.get("removedAt").getAsLong());
		}

		if (!object.get("removedReason").isJsonNull()) {
			grant.setRemovedReason(object.get("removedReason").getAsString());
		}

		if (!object.get("removed").isJsonNull()) {
			grant.setRemoved(object.get("removed").getAsBoolean());
		}

		return grant;
	}

}
