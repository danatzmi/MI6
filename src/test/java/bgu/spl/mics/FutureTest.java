package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    Future<Object> futureTest;

    @BeforeEach
    public void setUp(){
        futureTest = new Future<>();
    }

    @Test
    public void get() {
    }

    @Test
    public void resolve() {
        futureTest.resolve("gun");
        assertEquals(futureTest.get(), "gun");
    }

    @Test
    public void isDone() {
        assertFalse(futureTest.isDone());
        futureTest.resolve("gun");
        assertTrue(futureTest.isDone());
    }

}
