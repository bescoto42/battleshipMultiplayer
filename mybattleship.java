package main;
import java.io.*;
import java.util.*;

public class mybattleship {

  static int[][] board = new int[8][8];

  public static void main(String[] args) throws Exception {
    menu();
  }

  private static void menu() throws Exception {
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Welcome to Battleship!");
    System.out.println("Enter 1 for single player or 2 for multiplayer.");
    String userIn = inFromUser.readLine();
    if (userIn == "1") {
      singlePlayer();
    }
    if (userIn == "2") {
      multiplayer();
    }
    else {
      while (userIn != "1" || userIn != "2") {
        System.out.println("Enter 1 for single player or 2 for multiplayer.");
        String input = inFromUser.readLine();
      }
    }
  }

  private static void singlePlayer() {
    //single player game against computer
  }

  private static void multiplayer() {
    initializeBoard(board);

  }

  private static void initializeBoard(int[][] board) {
    for (int row = 0; row < 8; row ++ ) {
      for (int col = 0; col < 8; col ++) {
        board[row][col] = -1;
      }
    }
  }

  private static void showBoard(int[][] board) {
    System.out.println("\t0 \t1 \t2 \t3 \t4 \t5 \t6 \t7");
    for (int row = 0; row < 8; row ++) {
      //System.out.print((row) + "");
      for (int col = 0; col < 8; col ++) {
        System.out.println("\tX");
      }
      System.out.println();
    }
  }

  private static boolean newShip(int X, int Y, int  size, int shipType) {
    if((Y+size)<8){
      for(int i = 0; i < size; i++){
        if(board[X][X+i] != -1){
          return false;
        }
      }
    for(int i = 0; i < tamasizenho; i++){
      board[X][Y+i] = shipType;
    }
    }
    else {
      return false;
    }

}

private static void setRandomSubmarino(){
  Random randomize = new Random();
  boolean tryAgain = false;
  while(!tryAgain){
     int x = randomize.nextInt(8);
         int y = randomize.nextInt(8);
         boolean z = randomize.nextBoolean();
         tryAgain = setNewShip(x, y, 1, 1);
  }
}

private static void setRandomCruzador(){
  Random randomize = new Random();
  boolean tryAgain = false;
  while(!tryAgain){
     int x = randomize.nextInt(8);
         int y = randomize.nextInt(8);
         boolean z = randomize.nextBoolean();
         tryAgain = setNewShip(x, y, 2, 2);
  }
}

private static void setRandomPortaAvioes(){
  Random randomize = new Random();
  boolean tryAgain = false;
  while(!tryAgain){
     int x = randomize.nextInt(8);
         int y = randomize.nextInt(8);
         boolean z = randomize.nextBoolean();
         tryAgain = setNewShip(x, y, 3, 3);
  }
}

private static boolean gameOver(){
  for(int i=0;i < 8;i++){
    for(int j=0;j < 8;j++){
      if(board[i][j] == 1 || board[i][j] == 2 || board[i][j] == 3){
        return false;
      }
    }
  }
return true;
}
