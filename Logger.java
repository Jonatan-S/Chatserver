import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Date;

/**
 * Klasse um Servervorgaenge zu protokollieren und in einem Textdokument abzuspeichern.
 * 
 * @author Lukas Kraehling
 * @version V1 20171126
 */
public class Logger
{
    FileWriter fw;
    BufferedWriter bw;
    String datum;

    /**
     * Konstruktor fuer Objekte der Klasse Logger
     */
    public Logger() throws Exception 
    {
        datum = new Date().toString();
        
        //Schreibt, falls die Log-Datei bereits besteht, hinter den vorherigen Eintrag
        fw = new FileWriter("Log.txt", true);
        bw = new BufferedWriter(fw);
        
        //Überschrift des Logs erstellen
        bw.newLine();              
        bw.write(datum + ":");
        System.out.println(datum + ": Server wurde gestartet.");
        bw.newLine();
        
        //Speichert die Daten in der Datei ab, wenn der Thread beendet wird
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() 
        {
           public void run() {
             try{
               bw.close();
               fw.close();
             }catch (Exception e) {}
           }
        }));
    }

    
    /**
     * Methode um dem Log einen Eintrag hinzuzufügen
     *
     * @param String    s   Text des Eintrages
     */
    public void addToLog(String s) throws Exception
    {
        System.out.println(Time.getTime() + ": " + s);
        bw.write(Time.getTime() + ": " + s);
        bw.newLine();
    }
}

