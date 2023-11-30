import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Procesa un archivo CSV y genera archivos de texto a partir de una plantilla, reemplazando marcadores con datos del CSV que tenemos en la carpeta salida.
 */
public class ProcesarPlantilla {
    /**
     * Método principal que inicia el proceso de procesamiento del archivo CSV y la generación de archivos de texto.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        // Define las rutas de los archivos y la carpeta de salida
        String rutaArchivoCSV = "C:/Users/ElUltimoPasajero/IdeaProjects/ProcesadoArchivos/src/DatosEntrada/data.csv";
        String rutaPlantilla = "C:/Users/ElUltimoPasajero/IdeaProjects/ProcesadoArchivos/src/plantilla/template.txt";
        String carpetaSalida = "C:/Users/ElUltimoPasajero/IdeaProjects/ProcesadoArchivos/src/Salida/";

        try {
            // Vacia la carpeta de salida antes de comenzar
            vaciarCarpeta(carpetaSalida);

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCSV))) {
                String linea;
                int lineCount = 0;

                // Lee el archivo CSV línea por línea
                while ((linea = br.readLine()) != null) {
                    lineCount++; // Incrementar el contador de líneas
                    // Divide la línea en campos utilizando la coma como delimitador
                    String[] campos = linea.split(",");

                    if (!linea.trim().isEmpty()) { // Verificar si la línea no está vacía
                        if (campos.length == 5) {
                            String id = campos[0];
                            String nombreEmpresa = campos[1];
                            String ciudad = campos[2];
                            String email = campos[3];
                            String nombreUsuario = campos[4];

                            // Lee la plantilla de texto
                            String plantilla = leerPlantilla(rutaPlantilla);

                            // Reemplaza marcadores en la plantilla con valores específicos
                            String textoReemplazado = reemplazarMarcadores(plantilla, nombreEmpresa, ciudad, email, nombreUsuario);

                            // Crea un archivo de salida con el contenido reemplazado
                            String nombreArchivoSalida = carpetaSalida + "template-" + id + ".txt";

                            escribirArchivo(textoReemplazado, nombreArchivoSalida);
                        } else {
                            // Maneja la falta de datos en la línea CSV

                            System.err.println("Error: Datos faltantes en la línea CSV . Se omite esta línea: " + lineCount);
                        }
                    }
                }

                System.out.println("Proceso completado. Archivos generados en la carpeta de salida.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//piiiiiiiiiiiiiiiiii
    /**
     * Este método se utiliza para leer una plantilla de texto desde un archivo especificado.
     *
     * @param rutaPlantilla La ruta del archivo de plantilla que se va a leer.
     * @return El contenido de la plantilla como una cadena de caracteres.
     * @throws IOException Si ocurre un error al leer el archivo de plantilla.
     */
    private static String leerPlantilla(String rutaPlantilla) throws IOException {
        StringBuilder plantilla = new StringBuilder(); // Inicializa un StringBuilder para almacenar el contenido de la plantilla

        try (BufferedReader br = new BufferedReader(new FileReader(rutaPlantilla))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                plantilla.append(linea).append("\n"); // Lee cada línea de la plantilla y agregarla al StringBuilder
            }
        }

        return plantilla.toString(); // Devuelve el contenido de la plantilla como una cadena
    }

    /**
     * Reemplaza marcadores en una plantilla con valores específicos.
     *
     * @param plantilla     La plantilla de texto que contiene los marcadores.
     * @param nombreEmpresa El nombre de la empresa.
     * @param ciudad        El nombre de la ciudad.
     * @param email         La dirección de correo electrónico.
     * @param nombreUsuario El nombre de usuario.
     * @return La plantilla con los marcadores reemplazados por los valores proporcionados.
     */
    private static String reemplazarMarcadores(String plantilla, String nombreEmpresa, String ciudad, String email, String nombreUsuario) {
        String textoReemplazado = plantilla;
        if (textoReemplazado.contains("%%1%%")) {
            textoReemplazado = textoReemplazado.replace("%%1%%", nombreUsuario);
        } else {
            // Maneja el caso en que "%%1%%" no se encuentra en la plantilla
            System.err.println("Advertencia: El marcador %%1%% no ha sido encontrado.");
        }
        if (textoReemplazado.contains("%%2%%")) {
            textoReemplazado = textoReemplazado.replace("%%2%%", nombreEmpresa);
        } else {
            // Maneja el caso en que "%%2%%" no se encuentra en la plantilla
            System.err.println("Advertencia: El marcador %%2%% no ha sido encontrado.");
        }
        if (textoReemplazado.contains("%%3%%")) {
            textoReemplazado = textoReemplazado.replace("%%3%%", email);
        } else {
            // Maneja el caso en que "%%3%%" no se encuentra en la plantilla
            System.err.println("Advertencia: El marcador %%3%% no ha sido encontrado.");
        }
        if (textoReemplazado.contains("%%4%%")) {
            textoReemplazado = textoReemplazado.replace("%%4%%", ciudad);
        } else {
            // Maneja el caso en que "%%4%%" no se encuentra en la plantilla
            System.err.println("Advertencia: El marcador %%4%% no ha sido encontrado.");
        }
        return textoReemplazado;
    }

    /**
     * Este método se utiliza para escribir un contenido en un archivo especificado.
     *
     * @param contenido   El contenido que se escribirá en el archivo.
     * @param rutaArchivo La ruta del archivo en el que se escribirá el contenido.
     */
    private static void escribirArchivo(String contenido, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Crea un objeto FileWriter para escribir en el archivo especificado
            // FileWriter se cierra automáticamente cuando se sale del bloque try-with-resources
            writer.write(contenido); // Escribe el contenido en el archivo
        } catch (IOException e) {
            // Maneja cualquier excepción de E/S que pueda ocurrir durante la escritura del archivo
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de vaciar una carpeta eliminando todos los archivos contenidos en ella.
     *
     * @param carpeta La ruta de la carpeta que se desea vaciar.
     */
    private static void vaciarCarpeta(String carpeta) {
        // Crea un objeto File que representa la carpeta especificada
        File directorio = new File(carpeta);

        // Obtiene una lista de archivos en la carpeta
        File[] archivos = directorio.listFiles();

        if (archivos != null) {
            // Recorre cada archivo en la lista
            for (File archivo : archivos) {
                // Verifica si el elemento en la lista es un archivo
                if (archivo.isFile()) {
                    try {
                        // Intenta eliminar el archivo
                        if (archivo.delete()) {
                            // Archivo eliminado con éxito, muestra un mensaje informativo
                            System.out.println("Archivo eliminado: " + archivo.getName());
                        } else {
                            // No se pudo eliminar el archivo, muestraun mensaje de advertencia
                            System.out.println("No se pudo eliminar el archivo: " + archivo.getName());
                        }
                    } catch (SecurityException e) {
                        // Maneja cualquier excepción de seguridad que pueda ocurrir durante la eliminación del archivo
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}