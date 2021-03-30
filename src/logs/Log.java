package logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

    public static void createLog(String message) {
        String rutaArchivo = "logs/logger.txt";
        try {
            File directorio = new File("logs");
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    System.out.println("Directorio creado");
                }
            }

            File archivo = new File(rutaArchivo);
            BufferedWriter bw = null;
            if (!archivo.exists()) {
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.close();
            }
        } catch (Exception e) {
            System.out.println("Error al crear archivo de logs: " + e);
        }

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {

            fh = new FileHandler(rutaArchivo, true);
            logger.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info(message);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
