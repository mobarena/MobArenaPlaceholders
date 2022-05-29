package org.mobarena.placeholders;

import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mobarena.stats.MobArenaStatsPlugin;
import org.mobarena.stats.store.ArenaStats;
import org.mobarena.stats.store.GlobalStats;
import org.mobarena.stats.store.PlayerStats;
import org.mobarena.stats.store.StatsStore;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsResolverTest {

    ArenaLookup lookup;
    MobArenaStatsPlugin mastats;
    StatsResolver subject;

    @BeforeEach
    void setup() {
        lookup = mock(ArenaLookup.class);
        mastats = mock(MobArenaStatsPlugin.class);
        subject = new StatsResolver(lookup, mastats);
    }

    @Test
    void returnsNullIfRestIsNull() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, null);

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfRestIsInvalid() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "cheese");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfRestIsJustQualifier() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result1 = subject.resolve(target, "global");
        String result2 = subject.resolve(target, "arena");
        String result3 = subject.resolve(target, "player");

        assertThat(result1, nullValue());
        assertThat(result2, nullValue());
        assertThat(result3, nullValue());
    }

    @Test
    void returnsNullIfGlobalRestIsInvalid() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "global_cheese");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfArenaRestIsInvalid() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "arena_$current_cheese");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfPlayerRestIsInvalid() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "player_cheese");

        assertThat(result, nullValue());
    }

    @Test
    void returnsBlankForCurrentIfPlayerNotInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(lookup.lookup(target, "$current")).thenReturn(null);

        String result = subject.resolve(target, "arena_$current_total-kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankIfArenaNotFound() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        String slug = "castle";
        when(lookup.lookup(target, slug)).thenReturn(null);

        String result = subject.resolve(target, "arena_" + slug + "_total-kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsTotalKillsFromGlobal() {
        int kills = 42;
        StatsStore store = mock(StatsStore.class);
        GlobalStats stats = new GlobalStats(1, 2, kills, 4);
        when(mastats.getStatsStore()).thenReturn(store);
        when(store.getGlobalStats()).thenReturn(stats);

        String result = subject.resolve(null, "global_total-kills");

        String expected = String.valueOf(kills);
        assertThat(result, equalTo(expected));
    }

    @Test
    void returnsTotalKillsFromCurrentArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        String slug = "castle";
        int kills = 42;
        Arena arena = mock(Arena.class);
        StatsStore store = mock(StatsStore.class);
        ArenaStats stats = new ArenaStats(1, 2, 3, 4, 5, kills, 7);
        when(lookup.lookup(target, "$current")).thenReturn(arena);
        when(arena.getSlug()).thenReturn(slug);
        when(mastats.getStatsStore()).thenReturn(store);
        when(store.getArenaStats(slug)).thenReturn(stats);

        String result = subject.resolve(target, "arena_$current_total-kills");

        String expected = String.valueOf(kills);
        assertThat(result, equalTo(expected));
    }

    @Test
    void returnsLivePlayerCountFromArenaKey() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        String slug = "castle";
        int kills = 42;
        Arena arena = mock(Arena.class);
        StatsStore store = mock(StatsStore.class);
        ArenaStats stats = new ArenaStats(1, 2, 3, 4, 5, kills, 7);
        when(lookup.lookup(target, slug)).thenReturn(arena);
        when(arena.getSlug()).thenReturn(slug);
        when(mastats.getStatsStore()).thenReturn(store);
        when(store.getArenaStats(slug)).thenReturn(stats);

        String result = subject.resolve(target, "arena_" + slug + "_total-kills");

        String expected = String.valueOf(kills);
        assertThat(result, equalTo(expected));
    }

    @Test
    void returnsTotalKillsFromPlayer() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        String name = "garbagemule";
        int kills = 42;
        StatsStore store = mock(StatsStore.class);
        PlayerStats stats = new PlayerStats(1, 2, kills, 4);
        when(target.getName()).thenReturn(name);
        when(mastats.getStatsStore()).thenReturn(store);
        when(store.getPlayerStats(name)).thenReturn(stats);

        String result = subject.resolve(target, "player_total-kills");

        String expected = String.valueOf(kills);
        assertThat(result, equalTo(expected));
    }

}
