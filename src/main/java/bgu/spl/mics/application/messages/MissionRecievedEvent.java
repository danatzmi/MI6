package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionRecievedEvent implements Event<MissionInfo> {

    private MissionInfo missionInfo;

    public MissionRecievedEvent(MissionInfo mission) { this.missionInfo = mission; }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }
}
