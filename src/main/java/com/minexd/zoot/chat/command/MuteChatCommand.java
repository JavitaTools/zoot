package com.minexd.zoot.chat.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.profile.Profile;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "mutechat", permission = "zoot.staff.mutechat")
public class MuteChatCommand {

	public void execute(CommandSender sender) {
		Zoot.get().getChat().togglePublicChatMute();

		String senderName;

		if (sender instanceof Player) {
			Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
			senderName = profile.getActiveRank().getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Zoot.get().getChat().isPublicChatMuted() ? "muted" : "unmuted";

		Bukkit.broadcastMessage(Locale.MUTE_CHAT_BROADCAST.format(context, senderName));
	}

}
