package fr.uge.web.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBook extends Remote {
    long getIsbn() throws RemoteException;

    String getTitle() throws RemoteException;

    String getAuthor() throws RemoteException;

    void setIsbn(long isbn) throws RemoteException;

    void setTitle(String title) throws RemoteException;

    void setAuthor(String author) throws RemoteException;
}
