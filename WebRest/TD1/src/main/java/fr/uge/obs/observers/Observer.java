package fr.uge.obs.observers;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import fr.uge.obs.common.IObserver;

public class Observer extends UnicastRemoteObject implements IObserver {

    Observer() throws RemoteException {
        super();
    }

    @Override
    public void notifyChange(int i) throws RemoteException {
        System.out.println("Ot1 : "+i);
    }
}
