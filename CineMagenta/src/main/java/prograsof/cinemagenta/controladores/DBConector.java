package prograsof.cinemagenta.controladores;

// Importamos las clases necesarias para la conexión con base de datos MySQL usando JDBC.

import java.sql.Connection;     // Representa la conexión activa con la base de datos.
import java.sql.DriverManager; // Clase que gestiona los controladores JDBC y permite obtener conexiones.
import java.sql.SQLException;  // Excepción que se lanza si hay errores al interactuar con la base de datos.
import java.util.Properties; 
import java.io.FileInputStream;
import java.io.IOException;

// Clase pública que gestiona la conexión a la base de datos.
public class DBConector {
    
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, pass);
    }
    
    /**
     * Método estático que devuelve un objeto Connection
     * Lanza SQLException si ocurre un error al conectarse.
     * @return 
     * @throws java.sql.SQLException
     * public static Connection getConnection() throws SQLException {
        // Usamos DriverManager para obtener la conexión con los parámetros definidos arriba.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
     */
    
    /**
    * Método de prueba para verificar si la conexión a la base de datos funciona correctamente.
    * No lanza error hacia fuera, sino que imprime el resultado en consola.
    */
    public static void test() {
    // Se usa un bloque try-with-resources para asegurarse que la conexión se cierre automáticamente.
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión OK (DBConector.test)");
            } else {
                System.out.println("⚠️ Conexión nula o cerrada (DBConector.test)");
            }
        } catch (SQLException e) {
            System.out.println("❌ No se pudo conectar a la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("⚠️ Error leyendo archivo config.properties: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Error inesperado en la prueba de conexión: " + e.getMessage());
        }
    }


}