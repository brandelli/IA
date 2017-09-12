
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
 * @author Bruno Brandelli, Rodrigo Pacheco
 */
public class Agente {
    private Estados estado;
    private Estados ultimaDirecao;
    private int bateria;
    private int capLixo;
    private int capBateria;
    private int minBateria = 20;
    private int lixoColetado = 0;
    private int lixo;
    private Ambiente ambiente;
    private Point localizacao;
    
    public Agente(Ambiente amb, int capLixo, int capBateria){
        this.estado = Estados.DESLIGADO;
        this.ambiente = amb;
        this.localizacao = new Point(0,0);
        this.ambiente.putElement(this.localizacao, ' ');
        this.capLixo = capLixo;
        this.capBateria = capBateria;
        this.bateria = capBateria;
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
            this.ambiente.viewRoom(new Point(this.localizacao.x, this.localizacao.y));
            System.out.println("carga: "+this.bateria);
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
            case RECARREGANDO:
                this.recarrega();
                break;
        }
    }
    
    public void moveBaixo(){
        System.out.println("Baixo");
        int x,y;
        char element;
        x = this.localizacao.x;
        y = this.localizacao.y;
        element = this.ambiente.getElement(this.localizacao);
        if(this.bateria <= this.minBateria){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if(element == 'S'){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.LIMPANDO;
            return;
        }
        if(x == this.ambiente.getSize()-1 && y == this.ambiente.getSize()-1){
            this.estado = Estados.TERMINADO;
            return;
        }

        if(x == this.ambiente.getSize()-1){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DIREITA;
            return;
        }
        if (!Character.isWhitespace(element) && element != 'S') {
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DESVIANDO;
            return;
        }
        this.localizacao.x = x+1;
        this.bateria--;
    }
    
    public void moveCima(){
        System.out.println("Cima");
        int x,y;
        char element;
        x = this.localizacao.x;
        y = this.localizacao.y;
        element = this.ambiente.getElement(this.localizacao);
        if (this.bateria <= this.minBateria) {
            this.ultimaDirecao = this.estado;
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if(element == 'S'){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.LIMPANDO;
            return;
        }
        if(x == 0 && y == this.ambiente.getSize()-1){
            this.estado = Estados.TERMINADO;
            return;
        }

        if(x == 0){
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DIREITA;
            return;
        }
        if (!Character.isWhitespace(element) && element != 'S') {
            this.ultimaDirecao = this.estado;
            this.estado = Estados.DESVIANDO;
            return;
        }
        this.localizacao.x = x-1;
        this.bateria--;
    }

    public void moveDireita(){
        System.out.println("Direita");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        if(this.bateria <= this.minBateria){
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if(this.ultimaDirecao == Estados.CIMA){
            this.ultimaDirecao = Estados.BAIXO;
            this.estado = Estados.BAIXO;
            this.localizacao.y = y+1;
            this.bateria--;
            return;
        }
        if(this.ultimaDirecao == Estados.BAIXO){
            this.ultimaDirecao = Estados.CIMA;
            this.estado = Estados.CIMA;
            this.localizacao.y = y+1;
            this.bateria--;
            return;
        }     
    }

    public void moveEsquerda(){
        System.out.println("Esquerda");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        if (this.bateria <= this.minBateria) {
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if(this.ultimaDirecao == Estados.CIMA){
            this.ultimaDirecao = Estados.BAIXO;
            this.estado = Estados.BAIXO;
            this.localizacao.y = y-1;
            this.bateria--;
            return;
        }
        if(this.ultimaDirecao == Estados.BAIXO){
            this.ultimaDirecao = Estados.CIMA;
            this.estado = Estados.CIMA;
            this.localizacao.y = y-1;
            this.bateria--;
            return;
        } 
    }

    public void limpa(){
        System.out.println("Limpando");
        int x,y;
        x = this.localizacao.x;
        y = this.localizacao.y;
        this.lixo++;
        this.bateria--;
        this.ambiente.putElement(this.localizacao, ' ');
        if(this.lixo == this.capLixo){
            this.estado = Estados.ESVAZIANDO;
        }else{
            this.estado = this.ultimaDirecao;
        }
        return;
    }
    
    public void desvia(){
        System.out.println("Desvia");
        if (this.bateria <= this.minBateria) {
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if (this.localizacao.y == 0 || this.localizacao.y == 3 || this.localizacao.y == this.ambiente.getSize() - 3) {
            this.estado = Estados.DESVIANDO_DIREITA;
            this.localizacao.y++;
        } else {
            this.estado = Estados.DESVIANDO_ESQUERDA;
            this.localizacao.y--;
        }
        this.bateria--;
    }
    
    public void desviaDireita(){
        System.out.println("Desvia direita");
        Point ponto;
        char element;
        if (this.bateria <= this.minBateria) {
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if(this.ultimaDirecao == Estados.BAIXO){
            ponto = new Point(this.localizacao.x+1, this.localizacao.y-1);
            element = this.ambiente.getElement(ponto);
            if(Character.isWhitespace(element) || element == 'S'){
                this.localizacao.x++;
                this.localizacao.y--;
                this.estado = this.ultimaDirecao;
            }else{
                this.localizacao.x++;
            }
        }else{
            ponto = new Point(this.localizacao.x - 1, this.localizacao.y - 1);
            element = this.ambiente.getElement(ponto);
            if (Character.isWhitespace(element) || element == 'S') {
                this.localizacao.x--;
                this.localizacao.y--;
                this.estado = this.ultimaDirecao;
            } else {
                this.localizacao.x--;
            }
        }
        this.bateria--;
    }
    
    public void desviaEsquerda(){
        System.out.println("Desvia esquerda");
        Point ponto;
        char element;
        if (this.bateria <= this.minBateria) {
            this.estado = Estados.RECARREGANDO;
            return;
        }
        if (this.ultimaDirecao == Estados.BAIXO) {
            ponto = new Point(new Point(this.localizacao.x + 1, this.localizacao.y + 1));
            element = this.ambiente.getElement(ponto);
            if (Character.isWhitespace(element)|| element == 'S') {
                this.localizacao.x++;
                this.localizacao.y++;
                this.estado = this.ultimaDirecao;
            } else {
                this.localizacao.x++;
            }
        } else {
            ponto = new Point(new Point(this.localizacao.x - 1,this.localizacao.y + 1));
            element = this.ambiente.getElement(ponto);
            if (Character.isWhitespace(element) || element == 'S') {
                this.localizacao.x--;
                this.localizacao.y++;
                this.estado = this.ultimaDirecao;
            } else {
                this.localizacao.x--;
            }
        }
        this.bateria--;
    }
    
    public void esvazia(){
        System.out.println("Esvaziando");
        ArrayList<Point> pointList = new ArrayList<>();
        ArrayList<Point> ida = new ArrayList<>();
        ArrayList<Point> volta = new ArrayList<>();
        pointList = this.ambiente.getListElements('L');
        Nodo caminho, melhorCaminho;
        melhorCaminho = null;
        Point ultimoLugar = new Point(this.localizacao);
        double valorCaminho = Double.MAX_VALUE;
        for(Point p : pointList){
            System.out.println("inicio caminho");
            caminho = this.aStar(new Point(this.localizacao.x, this.localizacao.y),p);
            System.out.println("valor caminho: "+caminho.parent.finalCost);
            if(caminho.parent.finalCost < valorCaminho){
                valorCaminho = caminho.parent.finalCost;
                melhorCaminho = caminho;
            }
            while(caminho.parent != null){
                System.out.println(caminho.toString());
                caminho = caminho.parent;
            }
            System.out.println("final caminho");
        }
        
        System.out.println("Melhor caminho");
        System.out.println("Valor melhor caminho: "+melhorCaminho.parent.finalCost);
        while (melhorCaminho.parent != null) {
            System.out.println(melhorCaminho.toString());
            melhorCaminho = melhorCaminho.parent;
            ida.add(0,new Point(melhorCaminho.x, melhorCaminho.y));
        }
        System.out.println("Indo para lixeira");
        volta.add(0,ultimoLugar);
        for(Point p : ida){
            System.out.println(p.toString());
            this.localizacao = p;
            ambiente.viewRoom(p);
            volta.add(0,p);
        }
        System.out.println("chegou em vizinho da lixeira");
        System.out.println("Fim melhor caminho");
        System.out.println("voltando lugar original");
        for(Point p : volta){
            System.out.println(p.toString());
            this.localizacao = p;
            ambiente.viewRoom(p);
        }
        System.out.println("chegou lugar original");
        
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
            char element;
            if(atual.x != 0){
                if(atual.y > 0){
                    element = this.ambiente.getElement(new Point(atual.x-1, atual.y-1));
                    if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x-1, atual.y-1)))
                        sucessor.add(new Nodo(atual.x-1, atual.y-1));
                }
                element = this.ambiente.getElement(new Point(atual.x - 1, atual.y));
                if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x-1, atual.y)))
                    sucessor.add(new Nodo(atual.x-1, atual.y));
                if(atual.y < this.ambiente.getSize() -1){
                    element = this.ambiente.getElement(new Point(atual.x - 1, atual.y+1));
                    if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x-1, atual.y+1)))
                        sucessor.add(new Nodo(atual.x-1, atual.y+1));
                }
            }
            if(atual.x < this.ambiente.getSize() -1){
               if(atual.y > 0){   
                   element = this.ambiente.getElement(new Point(atual.x + 1, atual.y - 1));
                   if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x+1, atual.y-1))) 
                        sucessor.add(new Nodo(atual.x+1, atual.y-1));
                }
                element = this.ambiente.getElement(new Point(atual.x + 1, atual.y));
                if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x+1, atual.y)))
                    sucessor.add(new Nodo(atual.x+1, atual.y));
                if(atual.y < this.ambiente.getSize() -1){
                    element = this.ambiente.getElement(new Point(atual.x + 1, atual.y + 1));
                    if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x+1, atual.y+1)))
                        sucessor.add(new Nodo(atual.x+1, atual.y+1));
                } 
            }
            
            if(atual.y != 0){
                element = this.ambiente.getElement(new Point(atual.x, atual.y - 1));
                if((element != 'P' && element != 'L' && element != 'R')||Objective.equals(new Point(atual.x, atual.y-1)))
                    sucessor.add(new Nodo(atual.x, atual.y-1));
                
            }
            
            if(atual.y < this.ambiente.getSize() -1){
                element = this.ambiente.getElement(new Point(atual.x, atual.y+1));
                if((element != 'P' && element != 'L' && element != 'R')|| Objective.equals(new Point(atual.x, atual.y+1)))
                    sucessor.add(new Nodo(atual.x, atual.y+1));
            }
            for(Nodo vizinho: sucessor){
                vizinho.parent = atual;
                boolean flag = false;
                if(Objective.equals(new Point(vizinho.x, vizinho.y))){
                    return vizinho;
                }
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
    
    public void recarrega(){
        System.out.println("Recarregando");
        ArrayList<Point> pointList = new ArrayList<>();
        ArrayList<Point> ida = new ArrayList<>();
        ArrayList<Point> volta = new ArrayList<>();
        pointList = this.ambiente.getListElements('R');
        Nodo caminho, melhorCaminho;
        melhorCaminho = null;
        Point ultimoLugar = new Point(this.localizacao);
        double valorCaminho = Double.MAX_VALUE;
        for (Point p : pointList) {
            System.out.println("inicio caminho");
            caminho = this.aStar(new Point(this.localizacao.x, this.localizacao.y), p);
            System.out.println("valor caminho: " + caminho.parent.finalCost);
            if (caminho.parent.finalCost < valorCaminho) {
                valorCaminho = caminho.parent.finalCost;
                melhorCaminho = caminho;
            }
            while (caminho.parent != null) {
                System.out.println(caminho.toString());
                caminho = caminho.parent;
            }
            System.out.println("final caminho");
        }

        System.out.println("Melhor caminho");
        System.out.println("Valor melhor caminho: " + melhorCaminho.parent.finalCost);
        while (melhorCaminho.parent != null) {
            System.out.println(melhorCaminho.toString());
            melhorCaminho = melhorCaminho.parent;
            ida.add(0, new Point(melhorCaminho.x, melhorCaminho.y));
        }
        System.out.println("Indo para lixeira");
        volta.add(0, ultimoLugar);
        for (Point p : ida) {
            System.out.println(p.toString());
            this.localizacao = p;
            ambiente.viewRoom(p);
            volta.add(0, p);
        }
        System.out.println("chegou em vizinho da carga");
        System.out.println("Fim melhor caminho");
        System.out.println("voltando lugar original");
        for (Point p : volta) {
            System.out.println(p.toString());
            this.localizacao = p;
            ambiente.viewRoom(p);
        }
        System.out.println("chegou lugar original");

        this.estado = this.ultimaDirecao;
        this.bateria = this.capBateria;
        return;
    }
    
    public double diagonalDistace(Point start, Point end){
        double dx = Math.abs(start.x - end.x);
        double dy = Math.abs(start.y - end.y);
        return (1 *(dx + dy) + (Math.sqrt(2) - 2 * 1) * Math.min(dx, dy));
    }
}
