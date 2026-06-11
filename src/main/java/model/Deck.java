package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um deck de cartas.
 *
 * Regras aplicadas:
 *  - RN1:  deck deve ter exatamente 4 cartas
 *  - RN13: troca só permitida fora da batalha
 *  - RF8:  jogador pode substituir cartas entre batalhas
 */
public class Deck {

    public static final int TAMANHO_MAXIMO = 4;

    private List<Carta> cartas;
    private String nome; // identificação opcional do deck

    // =========================================================
    // Construtores
    // =========================================================

    public Deck(String nome) {
        this.nome   = nome;
        this.cartas = new ArrayList<>();
    }

    public Deck() {
        this("Deck Principal");
    }

    // =========================================================
    // Montagem do deck
    // =========================================================

    /**
     * Adiciona carta ao deck.
     * Lança exceção se já estiver cheio (RN1).
     */
    public void adicionarCarta(Carta carta) {
        if (cartas.size() >= TAMANHO_MAXIMO) {
            throw new IllegalStateException(
                "O deck já possui " + TAMANHO_MAXIMO + " cartas. Remova uma antes de adicionar.");
        }
        cartas.add(carta);
    }

    /**
     * Remove carta pelo índice (0–3).
     * Usada para troca entre batalhas (RF8 / RN13).
     */
    public Carta removerCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return cartas.remove(indice);
    }

    /**
     * Substitui a carta em determinada posição (RN13 — fora de batalha).
     */
    public void substituirCarta(int indice, Carta novaCarta) {
        if (indice < 0 || indice >= TAMANHO_MAXIMO) {
            throw new IndexOutOfBoundsException("Posição inválida: " + indice);
        }
        if (indice < cartas.size()) {
            cartas.set(indice, novaCarta);
        } else {
            cartas.add(novaCarta);
        }
    }

    /**
     * Verifica se o deck está completo (exatamente 4 cartas — RN1).
     */
    public boolean estaCompleto() {
        return cartas.size() == TAMANHO_MAXIMO;
    }

    /**
     * Retorna cartas vivas (HP > 0) — útil durante batalha.
     */
    public List<Carta> getCartasVivas() {
        List<Carta> vivas = new ArrayList<>();
        for (Carta c : cartas) {
            if (!c.estaMorta()) {
                vivas.add(c);
            }
        }
        return vivas;
    }

    /**
     * Verifica se todas as cartas estão mortas (derrota ou fim de turno).
     */
    public boolean todasMortas() {
        return getCartasVivas().isEmpty();
    }

    /**
     * Reseta vida de todas as cartas para o máximo (início de batalha).
     */
    public void resetarParaBatalha() {
        for (Carta c : cartas) {
            c.resetarVida();
        }
    }

    /**
     * Atualiza estado de recarga de todas as cartas.
     * Deve ser chamado a cada tick do jogo.
     */
    public void atualizarRecargas() {
        for (Carta c : cartas) {
            c.atualizarRecarga();
        }
    }

    // =========================================================
    // Getters
    // =========================================================

    public List<Carta> getCartas()    { return cartas; }
    public String getNome()           { return nome; }
    public int getTamanho()           { return cartas.size(); }

    public Carta getCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) return null;
        return cartas.get(indice);
    }

    public void setNome(String nome)  { this.nome = nome; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Deck '" + nome + "' [" + cartas.size() + "/4]:\n");
        for (int i = 0; i < cartas.size(); i++) {
            sb.append("  [").append(i).append("] ").append(cartas.get(i)).append("\n");
        }
        return sb.toString();
    }
}
