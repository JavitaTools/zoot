package com.minexd.zoot.essentials.command;

import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.util.BukkitReflection;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "ping")
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(CC.YELLOW + "Your Ping: " + colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
        } else {
            player.sendMessage(ZootAPI.getColoredName(target) + CC.YELLOW + "'s Ping: " +
                    colorPing(BukkitReflection.getPing(target)));
        }
    }

    private String colorPing(int ping) {
        if (ping <= 40) {
            return CC.GREEN + ping;
        } else if (ping <= 70) {
            return CC.YELLOW + ping;
        } else if (ping <= 100) {
            return CC.GOLD + ping;
        } else {
            return CC.RED + ping;
        }
    }

}
