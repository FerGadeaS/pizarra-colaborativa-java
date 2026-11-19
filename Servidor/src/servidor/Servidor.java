package servidor;

import Conexion.Conexion;
import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class Servidor extends javax.swing.JFrame {
    public static SemaforoBinario con = new SemaforoBinario(1);
    public static SemaforoBinario UsuariosMax = new SemaforoBinario(0);
    private static List<ServerHilo> clientes = new ArrayList<>();
    protected static int TotalCl = 0;
    private static long tiempoInicio;
    private static String bases = "";
    public static Statement sql;
    private static PanelServer panel;
    public static JList<String> Conexiones, usersCon;
    public static DefaultListModel<String> listModel, conectados;
    private JList<String> jList, jconectados;
    public static String nuevoElemento;
    public static int i = 1;
    public static String horasF;
    public Servidor() {
        initComponents();
        panel = new PanelServer();
        new Thread(new Runnable() {
            public void run() {
                Server();
            }
        }).start();
        listModel = new DefaultListModel<>();
        conectados = new DefaultListModel<>();
        Conexiones = new JList<>(listModel);
        usersCon = new JList<>(conectados);
        jScrollPane1.setViewportView(panel);
    }
    
    public void Server(){
        try {
            SQL();
            String[] strings = { "Servidor esperando conexiones..."};
            for (String item : strings) {
                listModel.addElement(item);
            }
            scroll3.setViewportView(Conexiones);
            usersConectados.setViewportView(usersCon);
            int i = 1;
            ServerSocket serverSocket = new ServerSocket(5000);
            try {
                ObtieneUsers();
            } catch (SQLException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            while(TotalCl < 10){
                if(TotalCl == 9){
                    JOptionPane.showMessageDialog(null, "Numero maximo de usuarios alcanzados.");
                    UsuariosMax.WAIT();
                    TotalCl --;
                }
                int valor = 0;
                Socket clientSocket = serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                int caso = in.readInt();
                if(caso == 1){
                    JOptionPane.showMessageDialog(null, "Opcion crear cuenta");
                    String nombreCl = in.readUTF();
                    String password = in.readUTF();
                    try{
                        String consulta = "CALL SP_CrearCuenta('" + nombreCl + "', '" + password + "')";
                        ResultSet resultado = sql.executeQuery(consulta);
                        if(resultado.next()){
                            valor = resultado.getInt(1);
                            System.out.println(valor);
                            actualizarLista();//Agregar nuevo user a la lista de usuarios y decir si está o no conectado
                            if(1 == valor){
                                out.writeUTF("creada");
                            }else{
                                out.writeUTF("La matricula ya existe.");
                                clientSocket.close();
                                out.close();
                                in.close();
                            }
                        }
                    }catch(SQLException ex){
                        JOptionPane.showMessageDialog(null, ex.toString());
                    }
                }
                else{
                    String nombreCl = in.readUTF();
                    String password = in.readUTF();
                    try{
                        tiempoInicio = System.currentTimeMillis();//tipo long
                        String consulta = "CALL SP_InicioSesion('" + nombreCl + "', '" + password + "')";
                        ResultSet resultado = sql.executeQuery(consulta);
                        if(resultado.next()){
                            valor = resultado.getInt(1);
                            if(valor != 0){
                                consulta = "SELECT conectado FROM usuario WHERE ID_cuenta = " + valor +"";
                                resultado = sql.executeQuery(consulta);
                                int Conexionn;
                                if(resultado.next()){
                                    Conexionn = resultado.getInt(1);
                                    if(Conexionn == 0){
                                        out.writeUTF("entras");
                                        consulta = "UPDATE usuario SET conectado =  1 where ID_cuenta = '" + valor + "'";
                                        sql.executeUpdate(consulta);
                                        actualizarLista();
                                        LocalDateTime horario = LocalDateTime.now();
                                        DateTimeFormatter horas = DateTimeFormatter.ofPattern("HH:mm:ss");
                                        DateTimeFormatter dias = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                        horasF = horario.format(horas);
                                        String diasF = horario.format(dias);
                                        consulta = "INSERT INTO datos values ( " + valor + ", '" + horasF + "', '" + diasF +"' )";
                                        sql.executeUpdate(consulta);
                                        ServerHilo hilo = new ServerHilo(clientSocket, in, out, nombreCl, tiempoInicio, horasF, panel, valor);
                                        synchronized (clientes) {
                                                clientes.add(hilo);
                                            }

                                        hilo.start();

                                        nuevoElemento = ("Creada la conexion con el cliente " + nombreCl + " el día " + diasF + " a las " + horasF);
                                        listModel.addElement(nuevoElemento);
                                        Conexiones.setModel(listModel);
                                        i++;
                                        TotalCl ++;
                                    }else{
                                        out.writeUTF("Hay una sesión con esta cuenta iniciada en otro dispositivo.");
                                    }
                                }
                            }else{
                                out.writeUTF("La cuenta o contraseña son incorrectas.");
                            }
                        }
                        nuevoElemento = "Esperando nueva conexion";
                        listModel.addElement(nuevoElemento);
                        Conexiones.setModel(listModel);
                    }catch(SQLException ex){
                        JOptionPane.showMessageDialog(null, ex.toString());
                        clientSocket.close();
                        out.close();
                        in.close();
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    public void Conexiones(){
        listModel = new DefaultListModel<>();
        Conexiones = new JList<>(listModel);
        String[] strings = { };
        for (String item : strings) {
            listModel.addElement(item);
        }
        
        listModel.addElement(nuevoElemento);
        Conexiones.setModel(listModel);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        usersConectados = new javax.swing.JScrollPane();
        scroll3 = new javax.swing.JScrollPane();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        guardarReporte = new javax.swing.JMenuItem();
        guardarDibujo = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");
        setBackground(new java.awt.Color(249, 249, 249));
        setResizable(false);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(162, 195, 206));
        jTextField1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 22)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(27, 99, 140));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Log");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(27, 99, 140));
        jTextField2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(162, 195, 206));
        jTextField2.setText("                                    Pizarra");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(162, 195, 206));
        jTextField3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(27, 99, 140));
        jTextField3.setText("                   Usuarios");

        jMenu3.setText("Archivo");

        guardarReporte.setText("Guardar reporte de usuarios");
        guardarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarReporteActionPerformed(evt);
            }
        });
        jMenu3.add(guardarReporte);

        guardarDibujo.setText("Guardar dibujo");
        guardarDibujo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarDibujoActionPerformed(evt);
            }
        });
        jMenu3.add(guardarDibujo);

        jMenuBar3.add(jMenu3);

        setJMenuBar(jMenuBar3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scroll3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usersConectados)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addComponent(jTextField3)
                    .addComponent(jTextField2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usersConectados, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
                    .addComponent(scroll3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarReporteActionPerformed
        try {
            // TODO add your handling code here:
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("HH_mm_ss");
            LocalDateTime HoraFin= LocalDateTime.now();
            String hora = HoraFin.format(formatoFecha);//Hora de salida
            generarReporte("ReporteUsuarios"+hora+".txt");
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_guardarReporteActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void guardarDibujoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarDibujoActionPerformed
        // TODO add your handling code here:
        panel.guardarDibujo();
    }//GEN-LAST:event_guardarDibujoActionPerformed

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
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Servidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Servidor ser = new Servidor();
                //ser.setExtendedState(MAXIMIZED_BOTH);
                ser.setVisible(true);
            }
        });
    }
    
    public static void SQL(){
        try{
                sql = Conexion.getConnection().createStatement();
                
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null, ex.toString());
            }
    }
    
    public void ObtieneUsers() throws SQLException{
        String consulta = "SELECT matricula, conectado FROM usuario";
        ResultSet resultado = sql.executeQuery(consulta);
        while(resultado.next()){
            String resu = resultado.getString(1);
            int conex = resultado.getInt(2);
            if(conex == 0){
                nuevoElemento = "Usuario " +i + ". " + resu + " Desconectado";
            }else{
                nuevoElemento = "Usuario " +i + ". " + resu + " Conectado";
            }
            conectados.addElement(nuevoElemento);
            usersCon.setModel(conectados);
            i++;
        }
    }
    
    private static void cargarDatos() {
        try {
            String consulta = "SELECT matricula, conectado FROM usuario";
            ResultSet resultado = sql.executeQuery(consulta);
            
            try {
                while (resultado.next()) {
                    String resu = resultado.getString(1);
                    int conex = resultado.getInt(2);
                    String nuevo;
                    
                    if (conex == 0) {
                        nuevo = "Usuario " + i + ". " + resu + " Desconectado";
                    } else {
                        nuevo = "Usuario " + i + ". " + resu + " Conectado";
                    }
                    
                    conectados.addElement(nuevo);
                    i++;
                }
            } catch (SQLException ex) { } finally {
                try {
                    resultado.close();
                } catch (SQLException ex) { }
            }
            
            usersCon.setModel(conectados);
            usersCon.revalidate();
            usersCon.repaint();
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }
    
    protected static void actualizarLista() {
        SwingUtilities.invokeLater(() -> {
            conectados.clear();
            i = 1;
            cargarDatos();
        });
    }

    public static int modo(DataInputStream in) throws IOException{
        return in.readInt();
    }

    public static void enviarListaDeListasDePuntos(DataOutputStream out, List<List<Point>> puntos, ServerHilo Rem) {

        List<ServerHilo> copiaClientes;

        synchronized (clientes) {
            copiaClientes = new ArrayList<>(clientes);
        }

        for (ServerHilo serverh : copiaClientes) {
            if (serverh != Rem) {
                serverh.enviarListaDeListasDePuntos(puntos);
            }
        }
    }
    
    public static void enviarInt(DataOutputStream out, List<Integer> x, List<Integer> y, List<String> text, ServerHilo Rem) throws IOException {
        for(ServerHilo serverh : clientes){
            if(serverh != Rem){
                serverh.enviarInt(x, y, text);
            }
        }
    }

    public static void enviarColoresL(DataOutputStream out, List<Color> colores, ServerHilo rem) throws IOException{
        for(ServerHilo serverh : clientes){
            if(serverh != rem){
                serverh.enviarColores(colores);
            }
        }
    }

    public static void enviarGrosorL(DataOutputStream out, List<Float> grosor, ServerHilo rem) throws IOException{
        for(ServerHilo serverh : clientes){
            if(serverh != rem){
                serverh.enviarGrosor(grosor);
            }
        }
    }

    public static void eliminarCliente(ServerHilo cliente) {
        synchronized (clientes) {
            clientes.remove(cliente);
        }
    }
    
    public static void generarReporte(String nombreArchivo) throws SQLException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo))) {
            List<Integer> id = new ArrayList<>(), conectado = new ArrayList<>();
            List<String> matricula = new ArrayList<>();
            escribirCabecera(escritor);
            String consulta1 = "SELECT ID_cuenta, conectado, matricula FROM usuario";
            ResultSet resultado1, resultado;
            resultado1 = sql.executeQuery(consulta1);
            System.out.println(resultado1);
            while (resultado1.next()) {
                 id.add(resultado1.getInt(1));
                 conectado.add(resultado1.getInt(2));
                 matricula.add(resultado1.getString(3));
            }
            for(int i = 0; i < id.size(); i++){                    
                if (conectado.get(i) == 0) {
                    String linea = generarDatos(id.get(i), matricula.get(i));
                    escritor.write(linea);
                    escritor.newLine();
                } else {
                    String consulta = "SELECT fecha, hora FROM datos where id = " + id.get(i);
                    resultado = sql.executeQuery(consulta);
                    if(resultado.next()){
                        String fecha = resultado.getString(1);
                        String hora = resultado.getString(2);
                        String linea = "Fecha de entrada: " + fecha + ", hora de entrada: " + hora + ", duracion en pizarra: aun conectado,"
                                + " hora de salida: aun conectado, fecha de salida: aun conectado";
                    escritor.write(linea);
                    escritor.newLine();
                    }
                }
           }
            JOptionPane.showMessageDialog(null, "Reporte generado con éxito: " + nombreArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void escribirCabecera(BufferedWriter escritor) throws IOException {
        escritor.write("Aplicación: Pizarra colaborativa BUAP");
        escritor.newLine();
        escritor.write("Título: Reporte estadístico de usuarios");
        escritor.newLine();
        escritor.newLine(); // Línea en blanco antes de los datos
    }
    
    public static String generarDatos(int id, String matricula) throws SQLException {
        String consulta = "SELECT TOP 1 fecha_ingreso, hora_entrada, duracion_en_pizarra, hora_salida, fecha_salida "
                + "FROM conexion where id_user = " + id + " ORDER BY id_conexion DESC";
        ResultSet resultado = sql.executeQuery(consulta);
        String fechaIn = "", horaIn = "", duracion = "", hora_salida = "", fechaout = "";
        while(resultado.next()){
            fechaIn = resultado.getString(1);
            horaIn = resultado.getString(2);
            duracion = resultado.getString(3);
            hora_salida = resultado.getString(4);
            fechaout = resultado.getString(5);
        }
        return "Usuario: " + matricula + ": fecha de entrada: " + fechaIn + ", hora de entrada: " + horaIn + ", duracion en pizarra: " 
                + duracion + " hora de salida: " + hora_salida + " fecha de salida: " + fechaout;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem guardarDibujo;
    private javax.swing.JMenuItem guardarReporte;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JScrollPane scroll3;
    private javax.swing.JScrollPane usersConectados;
    // End of variables declaration//GEN-END:variables
}
