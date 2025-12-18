package vista;

import controlador.ControladorProduccion;

import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ListarCosechadores extends JDialog {
    private JPanel contentPane;
    private JButton buttonBack;
    private JTable lista;
    private JLabel labelCosechadores;

    public ListarCosechadores() {
        setTitle("Listado de Cosechadores");
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass()
                        .getResource("/vista/icons/32x32/listaCliente.png")
                )
        );
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
        desplegarLista();

        pack();
        setLocationRelativeTo(null);
    }

    private void desplegarLista(){

        String [] datosCos = ControladorProduccion.getInstance().listCosechadores();
        //datos: ["dato1; dato2; dato3 ...; daton", "dato....]
        DefaultTableModel modelo;


        if(datosCos.length == 0){
            String[] columnaMensaje = {"Estado"};
            modelo = new DefaultTableModel(columnaMensaje, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            modelo.addRow(new String[]{"No hay cosechadores para listar"});

            //Centrar texto
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            lista.setDefaultRenderer(Object.class, centerRenderer);
        } else {
            String[] columnas = {"Rut", "Nombre", "Dirección", "email", "Fecha Nac.",
                    "Nro. Cuadrillas", "Monto impago $", "Monto pagado $"};

            modelo = new DefaultTableModel(columnas, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // ninguna celda editable
                }
            };

            for (String s: datosCos ){
                modelo.addRow(s.split("; "));
            }

            lista.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        }

        lista.setModel(modelo);

        lista.setPreferredScrollableViewportSize(
                new Dimension(lista.getPreferredSize().width,
                        lista.getRowHeight() * 5)
        );
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /*
    //Esto es para pruebas.
    public static void main(String[] datos) {
        ListarCosechadores dialog = new ListarCosechadores();
        dialog.setVisible(true);
    }
    */
    public static void display() {
        ListarCosechadores dialog = new ListarCosechadores();
        dialog.setVisible(true);
        dialog.toFront();
        dialog.requestFocus();
    }
}
