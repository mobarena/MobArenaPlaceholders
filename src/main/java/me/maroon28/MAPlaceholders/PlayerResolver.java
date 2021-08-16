package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;

public class PlayerResolver {

    private final MobArena mobarena;

    public PlayerResolver(MobArena mobarena) {
        this.mobarena = mobarena;
    }

    boolean isValid(Arena arena, OfflinePlayer player) {
        return !arena.inSpec(player.getPlayer())
                && !arena.inLobby(player.getPlayer())
                && !arena.isDead(player.getPlayer());
    }

    public String resolvePlayerPlaceholder(OfflinePlayer player, String tail) {
        Arena arena = mobarena.getArenaMaster().getArenaWithPlayer(player.getPlayer());
        if (arena == null || !isValid(arena, player)) {
            return "";
        }

        ArenaPlayerStatistics currentStats = arena.getArenaPlayer(player.getPlayer()).getStats();
        switch (tail) {
            case "class": {
                return arena.getArenaPlayer(player.getPlayer()).getArenaClass().getConfigName();
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
                return "";
            }
        }
     }
}
