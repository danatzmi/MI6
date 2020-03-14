package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionRecievedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private List<MissionInfo> missions;
	private int index;

	public Intelligence(int index) {
		super("Intelligence" + index);
		this.index = index;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, b -> {
			if(b.getTime() > 0) {
				for (MissionInfo mission : missions) {
					if (mission.getTimeIssued() == b.getTime()) {
						getSimplePublisher().sendEvent(new MissionRecievedEvent(mission));
					}
				}
			}
		});

		subscribeBroadcast(TerminateBroadcast.class, b -> {
			terminate();
		});
	}

	public List<MissionInfo> getMissions() {
		return missions;
	}

	public void setMissions(List<MissionInfo> missions) {
		this.missions = missions;
	}
}
