import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class Board extends JFrame implements ActionListener
{
  JTextField userField, enemyField;
  JButton sendCoor;
  Icon bg = new ImageIcon("resources/board.png");
  Board()
  {
    JFrame f=new JFrame();
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
    ImageIcon icon = new ImageIcon("board.png");
    JLabel label = new JLabel(icon);

    f.add(label);
    f.setSize(1100,700);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  public void actionPerformed(ActionEvent e)
  {
    String userTxt = userField.getText();
    enemyField.setText(userTxt);

  }
  public static void main(String[] args)
  {
    new Board();
  }

}
