package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Pair;


public class GadgetAvailableEvent implements Event<Pair<String, Integer>> {

    private String gadget;

    public GadgetAvailableEvent(String gadget) {
        this.gadget = gadget;
    }

    public String getGadget() {
        return gadget;
    }

}

