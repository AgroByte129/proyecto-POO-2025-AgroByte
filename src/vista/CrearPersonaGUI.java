package vista;
//Rafael Ignacio Figueroa Espinoza
// te sale arturo? sí
import controlador.ControladorProduccion;

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
        //Cambie  el onOK debido a la creacion de las clases auxiliares, y ya que al crear a una persona no reconocia los erroes de fechas, ruts etc
        try {
            String rut = txtRut.getText().trim();
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (rut.isEmpty() || nombre.isEmpty() || email.isEmpty() || direccion.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Existen datos obligatorios vacios",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (rbPropietario.isSelected()) {

                String dirComercial = txtExtra.getText().trim();
                if (dirComercial.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Debe ingresar la direccion comercial",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                cp.createPropietario(
                        utilidades.Rut.of(rut),
                        nombre,
                        email,
                        direccion,
                        dirComercial
                );

            } else if (rbSupervisor.isSelected()) {

                String profesion = txtExtra.getText().trim();
                if (profesion.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Debe ingresar la profesion",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                cp.createSupervisor(
                        utilidades.Rut.of(rut),
                        nombre,
                        email,
                        direccion,
                        profesion
                );

            } else if (rbCosechador.isSelected()) {

                String fechaTexto = txtFechaNac.getText().trim();
                if (fechaTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Debe ingresar la fecha de nacimiento",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.time.LocalDate fecha = java.time.LocalDate.parse(
                        fechaTexto,
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                );

                cp.createCosechador(
                        utilidades.Rut.of(rut),
                        nombre,
                        email,
                        direccion,
                        fecha
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Persona creada correctamente",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {

        try {
            ControladorProduccion.getInstance().readDataFromTextFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CrearPersonaGUI dialog = new CrearPersonaGUI();
        dialog.setVisible(true);
        System.exit(0);
    }
}
