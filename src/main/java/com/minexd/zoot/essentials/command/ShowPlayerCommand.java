package com.minexd.zoot.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "showplayer", permission = "zoot.admin.showplayer")
public class ShowPlayerCommand {

	public void execute(Player player, Player target) {
		player.showPlayer(target);
	}

}
