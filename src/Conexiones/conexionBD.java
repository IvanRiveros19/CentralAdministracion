package Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class conexionBD {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String database = "CentralAutobuses";
    private String hostname = "localhost";
    private String port = "3306";
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    private String username = "root";
    private String password = "toor";

    static public Connection conexion;
    
    public conexionBD() {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, username, password);
            System.out.println("conectdo");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No Se Puede Conectar A Su Base De Datos " + ex);
        }
    }
}
