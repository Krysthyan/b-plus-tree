/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolb;

import java.io.Serializable;

/**
 *
 * @author krysthyan
 */
public class Vehiculo implements Serializable{
    private String placa;
    private String marca;


    public Vehiculo(String placa, String marca) {
        this.placa = placa;
        this.marca = marca;

    }

    /**
     * @return the placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * @param placa the placa to set
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    } 
    
    @Override
    public String toString(){
        return "Vehiculo :"+this.placa.toString()+" "+this.marca.toString();
                
    }
}