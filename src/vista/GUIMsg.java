package vista;

import javax.swing.*;
//Clase de utilidad. Por Rafael
public final class GUIMsg {

    private GUIMsg() {
        // no instanciable
    }

    public static void error(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void info(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Informacion",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(
                null,
                mensaje,
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}
