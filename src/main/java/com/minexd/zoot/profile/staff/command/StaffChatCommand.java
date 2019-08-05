package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketStaffChat;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffchat", "sc" }, permission = "zoot.staff")
public class StaffChatCommand {

	public void execute(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getStaffOptions().staffChatModeEnabled(!profile.getStaffOptions().staffChatModeEnabled());

		player.sendMessage(profile.getStaffOptions().staffChatModeEnabled() ?
				CC.GREEN + "You are now talking in staff chat." : CC.RED + "You are no longer talking in staff chat.");
	}

	public void execute(Player player, String message) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (!profile.getStaffOptions().staffModeEnabled()) {
			player.sendMessage(CC.RED + "You are not in staff mode.");
			return;
		}

		Zoot.get().getPidgin().sendPacket(new PacketStaffChat(ZootAPI.getColoredName(player),
				Bukkit.getServerId(), message));
	}

}
