package org.mobarena.placeholders;

import com.garbagemule.MobArena.MobArena;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
