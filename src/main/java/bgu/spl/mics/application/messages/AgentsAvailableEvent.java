package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Trio;

import java.util.List;

public class AgentsAvailableEvent implements Event<Trio<List<String>, Integer, Future<Integer>>> {

    private List<String> serialNumbers;

    public AgentsAvailableEvent(List<String> serialNumbers) { this.serialNumbers = serialNumbers; }

    public List<String> getSerialNumbers() {
        return serialNumbers;
    }
}
