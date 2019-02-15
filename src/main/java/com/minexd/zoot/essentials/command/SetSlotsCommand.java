package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.util.BukkitReflection;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "setslots", async = true, permission = "zoot.setslots")
public class SetSlotsCommand {

	public void execute(CommandSender sender, int slots) {
		BukkitReflection.setMaxPlayers(Zoot.get().getServer(), slots);
		sender.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
	}

}
