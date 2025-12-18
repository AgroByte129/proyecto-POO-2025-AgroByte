package vista;

import controlador.ControladorProduccion;

import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ListarCosechadores extends JDialog {
    private JPanel contentPane;
    private JButton buttonBack;
    private JTable lista;
    private JLabel labelCosechadores;

    public ListarCosechadores(String[] datos) {
        setContentPane(contentPane);
        setModal(true);

        buttonBack.addActionListener(new ActionListener() {
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

        //Texto en el JLabel
        labelCosechadores.setText("<html>" +
                "<span style='font-size:14px; font-weight:bold;'>COSECHADORES</span>" +
                "</html>");

        //Lista
        String[] columnas = {"Rut", "Nombre", "Dirección", "email", "Fecha Nac.",
                "Nro. Cuadrillas", "Monto impago $", "Monto pagado $"};
        String [] arregloCosechadores = ControladorProduccion.getInstance().listCosechadores();

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ninguna celda editable
            }
        };
        for (int i=0; i<arregloCosechadores.length; i++){
            modelo.addRow(arregloCosechadores);
        }
        lista.setModel(modelo);

        lista.setPreferredScrollableViewportSize(
                new Dimension(lista.getPreferredSize().width,
                        lista.getRowHeight() * 5)
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] datos) {
        ListarCosechadores dialog = new ListarCosechadores(datos);
        dialog.setVisible(true);
    }

    public static void display(String[] datos) {
        if (datos == null || datos.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(null, "No hay registros para mostrar.");
            return;
        }

        ListarCosechadores dialog = new ListarCosechadores(datos);
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();
    }
}
