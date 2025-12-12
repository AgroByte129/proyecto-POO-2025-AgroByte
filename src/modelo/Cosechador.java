package modelo;

import utilidades.Rut;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Optional;

public class Cosechador extends Persona implements Serializable {
    private LocalDate fechaNacimiento;
    private List<CosechadorAsignado> cosAsignados = new ArrayList<>();

    public Cosechador(Rut rut, String nom, String email, String dir, LocalDate fNac){
        super(rut, nom, email, dir);
        fechaNacimiento = fNac;
    }

    public LocalDate getFechaNacimiento(){return fechaNacimiento;}
    public void setFechaNacimiento(LocalDate fNac){fechaNacimiento = fNac;}
    public void addCuadrilla(CosechadorAsignado cosAs){
        cosAsignados.add(cosAs);
    }
    public Cuadrilla[] getCuadrillas(){
        return cosAsignados.stream()
                .map(CosechadorAsignado::getCuadrilla)
                .toArray(Cuadrilla[]::new);
    }

    public Optional<CosechadorAsignado> getAsignacion(int idCuad, int idPlan) {
        return cosAsignados.stream()
                .filter(cosAs -> {
                    Cuadrilla c = cosAs.getCuadrilla();
                    return c.getId() == idCuad &&
                            c.getPlanCosecha().getId() == idPlan;
                })
                .findFirst();
    }

    public CosechadorAsignado[] getAsignaciones() {
        return cosAsignados.toArray(CosechadorAsignado[]::new);
    }
}