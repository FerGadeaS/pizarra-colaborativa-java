package servidor;
import java.awt.Color;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerHilo extends Thread{
    String nombre, hora;
    long tiempo;
    static List<List<Point>> puntos = new ArrayList<>();
    static List<Integer> entero = new ArrayList<>();
    static List<Color> colores = new ArrayList<>();
    static List<Float> grosor = new ArrayList<>();
    static List<Integer> xRec = new ArrayList<>();
    static List<Integer> yRec = new ArrayList<>();
    static List<String> textoEscRec = new ArrayList<>();
    
    static int Salir;
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
    DataInputStream in;
    DataOutputStream out;
    PanelServer panel;
    Socket cliente;
    int id;
    String sqlfechaIni;
    long tiempoConectado;
    //Hora de inicio
    DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("HH:mm:ss");
    public ServerHilo(Socket cliente ,DataInputStream in, DataOutputStream out, String nombre, long tiempoInicio, String hora, PanelServer panel, int id){
        this.in = in;
        this.out = out;
        this.nombre = nombre;
        this.tiempo = tiempoInicio;
        this.hora = hora;
        this.panel = panel;
        this.cliente = cliente;
        this.id = id;
        //Fecha de inicio
        LocalDateTime fechaInicial = LocalDateTime.now();
        sqlfechaIni = fechaInicial.format(formatoFecha);//Fecha de inicio
    }
    
    @Override
    public void run(){
        while (true) {
            try {
                Servidor.enviarListaDeListasDePuntos(out, puntos, this);
                Servidor.enviarColoresL(out, colores, this);
                Servidor.enviarGrosorL(out, grosor, this);
                Servidor.enviarInt(out, xRec, yRec, textoEscRec, this);
                //sleep(100);
                
                //Recibe de los clientes
                puntos = RecibePuntos(in);
                colores = recibeColores(in);
                grosor = recibeGrosor(in);
                recibeInt(in);
                Salir = in.readInt();
                panel.repaint();
                if (1 == Salir) {
                    finalizarConexion();
                    break;
                }
            } catch (IOException e) {
                finalizarConexion();
                break;
            }
        }

    }
    
    //Guardado en MySQL
    private void finalizarConexion() {
        try {
            // Hora y fecha de salida
            LocalDateTime HoraFin = LocalDateTime.now();
            String sqlhoraFinal = HoraFin.format(formatoHora);
            String sqlFechaFin = HoraFin.format(formatoFecha);

            // Duración de la sesión
            tiempoConectado = (System.currentTimeMillis() - tiempo) / 1000;
            String sqlTiempo = tiempoConectado + " segundos.";

            // Guardar sesión
            String enviar = "CALL SP_GuardarDatos(" + id + ", '"
                    + sqlfechaIni + "', '"
                    + hora + "', '"
                    + sqlTiempo + "', '"
                    + sqlFechaFin + "', '"
                    + sqlhoraFinal + "')";

            Servidor.sql.executeUpdate(enviar);

            // Eliminar datos temporales
            String consulta = "DELETE FROM datos WHERE id = " + id;
            Servidor.sql.executeUpdate(consulta);

            // Registrar desconexión
            Servidor.nuevoElemento = "Cliente " + nombre
                    + " se ha desconectado el dia " + sqlFechaFin
                    + " a las " + sqlhoraFinal;

            Servidor.listModel.addElement(Servidor.nuevoElemento);
            Servidor.Conexiones.setModel(Servidor.listModel);

            // Actualizar estado del usuario
            consulta = "UPDATE usuario SET conectado = 0 WHERE ID_cuenta = '" + id + "'";
            Servidor.sql.executeUpdate(consulta);

            Servidor.actualizarLista();

            if (Servidor.TotalCl == 9) {
                Servidor.UsuariosMax.SIGNAL();
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServerHilo.class.getName())
                    .log(Level.SEVERE, null, ex);

        } finally {
            // Cerrar recursos individualmente
            try {
                in.close();
            } catch (IOException ignored) {}

            try {
                out.close();
            } catch (IOException ignored) {}

            try {
                cliente.close();
            } catch (IOException ignored) {}

            // Eliminar hilo de clientes conectados
            Servidor.eliminarCliente(this);
        }
    }

    public void enviarListaDeListasDePuntos(List<List<Point>> listaDeListasDePuntos) {
        try {
            //out.writeUTF("Te envio cosas de : " + nombre);
            // Enviar el tamaño de la lista de listas al servidor
            out.writeInt(listaDeListasDePuntos.size());

            // Para cada lista de puntos, enviar su tamaño y luego cada punto
            for (List<Point> listaDePuntos : listaDeListasDePuntos) {
                out.writeInt(listaDePuntos.size());

                for (Point punto : listaDePuntos) {
                    out.writeInt((int) punto.getX());
                    out.writeInt((int) punto.getY());
                }
            }
        } catch (IOException ex) {
        }
    }

    public List<List<Point>> RecibePuntos(DataInputStream in) throws IOException{
        int listaSize;
        List<List<Point>> Lista = new ArrayList<>();
        try {
            listaSize = in.readInt();
            //System.out.println("Estoy recibiendo puntos : " + listaSize);
            
            for (int i = 0; i < listaSize; i++) {
                List<Point> innerList = new ArrayList<>();
                int entero = in.readInt();
                for (int j = 0; j < entero; j++) {
                    int x = in.readInt();
                    int y = in.readInt();
                    innerList.add(new Point(x, y));
                }
                Lista.add(innerList);
                panel.puntos.add(innerList);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        return Lista;
    }
 
    //Envia el texto escrito
    public void enviarInt(List<Integer> x, List<Integer> y, List<String> cadena){
        try {
            // Enviar el tamaño de la lista al servidor
            out.writeInt(x.size());
            out.writeInt(y.size());
            out.writeInt(cadena.size());
            
            // Enviar cada elemento de la lista al servidor
            for (int entero : x) {
                out.writeInt(entero);
            }

            for (int entero : y) {
                out.writeInt(entero);
            }

            for (String cad : cadena) {
                out.writeUTF(cad);
            }
        } catch (IOException e) {}
    }

    //Recibe el texto escrito
    public void recibeInt(DataInputStream in) throws IOException {
        // Recibe el tamaño de la lista
        try {
            int x = in.readInt();
            int y = in.readInt();
            int t = in.readInt();

            for (int i = 0; i < x; i++) {
                int xr = in.readInt();
                xRec.add(xr);
                panel.xRec.add(xr);
            }

            for (int i = 0; i < y; i++) {
                int yr = in.readInt();
                yRec.add(yr);
                panel.yRec.add(yr);
            }

            for (int i = 0; i < t; i++) {
                String text = in.readUTF();
                textoEscRec.add(text);
                panel.textoEscRec.add(text);
            }
        } catch (IOException e) {}
    }

    //Envia el color de la linea
    public void enviarColores(List<Color> colores){
        try {
            //Enviar el tamaño de la lista al servidor
            out.writeInt(colores.size());

            //Envia cada elemento de la lista al server
            for(Color color: colores){
                out.writeUTF(color.toString());
            }
        } catch (IOException e) {  }
    }
    
    //Envia el grosor de la linea
    public void enviarGrosor(List<Float> grosor){
        try {
            //Enviar el tamaño de la lista al servidor
            out.writeInt(grosor.size());

            //Envia cada elemento de la lista al server
            for(Float grueso: grosor){
                out.writeFloat(grueso);
            }
        } catch (IOException e) {  }
    }
    
    //Recibe el color de la linea
    public List<Color> recibeColores(DataInputStream in){
        List<Color> colorL = new ArrayList<>();
        try {
            int largo = in.readInt();
            for (int i = 0; i < largo; i++) {
                String colorS = in.readUTF();
                int startIndex = colorS.indexOf("[") + 1;
                int endIndex = colorS.indexOf("]");
                String rgbString = colorS.substring(startIndex, endIndex);
                String[] rgbValues = rgbString.split(",");
                int red = Integer.parseInt(rgbValues[0].substring(2));
                int green = Integer.parseInt(rgbValues[1].substring(2));
                int blue = Integer.parseInt(rgbValues[2].substring(2));
                colorL.add(new Color(red, green, blue));
                panel.colores.add(new Color(red, green, blue));
            }
        } catch (IOException | NumberFormatException e) { }
        return colorL;
    }

    //Recibe el grosor de la linea o goma
    public List<Float> recibeGrosor(DataInputStream in){
        List<Float> grueso = new ArrayList<>();
        try {
            int largo = in.readInt();
            for (int i = 0; i < largo; i++) {
                Float gruesin = in.readFloat();
                grueso.add(gruesin);
                panel.grosor.add(gruesin);
            }
        } catch (IOException e) { }
        return grueso;
    }
}