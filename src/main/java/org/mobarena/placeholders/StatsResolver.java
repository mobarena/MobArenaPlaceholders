package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.mobarena.stats.MobArenaStats;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;

public class StatsResolver {

    private final MobArena mobarena;
    private final MobArenaStats mastats;

    StatsResolver(Plugin mobarena, Plugin mastats) {
        this.mobarena = (MobArena) mobarena;
        this.mastats = (MobArenaStats) mastats;
    }

    String resolve(OfflinePlayer player, String rest) {
        // mobarena_stats is invalid
        if (rest == null) {
            return null;
        }
        String[] parts = rest.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        if (tail != null) {
            switch (head) {
                case "arena": {
                    return resolveArenaStat(player, tail);
                }
                case "global": {
                    return resolveGlobalStat(tail);
                }
                case "player": {
                    return resolvePlayerStat(player, tail);
                }
            }
        }
        return null;
    }

    private String resolveArenaStat(OfflinePlayer player, String rest) {
        String[] parts = rest.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        Arena arena = getArenaByKey(player, head);
        if (arena == null || tail == null) {
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
                return null;
            }
        }
    }

    private String resolveGlobalStat(String key) {
        GlobalStats stats = mastats.getStatsStore().getGlobalStats();
        if (key == null) {
            return null;
        }
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
                return null;
            }
        }
    }

    private String resolvePlayerStat(OfflinePlayer player, String key) {
        PlayerStats stats = mastats.getStatsStore().getPlayerStats(player.getName());
        if (key == null) {
            return null;
        }
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
        return null;
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
