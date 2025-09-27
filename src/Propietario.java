import java.util.ArrayList;
import java.util.List;

public class Propietario extends Persona{
  private String direccionCom;
  private List<Huerto> huertos = new ArrayList<>();

  public Propietario(Rut rut, String nom, String email, String dir, String direccionCom) {
    super(rut, nom, email,  dir);
    this.direccionCom = direccionCom;
  }

  public String getDireccionComercial(){return direccionCom;}
  public void getdireccionComercia(String direccion){direccionCom = direccion;}
  public boolean addHuerto(Huerto huerto){return huertos.add(huerto);} //El ArrayList en Huerto se deberá llamar "huertos"
  public Huerto[] getHuertos(){
    Huerto[] huerto = new Huerto[huertos.size()];
    for(int i = 0; i < huertos.size(); i++){
      huerto[i] = huertos.get(i);
    }
    return huerto;
  }
}



