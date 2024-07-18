import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Initialise client and server names and port number
        int port = 7777;
        String serverName = "";
        String clientName = "";

        // Creates a new ServerSocket on the specified port
        ServerSocket serverSocket = new ServerSocket(port);

        // Waits for the connection of a client
        System.out.println("Server waits for connection with a client\n");
        Socket socket = serverSocket.accept();

        if(socket.isConnected()){
            // Connection established with a new client
            System.out.println("New Client connected");
            System.out.println("---------------------------\n");

        }

        // Generates and initialises the data streams
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        // Asks the user for the server name
        while (serverName == null || serverName.trim().isEmpty()) {
            System.out.print("What is your name: ");
            serverName = scanner.nextLine();
            dos.writeUTF(serverName); // Sends the server name to the client
            clientName = dis.readUTF(); // Receives the client name
            break;
        }

        // Loop for receiving and sending messages
        while (true) {
            String message = dis.readUTF(); // Receives a message from the client

            // Outputs the received message
            System.out.println(clientName + ": " + message);

            // When ‘q’ is received, the server terminates
            if (message.equals("q")) {
                dos.writeUTF("Server will be  closed");
                break;
            }

            // Send response to the client
            System.out.print(serverName + ": ");
            String writeBack = scanner.nextLine();
            dos.writeUTF(writeBack);
            dos.flush();
        }

        // Closes the data streams and the socket
        dis.close();
        dos.close();
        socket.close();
    }
}
