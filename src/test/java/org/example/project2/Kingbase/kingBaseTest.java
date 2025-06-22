package org.example.project2.Kingbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class kingBaseTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.kingbase8.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:kingbase8://localhost:54321/info", "system", "ljzljz211302");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT version();");

            if (rs.next()) {
                System.out.println("Kingbase version: " + rs.getString(1));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
