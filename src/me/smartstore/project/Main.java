package me.smartstore.project;

public class Main {
    public static void main(String[] args) {
        SmartStoreApplication store = SmartStoreApplication.getInstance();
        store.details();
        // for test
        // store.test().run();
        store.run();
    }
}
