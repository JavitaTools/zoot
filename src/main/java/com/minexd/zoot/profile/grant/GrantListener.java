package com.minexd.zoot.profile.grant;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.network.packet.PacketDeleteGrant;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.event.GrantAppliedEvent;
import com.minexd.zoot.profile.grant.event.GrantExpireEvent;
import com.minexd.zoot.profile.grant.procedure.GrantProcedure;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureStage;
import com.minexd.zoot.profile.grant.procedure.GrantProcedureType;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import com.minexd.zoot.util.callback.TypeCallback;
import com.minexd.zoot.util.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GrantListener implements Listener {

	@EventHandler
	public void onGrantAppliedEvent(GrantAppliedEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(CC.GREEN + ("A `{rank}` grant has been applied to you for {time-remaining}.")
				.replace("{rank}", grant.getRank().getDisplayName())
				.replace("{time-remaining}", grant.getDuration() == Integer.MAX_VALUE ?
						"forever" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) -
						                                         System.currentTimeMillis())));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler
	public void onGrantExpireEvent(GrantExpireEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(CC.RED + ("Your `{rank}` grant has expired.")
				.replace("{rank}", grant.getRank().getDisplayName()));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("zoot.staff.grant")) {
			return;
		}

		GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

		if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
			event.setCancelled(true);

			if (event.getMessage().equalsIgnoreCase("cancel")) {
				GrantProcedure.getProcedures().remove(procedure);
				event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
				return;
			}

			if (procedure.getType() == GrantProcedureType.REMOVE) {
				new ConfirmMenu(CC.YELLOW + "Delete this grant?", new TypeCallback<Boolean>() {
					@Override
					public void callback(Boolean data) {
						if (data) {
							procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
							procedure.getGrant().setRemovedAt(System.currentTimeMillis());
							procedure.getGrant().setRemovedReason(event.getMessage());
							procedure.getGrant().setRemoved(true);
							procedure.finish();
							event.getPlayer().sendMessage(CC.GREEN + "The grant has been removed.");

							Zoot.get().getPidgin().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(),
									procedure.getGrant()));
						} else {
							procedure.cancel();
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
						}
					}
				}, true) {
					@Override
					public void onClose(Player player) {
						if (!isClosedByMenu()) {
							procedure.cancel();
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
						}
					}
				}.openMenu(event.getPlayer());
			}
		}
	}

}
