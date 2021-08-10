package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.mobarena.stats.MobArenaStatsPlugin;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;

public class MobArenaPlaceholders extends PlaceholderExpansion {

    private final MobArenaStatsPlugin plugin;

    public MobArenaPlaceholders(MobArenaStatsPlugin plugin) {
        this.plugin = plugin;
    }

    MobArena mobarena; // This instance is assigned in canRegister()

    @Override
    public boolean canRegister() {
        return (mobarena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena")) != null;
    }

    @Override
    public boolean register() {
        mobarena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena");
        if (mobarena != null) {
            return super.register();
        }
        return false;
    }

    @Override
    public String getAuthor() {
        return "Maroon28";
    }

    @Override
    public String getIdentifier() {
        return "mapapi";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        // stats_arena_castle_highest-kills
        // for stats: ["stats", "arena_castle_highest-kills"]
        // either: ["current-arena", "wave"]
        // or:     ["arena", "castle_wave"]
        String[] parts = identifier.split("_", 2);
        Parser parser = new Parser();

        if (parts[0].equals("current-arena")) {
            Arena arena = mobarena.getArenaMaster().getArenaWithPlayer(player.getPlayer());
            if (arena == null) {
                return "";
            }
            return parser.onArenaRequest(arena, player, parts[1]);
        }
        if (parts[0].equals("arena")) {
            // split to ["castle", "wave"]
            parts = parts[1].split("_", 2);
            Arena arena = mobarena.getArenaMaster().getArenaWithName(parts[0]);
            return parser.onArenaRequest(arena, player, parts[1]);
        }
        if (parts[0].equals("stats")) {
            if (parts[1].equals("global")) {
                // split to ["global", "highest-kills"]
                parts = parts[1].split("_", 2);
                GlobalStats store = plugin.getStatsStore().getGlobalStats();
                return parser.onGlobalStatsRequest(store, parts[1]);
            }
            if (parts[1].equals("player")) {
                // split to ["player", "stat-id"]
                parts = parts[1].split("_", 2);
                PlayerStats store = plugin.getStatsStore().getPlayerStats(player.getName());
                return parser.onPlayerStatsRequest(store, parts[1]);
            }
            if (parts[1].equals("arena")) {
                // split to ["arena", "arenaName", "stat-id"]
                parts = parts[1].split("_", 3);
                String arenaName = parts[1];
                ArenaStats store = plugin.getStatsStore().getArenaStats(arenaName);
                return parser.onArenaStatsRequest(store, parts[2]);
            }
        }
            return null; // Placeholder not recognized
    }
}

