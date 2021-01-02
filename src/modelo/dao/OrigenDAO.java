package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import modelo.conexion.Conexion;

public class OrigenDAO {
    private Connection conexion = Conexion.getConnection();
    private ResultSet rs;
    private String sql;
    
    public ResultSet consultar() {
        sql = "SELECT `ID`, `NOMBRE` FROM `corigen` ORDER BY `NOMBRE` ASC";
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
            String instruccion = "INSERT INTO `corigen` VALUES (NULL,'" + nombre + "');";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Origen registrado con éxito");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el origen");
        }
    }

    public void modificar(int id, String nombre) {
        try {
            sql = "UPDATE `corigen` SET `NOMBRE`='" + nombre + "' WHERE `ID`=" + id + ";";
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Origen actulizado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar el origen");
        }
    }

    public void eliminar(int id) {
        try {
            String instruccion = "DELETE FROM `corigen` WHERE `ID`='" + id + "'";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Origen eliminado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el origen");
        }
    }
}
