package persistencia;

import modelo.Cultivo;
import modelo.Persona;
import modelo.PlanCosecha;
import utilidades.GestionHuertosException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionHuertosIO {
    //Nicolás Ignacio González Canales
    private static GestionHuertosIO instance;
    private GestionHuertosIO() {
    }
    public static GestionHuertosIO getInstance() {
        if (instance == null) {
            instance = new GestionHuertosIO();
        }
        return instance;
    }

    //************ métodos SAVE ***************
    public void savePersonas(Persona[] personas) throws GestionHuertosException {
        try (ObjectOutputStream archivo =
                     new ObjectOutputStream(new FileOutputStream("Personas.obj"))) {

            for (Persona p : personas)
                archivo.writeObject(p);

        } catch (NotSerializableException nSE) {
            throw new GestionHuertosException(
                    "Error: Alguna clase no implementa la interfaz SERIALIZABLE."
            );
        } catch (FileNotFoundException fNFE) {
            throw new GestionHuertosException(
                    "Error: No se pudo abrir/crear el archivo Personas.obj"
            );
        } catch (IOException iOE) {
            throw new GestionHuertosException(
                    "IOException: Hubo un error al intentar guardar los datos."
            );
        }
    }
    public void saveCultivos(Cultivo[] cultivos) throws GestionHuertosException {
        try (ObjectOutputStream archivo =
                     new ObjectOutputStream(new FileOutputStream("Cultivos.obj"))) {

            for (Cultivo c : cultivos)
                archivo.writeObject(c);

        } catch (NotSerializableException nSE) {
            throw new GestionHuertosException(
                    "Error: Alguna clase no implementa la interfaz SERIALIZABLE."
            );
        } catch (FileNotFoundException fNFE) {
            throw new GestionHuertosException(
                    "Error: No se pudo abrir/crear el archivo Cultivos.obj"
            );
        } catch (IOException iOE) {
            throw new GestionHuertosException(
                    "IOException: Hubo un error al intentar guardar los datos."
            );
        }
    }
    public void savePlanesCosecha(PlanCosecha[] planes) throws GestionHuertosException{
        try(ObjectOutputStream archivo =
                    new ObjectOutputStream(new FileOutputStream("PlanesCosecha.obj"))){

            for(PlanCosecha pC: planes)
                archivo.writeObject(pC);

        }catch(NotSerializableException nSE){
            throw new GestionHuertosException(
                    "Error: Alguna clase no implementa la interfaz SERIALIZABLE."
            );
        } catch (FileNotFoundException fNFE) {
            throw new GestionHuertosException(
                    "Error: No se pudo abrir/crear el archivo PlanesCosecha.obj"
            );
        } catch (IOException iOE) {
            throw new GestionHuertosException(
                    "IOException: Hubo un error al intentar guardar los datos."
            );
        }
    }

    //************ métodos READ **************
    public Persona[] readPersonas() throws GestionHuertosException {
        List<Persona> personas = new ArrayList<>();

        try (ObjectInputStream archivo =
                     new ObjectInputStream(new FileInputStream("Personas.obj"))) {

            while (true) {
                personas.add((Persona) archivo.readObject());
            }

        } catch (EOFException e) {
            // Fin del archivo, lectura completa.
        } catch (ClassNotFoundException e) {
            throw new GestionHuertosException(
                    "Error: No se pudieron recuperar las personas porque " +
                            "\nse han modificado las clases en el sistema."
            );
        } catch (ClassCastException e) {
            throw new GestionHuertosException(
                    "Error: El archivo contiene objetos que no corresponden a una PERSONA."
            );
        } catch (FileNotFoundException e) {
            throw new GestionHuertosException(
                    "Error: El archivo de Personas.obj no fue encontrado."
            );
        } catch (IOException e) {
            throw new GestionHuertosException(
                    "IOException: Hubo un error al intentar leer los datos de personas."
            );
        }
        return personas.toArray(Persona[]::new);
    }
    public Cultivo[] readCultivos() throws GestionHuertosException {
        List<Cultivo> cultivos = new ArrayList<>();

        try (ObjectInputStream archivo =
                     new ObjectInputStream(new FileInputStream("Cultivos.obj"))) {
            while (true) {
                cultivos.add((Cultivo) archivo.readObject());
            }

        } catch (EOFException e) {
            //Fin del archivo, Lectura completa.
        } catch (ClassNotFoundException e) {
            throw new GestionHuertosException(
                    "Error: No se pudieron recuperar los cultivos porque" +
                            " \nse han modificado las clases en el sistema."
            );
        } catch (ClassCastException e) {
            throw new GestionHuertosException(
                    "Error: El archivo contiene objetos que no corresponden a un CULTIVO."
            );
        } catch (FileNotFoundException e) {
            throw new GestionHuertosException(
                    "Error: El archivo de Cultivos.obj no fue encontrado."
            );
        } catch (IOException e) {
            throw new GestionHuertosException(
                    "IOException: Hubo un error al intentar leer los datos."
            );
        }
        return cultivos.toArray(Cultivo[]::new);
    }
    public PlanCosecha[] readPlanesCosecha() throws GestionHuertosException {
        List<PlanCosecha> planes = new ArrayList<>();

        try (ObjectInputStream archivo =
                     new ObjectInputStream(new FileInputStream("PlanesCosecha.obj"))) {

            while (true) {
                planes.add((PlanCosecha) archivo.readObject());
            }

        } catch (EOFException e) {
            //Fin del archivo, Lectura completada.
        } catch (ClassNotFoundException e) {
            throw new GestionHuertosException(
                    "Error: No se pudieron recuperar los planes porque" +
                            " \nse han modificado las clases en el sistema."
            );
        } catch (ClassCastException e) {
            throw new GestionHuertosException(
                    "Error: El archivo contiene objetos que no corresponden a un PLAN COSECHA."
            );
        } catch (FileNotFoundException e) {
            throw new GestionHuertosException(
                    "Error: El archivo de PlanesCosecha.obj no fue encontrado."
            );
        } catch (IOException e) {
            throw new GestionHuertosException(
                    "IOException: Hubo un error al intentar leer los datos."
            );
        }
        return planes.toArray(PlanCosecha[]::new);
    }
}
