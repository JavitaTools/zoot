package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setsuffix", permission = "zoot.admin.rank", async = true)
public class RankSetSuffixCommand {

	public void execute(CommandSender sender, Rank rank, String suffix) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		rank.setSuffix(CC.translate(suffix));
		rank.save();

		sender.sendMessage(CC.GREEN + "You updated the rank's suffix.");
	}

}
