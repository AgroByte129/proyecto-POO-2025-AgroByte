package vista;

import controlador.ControladorProduccion;
import utilidades.Calidad;
import utilidades.GestionHuertosException;
import utilidades.Rut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Diego Jara Betancourt y Arturo Gómez Senn

public class AgregarPesajeCosechador extends JDialog {
    private JPanel contentPane;
    private JButton aceptarButton;
    private JButton cancelarButton;
    private JLabel agregaciónDePesajeCosechadorLabel;
    private JTextField textFieldIDPesaje;
    private JComboBox <String> comboBoxCosechador;
    private JComboBox <String> comboBoxCuadrilla;
    private JComboBox <Calidad> comboBoxCalidad;
    private JTextField textFieldCantidadKilos;
    private JLabel IDPesajeLabel;
    private JLabel cosechadorLabel;
    private JLabel cuadrillaLabel;
    private JLabel cantidadKilosLabel;
    private JLabel calidadLabel;

    private ControladorProduccion cp = ControladorProduccion.getInstance();

    public AgregarPesajeCosechador() {
        setTitle("Ingreso de Pesaje");
        try {
            setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass()
                            .getResource("/vista/icons/32x32/agregar-archivo.png")
                    )
            );
        }catch (Exception e){}

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(aceptarButton);

        cargarCosechadores();
        cargarCalidades();

        comboBoxCosechador.addActionListener(e -> cargarCuadrillasDelCosechador());

        if (comboBoxCosechador.getItemCount() > 0) {
            comboBoxCosechador.setSelectedIndex(0);
        }

        aceptarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarCalidades() {
        comboBoxCalidad.setModel(new DefaultComboBoxModel<>(Calidad.values()));
    }

    private void cargarCosechadores() {
        comboBoxCosechador.removeAllItems();
        String[] lista = cp.listCosechadores();

        for (String linea : lista) {
            String[] datos = linea.split(";");
            if (datos.length >= 2) {
                String item = datos[1].trim() + " (" + datos[0].trim() + ")";
                comboBoxCosechador.addItem(item);
            }
        }
    }

    private void cargarCuadrillasDelCosechador() {
        comboBoxCuadrilla.removeAllItems();

        String seleccion = (String) comboBoxCosechador.getSelectedItem();
        if (seleccion == null) return;

        try {

            int inicio = seleccion.lastIndexOf("(") + 1;
            int fin = seleccion.lastIndexOf(")");
            String rutString = seleccion.substring(inicio, fin);

            Rut rut = GUIHelper.obtenerRut(rutString);

            String[] cuadrillas = cp.getCuadrillasDeCosechadorDePlan(rut);

            for (String cuad : cuadrillas) {
                String[] datos = cuad.split(";");
                String item = "ID: " + datos[0].trim() + " - " + datos[1].trim() + " - Plan: " + datos[2].trim();
                comboBoxCuadrilla.addItem(item);
            }
        } catch (Exception e) {
        }
    }

    private void onOK() {
        try {

            GUIHelper.validarNoVacio(textFieldIDPesaje.getText(), "ID Pesaje");
            GUIHelper.validarNoVacio(textFieldCantidadKilos.getText(), "Kilos");

            if (comboBoxCosechador.getSelectedItem() == null || comboBoxCuadrilla.getSelectedItem() == null) {
                throw new IllegalArgumentException("Debe seleccionar Cosechador y Cuadrilla válida.");
            }

            int idPesaje = Integer.parseInt(textFieldIDPesaje.getText().trim());
            float kilos = Float.parseFloat(textFieldCantidadKilos.getText().trim().replace(",", "."));
            Calidad calidad = (Calidad) comboBoxCalidad.getSelectedItem();

            String seleccionCosechador = (String) comboBoxCosechador.getSelectedItem();
            int inicioRut = seleccionCosechador.lastIndexOf("(") + 1;
            int finRut = seleccionCosechador.lastIndexOf(")");
            String rutString = seleccionCosechador.substring(inicioRut, finRut);
            Rut rutCosechador = GUIHelper.obtenerRut(rutString);

            String seleccionCuadrilla = (String) comboBoxCuadrilla.getSelectedItem();
            String[] partesCuadrilla = seleccionCuadrilla.split(" - ");

            int idCuadrilla = Integer.parseInt(partesCuadrilla[0].replace("ID:", "").trim());

            int idPlan = Integer.parseInt(partesCuadrilla[2].replace("Plan:", "").trim());

            cp.addPesaje(idPesaje, rutCosechador, idPlan, idCuadrilla, kilos, calidad);

            GUIMsg.info("Pesaje agregado exitosamente.");
            dispose();

        } catch (NumberFormatException e) {
            GUIMsg.error("ID debe ser entero y Kilos número decimal.");
        } catch (Exception e) {
            GUIMsg.error(e.getMessage());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

//
    public static void display() {
        AgregarPesajeCosechador dialog = new AgregarPesajeCosechador();
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();
    }
}
