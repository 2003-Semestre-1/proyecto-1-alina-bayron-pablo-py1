/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Visual;

import Controler.Controller;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author Bayron
 */
public class InterfaceConfiguracion extends javax.swing.JFrame {

    public InterfaceConfiguracion() {
        initComponents();
        setTitle("Configuración");
        this.getContentPane().setBackground(new Color(225, 255, 255));
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    public static void informar(){
        JOptionPane.showMessageDialog(null, "Se ha cambiado el tamaño de memoria correctamente.", "Mensaje de afirmación", JOptionPane.INFORMATION_MESSAGE);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_principal = new javax.swing.JTextField();
        txt_virtual = new javax.swing.JTextField();
        txt_unidad = new javax.swing.JTextField();
        btn_guardarConfiguracion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Configuración");

        jLabel2.setText("Tamaño memoria principal :");

        jLabel3.setText("Tamaño memoria virtual :");

        jLabel4.setText("Tamaño unidad de Almacenamiento :");

        txt_principal.setText("256");
        txt_principal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_principalActionPerformed(evt);
            }
        });

        txt_virtual.setText("64");
        txt_virtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_virtualActionPerformed(evt);
            }
        });

        txt_unidad.setText("512");
        txt_unidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_unidadActionPerformed(evt);
            }
        });

        btn_guardarConfiguracion.setBackground(new java.awt.Color(255, 255, 255));
        btn_guardarConfiguracion.setForeground(new java.awt.Color(0, 0, 0));
        btn_guardarConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/guardar.png"))); // NOI18N
        btn_guardarConfiguracion.setText("Guardar tamaños");
        btn_guardarConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardarConfiguracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_principal, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_virtual, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(btn_guardarConfiguracion)
                        .addGap(15, 15, 15))))
            .addGroup(layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(jLabel1)
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txt_principal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txt_virtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_guardarConfiguracion)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_principalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_principalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_principalActionPerformed

    private void txt_virtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_virtualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_virtualActionPerformed

    private void txt_unidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_unidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_unidadActionPerformed

    private void btn_guardarConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardarConfiguracionActionPerformed
        dispose();
        int principal = Integer.parseInt(txt_principal.getText());
        int virtual = Integer.parseInt(txt_virtual.getText());
        int unidad = Integer.parseInt(txt_unidad.getText());
        Controller.guardarTamaniosMemoria(principal, virtual, unidad);
    }//GEN-LAST:event_btn_guardarConfiguracionActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_guardarConfiguracion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JTextField txt_principal;
    public static javax.swing.JTextField txt_unidad;
    public static javax.swing.JTextField txt_virtual;
    // End of variables declaration//GEN-END:variables
}
