package com.minexd.zoot.chat.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "slowchat", permission = "zoot.staff.slowchat")
public class SlowChatCommand {

	public void execute(CommandSender sender) {
		Zoot.get().getChat().togglePublicChatDelay();

		String senderName;

		if (sender instanceof Player) {
			senderName = ZootAPI.getColoredName((Player) sender);
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Zoot.get().getChat().getDelayTime() == 1 ? "" : "s";

		if (Zoot.get().getChat().isPublicChatDelayed()) {
			Bukkit.broadcastMessage(Locale.DELAY_CHAT_ENABLED_BROADCAST.format(senderName,
					Zoot.get().getChat().getDelayTime(), context));
		} else {
			Bukkit.broadcastMessage(Locale.DELAY_CHAT_DISABLED_BROADCAST.format(senderName));
		}
	}

	public void execute(CommandSender sender, Integer seconds) {
		if (seconds < 0 || seconds > 60) {
			sender.sendMessage(ChatColor.RED + "A delay can only be between 1-60 seconds.");
			return;
		}

		String context = seconds == 1 ? "" : "s";

		sender.sendMessage(ChatColor.GREEN + "You have updated the chat delay to " + seconds + " second" + context + ".");
		Zoot.get().getChat().setDelayTime(seconds);
	}

}
