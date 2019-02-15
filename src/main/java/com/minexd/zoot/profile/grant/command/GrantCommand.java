package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketAddGrant;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.profile.grant.event.GrantAppliedEvent;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import com.minexd.zoot.util.duration.Duration;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "grant", async = true, permission = "zoot.grants.add")
public class GrantCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile, Rank rank, Duration duration, String reason) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (duration.getValue() == -1) {
			sender.sendMessage(CC.RED + "That duration is not valid.");
			sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
			return;
		}

		UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
		Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
				duration.getValue());

		profile.getGrants().add(grant);
		profile.save();
		profile.activateNextGrant();

		Zoot.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

		sender.sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
				.replace("{rank}", rank.getDisplayName())
				.replace("{player}", profile.getName())
				.replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
						: TimeUtil.millisToRoundedTime(duration.getValue())));

		Player player = profile.getPlayer();

		if (player != null) {
			new GrantAppliedEvent(player, grant).call();
		}
	}

}
