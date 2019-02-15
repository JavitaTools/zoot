package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketClearPunishments;
import com.minexd.zoot.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "clearpunishments", permission = "zoot.admin.clearpunishments", async = true)
public class ClearPunishmentsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		profile.getPunishments().clear();
		profile.save();

		Zoot.get().getPidgin().sendPacket(new PacketClearPunishments(profile.getUuid()));

		sender.sendMessage(ChatColor.GREEN + "Cleared punishments of " + profile.getColoredUsername() +
		                   ChatColor.GREEN + "!");
	}

}
