import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Server {
    public static void main(String[] args) throws IOException {

        // Initialisieren von Client- und Servernamen sowie Portnummer
        int port = 7777;
        String serverName = "";
        String clientName = "";

        // Erstellt einen neuen ServerSocket auf dem angegebenen Port
        ServerSocket serverSocket = new ServerSocket(port);

        // Wartet auf die Verbindung eines Clients
        System.out.println("Server wartet auf Verbindung mit einem Client");
        Socket socket = serverSocket.accept();

        // Verbindung mit einem neuen Client hergestellt
        System.out.println("Neuer Client verbunden");
        System.out.println("---------------------------\n");

        // Erzeugt und initialisiert die Datenströme
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        // Fragt den Benutzer nach dem Servernamen
        while (serverName == null || serverName.trim().isEmpty()) {
            System.out.print("Wie ist dein Name ? :");
            serverName = scanner.nextLine();
            dos.writeUTF(serverName); // Sendet den Servernamen an den Client
            clientName = dis.readUTF(); // Empfängt den Clientnamen
            break;
        }

        // Schleife zum Empfangen und Senden von Nachrichten
        while (true) {
            String message = dis.readUTF(); // Empfängt eine Nachricht vom Client

            // Gibt die empfangene Nachricht aus
            System.out.println(clientName + ": " + message);

            // Wenn "q" empfangen wird, beendet sich der Server
            if (message.equals("q")) {
                dos.writeUTF("Server wird geschlossen");
                break;
            }

            // Antwort an den Client senden
            System.out.print(serverName + ": ");
            String writeBack = scanner.nextLine();
            dos.writeUTF(writeBack);
            dos.flush();
        }

        // Schließt die Datenströme und den Socket
        dis.close();
        dos.close();
        socket.close();
    }
}
