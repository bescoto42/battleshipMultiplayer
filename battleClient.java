//used to run the client

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

        System.out.println("Starting game.");
        Socket cs = new Socket(hostname, port);

        DataOutputStream sendOut = new DataOutputStream(cs.getOutputStream());

        //show data sent by the server
        System.out.println("Let's go ladies.");
        battleCListener l = new battleCListener(cs);
        Thread t = new Thread(l);
        t.start();

        Scanner keyboard = new Scanner(System.in);
        while(true)
        {
          String data = keyboard.nextLine();
          sendOut.writeBytes(data + "\n");

        }
      }
      catch (IOException e)
      {
        System.out.println(e.getMessage());
      }
    }
}
