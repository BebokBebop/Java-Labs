/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab.saved.users;

import java.util.ArrayList;
import java.util.List;
import pl.polsl.lab.annotations.RequiresTesting;
import pl.polsl.lab.exceptions.MyException;
import pl.polsl.lab.saved.users.User;

/**
 * Stores registered users
 *
 * @author Hubert Przegendza
 * @version 1.0
 */
public class UsersDB {

    /**
     * List of all saved users
     */
    private List<User> users = new ArrayList<>();

    /**
     * Adds an user to the list
     *
     * @param name name of the user
     * @param password password of the user
     * @throws MyException if the user already exists
     */
    @RequiresTesting
    public void addUser(String name, String password) throws MyException, NullPointerException {
        if (name == null || password == null) {
            throw new NullPointerException();
        }
        for (User u : users) {
            if (u.getName().equals(name)) {
                throw new MyException();
            }
        }
        users.add(new User(name, password));
    }

    /**
     * Checks whether user of a given name and password exists
     *
     * @param name name of the user
     * @param password password to be checked
     * @return true if name and password check out, otherwise false
     */
    @RequiresTesting
    public boolean checkUser(String name, String password) {
        if (name == null || password == null) {
            return false;
        }
        for (User u : users) {
            if (u.getName().equals(name)) {
                if (u.checkPassword(password)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Removes an user from the list
     *
     * @param name name of the user to be deleted
     * @throws MyException if the user doesn't exist
     */
    @RequiresTesting
    public void removeUser(String name) throws MyException, NullPointerException {
        if (name == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(name)) {
                users.remove(i);
                return;
            }
        }
        throw new MyException();
    }
}
