package vista;

//Rafael Ignacio Figueroa Espinoza

import controlador.ControladorProduccion;
import utilidades.Rut;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;

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
    private JLabel rutLabel;
    private JLabel nombreLabel;
    private JLabel emailLabel;
    private JLabel direccionLabel;
    private JLabel rolLabel;

    private JLabel extraLabel; //le puse de nombre extra, porque funcionara para dir comercial y para profesion
    private JLabel fechaNacimientoLabel;
    private JLabel creacionDePersonasLabel;

    private ControladorProduccion cp = ControladorProduccion.getInstance();

    public CrearPersonaGUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ButtonGroup grupoRoles = new ButtonGroup(); //defino el grupo
        grupoRoles.add(rbPropietario); //agrego cada uno de los rb al grupo
        grupoRoles.add(rbSupervisor);
        grupoRoles.add(rbCosechador);

        ActionListener listenerCambioRol = e -> actualizarCampos(); //creo un actionlistener para cada uno, con un metod que me deja cambiar la visibilidad de los campos de texto
        rbPropietario.addActionListener(listenerCambioRol);
        rbSupervisor.addActionListener(listenerCambioRol);
        rbCosechador.addActionListener(listenerCambioRol);

        rbPropietario.setSelected(true); //lo dejo seleccionado por defecto, para que se vea mejor XD
        actualizarCampos();

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        pack(); //pongo estas cosas aca para que funcionen cuando se ponga en el menu y no solo con el main de la misma clase
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }

    private void actualizarCampos() { //este es el metod

        extraLabel.setVisible(false); //hago que no se vean los labels y sus respectivos text fields
        txtExtra.setVisible(false);
        fechaNacimientoLabel.setVisible(false);
        txtFechaNac.setVisible(false);

        if (rbPropietario.isSelected()) { //se hace el cambio, viendo cuál fue seleccionado y escondiendo los que no lo están

            extraLabel.setText("Dirección Comercial:");
            extraLabel.setVisible(true);
            txtExtra.setVisible(true);

        } else if (rbSupervisor.isSelected()) {

            extraLabel.setText("Profesión:");
            extraLabel.setVisible(true);
            txtExtra.setVisible(true);

        } else if (rbCosechador.isSelected()) {

            fechaNacimientoLabel.setVisible(true);
            txtFechaNac.setVisible(true);
        }
        pack();//para ajustar el tamaño
    }


    private void onOK() {
        try {
            GUIHelper.validarNoVacio(txtRut.getText(), "Rut");
            GUIHelper.validarNoVacio(txtNombre.getText(), "Nombre");
            GUIHelper.validarNoVacio(txtEmail.getText(), "Email");
            GUIHelper.validarNoVacio(txtDireccion.getText(), "Dirección");

            Rut rut = GUIHelper.obtenerRut(txtRut.getText());

            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (rbPropietario.isSelected()) {

                String dirComercial = txtExtra.getText().trim();

                GUIHelper.validarNoVacio(dirComercial, "Dirección Comercial");

                cp.createPropietario(
                        rut,
                        nombre,
                        email,
                        direccion,
                        dirComercial
                );

            } else if (rbSupervisor.isSelected()) {

                String profesion = txtExtra.getText().trim();

                GUIHelper.validarNoVacio(profesion, "Profesión");

                cp.createSupervisor(
                        rut,
                        nombre,
                        email,
                        direccion,
                        profesion
                );

            } else if (rbCosechador.isSelected()) {

                String fechaTexto = txtFechaNac.getText().trim();
                GUIHelper.validarNoVacio(fechaTexto, "Fecha de Nacimiento");

                LocalDate fecha = GUIHelper.obtenerFecha(fechaTexto);

                cp.createCosechador(
                        rut,
                        nombre,
                        email,
                        direccion,
                        fecha
                );
            }

            GUIMsg.info("Persona creada correctamente");

            dispose();

        } catch (Exception ex) {
            GUIMsg.error(ex.getMessage());
        }
    }


    private void onCancel() {
        dispose();
    }

    public static void display() {
        CrearPersonaGUI dialog = new CrearPersonaGUI();
        dialog.setVisible(true);
    }

}
