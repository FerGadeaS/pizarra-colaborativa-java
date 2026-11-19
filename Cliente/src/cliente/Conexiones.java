package cliente;
import java.awt.Color;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class Conexiones {
    static DataOutputStream out;
    DataInputStream in;
    Clientes cl;

    public Conexiones(DataOutputStream out, DataInputStream in, Clientes cl){
        Conexiones.out = out;
        this.in = in;
        this.cl = cl;
    }
    
    //Envia las lineas dibujadas
    public void enviarListaDeListasDePuntos(List<List<Point>> listaDeListasDePuntos) {
        try {
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

    //Recibe las lineas dibujadas por cada user
    public void RecibePuntos(DataInputStream in) throws IOException{
        int listaSize;
        try {
            listaSize = in.readInt();
            
            for (int i = 0; i < listaSize; i++) {
                List<Point> innerList = new ArrayList<>();
                int entero = in.readInt();
                for (int j = 0; j < entero; j++) {
                    int x = in.readInt();
                    int y = in.readInt();
                    innerList.add(new Point(x, y));
                }
                cl.lineasRec.add(innerList);
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
                cl.xRec.add(xr);
            }

            for (int i = 0; i < y; i++) {
                int yr = in.readInt();
                cl.yRec.add(yr);
            }

            for (int i = 0; i < t; i++) {
                String text = in.readUTF();
                cl.textoEscRec.add(text);
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
    public void recibeColores(DataInputStream in){
        try {
            int largo = in.readInt();
            for (int i = 0; i < largo; i++) {
                String color = in.readUTF();
                int startIndex = color.indexOf("[") + 1;
                int endIndex = color.indexOf("]");
                String rgbString = color.substring(startIndex, endIndex);
                String[] rgbValues = rgbString.split(",");
                int red = Integer.parseInt(rgbValues[0].substring(2));
                int green = Integer.parseInt(rgbValues[1].substring(2));
                int blue = Integer.parseInt(rgbValues[2].substring(2));
                cl.coloresRec.add(new Color(red, green, blue));
            }
        } catch (IOException | NumberFormatException e) { }
    }

    //Recibe el grosor de la linea o goma
    public void recibeGrosor(DataInputStream in){
        try {
            int largo = in.readInt();
            for (int i = 0; i < largo; i++) {
                Float grueso = in.readFloat();
                cl.grosorLineaRec.add(grueso);
            }
        } catch (IOException e) { }
    }

}