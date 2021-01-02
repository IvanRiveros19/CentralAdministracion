package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.conexion.Conexion;

public class EmpresaDAO {

    private Connection conexion = Conexion.getConnection();
    private ResultSet rs;
    private String sql;

    public ResultSet consultar() {
        sql = "SELECT `ID`, `NOMBRE` FROM `cempresa` ORDER BY `NOMBRE` ASC";
        try {
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return rs;
    }

    public void insertar(String nombre) {
        try {
            String instruccion = "INSERT INTO `cempresa` VALUES (NULL,'" + nombre + "');";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empresa registrada con éxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar la empresa");
        }
    }

    public void modificar(int id, String nombre) {
        try {
            sql = "UPDATE `cempresa` SET `NOMBRE`='" + nombre + "' WHERE `ID`=" + id + ";";
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empresa actulizada");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar la empresa");
        }
    }

    public void eliminar(int id) {
        try {
            String instruccion = "DELETE FROM `cempresa` WHERE `ID`='" + id + "'";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Empresa eliminada");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar la empresa");
        }
    }
}
