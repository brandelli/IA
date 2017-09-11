
import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

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
    private int capLixo;
    private int capBateria;
    private int lixoColetado = 0;
    private int lixo;
    private char[][] ambiente;
    private Ponto localizacao;
    
    public Agente(char[][] ambiente, int capLixo, int capBateria){
        this.estado = Estados.DESLIGADO;
        this.ambiente = ambiente;
        this.ambiente[0][0] = ' ';
        this.localizacao = new Ponto(0,0);
        this.capLixo = capLixo;
        this.capBateria = capBateria;
        this.startCleaning();
    }
    
    private class Nodo implements Comparable<Nodo>{ 
        double stepsCost = 0;
        double heuristicCost = 0; //Heuristic cost
        double finalCost = 0; //G+H
        int x, y;
        Nodo parent; 
        
        Nodo(int x, int y){
            this.x = x;
            this.y = y; 
        }
        
        public int compareTo(Nodo n){
            if(this.finalCost < n.finalCost)
                return -1;
            else if(this.finalCost > n.finalCost)
                return 1;
            return 0;
        }
        @Override
        public String toString(){
            return "["+this.x+", "+this.y+"]";
        }
    }
    
    public void startCleaning(){
        this.estado = Estados.BAIXO;
        while(this.estado != Estados.TERMINADO){
            this.cleaning();
            this.viewRoom(this.ambiente);
        }
        System.out.println("Lixos coletados: "+(this.lixoColetado + this.lixo));
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
            case DESVIANDO:
                this.desvia();
                break;
            case DESVIANDO_DIREITA:
                this.desviaDireita();
                break;
            case DESVIANDO_ESQUERDA:
                this.desviaEsquerda();
                break;
            case ESVAZIANDO:
                this.esvazia();
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
        if (!Character.isWhitespace(this.ambiente[x][y]) && this.ambiente[x][y] != 'S') {
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DESVIANDO;
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
        if (!Character.isWhitespace(this.ambiente[x][y]) && this.ambiente[x][y] != 'S') {
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DESVIANDO;
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
        this.lixo++;
        this.ambiente[x][y] = ' ';
        if(this.lixo == this.capLixo){
            this.estado = Estados.ESVAZIANDO;
        }else{
            this.estado = this.ultimaDirecao;
        }
        return;
    }
    
    public void viewRoom(char[][] ambiente){
        int size = ambiente.length;
        for (int i = 0; i < size; i++) {
            System.out.print(" -");
        }
        System.out.println("");
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                if(i == this.localizacao.x && j == this.localizacao.y)
                    System.out.print('A');
                else
                    System.out.print(ambiente[i][j]);
                System.out.print("|");
            }
            System.out.println("");
            for (int j = 0; j < size; j++) {
                System.out.print(" -");
            }
            System.out.println("");
        }
    }
    
    public void desvia(){
        System.out.println("Desvia");
        if (this.localizacao.y == 0 || this.localizacao.y == 3 || this.localizacao.y == this.ambiente.length - 3) {
            this.estado = Estados.DESVIANDO_DIREITA;
            this.localizacao.y++;
        } else {
            this.estado = Estados.DESVIANDO_ESQUERDA;
            this.localizacao.y--;
        }
    }
    
    public void desviaDireita(){
        System.out.println("Desvia direita");
        if(this.ultimaDirecao == Estados.BAIXO){
            if(Character.isWhitespace(this.ambiente[this.localizacao.x+1][this.localizacao.y-1]) || this.ambiente[this.localizacao.x+1][this.localizacao.y-1] == 'S'){
                this.localizacao.x++;
                this.localizacao.y--;
                this.estado = this.ultimaDirecao;
            }else{
                this.localizacao.x++;
            }
        }else{
            if (Character.isWhitespace(this.ambiente[this.localizacao.x - 1][this.localizacao.y - 1]) || this.ambiente[this.localizacao.x - 1][this.localizacao.y - 1] == 'S') {
                this.localizacao.x--;
                this.localizacao.y--;
                this.estado = this.ultimaDirecao;
            } else {
                this.localizacao.x--;
            }
        }
    }
    
    public void desviaEsquerda(){
        System.out.println("Desvia esquerda");
        if (this.ultimaDirecao == Estados.BAIXO) {
            if (Character.isWhitespace(this.ambiente[this.localizacao.x + 1][this.localizacao.y + 1])|| this.ambiente[this.localizacao.x + 1][this.localizacao.y + 1] == 'S') {
                this.localizacao.x++;
                this.localizacao.y++;
                this.estado = this.ultimaDirecao;
            } else {
                this.localizacao.x++;
            }
        } else if (Character.isWhitespace(this.ambiente[this.localizacao.x - 1][this.localizacao.y + 1]) || this.ambiente[this.localizacao.x - 1][this.localizacao.y + 1] == 'S') {
            this.localizacao.x--;
            this.localizacao.y++;
            this.estado = this.ultimaDirecao;
        } else {
            this.localizacao.x--;
        }
    }
    
    public void esvazia(){
        System.out.println("Esvaziando");
        Nodo caminho = this.aStar(new Point(this.localizacao.x, this.localizacao.y), new Point(2, 2));
        while(caminho.parent != null){
            System.out.println(caminho.toString());
            caminho = caminho.parent;
        }
        this.estado = this.ultimaDirecao;
        this.lixoColetado = this.lixoColetado + this.lixo;
        this.lixo = 0;
        return;
    }
    
    public Nodo aStar(Point start, Point Objective){
        Nodo atual = null;
        Nodo comp = null;
        ArrayList<Nodo> aberto =  new ArrayList<>();
        ArrayList<Nodo> fechado = new ArrayList<>();
        Nodo nodo = new Nodo(start.x, start.y);
        nodo.finalCost = 0;
        nodo.heuristicCost = 0;
        aberto.add(nodo);
        while(!aberto.isEmpty()){
            double max_final_cost = Double.MAX_VALUE;
            for(Nodo n:aberto){
                if(n.finalCost < max_final_cost){
                    atual = n;
                    max_final_cost = n.finalCost;
                }
            }
            aberto.remove(atual);                                       
            fechado.add(atual);
            ArrayList<Nodo> sucessor = new ArrayList<>();
            if(atual.x != 0){
                if(atual.y > 0){
                    sucessor.add(new Nodo(atual.x-1, atual.y-1));
                }
                sucessor.add(new Nodo(atual.x-1, atual.y));
                if(atual.y < this.ambiente.length -1){
                    sucessor.add(new Nodo(atual.x-1, atual.y+1));
                }
            }
            if(atual.x < this.ambiente.length -1){
               if(atual.y > 0){
                    sucessor.add(new Nodo(atual.x+1, atual.y-1));
                }
                sucessor.add(new Nodo(atual.x+1, atual.y));
                if(atual.y < this.ambiente.length -1){
                    sucessor.add(new Nodo(atual.x+1, atual.y+1));
                } 
            }
            
            if(atual.y != 0){
                sucessor.add(new Nodo(atual.x, atual.y-1));
            }
            
            if(atual.y < this.ambiente.length -1){
                sucessor.add(new Nodo(atual.x, atual.y+1));
            }
            for(Nodo vizinho: sucessor){
                vizinho.parent = atual;
                boolean flag = false;
                if(Objective.equals(new Point(vizinho.x, vizinho.y)))
                    return vizinho;
                vizinho.stepsCost = atual.stepsCost + 1;
                vizinho.heuristicCost = this.diagonalDistace(new Point(vizinho.x,vizinho.y), Objective);
                vizinho.finalCost = vizinho.stepsCost + vizinho.heuristicCost;
                for(Nodo n : aberto){
                    if(n.x == vizinho.x && n.y == vizinho.y){
                        if(n.finalCost > vizinho.finalCost){
                            n.parent = vizinho.parent;
                            n.finalCost = vizinho.finalCost;
                            flag = true;
                            break;
                        }
                    }
                }
                for(Nodo n : fechado){
                    if(n.x == vizinho.x && n.y == vizinho.y){
                        if(n.finalCost > vizinho.finalCost){
                            aberto.add(vizinho);
                            flag = true;
                        }
                    }
                }
                if(flag == false){
                    aberto.add(vizinho);
                }
                
            } 
        }
        return null;
    }
    
    public double diagonalDistace(Point start, Point end){
        double dx = Math.abs(start.x - end.x);
        double dy = Math.abs(start.y - end.y);
        return (1 *(dx + dy) + (Math.sqrt(2) - 2 * 1) * Math.min(dx, dy));
    }
}
