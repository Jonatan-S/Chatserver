import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Beschreiben Sie hier die Klasse Verbindung.
 * 
 * @author Lukas Kraehling, Jonatan Steuernagel
 * @version V3 20171201
 */
public class Verbindung extends Thread
{
    private String inputLine;
    private Socket clientSocket;
    private String name;
    private User user;
    private Gruppenchat chat;
    private Logger log;
    private PrintWriter pr;
    private BufferedReader br;
    private Blacklist bl;

    /**
     * Konstruktor fuer Objekte der Klasse Verbindung
     * 
     * @param Socket        cs  Socket des Nutzers
     * @param Gruppenchat   gc  Aktueller Gruppenchat vom VerwalterServer
     * @param Logger        lg  Aktueller Logger vom VerwalterServer
     */
    public Verbindung(Socket cs, Gruppenchat gc, Logger lg, Blacklist b) throws Exception
    {
        clientSocket = cs; //Nutzer wird automatisch verbunden
        chat = gc;
        log = lg;
        bl = b;
        pr = new PrintWriter(clientSocket.getOutputStream(), true);
        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Methode um den Thread fuer die Verbindung des zugehoerigen Nutzers zu starten.
     *
     */
    public void run()
    {
        try{
            //Passwort ueberpruefen
            pr.println("Password: ");

            //Passwort wird ueberpueft
            if(PasswordChecker.checkPw(br.readLine().trim())) {

                //Nutzernamen erfragen, speichern und protokollieren
                pr.println("Username: ");
                name = br.readLine().trim();

                //Loggt den beigetretenen Nutzer mit IP Adresse
                InetAddress inet = clientSocket.getInetAddress();
                if(inet.toString().equals(clientSocket.getLocalAddress().toString())) {
                    log.addToLog("User >" + name + "< joined from localhost");
                } else {
                    log.addToLog("User >" + name + "< joined on IP " + clientSocket.getInetAddress());
                }

                //Nutzerliste aktualisieren
                if(chat.isEmpty()) {
                    user = new User(clientSocket, pr, name, Rank.ADMIN);
                } else {
                    user = new User(clientSocket, pr, name, Rank.GUEST);
                }
                chat.addUser(user);

                //Alle Chatmitglieder benachrichtigen
                chat.writeAll(name + " entered.");

                //Info zu Befehlen und aktuellen Nutzern                               
                /** Es fehlt noch /help und /rank im Schwitz Case :D */
                pr.println("/HELP To see a list of all commands.");
                pr.println("/LEAVE To leave the chat.");
                pr.println("/USERS To list all current chatroomusers.");
                pr.println("/RANK To see your current rank.");
                pr.println("");
                pr.println("Commands for Admins or Moderators:");
                pr.println("/KICK USERNAME To kick the specified user from the server. (Admin or Moderator)");
                pr.println("/BAN  USERNAME  To ban the specified user from the server. (Admin)");
                pr.println("/CHANGERANK USERNAME NEWRANK To change the rank of the user you specified. (Admin)");
                pr.println("");
                pr.println("Your current rank: " + user.getRank());
                pr.println("Current chatroomusers: " + chat.list());

                while(true)
                {
                    //Nutzereingabe abfragen
                    if(clientSocket.isClosed()) stop();
                    inputLine = br.readLine().trim();
                    String[] inputLines = inputLine.split(" ");
                    switch (inputLines[0].toLowerCase()) {
                        case "/leave":
                            //Nutzer will Chat verlassen
                            close();
                            break;
                        case "/users":
                            //Nutzer will die aktuellen Chatroomuser sehen
                            pr.println("Current chatroomusers: " + chat.list());
                            break;
                        case "/kick":
                            //Nutzer will einen Chatroomuser vom Server kicken
                            User tmpU1 = chat.getUser(inputLines[1]);
                            if (user.getRank() != Rank.ADMIN || user.getRank() != Rank.MODERATOR){
                                pr.println("Only Admins and Moderators are allowed to kick!");
                                log.addToLog(">" + name + "< tried to kick >" + inputLines[1] + "< without permission!");
                            }
                            else{
                                if (tmpU1 == null){
                                    pr.println("This user doesn't exist!");
                                    log.addToLog(">" + name + "< tried to kick >" + inputLines[1] + "<, but there was no such user.");
                                }
                                else
                                {
                                    kick(tmpU1,"kicked");
                                }
                            }
                            break;
                        case "/ban":
                            //Nutzer will einen Chatroomuser vom Server bannen
                            User tmpU2 = chat.getUser(inputLines[1]);
                            //Prüfen, ob der ausführende Nutzer genügend Rechte bestitzt
                            if (user.getRank() == Rank.ADMIN){
                                pr.println("Only Admins are allowed to ban!");
                                log.addToLog(">" + name + "< tried to ban >" + inputLines[1] + "< without permission!");
                            }
                            else
                            {
                                //Wenn dies der falls ist wird über prüft, ob der angegebene Nutzer existiert, ob der ausführende Nutzer hat
                                if(tmpU2 != null) {
                                    bl.add(tmpU2);
                                    kick(tmpU2,"banned");
                                } 
                                else
                                {
                                  pr.println("This user doesn't exist!");
                                  log.addToLog(">" + name + "< tried to ban >" + inputLines[1] + "<, but there was no such user.");  
                                }
                            }
                            break;
                        case "/changerank":
                            //Nutzer will den Rang eines Chatroomuser ändern.
                            User tmpU3 = chat.getUser(inputLines[1]);
                            if (user.getRank() != Rank.ADMIN){
                                pr.println("Only Admins are allowed to change ranks!");
                                log.addToLog(">" + name + "< tried to change the rank of >" + inputLines[1] + "< without permission!");
                            }
                            else
                            {
                                if(tmpU3 != null) 
                                {
                                    String tmpR = inputLines[2].toUpperCase();
                                    switch (tmpR){
                                        case "ADMIN":
                                            tmpU3.setRank(Rank.ADMIN);
                                            tmpU3.getPrintWriter().println("You are now ADMIN. (Changed by " +name+")");
                                            break;
                                        case "MODERATOR":
                                            tmpU3.setRank(Rank.MODERATOR);
                                            tmpU3.getPrintWriter().println("You are now MODERATOR. (Changed by " +name+")");
                                            break;
                                        case "GUEST":
                                            tmpU3.setRank(Rank.GUEST);
                                            tmpU3.getPrintWriter().println("You are now GUEST. (Changed by " +name+")");
                                            break;
                                    }
                                    pr.println("You have successfully changed the rank of " +tmpU3.getName()+ " to " +tmpR+ ".");
                                } 
                                else
                                {                                                  
                                    pr.println("This user doesn't exist!");
                                    log.addToLog(">" + name + "< tried to change the rank of >" + tmpU3.getName() + "<, but there was no such user."); 
                                }
                            }
                            break;
                        default:
                            //Nutzer schreibt in den Chat und das geschriebene wird protokolliert
                            chat.writeAll(name + " @ " + Time.getTime() + ": " + inputLine);
                            log.addToLog("User >" + name + "< wrote: " + inputLine);
                            break;
                    }
                }
            } 
            //Falls Passwort nicht korrekt ist, wird die Verbindung sofort gekappt.
            else
            {
                close();
            }
        }catch (Exception e) {
            //Wenn Fehler in der Verbindung auftauchen wird die Verbindung sofort gekappt.
            try{
                e.printStackTrace();
                close();
            } catch (Exception ex) {}
        }
    }

    private void kick(User u, String message) throws Exception
    {
        if(user.getRank() == Rank.ADMIN || user.getRank() == Rank.MODERATOR) {
            u.getPrintWriter().println("You were " + message + "!");
            u.getSocket().close();
            chat.delUser(u);
            chat.writeAll(u.getName() + " was " + message + " by " + name);
            log.addToLog("User >" + u.getName() + "< was " + message + " by >"+ name +"<.");
        } else {
            pr.println("Only Admins and Moderators are allowed to kick!");
            log.addToLog(">" + name + "< tried to kick >" + u.getName() + "< without permission!");
        }
    }

    private void close() throws Exception
    {
        //Socket schließen
        clientSocket.close();

        //Listen aktualisieren
        chat.delUser(user);

        //Andere Nutzer benachrichtigen, das Verlassen protokollieren und den Thread anhalten.
        if(name != null) {
            chat.writeAll(name + " left.");
            log.addToLog("User >"+ name + "< left.");
        } else {
            log.addToLog("A user entered the wrong password.");
        }
        stop();   
    }
}
