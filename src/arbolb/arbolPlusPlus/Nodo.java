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
public abstract class Nodo<Clave extends Comparable<Clave>> implements Serializable{
    private int padre;
    private TipoNodo tipo;
    private int numElementos = 0;
    private int posicion;
    
    public Boolean isMenorIgual(Clave elem1 ,Clave elem2){
        int comparacion = elem1.compareTo(elem2);
        if(comparacion <= 0){
            return true;
        }
        else{
            return false;
        }
    }
    
    public Boolean isMayor(Clave elem1 ,Clave elem2){
        int comparacion = elem1.compareTo(elem2);
        if(comparacion > 0){
            return true;
        }
        else{
            return false;
        }
    }
    
    public Boolean isIgual(Clave elem1 ,Clave elem2){
        int comparacion = elem1.compareTo(elem2);
        if(comparacion == 0){
            return true;
        }
        else{
            return false;
        }
    }
    
    

    public int getNumElementos() {
        return numElementos;
    }

    public void setNumElementos(int numElementos) {
        this.numElementos = numElementos;
    }
    
    public void modificarNumElementos(int numElementos){
        this.numElementos += numElementos;
    }
    
        public Boolean isEmpty(){
        if (numElementos == 0) return true;
        return false;   
    }
    
    public Boolean isFull(){
        if (numElementos == 2) return true;
        return false;
    }        


    public TipoNodo getTipo() {
        return tipo;
    }

    public void setTipo(TipoNodo tipo) {
        this.tipo = tipo;
    }

    public int getPadre() {
        return padre;
    }

    public void setPadre(int padre) {
        this.padre = padre;
    }
    
    public Boolean isInterno(){
        if (this.getTipo() == TipoNodo.INTERNO) return true;
        return false;
    }
    
    public Boolean isHoja(){
        if (this.getTipo() == TipoNodo.HOJA) return true;
        return false;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    
    // es si esta metodo es para comprobar a que lado se ubica el nodo hoja 
    // osea si el nodohoja se encuentra en una rama izquierda de su padre (return 0)
    // en una rama central (return 1) o una rama derecha(return 2)
    public Integer validarRamaPerteneceNodo(ArbolArchivo archivo) throws SerializadorException{
        
        // si este nodo no tiene padre retorna null
        if(this.padre == 0) return null;
        
        // caso contrario si el nodo hoja tiene padre procedemos a obtener dicho nodo padre el cual va ser interno
        NodoInterno parent = (NodoInterno) archivo.leer(this.padre);
        
        // con el padre obtenido vemos a que rama se encuentra ubicado el nodo hoja
        if (parent.getRamaIzq() == this.posicion) return 0;
        else if(parent.getRamaCen() == this.posicion) return 1;
        else if(parent.getRamaDer() == this.posicion) return 2;
        else return 0;
    }
    
    // este metodo es recursivo el cual nos va a cambiar la clave en toda la jerarquia de nuestro  arbol
    public void cambiarClave(Clave claveAnterior, Clave nuevaClave, ArbolArchivo archivo) throws SerializadorException{
        // este metodo recursivo va estar autollandose siempre que el padre sea diferente de cero
        // una vez que el this.padre sea igual a cero para la recursividad
        if (this.padre != 0){
            
            //obtenemos el padre del padre de nuestro nodo 
            NodoInterno parent = (NodoInterno) archivo.leer(this.padre);
            
            // comprobamos si nuestro padre del padre del nodo tiene uno o dos elementos
            if(parent.getNumElementos() == 1){
                // hacemos la comparacion que si clave del nodo parent es igual a la clave del elemento que queremos
                // borrar si es asi por lo tanto va cambiarse con la clave nueva
                if(this.isIgual((Clave) parent.getClave(0), claveAnterior)) parent.setClave(0, nuevaClave); 
                
                // una vez modificado el parent con su nueva clave mandamos a actualizar nuestro archivo del arbol
                archivo.Modificar(parent.getPosicion(), parent);
                
            // comprobamos si el parent tiene dos elementos va a entrar en este else
            }else if(parent.getNumElementos() == 2){
                
                // como son dos elementos vamos hacer dos comparaciones uno para elemento y  por ultimo mandamos actualizar el archivo del arbol
                // hacemos la comparacion que si clave del nodo parent es igual a la clave del elemento que queremos
                // borrar si es asi por lo tanto va cambiarse con la clave nueva
                if(this.isIgual((Clave) parent.getClave(0), claveAnterior)){
                    parent.setClave(0, nuevaClave);
                    archivo.Modificar(parent.getPosicion(), parent);
                }              
                else if(this.isIgual((Clave) parent.getClave(1), claveAnterior)){
                    parent.setClave(1, nuevaClave);
                    archivo.Modificar(parent.getPosicion(), parent);
                }
            }
            // aqui se va a estar auto llamando el metodo hasta llegar a la raiz del arbol la
            // cual no tiene padre y el this.padre==0 y por consiguiente se rompe la recursion.
            parent.cambiarClave(claveAnterior, nuevaClave, archivo);
        }       
    }
}
