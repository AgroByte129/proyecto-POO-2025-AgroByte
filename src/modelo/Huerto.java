package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Huerto implements Serializable {
    private String nombre;
    private float superficie;
    private String ubicacion;

    private Propietario propietario;
    private List<Cuartel> cuarteles = new ArrayList<>();

    public Huerto(String nombre, float superficie, String ubicacion, Propietario propietario) {
        this.nombre = nombre;
        this.superficie = superficie;
        this.ubicacion = ubicacion;
        this.propietario = propietario;
        this.propietario.addHuerto(this); //Relación bidireccional
    }

    public String getNombre() {return nombre;}
    public float getSuperficie() {return superficie;}
    public void setSuperficie(float superficie) {this.superficie = superficie;}
    public String getUbicacion() {return ubicacion;}
    public void setUbicacion(String ubicacion) {this.ubicacion = ubicacion;}
    public Propietario getPropietario() {return propietario;}
    public void setPropietario(Propietario propietario) {this.propietario = propietario;}

    public boolean addCuartel(int id, float sup, Cultivo cult) {
        if(getCuartel(id).isPresent()){return false;}//busca si ya hay cuartel duplicado

        Cuartel cuartel = new Cuartel(id, sup, cult, this);

        if(!cult.addCuartel(cuartel)){
            return false;
        }

        return cuarteles.add(cuartel);
    }

    public Optional<Cuartel> getCuartel(int id) {
        return cuarteles.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    public Cuartel[] getCuarteles() {
        return cuarteles.toArray(Cuartel[]::new);
    }
}
