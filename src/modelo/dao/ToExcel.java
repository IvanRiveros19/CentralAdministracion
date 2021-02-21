package modelo.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ToExcel {

    Date hoy = new Date();

    SimpleDateFormat shortFormatDate = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat longFormatDate = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
    SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    ReporteDAO reporteDAO = new ReporteDAO();

    private int indexRow = 0, indexColumn = 1;
    private Cell celda;
    private Row fila;

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private POIXMLProperties props = workbook.getProperties();

    int pictureIdx;
    private File file;
    private Sheet excelSheet;
    private FileOutputStream salida;

    private XSSFFont boldFont, noBoldFont;
    private CellStyle grayCellStyle;

    public ToExcel() {
        boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontName("Arial");
        boldFont.setFontHeight(10);

        noBoldFont = workbook.createFont();
        noBoldFont.setBold(false);
        noBoldFont.setFontName("Arial");
        noBoldFont.setFontHeight(10);

        grayCellStyle = workbook.createCellStyle();
        grayCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        grayCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        grayCellStyle.setFont(boldFont);
        grayCellStyle.setBorderTop(BorderStyle.MEDIUM);
        grayCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        grayCellStyle.setBorderRight(BorderStyle.MEDIUM);
        grayCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        grayCellStyle.setAlignment(HorizontalAlignment.CENTER);

        try {
            InputStream inputStream = new FileInputStream("logo.png");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
            inputStream.close();
        } catch (Exception ex) {
            System.out.println("Error al cargar imagen");
        }
    }

    //INICIA SECCIÓN DE MÉTODOS PARA CREAR EL REPORTE POR MES
    public void generarReporteMes(Date fecha) {
        try {
            ArrayList<HashMap> registros = reporteDAO.informeGeneral("mes", fecha);
            String nombreArchivo = (new SimpleDateFormat("MMMM 'DE' yyyy").format(fecha)).toUpperCase();
            if (registros.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay registros para " + nombreArchivo.toLowerCase());
                return;
            }

            file = new File("files\\xMes\\" + nombreArchivo + ".xlsx");
            POIXMLProperties.CoreProperties coreProp = props.getCoreProperties();
            coreProp.setCreator("Terminal Central del Norte del D.F. S.A. de C.V.");

            String fechaRegistro = registros.get(0).get("FECHA_SALIDA").toString();
            excelSheet = workbook.createSheet("EB");
            nuevaTabla(true, fechaRegistro);
            for (int i = 0; i < registros.size(); i++) {
                if (!fechaRegistro.equals(registros.get(i).get("FECHA_SALIDA").toString())) {
                    tablaInfo(fechaRegistro);
                    fechaRegistro = registros.get(i).get("FECHA_SALIDA").toString();
                    nuevaTabla(false, fechaRegistro);
                }
                fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
                celda = fila.createCell(indexColumn);
                celda.setCellValue(registros.get(i).get("NUMERO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 1);
                celda.setCellValue(registros.get(i).get("HORA_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 2);
                celda.setCellValue(registros.get(i).get("ORIGEN").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 3);
                celda.setCellValue(registros.get(i).get("DESTINO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 4);
                celda.setCellValue(registros.get(i).get("EMPRESA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 5);
                celda.setCellValue(registros.get(i).get("TIPO_SERVICIO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 6);
                celda.setCellValue(registros.get(i).get("TIPO_CORRIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 7);
                celda.setCellValue(registros.get(i).get("NUMERO_ECONOMICO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 8);
                celda.setCellValue(registros.get(i).get("NUMERO_PASAJEROS").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 9);
                celda.setCellValue(registros.get(i).get("NUMERO_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);

                indexRow++;
            }

            tablaInfo(fechaRegistro);

            registros = reporteDAO.obtenerRegistrosReporte("mes", fecha);

            fechaRegistro = registros.get(0).get("FECHA_SALIDA").toString();
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
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 1);
                celda.setCellValue(registros.get(i).get("HORA_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 2);
                celda.setCellValue(registros.get(i).get("ORIGEN").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 3);
                celda.setCellValue(registros.get(i).get("DESTINO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 4);
                celda.setCellValue(registros.get(i).get("EMPRESA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 5);
                celda.setCellValue(registros.get(i).get("TIPO_SERVICIO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 6);
                celda.setCellValue(registros.get(i).get("TIPO_CORRIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 7);
                celda.setCellValue(registros.get(i).get("NUMERO_ECONOMICO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 8);
                celda.setCellValue(registros.get(i).get("NUMERO_PASAJEROS").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 9);
                celda.setCellValue(registros.get(i).get("NUMERO_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                indexRow++;
            }
            save();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No hay registros para la fecha seleccionada");
        }
    }

    public void nuevaTabla(boolean isPrimera, String fechaRegistros) {
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

        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = excelSheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(indexColumn + 7);
        anchor.setRow1(indexRow + 1);
        anchor.setCol2(indexColumn + 9);
        anchor.setRow2(indexRow + 8);
        drawing.createPicture(anchor, pictureIdx);

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

    }

    public void tablaInfo(String fecha) {
        try {
            CellStyle infoCellStyle = workbook.createCellStyle();
            infoCellStyle.setBorderTop(BorderStyle.MEDIUM);
            infoCellStyle.setBorderLeft(BorderStyle.MEDIUM);
            infoCellStyle.setBorderRight(BorderStyle.MEDIUM);
            infoCellStyle.setBorderBottom(BorderStyle.MEDIUM);
            infoCellStyle.setAlignment(HorizontalAlignment.RIGHT);
            infoCellStyle.setFont(boldFont);
            ArrayList<HashMap> info = reporteDAO.getInfoResumen(shortFormatDate.parse(fecha));

            fila = excelSheet.getRow(indexRow + 4) != null ? excelSheet.getRow(indexRow + 4) : excelSheet.createRow(indexRow + 4);
            celda = fila.createCell(indexColumn + 7);
            celda.setCellValue("CANCELADAS");
            indexRow += 5;
            for (int i = 0; i < info.size(); i++) {
                fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
                celda = fila.createCell(indexColumn + 4);
                celda.setCellValue(info.get(i).get("empresa").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 5);
                celda.setCellValue(info.get(i).get("numCorridas").toString());
                celda.setCellStyle(infoCellStyle);
                celda = fila.createCell(indexColumn + 6);
                celda.setCellValue(info.get(i).get("numPasajeros").toString());
                celda.setCellStyle(infoCellStyle);
                celda = fila.createCell(indexColumn + 7);
                celda.setCellValue(info.get(i).get("canceladas").toString());
                celda.setCellStyle(infoCellStyle);
                celda = fila.createCell(indexColumn + 8);
                celda.setCellValue(info.get(i).get("totalSalidas").toString());
                celda.setCellStyle(infoCellStyle);
                indexRow++;
            }
        } catch (ParseException ex) {
            System.out.println("Error en el formato de hora");
        }
    }
    //FINALIZA SECCIÓN DE MÉTODOS PARA CREAR EL REPORTE POR MES

    public void generarReporteDia(Date fecha) {
        try {
            ArrayList<HashMap> registros = reporteDAO.informeGeneral("dia", fecha);
            String nombreArchivo = (new SimpleDateFormat("dd 'DE' MMMM 'DE' yyyy").format(fecha)).toUpperCase();
            if (registros.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay registros para " + nombreArchivo.toLowerCase());
                return;
            }

            file = new File("files\\xDia\\" + nombreArchivo + ".xlsx");
            POIXMLProperties.CoreProperties coreProp = props.getCoreProperties();
            coreProp.setCreator("Gerencia");

            excelSheet = workbook.createSheet("EB");
            nuevaTablaDia(fecha);
            for (int i = 0; i < registros.size(); i++) {
                fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
                celda = fila.createCell(indexColumn + 1);
                celda.setCellValue(registros.get(i).get("NUMERO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 2);
                celda.setCellValue(registros.get(i).get("HORA_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 3);
                celda.setCellValue(registros.get(i).get("DESTINO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 4);
                celda.setCellValue(registros.get(i).get("EMPRESA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 5);
                celda.setCellValue(registros.get(i).get("TIPO_CORRIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 6);
                celda.setCellValue(registros.get(i).get("NUMERO_ECONOMICO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 7);
                celda.setCellValue(registros.get(i).get("NUMERO_PASAJEROS").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 8);
                celda.setCellValue(registros.get(i).get("NUMERO_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                indexRow++;
            }

            registros = reporteDAO.obtenerRegistrosReporte("dia", fecha);

            String empresaRegistro = registros.get(0).get("EMPRESA").toString();
            excelSheet = workbook.createSheet(registros.get(0).get("EMPRESA").toString());
            nuevaTablaDia(fecha);
            for (int i = 0; i < registros.size(); i++) {
                if (!empresaRegistro.equals(registros.get(i).get("EMPRESA").toString())) {
                    excelSheet = workbook.createSheet(registros.get(i).get("EMPRESA").toString());
                    empresaRegistro = registros.get(i).get("EMPRESA").toString();
                    nuevaTablaDia(fecha);
                }
                fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
                celda = fila.createCell(indexColumn + 1);
                celda.setCellValue(registros.get(i).get("NUMERO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 2);
                celda.setCellValue(registros.get(i).get("HORA_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 3);
                celda.setCellValue(registros.get(i).get("DESTINO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 4);
                celda.setCellValue(registros.get(i).get("EMPRESA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 5);
                celda.setCellValue(registros.get(i).get("TIPO_CORRIDA").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 6);
                celda.setCellValue(registros.get(i).get("NUMERO_ECONOMICO").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 7);
                celda.setCellValue(registros.get(i).get("NUMERO_PASAJEROS").toString());
                celda.setCellStyle(grayCellStyle);
                celda = fila.createCell(indexColumn + 8);
                celda.setCellValue(registros.get(i).get("NUMERO_SALIDA").toString());
                celda.setCellStyle(grayCellStyle);
                indexRow++;
            }
            save();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No hay registros para la fecha seleccionada: " + ex);
        }
    }

    public void nuevaTablaDia(Date fechaRegistro) {
        indexRow = 0;
        indexColumn = 0;

        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontName("Arial");
        titleFont.setFontHeight(16);
        CellStyle principalCellStyle = workbook.createCellStyle();
        principalCellStyle.setAlignment(HorizontalAlignment.CENTER);
        principalCellStyle.setFont(titleFont);

        CellStyle infoCellStyle = workbook.createCellStyle();
        infoCellStyle.setFont(boldFont);

        CellStyle infoCellStyleNormal = workbook.createCellStyle();
        infoCellStyleNormal.setFont(noBoldFont);

        CellStyle yellowCellStyle = workbook.createCellStyle();
        yellowCellStyle.setFillForegroundColor(IndexedColors.YELLOW1.getIndex());
        yellowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        yellowCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        infoCellStyleNormal.setFont(noBoldFont);

        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = excelSheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(indexColumn + 5);
        anchor.setRow1(indexRow + 1);
        anchor.setCol2(indexColumn + 8);
        anchor.setRow2(indexRow + 9);
        drawing.createPicture(anchor, pictureIdx);

        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn + 1);
        celda.setCellValue("FORMATO DE SALIDAS DE AUTOBUS EN CENTRAL.");
        excelSheet.addMergedRegion(new CellRangeAddress(indexRow, indexRow, indexColumn + 1, indexColumn + 6));
        celda.setCellStyle(principalCellStyle);
        indexRow++;
        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn);
        celda.setCellValue("Hoja de Registro No. 1");
        celda.setCellStyle(infoCellStyle);
        indexRow++;
        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn);
        celda.setCellValue("POBLACION:");
        celda.setCellStyle(infoCellStyle);
        celda = fila.createCell(indexColumn + 3);
        celda.setCellValue("TULANCINGO");
        celda.setCellStyle(infoCellStyle);
        indexRow++;
        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn);
        celda.setCellValue("NOMBRE DE LA  CENTRAL O TERMINAL DE AUTOBUSES:");
        celda.setCellStyle(infoCellStyle);
        indexRow++;
        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn + 3);
        celda.setCellValue("CENTRAL DE AUTOBUSES DE TULANCINGO S.A. DE C.V.");
        celda.setCellStyle(infoCellStyle);
        indexRow++;
        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn);
        celda.setCellValue("DIRECCION:");
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
        celda.setCellValue("FECHA DE ELABORACIÓN:");
        celda.setCellStyle(infoCellStyle);
        celda = fila.createCell(indexColumn + 3);
        celda.setCellValue(shortFormatDate.format(hoy));
        celda.setCellStyle(infoCellStyleNormal);
        indexRow++;
        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        celda = fila.createCell(indexColumn);
        celda.setCellValue("REGISTRO DE SALIDAS CORRESPONDIENTE AL  DIA:");
        celda.setCellStyle(infoCellStyle);
        celda = fila.createCell(indexColumn + 3);
        celda.setCellValue((new SimpleDateFormat("dd 'DE' MMMM 'DE' yyyy").format(fechaRegistro)).toLowerCase());
        celda.setCellStyle(yellowCellStyle);
        indexRow += 2;

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setBorderTop(BorderStyle.MEDIUM);
        headerCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerCellStyle.setBorderRight(BorderStyle.MEDIUM);
        headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setFont(boldFont);

        excelSheet.setColumnWidth(indexColumn, 20 * 256);
        excelSheet.setColumnWidth(indexColumn + 1, 15 * 256);
        excelSheet.setColumnWidth(indexColumn + 2, 15 * 256);
        excelSheet.setColumnWidth(indexColumn + 3, 35 * 256);
        excelSheet.setColumnWidth(indexColumn + 4, 29 * 256);
        excelSheet.setColumnWidth(indexColumn + 5, 10 * 256);
        excelSheet.setColumnWidth(indexColumn + 6, 11 * 256);
        excelSheet.setColumnWidth(indexColumn + 7, 10 * 256);
        excelSheet.setColumnWidth(indexColumn + 8, 11 * 256);

        fila = excelSheet.getRow(indexRow) != null ? excelSheet.getRow(indexRow) : excelSheet.createRow(indexRow);
        fila.setHeight((short) (100 * 20));
        celda = fila.createCell(indexColumn + 1);
        celda.setCellValue("No.");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 2);
        celda.setCellValue("HORA DE SALIDA.");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 3);
        celda.setCellValue("DESTINO");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 4);
        celda.setCellValue("EMPRESA");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 5);
        celda.setCellValue("CORRIDA EXTRA");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 6);
        celda.setCellValue("NUMERO ECONOMICO DE AUTOBUS");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 7);
        celda.setCellValue("N° DE PASAJEROS");
        celda.setCellStyle(headerCellStyle);
        celda = fila.createCell(indexColumn + 8);
        celda.setCellValue("N° DE SALIDA DE AUTOBUS");
        celda.setCellStyle(headerCellStyle);
        indexRow++;
    }

    public void save() {
        try {
            salida = new FileOutputStream(file);
            workbook.write(salida);
            workbook.close();
            System.out.println("Archivo creado existosamente en " + file.getAbsolutePath());
        } catch (Exception ex) {
            System.out.println("Ocurrió un error al guardar el archivo: " + ex);
        }
    }

}
