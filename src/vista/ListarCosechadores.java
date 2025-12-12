package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ListarCosechadores extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonBack;
    private JTable lista;
    private JLabel labelCosechadores;

    public ListarCosechadores(String[] datos) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ninguna celda editable
            }
        };
        lista.setModel(modelo);

        lista.setPreferredScrollableViewportSize(
                new Dimension(lista.getPreferredSize().width,
                        lista.getRowHeight() * 5)
        );

        /*
        Aquí debes leer los datos, separarlos y crear una fila con cada datu ;)
        for(){
            modelo.addRow();
        }
        */
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void display(String[] datos) {
        ListarCosechadores dialog = new ListarCosechadores(datos);
        dialog.pack();
        dialog.setVisible(true);
    }
}
