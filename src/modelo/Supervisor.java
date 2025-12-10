package modelo;

import utilidades.Rut;

import java.io.Serializable;

public class Supervisor extends Persona implements Serializable {
  private String profesion;
  private Cuadrilla cuad;

  public Supervisor(Rut rut, String nombre, String email, String direccion, String profesion){
    super(rut, nombre, email, direccion); //Llama al constructor de modelo.Persona
    this.profesion = profesion;
  }
  
  public String getProfesion(){return profesion;}
  public void setProfesion(String profesion){this.profesion = profesion;}
  public void setCuadrilla(Cuadrilla cuad){this.cuad  = cuad;}
  public Cuadrilla getCuadrilla(){return cuad;}
}



