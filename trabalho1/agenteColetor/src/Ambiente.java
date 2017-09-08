
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
    private int dirty;
    private ArrayList<Integer> hasSpaceLeft;
    private ArrayList<Integer> hasSpaceRight;
    private Agente agente;
    
    public Ambiente(int size, int trash, int chargers, int dirty){
        if(size < 9){
           this.size = 9;
           System.out.println("Tamanho da sala muito pequeno, tamanho alterado para 9");
           size = this.size;
        }
        if(dirty < 45 || dirty > 80)
            dirty = 50;
        this.dirty = dirty;
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
        System.out.println("sala vazia: "+this.showRoomFreeSpaces());
        this.putAgent();
        System.out.println("com agente: "+this.showRoomFreeSpaces());
        this.buildWalls();
        System.out.println("com paredes: "+this.showRoomFreeSpaces());
        this.putSpecialElement('R');
        this.putSpecialElement('L');
        for (int i = 0; i < trash - 1; i++) {
            if(!putSpecialElement('L'))
                break;
        }
        for (int i = 0; i < chargers - 1; i++) {
            if(!putSpecialElement('R'))
                break;
        }
        System.out.println("Com lixeiras e carregadores: "+this.showRoomFreeSpaces());
        this.putDirty();
        this.viewRoom();
        int lixos = this.showNumberOfElements('S');
        this.agente = new Agente(this.room);
        System.out.println("numero de sujeiras " + lixos);
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
        int row, rowIndex, columnIndex;
        //0 parede, 1 limite mapa
        columnIndex = rand.nextInt(2);
        if(side == 0 && this.hasSpaceLeft.size() > 0){
            rowIndex = rand.nextInt(this.hasSpaceLeft.size());
            row = this.hasSpaceLeft.get(rowIndex);
            columnIndex = ((columnIndex == 0 && this.room[row][2] == ' ') || (columnIndex == 1 && this.room[row][0] != ' ')) ? 2 : 0;
            this.room[row][columnIndex] = element;
            if(this.room[row][0] != ' ' && this.room[row][2] != ' ')
                this.hasSpaceLeft.remove(rowIndex);
            System.out.println("Element: "+element+" Row: "+row+ " Column: "+columnIndex);
            return true;
        }else if(this.hasSpaceRight.size() > 0){
            rowIndex = rand.nextInt(this.hasSpaceRight.size());
            row = this.hasSpaceRight.get(rowIndex);
            columnIndex = ((columnIndex == 0 && this.room[row][this.size - 3] == ' ') || (columnIndex == 1 && this.room[row][this.size - 1] != ' ')) ? this.size - 3 : this.size - 1;
            this.room[row][columnIndex] = element;
            if(this.room[row][this.size - 1] != ' ' && this.room[row][this.size - 3] != ' ')
                this.hasSpaceRight.remove(rowIndex);
            System.out.println("Element: "+element+" Row: "+row+ " Column: "+columnIndex);
            return true; 
        }else if(this.hasSpaceLeft.size() > 0){
            rowIndex = rand.nextInt(this.hasSpaceLeft.size());
            row = this.hasSpaceLeft.get(rowIndex);
            columnIndex = ((columnIndex == 0 && this.room[row][2] == ' ') || (columnIndex == 1 && this.room[row][0] != ' ')) ? 2 : 0;
            this.room[row][columnIndex] = element;
            if(this.room[row][0] != ' ' && this.room[row][2] != ' ')
                this.hasSpaceLeft.remove(rowIndex);
            System.out.println("Element: "+element+" Row: "+row+ " Column: "+columnIndex);
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
    
    public int showRoomFreeSpaces(){
        int spaces = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(this.room[i][j] == ' ')
                    spaces++;
            }
        }
        return spaces;
    }
    
    public void putDirty(){
        Random rand = new Random();
        int freeSpaces = this.showRoomFreeSpaces();
        float percentage = (float)(this.dirty) / 100;
        int nDirty = (int)(freeSpaces * percentage);
        int x,y;
        System.out.println("Numero de sujeiras: "+nDirty);
        while (nDirty > 0) {            
            x = rand.nextInt(this.size);
            y = rand.nextInt(this.size);
            System.out.println("x: "+x+ " y: "+y);
            if(this.room[x][y] == ' '){
                System.out.println("sujeira colocada");
                nDirty--;
                this.room[x][y] = 'S';
            }else{
                System.out.println("Lugar ja preenchido");
            }
        }
    }
    
    
    
}
