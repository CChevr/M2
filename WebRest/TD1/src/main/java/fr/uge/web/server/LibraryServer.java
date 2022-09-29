package fr.uge.web.server;

import fr.uge.web.common.ILibrary;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class LibraryServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ILibrary l = new Library();
            Naming.rebind("LibraryService", l);
        } catch (Exception e) {
            System.out.println("Trouble: "+e);
        }
    }
}
