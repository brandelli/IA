/**
 *
 * @author Bruno Brandelli, Rodrigo Pacheco
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ambiente amb = new Ambiente(20,10,20,80);
        amb.viewRoom();
        System.out.println("parede esquerda");
        amb.showPossiblePlacesLeft();
        System.out.println("parede direita");
        amb.showPossiblePlacesRight();
        System.out.println("numero de lixeiras" + amb.showNumberOfElements('L'));
        System.out.println("numero de carregadores" + amb.showNumberOfElements('R'));
        amb.viewRoom();
        System.out.println("numero de sujeiras " + amb.showNumberOfElements('S'));
    }
    
}
