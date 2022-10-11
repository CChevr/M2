package fr.uge.web.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ILibrary extends Remote {
    void addBook(long isbn, String title, String author) throws RemoteException;
    void deleteBook(long isbn) throws RemoteException;
    List<IBook> searchBookByTitle(String title) throws RemoteException;
    List<IBook> searchBookByAuthor(String author) throws RemoteException;
}