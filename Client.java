import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Initialise client and server names and port number
        String nameClient = "";
        String serverName = "";
        int port = 7777;

        // Display a message that the connection is being established
        System.out.println(" Connection to the server...");

        // Create a new socket and connect to ‘localhost’ on port 7777
        Socket clientSocket = new Socket("localhost", port);

        // Display a message that the connection is successful
        System.out.println("Connection to server successful");
        System.out.println("--------------------------------------\n");

        // Create DataInputStream and DataOutputStream for communication
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        // As long as the client name is empty, the user is prompted to enter their name
        while (nameClient == null || nameClient.trim().isEmpty()) {
            System.out.print("What's your name ? : ");
            nameClient = scanner.nextLine();
            dos.writeUTF(nameClient); // Send the client name to the server
            serverName = dis.readUTF();  // Receive the server name
            break;
        }

        try {
            // Loop for the selection of options by the user
            while (true) {
                System.out.println("Select an option:");
                System.out.println("1. Chat");
                System.out.println("2. Exit");

                String choice = scanner.nextLine();

                // If the selection is ‘1’, chat mode is started
                if (choice.equals("1")) {
                    System.out.println("Chatting has started. Enter ‘exit’ to end the chat.");
                    System.out.println("Pressing q will close the server and take you to the menu.");

                    // Loop for sending and receiving messages
                    while (true) {
                        System.out.print(nameClient + ": ");
                        String message = scanner.nextLine();

                        // If the message is ‘exit’, the chat mode is ended
                        if (message.equalsIgnoreCase("exit")) {
                            break;
                        }

                        // Send the message to the server
                        dos.writeUTF(message);
                        dos.flush();

                        // Receive the response from the server
                        String response = dis.readUTF();

                        // If the response is ‘Server is closing’, the server is terminated
                        if (response.equals("Server will be closed")) {
                            System.out.println("Server closed !");
                            break;
                        }
                        System.out.println(serverName + " :" + response);
                    }
                } else if (choice.equals("2")) {
                    // If the selection is ‘2’, the client is terminated
                    System.out.println("Server will be closed");
                    break;
                } else {
                    // Invalid selection
                    System.out.println("Invalid selection. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the data streams and the socket
        dis.close();
        dos.close();
        clientSocket.close();
    }
}
