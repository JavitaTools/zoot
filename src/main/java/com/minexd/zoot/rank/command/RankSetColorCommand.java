package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setcolor", permission = "zoot.admin.rank", async = true)
public class RankSetColorCommand {

	public void execute(CommandSender sender, Rank rank, String color) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		try {
			rank.setColor(ChatColor.valueOf(color));
			rank.save();
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(CC.RED + "That color is not valid.");
			return;
		}

		sender.sendMessage(CC.GREEN + "You updated the rank's color.");
	}

}
