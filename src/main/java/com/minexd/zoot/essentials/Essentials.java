package com.minexd.zoot.essentials;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.essentials.event.SpawnTeleportEvent;
import com.minexd.zoot.util.LocationUtil;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class Essentials {

	private Zoot zoot;
	private Location spawn;

	public Essentials(Zoot zoot) {
		this.zoot = zoot;
		this.spawn = LocationUtil.deserialize(zoot.getMainConfig().getStringOrDefault("ESSENTIAL.SPAWN_LOCATION", null));
	}

	public void setSpawn(Location location) {
		spawn = location;

		if (spawn == null) {
			zoot.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", null);
		} else {
			zoot.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
		}

		try {
			zoot.getMainConfig().getConfiguration().save(zoot.getMainConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void teleportToSpawn(Player player) {
		Location location = spawn == null ? zoot.getServer().getWorlds().get(0).getSpawnLocation() : spawn;

		SpawnTeleportEvent event = new SpawnTeleportEvent(player, location);
		event.call();

		if (!event.isCancelled() && event.getLocation() != null) {
			player.teleport(event.getLocation());
		}
	}

	public int clearEntities(World world) {
		int removed = 0;

		for (Entity entity : world.getEntities()) {
			if (entity.getType() == EntityType.PLAYER) {
				continue;
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

	public int clearEntities(World world, EntityType... excluded) {
		int removed = 0;

		entityLoop:
		for (Entity entity : world.getEntities()) {
			if (entity instanceof Item) {
				removed++;
				entity.remove();
				continue entityLoop;
			}

			for (EntityType type : excluded) {
				if (entity.getType() == EntityType.PLAYER) {
					continue entityLoop;
				}

				if (entity.getType() == type) {
					continue entityLoop;
				}
			}

			removed++;
			entity.remove();
		}

		return removed;
	}

}
