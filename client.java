/** code from the tcp1 client server example*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.Socket;

class client {
  public static void main(String[] argv) throws Exception {

    // user input
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Type a sentence :");
    String sentence = inFromUser.readLine();

    //open socket
    Socket clientSocket = null;

    try {
      //eventually change to ip address
      clientSocket = new Socket("localhost", 6789);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }

    //connection to write out to server
    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer =  new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));

    //sending string to server
    outToServer.writeBytes(sentence + '\n');

    //wait for reply from server
    String modifiedSentence = inFromServer.readLine();
    System.out.println("FROM SERVER: " + modifiedSentence);

    //close connection
    clientSocket.close();

  }
}
