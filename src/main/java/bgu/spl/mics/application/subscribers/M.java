package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private MessageBroker messageBroker;
	private Diary diary;
	private int currentTime;
	private int index;


	public M(int index) {
		super("M" + index);
		messageBroker = MessageBrokerImpl.getInstance();
		diary = Diary.getInstance();
		currentTime = 0;
		this.index = index;
	}

	@Override
	protected void initialize() {
		subscribeEvent(MissionRecievedEvent.class, ev -> {
			diary.incrementTotal();
			Future<Trio<List<String>, Integer, Future<Integer>>> futureAgents = messageBroker.sendEvent(new AgentsAvailableEvent(ev.getMissionInfo().getSerialAgentsNumbers()));
			Trio<List<String>, Integer, Future<Integer>> agentsResult = null;
			if (futureAgents != null) {
				agentsResult = futureAgents.get();
			}
			if (agentsResult != null) {
				List<String> agentsNames = agentsResult.getFirst();
				List<String> serialNumbers = ev.getMissionInfo().getSerialAgentsNumbers();
				Future<Pair<String, Integer>> futureGadget = messageBroker.sendEvent(new GadgetAvailableEvent(ev.getMissionInfo().getGadget()));
				Pair<String, Integer> gadgetResult = null;
				if (futureGadget != null) {
					gadgetResult = futureGadget.get();
				}
				if (gadgetResult != null) {
					String gadget = gadgetResult.getFirst();
					if (ev.getMissionInfo().getTimeIssued() <= currentTime & currentTime <= ev.getMissionInfo().getTimeExpired()) {

						//Acknowledging MoneyPenny to call squad.sendAgents()
						agentsResult.getThird().resolve(ev.getMissionInfo().getDuration());

						//Creating new report that will be added to the Diary
						Report report = new Report(ev.getMissionInfo().getMissionName(), index,
								                   agentsResult.getSecond(), serialNumbers, agentsNames,
								                   gadget, gadgetResult.getSecond(),
								                   ev.getMissionInfo().getTimeIssued(), currentTime);
						diary.addReport(report);

					} else {
						//Acknowledging MoneyPenny to call squad.releaseAgents()
						agentsResult.getThird().resolve(null);
					}
				} else {
					//Acknowledging MoneyPenny to call squad.releaseAgents()
					agentsResult.getThird().resolve(null);
				}
			}
		});
		subscribeBroadcast(TickBroadcast.class, b -> {
			if (b.getTime() == -1) {
				terminate();
			} else {
				currentTime = b.getTime();
			}
		});
	}


}
