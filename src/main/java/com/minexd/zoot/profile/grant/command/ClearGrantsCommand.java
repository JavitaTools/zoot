package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketClearGrants;
import com.minexd.zoot.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "cleargrants", permission = "zoot.admin.cleargrants", async = true)
public class ClearGrantsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		profile.getGrants().clear();
		profile.save();

		Zoot.get().getPidgin().sendPacket(new PacketClearGrants(profile.getUuid()));

		sender.sendMessage(ChatColor.GREEN + "Cleared grants of " + profile.getName() + "!");
	}

}
