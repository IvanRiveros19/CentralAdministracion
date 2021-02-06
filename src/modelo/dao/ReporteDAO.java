package modelo.dao;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JOptionPane;
import modelo.conexion.Conexion;
import modelo.dto.ReporteDTO;

public class ReporteDAO {

    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
                    + reporte.getNumeroPasajeros() + "','" + reporte.getNumeroSalida() + "', 1, '" + reporte.getFecha() + "', now());";
            PreparedStatement s = conexion.prepareStatement(sql);
            System.out.println("insertar: " + sql);
            s.executeUpdate();
            JOptionPane.showMessageDialog(null, "Información agregada con éxito");
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error al registrar: " + e);
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
    
    public void actualizar(ReporteDTO reporte){
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
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print(e);
        }
        return rs;
    }

    public ArrayList reporteMes(Date fecha) {
        String mes = dbDateFormat.format(fecha).substring(0, 7);
        try {
            sql = "SELECT `treporte`.`ID`, `NUMERO`, date_format(`HORA_SALIDA`, '%h:%i') AS 'HORA_SALIDA', "
                    + "`cdestino`.`NOMBRE` AS 'DESTINO', `corigen`.`NOMBRE` AS 'ORIGEN', `cempresa`.`NOMBRE` AS 'EMPRESA', `TIPO_SERVICIO`, "
                    + "`TIPO_CORRIDA`, `NUMERO_ECONOMICO`, `NUMERO_PASAJEROS`, `NUMERO_SALIDA`, date_format(`FECHA`, '%d/%m/%Y') AS 'FECHA_SALIDA' "
                    + "FROM `treporte` "
                    + "INNER JOIN `corigen` ON `treporte`.`CORIGEN_ID`=`corigen`.`ID` "
                    + "INNER JOIN `cdestino` ON `treporte`.`CDESTINO_ID`=`cdestino`.`ID` "
                    + "INNER JOIN `cempresa` ON `treporte`.`CEMPRESA_ID`=`cempresa`.`ID` "
                    + "WHERE date_format(`FECHA`, '%Y-%m')='" + mes + "' "
                    + "ORDER BY `EMPRESA` ASC, `FECHA` ASC, `NUMERO` ASC;";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print("Error al consultar registros por mes: " + e);
        }
        return toArrayList(rs);
    }
    
    //Funcion que obtiene los registros para generar la tabla principal del reporte
    public ArrayList informeGeneralMes(Date fecha) {
        String mes = dbDateFormat.format(fecha).substring(0, 7);
        try {
            sql = "SELECT `treporte`.`ID`, `NUMERO`, date_format(`HORA_SALIDA`, '%h:%i') AS 'HORA_SALIDA', "
                    + "`cdestino`.`NOMBRE` AS 'DESTINO', `corigen`.`NOMBRE` AS 'ORIGEN', `cempresa`.`NOMBRE` AS 'EMPRESA', `TIPO_SERVICIO`, "
                    + "`TIPO_CORRIDA`, `NUMERO_ECONOMICO`, `NUMERO_PASAJEROS`, `NUMERO_SALIDA`, date_format(`FECHA`, '%d/%m/%Y') AS 'FECHA_SALIDA' "
                    + "FROM `treporte` "
                    + "INNER JOIN `corigen` ON `treporte`.`CORIGEN_ID`=`corigen`.`ID` "
                    + "INNER JOIN `cdestino` ON `treporte`.`CDESTINO_ID`=`cdestino`.`ID` "
                    + "INNER JOIN `cempresa` ON `treporte`.`CEMPRESA_ID`=`cempresa`.`ID` "
                    + "WHERE date_format(`FECHA`, '%Y-%m')='" + mes + "' "
                    + "ORDER BY `FECHA` ASC, `NUMERO` ASC;";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();
        } catch (Exception e) {
            System.out.print("Error al consultar registros por mes: " + e);
        }
        return toArrayList(rs);
    }

    //Método para convertir un Resultset a ArrayList
    public ArrayList<HashMap> toArrayList(ResultSet rs) {
        ArrayList<HashMap> results = new ArrayList();
        if (rs == null) {
            return results;
        }
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
    
    public ArrayList getInfoResumen(Date fecha) {
        String date = dbDateFormat.format(fecha);
        try {
            sql = "SELECT NOMBRE AS 'empresa', count(r.CEMPRESA_ID) AS 'numCorridas',"
                    + "sum(ifnull(r.NUMERO_PASAJEROS, 0)) AS 'numPasajeros', "
                    + "sum(if(r.CANCELADA = true, 1, 0)) AS 'canceladas', "
                    + "sum(if(r.CANCELADA = false, 1, 0)) AS 'totalSalidas' "
                    + "FROM `cempresa` e "
                    + "LEFT JOIN `treporte` r on e.ID = r.CEMPRESA_ID "
                    + "AND r.FECHA='" + date + "' "
                    + "GROUP BY e.NOMBRE "
                    + "UNION SELECT 'TOTAL', count(*), sum(NUMERO_PASAJEROS), "
                    + "sum(if(CANCELADA = true, 1, 0)) AS 'canceladas', "
                    + "sum(if(CANCELADA = false, 1, 0)) AS 'totalSalidas' "
                    + "FROM treporte "
                    + "WHERE FECHA='" + date + "'";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(sql);
            rs = stmt.getResultSet();            
        } catch (SQLException e) {
            System.out.println(e);
        }
        return toArrayList(rs);
    }
}
