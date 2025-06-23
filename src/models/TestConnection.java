package models;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = new ConnectionMysql().getConnection();

        if (conn == null) {
            System.err.println("La conexión NO se estableció correctamente.");
        } else {
            System.out.println("Conexión exitosa. ¡Lista para usar!");
        }
    }
}

    
    
    
    

