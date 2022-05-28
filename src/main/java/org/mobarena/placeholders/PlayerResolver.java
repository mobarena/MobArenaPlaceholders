package org.mobarena.placeholders;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class PlayerResolver {

    private final MobArena mobarena;

    PlayerResolver(Plugin mobarena) {
        this.mobarena = (MobArena) mobarena;
    }

    String resolve(OfflinePlayer target, String tail) {
        // mobarena_player is invalid
        if (tail == null) {
            return null;
        }

        // At this point in time, we need to check if the tail is a valid
        // placeholder or not. However, all valid placeholders will have to
        // run the same additional validation logic, so we can simply defer
        // the actual value extraction using higher-order functions.
        switch (tail) {
            case "class": {
                return withStats(target, ArenaPlayerStatistics::getClassName);
            }
            case "kills": {
                return withIntStat(target, "kills");
            }
            case "damage-done": {
                return withIntStat(target, "dmgDone");
            }
            case "damage-taken": {
                return withIntStat(target, "dmgTaken");
            }
            case "swings": {
                return withIntStat(target, "swings");
            }
            case "hits": {
                return withIntStat(target, "hits");
            }
            case "last-wave": {
                return withIntStat(target, "lastWave");
            }
            default: {
                return null;
            }
        }
    }

    private String withIntStat(OfflinePlayer target, String key) {
        return withStats(target, stats -> String.valueOf(stats.getInt(key)));
    }

    private String withStats(
        OfflinePlayer target,
        Function<ArenaPlayerStatistics, String> resolver
    ) {
        Player player = getPlayer(target);
        if (player == null) {
            return "";
        }

        Arena arena = getArena(player);
        if (arena == null) {
            return "";
        }

        ArenaPlayerStatistics stats = getStats(player, arena);
        if (stats == null) {
            return "";
        }

        return resolver.apply(stats);
    }

    private Player getPlayer(OfflinePlayer target) {
        if (target == null || !target.isOnline()) {
            return null;
        }
        return target.getPlayer();
    }

    private Arena getArena(Player player) {
        ArenaMaster am = mobarena.getArenaMaster();
        Arena arena = am.getArenaWithPlayer(player);
        if (arena == null || !arena.inArena(player)) {
            return null;
        }
        return arena;
    }

    private ArenaPlayerStatistics getStats(Player player, Arena arena) {
        ArenaPlayer ap = arena.getArenaPlayer(player);
        if (ap == null) {
            return null;
        }
        return ap.getStats();
    }

}
