# Implementación del árbol b+ en java.

## Caracteristicas de la implementación.

> Archivos de acceso aleatorio (RamdomAccessFile).

> Árbol generico para cualquier tipo de objeto.

> Insersión se da poniendo el tamaño del objeto, proximamente será dinamico.

> Serialización de objetos.

> Se puede crear varios árboles para un mismo archivo de objetos.

> extención arbol (.arb), extención de archivo de objetos (.dat).

>La implementación es realizada en archivos de acceso aleatorio serializados

## Como utlizar?

### Utilizar en proyecto nuevo

- Dentro del proyecto existe un paquete llamado **arbol_b_mas**, copiar dicho
paquete dentro de su proyecto.
- Llamar la clase _DespositoArchivos< clave, Objeto >_, donde le pasamos como parametros
la clave que puede ser de cualquier tipo de dato y siguiente el objeto que se desea 
guardar, guiarse en la implementación que se encuentra a continuación.

### Probar la implementación de este proyecto.

- Este proyecto esta realizado en [netbeans](https://netbeans.org/downloads/).

#### Descargar proyecto y ejecutar en netbeans
- git clone https://github.com/Krysthyan/Arbol_B_Mas.git
- cd Arbol_B_Mas
- Abrir netbeans, dar click en icono de **abrir proyecto** y buscar la ubicación 
del proyecto que se ha descargado, ejecutamos el proyectto  y puede utilizar el objeto que 
esta como ejemplo.

```java
import arbolb.arbol_b_mas.ArbolException;
import arbolb.arbol_b_mas.DepositoArchivos;
import arbolb.arbol_b_mas.SerializadorException;

public static void main(String[] args) {
        DepositoArchivos<String,Vehiculo> vehiculos=new DepositoArchivos("src/","vehiculos",1000);
        if(opcion == 1){
            vehiculos.agregar(placa, new Vehiculo(placa,marca));
        }
        if(opcion == 2){
            System.out.println("resultado de funcion buscar "+vehiculos.exists(placa));
        }
        if(opcion == 3){
            vehiculos.modificar(placaModificar, new Vehiculo(placa,marca));
        }
        if(opcion == 4){
            vehiculos.eliminar(placa);
        }
        if(opcion == 5){
            Vehiculo leerVehiculo=vehiculos.get(placa);
            System.out.println("Vehiculo de clace abs   "+leerVehiculo.toString());
        }
        if(opcion == 6){
            List<Vehiculo> lista = vehiculos.listar();
            lista.stream().forEach((vehiculo) -> {
                System.out.println(vehiculo.toString());
            });
        }
    }
public static void menu(){
    System.out.println("\n\nIMPLEMENTACION ARBOL++ EN ARCHIVOS");
    System.out.println("1.-agregar un objeto");
    System.out.println("2.-Comprobar existencia de un objeto");
    System.out.println("3.-Modificar un objeto");
    System.out.println("4.-Eliminar un objeto");
    System.out.println("5.-Obtener objeto por clave");
    System.out.println("6.-Listar objetos");
    System.out.println("7.-Salir");
        
    }


```
