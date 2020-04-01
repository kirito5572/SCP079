package me.kirito5572.scp079.listener;

import me.kirito5572.scp079.command.ConfigCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterListenerTest {

    public static final String USER_ID = "674941008265478157";
    private FilterListener filterListener;

    @Before
    public void setUp() {
        filterListener = new FilterListener();
        filterListener.onReady(null);
        new ConfigCommand();
    }

    @Test
    public void test() {
        Guild guild = mock(Guild.class);
        when(guild.getId()).thenReturn(USER_ID);
        assertEquals(USER_ID, guild.getId());
        GuildMessageReceivedEvent e = mock(GuildMessageReceivedEvent.class);
        when(e.getGuild()).thenReturn(guild);
        filterListener.onGuildMessageReceived(e);
    }
}