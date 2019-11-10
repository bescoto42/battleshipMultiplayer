import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.Scanner;
import java.util.ArrayList;

public class battleClient {

    public static void main(String[] args) {
      try
      {
        String hostname = "localhost";
        int port = 6969;

        System.out.println("Starting game.");
        Socket cs = new Socket(hostname, port);

        DataOutputStream sendOut = new DataOutputStream(cs.getOutputStream());

        //show data sent by the server
        System.out.println("Let's go ladies.");
        System.out.println("Type exit to end session.");
        battleCListener l = new battleCListener(cs);
        Thread t = new Thread(l);
        t.start();

        Scanner keyboard = new Scanner(System.in);
        while(true)
        {
          String data = keyboard.nextLine();
          if(data.charAt(0)=='e')
          {
            System.exit(0);
          }
          sendOut.writeBytes(data + "\n");

        }
      }
      catch (IOException e)
      {
        System.out.println(e.getMessage());
      }
    }
}

class battleCListener implements Runnable {
  private Socket cs = null;

  battleCListener(Socket q)
  {
    cs = q;
  }

  public void run()
  {
    try
    {
      BufferedReader fromServer = new BufferedReader(new InputStreamReader(cs.getInputStream()));

      while(true)
      {
        String serverText = fromServer.readLine();
        if (fromServer != null)
        {
          System.out.println("Enemy shot: " + serverText);
        }
        else
        {
          cs.close();
          break;
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("Whoopsies.");
    }
  }

}
