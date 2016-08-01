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
public class NodoHoja<Clave extends Comparable<Clave>> extends Nodo{
    private Elemento[] elementos = new Elemento[2];
    private int padre = 0;
    private int next = 0;
    private int prev = 0;
    
    
    
    // este contructor es cuando se desea crear un nodo hoja con un solo elemento
    // el  numElementos para ver cuantos elementos tiene el nodo
    
    protected NodoHoja(Elemento elem1){
        this.elementos[0] = elem1;
        this.setNumElementos(1);
        this.setTipo(TipoNodo.HOJA);
    }
    
    // este constructor es cuando se crea un nodo hoja pero este va a llevar dos elementos
    protected NodoHoja(Elemento elem1,Elemento elem2){
        this.elementos[0] = elem1;
        this.elementos[1] = elem2;
        this.setNumElementos(2);
        this.setTipo(TipoNodo.HOJA);
    }
    // esta funcion ordena cuando son dos elementos ingresados en un nodo
    public void ordenar(){
        if(isMenorIgual((Clave)this.elementos[1].getClave(),(Clave)this.elementos[0].getClave())){
            Elemento aux = this.elementos[0];
            this.elementos[0] = this.elementos[1];
            this.elementos[1] = aux;
        }
    }
    /// esta funcion de ordenar es cuando se tiene 3 elementos en el nodo
    public Elemento[] ordenar(Elemento nuevo){
        Elemento elementos[] = new Elemento[3];
        if(isMenorIgual((Clave)nuevo.getClave(),(Clave)this.elementos[0].getClave())){
            elementos[0] = nuevo;
            elementos[1] = this.elementos[0];
            elementos[2] = this.elementos[1];
        }
        else if(isMayor((Clave)nuevo.getClave(), (Clave)this.elementos[1].getClave())){
            elementos[2] = nuevo;
            elementos[0] = this.elementos[0];
            elementos[1] = this.elementos[1];
        }
        else{
            elementos[1] = nuevo;
            elementos[0] = this.elementos[0];
            elementos[2] = this.elementos[1];
        }
        return elementos;
    }
    
    public int addElemento(Clave clave, int posicion, ArbolArchivo archivo) throws SerializadorException{
        if (this.isHoja()){
            if(this.isEmpty()){
                this.elementos[0]  = new Elemento(clave,posicion);
                this.modificarNumElementos(1);
                archivo.Modificar(this.getPosicion(), this);
                return 0;
            }
            else if (!this.isFull()){
                this.elementos[1] = new Elemento(clave, posicion);
                this.ordenar();
                this.modificarNumElementos(1);
                archivo.Modificar(this.getPosicion(), this);
                return 0;
            }
            else{
                Elemento nuevoElemento = new Elemento(clave,posicion);
                Elemento[] ordenado = this.ordenar(nuevoElemento);
                
                
                // crea un nodo interno nuevo y toma el elemento de enmedio del arreglo ordenado ordenado[1].getClave()
                
                NodoInterno<Clave> nodoInterno = new NodoInterno<Clave>((Clave)ordenado[1].getClave());
                // creamos un nuevo nodo hoja el cual va ser el de la izquierda y va a llevar un elemento del arreglo ordenado
                // el cual sera una hoja con un elemento ordenado[0]
                NodoHoja NodoHojaIzq = new NodoHoja(ordenado[0]);
                
                // primero antes de crear un nodo derecho siempre se crea un nodo hoja central el cual se lleva los dos elementos
                // restantes ordenado[1] y ordenado[2]
                NodoHoja NodoHojaCen = new NodoHoja(ordenado[1],ordenado[2]);
                
                // escribimos los nodos en el arbol a continuacion de lo que ya se ha guardado y sacamos la posicion donde ha
                // sido guardado el el nodo izquierdo y el nodo central (hojas) las posiciones
                int posNodoHojaIzq = archivo.escribir(NodoHojaIzq);
                int posNodoHojaCen = archivo.escribir(NodoHojaCen);
                
                // en los nodos hojas colamos la posicion en el archivo respectivamente como se obtubo posNodoHojaIzq y posNodoHojaCen
                NodoHojaIzq.setPosicion(posNodoHojaIzq);
                NodoHojaCen.setPosicion(posNodoHojaCen);
                
                
                // ubicamos pos punteros de los nodos hojas ya que una de las condiciones del arbol b+ es que estan entrezados todos los nodos
                // hojas para asi  tener una eficiente recuperacion de los indices del arbol para que solo se recorre secuencialmente
                // los nodos hojas por lo  que ya se encuentran ordenados
                
                // colocamos en el nodo hoja ixquiero el puntero del nodo anterior
                NodoHojaIzq.setPrev(this.prev);
                
                //colocamos en el nodo central la posicion del nodo izquierdo que tiene a lado NodoHojaIzquierdo
                NodoHojaCen.setPrev(posNodoHojaIzq);
                
                // colocamos en el nodo hoja izquierdo la posicion del siguiente nodo que seria el nodo central
                NodoHojaIzq.setNext(posNodoHojaCen);
                
                // y el nodo central va a tener un siguiente el cual esta guardad this.next
                NodoHojaCen.setNext(this.next);
                
                
                // si el this.prev es diferente de cero es porque los nodos hojas de ese bloque no se encuentran al principio
                // por lo tanto tenemos que recuperar el nodo izquierdo(ojo no es el nodo de la linea 97 es el izquierdo de ese nodo) 
                // que para asignarle el next que vendria ser el nodo izquierdo
                // que guardamos en la linea 97, una vez hecho eso reescribimos ese nodo anterior pero con la diferencia que ya tiene el
                // punto del siguiente nodo que viene ser el nodo izquiero mencionado anteriormente.
                if(this.prev != 0){
                    NodoHoja anterior = (NodoHoja) archivo.leer(this.getPrev());
                    anterior.setNext(posNodoHojaIzq);
                    archivo.Modificar(anterior.getPosicion(), anterior);                    
                }
                
                // si el arbol tiene mas nodos guardados a la parte derecha debemos poner al puntero de ese nodo siguiente
                // que viene siendo el prev el cual contiene el nodo central----> por lo tanto el nodo anteror de siguiente viene ser el central
                // de la liena 98
                if(this.next != 0){
                    NodoHoja siguiente = (NodoHoja) archivo.leer(this.getNext());
                    siguiente.setPrev(posNodoHojaCen);
                    archivo.Modificar(siguiente.getPosicion(), siguiente);
                }
                
                // como se ha hecho una divison del nodo este nodo interno va ser el nodo padre de los nodos hojaa asi que  se obtiene la posicion de raiz del 
                // nodo anterior el cual tambien es el padre y le asignamos a los nodos hojas su padre linea 145 y 146
                nodoInterno.setPosicion(this.getPosicion());
                NodoHojaIzq.setPadre(this.getPosicion());
                NodoHojaCen.setPadre(this.getPosicion());
                
                // una vez hecho todos los cambios de los puntero de refencia de los nodos mandadon a reescribir en su posicion que fueron guardados
                // anteoirmente (nodo izquierdo y el nodo central)
                archivo.Modificar(NodoHojaIzq.getPosicion(), NodoHojaIzq);
                archivo.Modificar(NodoHojaCen.getPosicion(), NodoHojaCen);
                
                // le asignamos al nodo interno si tiene padre 
                nodoInterno.setPadre(this.getPadre());
                
                // al nodo interno le asignamos las ramas respectivas de sus nodos, setRamaIzq y setRamaCen ubicamos los punteros de los nodos hojas hijos
                nodoInterno.setRamaIzq(posNodoHojaIzq);
                nodoInterno.setRamaCen(posNodoHojaCen);
                
                // mandamos a modifica el nodo que etaba en la posicion de nodo padre e ingresa el nodo interno 
                archivo.Modificar(this.getPosicion() ,nodoInterno);
                
                // si es getPadre es igual a cero quiere decir que es el nodo interno es la raiz y retorna la posicion en el archivo de ese nodo interno
                if(this.getPadre() == 0){
                    return this.getPosicion();
                }
                // cuando se cra un nuevo nodo tenemos que ver si no hay un nodo al mismo nivel del msimo, si lo hay tenemos  que fusionar esos nodos internos
                // con la funcion insertar de la clase NodoInterno, pero llega un momento que de las fusiones ya se llena ese nodo fusionado
                // entonces entra la funcion dividirNodoInterno de la misma clase, como lo dice el nombre este divide el nodo interno en 3 nodos internos,
                // pero el nodo de enmedio sube de nivel y este viene a ser el padre de los otros nodos.
                // leemos el nodo padre osea un interno;
                
                NodoInterno padre = (NodoInterno)archivo.leer(this.getPadre());
                if(padre.isFull()){ 
                    
                    // la funcion padre.divirNodoInterno como lo dice divide el nodo cuando ya  esta lleno 
                    return padre.dividiNodoInterno(this.getPosicion(),archivo);
                }
                
                // encambio insertar lo que hace es fusionar dos nodos internos
                padre.insertar(this.getPosicion(),archivo);
            }
        }
        return 0;
    }
    
    // esta funcion va a realizar el eliminado del elemento en el arbol b+
    public void eliminar(Clave clave, ArbolArchivo archivo) throws SerializadorException, ArbolException{
        // obtenemos el numero de elementos del nodo hoja
        int numElementos = this.getNumElementos();
        
        // el metodo validarRamaPerteneceNodo es con el fin de saber en que rama se encuentra ubicado nuestro nodo
        int ramaPerteneceNodo = this.validarRamaPerteneceNodo(archivo);
        
        // obtenemos el padre de nuestro nodo hoja
        NodoInterno parent = (NodoInterno) archivo.leer(this.getPadre());
        
        // ahora validamos si el nodo hoja contiene uno o dos elementos
        if(numElementos == 2){
            if(isIgual(clave, (Clave)this.getElementos()[1].getClave())){
                this.getElementos()[1] = null;
                this.setNumElementos(1);
            }
            else if(isIgual(clave, (Clave)this.getElementos()[0].getClave())){
                this.setElemento(0, this.getElementos()[1]);
                this.setElemento(1, null);
                this.setNumElementos(1);
                if (ramaPerteneceNodo == 0) parent.cambiarClave(clave, (Clave)this.getElementos()[0].getClave(), archivo);
                else{
                    if (ramaPerteneceNodo == 1) parent.setClave(0, (Clave)this.getElementos()[0].getClave());
                    else if (ramaPerteneceNodo == 2) parent.setClave(1, (Clave)this.getElementos()[0].getClave());
                    archivo.Modificar(parent.getPosicion(), parent);
                }
            }
            archivo.Modificar(this.getPosicion(), this);
        }else if(numElementos == 1){
            // ahora comprobamos si el padre del nodo contiene uno o dos elementos 
            // con el fin de poder realizar la eliminacion el toda la jerarquia del arbol
            if(parent.getNumElementos() == 2){
                if(ramaPerteneceNodo == 0){
                   NodoHoja hermanoDer = (NodoHoja) archivo.leer(next);
                   if(hermanoDer.getNumElementos() == 2) this.robarDerecha(0, archivo, parent, hermanoDer);
                   else{
                       
                       parent.setRamaIzq(parent.getRamaCen());
                       parent.setRamaCen(parent.getRamaDer());
                       parent.cambiarClave(clave, (Clave)hermanoDer.getElementos()[0].getClave(), archivo);
                       parent.setRamaDer(0);
                       parent.setClave(0,(Clave) parent.getClave(1));
                       parent.setClave(1, null);
                       parent.setNumElementos(1);
                       this.cambiarListaLigada(archivo);
                       parent.cambiarClave(clave, (Clave)hermanoDer.getElementos()[0].getClave(), archivo);
                       archivo.Modificar(parent.getPosicion(), parent);
                       archivo.eliminar(this.getPosicion());
                   }
                }
                else if(ramaPerteneceNodo == 2){
                   NodoHoja hermanoIzq = (NodoHoja) archivo.leer(prev);
                   if(hermanoIzq.getNumElementos() == 2) this.robarIzquierda(2, archivo, parent, hermanoIzq);
                   else{
                       parent.setRamaDer(0);
                       parent.setClave(1, null);
                       parent.setNumElementos(1);
                       this.cambiarListaLigada(archivo);
                       archivo.Modificar(parent.getPosicion(), parent);
                       archivo.eliminar(this.getPosicion());
                   }
                }
                else if(ramaPerteneceNodo == 1){
                    NodoHoja hermanoIzq = (NodoHoja) archivo.leer(prev);
                    NodoHoja hermanoDer = (NodoHoja) archivo.leer(next);
                    if(hermanoDer.getNumElementos() == 2) this.robarDerecha(1, archivo, parent, hermanoDer);
                    else if(hermanoIzq.getNumElementos() == 2) this.robarIzquierda(1, archivo, parent, hermanoIzq);
                    else{
                        parent.setClave(0, parent.getClave(1));
                        parent.setClave(1, null);
                        parent.setNumElementos(1);
                        parent.setRamaCen(parent.getRamaDer());
                        parent.setRamaDer(0);
                        this.cambiarListaLigada(archivo);
                        archivo.Modificar(parent.getPosicion(), parent);
                        archivo.eliminar(this.getPosicion());
                    }
                }
            }else if(parent.getNumElementos() == 1){
                // la validacion de la rama a la que pertenece nuestro nodo la vamos a usar
                
                // ramaPerteneceNodo=0 -> ramaIzquierda
                // ramaPerteneceNodo=1 -> ramaCentral
                // ramaPerteneceNodo=2 -> ramaDerecha
                if(ramaPerteneceNodo == 0){
                    
                    // obtenemos el el nodo hoja hermano derecho con el puntero de next que se le asigno
                    // cuando se lo agrego al nodo hoja
                   NodoHoja hermanoDer = (NodoHoja) archivo.leer(next);
                   
                   // comprobamos si el hermano derecho tiene dos elementos para asi robarle un elemento
                   // donde ese metodo mandamos como parametros (ramaPerteneceNodo,archivoArbol,padre,hermanoDerecho)
                   if(hermanoDer.getNumElementos() == 2) this.robarDerecha(0, archivo, parent, hermanoDer);
                   
                   // si el hermano derecho tiene solamente un elemento vamos a utilizar el metodo de
                   // cambiarListaLigada
                   else{

                       this.cambiarListaLigada(archivo);
                       parent.setRamaIzq(parent.getRamaCen());
                       parent.cambiarClave(clave, (Clave)hermanoDer.getElementos()[0].getClave(), archivo);
                       parent.setRamaCen(0);
                       parent.setClave(0, null);
                       parent.setNumElementos(0);
                       archivo.eliminar(this.getPosicion());
                       archivo.Modificar(parent.getPosicion(), parent);
                       if(parent.getPadre() == 0 && parent.getNumElementos() == 0){
                            archivo.setRaiz(hermanoDer.getPosicion());
                            hermanoDer.setPadre(0);
                            hermanoDer.setNext(0);
                            hermanoDer.setPrev(0);
                            archivo.Modificar(hermanoDer.getPosicion(), hermanoDer);
                       }else{
                            parent.eliminar(archivo);
                       }
                   }
                }
                else if(ramaPerteneceNodo == 1){
                   NodoHoja hermanoIzq = (NodoHoja) archivo.leer(prev);
                   if(hermanoIzq.getNumElementos() == 2) this.robarIzquierda(1, archivo, parent, hermanoIzq);
                   else{
                       this.cambiarListaLigada(archivo);
                       parent.setRamaCen(0);
                       parent.setClave(0, null);
                       parent.setNumElementos(0);
                       archivo.eliminar(this.getPosicion());
                       archivo.Modificar(parent.getPosicion(), parent);
                       if(parent.getPadre() == 0 && parent.getNumElementos() == 0){
                            archivo.setRaiz(hermanoIzq.getPosicion());
                            hermanoIzq.setPadre(0);
                            hermanoIzq.setNext(0);
                            hermanoIzq.setPrev(0);
                            archivo.Modificar(hermanoIzq.getPosicion(), hermanoIzq);
                       }else{
                            parent.eliminar(archivo);
                       }
                   }
                }
            }
        }

    }
    
    public void cambiarListaLigada(ArbolArchivo archivo) throws SerializadorException{
        
        // este metodo lo que hace es hacer un puente entre los nodos siguiente y nodo anterior del nodo hoja
        // con los punteros prev y next 
        
        NodoHoja anterior = null;
        NodoHoja siguiente = null;
        if(this.getPrev() == 0){
            siguiente = (NodoHoja) archivo.leer(this.getNext());
            siguiente.setPrev(0);
            archivo.Modificar(siguiente.getPosicion(), siguiente);
        }else if(this.getNext() == 0){
            anterior = (NodoHoja) archivo.leer(this.getPrev());
            anterior.setNext(0);
            archivo.Modificar(anterior.getPosicion(), anterior);
        }else{
            siguiente = (NodoHoja) archivo.leer(this.getNext());
            anterior = (NodoHoja) archivo.leer(this.getPrev());
            siguiente.setPrev(anterior.getPosicion());
            anterior.setNext(siguiente.getPosicion());
            archivo.Modificar(anterior.getPosicion(), anterior);
            archivo.Modificar(siguiente.getPosicion(), siguiente);
        }
    }
    
    // la funcion de este metodo es robar un elemento del nodo que se encuentra a la derecha de nodo hoja
    public void robarDerecha(Integer tipoRama, ArbolArchivo archivo, NodoInterno parent, NodoHoja HermanoDer) throws SerializadorException{
        
        // robamos el elemento que se encuentra en la posicion 0 en el nodo hermano derecho
        parent.cambiarClave((Clave)this.getElementos()[0].getClave(), (Clave)HermanoDer.getElementos()[0].getClave() , archivo);
        
        // una vez actualizado las claves en todo el arbol procesedos con el robo del elemento
        // del hermano derecho el cual lo vamos asignar a la posicion 0 del nodo hoja
        this.getElementos()[0] = HermanoDer.getElementos()[0];
        
        // como hemos robado el elemento izquierdo del hermando derecho pasamos el elemento derecho al izquierdo del hermano derecho
        HermanoDer.setElemento(0, HermanoDer.getElementos()[1]);
        
        // para que no esten repetidas las claves en la posicion 1 del hermano derecho le asignamos un null
        HermanoDer.setElemento(1, null);
        
        // como hemos robado un elemento del hermano derecho tenemos que actualizar la cantidad de elementos del hermano derecho
        HermanoDer.setNumElementos(1);
        
        // si tipo de rama dond se encuentra ubicado nuestro nodo hoja es izquierdo pasamos al padre
        // como clave la clave del elemento del nodo hermano derecho de la posicion [0]
        if(tipoRama == 0) parent.setClave(0, (Clave)HermanoDer.getElementos()[0].getClave());
        
        // caso contrario si el tipo de rama es central (esto quiere decir que tiene 3 ramas, y el nodo padre tiene dos elementos)
        // el elemento en la pos 1 del padre ubicamos la clave del hermano derecho en la posicion 0 y el elemeto de la pos 0 del padre
        // ubicamos el elemento de nuestro nodo hoja el de la posicion [0].
        else if (tipoRama == 1) {
            parent.setClave(1, (Clave)HermanoDer.getElementos()[0].getClave());
            parent.setClave(0, (Clave)this.getElementos()[0].getClave());
        }
        // una vez hecho el robo y hecho los cambios respectivos mandamos actualizar el hermano el nodo hoja y el padre en el archivo del arbol.
        archivo.Modificar(HermanoDer.getPosicion(), HermanoDer);
        archivo.Modificar(this.getPosicion(), this);
        archivo.Modificar(parent.getPosicion(), parent);
    }
    
    
    public void robarIzquierda(Integer tipoRama, ArbolArchivo archivo, NodoInterno parent, NodoHoja HermanoIzq) throws SerializadorException{
        this.getElementos()[0] = HermanoIzq.getElementos()[1];
        HermanoIzq.setElemento(1, null);
        HermanoIzq.setNumElementos(1);
        if(tipoRama == 1) parent.setClave(0, (Clave)this.getElementos()[0].getClave());
        else if (tipoRama == 2) parent.setClave(1, (Clave)this.getElementos()[0].getClave());
        archivo.Modificar(HermanoIzq.getPosicion(), HermanoIzq);
        archivo.Modificar(this.getPosicion(), this);
        archivo.Modificar(parent.getPosicion(), parent);
    }

    public Elemento[] getElementos() {
        return elementos;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public void setElemento(int pos, Elemento elemento){
        this.getElementos()[pos] = elemento;
    }
    
    
}
