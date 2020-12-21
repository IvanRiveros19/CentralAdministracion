package Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conexion {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String database = "CentralAutobuses";
    private String hostname = "localhost";
    private String port = "3306";
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    private String username = "root";
    private String password = "toor";

    private static Connection connection = null;

    public Conexion() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al conextar con la Base De Datos " + ex);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            new Conexion();
        }
        return this.connection;
    }
}
