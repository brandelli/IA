/**
 *
 * @author Bruno Brandelli, Rodrigo Pacheco
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ambiente amb = new Ambiente(12,10,10);
        amb.viewRoom();
        System.out.println("parede esquerda");
        amb.showPossiblePlacesLeft();
        System.out.println("parede direita");
        amb.showPossiblePlacesRight();
        System.out.println("numero de carregadores" + amb.showNumberOfElements('R'));
        System.out.println("numero de lixeiras" + amb.showNumberOfElements('L'));
    }
    
}
