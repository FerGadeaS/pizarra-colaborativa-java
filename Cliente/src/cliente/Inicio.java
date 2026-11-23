package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Inicio extends javax.swing.JFrame {

    public Inicio() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bienvenidos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelSesion = new javax.swing.JPanel();
        matricula = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        CrearCuenta = new javax.swing.JButton();
        Amat = new javax.swing.JTextField();
        Apass = new javax.swing.JPasswordField();
        inSes1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);

        bienvenidos.setBackground(new java.awt.Color(120, 164, 189));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("High Tower Text", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(27, 100, 141));
        jLabel1.setText("BIENVENIDOS A LA PIZARRA");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("High Tower Text", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(27, 100, 141));
        jLabel2.setText("DIGITAL COLABORATIVA BUAP");

        javax.swing.GroupLayout bienvenidosLayout = new javax.swing.GroupLayout(bienvenidos);
        bienvenidos.setLayout(bienvenidosLayout);
        bienvenidosLayout.setHorizontalGroup(
            bienvenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bienvenidosLayout.createSequentialGroup()
                .addGroup(bienvenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bienvenidosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(bienvenidosLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel2)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        bienvenidosLayout.setVerticalGroup(
            bienvenidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bienvenidosLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        panelSesion.setBackground(new java.awt.Color(120, 164, 189));

        matricula.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        matricula.setForeground(new java.awt.Color(27, 100, 141));
        matricula.setText("Matricula:");

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(27, 100, 141));
        jLabel4.setText("Contraseña:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));

        CrearCuenta.setBackground(new java.awt.Color(63, 82, 82));
        CrearCuenta.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        CrearCuenta.setForeground(new java.awt.Color(255, 255, 255));
        CrearCuenta.setText("Registrarme");
        CrearCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearCuentaActionPerformed(evt);
            }
        });

        inSes1.setBackground(new java.awt.Color(63, 82, 82));
        inSes1.setFont(new java.awt.Font("Arial Narrow", 1, 18)); // NOI18N
        inSes1.setForeground(new java.awt.Color(255, 255, 255));
        inSes1.setText("Iniciar sesión");
        inSes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inSes1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSesionLayout = new javax.swing.GroupLayout(panelSesion);
        panelSesion.setLayout(panelSesionLayout);
        panelSesionLayout.setHorizontalGroup(
            panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSesionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(65, 65, 65)
                .addGroup(panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSesionLayout.createSequentialGroup()
                        .addComponent(matricula, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63))
                    .addGroup(panelSesionLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)))
                .addGroup(panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSesionLayout.createSequentialGroup()
                        .addComponent(Amat, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Apass, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(140, 140, 140))
            .addGroup(panelSesionLayout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(CrearCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSesionLayout.createSequentialGroup()
                    .addGap(251, 251, 251)
                    .addComponent(inSes1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(385, Short.MAX_VALUE)))
        );
        panelSesionLayout.setVerticalGroup(
            panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSesionLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(matricula)
                    .addComponent(jLabel3)
                    .addComponent(Amat, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSesionLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel5))
                    .addComponent(Apass, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(CrearCuenta)
                .addGap(18, 18, 18))
            .addGroup(panelSesionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSesionLayout.createSequentialGroup()
                    .addContainerGap(114, Short.MAX_VALUE)
                    .addComponent(inSes1)
                    .addGap(56, 56, 56)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bienvenidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 744, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bienvenidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSesion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CrearCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearCuentaActionPerformed
        if (!Amat.getText().isEmpty() && Apass.getPassword().length > 0) {
            Clientes.matricula = Amat.getText();
            Clientes.password = new String(Apass.getPassword());
            Clientes.crearCuenta = true;
            Clientes.cuenta.SIGNAL();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Para crear una cuenta, primero ingrese su matrícula y contraseña en los campos correspondientes.",
                "Crear cuenta",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

    }//GEN-LAST:event_CrearCuentaActionPerformed

    private void inSes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inSes1ActionPerformed
        if (!Amat.getText().isEmpty() && Apass.getPassword().length > 0) {
            Clientes.matricula = Amat.getText();
            Clientes.password = new String(Apass.getPassword());
            Clientes.crearCuenta = false;
            Clientes.cuenta.SIGNAL();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Para crear una cuenta, primero ingrese su matrícula y contraseña en los campos correspondientes.",
                "Crear cuenta",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

    }//GEN-LAST:event_inSes1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Amat;
    private javax.swing.JPasswordField Apass;
    private javax.swing.JButton CrearCuenta;
    private javax.swing.JPanel bienvenidos;
    private javax.swing.JButton inSes1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel matricula;
    private javax.swing.JPanel panelSesion;
    // End of variables declaration//GEN-END:variables
}
