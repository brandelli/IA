
import java.util.Random;

/**
 *
 * @author Bruno Brandelli, Rodrigo Pacheco
 */

public class Ambiente {
    private int size;
    private char[][] room;
    private int trash;
    private int chargers;
    private int innerWallSize;
    
    public Ambiente(int size, int trash, int chargers){
        if(size < 9){
           this.size = 9;
           System.out.println("Tamanho da sala muito pequeno, tamanho alterado para 9");
           size = this.size;
        }
        this.chargers = chargers;
        this.trash = trash;
        this.size = size;
        this.room = new char[size][size];
        this.innerWallSize = size - 6;
        this.resetRoom();
        this.putAgent();
        this.buildWalls();
        this.putFirstChargerAndTrash();
    }

    public int getSize() {
        return this.size;
    }
    
    public int getInnerWallSize(){
        return this.innerWallSize;
    }
    
    public void resetRoom(){
        int size = this.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.room[i][j] = ' ';
            }
        }
    }

    public void viewRoom(){
        int size = this.getSize();
        for (int i = 0; i < size; i++) {
            System.out.print(" -");
        }
        System.out.println("");
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                System.out.print(this.room[i][j]);
                System.out.print("|");
            }
            System.out.println("");
            for (int j = 0; j < size; j++) {
                System.out.print(" -");
            }
            System.out.println("");
        }
    }
    
    public void putAgent(){
        this.room[0][0] = 'A';
    }
    
    public void buildWalls(){
        int size = this.getSize();
        int endOfWall = size - 2;
        int buildingWall = 2;
        while (buildingWall < endOfWall) {
            this.room[buildingWall][3] = 'P';
            this.room[buildingWall][size - 4] = 'P';
            buildingWall++;
        }
        this.room[2][2] = 'P';
        this.room[endOfWall - 1][2] = 'P';
        this.room[2][size - 3] = 'P';
        this.room[endOfWall - 1][size - 3] = 'P';
    }
    
    public void putFirstChargerAndTrash(){
        Random rand = new Random();
        int sideCharger = rand.nextInt(1);
        int xRightSide = rand.nextInt(this.getInnerWallSize()) + 3;
        int xLeftSide = rand.nextInt(this.getInnerWallSize()) + 3;
        int yRightSide = rand.nextInt(2);
        int yLeftSide = this.getSize() - 1 - rand.nextInt(2);
        this.putElement(xRightSide, yRightSide, sideCharger == 1 ? 'R' : 'L');
        this.putElement(xLeftSide, yLeftSide, sideCharger == 0 ? 'R' : 'L');
    }
    
    public void putElement(int x, int y, char element){
        this.room[x][y] = element;
    }
}
