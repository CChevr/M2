package fr.uge.web.server;

import fr.uge.web.common.ILibrary;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Library extends UnicastRemoteObject implements ILibrary {
    private final Map<Long, Book> books;

    protected Library() throws RemoteException {
        super();
        books = new HashMap<>();
    }

    /**
     * Add a book to the library
     * @param isbn book id
     * @param title book's title
     * @param author book's author
     * @throws RemoteException
     */
    @Override
    public void addBook(long isbn, String title, String author) throws RemoteException {
        Objects.requireNonNull(title);
        Objects.requireNonNull(author);

        books.putIfAbsent(isbn, new Book(isbn, title, author));
    }

    /**
     * Delete the book with the corresponding ISBN
     * @param isbn id of the book to delete
     * @throws RemoteException
     */
    @Override
    public void deleteBook(long isbn) throws RemoteException {
        books.remove(isbn);
    }

    /**
     * Searching for all books with the corresponding title
     * @param title book's title to search
     * @return list of book with corresponding title
     * @throws RemoteException
     */
    @Override
    public List<Book> searchBookByTitle(String title) throws RemoteException {
        Objects.requireNonNull(title);
        var result = new ArrayList<Book>();

        for(var book: books.values()) {
            if(book.getTitle().equals(title)) {
                result.add(book);
            }
        }

        return result;
    }

    /**
     * Searching for all books with the corresponding author
     * @param author book's author to search
     * @return list of book with corresponding author
     * @throws RemoteException
     */
    @Override
    public List<Book> searchBookByAuthor(String author) throws RemoteException {
        Objects.requireNonNull(author);
        var result = new ArrayList<Book>();

        for(var book: books.values()) {
            if(book.getTitle().equals(author)) {
                result.add(book);
            }
        }

        return result;
        /* don't work with references
        return books.values().stream()
                .filter(x -> x.getAuthor().equals(author))
                .toList();
         */
    }
}
