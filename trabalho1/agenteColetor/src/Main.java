/**
 *
 * @author Bruno Brandelli, Rodrigo Pacheco
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ambiente amb = new Ambiente(12,0,0);
        amb.viewRoom();
        System.out.println(amb.getInnerWallSize());
    }
    
}
