package fr.dauphine.ws.rmi.calculator.server;

import fr.dauphine.ws.rmi.calculator.common.ICalculator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Calculator extends UnicastRemoteObject implements ICalculator {

    public Calculator() throws RemoteException {
        super();
    }

    @Override
    public long add(long a, long b) throws RemoteException {
        return a + b;
    }

    @Override
    public long sub(long a, long b) throws RemoteException {
        return a - b;
    }

    @Override
    public long mul(long a, long b) throws RemoteException {
        return a * b;
    }

    @Override
    public long div(long a, long b) throws RemoteException {
        if (b == 0)
                throw new RemoteException("Division by 0");
        return a / b;
    }
}
