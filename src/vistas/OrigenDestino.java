package vistas;

import modelo.conexion.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OrigenDestino extends javax.swing.JFrame {
    private int id = 0;
    Reporte principal = new Reporte();
    Connection conexion = Conexion.getConnection();
    private int filaSelect = -1;
    String sql, oldPass;
    int number;

    public OrigenDestino() {

        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);

        comparaTitulo();
        ocultarComponentes();

    }

    public void comparaTitulo() {
        if (Reporte.OpcionTitulo.equals("1")) {
            lblTitulo.setText("ORIGEN");
        } else if (Reporte.OpcionTitulo.equals("2")) {
            lblTitulo.setText("DESTINO");
        } else if (Reporte.OpcionTitulo.equals("3")) {
            lblTitulo.setText("EMPRESA");
        } else {
            lblTitulo.setText("No hay titulo");
        }
    }

    public void ocultarComponentes() {
        txtNombre.setVisible(false);
        lblNombre.setVisible(false);
        btnAcciones.setVisible(false);
        tblNombres.setVisible(false);
        pnlNombres.setVisible(false);
    }

    public void mostrarComponentes() {
        txtNombre.setVisible(true);
        lblNombre.setVisible(true);
        btnAcciones.setVisible(true);
        tblNombres.setVisible(true);
        pnlNombres.setVisible(true);
    }

    public void compararOpciones() {

        if (btnAcciones.getText().equals("Añadir") && lblTitulo.getText().equals("ORIGEN")) {
            insertarOrigenDestino(txtNombre.getText(), "ORIGEN");
            JOptionPane.showMessageDialog(null, "Origen añadido exitosamente");
            Consulta();
            txtNombre.setText(null);

        } else if (btnAcciones.getText().equals("Añadir") && lblTitulo.getText().equals("DESTINO")) {
            insertarOrigenDestino(txtNombre.getText(), "DESTINO");
            Consulta();
            JOptionPane.showMessageDialog(null, "Destino añadido exitosamente");
            txtNombre.setText(null);

        } else if (btnAcciones.getText().equals("Añadir") && lblTitulo.getText().equals("EMPRESA")) {
            insertarEmpresa(txtNombre.getText());
            JOptionPane.showMessageDialog(null, "Empresa añadida exitosamente");
            Consulta();
            txtNombre.setText(null);
//-----------------------------------------------------------------------------------------------------------------------------         
        } else if (btnAcciones.getText().equals("Modificar") && lblTitulo.getText().equals("ORIGEN")) {
            tomarID();
            JOptionPane.showMessageDialog(null, "Origen modificado exitosamente");
            Consulta();
            txtNombre.setText(null);

        } else if (btnAcciones.getText().equals("Modificar") && lblTitulo.getText().equals("DESTINO")) {
            tomarID();
            JOptionPane.showMessageDialog(null, "Destino modificado exitosamente");
            Consulta();
            txtNombre.setText(null);

        } else if (btnAcciones.getText().equals("Modificar") && lblTitulo.getText().equals("EMPRESA")) {
            tomarIDEm();
            JOptionPane.showMessageDialog(null, "Empresa modificada exitosamente");
            Consulta();
            txtNombre.setText(null);

//-----------------------------------------------------------------------------------------------------------------------------
        } else if (btnAcciones.getText().equals("Eliminar") && lblTitulo.getText().equals("ORIGEN")) {
            eliminarOrigenDestino(txtNombre.getText());
            JOptionPane.showMessageDialog(null, "El Origen fue eliminado");
            Consulta();
            txtNombre.setText(null);

        } else if (btnAcciones.getText().equals("Eliminar") && lblTitulo.getText().equals("DESTINO")) {
            eliminarOrigenDestino(txtNombre.getText());
            JOptionPane.showMessageDialog(null, "El Destino fue eliminado");
            Consulta();
            txtNombre.setText(null);

        } else if (btnAcciones.getText().equals("Eliminar") && lblTitulo.getText().equals("EMPRESA")) {
            eliminarEmpresa(txtNombre.getText());
            JOptionPane.showMessageDialog(null, "La Empresa fue eliminada");
            Consulta();
            txtNombre.setText(null);
//-----------------------------------------------------------------------------------------------------------------------------
        } else {
            JOptionPane.showMessageDialog(null, "Error de entrada");
        }

    }

    public void tomarID() {
        try {
            String instruccion = "select id from OrigenDestino where nombre ='" + oldPass + "'";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(instruccion);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                number = rs.getInt(1);
                System.out.println(number);
            }

            rs.close();

            modificarOrigenDestino(number, txtNombre.getText());
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void tomarIDEm() {

        try {
            String instruccion = "select id from Empresa where nombre ='" + oldPass + "'";
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(instruccion);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                number = rs.getInt(1);
                System.out.println(number);
            }

            rs.close();

            modificarEmpresa(number, txtNombre.getText());

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void Consulta() {

        DefaultTableModel model = (DefaultTableModel) tblNombres.getModel();
        model.setRowCount(0);

        if (lblTitulo.getText().equals("ORIGEN")) {
            sql = "SELECT id, nombre FROM OrigenDestino WHERE tipo = 'ORIGEN' ORDER BY nombre ASC";
        } else if (lblTitulo.getText().equals("DESTINO")) {
            sql = "SELECT id, nombre FROM OrigenDestino WHERE tipo = 'DESTINO' ORDER BY nombre ASC";
        } else {
            sql = "SELECT id, nombre FROM Empresa  ORDER BY nombre ASC";
        }

        try {
            String instruccion = sql;
            Statement stmt = conexion.createStatement();
            stmt.executeQuery(instruccion);
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString(2));

                model.addRow(v);
                tblNombres.setModel(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

//----------------------------------------------------------------------------------------    
    public void insertarOrigenDestino(String nom, String tipo) {
        try {
            String instruccion = "INSERT INTO `OrigenDestino` VALUES (NULL,'" + nom + "','" + tipo + "');";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Se Agreagó :(" + e);
        }
    }

    public void modificarOrigenDestino(int idM, String nombreM) {
        try {

            String instruccion = "UPDATE OrigenDestino Set nombre ='" + nombreM + "' where id =" + idM + " and nombre = @NombreOD";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Se Agreagó :(" + e);
        }
    }

    public void eliminarOrigenDestino(String nom) {
        try {
            String instruccion = "DELETE FROM OrigenDestino where nombre ='" + nom + "'";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Se Agreagó :(" + e);
        }
    }
//----------------------------------------------------------------------------------------

    public void insertarEmpresa(String nombreEmp) {
        try {
            String instruccion = "INSERT INTO `cempresa` VALUES (NULL,'" + nombreEmp + "');";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Se Agreagó :(" + e);
        }
    }

    public void modificarEmpresa(int idEmp, String nombreEmp) {
        try {
            String instruccion = "UPDATE `cempresa` SET `NOMBRE`='" + nombreEmp + "' WHERE `ID`=" + id + ";";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Se Agreagó :(" + e);
        }
    }

    public void eliminarEmpresa(String nomb) {
        try {
            String instruccion = "DELETE FROM `cempresa` WHERE `ID`=" + id + ";";
            PreparedStatement s = conexion.prepareStatement(instruccion);
            s.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No Se Agreagó :(" + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlNombres = new javax.swing.JScrollPane();
        tblNombres = new javax.swing.JTable();
        txtNombre = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        btnAcciones = new javax.swing.JButton();
        lblCerrar = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();
        mnOpciones = new javax.swing.JMenuBar();
        mnAdd = new javax.swing.JMenu();
        mnModificar = new javax.swing.JMenu();
        mnEliminar = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(465, 300));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblNombres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre"
            }
        ));
        tblNombres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNombresMouseClicked(evt);
            }
        });
        pnlNombres.setViewportView(tblNombres);

        getContentPane().add(pnlNombres, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 63, 207, 205));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 121, 204, 36));

        lblNombre.setText("NOMBRE");
        getContentPane().add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 96, 52, -1));

        lblTitulo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 0, 0));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 385, 37));

        btnAcciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccionesActionPerformed(evt);
            }
        });
        getContentPane().add(btnAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 168, 92, 30));

        lblCerrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblCerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/close.png"))); // NOI18N
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblCerrarMouseClicked(evt);
            }
        });
        getContentPane().add(lblCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 0, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/FONDOACCIONES.jpg"))); // NOI18N
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(-200, 0, 670, 280));

        mnAdd.setText("Añadir");
        mnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnAddMouseClicked(evt);
            }
        });
        mnOpciones.add(mnAdd);

        mnModificar.setText("Modificar");
        mnModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnModificarMouseClicked(evt);
            }
        });
        mnOpciones.add(mnModificar);

        mnEliminar.setText("Eliminar");
        mnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnEliminarMouseClicked(evt);
            }
        });
        mnOpciones.add(mnEliminar);

        setJMenuBar(mnOpciones);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnAddMouseClicked
        txtNombre.setText(null);
        btnAcciones.setText("Añadir");
        mostrarComponentes();

        txtNombre.setEnabled(true);
    }//GEN-LAST:event_mnAddMouseClicked

    private void mnModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnModificarMouseClicked
        txtNombre.setText(null);
        btnAcciones.setText("Modificar");
        mostrarComponentes();

        txtNombre.setEnabled(true);
    }//GEN-LAST:event_mnModificarMouseClicked

    private void mnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnEliminarMouseClicked
        txtNombre.setText(null);
        btnAcciones.setText("Eliminar");
        mostrarComponentes();

        txtNombre.setEnabled(false);
    }//GEN-LAST:event_mnEliminarMouseClicked

    private void btnAccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccionesActionPerformed

        if (txtNombre.getText().equals(null) || txtNombre.getText().equals("") || txtNombre.getText().equals(" ")) {
            JOptionPane.showMessageDialog(null, "No se realizó la operación, verifique sus datos");
        } else {
            compararOpciones();
        }

    }//GEN-LAST:event_btnAccionesActionPerformed

    private void lblCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCerrarMouseClicked
        Reporte pri = new Reporte();
        pri.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblCerrarMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Consulta();

    }//GEN-LAST:event_formWindowOpened

    private void tblNombresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNombresMouseClicked
        filaSelect = tblNombres.rowAtPoint(evt.getPoint());
        comprobarAccion();
        oldPass = txtNombre.getText();
    }//GEN-LAST:event_tblNombresMouseClicked

    public void comprobarAccion() {
        if (btnAcciones.getText().equals("Modificar") && lblTitulo.getText().equals("ORIGEN") || btnAcciones.getText().equals("Modificar") && lblTitulo.getText().equals("DESTINO") || btnAcciones.getText().equals("Modificar") && lblTitulo.getText().equals("EMPRESA")) {
            txtNombre.setText(String.valueOf(tblNombres.getValueAt(filaSelect, 0)));

        } else if (btnAcciones.getText().equals("Eliminar") && lblTitulo.getText().equals("ORIGEN") || btnAcciones.getText().equals("Eliminar") && lblTitulo.getText().equals("DESTINO") || btnAcciones.getText().equals("Eliminar") && lblTitulo.getText().equals("EMPRESA")) {
            txtNombre.setText(String.valueOf(tblNombres.getValueAt(filaSelect, 0)));

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OrigenDestino.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrigenDestino.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrigenDestino.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrigenDestino.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrigenDestino().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcciones;
    private javax.swing.JLabel lblCerrar;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JMenu mnAdd;
    private javax.swing.JMenu mnEliminar;
    private javax.swing.JMenu mnModificar;
    private javax.swing.JMenuBar mnOpciones;
    private javax.swing.JScrollPane pnlNombres;
    private javax.swing.JTable tblNombres;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

}
