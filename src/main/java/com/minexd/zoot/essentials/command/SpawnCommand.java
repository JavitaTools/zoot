package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "spawn", permission = "zoot.spawn")
public class SpawnCommand {

	public void execute(Player player) {
		Zoot.get().getEssentials().teleportToSpawn(player);
		player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
	}

}
