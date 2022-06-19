package org.mobarena.placeholders;

import com.garbagemule.MobArena.MonsterManager;
import com.garbagemule.MobArena.framework.Arena;
import com.garbagemule.MobArena.util.timer.AutoStartTimer;
import com.garbagemule.MobArena.waves.WaveManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class Placeholders {

        OfflinePlayer target;
        String slug = "castle";
        Arena arena;

        @BeforeEach
        void setup() {
            target = null;
            arena = mock(Arena.class);
            when(lookup.lookup(target, slug)).thenReturn(arena);
        }

        @Test
        void slug() {
            when(arena.getSlug()).thenReturn(slug);

            String result = subject.resolve(target, slug + "_slug");

            assertThat(result, equalTo(slug));
        }

        @Test
        void name() {
            String name = "Bob Island";
            when(arena.configName()).thenReturn(name);

            String result = subject.resolve(target, slug + "_name");

            assertThat(result, equalTo(name));
        }

        @Test
        void stateEditing() {
            String expected = "In Edit Mode";
            config.set("editing", expected);
            when(arena.inEditMode()).thenReturn(true);

            String result = subject.resolve(target, slug + "_state");

            assertThat(result, equalTo(expected));
        }

        @Test
        void stateRunning() {
            String expected = "Running";
            config.set("running", expected);
            when(arena.inEditMode()).thenReturn(false);
            when(arena.isRunning()).thenReturn(true);

            String result = subject.resolve(target, slug + "_state");

            assertThat(result, equalTo(expected));
        }

        @Test
        void stateAvailable() {
            String expected = "Open For Business";
            config.set("available", expected);
            when(arena.inEditMode()).thenReturn(false);
            when(arena.isRunning()).thenReturn(false);
            when(arena.isEnabled()).thenReturn(true);

            String result = subject.resolve(target, slug + "_state");

            assertThat(result, equalTo(expected));
        }

        @Test
        void stateDisabled() {
            String expected = "Under Construction";
            config.set("disabled", expected);
            when(arena.inEditMode()).thenReturn(false);
            when(arena.isRunning()).thenReturn(false);
            when(arena.isEnabled()).thenReturn(false);

            String result = subject.resolve(target, slug + "_state");

            assertThat(result, equalTo(expected));
        }

        @Test
        void minPlayers() {
            int threshold = 2;
            when(arena.getMinPlayers()).thenReturn(threshold);

            String result = subject.resolve(target, slug + "_min-players");

            String expected = String.valueOf(threshold);
            assertThat(result, equalTo(expected));
        }

        @Test
        void maxPlayers() {
            int threshold = 3;
            when(arena.getMaxPlayers()).thenReturn(threshold);

            String result = subject.resolve(target, slug + "_max-players");

            String expected = String.valueOf(threshold);
            assertThat(result, equalTo(expected));
        }

        @Test
        void autoStartTimerInSeconds() {
            long ticks = 85;
            int secs = (int) (ticks / 20);
            AutoStartTimer timer = mock(AutoStartTimer.class);
            when(arena.getAutoStartTimer()).thenReturn(timer);
            when(timer.getRemaining()).thenReturn(ticks);

            String result = subject.resolve(null, slug + "_auto-start-timer");

            String expected = String.valueOf(secs);
            assertThat(result, equalTo(expected));
        }

        @Test
        void currentWave() {
            int wave = 13;
            WaveManager waves = mock(WaveManager.class);
            when(arena.getWaveManager()).thenReturn(waves);
            when(waves.getWaveNumber()).thenReturn(wave);

            String result = subject.resolve(target, slug + "_current-wave");

            String expected = String.valueOf(wave);
            assertThat(result, equalTo(expected));
        }

        @Test
        void finalWaveNonZero() {
            int wave = 30;
            WaveManager waves = mock(WaveManager.class);
            when(arena.getWaveManager()).thenReturn(waves);
            when(waves.getFinalWave()).thenReturn(wave);

            String result = subject.resolve(target, slug + "_final-wave");

            String expected = String.valueOf(wave);
            assertThat(result, equalTo(expected));
        }

        @Test
        void finalWaveInfinity() {
            WaveManager waves = mock(WaveManager.class);
            when(arena.getWaveManager()).thenReturn(waves);
            when(waves.getFinalWave()).thenReturn(0);

            String result = subject.resolve(target, slug + "_final-wave");

            assertThat(result, equalTo("∞"));
        }

        @Test
        void lobbyPlayers() {
            Set<Player> players = Set.of(
                mock(Player.class),
                mock(Player.class),
                mock(Player.class)
            );
            when(arena.getPlayersInLobby()).thenReturn(players);

            String result = subject.resolve(target, slug + "_lobby-players");

            String expected = String.valueOf(players.size());
            assertThat(result, equalTo(expected));
        }

        @Test
        void readyPlayers() {
            Set<Player> players = Set.of(
                mock(Player.class)
            );
            when(arena.getReadyPlayersInLobby()).thenReturn(players);

            String result = subject.resolve(target, slug + "_ready-players");

            String expected = String.valueOf(players.size());
            assertThat(result, equalTo(expected));
        }

        @Test
        void livePlayers() {
            Set<Player> players = Set.of(
                mock(Player.class),
                mock(Player.class)
            );
            when(arena.getPlayersInArena()).thenReturn(players);

            String result = subject.resolve(target, slug + "_live-players");

            String expected = String.valueOf(players.size());
            assertThat(result, equalTo(expected));
        }

        @Test
        void deadPlayersNotRunning() {
            when(arena.isRunning()).thenReturn(false);

            String result = subject.resolve(target, slug + "_dead-players");

            assertThat(result, equalTo("0"));
        }

        @Test
        void deadPlayersRunning() {
            int initial = 15;
            Set<Player> players = Set.of(
                mock(Player.class),
                mock(Player.class)
            );
            when(arena.isRunning()).thenReturn(true);
            when(arena.getPlayerCount()).thenReturn(initial);
            when(arena.getPlayersInArena()).thenReturn(players);

            String result = subject.resolve(target, slug + "_dead-players");

            String expected = String.valueOf(initial - players.size());
            assertThat(result, equalTo(expected));
        }

        @Test
        void initialPlayersNotRunning() {
            when(arena.isRunning()).thenReturn(false);

            String result = subject.resolve(target, slug + "_initial-players");

            assertThat(result, equalTo("0"));
        }

        @Test
        void initialPlayersRunning() {
            int initial = 15;
            when(arena.isRunning()).thenReturn(true);
            when(arena.getPlayerCount()).thenReturn(initial);

            String result = subject.resolve(target, slug + "_initial-players");

            String expected = String.valueOf(initial);
            assertThat(result, equalTo(expected));
        }

        @Test
        void liveMobs() {
            Set<LivingEntity> monsters = Set.of(
                mock(LivingEntity.class),
                mock(LivingEntity.class),
                mock(LivingEntity.class),
                mock(LivingEntity.class)
            );
            MonsterManager manager = mock(MonsterManager.class);
            when(arena.getMonsterManager()).thenReturn(manager);
            when(manager.getMonsters()).thenReturn(monsters);

            String result = subject.resolve(target, slug + "_live-mobs");

            String expected = String.valueOf(monsters.size());
            assertThat(result, equalTo(expected));
        }

    }

}
