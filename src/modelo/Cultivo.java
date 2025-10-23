package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cultivo {
    private int id;
    private String especie;
    private String variedad;
    private float rendimiento;

    private List<Cuartel> cuarteles = new ArrayList<>();

    public Cultivo(int id, String especie, String variedad, float rendimiento) {
        this.id = id;
        this.especie = especie;
        this.variedad = variedad;
        this.rendimiento = rendimiento;
    }

    public int getId() {return id;}
    public String getEspecie() {return especie;}
    public String getVariedad() {return variedad;}
    public float getRendimiento() {return rendimiento;}
    public void setRendimiento(float rendimiento) {
        this.rendimiento = rendimiento;
    }
    public boolean addCuartel(Cuartel cuartel) {
        for(Cuartel c : cuarteles) {//puede tener duplicados?
            if(c.getId() == cuartel.getId()) {
                return false;
            }
        }
        return cuarteles.add(cuartel);
    }
    public Cuartel[] getCuarteles() {return cuarteles.toArray(new Cuartel[0]);}
}
