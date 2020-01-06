/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.scan;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author cedson
 */
public class dbutil {

    public static String url = "jdbc:sqlite:db/inventory.db";

    Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert() {
        System.out.println("Please enter the asset id.");
        Scanner sc = new Scanner(System.in);
        String assetid = sc.nextLine();
        System.out.println("Please enter the description.");
        String description = sc.nextLine();
        System.out.println("Please enter the owner.");
        String owner = sc.nextLine();
        String sql = "INSERT INTO inventory(assetid,description,owner) VALUES(?,?,?)";

        try ( Connection conn = this.connect();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, assetid);
            pstmt.setString(2, description);
            pstmt.setString(3, owner);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().equals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (UNIQUE constraint failed: inventory.assetid)")) {
                System.out.println("Asset already exists.");
            } else {
                System.out.println(e.getMessage());

            }
        }
    }

    public void insertGui(String assetid, String description, String owner) {

        String sql = "INSERT INTO inventory(assetid,description,owner) VALUES(?,?,?)";

        try ( Connection conn = this.connect();  PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, assetid);
            pstmt.setString(2, description);
            pstmt.setString(3, owner);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().equals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (UNIQUE constraint failed: inventory.assetid)")) {
                System.out.println("Asset already exists. Asking user for decision.");
                String[] choices = {"Edit", "Delete", "Cancel"};
                String input = (String) JOptionPane.showInputDialog(null, "Asset ID already exists! What would you like to do?",
                        "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, // Use
                        // default
                        // icon
                        choices, // Array of choices
                        choices[1]); // Initial choice
                if (input == "Edit") {
                    editGui(assetid, description, owner);
                } else if (input == "Delete") {

                    String[] choicedel = {"no", "no", "no", "no", "no", "no", "yes", "no", "no", "no"};
                    String choice = (String) JOptionPane.showInputDialog(null, "Are you SURE you would like to delete this row?",
                            "The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, // Use
                            // default
                            // icon
                            choicedel, // Array of choices
                            choicedel[1]); // Initial choice
                    if (choice == "yes") {
                        deleteGui(assetid);
                        System.out.println("deleting.");
                    }

                }
                System.out.println("User chose: " + input);
            } else {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Critical Error:" + e.getMessage());

            }
        }
    }

    public void read() {
        String sql = "SELECT id, assetid, description, owner FROM inventory";
        try ( Connection conn = this.connect();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t"
                        + rs.getString("assetid") + "\t"
                        + rs.getString("description") + "\t"
                        + rs.getString("owner"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Critical Error: " + e.getMessage());
        }
    }

    public static void createNewTable() {
        // SQLite connection string

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS inventory (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    assetid varchar (100) NOT NULL UNIQUE,\n"
                + "    description text NOT NULL,\n"
                + "    owner text NOT NULL\n"
                + ");";

        try ( Connection conn = DriverManager.getConnection(url);  Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewDatabase() {
        try ( Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete() {
        System.out.println("Please enter the asset id.");
        Scanner sc = new Scanner(System.in);
        String assetid = sc.nextLine();
        String sql = "DELETE from inventory WHERE assetid = " + assetid + ";";
        try ( Connection conn = DriverManager.getConnection(url);  Statement stmt = conn.createStatement()) {
            // create a new table
            int rowsUpdated = stmt.executeUpdate(sql);
            if (rowsUpdated == 0) {
                System.out.println("Asset ID does not exist.");
            } else {
                System.out.println("Asset deleted.");
            }
        } catch (SQLException e) {
            System.out.println("Please enter an ID to be deleted.");
        }
    }

    public void deleteGui(String assetid) {

        String sql = "DELETE from inventory WHERE assetid = " + assetid + ";";
        System.out.println(sql);
        try ( Connection conn = DriverManager.getConnection(url);  Statement stmt = conn.createStatement()) {
            // create a new table
            int rowsUpdated = stmt.executeUpdate(sql);
            if (rowsUpdated == 0) {
                System.out.println("Asset ID does not exist.");
            } else {
                System.out.println("Asset deleted.");
                JOptionPane.showMessageDialog(null, "Asset deleted.");
            }
        } catch (SQLException e) {
            System.out.println("Please enter an ID to be deleted.");
        }
    }

    public void edit() {
        System.out.println("Please enter the asset id.");
        Scanner sc = new Scanner(System.in);
        String assetid = sc.nextLine();
        System.out.println("Please enter the description.");
        String description = sc.nextLine();
        System.out.println("Please enter the owner.");
        String owner = sc.nextLine();
        String sql = "UPDATE inventory SET owner = '" + owner + "',description = '" + description + "' WHERE assetid = '" + assetid + "';";
        try ( Connection conn = DriverManager.getConnection(url);  Statement stmt = conn.createStatement()) {
            int rowsUpdated = stmt.executeUpdate(sql);
            if (rowsUpdated == 0) {
                System.out.println("Asset ID does not exist.");
            } else {
                System.out.println("Asset deleted.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editGui(String assetid, String description, String owner) {
        String sql = "UPDATE inventory SET owner = '" + owner + "',description = '" + description + "' WHERE assetid = '" + assetid + "';";
        try ( Connection conn = DriverManager.getConnection(url);  Statement stmt = conn.createStatement()) {
            int rowsUpdated = stmt.executeUpdate(sql);
            if (rowsUpdated == 0) {
                System.out.println("Asset ID does not exist.");

            } else {
                System.out.println("Asset edited.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
