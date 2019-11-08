/**
handles connections from clients
- uses client handler to recieve messages from the client
- uses threads to run clients
- to test start server first then run the clients
**/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

public class battleServer{
  private ArrayList<Socket> players;

  //constructor
  public battleServer()
  {
    players = new ArrayList<Socket>();
  }

  private void connectPlayers()
  {
    //wait for request from client
    try
    {
      System.out.println("Waiting on port 6969");
      ServerSocket ss = new ServerSocket(6969);

      //instead of infintely waiting, we only want two players to be able to connect
      Socket connectionSock1 = ss.accept();
      players.add(connectionSock1);
      battleCHandler handler1 = new battleCHandler(connectionSock1, players);
      Thread a = new Thread(handler1);
      a.start();

      Socket connectionSock2 = ss.accept();
      players.add(connectionSock2);
      battleCHandler handler2 = new battleCHandler(connectionSock2, players);
      Thread b = new Thread(handler2);
      b.start();

      ss.close();
    }
    catch(IOException e)
    {
      System.out.println("Bye");
    }

  }

  public static void main(String[] args)
  {
    battleServer k = new battleServer();
    k.connectPlayers();
  }

}












//
