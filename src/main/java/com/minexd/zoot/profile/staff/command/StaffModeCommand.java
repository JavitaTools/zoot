package com.minexd.zoot.profile.staff.command;

import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffmode", "sm" }, permission = "zoot.staff")
public class StaffModeCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().staffModeEnabled(!profile.getStaffOptions().staffModeEnabled());

        player.sendMessage(profile.getStaffOptions().staffModeEnabled() ?
                CC.GREEN + "You are now in staff mode." : CC.RED + "You are no longer in staff mode.");
    }

}
