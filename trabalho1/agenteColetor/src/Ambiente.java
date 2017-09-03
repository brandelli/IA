
import java.util.ArrayList;
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
    private ArrayList<Integer> hasSpaceLeft;
    private ArrayList<Integer> hasSpaceRight;
    
    public Ambiente(int size, int trash, int chargers){
        if(size < 9){
           this.size = 9;
           System.out.println("Tamanho da sala muito pequeno, tamanho alterado para 9");
           size = this.size;
        }
        this.chargers = chargers > 0 ? chargers : 1;
        this.trash = trash > 0 ? trash : 1;
        this.size = size;
        this.room = new char[size][size];
        this.innerWallSize = size - 6;
        this.hasSpaceLeft = new ArrayList<>();
        this.hasSpaceRight = new ArrayList<>();
        this.initializeInnerWallAvailableSpaces(hasSpaceLeft);
        this.initializeInnerWallAvailableSpaces(hasSpaceRight);
        this.resetRoom();
        this.putAgent();
        this.buildWalls();
        this.putFirstChargerAndTrash();
        for (int i = 0; i < chargers - 1; i++) {
            if(!putSpecialElement('R'))
                break;
            System.out.println("putting charger: "+(i+2));
        }
        for (int i = 0; i < trash - 1; i++) {
            if(!putSpecialElement('L'))
                break;
            System.out.println("putting trash: "+(i+2));
        }
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
        int row;
        int sideCharger = rand.nextInt(2);
        int xRightSide = rand.nextInt(this.hasSpaceRight.size());
        int xLeftSide = rand.nextInt(this.hasSpaceLeft.size());
        
        row = this.hasSpaceRight.get(xRightSide);
        this.putElement(row, this.size - 3,sideCharger == 1 ? 'R' : 'L');
        row = this.hasSpaceLeft.get(xLeftSide);
        this.putElement(row, 2, sideCharger == 0 ? 'R' : 'L');
        this.hasSpaceLeft.remove(xLeftSide);
        this.hasSpaceRight.remove(xRightSide);
    }
    
    public void putElement(int x, int y, char element){
        this.room[x][y] = element;
    }
    
    public void initializeInnerWallAvailableSpaces(ArrayList<Integer> side){
        for (int i = 0; i < this.innerWallSize; i++) {
            side.add(i+3);
        }
    }
    
    public void showPossiblePlacesRight(){
        this.showPossiblePlaces(this.hasSpaceRight);
    }
    
    public void showPossiblePlacesLeft() {
        this.showPossiblePlaces(this.hasSpaceLeft);
    }
    
    public void showPossiblePlaces(ArrayList<Integer> side){
        for (int b : side) {
            System.out.println(b);
        }
    }
    
    public boolean putSpecialElement(char element){
        Random rand = new Random();
        int side = rand.nextInt(2);
        int row, rowIndex;
        if(side == 0 && this.hasSpaceLeft.size() > 0){
            rowIndex = rand.nextInt(this.hasSpaceLeft.size());
            row = this.hasSpaceLeft.get(rowIndex);
            this.hasSpaceLeft.remove(rowIndex);
            System.out.println("botando elemento na parede esquerda, linha:"+ row);
            this.room[row][2] = element;
            return true;
        }else if(this.hasSpaceRight.size() > 0){
            rowIndex = rand.nextInt(this.hasSpaceRight.size());
            row = this.hasSpaceRight.get(rowIndex);
            this.hasSpaceRight.remove(rowIndex);
            System.out.println("botando elemento na parede direita, linha:" + row);
            this.room[row][this.size - 3] = element;
            return true; 
        }else if(this.hasSpaceLeft.size() > 0){
            rowIndex = rand.nextInt(this.hasSpaceLeft.size());
            row = this.hasSpaceLeft.get(rowIndex);
            this.hasSpaceLeft.remove(rowIndex);
            System.out.println("botando elemento na parede esquerda, linha:"+ row);
            this.room[row][2] = element;
            return true;
        }
        return false;
    }
    
    public int showNumberOfElements(char element){
        int nElements = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(this.room[i][j] == element)
                    nElements++;
            }
        }
        return nElements;
    }
    
    
    
}
