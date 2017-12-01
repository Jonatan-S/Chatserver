import java.util.ArrayList;
import java.net.InetAddress;

/**
 * Write a description of class Blacklist here.
 *
 * @author Lukas Kraehling, Jonatan Steuernagel
 * @version V1 20171201
 */
public class Blacklist
{
    //ArrayList erstellen um Nutzer zu speichern und zu verwalten
    ArrayList<InetAddress> bannedUsers;

    /**
     * Konstruktor fuer Objekte der Klasse Gruppenchat
     */
    public Blacklist()
    {
        bannedUsers = new ArrayList<InetAddress>();
    }

    public void add(User u)
    {
        bannedUsers.add(u.getSocket().getInetAddress());
    }

    public boolean isBanned(InetAddress i)
    {
        for(InetAddress in : bannedUsers) {
            if(in.equals(i)) return true;
        }
        return false;
    }
}
