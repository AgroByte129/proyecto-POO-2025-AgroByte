package modelo;

import utilidades.Rut;

import java.io.Serializable;

public class Persona implements Serializable {
    private Rut rut;
    private String nombre;
    private String email;
    private String direccion;

    public Persona(Rut rut, String nombre, String email, String direccion) {
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
    }

    public Rut getRut() {return rut;}
    public String getNombre() {return nombre;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}
}

