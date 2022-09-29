package fr.uge.web.server;

import fr.uge.web.common.IBook;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class Book extends UnicastRemoteObject implements IBook {
    private long isbn;
    private String title;
    private String author;

    public Book(long isbn, String title, String author) throws RemoteException {
        this.isbn = isbn;
        this.title = Objects.requireNonNull(title);
        this.author = Objects.requireNonNull(author);
    }

    public long getIsbn() throws RemoteException {
        return isbn;
    }

    public String getTitle() throws RemoteException {
        return title;
    }

    public String getAuthor() throws RemoteException {
        return author;
    }

    @Override
    public String toString() {
        return "ISBN:" + isbn + ", titre: " + title + " - auteur: " + author;
    }

    public void setIsbn(long isbn) throws RemoteException {
        this.isbn = isbn;
    }

    public void setTitle(String title) throws RemoteException {
        this.title = title;
    }

    public void setAuthor(String author) throws RemoteException {
        this.author = author;
    }
}
