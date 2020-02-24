/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.tests.saved.users;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.saved.users.UsersDB;

/**
 * Tests public methods of the UserDB class
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class UsersDBTest {

    private UsersDB usersDB = new UsersDB();

    public UsersDBTest() {
        try {
            usersDB.addUser("AAA", "123");
        } catch (MyException ex) {
            fail("Should not have thrown an exception.");
        } catch (NullPointerException ex) {
            fail("Should not have thrown an exception.");
        }
    }

    /**
     * Test of addUser method, of class UsersDB.
     */
    @Test
    public void testAddUser() throws Exception {
        try {
            usersDB.addUser("AAA", "123");
            usersDB.addUser("AAA", "321");
            fail("Should have thrown an exception.");
        } catch (MyException e) {
        } catch (NullPointerException e) {
            fail("Should not have thrown this exception.");
        }

        try {
            usersDB.addUser(null, "123");
            fail("Should have thrown an exception.");
        } catch (MyException e) {
            fail("Should not have thrown this exception.");
        } catch (NullPointerException e) {
        }

        try {
            usersDB.addUser(null, null);
            fail("Should have thrown an exception.");
        } catch (MyException e) {
            fail("Should not have thrown this exception.");
        } catch (NullPointerException e) {
        }

        try {
            usersDB.addUser("AAA", null);
            fail("Should have thrown an exception.");
        } catch (MyException e) {
            fail("Should not have thrown this exception.");
        } catch (NullPointerException e) {
        }

        try {
            usersDB.addUser("CCC", "321");
        } catch (MyException e) {
            fail("Should not have thrown an exception.");
        } catch (NullPointerException e) {
            fail("Should not have thrown an exception.");
        }
    }

    /**
     * Test of checkUser method, of class UsersDB.
     */
    @Test
    public void testCheckUser() {
        if (usersDB.checkUser("AAA", null)) {
            fail("Shouldn't have returned true");
        }
        if (usersDB.checkUser(null, null)) {
            fail("Shouldn't have returned true");
        }
        if (usersDB.checkUser(null, "123")) {
            fail("Shouldn't have returned true");
        }

        if (usersDB.checkUser("BBB", "123")) {
            fail("Shouldn't have returned true");
        }

        if (usersDB.checkUser("AAA", "321")) {
            fail("Shouldn't have returned true");
        }
        if (!usersDB.checkUser("AAA", "123")) {
            fail("Shouldn't have returned false");
        }
    }

    /**
     * Test of removeUser method, of class UsersDB.
     */
    @Test
    public void testRemoveUser() throws Exception {
        try {
            usersDB.removeUser(null);
            fail("Should have thrown an exception.");

        } catch (MyException e) {
            fail("Should not have thrown this exception.");
        } catch (NullPointerException e) {
        }

        try {
            usersDB.removeUser("ABC");
            fail("Should have thrown an exception.");

        } catch (MyException e) {
        } catch (NullPointerException e) {
            fail("Should not have thrown this exception.");
        }

        try {
            usersDB.removeUser("AAA");
        } catch (MyException e) {
            fail("Should not have thrown an exception.");
        } catch (NullPointerException e) {
            fail("Should not have thrown an exception.");
        }
    }

}
