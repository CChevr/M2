package fr.uge.web.client;

import fr.uge.web.common.IBook;
import fr.uge.web.common.ILibrary;

import java.rmi.Naming;
import java.util.List;

public class LibraryClient {
    public static void main(String[] args) {
        try {
            ILibrary l = (ILibrary) Naming.lookup("LibraryService");
            l.addBook(1234, "Philosophy", "Benoit");
            l.addBook(13256, "Science", "Ahmed");
            l.addBook(1354, "Chemistry", "David");

            List<IBook> ll = l.searchBookByAuthor("Benoit");
            System.out.println("Benoit wrote" + ll.get(0).getTitle());

            ll.get(0).setAuthor("Baptiste");
            ll = l.searchBookByAuthor("Benoit");
            System.out.println("Benoit wrote " + ll.get(0).getTitle());

        } catch(Exception e) {
            System.out.println("Exception"+e);
        }
    }
}
