package me.kirito5572.scp079.command;

import me.kirito5572.scp079.object.SQLDB;
import org.junit.Before;
import org.junit.Test;

import java.sql.Statement;

import static org.junit.Assert.*;

public class ConfigCommandTest {

    private ConfigCommand configCommand;
    private SQLDB sqldb;

    @Before
    public void setUp() {
        sqldb = new SQLDB();
        configCommand = new ConfigCommand();
    }

    @Test
    public void testReload() {


    }



}