package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {

	private static class TimeServiceHolder {
		private static TimeService instance = new TimeService();
	}

	private final long TEMPO = 100;

	private Timer timer;
	private int duration;
	private int currentTime;

	private TimeService() {
		super("TimeService");
		timer = new Timer();
		currentTime = 0;
	}

	public static TimeService getInstance() {
		return TimeServiceHolder.instance;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	protected void initialize() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (duration > 0) {
					duration = duration - 1;
					currentTime++;
					getSimplePublisher().sendBroadcast(new TickBroadcast(currentTime));
				}
				else {
					timer.cancel();
					timer.purge();
					getSimplePublisher().sendBroadcast(new TickBroadcast(-1));
				}
			}
		};
		timer.schedule(task, TEMPO, TEMPO);
	}

	@Override
	public void run() {
		initialize();
	}

}
