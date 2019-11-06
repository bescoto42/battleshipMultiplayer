import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.ArrayList;
import java.util.Scanner;

public class battleCListener implements Runnable {
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
