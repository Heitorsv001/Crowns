package model;

import java.util.ArrayList;
import java.util.List;


public class Deck {

    public static final int TAMANHO_MAXIMO = 4;

    private List<Carta> cartas;
    private String nome;


    public Deck(String nome) {
        this.nome   = nome;
        this.cartas = new ArrayList<>();
    }

    public Deck() {
        this("Deck Principal");
    }


    public void adicionarCarta(Carta carta) {
        if (cartas.size() >= TAMANHO_MAXIMO) {
            throw new IllegalStateException(
                "O deck já possui " + TAMANHO_MAXIMO + " cartas. Remova uma antes de adicionar.");
        }
        cartas.add(carta);
    }

    public Carta removerCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return cartas.remove(indice);
    }

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

   
    public boolean estaCompleto() {
        return cartas.size() == TAMANHO_MAXIMO;
    }

    public List<Carta> getCartasVivas() {
        List<Carta> vivas = new ArrayList<>();
        for (Carta c : cartas) {
            if (!c.estaMorta()) {
                vivas.add(c);
            }
        }
        return vivas;
    }

 
    public boolean todasMortas() {
        return getCartasVivas().isEmpty();
    }

    public void resetarParaBatalha() {
        for (Carta c : cartas) {
            c.resetarVida();
        }
    }

 
    public void atualizarRecargas() {
        for (Carta c : cartas) {
            c.atualizarRecarga();
        }
    }


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
