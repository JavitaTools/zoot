package com.minexd.zoot.essentials.command;

import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.util.BukkitReflection;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.StyleUtil;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "ping")
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(CC.YELLOW + "Your Ping: " + StyleUtil.colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
        } else {
            player.sendMessage(ZootAPI.getColoredName(target) + CC.YELLOW + "'s Ping: " +
                               StyleUtil.colorPing(BukkitReflection.getPing(target)));
        }
    }

}
