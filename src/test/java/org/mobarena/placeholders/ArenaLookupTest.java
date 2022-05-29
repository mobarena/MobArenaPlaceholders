package org.mobarena.placeholders;

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
class ArenaLookupTest {

    MobArena mobarena;
    ArenaLookup subject;

    @BeforeEach
    void setup() {
        mobarena = mock(MobArena.class);
        subject = new ArenaLookup(mobarena);
    }

    @Test
    void returnsNullForNameIfArenaDoesNotExist() {
        String name = "castle";
        ArenaMaster am = mock(ArenaMaster.class);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithName(name)).thenReturn(null);

        Arena result = subject.lookup(null, name);

        assertThat(result, nullValue());
    }

    @Test
    void returnsArenaForNameIfArenaExists() {
        String name = "castle";
        ArenaMaster am = mock(ArenaMaster.class);
        Arena arena = mock(Arena.class);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithName(name)).thenReturn(arena);

        Arena result = subject.lookup(null, name);

        assertThat(result, sameInstance(arena));
    }

    @Test
    void returnsNullForCurrentIfPlayerIsNull() {
        Arena result = subject.lookup(null, "$current");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullForCurrentIfPlayerIsNotOnline() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(target.isOnline()).thenReturn(false);

        Arena result = subject.lookup(target, "$current");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullForCurrentIfPlayerDoesNotExist() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(null);

        Arena result = subject.lookup(target, "$current");

        assertThat(result, nullValue());
    }

    @Test
    void returnsNullForCurrentIfPlayerIsNotInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        ArenaMaster am = mock(ArenaMaster.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithPlayer(player)).thenReturn(null);

        Arena result = subject.lookup(target, "$current");

        assertThat(result, nullValue());
    }

    @Test
    void returnsArenaForCurrentIfPlayerIsInArena() {
        OfflinePlayer target = mock(OfflinePlayer.class);
        Player player = mock(Player.class);
        ArenaMaster am = mock(ArenaMaster.class);
        Arena arena = mock(Arena.class);
        when(target.isOnline()).thenReturn(true);
        when(target.getPlayer()).thenReturn(player);
        when(mobarena.getArenaMaster()).thenReturn(am);
        when(am.getArenaWithPlayer(player)).thenReturn(arena);

        Arena result = subject.lookup(target, "$current");

        assertThat(result, sameInstance(arena));
    }

}
