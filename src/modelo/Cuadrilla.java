package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import utilidades.GestionHuertosException;

public class Cuadrilla {
    private int id;
    private String nombre;
    private static int maximoCosechadores;

    private Supervisor supervisor;
    private PlanCosecha planCosecha;
    private ArrayList<CosechadorAsignado> asignaciones;
    private ArrayList<Pesaje> pesajes;

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

        if (findCosechadorByRut(cos) != null) {
            throw new GestionHuertosException("Ya existe un cosechador con el mismo rut en esta cuadrilla.");
        }

        CosechadorAsignado nueva = new CosechadorAsignado(fIni, fFin, meta, this, cos);
        asignaciones.add(nueva);
    }
    public Cosechador[] getCosechadores() {
        Cosechador[] arr = new Cosechador[asignaciones.size()];
        for (int i = 0; i < asignaciones.size(); i++) {
            arr[i] = asignaciones.get(i).getCosechador();
        }
        return arr;
    }
    public double getKilosPesados() {
        double total = 0;
        for (Pesaje p : pesajes) {
            total +=p.getCantidadKg();

        }
        return total;
    }
    public CosechadorAsignado[] getAsignaciones() {
        return asignaciones.toArray(new CosechadorAsignado[0]);
    }

    public static int getMaximoCosechadores() { return maximoCosechadores; }
    public static void setMaximoCosechadores(int max) { maximoCosechadores = max; }

    private CosechadorAsignado findCosechadorByRut(Cosechador cos) {
        for (CosechadorAsignado ca : asignaciones) {
            if (ca.getCosechador().getRut().equals(cos.getRut())) {
                return ca;
            }
        }
        return null;
    }

}
