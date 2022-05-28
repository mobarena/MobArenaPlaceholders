package org.mobarena.placeholders;

import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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

        // Accept valid placeholders only (yikes!)
        switch (tail) {
            case "class":
            case "kills":
            case "damage-done":
            case "damage-taken":
            case "swings":
            case "hits":
            case "last-wave": {
                break;
            }
            default: {
                return null;
            }
        }

        if (target == null || !target.isOnline()) {
            return "";
        }

        Player player = target.getPlayer();
        if (player == null) {
            return "";
        }

        Arena arena = mobarena.getArenaMaster().getArenaWithPlayer(player);
        if (arena == null || !arena.inArena(player)) {
            return "";
        }

        ArenaPlayerStatistics currentStats = arena.getArenaPlayer(player).getStats();
        switch (tail) {
            case "class": {
                return currentStats.getClassName();
            }
            case "kills": {
                return String.valueOf(currentStats.getInt("kills"));
            }
            case "damage-done": {
                return String.valueOf(currentStats.getInt("dmgDone"));
            }
            case "damage-taken": {
                return String.valueOf(currentStats.getInt("dmgTaken"));
            }
            case "swings": {
                return String.valueOf(currentStats.getInt("swings"));
            }
            case "hits": {
                return String.valueOf(currentStats.getInt("hits"));
            }
            case "last-wave": {
                return String.valueOf(currentStats.getInt("lastWave"));
            }
            default: {
                return null;
            }
        }
    }
}
