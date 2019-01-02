package com.minexd.zoot.rank.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank delete", permission = "zoot.admin.rank", async = true)
public class RankDeleteCommand {

	public void execute(CommandSender sender, Rank rank) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		rank.delete();

		sender.sendMessage(CC.GREEN + "You deleted the rank.");
	}

}
