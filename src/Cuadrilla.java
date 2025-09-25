import java.util.Date;
import java.util.ArrayList;

public class Cuadrilla {
    private int id;
    private String nombre;
    private int maximoCosechadores;

    private Supervisor supervisor;
    private PlanCosecha planCosecha;
    private ArrayList<CosechadorAsignado> asignaciones;

    public Cuadrilla(int id, String nom, Supervisor sup, PlanCosecha plan) {
        this.id = id;
        this.nombre = nom;
        this.supervisor = sup;
        this.planCosecha = plan;
        this.maximoCosechadores = 0;
        this.asignaciones = new ArrayList<>();

        if (sup != null) {
            sup.setCuadrilla(this);
        }
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Supervisor getSupervisor() { return supervisor; }
    public PlanCosecha getPlanCosecha() { return planCosecha; }

    public boolean addCosechador(Date fIni, Date fFin, double meta, Cosechador cos) {
        if (maximoCosechadores > 0 && asignaciones.size() >= maximoCosechadores) {
            return false;
        }
        for (CosechadorAsignado ca : asignaciones) {
            if (ca.getCosechador().getRut().equals(cos.getRut())) {
                return false;
            }
        }
        CosechadorAsignado nueva = new CosechadorAsignado(fIni, fFin, meta, this, cos);
        asignaciones.add(nueva);
        cos.addCuadrilla(nueva);
        return true;
    }

    public Cosechador[] getCosechadores() {
        Cosechador[] arr = new Cosechador[asignaciones.size()];
        for (int i = 0; i < asignaciones.size(); i++) {
            arr[i] = asignaciones.get(i).getCosechador();
        }
        return arr;
    }

    public int getMaximoCosechadores() { return maximoCosechadores; }
    public void setMaximoCosechadores(int max) { this.maximoCosechadores = max; }
}
