package arbolb.arbol_mas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class Repositorio<Objecto>{
    private File archivo;
    private int separacion;
    
    
    public Repositorio(String path,String ruta, int separacion){
        this.archivo = new File(path,ruta);
        this.separacion = separacion;
    }
    
    
    public int escribir(Objecto objeto) throws SerializadorException{
        ObjectOutputStream ous = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            ous = new ObjectOutputStream(bos);
            ous.writeObject(objeto);
            ous.close();
            
            byte[] cadenaByte = bos.toByteArray();
            
            if(cadenaByte.length >= this.separacion)
                throw new SerializadorException("Error de escritura: Se ha ingresado demasiada información");
            
            byte[] tamCadena= ByteBuffer.allocate(4).putInt(cadenaByte.length).array();
            long posicionEscritura;
            
            try (RandomAccessFile tmp = new RandomAccessFile(archivo, "rw")) {
                long tamArchivo = tmp.length();
                posicionEscritura = (long) Math.ceil(tamArchivo/(float)this.separacion)*this.separacion;
                tmp.seek(posicionEscritura);
                tmp.write(tamCadena);
                tmp.write(cadenaByte);
            }
            return (int) (posicionEscritura/this.separacion);
            
        } catch (IOException ex) {
            throw new SerializadorException("Error al leer el archivo");
        } finally {
            try {
                ous.close();
            } catch (IOException ex) {
                throw new SerializadorException("Error al cerrar el archivo");
            }
        }
    }
    
    public Objecto leer(int pos) throws SerializadorException{
        try (RandomAccessFile tmp = new RandomAccessFile(archivo, "r")) {
            long size = tmp.length();
            tmp.seek(pos*this.separacion);
            byte[] byteTam = new byte[4];
            tmp.read(byteTam);
            int tam =   (byteTam[0]<<24)&0xff000000|
                        (byteTam[1]<<16)&0x00ff0000|
                        (byteTam[2]<< 8)&0x0000ff00|
                        (byteTam[3]<< 0)&0x000000ff;
            if(tam == 0){
                throw new SerializadorException("No existe un elemento en la posicion solicitada");
            }
            if (pos*this.separacion >= tmp.length()){
               throw new SerializadorException("Error de Indice");
            }
            byte[] cadena = new byte[tam];
            tmp.readFully(cadena);
            ByteArrayInputStream bis = new ByteArrayInputStream(cadena);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Objecto clase = (Objecto) ois.readObject();
            return clase;
        } catch (FileNotFoundException ex) {
            throw new SerializadorException("No se ha encontrado el archivo");
        } catch (IOException ex) {
            throw new SerializadorException("Error al leer el archivo");
        } catch (ClassNotFoundException ex) {
            throw new SerializadorException("Error al leer la información");
        }
        
    }
    
    public void listar() throws SerializadorException{
        try {
            RandomAccessFile tmp = new RandomAccessFile(archivo, "r");
            long numElementos = (long) Math.ceil(tmp.length()/(float)this.separacion);
            tmp.seek(0);
            long i = 0;
            while(i < numElementos){
                tmp.seek(i*this.separacion);
                byte[] byteTam = new byte[4];
                tmp.read(byteTam);
                int tam = (  byteTam[0]<<24)&0xff000000|
                            (byteTam[1]<<16)&0x00ff0000|
                            (byteTam[2]<< 8)&0x0000ff00|
                            (byteTam[3]<< 0)&0x000000ff;
                if(tam !=0){
                    byte[] cadena = new byte[tam];
                    tmp.readFully(cadena);
                    ByteArrayInputStream bis = new ByteArrayInputStream(cadena);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    Objecto clase = (Objecto) ois.readObject();
                }
                i += 1;
            }
        } catch (FileNotFoundException ex) {
            throw new SerializadorException("No se ha encontrado el archivo");
        } catch (IOException | ClassNotFoundException ex) {
            throw new SerializadorException("Error al leer la información");
        }
    }
    
    public void eliminar(int pos) throws SerializadorException{
        try (RandomAccessFile tmp = new RandomAccessFile(this.archivo ,"rwd")) {
            long inicio = pos*this.separacion;
            if (inicio >= tmp.length()){
                throw new SerializadorException("Error de Indice");
            }
            tmp.seek(pos*this.separacion);
            byte[] byteTam = new byte[4];
            tmp.read(byteTam);
            int tam = (  byteTam[0]<<24)&0xff000000|
                        (byteTam[1]<<16)&0x00ff0000|
                        (byteTam[2]<< 8)&0x0000ff00|
                        (byteTam[3]<< 0)&0x000000ff;
            if(tam == 0){
                throw new SerializadorException("No existe un elemento en la posicion solicitada");
            }
            tmp.seek(inicio);
            for(long j = 0; j<350;j++){
                tmp.write((byte) 0);
            }
            
        } catch (FileNotFoundException ex) {
            throw new SerializadorException("El archivo no ha sido encontrado");
        } catch (IOException ex) {
            throw new SerializadorException("Error al leer el archivo");
        }
        
    }
    
    public void Modificar(int pos, Objecto clase) throws SerializadorException{
        ObjectOutputStream ous = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ous = new ObjectOutputStream(bos);
            ous.writeObject(clase);
            ous.close();
            byte[] cadenaByte = bos.toByteArray();
            if(cadenaByte.length >= this.separacion){
                throw new SerializadorException("Error de modificacion: Se ha ingresado demasiada información");
            }
            byte[] tamCadena= ByteBuffer.allocate(4).putInt(cadenaByte.length).array();
            try (RandomAccessFile tmp = new RandomAccessFile(archivo, "rwd")) {
                long inicio = pos*this.separacion;
                if (inicio >= tmp.length()){
                    throw new SerializadorException("Se ha ingresado un indice erróneo");
                }
                tmp.seek(pos*this.separacion);
                long tamArchivo = tmp.length();
                tmp.write(tamCadena);
                tmp.write(cadenaByte);
            }
        } catch (IOException ex) {
            throw new SerializadorException("Error al escribir la informacion");
        } finally {
            try {
                ous.close();
            } catch (IOException ex) {
                throw new SerializadorException("Error al cerrar el archivo");
            }
        }
    }

    public File obtener_archivo() {
        return archivo;
    }

    public void asignar_archivo(File archivo) {
        this.archivo = archivo;
    }
    
    public void borrar_archivo() throws IOException{
        RandomAccessFile tmp = new RandomAccessFile(archivo, "rwd");
        tmp.getChannel().truncate(0);
    }

    public int obtener_separacion() {
        return separacion;
    }

    public void asignar_separacion(int separacion) {
        this.separacion = separacion;
    }
    
    
}