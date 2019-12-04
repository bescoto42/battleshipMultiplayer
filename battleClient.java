/**
client accepts the connection first
then calls for a function to set up player ships. after that
a while loop takes all input after that until the user quits.
**/

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

        Board t1 = new Board();
        t1.setShips();
        Listener l = new Listener(cs,t1);
        Thread t = new Thread(l);
        t.start();
        String data;

        Scanner keyboard = new Scanner(System.in);
        // get input from user and send it out
        // some basic error checking is implemented

        while(true)
        {
          data = keyboard.nextLine();
          if(t1.getVictory())
          {
            System.out.println("winner");
            sendOut.writeBytes("VICTORY!" + "\n");
          }


          if(data.toUpperCase().contains("TRY"))
          {
            if(t1.startGame()==false)
            {
              System.out.println("Wait!");
              continue;
            }
            if(t1.isYourTurn())
            {
              int check;
              try
              {
                check = t1.transform(data.substring(4));
              }
              catch(StringIndexOutOfBoundsException e)
              {
                System.out.println("Error!");
                continue;
              }

              if(check < 0)
              {
                System.out.println("Invalid coordinate!");
                continue;
              }
            }
            else
            {
              System.out.println("Wait your turn!");
              continue;
            }
          }
          //exit condition
          if(data.charAt(0)=='!')
          {
            sendOut.writeBytes("!");
            System.exit(0);
          }
          if(data.equals("send"))
          {
            for(int i=0;i<17;i++)
            {
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
        System.out.println(e.getMessage());
      }
    }
}


class Board
{
  //variables for GUI
  JFrame f;//the window
  JLabel bg; //the background
  JTextArea errormsg,instructions;

  //icons
  ImageIcon miss,hit,ship;

  //user and enemy boards
  JLabel uScreen[] = new JLabel[100];
  JLabel eScreen[] = new JLabel[100];

  //title image
  ImageIcon t;
  JLabel title;

  ImageIcon finished;
  JLabel gameDone;

  //player 1/2 icons
  ImageIcon p1,p2;
  JLabel pIcon;

  //x & y are used to have the offset be the same for objects
  int x, y;
  //used to write two lines in text area
  Boolean newLine;

  //coordinates for user and enemy ships
  int uShips[] = new int[17];
  int eShips[] = new int[17];

  // player 1/2 assignments
  String player, enemy;

  // function is called multiple times so index is called outside of it
  int eShipsindx;

  //to keep input organized
  Boolean yourTurn;

  //to check if game is over
  int hitCount;

  Board()
  {
    f=new JFrame("Battleship");
    bg = new JLabel(new ImageIcon("board.png"));
    bg.setVisible(true);

    instructions = new JTextArea("COMMANDS:\n| ! - exit game\n| try b10 - shoot");
    instructions.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    instructions.setBounds(850,600,300,100);
    instructions.setEditable(false);
    f.add(instructions);

    t = new ImageIcon("title.png");
    title = new JLabel(t);
    title.setBounds(0,0,1090,60);
    f.add(title);

    p1 = new ImageIcon("player1.png");
    pIcon = new JLabel(p1);
    // pIcon.setBounds(850,600,300,50);
    pIcon.setBounds(30,600,300,50);
    pIcon.setVisible(false);
    f.add(pIcon);

    finished = new ImageIcon("gameOver.png");
    gameDone = new JLabel(finished);
    gameDone.setBounds(300,200,500,300);
    gameDone.setVisible(false);
    f.add(gameDone);

    errormsg = new JTextArea("Welcome to the game.",3,20);
    // errormsg.setBounds(30,600,500,100);
    errormsg.setBounds(350,600,500,100);
    errormsg.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
    errormsg.setEditable(false);
    errormsg.setVisible(true);
    f.add(errormsg);

    //for loop sets icons
    miss = new ImageIcon("miss.png");
    hit = new ImageIcon("hit.png");
    ship = new ImageIcon("ship.png");

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
    yourTurn = false;
    hitCount = 0;

    f.add(bg);
    f.setSize(1100,700);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


  }

  public Boolean startGame()
  {
    if(eShips[16]==-5)
    {
      return false;
    }
    return true;
  }

  public void gameOver()
  {
    gameDone.setVisible(true);
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
    int a;

    s = s.toUpperCase();
    if(s.contains("TRY"))
    {
      a = s.indexOf('Y') + 2;
      a = transform(s.substring(a));
    }
    else
    {
      return;
    }
    if(s.contains(enemy + ":"))
    {
      yourTurn = true;
      for(int i = 0; i< 17;i++)
      {
        if(a==uShips[i])
        {
          uScreen[a].setIcon(hit);
        }
      }
      uScreen[a].setVisible(true);
    }
    else if(s.contains(player + ":"))
    {
      yourTurn = false;
      for(int i = 0; i < 17; i++)
      {
        if(a==eShips[i])
        {
          System.out.println("hit!");
          hitCount++;
        }
      }
      eScreen[a].setVisible(true);
    }
  }

  public boolean getVictory()
  {
    return hitCount>=17;
  }

  public int transform(String f)
  {
    f = f.toUpperCase();

    if(f.length() > 3)
    {
      return -1;
    }

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
      if(f.charAt(2)!='0')
      {
        return -1;
      }
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

public Boolean isYourTurn()
  {
    return yourTurn;
  }

public void setPlayer(String p)
  {
    player = p;
    if(p.equals("1"))
    {
      enemy="2";
      yourTurn=true;

    }
    else
    {
      enemy="1";
      p2 = new ImageIcon("player2.png");
      pIcon.setIcon(p2);
    }
    pIcon.setVisible(true);
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
    Scanner keyboard = new Scanner(System.in);
    String prompt[] = {"two","three","four","five"};

    Boolean roundone = true;
    String data;
    String s1 = "";
    String s2 = "";
    String s3 = "";
    String s4 = "";
    String s5 = "";
    String t1 = "";
    String t2 = "";
    String t3 = "";
    // String t4 = "";
    int v1 = 0;
    int v2 = 0;
    int v3 = 0;
    int v4 = 0;
    int v5 = 0;

    int index = 0;
    int temp;

    System.out.println("Ex: a5,a6,a7");

    while(index < 4)
    {
      System.out.println("Place a ship of size " + prompt[index]);
      data=keyboard.nextLine();

      switch (index)
      {
        case 0:
          try
          {
            s1 = data.substring(0,data.indexOf(','));
            s2 = data.substring(data.indexOf(',')+1);
          }
          catch(StringIndexOutOfBoundsException s)
          {
            System.out.println("Bad input!");
            break;
          }
          v1 = transform(s1);
          v2 = transform(s2);
          if(v1 < 0 || v2 < 0 || failedChecksum(v1,v2))
          {
            System.out.println("Invalid coordinates!!");
            break;
          }
          index++;
          System.out.println("\t\t| " + s1.toUpperCase() + "," + s2.toUpperCase() + " |");
          uShips[0] = v1;
          uShips[1] = v2;
          break;

        case 1:
          try
        {
          temp = data.indexOf(',');
          s1 = data.substring(0,temp);
          t1 = data.substring(temp+1);
          temp = t1.indexOf(',');
          s2 = t1.substring(0,temp);
          s3 = t1.substring(temp+1);
        }
        catch(StringIndexOutOfBoundsException s)
        {
          System.out.println("Bad input!");
          break;
        }
          v1 = transform(s1);
          v2 = transform(s2);
          v3 = transform(s3);

          if(v1 < 0 || v2 < 0 || failedChecksum(v1,v2) || failedChecksum(v2,v3))
          {
            System.out.println("Invalid coordinates!");
            break;
          }
          else if(shipsDuplicate(v1)||shipsDuplicate(v2)||shipsDuplicate(v3))
          {
            System.out.println("Error - overlapping ships");
            break;
          }
          if(roundone)
          {
            uShips[2] = v1;
            uShips[3] = v2;
            uShips[4] = v3;
            roundone = false;
          }
          else
          {
            uShips[5] = v1;
            uShips[6] = v2;
            uShips[7] = v3;
            index++;
          }
          System.out.println("\t\t| " + s1.toUpperCase() + "," + s2.toUpperCase() +  "," + s3.toUpperCase() + " |");
          break;
        case 2:
          try
          {
            temp = data.indexOf(',');
            s1 = data.substring(0,temp);
            t1 = data.substring(temp+1);
            temp = t1.indexOf(',');
            s2 = t1.substring(0,temp);
            t2 = t1.substring(temp+1);
            temp = t2.indexOf(',');
            s3 = t2.substring(0,temp);
            s4 = t2.substring(temp+1);
          }
          catch(StringIndexOutOfBoundsException s)
          {
            System.out.println("Bad input!");
            break;
          }

          v1 = transform(s1);
          v2 = transform(s2);
          v3 = transform(s3);
          v4 = transform(s4);

          if(v1 < 0 || v2 < 0 || v3 < 0 || v4 < 0)
          {
            System.out.println("Incorrect coordinates!");
            break;
          }
          else if(failedChecksum(v1,v2)||failedChecksum(v2,v3)||failedChecksum(v3,v4))
          {
            System.out.println("Incorrect coordinates!!");
            break;
          }
          else if(shipsDuplicate(v1)||shipsDuplicate(v2)||shipsDuplicate(v3)||shipsDuplicate(v4))
          {
            System.out.println("error - overlapping ships");
            break;
          }

          uShips[8] = v1;
          uShips[9] = v2;
          uShips[10] = v3;
          uShips[11] = v4;
          index++;
          System.out.print("\t\t| " + s1.toUpperCase() + "," + s2.toUpperCase() +  "," + s3.toUpperCase() + ",");
          System.out.println(s4.toUpperCase() + " |");
          break;

        case 3:
          try
          {
            temp = data.indexOf(',');
            s1 = data.substring(0,temp);
            t1 = data.substring(temp+1);
            temp = t1.indexOf(',');
            s2 = t1.substring(0,temp);
            t2 = t1.substring(temp+1);
            temp = t2.indexOf(',');
            s3 = t2.substring(0,temp);
            t3 = t2.substring(temp+1);
            temp = t3.indexOf(',');
            s4 = t3.substring(0,temp);
            s5 = t3.substring(temp+1);
          }
          catch(StringIndexOutOfBoundsException s)
          {
            System.out.println("Bad input!");
            break;
          }

          v1 = transform(s1);
          v2 = transform(s2);
          v3 = transform(s3);
          v4 = transform(s4);
          v5 = transform(s5);

          if(v1 < 0 || v2 < 0 || v3 < 0 || v4 < 0 || v5 < 0)
          {
            System.out.println("Incorrect coordinates!");
            break;
          }
          else if(failedChecksum(v1,v2)||failedChecksum(v2,v3)||failedChecksum(v3,v4)||failedChecksum(v4,v5))
          {
            System.out.println("Incorrect coordinates!");
            break;
          }
          else if(shipsDuplicate(v1)||shipsDuplicate(v2)||shipsDuplicate(v3)||shipsDuplicate(v4)||shipsDuplicate(v5))
          {
            System.out.println("error - overlapping ships");
            break;
          }

          uShips[12] = v1;
          uShips[13] = v2;
          uShips[14] = v3;
          uShips[15] = v4;
          uShips[16] = v5;
          System.out.print("\t\t| " + s1.toUpperCase() + "," + s2.toUpperCase() +  "," + s3.toUpperCase() + ",");
          System.out.println(s4.toUpperCase() + "," + s5.toUpperCase() + " |");
          index++;


        }
    //temporarily, we're going to assign ships to test function
    // uShips[0]=0;
    // uShips[1]=10;
    // uShips[2]=1;
    // uShips[3]=11;
    // uShips[4]=21;
    // uShips[5]=42;
    // uShips[6]=43;
    // uShips[7]=44;
    // uShips[8]=93;
    // uShips[9]=94;
    // uShips[10]=95;
    // uShips[11]=96;
    // uShips[12]=48;
    // uShips[13]=58;
    // uShips[14]=68;
    // uShips[15]=78;
    // uShips[16]=88;

  }
  int j;
  for(int i=0;i<17;i++)
  {
    j=uShips[i];
    uScreen[j].setIcon(ship);
    uScreen[j].setVisible(true);
  }
  System.out.println("Enter 'send' to start game.");
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

      return;
    }
    temp = temp.substring(2);


    int value = Integer.parseInt(temp);

    eShips[eShipsindx]=value;
    eShipsindx++;
    eScreen[value].setIcon(hit);
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

          if(serverText.contains("VICTORY!"))
          {
            b.setMessage("Restart program to play again");
            b.gameOver();
            continue;
          }

          //first message the server sends is just the player assignment: "1"/"2"
          if(playerset==false)
          {
            b.setPlayer(serverText.substring(0,1));
            playerset=true;
            continue;
          }

          if(serverText.startsWith("| 1: 1S")||serverText.startsWith("| 2: 2S"))
          {
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
