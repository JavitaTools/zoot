package com.minexd.zoot.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(label = "showallplayers", permission = "zoot.showallplayers")
public class ShowAllPlayersCommand {

	public void execute(Player player) {
		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			player.showPlayer(otherPlayer);
		}
	}

}
