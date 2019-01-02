package com.minexd.zoot.profile.grant.event;

import com.minexd.zoot.profile.grant.Grant;
import com.minexd.zoot.util.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class GrantExpireEvent extends BaseEvent {

	private Player player;
	private Grant grant;

}
