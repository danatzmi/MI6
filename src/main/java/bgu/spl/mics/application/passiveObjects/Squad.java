package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MessageBrokerImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private static class SquadHolder {
		private static Squad instance = new Squad();
	}

	private Map<String, Agent> agents;

	private Squad() {
		agents = new HashMap<>();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		return SquadHolder.instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		for (Agent a : agents) {
			this.agents.put(a.getSerialNumber(), new Agent(a.getSerialNumber(), a.getName()));
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		for(String serialNumber : serials) {
			if(agents.get(serialNumber) != null)
				agents.get(serialNumber).release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time) throws InterruptedException {
		Thread.sleep(time * 100);
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		for (String s : serials) {
			if (agents.get(s) == null) {
				return false;
			}
		}
		for (String s : serials) {
			while (!agents.get(s).isAvailable()){
				//System.out.println(agents.get(s).getName());
			}
			agents.get(s).acquire();
		}
		return true;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        List<String> output = new LinkedList<>();
        for (String s : serials)
        	output.add(agents.get(s).getName());
        return output;
    }
}
