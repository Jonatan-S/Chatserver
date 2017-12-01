import java.net.Socket;
import java.io.PrintWriter;

/**
 * Klasse, um einen Nutzer mit Namen und Socket zu speichern und zu verwalten.
 * 
 * @author Lukas Kraehling, Jonatan Steuernagel
 * @version V1 20171201
 */
public class User
{
    private Socket socket;
    private PrintWriter pr;
    private String name;
    private Rank rank;

    /**
     * Konstruktor fuer Objekte der Klasse User.
     * 
     * @param Socket        s   Socket des Nutzers
     * @param PrintWriter   pr  Socket des Nutzers
     * @param String        n   Name des Nutzers
     */
    public User(Socket s, PrintWriter p, String n, Rank r)
    {
        socket = s;
        pr = p;
        name = n;
        rank = r;
    }

    /**
     * Methode um den Namen des Nutzers zurueckzugeben.
     *
     * @return String      Name des Nutzers
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Methode um den Socket des Nutzers zurueckzugeben.
     *
     * @return Socket      Socket des Nutzers
     */
    public Socket getSocket()
    {
        return socket;
    }
    
    /**
     * Methode um den OutputStreamWriter des Nutzers zurueckzugeben.
     *
     * @return PrintWriter      Socket des Nutzers
     */
    public PrintWriter getPrintWriter()
    {
        return pr;
    }
    
    /**
     * Methode um den Rang des Nutzer zurueckzugeben.
     * 
     * @return Rank         Rang des Nutzers
     */
    public Rank getRank()
    {
        return rank;
    }
    
    /**
     * Methode um den Rang des Nutzer zu ändern.
     * 
     * @param Rank  neuRank Rang des Nutzers
     */
    public void setRank(Rank newRank)
    {
        rank = newRank;
    }
}
