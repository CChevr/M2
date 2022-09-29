package fr.dauphine.ws.rmi.calculator.client;

import fr.dauphine.ws.rmi.calculator.common.ICalculator;

import java.rmi.Naming;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            ICalculator c = (ICalculator) Naming.lookup("CalculatorService");
            //ICalculator c = (ICalculator) Naming.lookup("rmi://localhost:1099/CalculatorService");
            System.out.println(c.sub(4, 3));
            System.out.println(c.add(4, 3));
            System.out.println(c.mul(4, 3));
            System.out.println(c.div(4, 3));
        } catch(Exception e) {
            System.out.println("Exception"+e);
        }
    }
}
