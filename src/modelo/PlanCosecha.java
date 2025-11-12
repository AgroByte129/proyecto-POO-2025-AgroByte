package modelo;

import utilidades.EstadoPlan;
import java.time.LocalDate;
import java.util.ArrayList;
import utilidades.GestionHuertosException;


public class PlanCosecha {
    private int id;
    private String nombre;
    private LocalDate inicio;
    private LocalDate finEstimado;
    private LocalDate finReal;
    private double metaKilos;
    private double precioBaseKilo;
    private EstadoPlan estado;

    private Cuartel cuartel;
    private ArrayList<Cuadrilla> cuadrillas;

    public PlanCosecha(int id, String nom, LocalDate ini, LocalDate finEst, double meta, double precio, Cuartel cuartel) {
        this.id = id;
        this.nombre = nom;
        this.inicio = ini;
        this.finEstimado = finEst;
        this.finReal = null;
        this.metaKilos = meta;
        this.precioBaseKilo = precio;
        this.estado = EstadoPlan.PLANIFICADO;
        this.cuartel = cuartel;
        this.cuadrillas = new ArrayList<>();
        this.cuartel.addPlanCosecha(this);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public LocalDate getFinEstimado() {
        return finEstimado;
    }

    public LocalDate getFinReal() {
        return finReal;
    }

    public void setFinReal(LocalDate finReal) {
        this.finReal = finReal;
    }

    public double getMetaKilos() {
        return metaKilos;
    }

    public void setMetaKilos(double metaKilos) {
        this.metaKilos = metaKilos;
    }

    public double getPrecioBaseKilo() {
        return precioBaseKilo;
    }

    public void setPrecioBaseKilo(double precioBaseKilo) {
        this.precioBaseKilo = precioBaseKilo;
    }

    public EstadoPlan getEstado() {
        return estado;
    }

    public boolean setEstado(EstadoPlan estado) {
        this.estado = estado;
        return true;
    }

    public double getCumplimientoMeta() {
        double kilos = 0.0;

        for (Cuadrilla cuad : cuadrillas) {
            for (CosechadorAsignado asign : cuad.getAsignaciones()) {
                for (Pesaje p : asign.getPesajes()) {
                    kilos += p.getCantidadKg();
                }
            }
        }

        if (metaKilos <= 0) return 0;
        return (kilos / metaKilos) * 100.0;
    }

    public Cuartel getCuartel() {
        return cuartel;
    }

    public void addCuadrilla(int idCuad, String nomCuadrilla, Supervisor supervisor) throws GestionHuertosException {
        if (findCuadrillaById(idCuad) != null) {
            throw new GestionHuertosException("Ya existe en el plan una cuadrilla con id indicado");
        }
        Cuadrilla nueva = new Cuadrilla(idCuad, nomCuadrilla, supervisor, this);
        cuadrillas.add(nueva);
    }

    public void addCosechadorToCuadrilla(int idCuad, LocalDate fIni, LocalDate fFin, double meta, Cosechador cos) throws GestionHuertosException {
        Cuadrilla c = findCuadrillaById(idCuad);
        if (c == null) {
            throw new GestionHuertosException("No existe una cuadrilla en el plan con el id indicado");
        }//lanza excepción
        c.addCosechador(fIni, fFin, meta, cos);
    }

    public Cuadrilla[] getCuadrillas() {
        return cuadrillas.toArray(new Cuadrilla[0]);
    }

    private Cuadrilla findCuadrillaById(int idCuad) {
        for (Cuadrilla c : cuadrillas) {
            if (c.getId() == idCuad) {
                return c;
            }
        }
        return null;
    }
}
