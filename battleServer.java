/**
server class connects two players and then launches threads with game class
game class will handle all the gameplay
game class recieves the player's input and then outputs it to both players
currently, the biggest issue is that users can go more than once however that should be fixed when gameplay is implemented
**/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

public class battleServer{
  private Socket sock1 = null;
  private Socket sock2 = null;

  //constructor
  public battleServer()
  {

  }

  private void connectPlayers()
  {
    //wait for request from client
    try
    {
      System.out.println("Waiting on port 6969");
      ServerSocket ss = new ServerSocket(6969);

      //allow two players to connect
      sock1 = ss.accept();
      sock2 = ss.accept();

      //game class takes user's socket, enemy's socket, and designation as input
      Game handler1 = new Game(sock1, sock2,"1");
      Thread a = new Thread(handler1);
      a.start();

      Game handler2 = new Game(sock2, sock1,"2");
      Thread b = new Thread(handler2);
      b.start();

      //once threads are done, close the connection
      ss.close();
    }
    catch(IOException e)
    {
      System.out.println("Bye");
    }

  }

  public static void main(String[] args)
  {
    //all main does is launch the server
    battleServer k = new battleServer();
    k.connectPlayers();
  }

}

class Game implements Runnable {
  private Socket self = null;
  private Socket enemy = null;
  private String user,nemesis;
  String[] eships = new String[17];

  Game(Socket s, Socket e, String w)
  {
    self = s;
    enemy = e;
    user = w;

    if(user=="1")
    {
      nemesis="2";
    }
    else if(user=="2")
    {
      nemesis="1";
    }
    else
    {
      System.out.println("Error!");
    }
  }

  public void run()
  {
    try
    {
      System.out.println("Player " + user + " has connected.");

      //get the input from the user
      BufferedReader myInput = new BufferedReader(new InputStreamReader(self.getInputStream()));
      //send input to enemy
      DataOutputStream enemyOutput = new DataOutputStream(enemy.getOutputStream());
      //send input back to the user
      DataOutputStream myOutput = new DataOutputStream(self.getOutputStream());
      //user's input
      String x;
      int index=0;

      myOutput.writeBytes(user + "\n");
      //welcome message
      myOutput.writeBytes("Welcome to Battleship, player " + user + ".\nType ! to end session.\n");

      while(true)
      {
        x = myInput.readLine();
        if(x != null)
        {
          if(x.equals("!"))
          {
            enemy.close();
            self.close();
            break;
          }

          //send the same message to both players
          myOutput.writeBytes("| " + user + ": " + x + "\n");
          enemyOutput.writeBytes("| " + user + ": " + x + "\n");



        }
        else
        {
          System.out.println("Exiting.");
        }
      }
    }
    catch(Exception e)
    {
      //once one user disconnects, server closes, disconnecting the other player
      System.out.println("Player " + user + " disconnected.");
      System.exit(0);

    }
  }



}
