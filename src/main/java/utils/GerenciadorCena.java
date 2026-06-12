package utils;

import com.mycompany.crowns.App;
import java.io.IOException;
public class GerenciadorCena {

    public static void irPara(String nomeTela) {
        try {
            App.setRoot(nomeTela);
        } catch (IOException e) {
            System.err.println("[GerenciadorCena] Erro ao carregar tela: " + nomeTela);
            e.printStackTrace();
        }
    }

    public static void irParaMenu()       { irPara("primary"); }
    public static void irParaArena()      { irPara("arena"); }
    public static void irParaDeck()       { irPara("deck"); }
    public static void irParaLoja()       { irPara("loja"); }
    public static void irParaRecompensa() { irPara("recompensa"); }
    public static void irParaHistorico()  { irPara("historico"); }
    public static void irParaCreditos()   { irPara("creditos"); }
}
