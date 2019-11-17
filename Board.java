import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class Board extends JFrame implements ActionListener
{
  JTextField userField, enemyField,errormsg;//display text
  JButton sendCoor;//to confirm sending coordinates
  JFrame f;//the window
  JLabel jex;//the object that holds the image
  ImageIcon iex;//the actual image
  //user and enemy boards
  JLabel uScreen[] = new JLabel[100];
  JLabel eScreen[] = new JLabel[100];

  Board()
  {
    f=new JFrame();

    //setBounds(int x-coordinate, int y-coordinate, int width, int height)
    int x, y;
    x = 50;
    y = 610;
    //make input text field
    userField = new JTextField();
    userField.setBounds(x,y,150,20);

    //make read only enemy text field
    enemyField = new JTextField();
    enemyField.setBounds(710,y,150,20);
    enemyField.setEditable(false);

    errormsg = new JTextField("That is not a valid move.");
    errormsg.setBounds(x,y+25,200,20);
    f.add(errormsg);
    errormsg.setVisible(false);
    errormsg.setEditable(false);

    // add button to send the coordinate to the server
    // add implementality
    // for now, it will just send the coordinates to enemy text field
    sendCoor = new JButton("Attack!");
    sendCoor.setBounds(x+160,y,100,20);
    sendCoor.addActionListener(this);
    f.add(userField);
    f.add(enemyField);
    f.add(sendCoor);

    //to add the gameboard
    JLabel label = new JLabel(new ImageIcon("board.png"));

    //for loop to set explosions up
    iex = new ImageIcon("explosion.gif");
    //jex = new JLabel(iex);

    x = 35; //x coordinate of first box
    y = 95; //y coordinate of first box
    int z = 40; //size of gif
    int q=0;
    for(int i = 0;i<100;i++)
    {
      uScreen[i] = new JLabel(iex);
      eScreen[i] = new JLabel(iex);

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

    f.add(label);
    f.setSize(1100,700);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  public void actionPerformed(ActionEvent e)
  {
    String userTxt;
    int shot = -2;
    if(e.getSource()==sendCoor)
    {
      errormsg.setVisible(false);
      userTxt = userField.getText();

      if(userTxt.length() < 2|| userTxt.length() > 3)
      {
        errormsg.setVisible(true);
      }
      else
      {
        shot = transform(userTxt);
        if(shot==-1)
        {
          errormsg.setVisible(true);
        }
        else
        {
          eScreen[shot].setVisible(true);
        }
      }
    }
  }

  //will return numerical value of coordinates from 0 to 99
  public int transform(String f)
  {
    f = f.toUpperCase();
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

    int b = Character.getNumericValue(f.charAt(1));
    b = b - 1;
    b = b * 10;

    return b + a;
  }

  public static void main(String[] args)
  {
    new Board();
  }

}
