package controller;

import model.AccesoTemporal;
import model.CodigoAcceso;
import model.Elemento;
import model.Inventario;
import model.dao.AccesoTemporalDAO;
import model.dao.CodigoAccesoDAO;
import model.dao.ElementoDAO;
import model.dao.InventarioDAO;
import utils.ResponseProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import model.dao.UsuarioDAO;

/**
 * Controlador que gestiona el acceso temporal de usuarios a inventarios mediante códigos. 
 *
 * Proporciona funcionalidades como:
 * - Registrar accesos temporales con código.
 * - Consultar usuarios con acceso a un inventario.
 * - Consultar inventarios a los que un usuario tiene acceso.
 *
 * Los accesos se validan con códigos de acceso vigentes y se asocian a inventarios específicos.
 *
 * Endpoints disponibles:
 * - GET /accesos-temporales/inventario/{inventarioId}
 * - POST /accesos-temporales/acceder/{codigo}
 * - GET /accesos-temporales/{usuarioId}
 * 
 * @author Yariangel Aray
 */
@Path("/accesos-temporales") // Define la ruta base para todos los endpoints de este controlador
public class AccesoTemporalController {

    // DAO para operaciones de base de datos con accesos temporales
    private final AccesoTemporalDAO dao;
    // DAO para operaciones de base de datos con inventarios
    private final InventarioDAO daoInventario;
    // DAO para operaciones de base de datos con códigos de acceso
    private final CodigoAccesoDAO daoCodigo;
    // DAO para operaciones de base de datos con elementos
    private final ElementoDAO elementoDAO;
    // DAO para operaciones de base de datos con usuarios
    private final UsuarioDAO daoUsuario;

    /**
     * Constructor que inicializa los DAOs requeridos.
     */
    public AccesoTemporalController() {
        // Inicializa el DAO de accesos temporales
        dao = new AccesoTemporalDAO();
        // Inicializa el DAO de inventarios
        daoInventario = new InventarioDAO();
        // Inicializa el DAO de códigos de acceso
        daoCodigo = new CodigoAccesoDAO();
        // Inicializa el DAO de elementos
        elementoDAO = new ElementoDAO();
        // Inicializa el DAO de usuarios
        daoUsuario = new UsuarioDAO();
    }

    /**
     * Obtiene todos los usuarios con acceso temporal activo a un inventario.
     *
     * @param inventarioId ID del inventario.
     * @return Lista de accesos temporales si existen.
     */
    @GET // Método HTTP GET
    @Path("/inventario/{inventarioId}") // Ruta específica con parámetro inventarioId
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response obtenerUsuariosConAcceso(@PathParam("inventarioId") int inventarioId) {
        try {
            // Valida que el inventario exista
            Inventario inventario = daoInventario.getById(inventarioId);
            // Si el inventario no existe, retorna error 404
            if (inventario == null) {
                return ResponseProvider.error("El inventario especificado no existe", 404);
            }
            
            // Obtiene el código activo para este inventario
            CodigoAcceso codigo = daoCodigo.getCodigoActivo(inventarioId);
            // Si no hay código activo, retorna error 404
            if (codigo == null) {
                return ResponseProvider.error("No hay código activo para este inventario", 404);
            }

            // Obtiene todos los accesos temporales asociados al código
            List<AccesoTemporal> accesos = dao.getAccesosPorCodigo(codigo.getId());
            // Si no hay accesos temporales, retorna error 204 (no content)
            if (accesos.isEmpty()) {
                return ResponseProvider.error("No hay usuarios con acceso temporal actualmente", 204);
            }

            // Retorna la lista de accesos con status 200
            return ResponseProvider.success(accesos, "Usuarios con acceso obtenidos correctamente", 200);
        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno al obtener accesos", 500);
        }
    }

    /**
     * Registra un acceso temporal para un usuario utilizando un código válido.
     *
     * @param codigo  Código público generado previamente.
     * @param acceso  Objeto que contiene el ID del usuario.
     * @return Inventario accedido si el proceso es exitoso.
     */
    @POST // Método HTTP POST
    @Path("/acceder/{codigo}") // Ruta específica con parámetro codigo
    @Consumes(MediaType.APPLICATION_JSON) // Acepta datos en formato JSON
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response registrarAcceso(@PathParam("codigo") String codigo, AccesoTemporal acceso) {
        try {
            // Valida que el usuario exista
            Usuario usuario = daoUsuario.getById(acceso.getUsuario_id());
            // Si el usuario no existe, retorna error 404
            if (usuario == null) {
                return ResponseProvider.error("El usuario especificado no existe", 404);
            }
            // Verifica que el usuario tenga rol de usuario corriente (rol_id = 3)
            if (usuario.getRol_id() != 3) {
                return ResponseProvider.error("Solo los usuarios con rol de usuario corriente pueden usar códigos de acceso", 403);
            }
            
            // Paso 1: Validar que el código exista y no haya expirado
            CodigoAcceso encontrado = daoCodigo.searchValid(codigo);
            // Si el código no es válido o expiró, retorna error 404
            if (encontrado == null) {
                return ResponseProvider.error("Código inválido o expirado", 404);
            }
                        
            // Paso 2: Verificar que el usuario no tenga ya acceso a este código
            List<CodigoAcceso> accesos = dao.getCodigosAccesoActivosPorUsuario(acceso.getUsuario_id());
            // Si el usuario ya tiene acceso a este código, retorna error 409 (conflicto)
            if (accesos.contains(encontrado)) {
                return ResponseProvider.error("Este usuario ya cuenta con acceso al inventario", 409);
            }

            // Paso 3: Registrar el nuevo acceso temporal
            // Establece el ID del código de acceso en el objeto acceso
            acceso.setCodigo_acceso_id(encontrado.getId());
            // Intenta crear el nuevo acceso en la base de datos
            boolean creado = dao.createAcceso(acceso);

            // Si se creó exitosamente
            if (creado) {
                // Adjunta datos adicionales del inventario accedido
                Inventario inventario = daoInventario.getById(encontrado.getInventario_id());
                // Establece el nombre del inventario en el objeto código
                encontrado.setNombre_inventario(inventario.getNombre());
                // Retorna el código con información del inventario y status 201
                return ResponseProvider.success(encontrado, "Acceso exitoso", 201);
            }

            // Si falló la creación, retorna error 400
            return ResponseProvider.error("Error al registrar el acceso", 400);
        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno al registrar acceso", 500);
        }
    }

    /**
     * Obtiene todos los inventarios a los que un usuario tiene acceso temporal vigente.
     *
     * @param usuarioId ID del usuario.
     * @return Lista de inventarios con información adicional.
     */
    @GET // Método HTTP GET
    @Path("/usuario/{usuarioId}") // Ruta específica con parámetro usuarioId
    @Produces(MediaType.APPLICATION_JSON) // Produce respuesta en formato JSON
    public Response obtenerInventariosConAccesPorUsuario(@PathParam("usuarioId") int usuarioId) {
        try {
            // Valida que el usuario exista
            Usuario usuario = daoUsuario.getById(usuarioId);
            // Si el usuario no existe, retorna error 404
            if (usuario == null) {
                return ResponseProvider.error("El usuario especificado no existe", 404);
            }

            // Obtiene todos los códigos de acceso activos para este usuario
            List<CodigoAcceso> codigos = dao.getCodigosAccesoActivosPorUsuario(usuarioId);
            // Si no hay códigos activos, retorna mensaje informativo con status 204
            if (codigos.isEmpty()) {
                return ResponseProvider.success(null, "No hay inventarios activos para este usuario", 204);
            }

            // Inicializa lista para almacenar los inventarios con información adicional
            List<Inventario> inventarios = new ArrayList<>();
            // Itera sobre cada código de acceso activo
            for (CodigoAcceso codigo : codigos) {
                // Obtiene el inventario asociado al código
                Inventario inv = daoInventario.getById(codigo.getInventario_id());
                // Si el inventario existe
                if (inv != null) {
                    // Enriquecer el inventario con sus elementos
                    List<Elemento> elementos = elementoDAO.getAllByIdInventario(inv.getId());

                    // Inicializa el total del valor monetario
                    double totalValor = 0;
                    // Suma el valor de todos los elementos
                    for (Elemento el : elementos) {
                        totalValor += el.getValor_monetario();
                    }

                    // Establece el valor monetario total del inventario
                    inv.setValor_monetario(totalValor);
                    // Establece la cantidad de elementos en el inventario
                    inv.setCantidad_elementos(elementos.size());
                    // Establece la cantidad de ambientes cubiertos por el inventario
                    inv.setAmbientes_cubiertos(daoInventario.getAllAmbientesByInventario(inv.getId()).size());

                    // Agrega el inventario enriquecido a la lista
                    inventarios.add(inv);
                }
            }

            // Retorna la lista de inventarios con información adicional y status 200
            return ResponseProvider.success(inventarios, "Inventarios con acceso obtenidos correctamente", 200);
        } catch (Exception e) {            
            e.printStackTrace();            
            return ResponseProvider.error("Error interno al obtener inventarios", 500);
        }
    }
}