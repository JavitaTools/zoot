package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "ranks", permission = "zoot.admin.rank")
public class RanksCommand {

	public void execute(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Ranks");

		for (Rank rank : Rank.getRanks().values()) {
			sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.RESET + rank.getColor() + rank.getDisplayName() +
			                   ChatColor.RESET +  " (Weight: " + rank.getWeight() + ")");
		}
	}

}
