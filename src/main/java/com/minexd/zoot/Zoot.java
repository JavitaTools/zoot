package com.minexd.zoot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minexd.pidgin.Pidgin;
import com.minexd.zoot.chat.Chat;
import com.minexd.zoot.chat.ChatListener;
import com.minexd.zoot.chat.command.ClearChatCommand;
import com.minexd.zoot.chat.command.MuteChatCommand;
import com.minexd.zoot.chat.command.SlowChatCommand;
import com.minexd.zoot.config.ConfigValidation;
import com.minexd.zoot.essentials.Essentials;
import com.minexd.zoot.essentials.EssentialsListener;
import com.minexd.zoot.essentials.command.*;
import com.minexd.zoot.essentials.command.SudoAllCommand;
import com.minexd.zoot.essentials.command.SudoCommand;
import com.minexd.zoot.essentials.command.ZootDebugCommand;
import com.minexd.zoot.network.NetworkPacketListener;
import com.minexd.zoot.network.command.ReportCommand;
import com.minexd.zoot.network.command.RequestCommand;
import com.minexd.zoot.network.packet.PacketAddGrant;
import com.minexd.zoot.network.packet.PacketBroadcastPunishment;
import com.minexd.zoot.network.packet.PacketClearGrants;
import com.minexd.zoot.network.packet.PacketDeleteGrant;
import com.minexd.zoot.network.packet.PacketDeleteRank;
import com.minexd.zoot.network.packet.PacketRefreshRank;
import com.minexd.zoot.network.packet.PacketStaffChat;
import com.minexd.zoot.network.packet.PacketStaffJoinNetwork;
import com.minexd.zoot.network.packet.PacketStaffLeaveNetwork;
import com.minexd.zoot.network.packet.PacketStaffReport;
import com.minexd.zoot.network.packet.PacketStaffRequest;
import com.minexd.zoot.network.packet.PacketStaffSwitchServer;
import com.minexd.zoot.network.packet.PacketClearPunishments;
import com.minexd.zoot.profile.Profile;
import com.minexd.zoot.profile.ProfileListener;
import com.minexd.zoot.profile.ProfileTypeAdapter;
import com.minexd.zoot.profile.grant.GrantListener;
import com.minexd.zoot.profile.grant.command.ClearGrantsCommand;
import com.minexd.zoot.profile.option.command.OptionsCommand;
import com.minexd.zoot.profile.conversation.command.MessageCommand;
import com.minexd.zoot.profile.conversation.command.ReplyCommand;
import com.minexd.zoot.profile.grant.command.GrantCommand;
import com.minexd.zoot.profile.grant.command.GrantsCommand;
import com.minexd.zoot.profile.option.command.ToggleGlobalChatCommand;
import com.minexd.zoot.profile.option.command.TogglePrivateMessagesCommand;
import com.minexd.zoot.profile.option.command.ToggleSoundsCommand;
import com.minexd.zoot.profile.punishment.command.BanCommand;
import com.minexd.zoot.profile.punishment.command.CheckCommand;
import com.minexd.zoot.profile.punishment.command.ClearPunishmentsCommand;
import com.minexd.zoot.profile.punishment.command.KickCommand;
import com.minexd.zoot.profile.punishment.command.MuteCommand;
import com.minexd.zoot.profile.punishment.command.UnbanCommand;
import com.minexd.zoot.profile.punishment.command.UnmuteCommand;
import com.minexd.zoot.profile.punishment.command.WarnCommand;
import com.minexd.zoot.profile.punishment.listener.PunishmentListener;
import com.minexd.zoot.profile.staff.command.AltsCommand;
import com.minexd.zoot.profile.staff.command.StaffModeCommand;
import com.minexd.zoot.rank.Rank;
import com.minexd.zoot.rank.RankTypeAdapter;
import com.minexd.zoot.profile.staff.command.StaffChatCommand;
import com.minexd.zoot.rank.command.RankAddPermissionCommand;
import com.minexd.zoot.rank.command.RankCreateCommand;
import com.minexd.zoot.rank.command.RankDeleteCommand;
import com.minexd.zoot.rank.command.RankHelpCommand;
import com.minexd.zoot.rank.command.RankInfoCommand;
import com.minexd.zoot.rank.command.RankInheritCommand;
import com.minexd.zoot.rank.command.RankRemovePermissionCommand;
import com.minexd.zoot.rank.command.RankSetColorCommand;
import com.minexd.zoot.rank.command.RankSetPrefixCommand;
import com.minexd.zoot.rank.command.RankSetWeightCommand;
import com.minexd.zoot.rank.command.RankUninheritCommand;
import com.minexd.zoot.rank.command.RanksCommand;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.adapter.ChatColorTypeAdapter;
import com.minexd.zoot.util.duration.Duration;
import com.minexd.zoot.util.duration.DurationTypeAdapter;
import com.minexd.zoot.cache.RedisCache;
import com.minexd.zoot.util.menu.MenuListener;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.qrakn.honcho.Honcho;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Zoot extends JavaPlugin {

	public static final Gson GSON = new Gson();
	public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

	private static Zoot zoot;

	@Getter private BasicConfigurationFile mainConfig;

	@Getter private Honcho honcho;
	@Getter private Pidgin pidgin;

	@Getter private MongoDatabase mongoDatabase;
	@Getter private JedisPool jedisPool;
	@Getter private RedisCache redisCache;

	@Getter private Essentials essentials;
	@Getter private Chat chat;

	@Getter @Setter private boolean debug;

	@Override
	public void onEnable() {
		zoot = this;
		
		mainConfig = new BasicConfigurationFile(this, "config");

		new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 4).check();

		loadMongo();
		loadRedis();

		redisCache = new RedisCache(this);
		essentials = new Essentials(this);
		chat = new Chat(this);

		honcho = new Honcho(this);

		Arrays.asList(
				new BroadcastCommand(),
				new ClearCommand(),
				new GameModeCommand(),
				new HealCommand(),
				new ShowAllPlayersCommand(),
				new ShowPlayerCommand(),
				new HidePlayerCommand(),
				new LocationCommand(),
				new MoreCommand(),
				new RenameCommand(),
				new SetSlotsCommand(),
				new SetSpawnCommand(),
				new SpawnCommand(),
				new DayCommand(),
				new NightCommand(),
				new SunsetCommand(),
				new ClearChatCommand(),
				new SlowChatCommand(),
				new AltsCommand(),
				new BanCommand(),
				new CheckCommand(),
				new KickCommand(),
				new MuteCommand(),
				new UnbanCommand(),
				new UnmuteCommand(),
				new WarnCommand(),
				new GrantCommand(),
				new GrantsCommand(),
				new StaffChatCommand(),
				new StaffModeCommand(),
				new MuteChatCommand(),
				new OptionsCommand(),
				new RankAddPermissionCommand(),
				new RankCreateCommand(),
				new RankDeleteCommand(),
				new RankHelpCommand(),
				new RankInfoCommand(),
				new RankInheritCommand(),
				new RankRemovePermissionCommand(),
				new RanksCommand(),
				new RankSetColorCommand(),
				new RankSetPrefixCommand(),
				new RankSetWeightCommand(),
				new RankUninheritCommand(),
				new ZootDebugCommand(),
				new TeleportWorldCommand(),
				new MessageCommand(),
				new ReplyCommand(),
				new ToggleGlobalChatCommand(),
				new TogglePrivateMessagesCommand(),
				new ToggleSoundsCommand(),
				new PingCommand(),
				new ListCommand(),
				new ReportCommand(),
				new RequestCommand(),
				new ClearGrantsCommand(),
				new ClearPunishmentsCommand(),
				new SudoCommand(),
				new SudoAllCommand()
		).forEach(honcho::registerCommand);

		honcho.registerTypeAdapter(Rank.class, new RankTypeAdapter());
		honcho.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
		honcho.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
		honcho.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());

		pidgin = new Pidgin("zoot",
				mainConfig.getString("REDIS.HOST"),
				mainConfig.getInteger("REDIS.PORT"),
				mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
						mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
		);

		Arrays.asList(
				PacketAddGrant.class,
				PacketBroadcastPunishment.class,
				PacketDeleteGrant.class,
				PacketDeleteRank.class,
				PacketRefreshRank.class,
				PacketStaffChat.class,
				PacketStaffJoinNetwork.class,
				PacketStaffLeaveNetwork.class,
				PacketStaffSwitchServer.class,
				PacketStaffReport.class,
				PacketStaffRequest.class,
				PacketClearGrants.class,
				PacketClearPunishments.class
		).forEach(pidgin::registerPacket);

		pidgin.registerListener(new NetworkPacketListener(this));

		Arrays.asList(
				new ProfileListener(),
				new MenuListener(),
				new EssentialsListener(),
				new ChatListener(),
				new GrantListener(),
				new PunishmentListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		Rank.init();
	}

	@Override
	public void onDisable() {
		try {
			jedisPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Broadcasts a message to all server operators.
	 *
	 * @param message The message.
	 */
	public static void broadcastOps(String message) {
		Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(CC.translate(message)));
	}

	private void loadMongo() {
		if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
			ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
					mainConfig.getInteger("MONGO.PORT"));

			MongoCredential credential = MongoCredential.createCredential(
					mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
					mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

			mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
					.getDatabase("zoot");
		} else {
			mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"),
					mainConfig.getInteger("MONGO.PORT")).getDatabase("zoot");
		}
	}

	private void loadRedis() {
		jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

		if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
			}
		}
	}

	public static Zoot get() {
		return zoot;
	}

}
