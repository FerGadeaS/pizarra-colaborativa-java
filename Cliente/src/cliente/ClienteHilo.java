package cliente;

import static cliente.Clientes.conexion;
import static cliente.Clientes.out;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ClienteHilo extends JFrame implements Runnable{
    Conexiones conexion;
    DataInputStream in;
    DataOutputStream out;
    Clientes cl;
    JScrollPane scroll = new JScrollPane();
    JMenuBar menuBar = new JMenuBar();
    JMenuItem guardarDibujo = new JMenuItem();
    JMenu menu3 = new JMenu();

    public ClienteHilo(Conexiones con, DataInputStream in, DataOutputStream out, Clientes cl){
        this.conexion = con;
        this.in = in;
        this.out = out;
        this.cl = cl;
        guardarDibujo.setText("Guardar dibujo");
        guardarDibujo.addActionListener(this::guardarDibujoActionPerformed);
        menuBar.add(menu3);
        menu3.setText("Archivo");
        menu3.add(guardarDibujo);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ejecutar código antes de cerrar el programa
                Salir();

                // Cerrar la ventana
                dispose();
                System.exit(0);
            }
        });
        setJMenuBar(menuBar);
    }
    private void guardarDibujoActionPerformed(java.awt.event.ActionEvent evt) {
        cl.guardarDibujo();
    }
    
    @Override
    public void run(){
        setTitle("Cliente " + Clientes.matricula);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 800);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        scroll.setViewportView(cl);
        setContentPane(scroll);
        while (true) {
            try {
                conexion.RecibePuntos(in);
                conexion.recibeColores(in);
                conexion.recibeGrosor(in);
                conexion.recibeInt(in);
                SwingUtilities.invokeLater(() -> {
                    cl.repaint();       
                });
            } catch (IOException ex) {
                Logger.getLogger(ClienteHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Salir(){
        try {
            conexion.enviarListaDeListasDePuntos(cl.lineas);
            conexion.enviarColores(cl.colores);
            conexion.enviarGrosor(cl.grosorLinea);
            conexion.enviarInt(cl.x, cl.y, cl.textoEsc);
            try {
                out.writeInt(1);
            } catch (IOException ex) {
                Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.close();
            in.close();
            cl.socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}