package org.mobarena.placeholders;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerResolverTest {

    ArenaLookup lookup;
    PlayerResolver subject;

    @BeforeEach
    void setup() {
        lookup = mock(ArenaLookup.class);
        subject = new PlayerResolver(lookup);
    }

    @Test
    void returnsNullIfTailIsNull() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, null);

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfPlaceholderIsInvalid() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "cheese");

        assertThat(result, nullValue());
    }

    @Test
    void returnsBlankIfPlayerIsNull() {
        String result = subject.resolve(null, "kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankIfPlayerNotInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(lookup.lookupByPlayer(player)).thenReturn(null);

        String result = subject.resolve(target, "kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsKillCountFromStats() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        Arena arena = mock(Arena.class);
        ArenaPlayer ap = mock(ArenaPlayer.class);
        ArenaPlayerStatistics stats = mock(ArenaPlayerStatistics.class);
        int kills = 42;
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(lookup.lookupByPlayer(player)).thenReturn(arena);
        when(arena.inArena(player)).thenReturn(true);
        when(arena.getArenaPlayer(player)).thenReturn(ap);
        when(ap.getStats()).thenReturn(stats);
        when(stats.getInt("kills")).thenReturn(kills);

        String result = subject.resolve(target, "kills");

        String expected = String.valueOf(kills);
        assertThat(result, equalTo(expected));
    }

}
