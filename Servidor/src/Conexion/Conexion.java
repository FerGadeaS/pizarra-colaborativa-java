
package Conexion;

import java.sql.*;

public class Conexion {

    public static Connection getConnection() {
        String conexionUrl = "jdbc:mysql://localhost:3306/pizarra?useSSL=false&serverTimezone=UTC";

        String usuario = System.getenv("PIZARRA_DB_USER");
        String password = System.getenv("PIZARRA_DB_PASSWORD");

        if (usuario == null || password == null) {
            System.out.println("Faltan las variables de entorno de MySQL.");
            return null;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(conexionUrl, usuario, password);
            return con;

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
