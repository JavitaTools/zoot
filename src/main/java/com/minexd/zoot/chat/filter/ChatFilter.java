package com.minexd.zoot.chat.filter;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.bootstrap.Bootstrapped;
import org.bukkit.entity.Player;

public abstract class ChatFilter extends Bootstrapped {

	private String command;

	public ChatFilter(Zoot zoot, String command) {
		super(zoot);

		this.command = command;
	}

	public abstract boolean isFiltered(String message, String[] words);

	public void punish(Player player) {
		if (command != null) {
			zoot.getServer().dispatchCommand(zoot.getServer().getConsoleSender(), command
					.replace("{player}", player.getName())
					.replace("{player-uuid}", player.getUniqueId().toString()));
		}
	}

}
