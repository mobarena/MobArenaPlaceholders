package me.maroon28.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.mobarena.stats.MobArenaStats;
import org.mobarena.stats.MobArenaStatsPlugin;

public class MobArenaPlaceholders extends PlaceholderExpansion {

    private ArenaResolver arenaResolver;
    private PlayerResolver playerResolver;
    private StatsResolver statsResolver;

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

    @Override @NotNull
    public String getRequiredPlugin() {
        return "MobArena";
    }

    @Override
    public boolean register() {
        PluginManager manager = Bukkit.getPluginManager();
        MobArena mobarena = (MobArena) manager.getPlugin(getRequiredPlugin());
        MobArenaStats mastats = (MobArenaStatsPlugin) manager.getPlugin("MobArenaStats");
        arenaResolver = new ArenaResolver(mobarena);
        playerResolver = new PlayerResolver(mobarena);
        statsResolver = new StatsResolver(mobarena, mastats);
        return super.register();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        String[] parts = identifier.split("_", 2);
        String head = parts[0];
        String tail = (parts.length > 1) ? parts[1] : null;
        switch (head) {
            case "arena": {
                return arenaResolver.resolve(player, tail);
            }
            case "player": {
                return playerResolver.resolve(player, tail);
            }
            case "stats": {
                return statsResolver.resolve(player, tail);
            }
            default: {
                return null;
            }
        }
    }

}

