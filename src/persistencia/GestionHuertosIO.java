package persistencia;

import modelo.Cultivo;
import modelo.Persona;
import modelo.PlanCosecha;
import utilidades.GestionHuertosException;

import java.io.*;
import java.util.Arrays;

public class GestionHuertosIO {
    private static GestionHuertosIO instance;

    private GestionHuertosIO() {
    }

    public static GestionHuertosIO getInstance() {
        if (instance == null) {
            instance = new GestionHuertosIO();
        }
        return instance;
    }

    public void savePersonas(Persona[] personas) throws GestionHuertosException {
        try (ObjectOutputStream archivo =
                     new ObjectOutputStream(new FileOutputStream("Personas.obj"))) {
            for (Persona p : personas)
                archivo.writeObject(p);
        } catch (NotSerializableException nSE) {
            throw new GestionHuertosException("Alguna clase relacionada no implementa la interfaz serializable.");
        } catch (IOException iOE) {
            throw new GestionHuertosException("IOException: Hubo un error al intentar guardar los datos.");
        }
    }

    public void saveCultivos(Cultivo[] cultivos) throws GestionHuertosException {
        try (ObjectOutputStream archivo =
                     new ObjectOutputStream(new FileOutputStream("Cultivos.obj"))) {
            for (Cultivo c : cultivos)
                archivo.writeObject(c);
        } catch (NotSerializableException nSE) {
            throw new GestionHuertosException("Alguna clase relacionada no implementa la interfaz serializable.");
        } catch (IOException iOE) {
            throw new GestionHuertosException("IOException: Hubo un error al intentar guardar los datos.");
        }
    }

    public void savePlanesCosecha(PlanCosecha[] planes) throws GestionHuertosException{
        try(ObjectOutputStream archivo =
                    new ObjectOutputStream(new FileOutputStream("PlanesCosecha.obj"))){
            for(PlanCosecha pC: planes)
                archivo.writeObject(pC);
        }catch(NotSerializableException nSE){
            throw new GestionHuertosException("Alguna clase relacionada no implementa la interfaz serializable.");
        } catch (IOException iOE) {
            throw new GestionHuertosException("IOException: Hubo un error al intentar guardar los datos.");
        }
    }
}
