package com.mycompany.crowns.dao;

import java.sql.*;
import com.mycompany.crowns.dao.ConexaoDB;

public class CartaDAO {

    // Busca o ID de uma carta pelo nome (usado pelos outros DAOs)
    public static int buscarId(String nomeCarta) {
        String sql = "SELECT id FROM cartas WHERE nome = ?";
        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nomeCarta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            System.err.println("[CartaDAO] Erro ao buscar carta: " + e.getMessage());
        }
        return -1;
    }
} 