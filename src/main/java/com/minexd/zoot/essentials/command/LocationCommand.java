package com.minexd.zoot.essentials.command;

import com.minexd.zoot.util.LocationUtil;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "loc", permission = "zoot.loc")
public class LocationCommand {

	public void execute(Player player) {
		player.sendMessage(LocationUtil.serialize(player.getLocation()));
		System.out.println(LocationUtil.serialize(player.getLocation()));
	}

}
