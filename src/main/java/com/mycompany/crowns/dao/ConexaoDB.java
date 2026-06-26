package com.mycompany.crowns.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {

    private static final String URL      = "jdbc:postgresql://localhost:5432/Crowns";
    private static final String USUARIO  = "postgres";
    private static final String SENHA    = "postgresql";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}