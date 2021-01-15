package modelo.dao;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import modelo.conexion.Conexion;
import modelo.dto.ReporteDTO;

public class ReporteDAO {

    private Connection conexion = Conexion.getConnection();
    private ResultSet rs;
    private String sql;

    public ResultSet llenarTabla(String fecha) {
        try {
            sql = "SELECT `treporte`.`ID`, `NUMERO`, date_format(`HORA_SALIDA`, '%h:%i') AS 'HORA_SALIDA', `corigen`.`NOMBRE` AS 'ORIGEN', "
                    + "`cdestino`.`NOMBRE` AS 'DESTINO', `cempresa`.`NOMBRE` AS 'EMPRESA', `TIPO_SERVICIO`, "
                    + "`TIPO_CORRIDA`, `NUMERO_ECONOMICO`, `NUMERO_PASAJEROS`, `NUMERO_SALIDA`, date_format(`FECHA`, '%d/%m/%Y') AS 'FECHA_SALIDA' "
                    + "FROM `treporte` "
                    + "INNER JOIN `corigen` ON `treporte`.`CORIGEN_ID`=`corigen`.`ID` "
                    + "INNER JOIN `cdestino` ON `treporte`.`CDESTINO_ID`=`cdestino`.`ID` "
                    + "INNER JOIN `cempresa` ON `treporte`.`CEMPRESA_ID`=`cempresa`.`ID` "
                    + "WHERE `FECHA`='" + fecha + "'"
                    + "ORDER BY `FECHA` ASC, `NUMERO` ASC;";
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
                    + reporte.getNumeroPasajeros() + "','" + reporte.getNumeroSalida() + "', '" + reporte.getFecha() + "', now());";
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Información agregada con éxito");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar su información");
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

    public void actualizar(ReporteDTO reporte) {
        try {
            sql = "UPDATE `treporte` SET `HORA_SALIDA`='" + reporte.getHoraSalida() + "', "
                    + "`CORIGEN_ID`=(SELECT `ID` FROM `corigen` WHERE `NOMBRE`='" + reporte.getOrigen() + "'),"
                    + "`CDESTINO_ID`=(SELECT `ID` FROM `cdestino` WHERE `NOMBRE`='" + reporte.getDestino() + "'),"
                    + "`CEMPRESA_ID`=(SELECT `ID` FROM `cempresa` WHERE `NOMBRE`='" + reporte.getEmpresa() + "'),"
                    + "`TIPO_SERVICIO`='" + reporte.getTipoServicio() + "', `TIPO_CORRIDA`='" + reporte.getTipoCorrida() + "', "
                    + "`NUMERO_ECONOMICO`=" + reporte.getNumeroEconomico() + ",`NUMERO_PASAJEROS`=" + reporte.getNumeroPasajeros() + ", "
                    + "`NUMERO_SALIDA`=" + reporte.getNumeroSalida() + " "
                    + "WHERE `ID`=" + reporte.getId() + ";";
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Información actualizada");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al actualizar el registro");
        }
    }

    public void eliminar(int id) {
        try {
            sql = "DELETE FROM `treporte` WHERE `ID`=" + id + ";";
            System.out.println(sql);
            PreparedStatement s = conexion.prepareStatement(sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registro eliminado");
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar el registro");
        }
    }

    public ResultSet reporteDia(String fecha) {
        try {
            sql = "SELECT `treporte`.`ID`, `NUMERO`, date_format(`HORA_SALIDA`, '%h:%i') AS 'HORA_SALIDA', "
                    + "`cdestino`.`NOMBRE` AS 'DESTINO', `cempresa`.`NOMBRE` AS 'EMPRESA', `TIPO_SERVICIO`, "
                    + "`TIPO_CORRIDA`, `NUMERO_ECONOMICO`, `NUMERO_PASAJEROS`, `NUMERO_SALIDA` "
                    + "FROM `treporte` "
                    + "INNER JOIN `cdestino` ON `treporte`.`CDESTINO_ID`=`cdestino`.`ID` "
                    + "INNER JOIN `cempresa` ON `treporte`.`CEMPRESA_ID`=`cempresa`.`ID` "
                    + "WHERE `FECHA`='" + fecha + "' "
                    + "ORDER BY `FECHA` ASC, `NUMERO` ASC;";
            System.out.println(sql);
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print(e);
        }
        return rs;
    }

    public ArrayList reporteMes(String fecha) {
        fecha = fecha.substring(0, 7);
        try {
            sql = "SELECT `treporte`.`ID`, `NUMERO`, date_format(`HORA_SALIDA`, '%h:%i') AS 'HORA_SALIDA', "
                    + "`cdestino`.`NOMBRE` AS 'DESTINO', `corigen`.`NOMBRE` AS 'ORIGEN', `cempresa`.`NOMBRE` AS 'EMPRESA', `TIPO_SERVICIO`, "
                    + "`TIPO_CORRIDA`, `NUMERO_ECONOMICO`, `NUMERO_PASAJEROS`, `NUMERO_SALIDA`, date_format(`FECHA`, '%d/%m/%Y') AS 'FECHA_SALIDA' "
                    + "FROM `treporte` "
                    + "INNER JOIN `corigen` ON `treporte`.`CORIGEN_ID`=`corigen`.`ID` "
                    + "INNER JOIN `cdestino` ON `treporte`.`CDESTINO_ID`=`cdestino`.`ID` "
                    + "INNER JOIN `cempresa` ON `treporte`.`CEMPRESA_ID`=`cempresa`.`ID` "
                    + "WHERE date_format(`FECHA`, '%Y-%m')='" + fecha + "' "
                    + "ORDER BY `FECHA` ASC, `NUMERO` ASC;";
            System.out.println(sql);
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print(e);
        }
        return toArrayList(rs);
    }
    
    public ArrayList<HashMap> toArrayList(ResultSet rs) {
        ArrayList<HashMap> results = new ArrayList();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
                HashMap row = new HashMap();
                results.add(row);
                for (int i = 1; i <= columns; i++) {
                    row.put(md.getColumnLabel(i), rs.getObject(i));
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al convertir en ArrayList");
        }
        return results;
    }
}
