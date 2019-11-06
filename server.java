/** code from the tcp1 client server example*/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

class TcpServer {

  public static void main(String[] argv) throws Exception {
    //String welcomeMessage = "Welcome\n";
    String clientSentence;
    String capitalizedSentence;

    ServerSocket welcomeSocket = null;

    try {
      welcomeSocket = new ServerSocket(6789);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }

    while (true) {
      Socket connectionSocket = welcomeSocket.accept();
      BufferedReader inFromClient = new BufferedReader(
          new InputStreamReader(connectionSocket.getInputStream()));
      DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

      clientSentence = inFromClient.readLine();
      System.out.println(clientSentence);
      capitalizedSentence = clientSentence.toUpperCase() + '\n';
      System.out.println(capitalizedSentence);
      outToClient.writeBytes(capitalizedSentence);

      connectionSocket.close();
    }
  }
}
