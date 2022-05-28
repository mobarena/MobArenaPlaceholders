package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
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

    MobArena mobarena;
    ConfigurationSection config;
    ArenaResolver subject;

    @BeforeEach
    void setup() {
        mobarena = mock(MobArena.class);
        config = new MemoryConfiguration();
        subject = new ArenaResolver(mobarena, config);
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
    void returnsBlankForCurrentIfPlayerIsNull() {
        String result = subject.resolve(null, "$current_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankForCurrentIfPlayerIsOffline() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(target.isOnline()).thenReturn(false);

        String result = subject.resolve(target, "$current_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankForCurrentIfPlayerDoesNotExist() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(null);

        String result = subject.resolve(target, "$current_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankForCurrentIfPlayerNotInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        ArenaMaster am = mock(ArenaMaster.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithPlayer(player)).thenReturn(null);

        String result = subject.resolve(target, "$current_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankIfArenaNotFound() {
        String slug = "castle";
        ArenaMaster am = mock(ArenaMaster.class);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithName(slug)).thenReturn(null);

        String result = subject.resolve(null, slug + "_live-players");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsLivePlayerCountFromCurrentArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        ArenaMaster am = mock(ArenaMaster.class);
        Arena arena = mock(Arena.class);
        Set<Player> players = Set.of(player);
        when(target.getPlayer()).thenReturn(player);
        when(target.isOnline()).thenReturn(true);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithPlayer(player)).thenReturn(arena);
        when(arena.getPlayersInArena()).thenReturn(players);

        String result = subject.resolve(target, "$current_live-players");

        String expected = String.valueOf(players.size());
        assertThat(result, equalTo(expected));
    }

    @Test
    void returnsLivePlayerCountFromArenaKey() {
        String slug = "castle";
        ArenaMaster am = mock(ArenaMaster.class);
        Arena arena = mock(Arena.class);
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Set<Player> players = Set.of(player1, player2);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithName(slug)).thenReturn(arena);
        when(arena.getPlayersInArena()).thenReturn(players);

        String result = subject.resolve(null, slug + "_live-players");

        String expected = String.valueOf(players.size());
        assertThat(result, equalTo(expected));
    }

}
