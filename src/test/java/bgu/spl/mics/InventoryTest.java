package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    Inventory invTest;

    @BeforeEach
    public void setUp(){
        invTest = Inventory.getInstance();
    }

    @Test
    public void getInstance(){
        assertNotNull(invTest);
        assertEquals(invTest, Inventory.getInstance());
    }

    @Test
    public void load(){
        String[] gadgets = {"gun", "knife"};
        invTest.load(gadgets);

    }

    @Test
    public void getItem() {
        String[] gadgets = {"gun", "knife"};
        invTest.load(gadgets);
        assertTrue(invTest.getItem("gun"));
        assertFalse(invTest.getItem("gun"));
        assertFalse(invTest.getItem("sword"));
    }

    public void printToFile(){
    }
}
