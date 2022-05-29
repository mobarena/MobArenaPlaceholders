package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class ArenaLookup {

    private final MobArena mobarena;

    ArenaLookup(Plugin mobarena) {
        this.mobarena = (MobArena) mobarena;
    }

    Arena lookup(OfflinePlayer target, String name) {
        if (name.equals("$current")) {
            if (target == null || !target.isOnline()) {
                return null;
            }
            Player player = target.getPlayer();
            if (player == null) {
                return null;
            }
            return lookupByPlayer(player);
        } else {
            return lookupByName(name);
        }
    }

    Arena lookupByPlayer(Player player) {
        if (player == null) {
            return null;
        }
        ArenaMaster am = mobarena.getArenaMaster();
        return am.getArenaWithPlayer(player);
    }

    Arena lookupByName(String name) {
        ArenaMaster am = mobarena.getArenaMaster();
        return am.getArenaWithName(name);
    }

}
