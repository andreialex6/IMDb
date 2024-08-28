import java.util.List;

public class Autentification {
    public static boolean autentification(String email, String password, List<Regular> regular, List<Contributor> contributor, List<Admin> admin) {
        for (Regular r : regular) {
            Credentials credentials = r.info.getCredentials();
            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                return true;
            }
        }
        for (Contributor c : contributor) {
            Credentials credentials = c.info.getCredentials();
            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                return true;
            }
        }
        for (Admin a : admin) {
            Credentials credentials = a.info.getCredentials();
            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
