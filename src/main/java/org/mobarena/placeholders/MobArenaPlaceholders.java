package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
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
        Plugin mobarena = manager.getPlugin(getRequiredPlugin());
        Plugin mastats = manager.getPlugin("MobArenaStats");
        if (mobarena == null) {
            return false;
        }
        // Casts :(
        arenaResolver = new ArenaResolver((MobArena) mobarena, getConfigSection());
        playerResolver = new PlayerResolver((MobArena) mobarena);
        if (mastats != null) {
            statsResolver = new StatsResolver((MobArena) mobarena, (MobArenaStatsPlugin) mastats);
        }
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
                if (statsResolver != null) {
                    return statsResolver.resolve(player, tail);
                }
                return "MobArenaStats could not be found! Please download and install it first!";
            }
            default: {
                return null;
            }
        }
    }

}

