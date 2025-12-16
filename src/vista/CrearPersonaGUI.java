package vista;
//Rafael Ignacio Figueroa Esspinoza no terminado
import javax.swing.*;
import java.awt.event.*;

public class CrearPersonaGUI extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JTextField txtRut;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    private JTextField txtExtra;
    private JTextField txtFechaNac;

    private JRadioButton rbPropietario;
    private JRadioButton rbSupervisor;
    private JRadioButton rbCosechador;

    public CrearPersonaGUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onOK() {
        // logica despues
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        CrearPersonaGUI dialog = new CrearPersonaGUI();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }
}
