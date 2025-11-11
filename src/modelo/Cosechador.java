package modelo;

import utilidades.Rut;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Optional;

public class Cosechador extends Persona{
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
        Cuadrilla[] cuadrillas = new Cuadrilla[cosAsignados.size()];
        for (int i = 0; i < cosAsignados.size(); i++){
            cuadrillas[i] = cosAsignados.get(i).getCuadrilla();
        }
        return cuadrillas;
    }

    public Optional<CosechadorAsignado> getAsignacion(int idCuad, int idPlan) {
        Optional<CosechadorAsignado>  cosechadorAsignado = Optional.empty();
        for (CosechadorAsignado ca : cosAsignados) {
            Cuadrilla cuad = ca.getCuadrilla();
            if (cuad.getId() == idCuad && cuad.getPlanCosecha().getId() == idPlan) {
                cosechadorAsignado = Optional.of(ca);
                break;
            }
        }
        return cosechadorAsignado;
    }
    public CosechadorAsignado[] getAsignaciones() {
        return cosAsignados.toArray(new CosechadorAsignado[0]);
    }
}