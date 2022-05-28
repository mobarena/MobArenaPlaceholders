package org.mobarena.placeholders;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.ArenaPlayerStatistics;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.framework.ArenaMaster;
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

    MobArena mobarena;
    PlayerResolver subject;

    @BeforeEach
    void setup() {
        mobarena = mock(MobArena.class);
        subject = new PlayerResolver(mobarena);
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
    void returnsBlankIfPlayerIsOffline() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(target.isOnline()).thenReturn(false);

        String result = subject.resolve(target, "kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankIfPlayerDoesNotExist() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(null);

        String result = subject.resolve(target, "kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsBlankIfPlayerNotInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        ArenaMaster am = mock(ArenaMaster.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(player.getPlayer()).thenReturn(player);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithPlayer(player)).thenReturn(null);

        String result = subject.resolve(target, "kills");

        assertThat(result, equalTo(""));
    }

    @Test
    void returnsKillCountFromStats() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        ArenaMaster am = mock(ArenaMaster.class);
        Arena arena = mock(Arena.class);
        ArenaPlayer ap = mock(ArenaPlayer.class);
        ArenaPlayerStatistics stats = mock(ArenaPlayerStatistics.class);
        int kills = 42;
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(player.getPlayer()).thenReturn(player);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithPlayer(player)).thenReturn(arena);
        when(arena.inArena(player)).thenReturn(true);
        when(arena.getArenaPlayer(player)).thenReturn(ap);
        when(ap.getStats()).thenReturn(stats);
        when(stats.getInt("kills")).thenReturn(kills);

        String result = subject.resolve(target, "kills");

        String expected = String.valueOf(kills);
        assertThat(result, equalTo(expected));
    }

}
