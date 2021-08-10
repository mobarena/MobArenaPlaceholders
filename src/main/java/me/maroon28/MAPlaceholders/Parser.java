package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;

public class Parser {

    public String getArenaStatus(Arena arena, Boolean colored) {
        if (colored) {
            if (arena.inEditMode()) {
                return "&eEditing";
            } else if (arena.isRunning()) {
                return "&6Running";
            } else if (arena.isEnabled()) {
                return "&aAvailable";
            } else if (!arena.isEnabled()) {
                return "&cDisabled";
            } else {
                return "";
            }
        }
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


    public String onArenaRequest(Arena arena, OfflinePlayer player, String rest) {
        switch (rest) {
            case "wave": {
                return String.valueOf(arena.getWaveManager().getWaveNumber());
            }
            case "final-wave": {
                return String.valueOf(arena.getWaveManager().getFinalWave());
            }
            case "remaining-mobs": {
                return String.valueOf(arena.getMonsterManager().getMonsters());
            }
            case "kills": {
                return String.valueOf(arena.getArenaPlayer(player.getPlayer()).getStats().getInt("kills"));
            }
            case "ready": {
                return String.valueOf(arena.getReadyPlayersInLobby().size());
            }
            case "non-ready": {
                return String.valueOf(arena.getNonreadyPlayers().size());
            }
            case "players": {
               return String.valueOf(arena.getPlayerCount());
            }
            case "min-players": {
                return String.valueOf(arena.getMinPlayers());
            }
            case "max-players": {
                return String.valueOf(arena.getMaxPlayers());
            }
            case "auto-start-timer": {
                return String.valueOf(arena.getAutoStartTimer());
            }
            case "status": {
                return getArenaStatus(arena, false);
            }
        }
        return "";
    }
    public String onGlobalStatsRequest(GlobalStats store, String rest) {
        switch (rest) {
            case "sessions": {
                return String.valueOf(store.totalSessions);
            }
            case "seconds": {
                return String.valueOf(store.totalSeconds);
            }
            case "kills": {
                return String.valueOf(store.totalKills);
            }
            case "waves": {
                return String.valueOf(store.totalWaves);
            }
        }
        return "";
    }

    public String onPlayerStatsRequest(PlayerStats store, String rest) {
        switch (rest) {
            case "sessions": {
                return String.valueOf(store.totalSessions);
            }
            case "seconds": {
                return String.valueOf(store.totalSeconds);
            }
            case "kills": {
                return String.valueOf(store.totalKills);
            }
            case "waves": {
                return String.valueOf(store.totalWaves);
            }
        }
        return "";
    }

    public String onArenaStatsRequest(ArenaStats store, String rest) {
        switch (rest) {
            case "sessions": {
                return String.valueOf(store.totalSessions);
            }
            case "seconds": {
                return String.valueOf(store.totalSeconds);
            }
            case "kills": {
                return String.valueOf(store.totalKills);
            }
            case "waves": {
                return String.valueOf(store.totalWaves);
            }
        }
        return "";
    }
}
