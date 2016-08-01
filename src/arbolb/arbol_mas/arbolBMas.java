/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolb.arbol_mas;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author krysthyan
 */
public class arbolBMas<Clave extends Comparable<Clave>> {
    private int raiz = 0;
    private ArbolArchivo<Nodo> archivo;
    
    protected arbolBMas(String path,String ruta) throws ArbolException{
        this.archivo = new ArbolArchivo(path,ruta,750);
        try{
            this.raiz = this.archivo.getRaiz();
        }
        catch(Exception ex){
            this.raiz = 0;
        }
    }    
    
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
    
    public Boolean isMayorIgual(Clave elem1 ,Clave elem2){
        int comparacion = elem1.compareTo(elem2);
        if(comparacion >= 0){
            return true;
        }
        else{
            return false;
        }
    }
    
    public Boolean isMenor(Clave elem1 ,Clave elem2){
        int comparacion = elem1.compareTo(elem2);
        if(comparacion < 0){
            return true;
        }
        else{
            return false;
        }
    }    
    
    public void agregar(Clave clave, int posicion) throws ArbolException, SerializadorException{
        if (raiz == 0){
            Elemento nuevo = new Elemento(clave, posicion);             
            NodoHoja nuevaRaiz = new NodoHoja<Clave>(nuevo);
            nuevaRaiz.setPosicion(1);
            this.archivo.setRaiz(1);
            this.archivo.escribir(nuevaRaiz);
            this.raiz = 1;
        }
        else{
            Nodo buscador = (Nodo) this.archivo.leer(this.raiz); 
            NodoInterno nodoInt;
            while(buscador.isInterno()){
                nodoInt = (NodoInterno)buscador;
                if(nodoInt.getNumElementos() == 1){
                    if(isMenorIgual(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    }
                    else if(isMayor(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }
                }
                else{
                    if(isMenorIgual(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    }
                    else if(isMayor(clave, (Clave)nodoInt.getClave(1))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaDer());
                    }
                    else{
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }    
                }
            }
            NodoHoja nodoHoja = (NodoHoja) buscador;
            int verificar = nodoHoja.addElemento(clave, posicion, this.archivo);
            if(verificar != 0){
                this.raiz = verificar;
            }
        }
    }
    
    public Integer buscar(Clave clave) throws SerializadorException{
        if(this.raiz == 0){
            return null;
            
        }
        Nodo buscador = (Nodo) this.archivo.leer(this.raiz);
        NodoHoja nodoHoja;
        // el promer condicional es para ver si cuando inicia es un nodo hoja o un nodo interno
        
        if(buscador.isInterno()){
            NodoInterno nodoInt;
            
            // este while lo que va ser es ir escalando el arbol hasta encontrar el nodo hoja dond se encuentra
            // el elemento de la clave que se ha proporcionado
            while(buscador.isInterno()){
                nodoInt = (NodoInterno)buscador;
                
                // si el nodo interno tiene un solo elemento solo vamos a ocupar un par de comparaciones ya que solo contiene
                // dos ramas la izquierda y la central
                if(nodoInt.getNumElementos() == 1){
                    if(isMenor(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    }
                    else if(isMayorIgual(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }
                }
                // en cambio si el nodo interno tiene dos claves, van haber mas comparaciones ya que vamos a tener 
                // 3 ramas, una uzquierda, una central y una derecha
                else{
                    if(isMenor(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    }
                    else if(isMayorIgual(clave, (Clave)nodoInt.getClave(1))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaDer());
                    }
                    else{
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }    
                }
            }
        }
        // una vez acabado el while y se ha llegado al nodo hoja dond se encuentra el elemento
        // hacemos un cast para pasar el nodo interno buscador a nodo hoja
        nodoHoja = (NodoHoja)buscador;
        
        // esta condicion es para ver dond se encuenta ubicado el elemento con la clave proporcionada
        // en el nodo hoja, una vez enconntrado el elemento vamos a retornar la posicion dond esta escrito
        // el objeto en el archivo de datos con la clave dada
        if(isIgual((Clave)nodoHoja.getElementos()[0].getClave(),clave)){
            return nodoHoja.getElementos()[0].getPosicion();
        }
        else if(nodoHoja.getElementos()[1] != null && isIgual((Clave)nodoHoja.getElementos()[1].getClave(),clave)){
            return nodoHoja.getElementos()[1].getPosicion();
        }
        else{
            return null;
        }
    }
    // aqui vamos a eliminar el el elemento del arbol con una clave dada
    public Boolean eliminar(Clave clave) throws SerializadorException, ArbolException, IOException{
        // primero debemos de obtener la raiz del arbol con la cual hay que leer el archivo por la raiz
        // que se encuentra ubicada en la primera posicion raiz=1.
        Nodo buscador = (Nodo) this.archivo.leer(this.raiz);
        
        NodoHoja nodoHoja;
        
        // comprobamos si el nodo buscador es hoja o interno
        // el caso que nos de hoja es cuando recien estamos ingresando elementos
        // y todavia no hay ninguna division, por lo que cuando se crea por primera vez
        // es un nodo hoja con un elemento.
        if(buscador.isHoja()){
            nodoHoja = (NodoHoja) buscador;
            if(nodoHoja.getNumElementos() == 2){
                if(isIgual(clave,(Clave)nodoHoja.getElementos()[0].getClave())){
                    nodoHoja.setElemento(0, nodoHoja.getElementos()[1]);
                    nodoHoja.setElemento(1, null);
                    
                }else{
                    nodoHoja.setElemento(1, null);
                }
                nodoHoja.setNumElementos(1);
                archivo.Modificar(nodoHoja.getPosicion(), nodoHoja);
                return true;
            }else if(nodoHoja.getNumElementos() == 1 && isIgual((Clave) nodoHoja.getElementos()[0].getClave(),clave)){
                this.raiz = 0;
                archivo.borrar_archivo();
                return true;
            }
        }
        
        // el nodo buscador nos ha dado como resultado un nodo interno
        else if(buscador.isInterno()){
            
            // creamos un nodo interno, el cual le vamos asignar el nodo buscador
            // el bucle while va estar hasta que llegue al nodo hoja dond se encuentra la
            // clave que nos han proporcionado.
            NodoInterno nodoInt;
            
            //vemos si el nodo buscador es interno
            while(buscador.isInterno()){
                
                //realizamos un cast para pasar el nodo buscador a un nodo interno(casi relevante)
                nodoInt = (NodoInterno)buscador;
                
                // comprobamos si el nodo interno tiene uno o dos elementos
                
                // si tiene un elemento solo vamos hacer dos compraciones ya que el nodo interno que tiene
                // un solo elemento consta unicamente de dos ramas de la izquierda y de la central
                if(nodoInt.getNumElementos() == 1){
                    if(isMenor(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    }
                    else if(isMayorIgual(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }
                }
                
                // caso contrario si el nodo  interno tiene dos claves, nos toca hacer 3 comparaciones
                // ya que este nodo interno consta de 3 ramas, una izquierda, una central y una derecha
                else{
                    if(isMenor(clave, (Clave)nodoInt.getClave(0))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaIzq());
                    }
                    else if(isMayorIgual(clave, (Clave)nodoInt.getClave(1))){
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaDer());
                    }
                    else{
                        buscador = (Nodo) this.archivo.leer(nodoInt.getRamaCen());
                    }    
                }
            }
        }
        
        // una vez que se ha salido del while es porque hemos llegado al nodo hoja dond se encuentra
        // el elemento de nuestra clave proporcionada
        
        // realizamos un cast para convertir de un nodo interno a un nodo hoja
        nodoHoja = (NodoHoja)buscador;
        
        // ahora este nodoHoja vamos a utilizar una funcion de la misma que se llama eliminar, la cual va llevar la
        // clave y el archivo donde vamos hacer la eliminacion
        nodoHoja.eliminar(clave, this.archivo);
        
        
        this.raiz = archivo.getRaiz();
        return true;
    }
    
    public List<Elemento> listar() throws SerializadorException{
        if(this.raiz == 0){
            return null;
        }
        List<Elemento> lista = new LinkedList<>();
        Nodo buscador = (Nodo) this.archivo.leer(this.raiz); 
        while(buscador.isInterno()){
             NodoInterno buscadorInt = (NodoInterno)buscador;
             buscador = (Nodo) archivo.leer(buscadorInt.getRamaIzq());
        }
         NodoHoja hoja = (NodoHoja)buscador;
         while(hoja.getNext() != 0){ 
            if(hoja.getElementos()[0] != null){
                lista.add(hoja.getElementos()[0]);
            }
            if(hoja.getElementos()[1] != null){
                lista.add(hoja.getElementos()[1]);}
            hoja = (NodoHoja) archivo.leer(hoja.getNext());
        }
        if(hoja.getElementos()[0] != null){
            lista.add(hoja.getElementos()[0]);
        }
        if(hoja.getElementos()[1] != null){
            lista.add(hoja.getElementos()[1]);}
        return lista;
    }

    public void Imprimir() throws SerializadorException{
        if(this.raiz != 0){
            Nodo buscador = (Nodo) this.archivo.leer(this.raiz); 
            this.Imprimir(buscador, "");
        }
    }
    
    private void Imprimir(Nodo buscador ,String str) throws SerializadorException{
        if (buscador.getTipo() == TipoNodo.HOJA){
            NodoHoja hoja = (NodoHoja)buscador;
            try{
                System.out.println("Hoja " + str + hoja.getElementos()[0].getClave() + " posicion " + hoja.getPosicion()  + " El padre es: " + hoja.getPadre() + " tam: " + hoja.getNumElementos());
            }
            catch(Exception ex){
                System.out.println("");
            }
            try{
                System.out.println("Hoja " + str + hoja.getElementos()[1].getClave() + " posicion " + hoja.getPosicion()  + " El padre es: " + hoja.getPadre() + " tam: " + hoja.getNumElementos());
            }
            catch(Exception ex){
                System.out.println("");
            }
            System.out.println("\n");
        }
        else{
            NodoInterno interno = (NodoInterno)buscador;
            System.out.println("Int  " + str + interno.getClave(0) + " El padre es: " + interno.getPadre()+ " Esto nodo es: "  + interno.getPosicion() +  " tam: " + interno.getNumElementos());
            if(interno.getClave(1) != null) System.out.println("Int  " + str + interno.getClave(1) + " El padre es: " + interno.getPadre() + " Esto nodo es: " + interno.getPosicion() + " tam: " + interno.getNumElementos());
            Nodo ramaIzq = (Nodo) archivo.leer(interno.getRamaIzq());
            Nodo ramaCen = (Nodo) archivo.leer(interno.getRamaCen());
            Imprimir(ramaIzq, str + "\t");
            Imprimir(ramaCen, str + "\t");
            if (interno.getRamaDer() != 0){
                Nodo ramaDer = (Nodo) archivo.leer(interno.getRamaDer());
                Imprimir(ramaDer, str + "\t");
            }
        }
        
    }
    
    public void ImprimirLista() throws SerializadorException{
        if(this.raiz != 0){
        Nodo buscador = (Nodo) this.archivo.leer(this.raiz); 
        while(buscador.isInterno()){
            NodoInterno buscadorInt = (NodoInterno)buscador;
            buscador = (Nodo) archivo.leer(buscadorInt.getRamaIzq());
        }
        NodoHoja hoja = (NodoHoja)buscador;
        while(hoja.getNext() != 0){
            try{
                    System.out.println("Hoja " + hoja.getElementos()[0].getClave() + " posicion " + hoja.getPosicion()  + " El padre es: " + hoja.getPadre() + " tam: " + hoja.getNumElementos());
                }
                catch(Exception ex){
                    System.out.println("");
                }
                try{
                    System.out.println("Hoja " + hoja.getElementos()[1].getClave() + " posicion " + hoja.getPosicion()  + " El padre es: " + hoja.getPadre() + " tam: " + hoja.getNumElementos());
                }catch(Exception ex){
                    System.out.println("");
                }
            hoja = (NodoHoja) archivo.leer(hoja.getNext());
        }
                try{
                    System.out.println("Hoja " + hoja.getElementos()[0].getClave() + " posicion " + hoja.getPosicion()  + " El padre es: " + hoja.getPadre() + " tam: " + hoja.getNumElementos());
                }
                catch(Exception ex){
                    System.out.println("");
                }
                try{
                    System.out.println("Hoja " + hoja.getElementos()[1].getClave() + " posicion " + hoja.getPosicion()  + " El padre es: " + hoja.getPadre() + " tam: " + hoja.getNumElementos());
                }catch(Exception ex){
                    System.out.println("");
                }
        }
    } 

    public int getRaiz() {
        return raiz;
    }
    
    
    
}
