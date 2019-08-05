package com.minexd.zoot.network.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.network.packet.PacketStaffRequest;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.Cooldown;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandMeta(label = { "request", "helpop" }, async = true)
public class RequestCommand {

	public void execute(Player player, @CPL("reason") String reason) {
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (!profile.getRequestCooldown().hasExpired()) {
			player.sendMessage(ChatColor.RED + "You cannot request assistance that quickly. Try again later.");
			return;
		}

		Zoot.get().getPidgin().sendPacket(new PacketStaffRequest(
				ZootAPI.getColoredName(player),
				reason,
				Bukkit.getServerId(),
				Bukkit.getServerName()
		));

		profile.setRequestCooldown(new Cooldown(120_000L));
		player.sendMessage(Locale.STAFF_REQUEST_SUBMITTED.format());
	}

}
