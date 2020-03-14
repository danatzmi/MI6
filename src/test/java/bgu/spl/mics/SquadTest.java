package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad squadTest;
    private Agent agent1;
    private Agent agent2;
    private Agent[] agents;

    @BeforeEach
    public void setUp(){
        squadTest = Squad.getInstance();
        agent1 = new Agent("007", "dan");
        agent2 = new Agent("00100", "amit");
        agents = new Agent[]{agent1, agent2};
    }

    @Test
    public void getInstance() {
        assertNotNull(squadTest);
        assertEquals(squadTest, Squad.getInstance());
    }

    @Test
    public void load () {
        squadTest.load(agents);
        List<String> names = squadTest.getAgentsNames(Arrays.asList("007" , "00100"));
        assertTrue(names.contains("dan"));
        assertTrue(names.contains("amit"));
        assertFalse(names.contains("or smolarski"));
    }

    @Test
    public void releaseAgents(){
        squadTest.load(agents);
        if (!agent1.isAvailable()) {
            squadTest.releaseAgents(Arrays.asList(agent1.getSerialNumber()));
            assertTrue(agent1.isAvailable());
        }
    }

    @Test
    public void sendAgents(){
    }

    @Test
    public void getAgents(){
        squadTest.load(agents);
        if (agent1.isAvailable()) {
            squadTest.getAgents(Arrays.asList(agent1.getSerialNumber()));
            assertFalse(agent1.isAvailable());
        }
    }

    @Test
    public void getAgentsNames(){
        squadTest.load(agents);
        List<String> names = squadTest.getAgentsNames(Arrays.asList(agent1.getSerialNumber(), agent2.getSerialNumber()));
        assertTrue(names.contains("dan"));
        assertTrue(names.contains("amit"));
        assertFalse(names.contains("or smolarski"));
    }

}
