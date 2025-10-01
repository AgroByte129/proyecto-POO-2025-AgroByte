import java.util.ArrayList;
import java.util.List;

public class Propietario extends Persona{ //extends persona es la herencia
  private String direccionConmercial;
  private List<Huerto> huertos = new ArrayList<>();

  public Propietario(String nom,String direccionConmercial) {}

  public String getDireccionComercial(){return direccionComercial;}
  public void getdireccionComercia(String direccion){direccionComercial = direccion;}
  public boolean addHuerto(Huerto huerto){return huertos.add(huerto);}
  public Huerto[] getHuertos(){
    Huerto[] huerto = new Huerto[huertos.size()];
    for(int i = 0; i < huertos.size(); i++){
      huerto[i] = huertos.get(i);
    }
    return huerto;
  }
}



