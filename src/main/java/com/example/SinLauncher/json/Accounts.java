package com.example.SinLauncher.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.example.SinLauncher.App;
import com.example.SinLauncher.SinLauncherEntites.User;

/**
 * Accounts
 * this is basically a json file containing all accounts it's path is in `path`
 * all setters must be static that peforms sets on readAccounts and writes them
 * using writeAccounts
 */
public class Accounts {
    public static class NoSuchAccountException extends Exception {
        public String username;

        public NoSuchAccountException(String username, String msg) {
            super(msg);

            this.username = username;
        }
    }

    public static final Path PATH = Paths.get(App.DIR, "accounts.json");

    private String defaultUser;
    private List<User> users;

    public static Accounts readAccounts() throws IOException {
        if (!PATH.toFile().exists())
            writeAccounts(new Accounts());

        var contents = Files.readString(PATH);

        return App.GSON.fromJson(contents, Accounts.class);
    }

    private static void writeAccounts(Accounts accounts) throws IOException {
        var json = App.GSON.toJson(accounts).getBytes();
        Files.write(PATH, json);
    }

    public Accounts() {
        User defaultAccount = new User("0", "SebSucks", "", "", false, true);

        this.users = new ArrayList<User>();
        this.users.add(defaultAccount);
        this.defaultUser = defaultAccount.getUsername();
    }

    /**
     * adds {@code user} to accounts.json to read the accounts after adding do
     * {@code Accounts.readAccounts}
     */
    public static void addUser(User user) throws IOException {
        Accounts accounts = readAccounts();
        accounts.users.add(user);

        writeAccounts(accounts);
    }

    /**
     * removes {@link User} with username {@code username} from accounts.json
     */
    public static void removeUser(String username) throws IOException {
        Accounts accounts = readAccounts();
        accounts.users.removeIf((x) -> x.getUsername() == username);
    }

    /**
     * gets a {@link User} from {@code this.users} with username {@code username}
     * returns null if no such a user
     */
    public User getUser(String username) throws NoSuchAccountException {
        List<User> users = this.users;

        for (User user : users)
            if (user.getUsername().equals(username))
                return user;

        throw new NoSuchAccountException(username, "couldn't get user because it doesn't exist");
    }

    /**
     * fetches an array of usernames in {@code this.users}}
     */
    public String[] getUsernames() {
        List<String> usernames = new ArrayList<String>();

        for (User user : this.users)
            usernames.add(user.getUsername());

        return usernames.toArray(new String[0]);
    }

    public User getDefaultUser() {
        try {
            return this.getUser(this.defaultUser);
        } catch (NoSuchAccountException _e) {
            App.LOGGER.log(Level.SEVERE, "FAILED GETTING DEFAULT USER PANIC!");
            return null;
        }
    }

    /**
     * sets a {@code this.defaultUser} to {@code username}
     * 
     * @throws
     */
    public static void setDefaultUser(String username) throws NoSuchAccountException, IOException {
        Accounts accounts = readAccounts();
        User user = accounts.getUser(username);
        if (user == null)
            throw new NoSuchAccountException(username, "no such logged in account with username '" + username + "'");

        accounts.defaultUser = username;
    }
}
