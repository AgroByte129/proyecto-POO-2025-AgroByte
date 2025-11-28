package persistencia;

public class GestionHuertosIO {
    private static GestionHuertosIO instance;
    private GestionHuertosIO(){}
    public static GestionHuertosIO getInstance() {
        if (instance == null) {
            instance = new GestionHuertosIO();
        }
        return instance;
    }
}
