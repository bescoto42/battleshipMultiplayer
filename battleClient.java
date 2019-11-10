//https://cs.lmu.edu/~ray/notes/javanetexamples/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.Scanner;


public class battleClient {

    public static void main(String[] args) {
      try
      {
        String hostname = "localhost";
        int port = 6969;

        System.out.println("\n*--*\nStarting game.");
        Socket cs = new Socket(hostname, port);

        //used to send data from keyboard to server
        DataOutputStream sendOut = new DataOutputStream(cs.getOutputStream());

        System.out.println("Let's go ladies.");

        Listener l = new Listener(cs);
        Thread t = new Thread(l);
        t.start();
        String data;

        Scanner keyboard = new Scanner(System.in);
        //get input from user and send it out
        while(true)
        {
          data = keyboard.nextLine();
          //exit condition
          if(data.charAt(0)=='e')
          {
            sendOut.writeBytes("0");
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

class Listener implements Runnable {
  private Socket cs = null;

  Listener(Socket q)
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
          System.out.println(serverText);
          
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
      System.out.println("Connection terminated.");
      System.exit(0);
    }
  }

}
