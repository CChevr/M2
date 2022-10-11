package fr.uge.obs.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserved extends Remote {
    public void subscribe(IObserver observer) throws RemoteException;
}
