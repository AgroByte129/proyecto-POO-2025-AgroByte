package modelo;

import utilidades.Rut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Propietario extends Persona implements Serializable {
    private String direccionCom;
    private List<Huerto> huertos = new ArrayList<>();

    public Propietario(Rut rut, String nom, String email, String dir, String direccionCom) {
        super(rut, nom, email,  dir);
        this.direccionCom = direccionCom;
    }

    public String getDireccionComercial(){return direccionCom;}

    public void setDireccionComercial(String direccion){direccionCom = direccion;}

    public boolean addHuerto(Huerto huerto){

        for (Huerto existente : huertos) {

            if (existente.getNombre().equalsIgnoreCase(huerto.getNombre())) {
                return false;
            }
        }

        huertos.add(huerto);

        return true;
    }

    public Huerto[] getHuertos(){
        return huertos.toArray(new Huerto[0]);
    }
}





