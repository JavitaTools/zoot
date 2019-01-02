package com.minexd.zoot.profile.punishment.command;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.cache.RedisPlayerData;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.punishment.menu.PunishmentsListMenu;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "check", "c" }, permission = "zoot.staff.check", async = true)
public class CheckCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		RedisPlayerData redisPlayerData = Zoot.get().getRedisCache().getPlayerData(profile.getUuid());

		if (redisPlayerData == null) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
	}

}
