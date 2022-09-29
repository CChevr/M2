package fr.dauphine.ws.rmi.calculator.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICalculator extends Remote {
    public long add(long a, long b) throws RemoteException;
    public long sub(long a, long b) throws RemoteException;
    public long mul(long a, long b) throws RemoteException;
    public long div(long a, long b) throws RemoteException;
}
