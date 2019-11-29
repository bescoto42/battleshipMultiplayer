//https://cs.lmu.edu/~ray/notes/javanetexamples/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;

import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

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


        Board t1 = new Board();
        Listener l = new Listener(cs,t1);
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


class Board
{
  //variables from GUI
  JFrame f;//the window
  JLabel bg,instructions;
  JTextArea errormsg;

  ImageIcon miss;//no ship was hit
  ImageIcon hit; //a ship was hit

  //user and enemy boards
  JLabel uScreen[] = new JLabel[100];
  JLabel eScreen[] = new JLabel[100];

  //title image
  ImageIcon t;
  JLabel title;

  int x, y;
  Boolean newLine;

  Board()
  {
    f=new JFrame("Battleship");
    bg = new JLabel(new ImageIcon("board.png"));
    bg.setVisible(true);//set false

    t = new ImageIcon("title.png");
    title = new JLabel(t);
    title.setBounds(0,0,1090,60);
    f.add(title);

    errormsg = new JTextArea("Welcome to the game.",3,20);
    errormsg.setBounds(30,600,500,100);
    errormsg.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    errormsg.setEditable(false);
    errormsg.setVisible(true);
    f.add(errormsg);

    //for loop to set explosions up
    miss = new ImageIcon("lol.png");
    hit = new ImageIcon("sad.png");

    x = 35; //x coordinate of first box
    y = 95; //y coordinate of first box
    int z = 40; //size of image
    int q=0;
    for(int i = 0;i<100;i++)
    {

      uScreen[i] = new JLabel(miss);
      eScreen[i] = new JLabel(miss);

      uScreen[i].setBounds(x,y,z,z);
      eScreen[i].setBounds(x+515,y,z,z);

      f.add(uScreen[i]);
      f.add(eScreen[i]);

      uScreen[i].setVisible(true);
      eScreen[i].setVisible(true);

      x = x + 51;
      q++;

      if(q==10) // new row
      {
        //System.out.print(i);
        x = 35;
        y = y + 50;
        q = 0;
      }
    }

    newLine= true;

    f.add(bg);
    f.setSize(1100,700);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


  }

  public void setMessage(String s)
  {

    if(newLine)
    {
      String temp = errormsg.getText();
      errormsg.setText(temp + "\n" + s);
      newLine=false;
    }
    else
    {
      errormsg.setText(s);
      newLine= true;
    }
  }



}


class Listener implements Runnable {
  private Socket cs = null;
  Board b;

  Listener(Socket q, Board t)
  {
    cs = q;
    b = t;
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
          b.setMessage(serverText);
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
