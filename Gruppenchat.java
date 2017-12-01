import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse um den Gruppenchat, bzw. dessen Mitglieder zu verwalten und an diese Nachrichten zu uebermitteln.
 * 
 * @author Lukas Kraehling, Jonatan Steuernagel
 * @version V3 20171201
 */
public class Gruppenchat
{
    //ArrayList erstellen um Nutzer zu speichern und zu verwalten
    ArrayList<User> users;

    /**
     * Konstruktor fuer Objekte der Klasse Gruppenchat
     */
    public Gruppenchat()
    {
        users = new ArrayList<User>();
    }

    /**
     * Methode um einen Nutzer der Liste hinzuzufuegen
     *
     * @param User  u   Nutzer der hinzugefuegt werden soll
     */
    public void addUser(User u)
    {
        users.add(u);
    }

    /**
     * Methode um einen Nutzer aus der Liste zu loeschen
     *
     * @param User  u   Nutzer der geloescht werden soll
     */
    public void delUser(User u)
    {
        users.remove(u);
    }

    /**
     * Methode um an alle Nutzer der Liste zu schreiben
     *
     * @param String  t   Der zu uebertragende Text
     */
    public void writeAll(String t) throws Exception
    {
        //Jedem Nutzer aus der Liste den gleichen Text uebermitteln
        for(User u : users) 
        {
            u.getPrintWriter().println(t);    
        }
    }
    
    public boolean isEmpty()
    {
        return users.isEmpty();
    }

    /**
     * Methode um an einen Nutzer eine Liste aller aktuellen Chatroommitglieder zu uebermitteln
     *
     * @return String  Die Liste als String. Namen mit Komma und Leerzeichen getrennt.
     */
    public String list()
    {
        StringBuilder tmp = new StringBuilder();
        //Jeden Nutzer, außer den letzten, aus der Liste auslesen und an den String zusammen mit einem Komma anhaengen
        for(int i = 0; i < users.size() - 1; i++) {
            tmp.append(users.get(i).getName() + ", ");
        }
        //Den letzten Nutzer ohne Komma anhängen
        tmp.append(users.get(users.size() - 1).getName());
        return tmp.toString();
    }
    
    public User getUser(String n)
    {
        User tmp = null;
        for(User u : users) {
            if(u.getName().equals(n)) {
                tmp = u;
                break;
            }
        }
        return tmp;
    }
}
