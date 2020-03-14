package Model;

public class Consorcio {
    private int id;
    private String nombre;
    private String directorioFtp;
    private int administradores_id;
    private String directorioDescargadoExpensas;
    private String directorioDescargadoIndices;
    private String directorioDescargadoMora;
    private String directorioDescargadoLiquidacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAdministradores_id() {
        return administradores_id;
    }

    public void setAdministradores_id(int administradores_id) {
        this.administradores_id = administradores_id;
    }

    public String getDirectorioFtp() {
        return directorioFtp;
    }

    public void setDirectorioFtp(String directorioFtp) {
        this.directorioFtp = directorioFtp;
    }

    public String getDirectorioDescargadoExpensas() {
        return directorioDescargadoExpensas;
    }

    public void setDirectorioDescargadoExpensas(String directorioDescargadoExpensas) {
        this.directorioDescargadoExpensas = directorioDescargadoExpensas;
    }

    public String getDirectorioDescargadoIndices() {
        return directorioDescargadoIndices;
    }

    public void setDirectorioDescargadoIndices(String directorioDescargadoIndices) {
        this.directorioDescargadoIndices = directorioDescargadoIndices;
    }

    public String getDirectorioDescargadoMora() {
        return directorioDescargadoMora;
    }

    public void setDirectorioDescargadoMora(String directorioDescargadoMora) {
        this.directorioDescargadoMora = directorioDescargadoMora;
    }

    public String getDirectorioDescargadoLiquidacion() {
        return directorioDescargadoLiquidacion;
    }

    public void setDirectorioDescargadoLiquidacion(String directorioDescargadoLiquidacion) {
        this.directorioDescargadoLiquidacion = directorioDescargadoLiquidacion;
    }
}
