/** handles output to client
**/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.ArrayList;
import java.util.Scanner;

public class battleCHandler implements Runnable {
  private Socket cs = null;
  private ArrayList<Socket> players;

  battleCHandler(Socket s, ArrayList<Socket> p)
  {
    players = p;
    cs = s;
  }

  public void run()
  {
    try
    {
      System.out.println("Player has connected.");
      BufferedReader clientInput = new BufferedReader(new InputStreamReader(cs.getInputStream()));
      String input;
      while(true)
      {
        System.out.println("--");
        input = clientInput.readLine();

        if(input != null)
        {
          System.out.println("Shooting " + input);
          for (Socket temp : players)
          {
            if (temp != cs)
            {
              DataOutputStream send =  new DataOutputStream(temp.getOutputStream());
              send.writeBytes(input + "\n");
            }
          }
        }
        else
        {
          System.out.println("Error.");
          players.remove(cs);
          cs.close();
        }
      }
    }
    catch(Exception e)
    {
      players.remove(cs);
    }
  }


}









//
