package middleware;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Anotación personalizada para indicar que un método o clase debe ser validado.
 * 
 * Se usa para decirle al filtro (ValidarCamposFilter) que valide los campos del
 * cuerpo de la solicitud antes de que llegue al método del recurso.
 * 
 * Ejemplo de uso:
 * 
 * @ValidarCampos(entidad = "usuario")
 * public Response crearUsuario(...) { ... }
 * 
 * La propiedad "entidad" sirve para saber qué objeto se esta manejando.
 * 
 * @author YariangelAray
 */
@NameBinding
@Retention(RUNTIME) // Disponible en tiempo de ejecución
@Target({TYPE, METHOD}) // Puede usarse sobre clases o métodos
public @interface ValidarCampos {
    String entidad(); // Nombre de la entidad a validar (ej. "usuario")
}
