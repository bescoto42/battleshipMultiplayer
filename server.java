/** code from the tcp1 client server example*/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

class server {

  public static void main(String[] argv) throws Exception {
    //String welcomeMessage = "Welcome\n";
    String clientSentence;
    String capitalizedSentence;

    //open socket
    ServerSocket welcomeSocket = null;

    try {
      welcomeSocket = new ServerSocket(6789);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }

    //keep waiting for user input until connection is closed
    while (true) {
      //create connection
      Socket connectionSocket = welcomeSocket.accept();

      //read strings from client
      BufferedReader inFromClient = new BufferedReader(
          new InputStreamReader(connectionSocket.getInputStream()));

      //get data from client and modify it
      DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

      clientSentence = inFromClient.readLine();
      System.out.println(clientSentence);
      capitalizedSentence = clientSentence.toUpperCase() + '\n';
      System.out.println(capitalizedSentence);

      //send data to client
      outToClient.writeBytes(capitalizedSentence);

      //close connection
      connectionSocket.close();
    }
  }
}
