/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolb.arbolPlusPlus;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 *
 * @author krysthyan
 * @param <Clase>
 */
public class ArbolArchivo<Clase> extends Repositorio{

    protected ArbolArchivo(String path,String ruta, int separacion) {
        super(path,ruta, separacion);
    }
    
    public void setRaiz(int pos) throws ArbolException{
        try {
            RandomAccessFile tmp = new RandomAccessFile(this.getArchivo(),"rwd"); 
            byte[] tam = ByteBuffer.allocate(4).putInt(pos).array();
            tmp.seek(0);
            tmp.write(tam);
            tmp.close();
        } catch (FileNotFoundException ex) {
            throw new ArbolException("No se ha encontrado el archivo");
        } catch (IOException ex) {
            throw new ArbolException("Error al escribir el archivo");
        }
    }
    
    public int getRaiz() throws ArbolException{
        try {
            RandomAccessFile tmp = new RandomAccessFile(this.getArchivo(),"rw"); 
            byte[] pos = new byte[4];
            tmp.seek(0);
            tmp.read(pos);
            int intPosicion = (pos[0]<<24)&0xff000000|
                    (pos[1]<<16)&0x00ff0000|
                    (pos[2]<< 8)&0x0000ff00|
                    (pos[3]<< 0)&0x000000ff;
            tmp.close();
            return intPosicion;
        } catch (FileNotFoundException ex) {
            throw new ArbolException("No se ha encontrado el archivo");
            
        } catch (IOException ex) {
            throw new ArbolException("Error al leer el archivo");
        }
    }
}
