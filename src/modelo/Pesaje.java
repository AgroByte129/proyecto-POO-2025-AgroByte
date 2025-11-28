package modelo;

import utilidades.Calidad;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Pesaje implements Serializable {
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
        this.precioKg = cosAsig.getCuadrilla().getPlanCosecha().getPrecioBaseKilo();
        cosAsgn.addPesaje(this); //relación bidireccional
    }

    public int getId() {return id;}
    public double getCantidadKg() {return cantidadKg;}
    public Calidad getCalidad() {return calidad;}
    public LocalDateTime getFechaHora() {return fechaHora;}
    public double getPrecioKg() {return precioKg;}
    public double getMonto(){
        double factor = switch (calidad) {
            case EXCELENTE -> 1.0; //redundante pero deja explícito que si es excelente se deja el precio completo
            case SUFICIENTE -> 0.8;
            case DEFICIENTE -> 0.6;
            default -> 1.0;
        };
        return precioKg * cantidadKg * factor;
    }
    public CosechadorAsignado getCosechadorAsignado() {return cosAsig;}
    public void setPago(PagoPesaje pago){
        this.pagoPesaje = pago;
    }
    public boolean isPagado(){
        return this.pagoPesaje != null;
    }
    public PagoPesaje getPagoPesaje(){return pagoPesaje;}
}
