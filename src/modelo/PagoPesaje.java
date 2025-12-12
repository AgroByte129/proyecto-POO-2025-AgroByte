package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class PagoPesaje implements Serializable {
    private int id;
    private LocalDate fecha;

    private List<Pesaje> pesaje;

    public PagoPesaje(int id, LocalDate fecha, List<Pesaje> pesaje){
        this.id = id;
        this.fecha = fecha;
        this.pesaje = pesaje;
    }
    public int getId() {return id;}
    public LocalDate getFecha(){return fecha;}
    public double getMonto(){
        double total = 0.0;
        for (Pesaje p : pesaje) total += p.getMonto();
        return total;
    }
    public Pesaje[] getPesajes(){return pesaje.toArray(Pesaje[]::new);}
}
