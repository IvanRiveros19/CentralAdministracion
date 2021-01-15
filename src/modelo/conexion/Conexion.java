package modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String database = "central_autobuses";
    private String hostname = "localhost";
    private String port = "3306";
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    private String username = "root";
    private String password = "12345";

    private static Connection connection = null;

    private Conexion() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conextar con la Base De Datos " + ex);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            new Conexion();
        }
        return connection;
    }
}
