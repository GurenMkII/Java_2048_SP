package com.codegym.games.game2048;
import com.codegym.engine.cell.*;
import java.util.Arrays;


public class Game2048 extends Game {
    
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;
    
    @Override
    public void initialize(){
        
        setScreenSize(SIDE, SIDE);
        
        createGame();
        
        drawScene();
        
    }
    
    @Override
    public void onKeyPress(Key key){
        
        if(isGameStopped){
            
            if(key == Key.SPACE){
                isGameStopped = false;
                createGame();
                score = 0;
                setScore(score);
                drawScene();
            }
        }else if (!canUserMove()){
            gameOver();
        } else if (key == Key.LEFT){
            moveLeft();
            drawScene();
        } else if (key == Key.RIGHT){
            moveRight();
            drawScene();
        } else if (key == Key.UP){
            moveUp();
            drawScene();
        } else if (key == Key.DOWN){
            moveDown();
            drawScene();
        }
        
    }
    
    private void createGame(){
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }
    
    private void drawScene(){
        for (int row = 0; row<gameField.length; row++){
            for (int col = 0; col<gameField[row].length; col++){
                // setCellColor(row, col, Color.GRAY);
                setCellColoredNumber(row, col, gameField[col][row]);
            }
        }
    }
    
    private void createNewNumber(){
        
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        int value;    
        if(getRandomNumber(10) == 9){
            value = 4;
        }
        else{ 
            value = 2; 
        }
        if (gameField[x][y] != 0){
            createNewNumber();
        }
        else{
            gameField[x][y] = value;
        }
        if(getMaxTileValue() == 2048 ){
            win();
        }
        
       
        
        // while (true) {
        //     int randRow = getRandomNumber(SIDE);
        //     int randCol = getRandomNumber(SIDE);
        //     int randRoll = getRandomNumber(10);
            
            
        //     if (gameField[randRow][randCol] == 0) {
        //         if (randRoll == 9){
        //             gameField[randRow][randCol] = 4;
        //             setCellColoredNumber(randRow, randCol, 4);
        //         } else {
        //             gameField[randRow][randCol] = 2;
        //             setCellColoredNumber(randRow, randCol, 2);
        //         }
        //         break;    
        //     }
            
        // } 
        // if (getMaxTileValue() == 2048){
        //     win();
        // }
        
    }
    
    private Color getColorByValue(int value) {
        Color color = null;
        switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.RED;
                break;
            case 8:
                color = Color.CYAN;
                break;
            case 16:
                color = Color.GREEN;
                break;
            case 32:
                color = Color.YELLOW;
                break;
            case 64:
                color = Color.ORANGE;
                break;
            case 128:
                color = Color.PINK;
                break;
            case 256:
                color = Color.MAGENTA;
                break;
            case 512:
                color = Color.BLACK;
                break;
            case 1024:
                color = Color.PURPLE;
                break;
            case 2048:
                color = Color.GRAY;
                break;
            default:
                break;
        }    
            return color;    
    }
    
    private void setCellColoredNumber(int row, int col, int value){
        Color color = getColorByValue(value);
        if (value == 0){
            setCellValueEx(row, col, color, "");
        } else {
            setCellValueEx(row, col, color, Integer.toString(value));
        }
    }
    
    private boolean compressRow(int[] row){
        int temp = 0;
        int[] rowtemp = row.clone(); 
        boolean isCompressed = false;
        for(int i = 0; i < row.length; i++) {
            for(int j=0; j < row.length-i-1; j++){
               if(row[j] == 0) {
                  temp = row[j];
                  row[j] = row[j+1];
                  row[j+1] = temp;
               }
            }
        }
        
        if(!Arrays.equals(row,rowtemp)){
            isCompressed = true;
            
        }
        
        return isCompressed;
    }
    
    private boolean mergeRow(int[] row){
        
        boolean isMerged = false;
        for (int i = 0; i < row.length-1; i++){
            if ((row[i]!=0) && (row[i] == row[i+1])){
                score += row[i]*2;
                setScore(score);
                row[i] = row[i] * 2;
                row[i+1] = 0;
                isMerged = true;
            }
        }
        
        
        return isMerged;
    }
    
    private void moveLeft(){
        
        // int move = 0;
    
        // for (int i = 0; i<SIDE; i++){    
        //     compressRow(gameField[i]);
        //     mergeRow(gameField[i]);
        //     compressRow(gameField[i]);
        //     if (compressRow(gameField[i]) || mergeRow(gameField[i])){
        //         move++;
        //     }
        // }
        // if (move != 0){
        //     createNewNumber();
        // }
        
        boolean compress;    // variable to get return from compressRow
        boolean merge;          // variable to get return from mergeRow
        boolean compresss;     // variable to get return from compressRow
        int move=0;                  // to check if compressRow or mergeRow occurs
        for (int i = 0; i < SIDE; i++){
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compresss=compressRow(gameField[i]);
            if(compress || merge || compresss){
                  move++;
            }
        }
        if( move!= 0 ){
          createNewNumber();
        }
        
    }
    
    private void moveRight(){
        
        rotateClockwise();
        rotateClockwise();
        
        moveLeft();
        
        rotateClockwise();
        rotateClockwise();
        
    }
    
    private void moveUp(){
        
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        
        moveLeft();
        
        rotateClockwise();
        
    }
    
    private void moveDown(){
        
        rotateClockwise();
        
        moveLeft();
        
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        
    }
    
    private void rotateClockwise(){
        // int[][] tempGameField = gameField.clone();
        // for (int row = 0; row < SIDE; row++){
        //     for (int col = 0; col < SIDE; col++){
        //         gameField[col][SIDE - 1 - row] = tempGameField[row][col];
        //     }
        // }
        
        int[][] tempMat = new int[SIDE][SIDE];
        for(int r = 0; r<SIDE; r++){
            for(int c = 0; c<SIDE ; c++){
                tempMat[c][SIDE - 1 - r] = gameField[r][c];
            }
        }
        for(int r = 0; r < SIDE; r++){
            for (int c = 0; c < SIDE; c++){
                gameField[r][c] = tempMat[r][c];
            }
        }
        
    }
    
    private int getMaxTileValue(){
        int value = 0;
        for (int row = 0; row < SIDE; row++){
            for (int col = 0; col < SIDE; col++){
                if (gameField[row][col] > value){
                    value = gameField[row][col];
                }
            }
        }
        return value;
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "You've won!!!", Color.BLUE, 28);
        
    }
    
    private boolean canUserMove(){
        for (int row = 0; row < SIDE; row++){
            for (int col = 0; col < SIDE; col++){
                if (gameField[row][col] == 0){
                    return true;
                }
            }
        }
        
        for (int row = 0; row < SIDE; row++){
            for (int col = 0; col < SIDE - 1; col++){
                if (gameField[row][col] == gameField[row][col+1]){
                    return true;
                }
            }
        }
        
        for (int row = 0; row < SIDE - 1; row++){
            for (int col = 0; col < SIDE; col++){
                if (gameField[row][col] == gameField[row+1][col]){
                    return true;
                }
            }
        }
        return false;
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.YELLOW, "You've lost...", Color.RED, 28);
    }
    
    
    
}























