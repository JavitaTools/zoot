package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

@CommandMeta(label = "tpworld", permission = "zoot.tpworld")
public class TeleportWorldCommand {

	public void execute(Player player, String worldName) {
		World world = Bukkit.getWorld(worldName);

		if (world == null) {
			world = Bukkit.createWorld(new WorldCreator(worldName));
			player.sendMessage(CC.GOLD + "Generating new world \"" + worldName + "\"");
		}

		if (world == null) {
			player.sendMessage(CC.RED + "A world with that name does not exist.");
		} else {
			player.teleport(world.getSpawnLocation());
			player.sendMessage(CC.GOLD + "Teleported you to " + world.getName());
		}
	}

}
