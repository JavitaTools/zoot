package com.minexd.zoot.essentials.command;

import com.minexd.zoot.ZootAPI;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@CommandMeta(label = "list")
public class ListCommand {

    public void executue(Player sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                Profile p1 = Profile.getByUuid(o1.getUniqueId());
                Profile p2 = Profile.getByUuid(o2.getUniqueId());

                return p1.getActiveRank().getWeight() - p2.getActiveRank().getWeight();
            }
        });

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort(new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                return o1.getWeight() - o2.getWeight();
            }
        });

        StringBuilder ranksBuilder = new StringBuilder();

        for (Rank rank : sortedRanks) {
            ranksBuilder.append(rank.getColor() + rank.getDisplayName());
            ranksBuilder.append(ChatColor.WHITE.toString());
            ranksBuilder.append(", ");
        }

        StringBuilder playerBuilder = new StringBuilder();

        for (Player player : sortedPlayers) {
            playerBuilder.append(ZootAPI.getColoredName(player));
            ranksBuilder.append(ChatColor.WHITE.toString());
            playerBuilder.append(", ");
        }

        sender.sendMessage(ranksBuilder.substring(0, ranksBuilder.length() - 2));
        sender.sendMessage(playerBuilder.substring(0, playerBuilder.length() - 2));
    }

}
