package org.mobarena.placeholders;

import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class MobArenaPlaceholders extends PlaceholderExpansion implements Configurable {

    private ArenaResolver arenaResolver;
    private PlayerResolver playerResolver;
    private StatsResolver statsResolver;

    @Override
    @NotNull
    public String getAuthor() {
        return "Maroon28";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "mobarena";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    @NotNull
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

        ArenaLookup lookup = new ArenaLookup(mobarena);
        arenaResolver = new ArenaResolver(lookup, getConfigSection());
        playerResolver = new PlayerResolver(lookup);
        if (mastats != null) {
            statsResolver = new StatsResolver(lookup, mastats);
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
                if (statsResolver == null) {
                    warning("Failed to resolve \"mobarena_" + identifier + "\", because the MobArenaStats extension was not found!");
                    return null;
                }
                return statsResolver.resolve(player, tail);
            }
            default: {
                return null;
            }
        }
    }

}
