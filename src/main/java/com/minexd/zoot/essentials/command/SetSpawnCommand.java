package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "setspawn", permission = "zoot.setspawn")
public class SetSpawnCommand {

	public void execute(Player player) {
		Zoot.get().getEssentials().setSpawn(player.getLocation());
		player.sendMessage(CC.GREEN + "You updated this world's spawn.");
	}

}
