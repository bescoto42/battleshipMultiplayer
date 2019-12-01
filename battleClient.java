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
        int index=0;
        String hostname = "localhost";
        int port = 6969;

        System.out.println("\n*--*\nStarting game.");
        Socket cs = new Socket(hostname, port);

        //used to send data from keyboard to server
        DataOutputStream sendOut = new DataOutputStream(cs.getOutputStream());

        System.out.println("Let's go ladies.");
        int temp = 0;//find some way to signal that ships are ready to send

        Board t1 = new Board();
        t1.setShips();
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
          if(data.charAt(0)=='!')
          {
            sendOut.writeBytes("!");
            System.exit(0);
          }

          if(data.equals("send ships"))
          {
            for(int i=0;i<17;i++)
            {
              //System.out.println(t1.giveShips(i) + "\n");
              sendOut.writeBytes(t1.giveShips(i) + "\n");

            }
          }
          else
          {
            sendOut.writeBytes(data + "\n");
          }

        }
      }
      catch (IOException e)
      {
        System.out.println("client line 80");
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

  int uShips[] = new int[17];
  int eShips[] = new int[17];
  String player, enemy;
  int eShipsindx;

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

      uScreen[i].setVisible(false);
      eScreen[i].setVisible(false);

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

    //will help when checking for duplicates
    for(int i=0;i<17;i++)
    {
      uShips[i] = -5;
      eShips[i] = -5;
    }

    newLine= true;
    eShipsindx=0;

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

    // int val = transform(s.substring(3));
    // System.out.println("val :" + val);
  }

  public int transform(String f)
  {
    f = f.toUpperCase();
    System.out.println("f: " + f);
    int a = 0;
    if(f.charAt(0)=='B')//starts at B because A column is zero
    {
      a = a + 1;
    }
    else if(f.charAt(0)=='C')
    {
      a = a + 2;
    }
    else if(f.charAt(0)=='D')
    {
      a = a + 3;
    }
    else if(f.charAt(0)=='E')
    {
      a = a + 4;
    }
    else if(f.charAt(0)=='F')
    {
      a = a + 5;
    }
    else if(f.charAt(0)=='G')
    {
      a = a + 6;
    }
    else if(f.charAt(0)=='H')
    {
      a = a + 7;
    }
    else if(f.charAt(0)=='I')
    {
      a = a + 8;
    }
    else if(f.charAt(0)=='J')
    {
      a = a + 9;
    }
    else if(f.charAt(0)!='A')
    {
      return -1;
    }

    int b;

    if(f.length()==3)
    {
      b = 9;
    }
    else
    {
      b = Character.getNumericValue(f.charAt(1));
      b = b - 1;
    }
    b = b * 10;

    return b + a;
  }

  public Boolean shipsDuplicate(int a)
  {
    for(int i=0;i<17;i++)
    {
      if(uShips[i]==-5)
      {
        return false;
      }
      if(a==uShips[i])
      {
        return true;
      }

    }
    return false;
  }

  //returns true if the values are not consecutive
  public Boolean failedChecksum(int a, int b)
  {
    //checksum has to be one value off the first value for horizontal alignment
    //and ten off for vertical alignment

    int c = a + 1; //horizontal check
    if(c!=b)
    {
      c = a - 1;
      if(c!=b)
      {
        //vertical check
        c = a + 10;
        if(c != b)
        {
          c = a - 10;
          if(c != b)
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  public void setPlayer(String p)
  {
    player = p;
    if(p.equals("1"))
    {
      enemy="2";

    }
    else
    {
      enemy="1";
    }
    setMessage("player: " + player + " enemy: " + enemy);

  }

  public void setShips()
  {
    // /**
    // steps for placing ships
    //   1. check that values are valid coordinates (transform function)
    //   2. check that values do not overlap (shipsDuplicate function)
    //   3. check that values are consecutive (failedChecksum function)
    //   4. place ships in user ships array
    //   5. set next text box as visible
    //   6. show user what coordinates have already been inputted
    //   7. when everything is set, place ships on board
    //   **/
    // Scanner keyboard = new Scanner(System.in);
    // String data,s1,s2,s3,s4,s5;
    // int indx = 0;//not a for loop bc we have to account for errors which might cause more loops
    // int length,i1,i2,i3,i4,i5;
    // while(true)
    // {
    //   if(indx>16)
    //   {
    //     break;
    //   }
    //   switch(indx)
    //   {
    //     case 0:
    //       System.out.println("Enter comma separated coordinates for a ship of size two:\nEx: F4,F5\n— ");
    //       data = keyboard.nextLine();
    //       length=data.length();
    //       if(data.charAt(2)==',')
    //       {
    //         s1=data.substring(0,1);
    //         s2=data.substring(3,length-1);
    //       }
    //       else if(data.charAt(3)==',')
    //       {
    //         s1=data.substring(0,2);
    //         s2=data.substring(4,length-1);
    //       }
    //       i1 = transform(s1);
    //       i2 = transform(s2);
    //
    //       if(i1 < 0 || i2 < 0 ||failedChecksum(i1,i2)) //not continuous
    //       {
    //         System.out.println("Incorrect input.");
    //         break;
    //       }
    //       uShips[indx] = i1;
    //       indx++;
    //       uShips[indx] = i2;
    //       indx++;
    //   case 2:
    //     System.out.println("Enter comma separated coordinates for a ship of size three:\nEx: D10,E10,F10\n— ");
    //
    //
    //   }
    //   System.out.println("Place a ship one coordinate at a time, ex B6:");
    //   data = keyboard.nextLine(); //A1
    //   val = transform(data);
    //   if(val < 0)
    //   {
    //     System.out.println("Incorrect coordinate!");
    //   }
    //   else
    //   {
    //     uShips[indx] = val;
    //     indx++;
    //
    //   }
    // }
    //
    // // for(17)
    // // {
    // //   set uships
    // // }

    //temporarily, we're going to assign ships to test function
    uShips[0]=0;
    uShips[1]=10;
    uShips[2]=1;
    uShips[3]=11;
    uShips[4]=21;
    uShips[5]=42;
    uShips[6]=43;
    uShips[7]=44;
    uShips[8]=93;
    uShips[9]=94;
    uShips[10]=95;
    uShips[11]=96;
    uShips[12]=48;
    uShips[13]=58;
    uShips[14]=68;
    uShips[15]=78;
    uShips[16]=88;

    hit = new ImageIcon("sad.png");

    int j;
    for(int i=0;i<17;i++)
    {
      j=uShips[i];
      uScreen[j].setIcon(hit);
      uScreen[j].setVisible(true);
    }
    // System.out.println("user ships set");

  }


  //called by sender/main client class
  public String giveShips(int indx)
  {
    String ucoor = player + "S" + uShips[indx];
    return ucoor;
  }

  //called by listener
  public void setEnemyShips(String eship)
  {
    String temp = eship.substring(5);

    if(temp.startsWith(player + "S"))
    {
      // System.out.println("my ship");
      return;
    }
    temp = temp.substring(2);
    // System.out.println(temp);

    int value = Integer.parseInt(temp);

    eScreen[value].setIcon(hit);
    eScreen[value].setVisible(true);
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
      boolean noShips=true;
      boolean playerset=false;
      BufferedReader fromServer = new BufferedReader(new InputStreamReader(cs.getInputStream()));

      while(true)
      {
        String serverText = fromServer.readLine();
        if (fromServer != null)
        {
          //first message the server sends is just the player assignment: "1"/"2"
          if(playerset==false)
          {
            b.setPlayer(serverText.substring(0,1));
            playerset=true;
            continue;
          }

          if(serverText.startsWith("| 1: 1S")||serverText.startsWith("| 2: 2S"))
          {
            //System.out.println("recieved: " + serverText);
            b.setEnemyShips(serverText);
            continue;
          }


          b.setMessage(serverText);
          System.out.println(serverText);

        }
        else
        {
          System.out.println("got null");
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
