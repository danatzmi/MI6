package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {

    private MessageBroker mbTest;

    @BeforeEach
    public void setUp() {
        mbTest = MessageBrokerImpl.getInstance();
    }

    @Test
    public void subscribeEvent() {
        Subscriber m1 = new M(1);
        mbTest.register(m1);
        Event<String> e = new ExampleEvent("e");
        try {
            mbTest.subscribeEvent(ExampleEvent.class, m1);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void subscribeBroadcast() {
        Subscriber m1 = new M(1);
        mbTest.register(m1);
        Broadcast b = new ExampleBroadcast("b");
        try {
            mbTest.subscribeBroadcast(ExampleBroadcast.class, m1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void complete() {
//        Subscriber m1 = new M(1);
//        Event<String> e = new ExampleEvent("e");
//        try {
//            mbTest.complete(e, m1);
//        } catch (Exception ex) {
//            fail();
//        }
    }

    @Test
    public void sendBroadcast() {
        Broadcast b = new ExampleBroadcast("b");
        try {
            mbTest.sendBroadcast(b);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void sendEvent() {
        Event<String> e = new ExampleEvent("e");
        try {
            mbTest.sendEvent(e);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void register() {
        Subscriber mp = new Moneypenny(1);
        try {
            mbTest.register(mp);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void unregister() {
        Subscriber mp = new Moneypenny(1);
        mbTest.register(mp);
        try {
            mbTest.unregister(mp);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void awaiteMessage() {
        Subscriber mp = new Moneypenny(1);
        mbTest.register(mp);
        Event<String> e = new ExampleEvent("e");
        mbTest.subscribeEvent(ExampleEvent.class, mp);
        mbTest.sendEvent(e);
        try {
            mbTest.awaitMessage(mp);
        } catch (Exception ex) {
            fail();
        }
    }
}