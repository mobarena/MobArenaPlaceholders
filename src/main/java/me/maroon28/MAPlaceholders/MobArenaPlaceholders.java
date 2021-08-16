package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.mobarena.stats.MobArenaStatsPlugin;

public class MobArenaPlaceholders extends PlaceholderExpansion {

    MobArena mobarena;
    MobArenaStatsPlugin mastats;

    ArenaResolver arenaResolver;
    StatsResolver statsResolver;
    PlayerResolver playerResolver;

    @Override
    public String getRequiredPlugin() {
        return "MobArena";
    }

    @Override
    public boolean register() {
        PluginManager manager = Bukkit.getPluginManager();
        mobarena = (MobArena) manager.getPlugin(getRequiredPlugin());
        mastats = (MobArenaStatsPlugin) manager.getPlugin("MobArenaStats");
        arenaResolver = new ArenaResolver(mobarena);
        statsResolver = new StatsResolver(mobarena, mastats);
        playerResolver = new PlayerResolver(mobarena);
        return super.register();
    }

    @Override
    public String getAuthor() {
        return "Maroon28, garbagemule";
    }

    @Override
    public String getIdentifier() {
        return "mapapi";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        // arena_default_wave
        // arena default_wave
        String[] parts = identifier.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        switch (head) {
            case "stats": {
                return statsResolver.resolve(player, tail);
            }
            case "arena": {
                return arenaResolver.resolve(player, tail);
            }
            case "player": {
                return playerResolver.resolvePlayerPlaceholder(player, tail);
            }
            default: {
                return null; // placeholder not recognized.
            }
        }
    }
}

