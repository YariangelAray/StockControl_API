package service;

import model.dao.RolDAO;
import model.entity.Rol;
import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con roles.
 * Actúa como intermediario entre el controlador (API) y la capa DAO (acceso a datos).
 * 
 * Métodos disponibles:
 * - obtenerTodos()
 * - obtenerRol(int id)
 * - crearRol(Rol rol)
 * - actualizarRol(int id, Rol rol)
 * - eliminarRol(int id)
 * 
 * @author Yariangel Aray
 */
public class RolService {
    private RolDAO dao;

    public RolService() {
        dao = new RolDAO(); // Se crea el DAO para delegar las operaciones de base de datos
    }

    /**
     * Retorna la lista completa de roles.
     *
     * @return Lista de objetos Rol.
     */
    public List<Rol> obtenerTodos() {
        return dao.getAll();
    }

    /**
     * Busca un rol por su ID.
     *
     * @param id ID del rol.
     * @return Rol encontrado o null.
     */
    public Rol obtenerRol(int id) {
        return dao.getById(id);
    }
    
    /**
     * Crea un nuevo rol.
     *
     * @param rol Objeto Rol a crear.
     * @return true si se creó exitosamente, false si falló.
     */
    public boolean crearRol(Rol rol) {
        return dao.create(rol);
    }

    /**
     * Actualiza un rol existente por ID.
     *
     * @param id ID del rol a actualizar.
     * @param rol Nuevos datos del rol.
     * @return true si se actualizó correctamente, false si falló.
     */
    public boolean actualizarRol(int id, Rol rol) {
        return dao.update(id, rol);
    }

    /**
     * Elimina un rol por su ID.
     *
     * @param id ID del rol a eliminar.
     * @return true si se eliminó, false si falló.
     */
    public boolean eliminarRol(int id) {
        return dao.delete(id);
    }
}
