package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank addpermission", "rank addperm" }, permission = "zoot.admin.rank", async = true)
public class RankAddPermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (!rank.addPermission(permission)) {
			sender.sendMessage(CC.RED + "That rank already has that permission.");
		} else {
			rank.save();
			sender.sendMessage(CC.GREEN + "Successfully added permission to rank.");
		}
	}

}
