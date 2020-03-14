package bgu.spl.mics.application;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.Intelligence;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;

import java.io.IOException;
import java.util.*;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) throws IOException {
        try {
            Input input = JsonInputReader.getInputFromJson(args[0]);
            List<Thread> threads = new LinkedList<>();

            //MessageBroker init
            MessageBroker messageBroker = MessageBrokerImpl.getInstance();

            //Inventory init
            Inventory inventory = Inventory.getInstance();
            inventory.load(input.getInventory());

            //Squad init
            Squad squad = Squad.getInstance();
            squad.load(input.getSquad());

            //Diary init
            Diary diary = Diary.getInstance();

            //Subscribe M
            for(int i = 1; i <= input.getServices().getM(); i++) {
                Subscriber m = new M(i);
                Thread t = new Thread(m);
                threads.add(t);
            }

            //Subscribe MoneyPenny
            for(int i = 1; i <= input.getServices().getMoneypenny(); i++) {
                Subscriber mp = new Moneypenny(i);
                Thread t = new Thread(mp);
                threads.add(t);
            }

            //Subscribe Q
            Subscriber q = Q.getInstance();
            threads.add(new Thread(q));

            //Intelligence init
            for(int i = 1; i <= input.getServices().getIntelligence().length; i++) {
                Intelligence intel = new Intelligence(i);
                intel.setMissions(input.getServices().getIntelligence()[i - 1].getMissions());
                Thread t = new Thread(intel);
                threads.add(t);
            }

            //TimeService init
            TimeService timeService = TimeService.getInstance();
            timeService.setDuration(input.getServices().getTime());
            threads.add(new Thread(timeService));

            for(Thread t : threads) {
                t.start();
            }

            for(Thread t : threads) {
                t.join();
            }

            inventory.printToFile(args[1]);
            diary.printToFile(args[2]);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
