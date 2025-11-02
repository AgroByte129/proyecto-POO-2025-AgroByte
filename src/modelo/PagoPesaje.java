package modelo;

import java.time.LocalDate;
import java.util.List;

public class PagoPesaje {
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
    public double getMonto(){}
    public Pesaje[] getPesajes(){return pesaje.toArray(new Pesaje[0]);}
}
