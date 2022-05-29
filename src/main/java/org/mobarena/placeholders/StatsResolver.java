package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mobarena.stats.MobArenaStats;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;
import org.mobarena.stats.store.StatsStore;

import java.util.function.Function;

class StatsResolver {

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

        // mobarena_stats_<area> is invalid
        String[] parts = rest.split("_", 2);
        if (parts.length != 2) {
            return null;
        }

        String area = parts[0];
        String tail = parts[1];

        // Branch off based on area
        switch (area) {
            case "arena": {
                return resolveArenaStat(player, tail);
            }
            case "global": {
                return resolveGlobalStat(tail);
            }
            case "player": {
                return resolvePlayerStat(player, tail);
            }
            default: {
                return null;
            }
        }
    }

    private String resolveArenaStat(OfflinePlayer target, String rest) {
        // mobarena_stats_arena_<key> is invalid
        String[] parts = rest.split("_", 2);
        if (parts.length != 2) {
            return null;
        }

        String key = parts[0];
        String tail = parts[1];

        switch (tail) {
            case "total-kills": {
                return withArenaStats(target, key, stats -> stats.totalKills);
            }
            case "total-seconds": {
                return withArenaStats(target, key, stats -> stats.totalSeconds);
            }
            case "total-sessions": {
                return withArenaStats(target, key, stats -> stats.totalSessions);
            }
            case "total-waves": {
                return withArenaStats(target, key, stats -> stats.totalWaves);
            }
            case "highest-kills": {
                return withArenaStats(target, key, stats -> stats.highestKills);
            }
            case "highest-wave": {
                return withArenaStats(target, key, stats -> stats.highestWave);
            }
            case "highest-seconds": {
                return withArenaStats(target, key, stats -> stats.highestSeconds);
            }
            default: {
                return null;
            }
        }
    }

    private String withArenaStats(
        OfflinePlayer target,
        String key,
        Function<ArenaStats, Number> resolver
    ) {
        Arena arena = getArenaByKey(target, key);
        if (arena == null) {
            return "";
        }

        return withStatsStore(store -> {
            ArenaStats stats = store.getArenaStats(arena.getSlug());
            if (stats == null) {
                return "";
            }

            Number value = resolver.apply(stats);
            return String.valueOf(value);
        });
    }

    private String resolveGlobalStat(String key) {
        if (key == null) {
            return null;
        }

        switch (key) {
            case "total-kills": {
                return withGlobalStats(stats -> stats.totalKills);
            }
            case "total-waves": {
                return withGlobalStats(stats -> stats.totalWaves);
            }
            case "total-seconds": {
                return withGlobalStats(stats -> stats.totalSeconds);
            }
            case "total-sessions": {
                return withGlobalStats(stats -> stats.totalSessions);
            }
            default: {
                return null;
            }
        }
    }

    private String withGlobalStats(Function<GlobalStats, Number> resolver) {
        return withStatsStore(store -> {
            GlobalStats stats = store.getGlobalStats();
            if (stats == null) {
                return "";
            }

            Number value = resolver.apply(stats);
            return String.valueOf(value);
        });
    }

    private String resolvePlayerStat(OfflinePlayer target, String key) {
        if (key == null) {
            return null;
        }

        switch (key) {
            case "total-kills": {
                return withPlayerStats(target, stats -> stats.totalKills);
            }
            case "total-waves": {
                return withPlayerStats(target, stats -> stats.totalWaves);
            }
            case "total-seconds": {
                return withPlayerStats(target, stats -> stats.totalSeconds);
            }
            case "total-sessions": {
                return withPlayerStats(target, stats -> stats.totalSessions);
            }
            default: {
                return null;
            }
        }
    }

    private String withPlayerStats(
        OfflinePlayer target,
        Function<PlayerStats, Number> resolver
    ) {
        return withStatsStore(store -> {
            String name = target.getName();
            PlayerStats stats = store.getPlayerStats(name);
            if (stats == null) {
                return "";
            }

            Number value = resolver.apply(stats);
            return String.valueOf(value);
        });
    }

    private String withStatsStore(Function<StatsStore, String> resolver) {
        StatsStore store = mastats.getStatsStore();
        if (store == null) {
            return "";
        }
        return resolver.apply(store);
    }

    private Arena getArenaByKey(OfflinePlayer target, String key) {
        ArenaMaster am = mobarena.getArenaMaster();
        if (key.equals("$current")) {
            if (target == null || !target.isOnline()) {
                return null;
            }
            Player player = target.getPlayer();
            if (player == null) {
                return null;
            }
            return am.getArenaWithPlayer(player);
        }
        return am.getArenaWithName(key);
    }

}
