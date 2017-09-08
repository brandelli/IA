/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Brandelli
 */
public class Agente {
    private Estados estado;
    private Estados ultimaDirecao;
    private int bateria;
    private int lixo;
    private char[][] ambiente;
    private Ponto localizacao;
    
    public Agente(char[][] ambiente){
        this.estado = Estados.DESLIGADO;
        this.ambiente = ambiente;
        this.localizacao = new Ponto(0,0);
        this.startCleaning();
    }
    
    public void startCleaning(){
        this.estado = Estados.BAIXO;
        while(this.estado != Estados.TERMINADO){
            this.cleaning();
            this.viewRoom();
        }
        System.out.println("Lixos coletados"+this.lixo);
    }
    
    public void cleaning(){
        switch(this.estado){
            case BAIXO:
                this.moveBaixo();
                break;
            case CIMA:
                this.moveCima();
                break;
            case DIREITA:
                this.moveDireita();
                break;
            case ESQUERDA:
                this.moveEsquerda();
                break;
            case LIMPANDO:
                this.limpa();
                break;
        }
    }
    
    public void moveBaixo(){
        System.out.println("Baixo");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        if(this.ambiente[x][y] == 'S'){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.LIMPANDO;
            return;
        }
        if(x == this.ambiente.length-1 && y == this.ambiente.length-1){
            this.estado = Estados.TERMINADO;
            return;
        }
        if(x == this.ambiente.length-1){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DIREITA;
            return;
        }

        this.localizacao.x = x+1;
    }
    
    public void moveCima(){
        System.out.println("Cima");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        if(this.ambiente[x][y] == 'S'){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.LIMPANDO;
            return;
        }
        if(x == 0 && y == this.ambiente.length-1){
            this.estado = Estados.TERMINADO;
            return;
        }
        if(x == 0){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DIREITA;
            return;
        }
        this.localizacao.x = x-1;
    }

    public void moveDireita(){
        System.out.println("Direita");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        if(this.ultimaDirecao == Estados.CIMA){
            this.ultimaDirecao = Estados.BAIXO;
            this.estado = Estados.BAIXO;
            this.localizacao.y = y+1;
            return;
        }
        if(this.ultimaDirecao == Estados.BAIXO){
            this.ultimaDirecao = Estados.CIMA;
            this.estado = Estados.CIMA;
            this.localizacao.y = y+1;
            return;
        }     
    }

    public void moveEsquerda(){
        System.out.println("Esquerda");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        if(this.ultimaDirecao == Estados.CIMA){
            this.ultimaDirecao = Estados.BAIXO;
            this.estado = Estados.BAIXO;
            this.localizacao.y = y-1;
            return;
        }
        if(this.ultimaDirecao == Estados.BAIXO){
            this.ultimaDirecao = Estados.CIMA;
            this.estado = Estados.CIMA;
            this.localizacao.y = y-1;
            return;
        } 
    }

    public void limpa(){
        System.out.println("Limpando");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        lixo++;
        this.ambiente[x][y] = ' ';
        this.estado = this.ultimaDirecao;
        return;
    }
    
    public void viewRoom() {
        int size = this.ambiente.length;
        for (int i = 0; i < size; i++) {
            System.out.print(" -");
        }
        System.out.println("");
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                System.out.print(this.ambiente[i][j]);
                System.out.print("|");
            }
            System.out.println("");
            for (int j = 0; j < size; j++) {
                System.out.print(" -");
            }
            System.out.println("");
        }
    }
}
