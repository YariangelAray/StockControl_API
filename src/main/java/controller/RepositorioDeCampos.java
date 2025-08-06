package controller;

import model.Campo;
import controller.campos.CamposReporte;
import controller.campos.CamposEstado;
import controller.campos.CamposProgramaFormacion;
import controller.campos.CamposTipoDocumento;
import controller.campos.CamposAmbiente;
import controller.campos.CamposTipoElemento;
import controller.campos.CamposRol;
import controller.campos.CamposGenero;
import controller.campos.CamposInventario;
import controller.campos.CamposElemento;
import controller.campos.CamposCentro;
import controller.campos.CamposFicha;
import controller.campos.CamposUsuario;
import java.util.List;

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
            case "inventario" -> CamposInventario.obtener();            
            case "centro" -> CamposCentro.obtener();
            case "ambiente" -> CamposAmbiente.obtener();
            case "tipo_elemento" -> CamposTipoElemento.obtener();
            case "estado" -> CamposEstado.obtener();
            case "elemento" -> CamposElemento.obtener();
            case "reporte" -> CamposReporte.obtener();
            default -> null;
        };
    }
}
