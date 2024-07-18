import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {

        // Initialisieren von Client- und Servernamen sowie Portnummer
        String nameClient = "";
        String serverName = "";
        int port = 7777;

        // Anzeige einer Nachricht, dass die Verbindung aufgebaut wird
        System.out.println("Verbindungsaufbau mit dem Server..");

        // Erzeugen eines neuen Sockets und Verbindung zu "localhost" auf Port 7777
        Socket clientSocket = new Socket("localhost", port);

        // Anzeige einer Nachricht, dass die Verbindung erfolgreich ist
        System.out.println("Verbindung mit Server erfolgreich");
        System.out.println("--------------------------------------\n");

        // Erzeugen von DataInputStream und DataOutputStream für die Kommunikation
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        // Solange der Clientname leer ist, wird der Benutzer aufgefordert, seinen Namen einzugeben
        while (nameClient == null || nameClient.trim().isEmpty()) {
            System.out.print("Wie ist dein Name ? : ");
            nameClient = scanner.nextLine();
            dos.writeUTF(nameClient);  // Senden des Clientnamens an den Server
            serverName = dis.readUTF();  // Empfangen des Servernamens
            break;
        }

        try {
            // Schleife zur Auswahl von Optionen durch den Benutzer
            while (true) {
                System.out.println("Wählen Sie eine Option:");
                System.out.println("1. Chatten");
                System.out.println("2. Beenden");

                String choice = scanner.nextLine();

                // Falls die Wahl "1" ist, wird der Chat-Modus gestartet
                if (choice.equals("1")) {
                    System.out.println("Chatten gestartet. Geben Sie 'exit' ein, um das Chatten zu beenden.");
                    System.out.println("Drücken Sie q wird der Server beendet und Sie kommen in das Menü.");

                    // Schleife für das Senden und Empfangen von Nachrichten
                    while (true) {
                        System.out.print(nameClient + ": ");
                        String message = scanner.nextLine();

                        // Falls die Nachricht "exit" ist, wird der Chat-Modus beendet
                        if (message.equalsIgnoreCase("exit")) {
                            break;
                        }

                        // Senden der Nachricht an den Server
                        dos.writeUTF(message);
                        dos.flush();

                        // Empfangen der Antwort vom Server
                        String response = dis.readUTF();

                        // Falls die Antwort "Server wird geschlossen" ist, wird der Server beendet
                        if (response.equals("Server wird geschlossen")) {
                            System.out.println("Server beendet !");
                            break;
                        }
                        System.out.println(serverName + " :" + response);
                    }
                } else if (choice.equals("2")) {
                    // Falls die Wahl "2" ist, wird der Client beendet
                    System.out.println("Client wird beendet.");
                    break;
                } else {
                    // Ungültige Auswahl
                    System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Schließen der Datenströme und des Sockets
        dis.close();
        dos.close();
        clientSocket.close();
    }
}
