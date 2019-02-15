package com.minexd.zoot.essentials.command;

import com.minexd.zoot.Zoot;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "zoot debug", permission = "zoot.debug")
public class ZootDebugCommand {

	public void execute(CommandSender sender) {
		Zoot.get().setDebug(!Zoot.get().isDebug());
		sender.sendMessage("Debug: " + Zoot.get().isDebug());
	}

}
