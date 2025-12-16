package vista;

import utilidades.Rut;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GUIHelper {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private GUIHelper() {
    }

    public static Rut obtenerRut(String texto) {
        return Rut.of(texto.trim());
    }

    public static LocalDate obtenerFecha(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha invalida (dd/MM/yyyy)");
        }
    }

    public static void validarNoVacio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El campo '" + campo + "' no puede estar vacio"
            );
        }
    }
}
