package vistas;

import modelo.conexion.Conexion;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import modelo.dao.ReporteDAO;
import modelo.dto.ReporteDTO;

public class Reporte extends javax.swing.JFrame {

    private Connection conexion = Conexion.getConnection();
    String sql;
    private int idActual;

    private ReporteDTO reporteDTO;
    private ReporteDAO reporteDAO = new ReporteDAO();

    Date hoy = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

    static public String OpcionTitulo;
    FondoPanel fondo = new FondoPanel();

    public Reporte() {
        this.setContentPane(fondo);
        initComponents();

        tblAdministracion.getColumnModel().getColumn(0).setMaxWidth(0);
        tblAdministracion.getColumnModel().getColumn(0).setMinWidth(0);
        tblAdministracion.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        tblAdministracion.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        llenarComboDestinos();
        llenarComboOrigenes();
        llenarComboEmpresas();
        llenarTabla();
    }

    public void llenarTabla() {
        DefaultTableModel model = (DefaultTableModel) tblAdministracion.getModel();
        model.setRowCount(0);
        try {
            ResultSet rs = reporteDAO.llenarTabla();
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("ID"));
                v.add(rs.getString("NUMERO"));
                v.add(rs.getString("HORA_SALIDA"));
                v.add(rs.getString("ORIGEN"));
                v.add(rs.getString("DESTINO"));
                v.add(rs.getString("EMPRESA"));
                v.add(rs.getString("TIPO_SERVICIO"));
                v.add(rs.getString("TIPO_CORRIDA"));
                v.add(rs.getString("NUMERO_ECONOMICO"));
                v.add(rs.getString("NUMERO_PASAJEROS"));
                v.add(rs.getString("NUMERO_SALIDA"));
                v.add(rs.getString("FECHA"));
                model.addRow(v);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        tblAdministracion.setModel(model);
    }

    public void llenarComboOrigenes() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            ResultSet rs = reporteDAO.llenarComboOrigenes();
            while (rs.next()) {
                model.addElement(rs.getString(1));
            }
            rs.close();
        } catch (Exception e) {
            System.out.print(e);
        }
        cmbOrigen.setModel(model);
    }

    public void llenarComboDestinos() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            ResultSet rs = reporteDAO.llenarComboDestinos();
            while (rs.next()) {
                model.addElement(rs.getString(1));
            }
            rs.close();
        } catch (Exception e) {
            System.out.print(e);
        }
        cmbDestino.setModel(model);
    }

    public void llenarComboEmpresas() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            ResultSet rs = reporteDAO.llenarComboEmpresas();
            while (rs.next()) {
                model.addElement(rs.getString(1));
            }
            rs.close();
        } catch (Exception e) {
            System.out.print(e);
        }
        cmbEmpresa.setModel(model);
    }

    public void actualizarDatos() {

    }

    public void Limpiar() {
        calAdministracion.setDate(null);
        calAdministracion.setToolTipText(null);
        txtHora.setText(null);
        txtMinutos.setText(null);
        txtNoeconomico.setText(null);
        txtNopasajeros.setText(null);
        txtNosalida.setText(null);
    }

    public void validarCampos() {
        String mensaje = "";
        Matcher matcher = null;
        Pattern digitosPattern = Pattern.compile("^[0-9]+$");

        String numEconomico = txtNoeconomico.getText();
        matcher = digitosPattern.matcher(numEconomico);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this, "Solo se pueden introducir números");
        }
        String numPasajeros = txtNopasajeros.getText();
        matcher = digitosPattern.matcher(numPasajeros);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this, "Solo se pueden introducir números");
        }
        String numSalida = txtNosalida.getText();
        matcher = digitosPattern.matcher(numSalida);
        if (!matcher.matches()) {
            JOptionPane.showMessageDialog(this, "Solo se pueden introducir números");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdministracion = new javax.swing.JTable();
        calAdministracion = new com.toedter.calendar.JDateChooser();
        lblFecha = new javax.swing.JLabel();
        lblAdministracion = new javax.swing.JLabel();
        lblNosalida = new javax.swing.JLabel();
        txtMinutos = new javax.swing.JTextField();
        txtHora = new javax.swing.JTextField();
        lblAddempresa = new javax.swing.JLabel();
        lblOrigen = new javax.swing.JLabel();
        lblHorasalida = new javax.swing.JLabel();
        lblEmpresa = new javax.swing.JLabel();
        lblDestino = new javax.swing.JLabel();
        lblTipocorrida = new javax.swing.JLabel();
        lblTiposervicio = new javax.swing.JLabel();
        lblNopasajeros = new javax.swing.JLabel();
        lblNoeconomico = new javax.swing.JLabel();
        cmbTipocorrida = new javax.swing.JComboBox<>();
        cmbDestino = new javax.swing.JComboBox<>();
        cmbOrigen = new javax.swing.JComboBox<>();
        cmbTiposervicio = new javax.swing.JComboBox<>();
        cmbEmpresa = new javax.swing.JComboBox<>();
        txtNosalida = new javax.swing.JTextField();
        txtNopasajeros = new javax.swing.JTextField();
        txtNoeconomico = new javax.swing.JTextField();
        lblAddestino = new javax.swing.JLabel();
        lblAddorigen = new javax.swing.JLabel();
        lblDospuntos = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        lblArchivos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblAdministracion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "No.", "Hora Salida", "Origen", "Destino", "Empresa", "Tipo Servicio", "Tipo Corrida", "No. Eco. Autobus", "No. Pasajeros", "No. Salida", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAdministracion.setAlignmentX(0.0F);
        tblAdministracion.setAlignmentY(0.0F);
        tblAdministracion.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblAdministracion.getTableHeader().setReorderingAllowed(false);
        tblAdministracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdministracionMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAdministracion);
        if (tblAdministracion.getColumnModel().getColumnCount() > 0) {
            tblAdministracion.getColumnModel().getColumn(0).setResizable(false);
            tblAdministracion.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblAdministracion.getColumnModel().getColumn(1).setResizable(false);
            tblAdministracion.getColumnModel().getColumn(2).setResizable(false);
            tblAdministracion.getColumnModel().getColumn(3).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 1000, 450));

        calAdministracion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(calAdministracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 150, 20));

        lblFecha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblFecha.setText("Fecha");
        getContentPane().add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 90, 20));

        lblAdministracion.setFont(new java.awt.Font("Verdana", 1, 42)); // NOI18N
        lblAdministracion.setForeground(new java.awt.Color(255, 0, 0));
        lblAdministracion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAdministracion.setText("CENTRAL DE AUTOBUSES TULANCINGO S.A. DE C.V");
        lblAdministracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAdministracionMouseClicked(evt);
            }
        });
        getContentPane().add(lblAdministracion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1370, 90));

        lblNosalida.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNosalida.setText("No. Salida");
        getContentPane().add(lblNosalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, -1, -1));

        txtMinutos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMinutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMinutosActionPerformed(evt);
            }
        });
        getContentPane().add(txtMinutos, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, 50, -1));

        txtHora.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoraActionPerformed(evt);
            }
        });
        getContentPane().add(txtHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 50, -1));

        lblAddempresa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAddempresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddempresa.setText("+");
        lblAddempresa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAddempresaMouseClicked(evt);
            }
        });
        getContentPane().add(lblAddempresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, 30, 20));

        lblOrigen.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblOrigen.setText("Origen");
        getContentPane().add(lblOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 90, -1));

        lblHorasalida.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblHorasalida.setText("Hora Salida");
        getContentPane().add(lblHorasalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        lblEmpresa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblEmpresa.setText("Empresa");
        getContentPane().add(lblEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 90, -1));

        lblDestino.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDestino.setText("Destino");
        getContentPane().add(lblDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 90, -1));

        lblTipocorrida.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTipocorrida.setText("Tipo Corrida");
        getContentPane().add(lblTipocorrida, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, -1, -1));

        lblTiposervicio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblTiposervicio.setText("Tipo Servicio");
        getContentPane().add(lblTiposervicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        lblNopasajeros.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNopasajeros.setText("No. Pasajeros");
        getContentPane().add(lblNopasajeros, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, -1, -1));

        lblNoeconomico.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblNoeconomico.setText("No. Economico");
        getContentPane().add(lblNoeconomico, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, -1, -1));

        cmbTipocorrida.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTipocorrida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L", "P", "V" }));
        getContentPane().add(cmbTipocorrida, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 150, -1));

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(cmbDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 150, -1));

        cmbOrigen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(cmbOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 150, -1));

        cmbTiposervicio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbTiposervicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PRIMERA", "SEGUNDA" }));
        cmbTiposervicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTiposervicioActionPerformed(evt);
            }
        });
        getContentPane().add(cmbTiposervicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 150, -1));

        cmbEmpresa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(cmbEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 150, -1));

        txtNosalida.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtNosalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 490, 130, -1));

        txtNopasajeros.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtNopasajeros, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 130, -1));

        txtNoeconomico.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(txtNoeconomico, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 410, 130, -1));

        lblAddestino.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAddestino.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddestino.setText("+");
        lblAddestino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAddestinoMouseClicked(evt);
            }
        });
        getContentPane().add(lblAddestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 250, 30, 20));

        lblAddorigen.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAddorigen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddorigen.setText("+");
        lblAddorigen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAddorigenMouseClicked(evt);
            }
        });
        getContentPane().add(lblAddorigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 30, 20));

        lblDospuntos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDospuntos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDospuntos.setText(":");
        getContentPane().add(lblDospuntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 50, 20));

        btnModificar.setBackground(new java.awt.Color(255, 255, 102));
        btnModificar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 610, -1, -1));

        btnEliminar.setBackground(new java.awt.Color(255, 102, 102));
        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 610, -1, -1));

        btnAdd.setBackground(new java.awt.Color(102, 255, 102));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAdd.setText("Añadir");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 610, -1, -1));

        lblArchivos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblArchivos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagen/ExcelCentral.png"))); // NOI18N
        lblArchivos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblArchivosMouseClicked(evt);
            }
        });
        getContentPane().add(lblArchivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 590, 970, 110));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMinutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinutosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMinutosActionPerformed

    private void txtHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraActionPerformed

    private void lblAdministracionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAdministracionMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblAdministracionMouseClicked

    private void lblArchivosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblArchivosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblArchivosMouseClicked

    private void lblAddorigenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAddorigenMouseClicked
        OpcionTitulo = "1";
        OrigenDestino od = new OrigenDestino();
        od.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblAddorigenMouseClicked

    private void lblAddestinoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAddestinoMouseClicked
        OpcionTitulo = "2";
        OrigenDestino od = new OrigenDestino();
        od.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblAddestinoMouseClicked

    private void lblAddempresaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAddempresaMouseClicked
        OpcionTitulo = "3";
        OrigenDestino od = new OrigenDestino();
        od.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblAddempresaMouseClicked

    private void cmbTiposervicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTiposervicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTiposervicioActionPerformed

    private void tblAdministracionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdministracionMouseClicked
        int index = tblAdministracion.getSelectedRow();
        idActual = Integer.parseInt(tblAdministracion.getValueAt(index, 0).toString());
        txtHora.setText(String.valueOf(tblAdministracion.getValueAt(index, 2)).substring(0, 2));
        txtMinutos.setText(String.valueOf(tblAdministracion.getValueAt(index, 2)).substring(3, 5));
        cmbOrigen.setSelectedItem(String.valueOf(tblAdministracion.getValueAt(index, 3)));
        cmbDestino.setSelectedItem(String.valueOf(tblAdministracion.getValueAt(index, 4)));
        cmbEmpresa.setSelectedItem(String.valueOf(tblAdministracion.getValueAt(index, 5)));
        cmbTiposervicio.setSelectedItem(String.valueOf(tblAdministracion.getValueAt(index, 6)));
        cmbTipocorrida.setSelectedItem(String.valueOf(tblAdministracion.getValueAt(index, 7)));
        txtNoeconomico.setText(String.valueOf(tblAdministracion.getValueAt(index, 8)));
        txtNopasajeros.setText(String.valueOf(tblAdministracion.getValueAt(index, 9)));
        txtNosalida.setText(String.valueOf(tblAdministracion.getValueAt(index, 10)));
    }//GEN-LAST:event_tblAdministracionMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        validarCampos();
        Date date = calAdministracion.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaSeleccionada = String.valueOf(sdf.format(date));

        reporteDTO = new ReporteDTO();
        reporteDTO.setFecha(fechaSeleccionada);
        reporteDTO.setHoraSalida(txtHora.getText() + ":" + txtMinutos.getText());
        reporteDTO.setOrigen(cmbOrigen.getSelectedItem().toString());
        reporteDTO.setDestino(cmbDestino.getSelectedItem().toString());
        reporteDTO.setEmpresa(cmbEmpresa.getSelectedItem().toString());
        reporteDTO.setTipoServicio(cmbTiposervicio.getSelectedItem().toString());
        reporteDTO.setTipoCorrida(cmbTipocorrida.getSelectedItem().toString());
        reporteDTO.setNumeroEconomico(Integer.parseInt(txtNoeconomico.getText()));
        reporteDTO.setNumeroPasajeros(Integer.parseInt(txtNopasajeros.getText()));
        reporteDTO.setNumeroSalida(Integer.parseInt(txtNosalida.getText()));

        reporteDAO.insertar(reporteDTO);
        llenarTabla();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        reporteDTO = new ReporteDTO();
        reporteDTO.setId(idActual);
        reporteDTO.setHoraSalida(txtHora.getText() + ":" + txtMinutos.getText());
        reporteDTO.setOrigen(cmbOrigen.getSelectedItem().toString());
        reporteDTO.setDestino(cmbDestino.getSelectedItem().toString());
        reporteDTO.setEmpresa(cmbEmpresa.getSelectedItem().toString());
        reporteDTO.setTipoServicio(cmbTiposervicio.getSelectedItem().toString());
        reporteDTO.setTipoCorrida(cmbTipocorrida.getSelectedItem().toString());
        reporteDTO.setNumeroEconomico(Integer.parseInt(String.valueOf(txtNoeconomico.getText())));
        reporteDTO.setNumeroPasajeros(Integer.parseInt(String.valueOf(txtNopasajeros.getText())));
        reporteDTO.setNumeroSalida(Integer.parseInt(String.valueOf(txtNosalida.getText())));

        reporteDAO.actualizar(reporteDTO);
        llenarTabla();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        reporteDAO.eliminar(idActual);
        llenarTabla();
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reporte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnEliminar;
    public javax.swing.JButton btnModificar;
    public com.toedter.calendar.JDateChooser calAdministracion;
    public javax.swing.JComboBox<String> cmbDestino;
    public javax.swing.JComboBox<String> cmbEmpresa;
    public javax.swing.JComboBox<String> cmbOrigen;
    public javax.swing.JComboBox<String> cmbTipocorrida;
    public javax.swing.JComboBox<String> cmbTiposervicio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddempresa;
    private javax.swing.JLabel lblAddestino;
    private javax.swing.JLabel lblAddorigen;
    private javax.swing.JLabel lblAdministracion;
    private javax.swing.JLabel lblArchivos;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JLabel lblDospuntos;
    private javax.swing.JLabel lblEmpresa;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblHorasalida;
    private javax.swing.JLabel lblNoeconomico;
    private javax.swing.JLabel lblNopasajeros;
    private javax.swing.JLabel lblNosalida;
    private javax.swing.JLabel lblOrigen;
    private javax.swing.JLabel lblTipocorrida;
    private javax.swing.JLabel lblTiposervicio;
    public javax.swing.JTable tblAdministracion;
    public javax.swing.JTextField txtHora;
    public javax.swing.JTextField txtMinutos;
    public javax.swing.JTextField txtNoeconomico;
    public javax.swing.JTextField txtNopasajeros;
    public javax.swing.JTextField txtNosalida;
    // End of variables declaration//GEN-END:variables

    class FondoPanel extends JPanel {

        private Image ima;

        @Override
        public void paint(Graphics g) {
            ima = new ImageIcon(getClass().getResource("/Imagen/FONDO PRINCIPAL.jpg")).getImage();

            g.drawImage(ima, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
            super.paint(g);
            setResizable(false);
        }
    }
}
