package fr.dauphine.ws.rmi.calculator.server;

import fr.dauphine.ws.rmi.calculator.common.ICalculator;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CalculatorServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ICalculator c = new Calculator();
            Naming.rebind("CalculatorService", c);
            //Naming.rebind("rmi://localhost:1099/CalculatorService", c);
        } catch (Exception e) {
            System.out.println("Trouble: "+e);
        }
    }
}
