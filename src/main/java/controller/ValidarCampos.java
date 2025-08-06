package controller;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;   

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Target;

// Importa la anotación clave de JAX-RS que permite conectar anotaciones con filtros
import javax.ws.rs.NameBinding;

/**
 * Esta es una ANOTACIÓN PERSONALIZADA que permite marcar métodos o clases que necesitan validación de datos ANTES de ejecutarse
 * 
 * @author YariangelAray
 */

// Le dice a JAX-RS que esta anotación puede "conectarse" con filtros. Sin esto, JAX-RS no sabría que debe buscar un filtro relacionado             
@NameBinding

// Define CUÁNDO está disponible la anotación
// RUNTIME = Estará disponible cuando el programa esté ejecutándose
// Esto es necesario para que JAX-RS pueda "leer" la anotación durante la ejecución
// Sin esto, la anotación desaparecería después de compilar el código
@Retention(RUNTIME)

// Define DÓNDE se puede usar la anotación
// TYPE = Se puede usar en clases completas
// METHOD = Se puede usar en métodos individuales
@Target({TYPE, METHOD})

public @interface ValidarCampos {
    
    /**          
     * Este parámetro permite especificar QUÉ TIPO DE DATOS se va a validar y que reglas de validación aplicar
     * Haciendo la anotación reutilizable para diferentes entidades          
     * 
     * @return String con el nombre de la entidad a validar (ej: "usuario", "producto")
     */
    String entidad();
}