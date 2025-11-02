package modelo;

import utilidades.Calidad;

import java.time.LocalDateTime;

public class Pesaje {
    private int id;
    private double cantidadKg;
    private Calidad calidad;
    private LocalDateTime fechaHora;
    private double precioKg;

    private CosechadorAsignado cosAsig;
    private PagoPesaje pagoPesaje;

    public Pesaje(int id, double cant, Calidad cal,
                  LocalDateTime fechaHora, CosechadorAsignado cosAsgn){
        this.id = id;
        this.cantidadKg = cant;
        this.calidad = cal;
        this.fechaHora = fechaHora;
        this.cosAsig = cosAsgn;
    }

    public int getId() {return id;}
    public double getCantidadKg() {return cantidadKg;}
    public Calidad getCalidad() {return calidad;}
    public LocalDateTime getFechaHora() {return fechaHora;}
    public double getPrecioKg() {return precioKg;}
    public double getMonto(){}
    public CosechadorAsignado getCosechadorAsignado() {return cosAsig;}
    public void setPago(PagoPesaje pago){}
    public boolean isPagado(){}
    public PagoPesaje getPagoPesaje(){return pagoPesaje;}
}
