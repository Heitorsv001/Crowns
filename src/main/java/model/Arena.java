package model;
public class Arena {

  
    private int    numero;       
    private String nome;
    private String descricao;
    private Inimigo inimigo;
    private boolean desbloqueada;
    private int    recompensaMoedas; 

    public Arena(int numero, String nome, String descricao,
                 Inimigo inimigo, int recompensaMoedas, boolean desbloqueada) {
        this.numero           = numero;
        this.nome             = nome;
        this.descricao        = descricao;
        this.inimigo          = inimigo;
        this.recompensaMoedas = recompensaMoedas;
        this.desbloqueada     = desbloqueada;
    }

 
    public static Arena criarArena1() {
        return new Arena(
            1,
            "Pátio dos Iniciantes",
            "A primeira batalha. — aprenda as mecânicas basicas.",
            Inimigo.criarRingo(),
            50,
            true // aberta desde o início
        );
    }


    public static Arena criarArena2() {
        return new Arena(
            2,
            "Salão das Sombras",
            "Diego ataca em pares.",
            Inimigo.criarDiego(),
            100,
            false
        );
    }

 
    public static Arena criarArena3() {
        return new Arena(
            3,
            "Trono de Ferro",
            "Valentine tem um mega cavaleiro no bolso",
            Inimigo.criarValentine(),
            200,
            false
        );
    }


    public void desbloquear() {
        this.desbloqueada = true;
        this.inimigo.resetarParaBatalha();
    }

    public boolean isArenaFinal() {
        return inimigo.isEhBoss();
    }


    public int     getNumero()            { return numero; }
    public String  getNome()              { return nome; }
    public String  getDescricao()         { return descricao; }
    public Inimigo getInimigo()           { return inimigo; }
    public boolean isDesbloqueada()       { return desbloqueada; }
    public int     getRecompensaMoedas()  { return recompensaMoedas; }

    public void setDesbloqueada(boolean d){ this.desbloqueada = d; }
    public void setInimigo(Inimigo i)     { this.inimigo = i; }

    @Override
    public String toString() {
        return String.format("Arena{numero=%d, nome='%s', inimigo='%s', desbloqueada=%b}",
                numero, nome, inimigo.getNome(), desbloqueada);
    }
}
