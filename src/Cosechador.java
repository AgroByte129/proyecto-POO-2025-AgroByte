import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class Cosechador extends Persona{
  private Date fechaNacimiento
  private List<CosechadorAsignado> cosAsignados = new ArrayList<>();

  public Cosechador(Rut rut, String nom, String email, String dir, Date fNac){
    super(rut, nom, email, dir);
    fechaNacimiento = fNac;
  }
  
  public Date getFechaNacimiento(){return fechaNacimiento;}
  public void setFechaNacimiento(Date fNac){fechaNacimiento = fNac;}
  public void addCuadrilla(CosehadorAsignado cosAs){
    if(cosAs != null && !cosAsignados.contains(cosAS)){
      cosAsignados.add(cosAS);
    }
  }
  public Cuadrilla[] getCuadrillas(){
    Cuadrilla[] cuadrillas = new Cuadrilla[cosAsignados.size()];
    for (int i = 0; i < cosAsignados.size(); i++){
      cuadrillas[i] = cosAsignados.get(i).getCuadrilla();
    }
    return cuadrillas;
  }
}






