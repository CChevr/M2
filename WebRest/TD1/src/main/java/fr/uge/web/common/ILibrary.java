package fr.uge.web.common;

import fr.uge.web.server.Book;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ILibrary extends Remote {
    void addBook(long isbn, String title, String author) throws RemoteException;
    void deleteBook(long isbn) throws RemoteException;
    List<Book> searchBookByTitle(String title) throws RemoteException;
    List<Book> searchBookByAuthor(String author) throws RemoteException;
}