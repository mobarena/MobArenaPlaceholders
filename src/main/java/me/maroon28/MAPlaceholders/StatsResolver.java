package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;
import org.mobarena.stats.MobArenaStats;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;

public class StatsResolver {

    private final MobArena mobarena;
    private final MobArenaStats mastats;

    public StatsResolver(MobArena mobarena, MobArenaStats mastats) {
        this.mobarena = mobarena;
        this.mastats = mastats;
    }

    public String resolve(OfflinePlayer player, String rest) {
        String[] parts = rest.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        switch (head) {
            case "global": {
                return resolveGlobalStat(tail);
            }
            case "arena": {
                return resolveArenaStat(player, tail);
            }
            case "player": {
                return resolvePlayerStat(player, tail);
            }
        }
        return "";
    }

    private String resolveGlobalStat(String key) {
        GlobalStats stats = mastats.getStatsStore().getGlobalStats();
        switch (key) {
            case "total-kills": {
                return String.valueOf(stats.totalKills);
            }
            case "total-waves": {
                return String.valueOf(stats.totalWaves);
            }
            case "total-seconds": {
                return String.valueOf(stats.totalSeconds);
            }
            case "total-sessions": {
                return String.valueOf(stats.totalSessions);
            }
            default: {
                return "";
            }
        }
    }

    private String resolvePlayerStat(OfflinePlayer player, String key) {
        PlayerStats stats = mastats.getStatsStore().getPlayerStats(player.getName());
        switch (key) {
            case "total-kills": {
                return String.valueOf(stats.totalKills);
            }
            case "total-waves": {
                return String.valueOf(stats.totalWaves);
            }
            case "total-seconds": {
                return String.valueOf(stats.totalSeconds);
            }
            case "total-sessions": {
                return String.valueOf(stats.totalSessions);
            }
        }
        return "";
    }

    private String resolveArenaStat(OfflinePlayer player, String rest) {
        String[] parts = rest.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        Arena arena = getArenaByKey(player, head);
        if (arena == null) {
            return "";
        }
        ArenaStats stats = mastats.getStatsStore().getArenaStats(arena.getSlug());
        switch (tail) {
            case "total-kills": {
                return String.valueOf(stats.totalKills);
            }
            case "total-seconds": {
                return String.valueOf(stats.totalSeconds);
            }
            case "total-sessions": {
                return String.valueOf(stats.totalSessions);
            }
            case "total-waves": {
                return String.valueOf(stats.totalWaves);
            }
            case "highest-kills": {
                return String.valueOf(stats.highestKills);
            }
            case "highest-wave": {
                return String.valueOf(stats.highestWave);
            }
            case "highest-seconds": {
                return String.valueOf(stats.highestSeconds);
            }
            default: {
                return "";
            }
        }
    }

    private Arena getArenaByKey(OfflinePlayer player, String key) {
        ArenaMaster am = mobarena.getArenaMaster();
        if (key.equals("$current")) {
            return am.getArenaWithPlayer(player.getPlayer());
        } else {
            return am.getArenaWithName(key);
        }
    }


}
