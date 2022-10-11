package fr.uge.obs.observed;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.obs.common.IObserved;
import fr.uge.obs.common.IObserver;

public class Observed  extends UnicastRemoteObject implements IObserved {
    private final List<IObserver> observers;
    private int value;

    Observed() throws RemoteException {
        observers = new ArrayList<>();
    }
    
    @Override
    public void subscribe(IObserver observer) throws RemoteException {
        Objects.requireNonNull(observer);
        observers.add(observer);
    }  

    public void changeValue(int value) throws RemoteException {
        this.value = value;
        
        for(var obs: observers) {
            obs.notifyChange(this.value);
        }
    }
}
