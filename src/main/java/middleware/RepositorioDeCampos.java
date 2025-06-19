package middleware;

import middleware.campos.CamposUsuario;
import java.util.List;
import middleware.campos.*;

/**
 * Devuelve la lista de campos que se deben validar para cada entidad (como "usuario").
 * 
 * Esta clase actúa como una especie de "puente" entre el nombre de la entidad
 * y su lista de campos definida. 
 * 
 * @author Yariangel Aray
 */
public class RepositorioDeCampos {
    
    /**
     * Devuelve la lista de campos según el nombre de la entidad.
     * 
     * @param entidad El nombre de la entidad (por ejemplo: "usuario").
     * @return Lista de campos definidos para esa entidad, o null si no se reconoce.
     */
    public static List<Campo> obtenerCampos(String entidad) {
        return switch (entidad.toLowerCase()) {
            case "usuario" -> CamposUsuario.obtener();
            case "rol" -> CamposRol.obtener();
            case "tipo_documento" -> CamposTipoDocumento.obtener();
            case "programa_formacion" -> CamposProgramaFormacion.obtener();
            case "ficha" -> CamposFicha.obtener();
            case "genero" -> CamposGenero.obtener();
            default -> null;
        };
    }
}
