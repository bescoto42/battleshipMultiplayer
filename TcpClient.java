/** code from the tcp1 client server example*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.Socket;

class TcpClient {
  public static void main(String[] argv) throws Exception {

    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Type a sentence :");
    String sentence = inFromUser.readLine();

    Socket clientSocket = null;

    try {
      clientSocket = new Socket("localhost", 6789);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }

    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer =  new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
    outToServer.writeBytes(sentence + '\n');

    String modifiedSentence = inFromServer.readLine();
    System.out.println("FROM SERVER: " + modifiedSentence);

    clientSocket.close();

  }
}
