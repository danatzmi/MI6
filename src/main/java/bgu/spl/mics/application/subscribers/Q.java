package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Pair;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private static class QHolder {
		private static Q instance = new Q();
	}

	private Inventory inventory;
	private Integer qTime;

	private Q() {
		super("Q");
		inventory = Inventory.getInstance();
		qTime = 0;
	}

	public static Q getInstance() {
		return QHolder.instance;
	}

	@Override
	protected void initialize() {
		subscribeEvent(GadgetAvailableEvent.class, ev -> {
			if(shouldTerminate) {
				complete(ev, null);
			} else {
				if (inventory.getItem(ev.getGadget())) {
					Pair<String, Integer> p = new Pair<>(ev.getGadget(), qTime);
					complete(ev, p);
				} else {
					complete(ev, null);
				}
			}
		});

		subscribeBroadcast(TickBroadcast.class, b -> {
			if(b.getTime() == -1) {
				shouldTerminate = true;
			} else {
				qTime++;
			}
		});

		subscribeBroadcast(TerminateBroadcast.class, b -> {
			terminate();
		});
	}

}
