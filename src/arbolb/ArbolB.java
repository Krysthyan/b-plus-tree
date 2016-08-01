/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolb;

import arbolb.arbol_mas.ArbolException;
import arbolb.arbol_mas.DepositoArchivos;
import arbolb.arbol_mas.Repositorio;
import arbolb.arbol_mas.SerializadorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.applet.Main;

/**
 *
 * @author krysthyan
 */


public class ArbolB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SerializadorException, IOException {
        int validar=0;
        int opcion;
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        try {
            DepositoArchivos<String,Vehiculo> vehiculos=new DepositoArchivos("src/","vehiculos",1000);
            while(validar==0){
                menu();
                opcion=INT(br.readLine());
                if(opcion==1){
                    System.out.println("Ingrese la placa de su vehiculo ");
                    String placa=br.readLine();
                    System.out.println("Ingrese la marca de su vehiculo");
                    String marca=br.readLine();
                    vehiculos.agregar(placa, new Vehiculo(placa,marca));
                }
                if(opcion==2){
                    System.out.println("Ingrese la placa que quiere buscar");
                    String placa=br.readLine();
                    System.out.println("resultado de funcion buscar "+vehiculos.exists(placa));
                }
                if(opcion==3){
                    System.out.println("Ingrese la placa de su vehiculo que quiere modificar ");
                    String placaModificar=br.readLine();
                    System.out.println("Ingrese la placa de su vehiculo nueva");
                    String placa=br.readLine();
                    System.out.println("Ingrese la marca de su vehiculo nueva");
                    String marca=br.readLine();
                    vehiculos.modificar(placaModificar, new Vehiculo(placa,marca));
                }
                if(opcion==4){
                    System.out.println("Ingrese la placa que quiere eliminar");
                    String placa=br.readLine();
                    vehiculos.eliminar(placa);
                }
                if(opcion==5){
                    System.out.println("Ingrese la placa que quiere obtener");
                    String placa=br.readLine();
                    Vehiculo leerVehiculo=vehiculos.get(placa);
                    System.out.println("Vehiculo de clace abs   "+leerVehiculo.toString());
                }
                if(opcion==6){
                    List<Vehiculo> lista = vehiculos.listar();
                    System.out.println("\nLista");
                    for(Vehiculo vehiculo: lista){
                        System.out.println(vehiculo.toString());
                    }
                }
                if(opcion==7){
                    validar=1;
                }
            }

        } catch (ArbolException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static int INT(String numero){
        return Integer.parseInt(numero);
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
       
    
}
