
package cellautomata;

import java.io.*;
//import java.util.Scanner;
import java.awt.*; //needed for graphics
import javax.swing.*; //needed for graphics
import static javax.swing.JFrame.EXIT_ON_CLOSE; //needed for graphics
import java.util.Random;
public class CellAutomata extends JFrame {

    //FIELDS
    int numGenerations = 100;
    int currGeneration = 1;
    
    Color likeColor = Color.blue;
    Color hateColor = Color.red;
    
    //String fileName = "Initial cells.txt";

    int width = 800; //width of the window in pixels
    int height = 800;
    int borderWidth = 50;

    int numCellsX = 30; //width of the grid (in cells)
    int numCellsY = 30;
    
    boolean like[][] = new boolean[numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION 
    boolean likeNext[][] = new boolean[numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION
    
    
    int cellWidth = (width - 2*borderWidth)/numCellsX; //replace with the correct formula that uses width, borderWidth and numCellsX
    
    int labelX = width / 2;
    int labelY = borderWidth;
 
    
    //METHODS
    public void plantFirstGeneration() throws IOException {
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                if(j%3==0){
                    like[i][j] = true;
                }
                if(i%4==0){
                    like[i][j] = true;
                }
            }
        }
    }

    public void plantBlock(int startX, int startY, int numColumns, int numRows) {
        
        int endCol = Math.min(startX + numColumns, numCellsX);
        int endRow = Math.min(startY + numRows, numCellsY);

        for (int i = startX; i < endCol; i++) {
            for (int j = startY; j < endRow; j++) {
                like[i][j] = true;
            }
        }
    }
    public void computeNextGeneration() {
        for(int i = 0; i < likeNext.length; i ++){
            for(int j = 0; j < likeNext[0].length; j ++){
                //get number of living cells
                int liked = countLivingNeighbors(i,j);
                if(liked > 6){
                    likeNext[i][j] = false;
                }
                else if(liked > 2){
                    likeNext[i][j] = true;
                }
                else if(liked < 4){
                    likeNext[i][j] = false;
                    
                }
                else{
                    likeNext[i][j] = like[i][j];
                }
            }
        }
    }
   
    //Overwrites the current generation's 2-D array with the values from the next generation's 2-D array
    public void plantNextGeneration() {
        Random rand = new Random();
        for(int i = 0; i < like.length; i ++){
            for(int j = 0; j < like[0].length; j ++){
                if(rand.nextInt() % 400 == 0){
                    like[i][j] = !likeNext[i][j];
                }
                else{
                    like[i][j] = likeNext[i][j];
                }
            }
        }
        currGeneration ++;
    }
    
    //Counts the number of living cells adjacent to cell (i, j)
    public int countLivingNeighbors(int i, int j) {
        int liked = 0;
        int startrow = i - 1;
        int endrow = i + 1;
        int startcol = j - 1;
        int endcol = j + 1;
        //if top row
        if(i == 0){
            startrow = i;
        }
        //if bottom row
        if(i == numCellsX - 1){
            endrow = i;
        }
        //if leftmost column
        if(j == 0){
            startcol = j;
        }
        //if rightmost column
        if(j == numCellsY - 1){
            endcol = j;
        }
        //counts living cells surrounding middle cell, including middle cell
        for(int m = startrow; m <= endrow; m ++){
            for(int n = startcol; n <= endcol; n++){
                if(like[m][n]){
                        liked ++;
                }
            }
        }
        //removes middle cell from count
        if(like[i][j]){
            liked --;
        }
        return liked; //make it return the right thing
    }
 
    //Makes the pause between generations
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }
    
    //Displays the statistics at the top of the screen
    void drawLabel(Graphics g, int state) {
        g.setColor(Color.black);
        g.fillRect(0, 0, width, borderWidth);
        g.setColor(Color.yellow);
        g.drawString("Generation: " + state, labelX, labelY);
    }
    
    //Draws the current generation of living cells on the screen
    public void paint( Graphics g ) {
        int x, y, i, j;
        
        //start with first row
        y = borderWidth;
        
        //set background to black for first generation
        if(currGeneration == 1 ){
            g.setColor(Color.black); 
            g.fillRect(0, 0, width, height);
         
        }
        
        drawLabel(g, currGeneration);
        
        for (i = 0; i < numCellsX; i++) {
            //start at beginning of row
            x = borderWidth;
            for (j = 0; j < numCellsY; j++) {
                if(like[i][j]){
                    g.setColor(likeColor);
                }
                else{
                    g.setColor(hateColor);
                }
                //fill cell with colour
                g.fillRect(x, y, cellWidth, cellWidth);
                //white gridlines
                g.setColor(Color.white);
                g.drawRect(x , y,  cellWidth, cellWidth);
                //next cell in row
                x += cellWidth;
            }
            //next row
            y += cellWidth;
        }
    }

    //Sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Game of Life Simulator");
        setSize(height, width);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);//calls paint() for the first time
    }
    
    //Main algorithm
    public static void main(String args[]) throws IOException {

        CellAutomata currGame = new CellAutomata();

        currGame.initializeWindow();
        //Sets the initial generation of living cells, either by reading from a 
        //file or creating them algorithmically
        currGame.plantFirstGeneration(); 
        for (int i = 0; i < currGame.numGenerations; i++) {
            currGame.repaint();
            currGame.sleep(150);
            currGame.computeNextGeneration();
            currGame.plantNextGeneration();
        }
    } 
} //end of class
