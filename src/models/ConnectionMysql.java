
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMysql {
    
    private final String database_name = "pharmacy_database";
    private final String user = "root";
    private final String password = "Saban24";
    private final String url = "jdbc:mysql://localhost:3306/" + database_name;
    private Connection conn = null; // Usa 'private' en lugar de 'Connection conn = null;'

    public Connection getConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Intentar conectar
            
            
           conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión establecida correctamente."); // Depuración
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error SQL al intentar conectar: " + e.getMessage());
        }

        if (conn == null) {
            System.err.println("Error: La conexión a la base de datos es NULL. Verifica las credenciales y el servidor.");
        }
        return conn;
    }
}
