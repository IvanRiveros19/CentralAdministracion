package modelo.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ToExcel {

    Date hoy = new Date();

    SimpleDateFormat shortFormatDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat longFormatDate = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    ReporteDAO reporteDAO = new ReporteDAO();
    int indexRow = 0, indexColumn = 1;
    Cell celda;
    Row fila;

    XSSFWorkbook workbook = new XSSFWorkbook();
    File file;
    Sheet excelSheet;
    FileOutputStream salida;

    private XSSFFont boldFont, noBoldFont;

    public ToExcel() {
        boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontName("Arial");
        boldFont.setFontHeight(10);

        noBoldFont = workbook.createFont();
        noBoldFont.setBold(false);
        noBoldFont.setFontName("Arial");
        noBoldFont.setFontHeight(10);
    }

    public void generateXLSfile(Date fecha) {
        try {
            ArrayList<HashMap> registros = reporteDAO.reporteMes(dbDateFormat.format(fecha));
            String nombreArchivo = (new SimpleDateFormat("MMMM 'DE' yyyy").format(fecha)).toUpperCase();
            if (registros.size() == 0) {
                JOptionPane.showMessageDialog(null, "No hay registros para" + nombreArchivo);
                return;
            }
            file = new File("files\\xMes\\" + nombreArchivo + ".xlsx");

            CellStyle registerCellStyle = workbook.createCellStyle();
            registerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            registerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            registerCellStyle.setFont(boldFont);
            registerCellStyle.setBorderTop(BorderStyle.MEDIUM);
            registerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
            registerCellStyle.setBorderRight(BorderStyle.MEDIUM);
            registerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
            registerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            String fechaRegistro = registros.get(0).get("FECHA_SALIDA").toString();
            String empresaRegistro = registros.get(0).get("EMPRESA").toString();
            excelSheet = workbook.createSheet(registros.get(0).get("EMPRESA").toString());
            nuevaTabla(true, fechaRegistro);
            for (int i = 0; i < registros.size(); i++) {
                if (!empresaRegistro.equals(registros.get(i).get("EMPRESA").toString())) {
                    excelSheet = workbook.createSheet(registros.get(i).get("EMPRESA").toString());
                    fechaRegistro = registros.get(i).get("FECHA_SALIDA").toString();
                    empresaRegistro = registros.get(i).get("EMPRESA").toString();
                    nuevaTabla(true, fechaRegistro);
                } else if (!fechaRegistro.equals(registros.get(i).get("FECHA_SALIDA").toString())) {
                    fechaRegistro = registros.get(i).get("FECHA_SALIDA").toString();
                    nuevaTabla(false, fechaRegistro);
                }
                fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
                celda = fila.createCell(indexColumn);
                celda.setCellValue(registros.get(i).get("NUMERO").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 1);
                celda.setCellValue(registros.get(i).get("HORA_SALIDA").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 2);
                celda.setCellValue(registros.get(i).get("ORIGEN").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 3);
                celda.setCellValue(registros.get(i).get("DESTINO").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 4);
                celda.setCellValue(registros.get(i).get("EMPRESA").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 5);
                celda.setCellValue(registros.get(i).get("TIPO_SERVICIO").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 6);
                celda.setCellValue(registros.get(i).get("TIPO_CORRIDA").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 7);
                celda.setCellValue(registros.get(i).get("NUMERO_ECONOMICO").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 8);
                celda.setCellValue(registros.get(i).get("NUMERO_PASAJEROS").toString());
                celda.setCellStyle(registerCellStyle);
                celda = fila.createCell(indexColumn + 9);
                celda.setCellValue(registros.get(i).get("NUMERO_SALIDA").toString());
                celda.setCellStyle(registerCellStyle);
                indexRow++;

            }

            // Creamos una fila en la hoja 
            // Ahora guardaremos el archivo
            try {
                salida = new FileOutputStream(file);
                workbook.write(salida);
                workbook.close();

                System.out.println("Archivo creado existosamente en " + file.getAbsolutePath());

            } catch (FileNotFoundException ex) {
                System.out.println("Archivo no localizable en sistema de archivos");
            }
        } catch (IOException ex) {
            System.out.println("Error al escribir en el disco: " + ex);
        }
    }

    public void nuevaTabla(boolean isPrimera, String fechaRegistros) {
        try {
            indexRow = 0;
            if (isPrimera) {
                indexColumn = 1;
            } else {
                indexColumn += 11;
            }

            CellStyle infoCellStyle = workbook.createCellStyle();
            infoCellStyle.setFont(boldFont);

            CellStyle infoCellStyleNormal = workbook.createCellStyle();
            infoCellStyleNormal.setFont(noBoldFont);

            excelSheet.setColumnWidth(indexColumn, 7 * 256);
            excelSheet.setColumnWidth(indexColumn + 1, 10 * 256);
            excelSheet.setColumnWidth(indexColumn + 2, 25 * 256);
            excelSheet.setColumnWidth(indexColumn + 3, 45 * 256);
            excelSheet.setColumnWidth(indexColumn + 4, 30 * 256);
            excelSheet.setColumnWidth(indexColumn + 5, 15 * 256);
            excelSheet.setColumnWidth(indexColumn + 6, 10 * 256);
            excelSheet.setColumnWidth(indexColumn + 7, 13 * 256);
            excelSheet.setColumnWidth(indexColumn + 8, 13 * 256);
            excelSheet.setColumnWidth(indexColumn + 9, 12 * 256);

            //FileInputStream obtains input bytes from the image file
            InputStream inputStream = new FileInputStream("logo.png");
            //Get the contents of an InputStream as a byte[].
            byte[] bytes = IOUtils.toByteArray(inputStream);
            //Adds a picture to the workbook
            int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
            //close the input stream
            inputStream.close();
            //Returns an object that handles instantiating concrete classes
            CreationHelper helper = workbook.getCreationHelper();
            //Creates the top-level drawing patriarch.
            Drawing drawing = excelSheet.createDrawingPatriarch();

            //Create an anchor that is attached to the worksheet
            ClientAnchor anchor = helper.createClientAnchor();

            //Creates a picture
            drawing.createPicture(anchor, pictureIdx);

            //create an anchor with upper left cell _and_ bottom right cell
            anchor.setCol1(indexColumn + 7); //Column B
            anchor.setRow1(indexRow + 1); //Row 3
            anchor.setCol2(indexColumn + 9); //Column C
            anchor.setRow2(indexRow + 8); //Row 4

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn + 4);
            celda.setCellValue("(1)-FORMATO DE SALIDAS DE AUTOBUS EN CENTRAL.");
            celda.setCellStyle(infoCellStyle);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(2)-INFORME A DETALLE.");
            celda.setCellStyle(infoCellStyleNormal);
            indexRow += 2;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(3)-POBLACION:");
            celda.setCellStyle(infoCellStyle);
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue("HIDALGO");
            celda.setCellStyle(infoCellStyleNormal);
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(4)-NOMBRE DE LA  CENTRAL O TERMINAL DE AUTOBUSES:");
            celda.setCellStyle(infoCellStyle);
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue("CENTRAL DE AUTOBUSES DE TULANCINGO S.A. DE C.V.");
            celda.setCellStyle(infoCellStyle);
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(5)-DIRECCION:");
            celda.setCellStyle(infoCellStyle);
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue("CARRETERA MEXICO-TUXPAN KM. 143");
            celda.setCellStyle(infoCellStyleNormal);
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue("COL. NUEVO TULANCINGO. C.P. 43612");
            celda.setCellStyle(infoCellStyleNormal);
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(6)-FECHA DE ELABORACIÓN:");
            celda.setCellStyle(infoCellStyle);
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue((new SimpleDateFormat("dd 'DE' MMMM 'DE' yyyy").format(hoy)).toUpperCase());
            celda.setCellStyle(infoCellStyle);
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(7)-REGISTRO DE SALIDAS CORRESPONDIENTE AL  DIA:");
            celda.setCellStyle(infoCellStyle);
            celda = fila.createCell(indexColumn + 4);
            celda.setCellValue(fechaRegistros);
            celda.setCellStyle(infoCellStyle);
            indexRow++;

            File imageFile = new File("logo.png");
            BufferedImage input = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(input, "PNG", baos);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setBorderTop(BorderStyle.MEDIUM);
            headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerCellStyle.setBorderRight(BorderStyle.MEDIUM);
            headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setWrapText(true);
            headerCellStyle.setFont(boldFont);

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            celda = fila.createCell(indexColumn);
            celda.setCellValue("(1-A)");
            celda = fila.createCell(indexColumn + 1);
            celda.setCellValue("(2-A)");
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue("(3-C)");
            celda = fila.createCell(indexColumn + 4);
            celda.setCellValue("(4-D)");
            celda = fila.createCell(indexColumn + 6);
            celda.setCellValue("(5-E)");
            celda = fila.createCell(indexColumn + 7);
            celda.setCellValue("(7-G)");
            celda = fila.createCell(indexColumn + 8);
            celda.setCellValue("(8-H)");
            celda = fila.createCell(indexColumn + 9);
            celda.setCellValue("(9-I)");
            indexRow++;

            fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
            fila.setHeight((short) (40 * 20));
            celda = fila.createCell(indexColumn);
            celda.setCellValue("No.");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 1);
            celda.setCellValue("HORA DE SALIDA.");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 2);
            celda.setCellValue("ORIGEN");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 3);
            celda.setCellValue("DESTINO");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 4);
            celda.setCellValue("EMPRESA");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 5);
            celda.setCellValue("TIPO DE SERVICIO");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 6);
            celda.setCellValue("TIPO DE CORRIDA (L/P/V)");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 7);
            celda.setCellValue("NUMERO ECONOMICO DE AUTOBUS");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 8);
            celda.setCellValue("N° DE PASAJEROS");
            celda.setCellStyle(headerCellStyle);
            celda = fila.createCell(indexColumn + 9);
            celda.setCellValue("N° DE SALIDA DE AUTOBUS");
            celda.setCellStyle(headerCellStyle);
            indexRow++;

        } catch (IOException ex) {
            Logger.getLogger(ToExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateCSVfile(String fecha) {

        try {
            Date fechaReporte = shortFormatDate.parse(fecha);
            String nombreArchivo = (new SimpleDateFormat("dd 'DE' MMMM 'DE' yyyy").format(fechaReporte)).toUpperCase();

            FileOutputStream file = new FileOutputStream("files\\xDia\\" + nombreArchivo + ".csv");
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
            while (res.next()) {
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
