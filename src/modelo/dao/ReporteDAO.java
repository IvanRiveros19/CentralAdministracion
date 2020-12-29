/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modelo.conexion.Conexion;
import modelo.dto.ReporteDTO;

public class ReporteDAO {

    private Connection conexion = Conexion.getConnection();
    private ResultSet rs;
    private String sql;

    public ResultSet llenarTabla() {
        try {
            sql = "SELECT `treporte`.`ID`, `NUMERO`, `HORA_SALIDA`, `corigen`.`NOMBRE` AS 'ORIGEN', "
                    + "`cdestino`.`NOMBRE` AS 'DESTINO', `cempresa`.`NOMBRE` AS 'EMPRESA', `TIPO_SERVICIO`, "
                    + "`TIPO_CORRIDA`, `NUMERO_ECONOMICO`, `NUMERO_PASAJEROS`, `NUMERO_SALIDA`, date_format(`FECHA`, '%d/%c/%Y') AS 'FECHA' "
                    + "FROM `treporte` "
                    + "INNER JOIN `corigen` ON `treporte`.`CORIGEN_ID`=`corigen`.`ID` "
                    + "INNER JOIN `cdestino` ON `treporte`.`CDESTINO_ID`=`cdestino`.`ID` "
                    + "INNER JOIN `cempresa` ON `treporte`.`CEMPRESA_ID`=`cempresa`.`ID` "
                    + "ORDER BY `FECHA` ASC;";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (SQLException ex) {
            System.out.println("Error en la consulta: " + ex);
        }
        return rs;
    }

    public ResultSet llenarComboOrigenes() {
        try {
            sql = "SELECT `NOMBRE` FROM `corigen` ORDER BY `NOMBRE` ASC";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print(e);
        }
        return rs;
    }
    
    public ResultSet llenarComboDestinos() {
        try {
            sql = "SELECT `NOMBRE` FROM `cdestino` ORDER BY `NOMBRE` ASC";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print(e);
        }
        return rs;
    }
    
    public ResultSet llenarComboEmpresas() {
        try {
            sql = "SELECT `NOMBRE` FROM `cempresa` ORDER BY `NOMBRE` ASC";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print(e);
        }
        return rs;
    }
    
    public void insertar(ReporteDTO reporte) {
        try {
            sql = "INSERT INTO `treporte` VALUES (NULL,'" + getNumero(reporte.getFecha()) + "','" + reporte.getHoraSalida()
                    + "',(SELECT `ID` FROM `corigen` WHERE `NOMBRE`='" + reporte.getOrigen() + "'),"
                    + "(SELECT `ID` FROM `cdestino` WHERE `NOMBRE`='" + reporte.getDestino() + "'),"
                    + "(SELECT `ID` FROM `cempresa` WHERE `NOMBRE`='" + reporte.getEmpresa() + "'),"
                    + "'" + reporte.getTipoServicio() + "','" + reporte.getTipoCorrida() + "','" + reporte.getNumeroEconomico() + "','"
                    + reporte.getNumeroPasajeros() + "','" + reporte.getNumeroSalida() + "', '" + reporte.getFecha() + "');";
            System.out.println(sql);
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Información agregada con éxito");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se logró agregar la información");
            System.out.println(e);
        }
    }
    
    public int getNumero(String date) {
        int maximo = 0;
        try {
            sql = "SELECT max(`NUMERO`) FROM `treporte` WHERE `FECHA`='" + date + "';";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
            while (rs.next()) {
                maximo = rs.getInt(1);
            }
            rs.close();
            maximo++;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return maximo;
    }
    
    public void actualizar(ReporteDTO reporte){
        try {
            sql = "UPDATE `treporte` SET `HORA_SALIDA`='" + reporte.getHoraSalida() + "', "
                + "`CORIGEN_ID`=(SELECT `ID` FROM `corigen` WHERE `NOMBRE`='" + reporte.getOrigen() +"'),"
                + "`CDESTINO_ID`=(SELECT `ID` FROM `cdestino` WHERE `NOMBRE`='" + reporte.getDestino()+"'),"
                + "`CEMPRESA_ID`=(SELECT `ID` FROM `cempresa` WHERE `NOMBRE`='" + reporte.getEmpresa()+"'),"
                + "`TIPO_SERVICIO`='" + reporte.getTipoServicio() + "', `TIPO_CORRIDA`='" + reporte.getTipoCorrida()+ "', "
                + "`NUMERO_ECONOMICO`=" + reporte.getNumeroEconomico()+ ",`NUMERO_PASAJEROS`=" + reporte.getNumeroPasajeros()+ ", "
                + "`NUMERO_SALIDA`=" + reporte.getNumeroSalida()+ " "
                + "WHERE `ID`=" + reporte.getId() + ";";
            System.out.println(sql);
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Información agregada con éxito");
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
    }
    
    public void eliminar(int id){
        try {
            sql = "DELETE FROM `treporte` WHERE `ID`=" + id + ";";
            System.out.println(sql);
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Información agregada con éxito");
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
    }
    
}
