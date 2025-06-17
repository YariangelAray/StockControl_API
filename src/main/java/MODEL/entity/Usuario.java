package model.entity;

/**
 * Clase que representa la entidad Usuario del sistema.
 * 
 * Este modelo se usa para mapear los datos provenientes de la tabla 'usuarios'
 * en la base de datos. Se utiliza en operaciones de inserción, consulta,
 * actualización y eliminación a través del UsuarioDAO.
 * 
 * También se considera un POJO (Plain Old Java Object) porque solo contiene
 * atributos, constructores y métodos getters y setters.
 * 
 * @author Yariangel Aray
 */
public class Usuario {
    
    private int id;
    private String nombres;
    private String apellidos;
    private int tipo_documento_id;
    private String documento;
    private int genero_id;
    private String telefono;
    private String correo;
    private Integer ficha_id;
    private String contrasena;
    private int rol_id;

    public Usuario(){}
    
    public Usuario(int id, String nombres, String apellidos, int tipo_documento_id, String documento, int genero_id, String telefono, String correo, Integer ficha_id, String contrasena, int rol_id) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.tipo_documento_id = tipo_documento_id;
        this.documento = documento;
        this.genero_id = genero_id;
        this.telefono = telefono;
        this.correo = correo;
        this.ficha_id = ficha_id;
        this.contrasena = contrasena;
        this.rol_id = rol_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getTipo_documento_id() {
        return tipo_documento_id;
    }

    public void setTipo_documento_id(int tipo_documento_id) {
        this.tipo_documento_id = tipo_documento_id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public int getGenero_id() {
        return genero_id;
    }

    public void setGenero_id(int genero_id) {
        this.genero_id = genero_id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getFicha_id() {
        return ficha_id;
    }

    public void setFicha_id(Integer ficha_id) {
        this.ficha_id = ficha_id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getRol_id() {
        return rol_id;
    }

    public void setRol_id(int rol_id) {
        this.rol_id = rol_id;
    }
}
