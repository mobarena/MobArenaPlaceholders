package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mobarena.stats.MobArenaStatsPlugin;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsResolverTest {

    MobArena mobarena;
    MobArenaStatsPlugin mastats;
    StatsResolver subject;

    @BeforeEach
    void setup() {
        mobarena = mock(MobArena.class);
        mastats = mock(MobArenaStatsPlugin.class);
        subject = new StatsResolver(mobarena, mastats);
    }

    @Test
    void returnsNullIfRestIsNull() {
        OfflinePlayer target = mock(OfflinePlayer.class);

        String result = subject.resolve(target, null);

        assertThat(result, nullValue());
    }

}
