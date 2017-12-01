import java.util.Date;

/**
 * Gibt die aktuelle Uhrzeit zurück.
 * 
 * @author Lukas Kraehling
 * @version V1 20171127
 */
public class Time
{
    /**
     * Methode um die Stunden, Minuten und Sekunden von der Date-Klasse
     *
     * @return String Uhrzeit im Format: HH:MM:SS
     */
    public static String getTime()
    {
        Date uhrzeit = new Date();
        String stunden = formatTo00(uhrzeit.getHours());
        String minuten = formatTo00(uhrzeit.getMinutes());
        String sekunden = formatTo00(uhrzeit.getSeconds());
        return stunden + ":" + minuten + ":" + sekunden;
    }

    /**
     * Methode um einen Int-Wert zu einem String mit einer vorangestellten Null zu machen, falls dieser einstellig ist.
     *
     * @param  int    i     Int-Wert
     * @return String       Int-Wert als String mit dem Format 0#.
     */
    private static String formatTo00(int i)
    {
        if(i<10)
        {
           return "0" + i; 
        }
        return "" + i;
    }
}
