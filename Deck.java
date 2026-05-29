package model;

import java.util.ArrayList;
import java.util.List;


public class Deck {


    public static final int TAMANHO_MAXIMO = 4;

    private List<Carta> cartas;
    private boolean emBatalha;


    public Deck() {
        this.cartas = new ArrayList<>();
        this.emBatalha = false;
    }

    
    public boolean adicionarCarta(Carta carta) {
        if (emBatalha) return false;
        if (cartas.size() >= TAMANHO_MAXIMO) return false;
        if (carta == null) return false;
        cartas.add(carta);
        return true;
    }

    
    public Carta removerCarta(int indice) {
        if (emBatalha) return null;
        if (indice < 0 || indice >= cartas.size()) return null;
        return cartas.remove(indice);
    }

    public boolean substituirCarta(int indice, Carta novaCarta) {
        if (emBatalha) return false;
        if (indice < 0 || indice >= cartas.size()) return false;
        if (novaCarta == null) return false;
        cartas.set(indice, novaCarta);
        return true;
    }

   
    public List<Carta> getCartasDisponiveis() {
        List<Carta> disponiveis = new ArrayList<>();
        for (Carta c : cartas) {
            if (c.isDisponivel()) {
                disponiveis.add(c);
            }
        }
        return disponiveis;
    }

   
    public boolean estaCompleto() {
        return cartas.size() == TAMANHO_MAXIMO;
    }

    
    public void prepararParaBatalha() {
        for (Carta c : cartas) {
            c.resetarHP();
            c.voltarDaRecarga();
        }
        emBatalha = true;
    }

  
    public void encerrarBatalha() {
        emBatalha = false;
    }

    public boolean todasMortas() {
        for (Carta c : cartas) {
            if (c.estaViva()) return false;
        }
        return true;
    }

    

    public List<Carta> getCartas() { return cartas; }

    public boolean isEmBatalha() { return emBatalha; }

    public int getTamanho() { return cartas.size(); }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Deck (" + cartas.size() + "/" + TAMANHO_MAXIMO + "):\n");
        for (int i = 0; i < cartas.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(cartas.get(i)).append("\n");
        }
        return sb.toString();
    }
}