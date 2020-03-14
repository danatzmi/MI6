package bgu.spl.mics;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

import java.util.concurrent.*;
import java.util.Queue;
import java.util.Map;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	private static class MessageBrokerImplHolder {
		private static MessageBrokerImpl instance = new MessageBrokerImpl();
	}

	private Map<Class<? extends Event>, Queue<Subscriber>> eventsMap;
	private Map<Class<? extends Broadcast>, Queue<Subscriber>> broadcastsMap;
	private Map<Subscriber, LinkedBlockingQueue<Message>> subscribersMap;
	private Map<Event<?>, Future<?>> futureMap;

	private MessageBrokerImpl(){
		eventsMap = new ConcurrentHashMap<>();
		broadcastsMap = new ConcurrentHashMap<>();
		subscribersMap = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerImplHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		eventsMap.computeIfAbsent(type, v -> new ConcurrentLinkedQueue<>());
		eventsMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		broadcastsMap.computeIfAbsent(type, v -> new ConcurrentLinkedQueue<>());
		broadcastsMap.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		synchronized (e) {
			Future<T> f = (Future) (futureMap.get(e));
			if (f != null)
				f.resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (broadcastsMap.get(b.getClass()) != null) {
			Queue<Subscriber> q = broadcastsMap.get(b.getClass());
			for (Subscriber s : q) {
				if(subscribersMap.get(s) != null) {
					subscribersMap.get(s).add(b);
				}
			}
		}
	}


	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		Future<T> output = null;
		while(eventsMap.get(e.getClass()) == null);
		if (eventsMap.get(e.getClass()) != null) {
			Queue<Subscriber> q = eventsMap.get(e.getClass());
			while (q.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			if(!q.isEmpty()) {
				Subscriber s = q.poll();
				subscribersMap.get(s).add(e);
				q.add(s);
				output = new Future<>();
				futureMap.put(e, output);
			}
		}
		return output;
	}

	@Override
	public void register(Subscriber m) {
		if (!subscribersMap.containsKey(m)) {
			subscribersMap.put(m, new LinkedBlockingQueue<>());
			if(subscribersMap.get(m).size() == 1)
				notifyAll();
		}
	}

	@Override
	public void unregister(Subscriber m) {
		if (subscribersMap.containsKey(m)) {
			subscribersMap.remove(m, subscribersMap.get(m));
			for (Map.Entry<Class<? extends Event>, Queue<Subscriber>> pair: eventsMap.entrySet()) {
				if(pair.getValue().contains(m))
					pair.getValue().remove(m);
			}
			for (Map.Entry<Class<? extends Broadcast>, Queue<Subscriber>> pair: broadcastsMap.entrySet()) {
				if(pair.getValue().contains(m))
					pair.getValue().remove(m);
			}
			if(m instanceof M) {
				if(eventsMap.get(MissionRecievedEvent.class).isEmpty())
					sendBroadcast(new TerminateBroadcast());
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		if(!subscribersMap.containsKey(m))
			throw new IllegalStateException();
		return subscribersMap.get(m).take();
	}

}
