/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolb.arbolPlusPlus;
import java.io.Serializable;

/**
 *
 * @author krysthyan
 */
public class Elemento<Clave> implements Serializable{
    private Clave clave;
    private int posicion;

    protected Elemento(Clave clave, int posicion) {
        this.clave = clave;
        this.posicion = posicion;
    }

    public Clave getClave() {
        return clave;
    }

    public void setClave(Clave clave) {
        this.clave = clave;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    
    
}
