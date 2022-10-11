package fr.uge.obs.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    public void notifyChange(int i) throws RemoteException;
}
