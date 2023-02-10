package fr.uge.ia;

import com.bigdata.journal.Options;
import com.bigdata.rdf.sail.BigdataSail;
import com.bigdata.rdf.sail.BigdataSailRepository;
import org.openrdf.repository.Repository;

import java.util.Properties;

public class LoadData {
    public static void main(String[] args) {
        final Properties props = new Properties();
        props.put(Options.)
        props.put(Options.BUFFER_MODE, "DiskRW"); // persistent file system located journal
        props.put(Options.FILE, "/tmp/blazegraph/test.jnl"); // journal file location

        final BigdataSail sail = new BigdataSail(props); // instantiate a sail
        final Repository repo = new BigdataSailRepository(sail); // create a Sesame repository


    }
}
