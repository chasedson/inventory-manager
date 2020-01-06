/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.scan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import inventory.scan.dbutil;
import inventory.scan.gui;

/**
 *
 * @author cedson
 */
public class InventoryScan {
Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(inventory.scan.dbutil.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        dbutil.createNewDatabase();
        dbutil.createNewTable();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui().setVisible(true);
                
            }
        });
        InventoryScan app = new InventoryScan();
        while (true) {
            app.check();
        }

    }

    public void check() {
        dbutil app2 = new dbutil();
        System.out.println("Would you like to read all assets (read), write an asset (write), delete an asset (delete), edit an asset (edit), or quit (quit)?");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        if ("read".equals(choice.toLowerCase())) {
            app2.read();
        } else if ("write".equals(choice.toLowerCase())) {
            app2.insert();
        } else if ("quit".equals(choice.toLowerCase())) {
            System.exit(0);
        } else if ("delete".equals(choice.toLowerCase())) {
            app2.delete();
        } else if ("edit".equals(choice.toLowerCase())) {
            app2.edit();
        }     
        else {
            System.out.println("your choices are:");
            System.out.println("> READ");
            System.out.println("> WRITE");
            System.out.println("> EDIT");
            System.out.println("> DELETE");
            System.out.println("> QUIT");
            check();
        }
    }

    

    

}
