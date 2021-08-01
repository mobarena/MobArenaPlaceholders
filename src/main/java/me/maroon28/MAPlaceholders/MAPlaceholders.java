package me.maroon28.MAPlaceholders;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;


public class MAPlaceholders extends PlaceholderExpansion {

    MobArena mobArena; // This instance is assigned in canRegister()

    @Override
    public boolean canRegister() {
        return (mobArena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena")) != null;
    }

    @Override
    public boolean register() {
        mobArena = (MobArena) Bukkit.getPluginManager().getPlugin("MobArena");
        if (mobArena != null) {
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
        return "mobarena";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    public String getArenaStatus(Arena arena, Boolean colored) {
        if (colored == true) {
            if (arena.inEditMode()) {
                return "§eEditing";
            } else if (arena.isRunning()) {
                return "§6Running";
            } else if (arena.isEnabled()) {
                return "§aAvailable";
            } else if (!arena.isEnabled()) {
                return "§cDisabled";
            }
        } else {
            if (arena.inEditMode()) {
                return "EDITING";
            } else if (arena.isRunning()) {
                return "RUNNING";
            } else if (arena.isEnabled()) {
                return "ENABLED";
            } else if (!arena.isEnabled()) {
                return "DISABLED";
            } else return "null";
        }
        return "";
    }

    public boolean isValid(Arena arena, Player player) {
        if (arena.inSpec((player)) || arena.getPlayersInLobby().contains(player) || arena.isDead(player)){
            return false;
        } else return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {


        final String[] args = identifier.split("\\_");
        final String arenaName = args[0];

        // global placeholders

        if (identifier.equalsIgnoreCase("total_enabled")) {
            return String.valueOf(mobArena.getArenaMaster().getEnabledArenas().size());
        } else if (identifier.equalsIgnoreCase("total_playing")) {
            return String.valueOf(mobArena.getArenaMaster().getAllPlayers().size());
        }


        // get the arena the player is in.
        Arena playerArena = mobArena.getArenaMaster().getArenaWithPlayer(player.getName());
        // get the arena from the given arguments
        Arena selectedArena = mobArena.getArenaMaster().getArenaWithName(arenaName);

        // check if the user is parsing player-arena
        if (identifier.contains("player-arena")) {
            /* Return an empty string if the player isn't in an arena,
            or if they're in the lobby,
            or if they're a spectator */
            if (playerArena == null || isValid(playerArena, player.getPlayer()) == false) {
                return "";
            }
            // Player-Arena placeholders. E.G player-arena_{variable}
            switch (identifier) {
                case "player-arena_name":
                    return playerArena.getSlug();
                case "player-arena_wave":
                    return String.valueOf(playerArena.getWaveManager().getWaveNumber());
                case "player-arena_final-wave": {
                    // check if an arena has a final wave
                    if (playerArena.getWaveManager().getFinalWave() > 0) {
                        return String.valueOf(playerArena.getWaveManager().getFinalWave());
                    } else return "∞";
                }
                case "player-arena_remaining-mobs":
                    return String.valueOf(playerArena.getMonsterManager().getMonsters().size());
                case "player-arena_kills":
                    return String.valueOf(playerArena.getArenaPlayer(player.getPlayer()).getStats().getInt("kills"));
                case "player-arena_ready":
                    return String.valueOf(playerArena.getReadyPlayersInLobby().size());
                case "player-arena_non-ready":
                    return String.valueOf(playerArena.getNonreadyPlayers().size());
                case "player-arena_players": {
                    if (playerArena.getPlayersInArena().size() > 1) {
                        return String.valueOf(playerArena.getPlayersInArena().size());
                    } else return "0";
                }
                case "player-arena_min-players":
                    return String.valueOf(playerArena.getMinPlayers());
                case "player-arena_max-players":
                    return String.valueOf(playerArena.getMaxPlayers());
                case "player-arena_auto-start-timer": {
                    if (playerArena.getAutoStartTimer().isRunning()) {
                        // divide by 20 to convert from Ticks -> Seconds
                        return String.valueOf(playerArena.getAutoStartTimer().getRemaining() / 20);
                    } else return "0";
                }
                case "player-arena_isready": {
                    if (playerArena.getReadyPlayersInLobby().contains(player.getPlayer()))
                        return "§aReady";
                    else if (!playerArena.getReadyPlayersInLobby().contains(player.getPlayer()))
                        return "§cNot Ready";
                    else return "§aPlaying";
                }
                default:
                    return "PNF";
            }
        }
        //selected arena placeholders. I.E arenaName_ID
        if (args.length == 2) {
            final String param = args[1];
            switch (param) {
                case "name":
                    return selectedArena.getSlug();
                case "wave":
                    return String.valueOf(selectedArena.getWaveManager().getWaveNumber());
                case "final-wave": {
                    if (selectedArena.getWaveManager().getFinalWave() > 0) {
                        return String.valueOf(selectedArena.getWaveManager().getFinalWave());
                    } else return "∞";
                }
                case "remaining-mobs":
                    return String.valueOf(selectedArena.getMonsterManager().getMonsters().size());
                case "ready":
                    return String.valueOf(selectedArena.getReadyPlayersInLobby().size());
                case "non-ready":
                    return String.valueOf(selectedArena.getNonreadyPlayers().size());
                case "players":
                    if (selectedArena.getPlayersInArena().size() > 1) {
                        return String.valueOf(selectedArena.getPlayersInArena().size());
                    } else return "0";
                case "min-players":
                    return String.valueOf(selectedArena.getMinPlayers());
                case "max-players":
                    return String.valueOf(selectedArena.getMaxPlayers());
                case "auto-start-timer": {
                    if (selectedArena.getAutoStartTimer().isRunning()) {
                        // divided to turn from ticks to seconds
                        return String.valueOf(selectedArena.getAutoStartTimer().getRemaining() / 20);
                    } else return "0";
                }
                case "player-status": {
                    if (selectedArena.isDead(player.getPlayer())) {
                        return "§cDead";
                    } else if (selectedArena.inSpec(player.getPlayer())) {
                        return "§7Spectating";
                    } else if (selectedArena.getPlayersInArena().contains(player.getPlayer())) {
                        return "§aPlaying";
                    } else return "§7Not Playing";
                }
                case "status": {
                    return getArenaStatus(selectedArena, false);
                }
                case "status-colored": {
                    return getArenaStatus(selectedArena, true);
                }
            }
        }
        return null;
    }
}