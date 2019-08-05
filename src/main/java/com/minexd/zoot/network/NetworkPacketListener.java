package com.minexd.zoot.network;

import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.event.ReceiveStaffChatEvent;
import com.minexd.zoot.network.packet.PacketAddGrant;
import com.minexd.zoot.network.packet.PacketBroadcastPunishment;
import com.minexd.zoot.network.packet.PacketDeleteGrant;
import com.minexd.zoot.network.packet.PacketDeleteRank;
import com.minexd.zoot.network.packet.PacketRefreshRank;
import com.minexd.zoot.network.packet.PacketStaffChat;
import com.minexd.zoot.network.packet.PacketStaffJoinNetwork;
import com.minexd.zoot.network.packet.PacketStaffLeaveNetwork;
import com.minexd.zoot.network.packet.PacketStaffReport;
import com.minexd.zoot.network.packet.PacketStaffRequest;
import com.minexd.zoot.network.packet.PacketStaffSwitchServer;
import com.minexd.zoot.network.packet.PacketClearPunishments;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.profile.grant.event.GrantAppliedEvent;
import com.minexd.zoot.profile.grant.event.GrantExpireEvent;
import com.minexd.zoot.profile.punishment.Punishment;
import com.minexd.zoot.rank.Rank;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NetworkPacketListener implements PacketListener {

	private Zoot zoot;

	public NetworkPacketListener(Zoot zoot) {
		this.zoot = zoot;
	}

	@IncomingPacketHandler
	public void onAddGrant(PacketAddGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantAppliedEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onDeleteGrant(PacketDeleteGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantExpireEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
		Punishment punishment = packet.getPunishment();
		punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());

		Player player = Bukkit.getPlayer(packet.getTargetUuid());

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
			profile.getPunishments().add(punishment);

			if (punishment.getType().isBan() && !punishment.isRemoved() && !punishment.hasExpired()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.kickPlayer(punishment.getKickMessage());
					}
				}.runTask(Zoot.get());
			}
		}
	}

	@IncomingPacketHandler
	public void onRankRefresh(PacketRefreshRank packet) {
		Rank rank = Rank.getRankByUuid(packet.getUuid());

		if (rank == null) {
			rank = new Rank(packet.getUuid(), packet.getName());
		}

		rank.load();

		Zoot.broadcastOps("&8[&eNetwork&8] &fRefreshed rank " + rank.getDisplayName());
	}

	@IncomingPacketHandler
	public void onRankDelete(PacketDeleteRank packet) {
		Rank rank = Rank.getRanks().remove(packet.getUuid());

		if (rank != null) {
			Zoot.broadcastOps("&8[&eNetwork&8] &fDeleted rank " + rank.getDisplayName() );
		}
	}

	@IncomingPacketHandler
	public void onStaffChat(PacketStaffChat packet) {
		zoot.getServer().getOnlinePlayers().stream()
		    .filter(onlinePlayer -> onlinePlayer.hasPermission("zoot.staff"))
		    .forEach(onlinePlayer -> {
			    ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);

			    zoot.getServer().getPluginManager().callEvent(event);

			    if (!event.isCancelled()) {
			    	Profile profile = Profile.getProfiles().get(event.getPlayer().getUniqueId());

			    	if (profile != null && profile.getStaffOptions().staffModeEnabled()) {
					    onlinePlayer.sendMessage(Locale.STAFF_CHAT.format(packet.getPlayerName(), packet.getServerName(),
							    packet.getChatMessage()));
				    }
			    }
		    });
	}

	@IncomingPacketHandler
	public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
		zoot.getServer().broadcast(Locale.STAFF_JOIN_NETWORK.format(packet.getPlayerName(), packet.getServerName()),
				"zoot.staff");
	}

	@IncomingPacketHandler
	public void onStaffLeaveNetwork(PacketStaffLeaveNetwork packet) {
		zoot.getServer().broadcast(Locale.STAFF_LEAVE_NETWORK.format(packet.getPlayerName()), "zoot.staff");
	}

	@IncomingPacketHandler
	public void onStaffSwitchServer(PacketStaffSwitchServer packet) {
		zoot.getServer().broadcast(Locale.STAFF_SWITCH_SERVER.format(packet.getPlayerName(), packet.getToServerName(),
				packet.getFromServerName()), "zoot.staff");
	}

	@IncomingPacketHandler
	public void onStaffReport(PacketStaffReport packet) {
		List<String> messages = Locale.STAFF_REPORT_BROADCAST.formatLines(packet.getSentBy(), packet.getAccused(),
				packet.getReason(), packet.getServerId(), packet.getServerName());

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("zoot.staff")) {
				Profile profile = Profile.getByUuid(player.getUniqueId());

				if (profile.getStaffOptions().staffModeEnabled()) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	@IncomingPacketHandler
	public void onStaffRequest(PacketStaffRequest packet) {
		List<String> messages = Locale.STAFF_REQUEST_BROADCAST.formatLines(packet.getSentBy(), packet.getReason(),
				packet.getServerId(), packet.getServerName());

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("zoot.staff")) {
				Profile profile = Profile.getByUuid(player.getUniqueId());

				if (profile.getStaffOptions().staffModeEnabled()) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	@IncomingPacketHandler
	public void onClearGrants(PacketClearPunishments packet) {
		Player player = Bukkit.getPlayer(packet.getUuid());

		if (player != null) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getGrants().clear();
		}
	}

	@IncomingPacketHandler
	public void onClearPunishments(PacketClearPunishments packet) {
		Player player = Bukkit.getPlayer(packet.getUuid());

		if (player != null) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getPunishments().clear();
		}
	}

}
