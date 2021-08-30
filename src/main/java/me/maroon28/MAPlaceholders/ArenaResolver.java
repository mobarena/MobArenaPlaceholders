package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;

 class ArenaResolver {

    private final MobArena mobarena;

    ArenaResolver(MobArena mobarena) {
        this.mobarena = mobarena;
    }


     String resolve(OfflinePlayer player, String rest) {
        String[] parts = rest.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        Arena arena = getArenaByKey(player, head);
        if (tail == null || arena == null) {
            return "";
        }
        switch (tail) {
            case "wave": {
                return String.valueOf(arena.getWaveManager().getWaveNumber());
            }
            case "final-wave": {
                if (arena.getWaveManager().getFinalWave() > 0) {
                    return String.valueOf(arena.getWaveManager().getWaveNumber());
                } else {
                    return "∞";
                }            }
            case "living-mobs": {
                return String.valueOf(arena.getMonsterManager().getMonsters().size());
            }
            case "ready": {
                return String.valueOf(arena.getReadyPlayersInLobby().size());
            }
            case "non-ready": {
                return String.valueOf(arena.getNonreadyPlayers().size());
            }
            case "players": {
                return String.valueOf(arena.getArenaPlayerSet().size());
            }
            case "min-players": {
                return String.valueOf(arena.getMinPlayers());
            }
            case "max-players": {
                return String.valueOf(arena.getMaxPlayers());
            }
            case "auto-start-timer": {
                return String.valueOf(arena.getAutoStartTimer().getRemaining() / 20);
            }
            case "status": {
                return getArenaStatus(arena);
            }
            default: {
                return null;
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

    private String getArenaStatus(Arena arena) {
         if (arena.inEditMode()) {
             return "Editing";
         } else if (arena.isRunning()) {
             return "Running";
         } else if (arena.isEnabled()) {
             return "Available";
         } else if (!arena.isEnabled()) {
             return "Disabled";
         } else {
             return "";
         }
     }
}
