package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

class ArenaResolver {

    private final MobArena mobarena;
    private final ConfigurationSection config;

    ArenaResolver(MobArena mobarena, ConfigurationSection config) {
        this.mobarena = mobarena;
        this.config = config;
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
            case "current-wave": {
                return String.valueOf(arena.getWaveManager().getWaveNumber());
            }
            case "final-wave": {
                if (arena.getWaveManager().getFinalWave() > 0) {
                    return String.valueOf(arena.getWaveManager().getWaveNumber());
                } else {
                    return "∞";
                }
            }
            case "live-mobs": {
                return String.valueOf(arena.getMonsterManager().getMonsters().size());
            }
            case "ready-players": {
                return String.valueOf(arena.getReadyPlayersInLobby().size());
            }
            case "live-players": {
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
            case "state": {
                return getArenaState(arena);
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

    private String getArenaState(Arena arena) {
         if (arena.inEditMode()) {
             return config.getString("editing", "Editing");
         } else if (arena.isRunning()) {
             return config.getString("running", "Running");
         } else if (arena.isEnabled()) {
             return config.getString("available", "Available");
         } else if (!arena.isEnabled()) {
             return config.getString("disabled", "Disabled");
         } else {
             return "";
         }
     }
}
