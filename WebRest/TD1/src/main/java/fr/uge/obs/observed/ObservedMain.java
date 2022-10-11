package fr.uge.obs.observed;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ObservedMain {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Observed obs = new Observed();
            Naming.rebind("rmi://localhost:1099/observee", obs);
            for(int i = 1; i <= 30; i++) {
                obs.changeValue(i*10);
                Thread.sleep(1000);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
