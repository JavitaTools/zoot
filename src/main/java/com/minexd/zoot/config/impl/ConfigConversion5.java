package com.minexd.zoot.config.impl;

import com.minexd.zoot.config.ConfigConversion;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigConversion5 implements ConfigConversion {

	@Override
	public void convert(File file, FileConfiguration fileConfiguration) {
		fileConfiguration.set("CONFIG_VERSION", 5);
		fileConfiguration.set("GLOBAL_WHITELIST.KICK_MAINTENANCE", "&cThe server is currently in maintenance.\\nCheck our discord for more announcements!");
		fileConfiguration.set("GLOBAL_WHITELIST.KICK_CLOSED_TESTING", "&cYou are not whitelisted. To gain early access, you can\\npurchase an eligible rank &7(&6Gold+&7) &con our store.\\n&fhttps://store.zonix.us");

		try {
			fileConfiguration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
