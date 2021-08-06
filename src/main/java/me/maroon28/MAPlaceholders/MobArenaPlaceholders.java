package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mobarena.stats.MobArenaStats;
import org.mobarena.stats.MobArenaStatsPlugin;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;
import org.mobarena.stats.store.StatsStore;

public class MobArenaPlaceholders extends PlaceholderExpansion {

    MobArena MobArena; // This instance is assigned in canRegister()

    @Override
    public boolean canRegister() {
        return (MobArena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena")) != null;
    }

    @Override
    public boolean register() {
        MobArena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena");
        if (MobArena != null) {
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

    public boolean isValid(Arena arena, Player player) {
        return !arena.inSpec((player)) &&
                !arena.getPlayersInLobby().contains(player) &&
                !arena.isDead(player);
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        final String[] args = identifier.split("\\_");
        final String arenaName = args[0];

        // Global
        if (identifier.equalsIgnoreCase("total_enabled")) {
            return String.valueOf(MobArena.getArenaMaster().getEnabledArenas().size());
        } else if (identifier.equalsIgnoreCase("total_playing")) {
            return String.valueOf(MobArena.getArenaMaster().getAllPlayers().size());
        }

        // get the arena the player is in.
        Arena playerArena = MobArena.getArenaMaster().getArenaWithPlayer(player.getName());
        // get the arena from the given arguments
        Arena selectedArena = MobArena.getArenaMaster().getArenaWithName(arenaName);

        // check if the user is parsing player-arena
        if (identifier.contains("player-arena")) {
            // Return an empty string if the player isn't in a valid state
            if (playerArena == null || !isValid(playerArena, player.getPlayer())) {
                return "";
            }
            // Player-Arena placeholders. E.G player-arena_{variable}
            switch (identifier) {
                case "player-arena_name": {
                    return playerArena.getSlug();
                }
                case "player-arena_wave": {
                    return String.valueOf(playerArena.getWaveManager().getWaveNumber());
                }
                case "player-arena_final-wave": {
                    // check if an arena has a final wave
                    if (playerArena.getWaveManager().getFinalWave() > 0) {
                        return String.valueOf(playerArena.getWaveManager().getFinalWave());
                    } else {
                        return "∞";
                    }
                }
                case "player-arena_remaining-mobs": {
                    return String.valueOf(playerArena.getMonsterManager().getMonsters().size());
                }
                case "player-arena_kills": {
                    return String.valueOf(playerArena.getArenaPlayer(player.getPlayer()).getStats().getInt("kills"));
                }
                case "player-arena_ready": {
                    return String.valueOf(playerArena.getReadyPlayersInLobby().size());
                }
                case "player-arena_non-ready": {
                    return String.valueOf(playerArena.getNonreadyPlayers().size());
                }
                case "player-arena_players": {
                    if (playerArena.getPlayersInArena().size() > 1) {
                        return String.valueOf(playerArena.getPlayersInArena().size());
                    } else {
                        return "0";
                    }
                }
                case "player-arena_min-players": {
                    return String.valueOf(playerArena.getMinPlayers());
                }
                case "player-arena_max-players": {
                    return String.valueOf(playerArena.getMaxPlayers());
                }
                case "player-arena_auto-start-timer": {
                    if (playerArena.getAutoStartTimer().isRunning()) {
                        // divide by 20 to convert from Ticks -> Seconds
                        return String.valueOf(playerArena.getAutoStartTimer().getRemaining() / 20);
                    } else {
                        return "0";
                    }
                }
                case "player-arena_isready": {
                    if (playerArena.getReadyPlayersInLobby().contains(player.getPlayer())) {
                        return "Ready";
                    } else if (!playerArena.getReadyPlayersInLobby().contains(player.getPlayer())) {
                        return "Not Ready";
                    } else {
                        return "Playing";
                    }
                }
                default: {
                    return "PNF";
                }
            }
        }
        //selected arena placeholders. I.E arenaName_ID
        if (args.length == 2) {
            final String param = args[1];
            switch (param) {
                case "name": {
                    return selectedArena.getSlug();
                }
                case "wave": {
                    return String.valueOf(selectedArena.getWaveManager().getWaveNumber());
                }
                case "final-wave": {
                    if (selectedArena.getWaveManager().getFinalWave() > 0) {
                        return String.valueOf(selectedArena.getWaveManager().getFinalWave());
                    } else {
                        return "∞";
                    }
                }
                case "remaining-mobs": {
                    return String.valueOf(selectedArena.getMonsterManager().getMonsters().size());
                }
                case "ready": {
                    return String.valueOf(selectedArena.getReadyPlayersInLobby().size());
                }
                case "non-ready": {
                    return String.valueOf(selectedArena.getNonreadyPlayers().size());
                }
                case "players": {
                    if (selectedArena.getPlayersInArena().size() > 1) {
                        return String.valueOf(selectedArena.getPlayersInArena().size());
                    } else {
                        return "0";
                    }
                }
                case "min-players": {
                    return String.valueOf(selectedArena.getMinPlayers());
                }
                case "max-players": {
                    return String.valueOf(selectedArena.getMaxPlayers());
                }
                case "auto-start-timer": {
                    if (selectedArena.getAutoStartTimer().isRunning()) {
                        // divided to turn from ticks to seconds
                        return String.valueOf(selectedArena.getAutoStartTimer().getRemaining() / 20);
                    } else {
                        return "0";
                    }
                }
                case "player-status": {
                    if (selectedArena.isDead(player.getPlayer())) {
                        return "Dead";
                    } else if (selectedArena.inSpec(player.getPlayer())) {
                        return "Spectating";
                    } else if (selectedArena.getPlayersInArena().contains(player.getPlayer())) {
                        return "Playing";
                    } else {
                        return "Not Playing";
                    }
                }
                case "status": {
                    return getArenaStatus(selectedArena, false);
                }
                case "status-colored": {
                    return getArenaStatus(selectedArena, true);
                }
            }
        }

        // Stats start.
        if (Bukkit.getPluginManager().getPlugin("MobArenaStats") == null) {
            return "";
        } else {
            MobArenaStats plugin = MobArenaStatsPlugin.getInstance();
            StatsStore store = plugin.getStatsStore();

            ArenaStats arenaStats = store.getArenaStats(arenaName);
            GlobalStats globalStats = store.getGlobalStats();
            PlayerStats playerStats = store.getPlayerStats(player.getName());

            switch (identifier) {
                // Global stats
                case "global_sessions": {
                    return Integer.toString(globalStats.totalSessions);
                }
                case "global_seconds": {
                    return Long.toString(globalStats.totalSeconds);
                }
                case "global_seconds-formatted": {
                    return DurationFormatUtils.formatDuration(globalStats.totalSeconds * 1000L, "HH:mm:ss", true);
                }
                case "global_kills": {
                    return Long.toString(globalStats.totalKills);
                }
                case "global_waves": {
                    return Long.toString(globalStats.totalWaves);
                }
                // Player Stats
                case "player_total-sessions": {
                    return Integer.toString(playerStats.totalSessions);
                }
                case "player_total-kills": {
                    return Long.toString(playerStats.totalKills);
                }
                case "player_total-seconds": {
                    return Long.toString(playerStats.totalSeconds);
                }
                case "player_total-seconds-formatted": {
                    return DurationFormatUtils.formatDuration(playerStats.totalSeconds * 1000L, "HH:mm:ss", true);
                }
                case "player_total-waves": {
                    return Long.toString(playerStats.totalWaves);
                }
            }

            // arenaName_stat
            if (args.length == 2) {
                final String param = args[1];
                switch (param) {
                    case "highest-wave": {
                        return Integer.toString(arenaStats.highestWave);
                    }
                    case "highest-kills": {
                        return Integer.toString(arenaStats.highestKills);
                    }
                    case "highest-seconds": {
                        return Integer.toString(arenaStats.highestSeconds);
                    }
                    case "highest-seconds-formatted": {
                        return DurationFormatUtils.formatDuration(arenaStats.highestSeconds * 1000L, "HH:mm:ss", true);
                    }
                    case "total-kills": {
                        return Long.toString(arenaStats.totalKills);
                    }
                    case "total-waves": {
                        return Long.toString(arenaStats.totalWaves);
                    }
                    case "total-sessions": {
                        return Integer.toString(arenaStats.totalSessions);
                    }
                    case "total-seconds": {
                        return Long.toString(arenaStats.totalSeconds);
                    }
                    case "total-seconds-formatted": {
                        return DurationFormatUtils.formatDuration(arenaStats.totalSeconds * 1000L, "HH:mm:ss", true);
                        }
                }
            }
        }
        return null; // Placeholder not recognized
    }

}
