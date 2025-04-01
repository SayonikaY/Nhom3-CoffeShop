package org.example;

import model.DB;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {

        //Testing
        Session session = DB.openSession();
        // Do your shit
        DB.closeSession();

    }
}