package storlien.beertracker.application.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FileHandlerTest {

    @Test
    public void testReadFile() {
        assertEquals(true, Person.getObjPersons().isEmpty());
        Filehandler.readFile();
        assertEquals(false, Person.getObjPersons().isEmpty());
    }

    @Test
    public void testReadHashFile() {
        assertEquals(null, PurchaseReader.getLastPurchaseHash());
        Filehandler.readHashFile();
        assertEquals(false, PurchaseReader.getLastPurchaseHash().isEmpty());
    }

}