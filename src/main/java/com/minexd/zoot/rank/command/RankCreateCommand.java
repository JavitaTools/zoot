package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank create", permission = "zoot.admin.rank", async = true)
public class RankCreateCommand {

	public void execute(CommandSender sender, String name) {
		if (Rank.getRankByDisplayName(name) != null) {
			sender.sendMessage(CC.RED + "A rank with that name already exists.");
			return;
		}

		Rank rank = new Rank(name);
		rank.save();

		sender.sendMessage(CC.GREEN + "You created a new rank.");
	}

}
