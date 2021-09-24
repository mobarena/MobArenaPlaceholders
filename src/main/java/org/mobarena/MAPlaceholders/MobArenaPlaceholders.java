package org.mobarena.MAPlaceholders;

import com.garbagemule.MobArena.MobArena;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.mobarena.stats.MobArenaStats;
import org.mobarena.stats.MobArenaStatsPlugin;

import java.util.HashMap;
import java.util.Map;

public class MobArenaPlaceholders extends PlaceholderExpansion implements Configurable {

    private ArenaResolver arenaResolver;
    private PlayerResolver playerResolver;
    private StatsResolver statsResolver;

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

    @Override @NotNull
    public String getRequiredPlugin() {
        return "MobArena";
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("editing", "Editing");
        defaults.put("available", "Available");
        defaults.put("disabled", "Disabled");
        defaults.put("running", "Running");
        return defaults;
    }

    @Override
    public boolean register() {
        if (!super.register()) {
            return false;
        }
        PluginManager manager = Bukkit.getPluginManager();
        MobArena mobarena = (MobArena) manager.getPlugin(getRequiredPlugin());
        MobArenaStats mastats = (MobArenaStatsPlugin) manager.getPlugin("MobArenaStats");
        arenaResolver = new ArenaResolver(mobarena, getConfigSection());
        playerResolver = new PlayerResolver(mobarena);
        statsResolver = new StatsResolver(mobarena, mastats);
        return true;
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

