package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Pair;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.passiveObjects.Trio;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {

	private Squad squad;
	private int index;

	public Moneypenny(int index) {
		super("MoneyPenny" + index);
		squad = Squad.getInstance();
		this.index = index;
	}

	@Override
	protected void initialize() {
		subscribeEvent(AgentsAvailableEvent.class, ev -> {
			if(shouldTerminate) {
				complete(ev, null);
			} else {
				synchronized (squad) {
					List<String> serialNumbers = ev.getSerialNumbers();
					if (squad.getAgents(ev.getSerialNumbers())) {
						Pair<List<String>, Integer> p = new Pair<>(squad.getAgentsNames(serialNumbers), index);
						Future<Integer> sendAgentsFuture = new Future<>();
						complete(ev, new Trio<>(p.getFirst(), p.getSecond(), sendAgentsFuture));
						Integer sendAgentsResult = sendAgentsFuture.get();
						if (sendAgentsResult != null) {
							squad.sendAgents(serialNumbers, sendAgentsResult);
						} else {
							squad.releaseAgents(serialNumbers);
						}

					} else {
						squad.releaseAgents(serialNumbers);
						complete(ev, null);
					}
				}
			}
		});

		subscribeBroadcast(TickBroadcast.class, b -> {
			if(b.getTime() == -1)
				shouldTerminate = true;
		});

		subscribeBroadcast(TerminateBroadcast.class, b -> {
			terminate();
		});
	}
}
