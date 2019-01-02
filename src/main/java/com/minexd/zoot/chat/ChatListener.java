package com.minexd.zoot.chat;

import com.minexd.zoot.Locale;
import com.minexd.zoot.Zoot;
import com.minexd.zoot.bootstrap.BootstrappedListener;
import com.minexd.zoot.chat.event.ChatAttemptEvent;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends BootstrappedListener {

	public ChatListener(Zoot zoot) {
		super(zoot);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		ChatAttempt chatAttempt = zoot.getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
		ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

		zoot.getServer().getPluginManager().callEvent(chatAttemptEvent);

		if (!chatAttemptEvent.isCancelled()) {
			switch (chatAttempt.getResponse()) {
				case ALLOWED: {
					event.setFormat("%1$s" + CC.RESET + ": %2$s");
				}
				break;
				case MESSAGE_FILTERED: {
					event.setCancelled(true);
					chatAttempt.getFilterFlagged().punish(event.getPlayer());
				}
				break;
				case PLAYER_MUTED: {
					event.setCancelled(true);

					if (chatAttempt.getPunishment().isPermanent()) {
						event.getPlayer().sendMessage(CC.RED + "You are muted for forever.");
					} else {
						event.getPlayer().sendMessage(CC.RED + "You are muted for another " +
								chatAttempt.getPunishment().getTimeRemaining() + ".");
					}
				}
				break;
				case CHAT_MUTED: {
					event.setCancelled(true);
					event.getPlayer().sendMessage(CC.RED + "The public chat is currently muted.");
				}
				break;
				case CHAT_DELAYED: {
					event.setCancelled(true);
					event.getPlayer().sendMessage(Locale.CHAT_DELAYED.format(
							TimeUtil.millisToSeconds((int) chatAttempt.getValue())) + " seconds");
				}
				break;
			}
		}
	}

}
