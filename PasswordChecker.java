/**
 * Klasse um ein eingegebenes Passwort zu ueberpruefen.
 * 
 * @author Lukas Kraehling
 * @version V1 20171126
 */
public class PasswordChecker
{
    /**
     * Methode um das eingegebene Passwort zu ueberpruefen.
     *
     * @param  String    pw     Eingegebenes Passwort.
     * @return boolean          Gibt an, ob das Passwort richtig oder falsch war.
     */
    public static boolean checkPw (String pw)
    {
        if(pw.equals("Mesa")){
          return true;    
        }
        else{
          return false;
        } 
    }
}
