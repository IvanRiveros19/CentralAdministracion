package Funcionalidad;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    File file = new File("output.xls");
    ReporteDAO reporteDAO = new ReporteDAO();

    public void formatearArchivoXLSX() {
        int row = 1, column = 1;

        WritableFont titulo1 = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD);
        WritableFont letraNegrita = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableFont letraSencilla = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
        try {
            WritableCellFormat celdaSencilla = new WritableCellFormat(letraSencilla);

            WritableCellFormat celdaSencillaNegrita = new WritableCellFormat(letraNegrita);

            WritableCellFormat celdaGrisBorde = new WritableCellFormat(letraNegrita);
            celdaGrisBorde.setBackground(Colour.GRAY_25);
            celdaGrisBorde.setBorder(Border.ALL, BorderLineStyle.THICK);
            celdaGrisBorde.setAlignment(Alignment.CENTRE);

            WritableCellFormat celdaBorde = new WritableCellFormat(letraNegrita);
            celdaBorde.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
            celdaBorde.setAlignment(Alignment.CENTRE);
            celdaBorde.setVerticalAlignment(VerticalAlignment.CENTRE);
            celdaBorde.setWrap(true);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("EB", 0);
            workbook.createSheet("ADO", 1);

            WritableSheet excelSheet = workbook.getSheet(0);

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
            excelSheet.addCell(new Label(column + 4, row, "02 DE ABRIL DE 2020", celdaSencilla));
            row++;
            excelSheet.mergeCells(column, row, column + 3, row);
            excelSheet.addCell(new Label(column, row, "(7)-REGISTRO DE SALIDAS CORRESPONDIENTE AL  DIA:", celdaSencillaNegrita));
            excelSheet.addCell(new Label(column + 4, row, "01/04/2020", celdaSencilla));

            File imageFile = new File("logo.png");
            BufferedImage input = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(input, "PNG", baos);
            excelSheet.addImage(new WritableImage(8, 1, 2, 7, baos.toByteArray()));

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

            ResultSet rs = reporteDAO.llenarTabla();
            while (rs.next()) {
                excelSheet.addCell(new Label(column, row, rs.getString("NUMERO"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 1, row, rs.getString("HORA_SALIDA"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 2, row, rs.getString("ORIGEN"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 3, row, rs.getString("DESTINO"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 4, row, rs.getString("EMPRESA"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 5, row, rs.getString("TIPO_SERVICIO"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 6, row, rs.getString("TIPO_CORRIDA"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 7, row, rs.getString("NUMERO_ECONOMICO"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 9, row, rs.getString("NUMERO_PASAJEROS"), celdaGrisBorde));
                excelSheet.addCell(new Label(column + 8, row, rs.getString("NUMERO_SALIDA"), celdaGrisBorde));
                row++;
            }
            rs.close();

            workbook.write();
            workbook.close();

        } catch (WriteException ex) {
            System.out.println("Error al escribir en el archivo: " + ex);
        } catch (IOException ex) {
            System.out.println("Error al escribir en el disco: " + ex);
        } catch (SQLException ex) {
            Logger.getLogger(toExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateCSVfile() {
        try {
            PrintWriter pw = new PrintWriter(new File("16 DE ABRIL.csv"));
            StringBuilder sb = new StringBuilder();

            sb.append("FORMATO DE SALIDAS DE AUTOBUS EN CENTRAL \n");
            sb.append("Hoja de Registro No. 1 \n");
            sb.append("POBLACION:,,,TULANCINGO\n");
            sb.append("NOMBRE DE LA  CENTRAL O TERMINAL DE AUTOBUSES:,,,CENTRAL DE AUTOBUSES DE TULANCINGO S.A. DE C.V.\n");
            sb.append("DIRECCION:,,,CARRETERA MEXICO-TUXPAN KM. 143\n");
            sb.append(",,,COL. NUEVO TULANCINGO. C.P. 43612\n");
            sb.append("FECHA DE ELABORACIÓN:,,,17/04/2020\n");
            sb.append("REGISTRO DE SALIDAS CORRESPONDIENTE AL DIA:,,,jueves, 16 de abril de 2020\n\n");
            
            sb.append(",No.,HORA DE SALIDA,DESTINO,EMPRESA,CORRIDA EXTRA,NUMERO ECONOMICO DE AUTOBUS,N° DE PASAJEROS,N° DE SALIDA DE AUTOBUS\n\n");
            ResultSet res = null;
            while(res.next()){
                
            }
            res.close();
            pw.write(sb.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("error: " + e);
        } catch (SQLException ex) {
            Logger.getLogger(toExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
