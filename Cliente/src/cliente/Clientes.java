package cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class Clientes extends JPanel{//Cliente
    //Inicio de sesion
    protected static SemaforoBinario cuenta = new SemaforoBinario(0);
    protected static SemaforoBinario iniciado = new SemaforoBinario(1);
    public static String matricula;
    public static String password;
    //Componentes de los sockets
    public static DataInputStream in; //Entrada de datos
    public static DataOutputStream out; //Salida de datos
    public static Conexiones conexion; //Clase conexion
    public String nombre; //nombre del cliente
    public static Socket socket; //Socket para conexion
    // Constantes para el tamaño del ícono y el grosor de la línea.
    private static final int iconoBoton = 40; //Tmaño de iconos
    private float grosor = 3; //Grosor inicial de las lineas
    //Listas para almacenar la información de dibujo./
    public static List<List<Point>> lineas = new ArrayList<>(); //lineas dibujadas por usuario actual
    public List<List<Point>> lineasRec = new ArrayList<>(); ///Lineas recibidas de usuarios
    public static List<Integer> x = new ArrayList<>(); //Posicion x del texto escrito
    public static List<Integer> y = new ArrayList<>(); //Posicion y del texto escrito
    public static List<String> textoEsc = new ArrayList<>(); //Texto escrito
    public List<Integer> xRec = new ArrayList<>(); //Posicion x del texto escrito
    public List<Integer> yRec = new ArrayList<>(); //Posicion y del texto escrito
    public List<String> textoEscRec = new ArrayList<>(); //Texto escrito
    private List<Point> puntos = new ArrayList<>();
    public static List<Color> colores = new ArrayList<>(); //Color de la linea
    public List<Color> coloresRec = new ArrayList<>(); //Color de la linea recibida
    public static List<Float> grosorLinea = new ArrayList<>(); //Grosor de linea
    public List<Float> grosorLineaRec = new ArrayList<>(); //Grosor de linea
    
    private boolean dibujando = false;
    //Componentes del dibujo
    private JPanel choice = new JPanel();
    private JButton dibujar, texto, borrar, tamanno, color;
    private ImageIcon IMGdibujar, IMGtexto, IMGborrar, IMGtamanno, IMGcolor;
    private Image IMGdibujar1, IMGtexto1, IMGborrar1, IMGtamanno1, IMGcolor1;
    private Color colorActual = Color.BLACK;
    private String modoActual = "dibujar";
    private int k;
    private JTextArea cuadroTexto = new JTextArea();

    private boolean ListenSDibujar = true;
    //Mouse listeners
    private MouseAdapter mousePressListener;
    //private MouseAdapter mouseReleaseListener;
    private MouseAdapter mouseDraggedListener;
    public int opcion;
    private Inicio ini;
    
    public static boolean crearCuenta = false;
    public static boolean correcto = false;

    public Clientes() {
        ini = new Inicio();
        setPreferredSize(new Dimension(1500, 1500));
        ini.setTitle("Bienvenidos");
        ini.setVisible(true);
        
        //Creando conexion con Server
        
        //Inicializando componentes del frame
        Iniciar();

        //Agregando componentes al Panel
        choice.add(dibujar);
        choice.add(texto);
        choice.add(borrar);
        choice.add(tamanno);
        choice.add(color);
        choice.setBounds(200, 50, 400, 100);
        //Agregando panel al frame
        add(choice);

        //Agregando listeners a los botones
        Listeners();
        try {
            do{
                cuenta.WAIT();
                socket = new Socket("localhost", 5000);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                conexion = new Conexiones(out, in, this);
                if(crearCuenta == true){
                    out.writeInt(1);
                    out.writeUTF(matricula);
                    out.writeUTF(password);
                    String mensaje = in.readUTF();

                    if(mensaje.equals("creada")){
                        JOptionPane.showMessageDialog(
                            ini,
                            "La cuenta ha sido creada exitosamente.",
                            "Cuenta creada",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }else{
                        JOptionPane.showMessageDialog(
                            ini,
                            mensaje,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        socket.close();
                    }
                }else{
                    out.writeInt(0);
                    out.writeUTF(matricula);
                    out.writeUTF(password);

                    String mensaje = in.readUTF();

                    if(mensaje.equals("entras")){
                        ini.setVisible(false);
                        ClienteHilo hilo = new ClienteHilo(conexion, in, out, this);
                        Thread tr = new Thread(hilo);
                        tr.start();
                        tr.join();
                        opcion = 1;
                        correcto = true;
                    }else{
                        JOptionPane.showMessageDialog(
                            ini,
                            mensaje,
                            "Error de inicio de sesión",
                            JOptionPane.ERROR_MESSAGE
                        );
                        socket.close();
                    }
                }
            }while(correcto == false );

        } catch (IOException ex) {} catch (InterruptedException ex) {
            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Metodo para inicializar los componentes del FRAME
    public void Iniciar(){
        IMGdibujar = new ImageIcon(getClass().getResource("/Imagenes/dibujarimg.png"));
        IMGtexto = new ImageIcon(getClass().getResource("/Imagenes/textoImg.png"));
        IMGborrar = new ImageIcon(getClass().getResource("/Imagenes/gomaImg.png"));
        IMGtamanno = new ImageIcon(getClass().getResource("/Imagenes/grosorImg.png"));
        IMGcolor = new ImageIcon(getClass().getResource("/Imagenes/colorImg.png"));

        IMGdibujar1 = IMGdibujar.getImage().getScaledInstance(iconoBoton, iconoBoton, Image.SCALE_SMOOTH);
        IMGtexto1 = IMGtexto.getImage().getScaledInstance(iconoBoton, iconoBoton, Image.SCALE_SMOOTH);
        IMGborrar1 = IMGborrar.getImage().getScaledInstance(iconoBoton, iconoBoton, Image.SCALE_SMOOTH);
        IMGtamanno1 = IMGtamanno.getImage().getScaledInstance(iconoBoton, iconoBoton, Image.SCALE_SMOOTH);
        IMGcolor1 = IMGcolor.getImage().getScaledInstance(iconoBoton, iconoBoton, Image.SCALE_SMOOTH);
        IMGdibujar = new ImageIcon(IMGdibujar1);
        IMGtexto = new ImageIcon(IMGtexto1);
        IMGborrar = new ImageIcon(IMGborrar1);
        IMGtamanno = new ImageIcon(IMGtamanno1);
        IMGcolor = new ImageIcon(IMGcolor1);

        dibujar = new JButton("Dibujar", IMGdibujar);
        dibujar.setToolTipText("Presione para cambiar a modo dibujo.");
        texto = new JButton("Texto", IMGtexto);
        texto.setToolTipText("Presione para cambiar a modo escritura.");
        borrar = new JButton("Goma", IMGborrar);
        borrar.setToolTipText("Presione para cambiar a modo goma.");
        tamanno = new JButton("Grosor", IMGtamanno);
        tamanno.setToolTipText("Presione para cambiar el grosor de las herramientas.");
        color = new JButton("Color", IMGcolor);
        color.setToolTipText("Presione para cambiar el color.");
        Dibujar(); //ListenDibujar = true
    }
    
    //Metodo para agregar Listeners a los botones
    public void Listeners(){
        
        //ActionListener para el botón "Dibujar".
        dibujar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                modoActual = "dibujar";
                if(ListenSDibujar == false){
                    removeListenerTexto();
                    Dibujar();
                    ListenSDibujar = true;
                }
            }
        });
        //ActionListener para el botón "texto".
        texto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                modoActual = "texto";
                Texto();
            }
        });
        //ActionListener para el botón "borrar".
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                modoActual = "borrar";
                if(ListenSDibujar == false){
                    removeListenerTexto();
                    Dibujar();
                    ListenSDibujar = true;
                }
                //setColorActual(Color.decode("#00FF00"));
                //borrarUltimaLinea();
            }
        });
        //ActionListener para el botón "grosor".
        tamanno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String[] thicknessOptions = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                String selectedThickness = (String) JOptionPane.showInputDialog(
                        Clientes.this,
                        "Seleccione el grosor de la línea:",
                        "Selector de Grosor",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        thicknessOptions,
                        thicknessOptions[0]);

                if (selectedThickness != null) {
                    grosor = Float.parseFloat(selectedThickness);
        } 
            }
        });
        //ActionListener para el botón "Cambiar color".
        color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                colorActual = JColorChooser.showDialog(Clientes.this, "Seleccionar Color", Color.WHITE);
            }
        });
        
    }

    //Seleccionando el color actual
    public void setColorActual(Color color) {
        this.colorActual = color;
    }
    
    //Metodo para dibujar y borrar
    public void Dibujar(){
        mousePressListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if(modoActual.equals("dibujar") || modoActual.equals("borrar")){
                        puntos.add(new Point(e.getX(), e.getY()));
                        dibujando = true;
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if(modoActual.equals("dibujar") || modoActual.equals("borrar")){
                        dibujando = false;
                        lineas.add(new ArrayList<>(puntos));
                        if (modoActual.equals("dibujar")) {
                            colores.add(colorActual);
                            grosorLinea.add(grosor);
                        } else if (modoActual.equals("borrar")) {
                            colores.add(Color.decode("#EEEEEE"));
                            grosorLinea.add(grosor);
                        }
                        puntos.clear();
                        //Poner algo para que sepa que va a recibir dibujo
                        conexion.enviarListaDeListasDePuntos(lineas);
                        conexion.enviarColores(colores);
                        conexion.enviarGrosor(grosorLinea);
                        conexion.enviarInt(x, y, textoEsc);
                        try {
                            out.writeInt(0);
                        } catch (IOException ex) {
                            Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //removeListeners();
                        opcion = 2;
                        SwingUtilities.invokeLater(() -> {
                            repaint();
                        });

                    }
                }
            }
        };

            mouseDraggedListener = new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if(modoActual.equals("dibujar") || modoActual.equals("borrar")){
                        if (dibujando) {
                            puntos.add(new Point(e.getX(), e.getY()));
                        }   
                    }
                }
            };
        this.addMouseListener(mousePressListener);
        this.addMouseMotionListener(mouseDraggedListener);
    }
    
    public void removeListeners() { //Dibujo
        this.removeMouseListener(mousePressListener);
        this.removeMouseMotionListener(mouseDraggedListener);
    }

    public void removeListenerTexto() { //Texto
        this.removeMouseListener(mousePressListener);
    }

    //Metodo para escribir
    public void Texto(){
        if(ListenSDibujar == true){
            removeListeners();
            mousePressListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(modoActual.equals("texto")){
                        cuadroTexto.setEditable(true);
                        cuadroTexto.requestFocus();
                        add(cuadroTexto);
                        cuadroTexto.setBounds(e.getX(), e.getY(), 50, 20);
                        if(cuadroTexto.getText().isEmpty() == false){
                            x.add(e.getX());
                            y.add(e.getY());
                            textoEsc.add(cuadroTexto.getText());
                            conexion.enviarListaDeListasDePuntos(lineas);
                            conexion.enviarColores(colores);
                            conexion.enviarGrosor(grosorLinea);
                            conexion.enviarInt(x, y, textoEsc);
                            try {
                                out.writeInt(0);
                            } catch (IOException ex) {
                                Logger.getLogger(Clientes.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            cuadroTexto.setText("");
                        }
                    }
                }
            };
            this.addMouseListener(mousePressListener);
            ListenSDibujar = false;
        }
    }

    public void guardarDibujo() {
        BufferedImage imagen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        paint(g2d);

        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            try {
                ImageIO.write(imagen, "png", archivo);
                JOptionPane.showMessageDialog(null, "Dibujo guardado en: " + archivo.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    //Dibujar 
    private void drawLines(Graphics2D g2d, List<Point> linea, Color color) {
        g2d.setColor(color);
        for (int i = 0; i < linea.size() - 1; i++) {
            Point punto1 = linea.get(i);
            Point punto2 = linea.get(i + 1);
            g2d.drawLine(punto1.x, punto1.y, punto2.x, punto2.y);
        }
    }
    
    //@Override
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        k = 0;
        for (List<Point> linea : new ArrayList<>(lineasRec)) {
            g2d.setStroke(new BasicStroke(grosorLineaRec.get(k)));
            drawLines(g2d, linea, coloresRec.get(k));
            k++;
        }

        k = 0;
        for (List<Point> linea : new ArrayList<>(lineas)) {
            g2d.setStroke(new BasicStroke(grosorLinea.get(k)));
            drawLines(g2d, linea, colores.get(k));
            k++;
        }

        k=0;
        for (List<Point> linea : new ArrayList<>(lineasRec)) {
            if(coloresRec.get(k).equals(Color.decode("#EEEEEE"))){
                g2d.setStroke(new BasicStroke(grosorLineaRec.get(k)));
                drawLines(g2d, linea, coloresRec.get(k));
            }
            k++;
        }

        k = 0;
        for (List<Point> linea : new ArrayList<>(lineas)) {
            if(colores.get(k).equals(Color.decode("#EEEEEE"))){
                g2d.setStroke(new BasicStroke(grosorLinea.get(k)));
                drawLines(g2d, linea, colores.get(k));
            }
            k++;
        }
        
        //Dibujar texto
        for (int i = 0; i < textoEsc.size(); i++) {
            g2d.setColor(Color.decode("#000000"));
            g2d.drawString(textoEsc.get(i), x.get(i), y.get(i));
        }

        //Dibujar texto
        for (int i = 0; i < textoEscRec.size(); i++) {
            g2d.setColor(Color.decode("#000000"));
            g2d.drawString(textoEscRec.get(i), xRec.get(i), yRec.get(i));
        }
    }
    
    public static void main(String[] args) {
        new Clientes().setVisible(true);
    }
}