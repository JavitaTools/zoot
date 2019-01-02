package com.minexd.zoot.rank.command;

import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank removepermission", "rank removeperm", "rank deleteperm", "rank delperm" },
             permission = "zoot.admin.rank",
             async = true)
public class RankRemovePermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (!rank.removePermission(permission)) {
			sender.sendMessage(CC.RED + "That rank does not have that permission.");
		} else {
			rank.save();
			sender.sendMessage(CC.GREEN + "Successfully removed permission from rank.");
		}
	}

}
