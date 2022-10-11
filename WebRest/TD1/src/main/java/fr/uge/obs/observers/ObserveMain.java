package fr.uge.obs.observers;

import java.rmi.Naming;

import fr.uge.obs.common.IObserved;
import fr.uge.obs.common.IObserver;

public class ObserveMain {
    public static void main(String[] args) {
        try {
            IObserver obs1 = new Observer();
            IObserver obs2 = new Observer();
            IObserved sub = (IObserved) Naming.lookup("rmi://localhost:1099/observee");
            
            sub.subscribe(obs1);
            sub.subscribe(obs2);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
