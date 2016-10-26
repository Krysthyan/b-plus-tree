/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolb.arbol_mas;
/**
 *
 * @author krysthyan
 * @param <Clave>
 */
public class NodoInterno<Clave extends Comparable<Clave>> extends Nodo{
    
    private Object[] clave = new Object[2];
    private int ramaIzq = 0;
    private int ramaCen = 0;
    private int ramaDer = 0;
    
    protected NodoInterno(Clave clave){
        this.clave[0] = clave;  
        this.clave[1] = null;
        this.modificarNumElementos(1);
        this.setTipo(TipoNodo.INTERNO);
    }
    
    
    // este nodo fusiona dos nodos internos cuando estos se encuentran en el mismo nivel
    
    public void insertar(int posNuevoNodo, ArbolArchivo archivo) throws SerializadorException{
        
        // se lee el nodo interno pero el nuevo que se a creado, el cual es el que queremos fusionar con su pareja
        NodoInterno nuevoNodo = (NodoInterno) archivo.leer(posNuevoNodo);
        
        // este comparador lo que hace es ordenar los elemennos del nodo interno por la clave si la clave del nodo nuevo
        // es menor o igual que la clave en la posicion 0 de del otro nodo interno entonces entra aqui
        if(isMenorIgual((Clave)nuevoNodo.getClave(0),(Clave)this.clave[0])){
            // aux lo que hace es guardar la clave del nodoprincipal para que no sea perdido ese dato
            Object aux = this.clave[0];
            
            // ahora como ya tenemos el respaldo de la clave en la pos 0 reescribimos con la clave del nodo nuevo  ya que es menor que la otra
            // y esto debe de estar ordenada
            this.clave[0] = nuevoNodo.getClave(0);
            
            // como vemos la pos 1 del arreglo de claves ubicamos la varibale aux la cual guardo la clave que estaba ubicada en la pos 0 anteriormente
            this.clave[1] = aux;
            
            // como vemos que la el nodo interno neuvo se va ubicar a lado izquiero del nodo fusionado entonces la ramana derecha del nodo fusionado(este nodo es 
            // el que se le va a unir el nodo interno nuevo) viene a ser la rama central del nodo fusionado
            this.ramaDer = this.ramaCen;
            
            // la rama izquierda del nodo fusinado es la rama es la rama izquierda del nodo interno nuevo
            this.ramaIzq = nuevoNodo.getRamaIzq();
            
            // la rama central no se modifica ya queda intacta donde se le quiere que este, en el centro
            
            // se lee el nodo izquiedo para poder asignarle el nuevo padre que tiene
            Nodo ramaIzq1 = (Nodo) archivo.leer(this.ramaIzq);
            ramaIzq1.setPadre(this.getPosicion());
            
            // igualmente el nodo de la rama central se le llama para indicar que tiene nuevo padre()
            this.ramaCen = nuevoNodo.getRamaCen();
            Nodo ramaCen1 = (Nodo) archivo.leer(this.ramaCen);
            ramaCen1.setPadre(this.getPosicion());
            
            // modificamos el numero de elementos del nodo interno fusionado ya que esta vez consta con uno mas
            this.modificarNumElementos(1);
            
            // mandamos a guardar en las posiciones que les corresponde pero ya modificados con su respestivo padre
            archivo.Modificar(this.ramaIzq, ramaIzq1);
            archivo.Modificar(this.ramaCen, ramaCen1);
            archivo.Modificar(this.getPosicion(), this);
            
            // como ya hemos fusionado el nodo interno nuevo con el otro nodo, por consiguiente ya no ocupados dicho nodo
            // por lo tanto mandamos a eliminar
            archivo.eliminar(posNuevoNodo);
        }
        
        // ya que el comparador anterior dio falso entonces esto quiere decir que le nodo se lo puede agregrar nomas ya que esto
        // quedaria ordenado
        else{
            this.clave[1] = nuevoNodo.getClave(0);
            this.ramaCen = nuevoNodo.ramaIzq;
            Nodo ramaCen1 = (Nodo) archivo.leer(this.ramaCen);
            ramaCen1.setPadre(this.getPosicion());
            this.ramaDer = nuevoNodo.ramaCen;
            Nodo ramaDer1 = (Nodo) archivo.leer(this.ramaDer);
            ramaDer1.setPadre(this.getPosicion());
            this.modificarNumElementos(1);
            archivo.Modificar(this.ramaDer, ramaDer1);
            archivo.Modificar(this.ramaCen, ramaCen1);
            archivo.Modificar(this.getPosicion(), this);
            archivo.eliminar(posNuevoNodo);
        }       
    }
    
    public int dividiNodoInterno(int posNuevoNodo, ArbolArchivo archivo) throws SerializadorException{
        
        // creamos un nuevo nodo interno el cual va ser el igual al nodo innterno  que nos dio como resultado de la ultima insercion de
        // un nodo hoja, este nodo se va a fusionar con el otro nodo interno que se enncuentra en mismo nivel
        NodoInterno nodoInternoResultanteDivisionAnterior = (NodoInterno) archivo.leer(posNuevoNodo);
        
        
        NodoInterno nuevoNodoPadre;
        
        // en esa division va haber 3 casos ya que contamos con 3 elementos en los nodos internos
        // el primer comparador es para ver si la clave del nodo nuevo interno es menor que la clave en la
        // posicion uno del nodo padre el que se encuentra en el mismo nivel
        if(isMenorIgual((Clave)nodoInternoResultanteDivisionAnterior.getClave(0),(Clave)this.clave[0])){
            nuevoNodoPadre = new NodoInterno((Clave)this.clave[0]);
            nuevoNodoPadre.setPosicion(this.getPosicion());
            
            nodoInternoResultanteDivisionAnterior.setPadre(this.getPosicion());
            
            NodoInterno nuevaRamaCen = new NodoInterno((Clave)this.clave[1]);
            int posNuevaRamaCen = archivo.escribir(nuevaRamaCen);
            nuevaRamaCen.setPosicion(posNuevaRamaCen);
            nuevaRamaCen.setRamaIzq(this.ramaCen);
            nuevaRamaCen.setRamaCen(this.ramaDer);
            nuevaRamaCen.setPadre(this.getPosicion());
            archivo.Modificar(posNuevaRamaCen, nuevaRamaCen);
            archivo.Modificar(nodoInternoResultanteDivisionAnterior.getPosicion(), nodoInternoResultanteDivisionAnterior);
            
            Nodo ramaDer1 = (Nodo) archivo.leer(this.ramaDer);
            Nodo ramaCen1 = (Nodo) archivo.leer(this.ramaCen);
            ramaDer1.setPadre(nuevaRamaCen.getPosicion());
            ramaCen1.setPadre(nuevaRamaCen.getPosicion());
            archivo.Modificar(ramaDer1.getPosicion(), ramaDer1);
            archivo.Modificar(ramaCen1.getPosicion(), ramaCen1);
            
            nuevoNodoPadre.setRamaCen(nuevaRamaCen.getPosicion());
            nuevoNodoPadre.setRamaIzq(nodoInternoResultanteDivisionAnterior.getPosicion());
           
            nuevoNodoPadre.setPadre(this.getPadre()); 
            
            archivo.Modificar(this.getPosicion(), nuevoNodoPadre);
        }
        // esta  opcion se cumple cuando la clave del nodo nuevo osea el que vamos a fusionar es mayor 
        // a la clave del nodo en la posicion 1
        else if(isMayor((Clave)nodoInternoResultanteDivisionAnterior.getClave(0),(Clave)this.clave[1])){
            nuevoNodoPadre = new NodoInterno((Clave)this.clave[1]);
            
            nodoInternoResultanteDivisionAnterior.setPadre(this.getPosicion());
            archivo.Modificar(nodoInternoResultanteDivisionAnterior.getPosicion(), nodoInternoResultanteDivisionAnterior);
            
            nuevoNodoPadre.setRamaCen(nodoInternoResultanteDivisionAnterior.getPosicion());
            
            NodoInterno nuevaRamaIzq = new NodoInterno((Clave)this.clave[0]);
            int posNuevaRamaIzq = archivo.escribir(nuevaRamaIzq);
            nuevaRamaIzq.setPosicion(posNuevaRamaIzq);
            nuevaRamaIzq.setPadre(this.getPosicion());
            
            
            Nodo ramaIzq1 = (Nodo) archivo.leer(this.ramaIzq);
            Nodo ramaCen1 = (Nodo) archivo.leer(this.ramaCen);
            ramaIzq1.setPadre(nuevaRamaIzq.getPosicion());
            ramaCen1.setPadre(nuevaRamaIzq.getPosicion());
            archivo.Modificar(ramaIzq1.getPosicion(), ramaIzq1);
            archivo.Modificar(ramaCen1.getPosicion(), ramaCen1);
            
            nuevaRamaIzq.setRamaIzq(this.ramaIzq);
            nuevaRamaIzq.setRamaCen(this.ramaCen);
            
            nuevoNodoPadre.setRamaIzq(nuevaRamaIzq.getPosicion());
            nuevoNodoPadre.setPadre(this.getPadre());
            nuevoNodoPadre.setPosicion(this.getPosicion());
            
            archivo.Modificar(nuevaRamaIzq.getPosicion(), nuevaRamaIzq);
            archivo.Modificar(this.getPosicion(), nuevoNodoPadre);
        }
        // esta condicion se cumple cuando la clave del nodo a fusionar se ubica en medio de las claves 
        // del nodo padre
        else{
            // este nodo nodoInternoResultanteDivisionAnterior es el que va ir como padre de todo osea el que sube en el arbol
            nuevoNodoPadre = new NodoInterno(nodoInternoResultanteDivisionAnterior.getClave(0));
            
            // este nodo le asignamos la posicion de padre
            nuevoNodoPadre.setPosicion(this.getPosicion());
            
            // como ahora lo que vamos a dividir son nodos internos creamos nodos internos para ir ubicando las ramas del nodo padre 
            // que seria el nodo nuevaDivision
            // el nodo de la rama izquierda va ser el el nodo que se encuentra en la posicion 0 en el nodointerno padre
            NodoInterno nuevaRamaIzqPadre = new NodoInterno((Clave)this.clave[0]);
            
            // escribimos el nuevo nodo interno que es la rama izquiera del nuevo nodo padre (nuevaDivision) y obtenemos el puntero de dond se 
            // se encuentra ubicado en el archivo(posNuevaRamaIzq)
            int posNuevaRamaIzqPadre = archivo.escribir(nuevaRamaIzqPadre);
            
            // le ubicamos a esta rama la posicion dond se encuentra guardada en el archivo del arbol
            nuevaRamaIzqPadre.setPosicion(posNuevaRamaIzqPadre);
            
            // le asignamos al padre que pertenece el cual es el nodo NuevaDivision
            nuevaRamaIzqPadre.setPadre(this.getPosicion()); 
            
            // obtenemos de nuevo el nodo de rama izquierda con el fin de reescribir los cambios que se hizo y mandamos al archivo para modificar
            // la el nodo de la rama izquiera del nodo que contiene dos elementos la tomamos y le asignamos su nuevo padre
            // que es la rama izquierda de la nueva raiz
            Nodo ramaIzqPadreAntiguo = (Nodo) archivo.leer(this.ramaIzq);
            ramaIzqPadreAntiguo.setPadre(nuevaRamaIzqPadre.getPosicion());
            archivo.Modificar(ramaIzqPadreAntiguo.getPosicion(), ramaIzqPadreAntiguo);
            
            // tomamos el nodo de la izquierda del nodo interno resultado de la division anterior el cual le asignamos su padre
            // y lo ubicamos como el nodo central de la rama izquierda del nuevo padre
            // y el nodo izquiedo del nodo interno que contiene los dos elementos cogemos el de la rama izquierda
            // y le asignnamos como nodo izquiero de la rama izquerda del nuevo padre
            Nodo nuevoNodoRamaIzq = (Nodo) archivo.leer(nodoInternoResultanteDivisionAnterior.getRamaIzq());
            nuevoNodoRamaIzq.setPadre(nuevaRamaIzqPadre.getPosicion());
            archivo.Modificar(nuevoNodoRamaIzq.getPosicion(), nuevoNodoRamaIzq);
            
            nuevaRamaIzqPadre.setRamaIzq(this.ramaIzq);
            nuevaRamaIzqPadre.setRamaCen(nodoInternoResultanteDivisionAnterior.getRamaIzq());
            
            
            
            // creamos la rama central del nuevo padre el cual va ser el nodo interno de la pos 1
            NodoInterno nuevaRamaCentralPadre = new NodoInterno((Clave)this.clave[1]);
            
            //escribimos el nuevo nodo interno que sera la rama central del nuevo padre
            int posNuevaRamaCenPadre = archivo.escribir(nuevaRamaCentralPadre);
            
            // le asignamos la ubicacion que se encuentra en el archivo del arbol
            nuevaRamaCentralPadre.setPosicion(posNuevaRamaCenPadre);
            
            // le asignamos su padre 
            nuevaRamaCentralPadre.setPadre(this.getPosicion());
            
            // sacamos el nodo de la rama derecha del nodo padre antiguo, el cual viene ser el nodo central de la rama central de
            // el nuevo padre
            Nodo nodoCentralRamaCentral = (Nodo) archivo.leer(this.ramaDer);
            
            // le asignamos su padre el cual es la rama central del padre nuevo
            nodoCentralRamaCentral.setPadre(nuevaRamaCentralPadre.getPosicion());
            
            // mandamos a modificar este nodo con los cambios que se realizo
            archivo.Modificar(nodoCentralRamaCentral.getPosicion(), nodoCentralRamaCentral);
            
            // obtenemos el nodo central del nodo interno resultalde la la division anterior el cual le asignamos su nuevo padre
            //  que es la neuva rama central de padre nuevo y mandamos a guardar las modificaciones
            Nodo nuevoNodoRamaCen = (Nodo) archivo.leer(nodoInternoResultanteDivisionAnterior.getRamaCen());
            nuevoNodoRamaCen.setPadre(nuevaRamaCentralPadre.getPosicion());
            archivo.Modificar(nuevoNodoRamaCen.getPosicion(), nuevoNodoRamaCen);
            
            // asignamos la rama izquierda de la rama  central del nuevo padre que es la rama central del nodo resultante de la division
            nuevaRamaCentralPadre.setRamaIzq(nodoInternoResultanteDivisionAnterior.ramaCen);
            
            // la rama derecha del nodo que tenia los dos elementos este sera el central de la nueva rama central del nuevo padre
            nuevaRamaCentralPadre.setRamaCen(this.ramaDer);

            //se manda a guardar los cambios que se les hizo a los nodos
            archivo.Modificar(nuevaRamaIzqPadre.getPosicion(), nuevaRamaIzqPadre);
            archivo.Modificar(nuevaRamaCentralPadre.getPosicion(), nuevaRamaCentralPadre);
            
            // al nuevo padre le asignamos la rama izquierda y la derecha respectivamente
            nuevoNodoPadre.setRamaIzq(nuevaRamaIzqPadre.getPosicion());
            nuevoNodoPadre.setRamaCen(nuevaRamaCentralPadre.getPosicion());
            
            // al nuevo padre se lo asigna como el nuevo padre oficial
            nuevoNodoPadre.setPadre(this.getPadre());
            
            // modificacon el nodo padre que esta recien creado
            archivo.Modificar(this.getPosicion(), nuevoNodoPadre);
            
            // eliminamos el nodo anterior que nos resulto de la division
            archivo.eliminar(nodoInternoResultanteDivisionAnterior.getPosicion());
        }
        if(this.getPadre() == 0){
            return nuevoNodoPadre.getPosicion(); //La raiz ha cambiado
        }
        NodoInterno parent = (NodoInterno) archivo.leer(this.getPadre());
        if(parent.isFull()){
            int resultado = parent.dividiNodoInterno(nuevoNodoPadre.getPosicion(),archivo);
            if (resultado != 0){
                NodoInterno parent1 = (NodoInterno) archivo.leer(resultado);
                if(parent1.getPadre() == 0)
                    return resultado;
            }
        }
        else if(!parent.isEmpty() && !parent.isFull()){
            parent.insertar(nuevoNodoPadre.getPosicion(),archivo);
        }
        return 0; //Si la raiz sigue siendo la misma
    }
    
    public void eliminar(ArbolArchivo archivo) throws SerializadorException, ArbolException{
        int ramaActual = this.validarRamaPerteneceNodo(archivo);
        NodoInterno parent = (NodoInterno) archivo.leer(this.getPadre());
        if(parent.getNumElementos() == 2){
            if(ramaActual == 0){
                NodoInterno hermanoDer = (NodoInterno) archivo.leer(parent.getRamaCen());
                if(hermanoDer.getNumElementos() == 2)   this.robarDerecha(ramaActual, archivo, parent, hermanoDer);
                else{
                    this.FusionarIzquierda(ramaActual, archivo, parent, hermanoDer);
                }
            }
            else if(ramaActual == 2){
                NodoInterno hermanoIzq = (NodoInterno) archivo.leer(parent.getRamaCen());
                if(hermanoIzq.getNumElementos() == 2)   this.robarIzquierda(ramaActual, archivo, parent, hermanoIzq);
                else{
                    this.FusionarDerecha(ramaActual, archivo, parent, hermanoIzq);
                }
            }
            else if(ramaActual == 1){
                NodoInterno hermanoDer = (NodoInterno) archivo.leer(parent.getRamaDer());
                NodoInterno hermanoIzq = (NodoInterno) archivo.leer(parent.getRamaIzq());
                if(hermanoIzq.getNumElementos() == 2)   this.robarIzquierda(ramaActual, archivo, parent, hermanoIzq);
                else if(hermanoDer.getNumElementos() == 2) this.robarDerecha(ramaActual, archivo, parent, hermanoDer);
                else{
                    this.FusionarIzquierda(ramaActual, archivo, parent, hermanoIzq);
                }
            }
        }
        else if(parent.getNumElementos() == 1){
            if(ramaActual == 0){
                NodoInterno hermanoDer = (NodoInterno) archivo.leer(parent.getRamaCen());
                if(hermanoDer.getNumElementos() == 2) this.robarDerecha(ramaActual, archivo, parent, hermanoDer);
                else{
                    this.FusionarIzquierda(ramaActual, archivo, parent, hermanoDer);
                }
            }else if(ramaActual == 1){
                NodoInterno hermanoIzq = (NodoInterno) archivo.leer(parent.getRamaIzq());
                if(hermanoIzq.getNumElementos() == 2) this.robarIzquierda(ramaActual, archivo, parent, hermanoIzq);
                else{
                    this.FusionarIzquierda(ramaActual, archivo, parent, hermanoIzq);
                }
            }
        }
        parent = (NodoInterno) archivo.leer(this.getPadre());
        if(parent.getNumElementos() == 0){
            if(parent.getPadre() == 0){
                archivo.setRaiz(this.getPosicion());
                this.setPadre(0);
                archivo.Modificar(this.getPosicion(), this);
            }else{
                parent.eliminar(archivo);
            }
        }
    }
    
    public void robarDerecha(Integer ramaActual, ArbolArchivo archivo, NodoInterno parent, NodoInterno hermanoDer) throws SerializadorException{
        
        this.setNumElementos(1);
        if(ramaActual == 0){
            this.setClave(0, (Clave) parent.getClave(0));
            parent.setClave(0, hermanoDer.getClave(0));
        }else if(ramaActual == 1){
            this.setClave(0, (Clave) parent.getClave(1));
            parent.setClave(1, hermanoDer.getClave(0));
        }
        hermanoDer.setClave(0, hermanoDer.getClave(1));
        hermanoDer.setClave(1, null);
        hermanoDer.setNumElementos(1);
        this.setRamaCen(hermanoDer.getRamaIzq());
        Nodo nodoMovido = (Nodo) archivo.leer(this.getRamaCen());
        nodoMovido.setPadre(this.getPosicion());
        archivo.Modificar(nodoMovido.getPosicion(), nodoMovido);
        hermanoDer.setRamaIzq(hermanoDer.getRamaCen());
        hermanoDer.setRamaCen(hermanoDer.getRamaDer());
        hermanoDer.setRamaDer(0);
        archivo.Modificar(this.getPosicion(), this);
        archivo.Modificar(parent.getPosicion(), parent);
        archivo.Modificar(hermanoDer.getPosicion(), hermanoDer);
    }
    
    public void robarIzquierda(Integer ramaActual, ArbolArchivo archivo, NodoInterno parent, NodoInterno hermanoIzq) throws SerializadorException{
        
        this.setNumElementos(1);
        if(ramaActual == 1){
            this.setClave(0, (Clave) parent.getClave(0));
            parent.setClave(0, hermanoIzq.getClave(1));
        }else if(ramaActual == 2){
            this.setClave(0, (Clave) parent.getClave(1));
            parent.setClave(1, hermanoIzq.getClave(1));
        }
        hermanoIzq.setClave(1, null);
        hermanoIzq.setNumElementos(1);
        this.setRamaCen(this.getRamaIzq());
        this.setRamaIzq(hermanoIzq.getRamaDer());
        Nodo nodoMovido = (Nodo) archivo.leer(this.getRamaIzq());
        nodoMovido.setPadre(this.getPosicion());
        archivo.Modificar(nodoMovido.getPosicion(), nodoMovido);
        hermanoIzq.setRamaDer(0);
        archivo.Modificar(this.getPosicion(), this);
        archivo.Modificar(parent.getPosicion(), parent);
        archivo.Modificar(hermanoIzq.getPosicion(), hermanoIzq);
    }
    
    public void FusionarIzquierda(Integer ramaActual, ArbolArchivo archivo, NodoInterno parent, NodoInterno hermano) throws SerializadorException{
        this.setNumElementos(2);
        if(ramaActual == 0){
            this.setClave(0, (Clave) parent.getClave(0));
            this.setClave(1, (Clave) hermano.getClave(0));  
            this.setRamaCen(hermano.getRamaIzq());
            this.setRamaDer(hermano.getRamaCen());
            Nodo nuevaRama1 = (Nodo) archivo.leer(this.getRamaCen());
            Nodo nuevaRama2 = (Nodo) archivo.leer(this.getRamaDer());
            nuevaRama1.setPadre(this.getPosicion());
            nuevaRama2.setPadre(this.getPosicion());
            archivo.Modificar(nuevaRama1.getPosicion(), nuevaRama1);
            archivo.Modificar(nuevaRama2.getPosicion(), nuevaRama2);
        }else if(ramaActual == 1){
            this.setClave(1, (Clave) parent.getClave(0));
            this.setClave(0, (Clave) hermano.getClave(0));
            this.setRamaDer(this.getRamaIzq());
            this.setRamaIzq(hermano.getRamaIzq());
            this.setRamaCen(hermano.getRamaCen());
            Nodo nuevaRama1 = (Nodo) archivo.leer(this.getRamaIzq());
            Nodo nuevaRama2 = (Nodo) archivo.leer(this.getRamaCen());
            nuevaRama1.setPadre(this.getPosicion());
            nuevaRama2.setPadre(this.getPosicion());
            archivo.Modificar(nuevaRama1.getPosicion(), nuevaRama1);
            archivo.Modificar(nuevaRama2.getPosicion(), nuevaRama2);
        }
        parent.setClave(0, parent.getClave(1));
        parent.setClave(1, null);
        parent.modificarNumElementos(-1);
        parent.setRamaIzq(this.getPosicion());
        parent.setRamaCen(parent.getRamaDer());
        parent.setRamaDer(0);
        archivo.Modificar(parent.getPosicion(), parent);
        archivo.Modificar(this.getPosicion(), this);
        archivo.eliminar(hermano.getPosicion());
    }
    
        public void FusionarDerecha(Integer ramaActual, ArbolArchivo archivo, NodoInterno parent, NodoInterno hermano) throws SerializadorException{
        this.setNumElementos(2);
        this.setClave(1, (Clave) parent.getClave(1));
        this.setClave(0, (Clave) hermano.getClave(0));  
        this.setRamaDer(this.getRamaIzq());
        this.setRamaCen(hermano.getRamaCen());
        this.setRamaIzq(hermano.getRamaIzq());
        Nodo nuevaRama1 = (Nodo) archivo.leer(this.getRamaIzq());
        Nodo nuevaRama2 = (Nodo) archivo.leer(this.getRamaCen());
        nuevaRama1.setPadre(this.getPosicion());
        nuevaRama2.setPadre(this.getPosicion());
        archivo.Modificar(nuevaRama1.getPosicion(), nuevaRama1);
        archivo.Modificar(nuevaRama2.getPosicion(), nuevaRama2);
        parent.setClave(1, null);
        parent.modificarNumElementos(-1);
        parent.setRamaCen(this.getPosicion());
        parent.setRamaDer(0);
        archivo.Modificar(parent.getPosicion(), parent);
        archivo.Modificar(this.getPosicion(), this);
        archivo.eliminar(hermano.getPosicion());
    }

    public int getRamaIzq() {
        return ramaIzq;
    }

    public void setRamaIzq(int ramaIzq) {
        this.ramaIzq = ramaIzq;
    }

    public int getRamaCen() {
        return ramaCen;
    }

    public void setRamaCen(int ramaCen) {
        this.ramaCen = ramaCen;
    }

    public int getRamaDer() {
        return ramaDer;
    }

    public void setRamaDer(int ramaDer) {
        this.ramaDer = ramaDer;
    }


  

    public Clave getClave(int i) {
        return (Clave)clave[i];
    }
    
    public void setClave(int i, Clave clave) {
        this.clave[i] = clave;
    }
    
    
    
}
