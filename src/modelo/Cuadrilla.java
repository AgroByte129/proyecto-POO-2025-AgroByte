package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import utilidades.GestionHuertosException;

public class Cuadrilla implements Serializable {
    private int id;
    private String nombre;
    private static int maximoCosechadores = 5;

    private Supervisor supervisor;
    private PlanCosecha planCosecha;
    private ArrayList<CosechadorAsignado> asignaciones;

    public Cuadrilla(int id, String nom, Supervisor sup, PlanCosecha plan) {
        this.id = id;
        this.nombre = nom;
        this.supervisor = sup;
        this.planCosecha = plan;
        this.asignaciones = new ArrayList<>();
        sup.setCuadrilla(this); //relación bicondicional
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Supervisor getSupervisor() { return supervisor; }
    public PlanCosecha getPlanCosecha() { return planCosecha; }

    public void addCosechador(LocalDate fIni, LocalDate fFin, double meta, Cosechador cos)
            throws GestionHuertosException {

        if (asignaciones.size() >= maximoCosechadores) {
            throw new GestionHuertosException("No es posible agregar el nuevo cosechador porque se alcanzó el máximo permitido.");
        }

        if (findCosechadorByRut(cos).isPresent()) {
            throw new GestionHuertosException("Ya existe un cosechador con el mismo rut en esta cuadrilla.");
        }

        CosechadorAsignado nueva = new CosechadorAsignado(fIni, fFin, meta, this, cos);
        asignaciones.add(nueva);
    }

    public Cosechador[] getCosechadores() {
        return asignaciones.stream()
                .map(CosechadorAsignado::getCosechador)
                .toArray(Cosechador[]::new);
    }

    public double getKilosPesados() {
        return asignaciones.stream()
                .flatMap(a -> Arrays.stream(a.getPesajes()))
                .mapToDouble(Pesaje::getCantidadKg)
                .sum();
    }
    public CosechadorAsignado[] getAsignaciones() {
        return asignaciones.toArray(CosechadorAsignado[]::new);
    }

    public static int getMaximoCosechadores() { return maximoCosechadores; }
    public static void setMaximoCosechadores(int max) { maximoCosechadores = max; }

    private Optional<CosechadorAsignado> findCosechadorByRut(Cosechador cos) {
        return asignaciones.stream()
                .filter(ca -> ca.getCosechador()
                        .getRut()
                        .equals(cos.getRut()))
                .findFirst();
    }

}
