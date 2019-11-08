import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Scanner;

public class Test{
  public static void main(String[] args) {

    System.out.println("Let's go ladies.");
    Scanner keyboard = new Scanner(System.in);
    String data = keyboard.nextLine();
    if(data.charAt(0)=='e')
    {
      System.out.println("ya");
    }
    else
    {
      System.out.println("No");
    }
  }
}
