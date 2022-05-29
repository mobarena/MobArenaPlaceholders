package org.mobarena.placeholders;

import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArenaResolverTest {

    ArenaLookup lookup;
    ConfigurationSection config;
    ArenaResolver subject;

    @BeforeEach
    void setup() {
        lookup = mock(ArenaLookup.class);
        config = new MemoryConfiguration();
        subject = new ArenaResolver(lookup, config);
    }

    @Test
    void returnsNullIfRestIsNull() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, null);

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfRestIsJustArenaKey() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "$current");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullIfPlaceholderIsInvalid() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, "$current_cheese");

        assertThat(result, nullValue());
    }

    @Test
    void returnsBlankForCurrentIfPlayerNotInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(lookup.lookup(target, "$current")).thenReturn(null);

        String result = subject.resolve(target, "$current_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankIfArenaNotFound() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        String slug = "castle";
        when(lookup.lookup(target, slug)).thenReturn(null);

        String result = subject.resolve(target, slug + "_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsLivePlayerCountFromCurrentArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        Arena arena = mock(Arena.class);
        Set<Player> players = Set.of(player);
        when(lookup.lookup(target, "$current")).thenReturn(arena);
        when(arena.getPlayersInArena()).thenReturn(players);

        String result = subject.resolve(target, "$current_live-players");

        String expected = String.valueOf(players.size());
        assertThat(result, equalTo(expected));
    }

    @Test
    void returnsLivePlayerCountFromArenaKey() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        String slug = "castle";
        Arena arena = mock(Arena.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Set<Player> players = Set.of(player1, player2);
        when(lookup.lookup(target, slug)).thenReturn(arena);
        when(arena.getPlayersInArena()).thenReturn(players);

        String result = subject.resolve(target, slug + "_live-players");

        String expected = String.valueOf(players.size());
        assertThat(result, equalTo(expected));
    }

}
