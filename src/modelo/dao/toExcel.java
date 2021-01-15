package modelo.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import modelo.dao.ReporteDAO;

public class toExcel {

    Date hoy = new Date();
    SimpleDateFormat shortFormatDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat longFormatDate = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    ReporteDAO reporteDAO = new ReporteDAO();
    int row = 1, column = 1;
    WritableFont letraNegrita = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
    
    public void generateXLSfile(Date fecha) {
        try {
            String nombreArchivo = (new SimpleDateFormat("MMMM 'DE' yyyy").format(fecha)).toUpperCase();
            File file = new File("files\\xMes\\" + nombreArchivo + ".xls");
            WritableCellFormat celdaGrisBorde = new WritableCellFormat(letraNegrita);
            celdaGrisBorde.setBackground(Colour.GRAY_25);
            celdaGrisBorde.setBorder(Border.ALL, BorderLineStyle.THICK);
            celdaGrisBorde.setAlignment(Alignment.CENTRE);

            

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("EB", 0);

            WritableSheet excelSheet = workbook.getSheet(0);
            
            ArrayList<HashMap> rs = reporteDAO.reporteMes(dbDateFormat.format(fecha));
            String fechaRegistro = rs.get(0).get("FECHA_SALIDA").toString();
            
            nuevaTabla(excelSheet, true, fechaRegistro);
            for (int i = 0; i < rs.size(); i++) {
                if (!fechaRegistro.equals(rs.get(i).get("FECHA_SALIDA").toString())) {
                    fechaRegistro = rs.get(i).get("FECHA_SALIDA").toString();
                    nuevaTabla(excelSheet, false, fechaRegistro);
                }
                excelSheet.addCell(new Label(column, row, rs.get(i).get("NUMERO").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 1, row, rs.get(i).get("HORA_SALIDA").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 2, row, rs.get(i).get("ORIGEN").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 3, row, rs.get(i).get("DESTINO").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 4, row, rs.get(i).get("EMPRESA").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 5, row, rs.get(i).get("TIPO_SERVICIO").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 6, row, rs.get(i).get("TIPO_CORRIDA").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 7, row, rs.get(i).get("NUMERO_ECONOMICO").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 9, row, rs.get(i).get("NUMERO_PASAJEROS").toString(), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 8, row, rs.get(i).get("NUMERO_SALIDA").toString(), celdaGrisBorde));
                row++;
            }

            workbook.write();
            workbook.close();

        } catch (WriteException ex) {
            System.out.println("Error al escribir en el archivo: " + ex);
        } catch (IOException ex) {
            System.out.println("Error al escribir en el disco: " + ex);
        }
    }

    public void nuevaTabla(WritableSheet excelSheet, boolean primera, String fechaRegistros){
        try {
            System.out.println("nueva tabla: " + fechaRegistros);
            row = 1;
            if (!primera) {
                column += 11;
            }
            WritableFont titulo1 = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD);
            WritableFont letraSencilla = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            
            WritableCellFormat celdaSencilla = new WritableCellFormat(letraSencilla);
            WritableCellFormat celdaSencillaNegrita = new WritableCellFormat(letraNegrita);
            
            WritableCellFormat celdaBorde = new WritableCellFormat(letraNegrita);
            celdaBorde.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
            celdaBorde.setAlignment(Alignment.CENTRE);
            celdaBorde.setVerticalAlignment(VerticalAlignment.CENTRE);
            celdaBorde.setWrap(true);
            
            excelSheet.mergeCells(column, row, column + 8, row);
            excelSheet.addCell(new Label(column, row, "(1)-FORMATO DE SALIDAS DE AUTOBUS EN CENTRAL.", new WritableCellFormat(titulo1)));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(2)-INFORME A DETALLE.", celdaSencillaNegrita));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(3)-POBLACION:", celdaSencillaNegrita));
            excelSheet.addCell(new Label(column + 4, row, "HIDALGO", celdaSencilla));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(4)-NOMBRE DE LA  CENTRAL O TERMINAL DE AUTOBUSES:", celdaSencillaNegrita));
            excelSheet.addCell(new Label(column + 4, row, "CENTRAL DE AUTOBUSES DE TULANCINGO S.A. DE C.V.", celdaSencilla));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(5)-DIRECCION:", celdaSencillaNegrita));
            excelSheet.addCell(new Label(column + 4, row, "CARRETERA MEXICO-TUXPAN KM. 143", celdaSencilla));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(6)-FECHA DE ELABORACIÓN:", celdaSencillaNegrita));
            excelSheet.addCell(new Label(column + 4, row, (new SimpleDateFormat("dd 'DE' MMMM 'DE' yyyy").format(hoy)).toUpperCase(), celdaSencilla));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(7)-REGISTRO DE SALIDAS CORRESPONDIENTE AL  DIA:", celdaSencillaNegrita));
            excelSheet.addCell(new Label(column + 4, row, fechaRegistros, celdaSencilla));

            File imageFile = new File("logo.png");
            BufferedImage input = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(input, "PNG", baos);
            excelSheet.addImage(new WritableImage(column + 7, 1, 2, 7, baos.toByteArray()));

            row += 2;
            excelSheet.setRowView(row, 800);

            excelSheet.setColumnView(column, 7);
            excelSheet.setColumnView(column + 1, 10);
            excelSheet.setColumnView(column + 2, 25);
            excelSheet.setColumnView(column + 3, 45);
            excelSheet.setColumnView(column + 4, 30);
            excelSheet.setColumnView(column + 5, 15);
            excelSheet.setColumnView(column + 6, 10);
            excelSheet.setColumnView(column + 7, 13);
            excelSheet.setColumnView(column + 8, 13);
            excelSheet.setColumnView(column + 9, 12);

            excelSheet.addCell(new Label(column, row, "No.", celdaBorde));
            excelSheet.addCell(new Label(column + 1, row, "HORA DE SALIDA.", celdaBorde));
            excelSheet.addCell(new Label(column + 2, row, "ORIGEN", celdaBorde));
            excelSheet.addCell(new Label(column + 3, row, "DESTINO", celdaBorde));
            excelSheet.addCell(new Label(column + 4, row, "EMPRESA", celdaBorde));
            excelSheet.addCell(new Label(column + 5, row, "TIPO DE SERVICIO", celdaBorde));
            excelSheet.addCell(new Label(column + 6, row, "TIPO DE CORRIDA (L/P/V)", celdaBorde));
            excelSheet.addCell(new Label(column + 7, row, "NUMERO ECONOMICO DE AUTOBUS", celdaBorde));
            excelSheet.addCell(new Label(column + 8, row, "N° DE PASAJEROS", celdaBorde));
            excelSheet.addCell(new Label(column + 9, row, "N° DE SALIDA DE AUTOBUS", celdaBorde));
            row++;
        } catch (WriteException ex) {
            Logger.getLogger(toExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(toExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generateCSVfile(String fecha) {
        
        try {
            Date fechaReporte = shortFormatDate.parse(fecha);
            String nombreArchivo = (new SimpleDateFormat("dd 'DE' MMMM 'DE' yyyy").format(fechaReporte)).toUpperCase();
            
            FileOutputStream file = new FileOutputStream("files\\xDia\\"+nombreArchivo + ".csv");
            OutputStreamWriter fileWriter = new OutputStreamWriter(file, "UTF-8");
            StringBuilder sb = new StringBuilder();

            sb.append("FORMATO DE SALIDAS DE AUTOBUS EN CENTRAL \n");
            sb.append("Hoja de Registro No. 1 \n");
            sb.append("POBLACION:,,,TULANCINGO\n");
            sb.append("NOMBRE DE LA  CENTRAL O TERMINAL DE AUTOBUSES:\n");
            sb.append(",,,CENTRAL DE AUTOBUSES DE TULANCINGO S.A. DE C.V.\n");
            sb.append("DIRECCION:,,,CARRETERA MEXICO-TUXPAN KM. 143\n");
            sb.append(",,,COL. NUEVO TULANCINGO. C.P. 43612\n");
            sb.append("FECHA DE ELABORACION:,,," + shortFormatDate.format(hoy) + "\n");
            sb.append("REGISTRO DE SALIDAS CORRESPONDIENTE AL DIA:,,," + longFormatDate.format(fechaReporte) + "\n\n");
            
            sb.append(",No.,HORA DE SALIDA,DESTINO,EMPRESA,CORRIDA EXTRA,NUMERO ECONOMICO DE AUTOBUS,No. DE PASAJEROS,No. DE SALIDA DE AUTOBUS\n\n");
            ResultSet res = reporteDAO.reporteDia(dbDateFormat.format(fechaReporte));
            while(res.next()) {
                sb.append(",");
                sb.append(res.getString("NUMERO"));
                sb.append(",");
                sb.append(res.getString("HORA_SALIDA"));
                sb.append(",");
                sb.append(res.getString("DESTINO"));
                sb.append(",");
                sb.append(res.getString("EMPRESA"));
                sb.append(",");
                sb.append(res.getString("TIPO_CORRIDA"));
                sb.append(",");
                sb.append(res.getString("NUMERO_ECONOMICO"));
                sb.append(",");
                sb.append(res.getString("NUMERO_PASAJEROS"));
                sb.append(",");
                sb.append(res.getString("NUMERO_SALIDA"));
                sb.append("\n");
            }
            fileWriter.write(sb.toString());
            fileWriter.close();
            generateXLSfile(fechaReporte);
            JOptionPane.showMessageDialog(null, "Se ha generado el informe del dia " + nombreArchivo);
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }
}
