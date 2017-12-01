import java.io.IOException;
import java.util.Date;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Beschreiben Sie hier die Klasse VerwalterServer.
 * 
 * @author Lukas Kraehling, Jonatan Steuernagel
 * @version V3 20171201
 */
public class VerwalterServer
{
    /**
     * Hauptmethode, die alle eingehenden Anfragen verwaltet und die Serverstruktur aufbaut.
     */
    public static void main (String [] args) throws Exception
    {
        //Server an Port 5000 und der aktuellen IP starten
        ServerSocket server = new ServerSocket(8080);

        //Socket erstellen, um Nutzeradressen zu speichern bzw. weiterzugeben
        Socket client = null;

        //Log anlegen
        Logger log = new Logger();

        //Gruppenchat erzeugen
        Gruppenchat chat = new Gruppenchat();

        Blacklist bl = new Blacklist();

        try{
            while(true)
            {
                //Alle Socket-Anfragen annehmen
                client = server.accept();

                if(bl.isBanned(client.getInetAddress()))
                {
                    client.close();
                } else {
                    //Verbindung mit neuem Socket protokollieren
                    log.addToLog("A new client has connected.");

                    //Neue Verbindung mit den noetigen Daten erzeugen und starten
                    new Verbindung(client, chat, log, bl).start();  
                }
            }
        }
        catch (Exception e) {
            //Bei Fehlern System beenden
            server.close();
            System.exit(1);
        }
    }
}
