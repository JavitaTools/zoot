package com.minexd.zoot.profile;

import com.minexd.zoot.Zoot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProfileRepository {

	private final Zoot zoot;
	private final Map<UUID, Profile> profiles;
	private MongoCollection<Document> collection;

	public ProfileRepository(Zoot zoot) {
		this.zoot = zoot;
		this.profiles = new HashMap<>();
		this.collection = zoot.getMongoDatabase().getCollection("profiles");
	}

	public void putInCache(Profile profile) {
		profiles.put(profile.getUuid(), profile);
	}

	public void removeFromCache(Profile profile) {
		profiles.remove(profile.getUuid());
	}

	public void removeFromCache(UUID uuid) {
		profiles.remove(uuid);
	}

	private Profile loadInternally(UUID uuid) {
		Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

		if (document == null) {
			return new Profile(uuid, null);
		}

		Profile profile = new Profile(uuid, document.getString("name"));

		
	}

	public CompletableFuture<Profile> loadByUsername(String username) {
		return CompletableFuture.supplyAsync(() -> {
			UUID uuid = Zoot.get().getRedisCache().getUuid(username);

			if (uuid != null) {
				return loadInternally(uuid);
			}

			return null;
		});
	}

	public CompletableFuture<Profile> loadByUuid(UUID uuid) {
		return CompletableFuture.supplyAsync(() -> loadInternally(uuid));
	}

	public Optional<Profile> getProfile(UUID uuid) {
		return Optional.ofNullable(profiles.get(uuid));
	}

}
