package com.minexd.zoot.profile.grant.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.grant.menu.GrantsListMenu;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "grants", async = true, permission = "zoot.grants.show")
public class GrantsCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new GrantsListMenu(profile).openMenu(player);
	}

}
