package org.mobarena.placeholders;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
    void returnsBlankIfPlayerNotInArenaSession() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        Arena arena = mock(Arena.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(lookup.lookupByPlayer(player)).thenReturn(arena);
        when(arena.inArena(player)).thenReturn(false);

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

    @Nested
    class Placeholders {

        OfflinePlayer target;
        ArenaPlayerStatistics stats;

        @BeforeEach
        void setup() {
            target = mock(OfflinePlayer.class);
            Player player = mock(Player.class);
            Arena arena = mock(Arena.class);
            ArenaPlayer ap = mock(ArenaPlayer.class);
            stats = mock(ArenaPlayerStatistics.class);
            when(target.isOnline()).thenReturn(true);
            when(target.getPlayer()).thenReturn(player);
            when(lookup.lookupByPlayer(player)).thenReturn(arena);
            when(arena.inArena(player)).thenReturn(true);
            when(arena.getArenaPlayer(player)).thenReturn(ap);
            when(ap.getStats()).thenReturn(stats);
        }

        @Test
        void className() {
            String name = "Knight";
            when(stats.getClassName()).thenReturn(name);

            String result = subject.resolve(target, "class");

            assertThat(result, equalTo(name));
        }

        @Test
        void kills() {
            int kills = 42;
            when(stats.getInt("kills")).thenReturn(kills);

            String result = subject.resolve(target, "kills");

            String expected = String.valueOf(kills);
            assertThat(result, equalTo(expected));
        }

        @Test
        void damageDone() {
            int damage = 1337;
            when(stats.getInt("dmgDone")).thenReturn(damage);

            String result = subject.resolve(target, "damage-done");

            String expected = String.valueOf(damage);
            assertThat(result, equalTo(expected));
        }

        @Test
        void damageTaken() {
            int damage = 1;
            when(stats.getInt("dmgTaken")).thenReturn(damage);

            String result = subject.resolve(target, "damage-taken");

            String expected = String.valueOf(damage);
            assertThat(result, equalTo(expected));
        }

        @Test
        void swings() {
            int swings = 1234;
            when(stats.getInt("swings")).thenReturn(swings);

            String result = subject.resolve(target, "swings");

            String expected = String.valueOf(swings);
            assertThat(result, equalTo(expected));
        }

        @Test
        void hits() {
            int hits = 12;
            when(stats.getInt("hits")).thenReturn(hits);

            String result = subject.resolve(target, "hits");

            String expected = String.valueOf(hits);
            assertThat(result, equalTo(expected));
        }

        @Test
        void lastWave() {
            int wave = 21;
            when(stats.getInt("lastWave")).thenReturn(wave);

            String result = subject.resolve(target, "last-wave");

            String expected = String.valueOf(wave);
            assertThat(result, equalTo(expected));
        }

    }

}
