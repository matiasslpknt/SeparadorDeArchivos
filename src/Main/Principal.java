package Main;

import Business.*;
import Model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.Splitter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.apache.commons.net.ftp.FTP;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

public class Principal extends javax.swing.JFrame {
    private JPanel panel1;
    private JLabel imagen;
    private JButton btnEjecutar;
    private JProgressBar progressBar;
    private JButton Carga;
    private JButton btnVer;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JComboBox cmbEstilo;
    private JCheckBox chkFinMes;
    private JLabel txtProgreso;

    private final String pathProyecto = System.getProperty("user.dir");
    private final String directorioFtp = pathProyecto + "\\archivos\\ftp\\";
    private final String directorioDescargas = pathProyecto + "\\archivos\\descargados\\";
    private final String directorioSalida = pathProyecto + "\\archivos\\salida\\";
    private AdministradorBO administradorBO = new AdministradorBO();
    private ConsorcioBO consorcioBO = new ConsorcioBO();
    private UsuarioFtpBO usuarioFtoBO = new UsuarioFtpBO();
    private AccessBO acc = new AccessBO();
    private PDFReader pdfReader = new PDFReader();
    //Traemnos todos los administradores que tienen una carpeta en la carpeta de archivos descargados
    private ArrayList<Administrador> administradores = new ArrayList<Administrador>();
    //Guarda los archivos no reconocidos (expensas, mora, liquidacion)
    private ArrayList<String> archivosDesconocidos = new ArrayList<String>();
    private int progreso = 0;

    private String urlApi = "https://api.androidhive.info/contacts/";
    private String objeto = "servicios";
    private String campo = "idservicio";
    private String username = "api";
    private String pass = "M@nz@nelli";
    private RestService restService = new RestService(new RestTemplateBuilder());

    public Principal() {
        super("COTERRANEA");
        setContentPane(panel1);
        progressBar.setStringPainted(true);
        progressBar.setValue(progreso);
        cargarComboBoxEstilo(cmbEstilo);
        chkFinMes.setSelected(false);
        eliminarCarpetasFTP(directorioFtp);
        eliminarCarpetasFTP(directorioSalida);
        //mati("");
        //login();
        //loginBeta();
        //loginTest();
        view("69985");

        btnEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                main();
            }
        });

        Carga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Carga carga = new Carga();
                carga.pack();
                carga.setVisible(true);
                carga.setLocationRelativeTo(null);
                carga.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Modificacion modificacion = new Modificacion();
                modificacion.pack();
                modificacion.setVisible(true);
                modificacion.setLocationRelativeTo(null);
                modificacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Eliminacion eliminacion = new Eliminacion();
                eliminacion.pack();
                eliminacion.setVisible(true);
                eliminacion.setSize(717, 421);
                eliminacion.setLocationRelativeTo(null);
                eliminacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
        btnVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Visualizacion visualizacion = new Visualizacion();
                visualizacion.pack();
                visualizacion.setVisible(true);
                visualizacion.setSize(1000, 600);
                visualizacion.setLocationRelativeTo(null);
                visualizacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });
    }

    public void main() {
        System.out.println();
        System.out.println("Moviendo archivos descargados: ");
        actualizarLabelProgreso("Moviendo archivos descargados: ");
        super.update(this.getGraphics());
        reacomodarArchivosDescargados();
        System.out.println();
        System.out.println("Obteniendo administradores");
        actualizarLabelProgreso("Obteniendo administradores...");
        administradores = getAdministradoresEnDirectorio(directorioDescargas);
        setearDirectoriosAdministradoresAVacios();
        if (administradores.size() > 0) {
            System.out.println();
            System.out.println("Eliminando carpetas vacias...");
            actualizarLabelProgreso("Obteniendo administradores...");
            eliminarCarpetasVacias(directorioDescargas);
            System.out.println();
            System.out.println("Reconociendo tipos de PDF descargados...");
            actualizarLabelProgreso("Reconociendo tipos de PDF descargados...");
            System.out.println();
            reconocerTiposPDF();
            progreso += 5;
            actualizarProgressBar();
            System.out.println("Limpiando memoria...");
            actualizarLabelProgreso("Limpiando memoria...");
            System.out.println();
            quitarAdministradoresSinarchivos();
            System.out.println("Generando directorios temporales de descarga...");
            actualizarLabelProgreso("Generando directorios temporales de descarga...");
            System.out.println();
            generarCarpetasParaPoderDescargarDelFtp();
            progreso += 5;
            actualizarProgressBar();
            System.out.println("Descargando archivos PDF:");
            actualizarLabelProgreso("Descargando archivos PDF:");
            descargaMasiva("pdf");
            System.out.println("Generando directorios de salida...");
            actualizarLabelProgreso("Generando directorios de salida...");
            System.out.println();
            generarCarpetaMesEnCurso(getFechaHoy());
            System.out.println("Obteniendo administradores FTP...");
            actualizarLabelProgreso("Obteniendo administradores FTP...");
            System.out.println();
            progreso += 5;
            actualizarProgressBar();
            ArrayList<Administrador> administradoresFtp = getAdministradoresFtp();
            System.out.println();

            System.out.println("*****************************************************");
            System.out.println("*****************************************************");
            System.out.println("FTP  ************************************************");
            System.out.println("*****************************************************");
            System.out.println("*****************************************************");
            System.out.println();
            for (Administrador a : administradoresFtp) {
                System.out.println("Administradoor: " + a.getNombre());
                System.out.println("========================================================================");
                for (Consorcio b : a.getConsorcios()) {
                    System.out.println("Consorcio: " + b.getNombre());
                    System.out.println("Directorio FTP: " + b.getDirectorioFtp());
                    System.out.println("Directorio Expensas: " + b.getDirectorioDescargadoExpensas());
                    System.out.println("Directorio Liquidacion: " + b.getDirectorioDescargadoLiquidacion());
                    System.out.println("Directorio Mora: " + b.getDirectorioDescargadoMora());
                    System.out.println("Usuario: " + b.getUsuarioFtp().getUsuario());
                    System.out.println("Contraseña: " + b.getUsuarioFtp().getPassword());
                    System.out.println("--------------------------------------------------");
                }
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
            }

            System.out.println("*****************************************************");
            System.out.println("*****************************************************");
            System.out.println("ADMINISTRADOES   ************************************");
            System.out.println("*****************************************************");
            System.out.println("*****************************************************");
            for (Administrador a : administradores) {
                System.out.println("Administradoor: " + a.getNombre());
                System.out.println("========================================================================");
                for (Consorcio b : a.getConsorcios()) {
                    System.out.println("Consorcio: " + b.getNombre());
                    System.out.println("Directorio FTP: " + b.getDirectorioFtp());
                    System.out.println("Directorio Expensas: " + b.getDirectorioDescargadoExpensas());
                    System.out.println("Directorio Liquidacion: " + b.getDirectorioDescargadoLiquidacion());
                    System.out.println("Directorio Mora: " + b.getDirectorioDescargadoMora());
                    System.out.println("Usuario: " + b.getUsuarioFtp().getUsuario());
                    System.out.println("Contraseña: " + b.getUsuarioFtp().getPassword());
                    System.out.println("--------------------------------------------------");
                }
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
            }

            System.out.println("Renombrando y moviendo archivos:");
            actualizarLabelProgreso("Renombrando y moviendo archivos...");
            cambiarNombresDescargasAlDeFtp(administradoresFtp);
            System.out.println();
            System.out.println("Descargando archivos ASP:");
            actualizarLabelProgreso("Descargando archivos ASP:");
            descargaMasiva("asp");
            System.out.println();
            System.out.println("Moviendo archivos ASP:");
            actualizarLabelProgreso("Moviendo archivos ASP...");
            moverArchivosASP();
            System.out.println("Descargando archivos MDB:");
            actualizarLabelProgreso("Descargando archivos MDB:");
            descargaMasiva("mdb");
            System.out.println();
            System.out.println("Renombrando y moviendo archivos MDB:");
            actualizarLabelProgreso("Renombrando y moviendo archivos MDB...");
            moverYRenombrarArchivosMDB();
            System.out.println();
            System.out.println("Eliminando carpetas vacias...");
            actualizarLabelProgreso("Eliminando carpetas vacias...");
            eliminarCarpetasVacias(directorioDescargas);
            System.out.println();
            progreso += 5;
            actualizarProgressBar();
            System.out.println("Spliteando pdfs...");
            actualizarLabelProgreso("Splitting PDF's...");
            splittearPDFS();
            System.out.println();
            System.out.println("Borrando archivos FTP...");
            actualizarLabelProgreso("Borrando archivos FTP...");
            borrarArchivosFTP();
            System.out.println();
            System.out.println("Subiendo archivos:");
            actualizarLabelProgreso("Subiendo archivos...");
            subirMasivamente();
            System.out.println();
            progreso = 100;
            actualizarProgressBar();
            System.out.println();
            actualizarLabelProgreso("FINALIZADO!!!");
            JOptionPane.showMessageDialog(null, "FINALIZADO!!!");
            dispose();
        } else {
            progreso = 100;
            actualizarProgressBar();
            System.out.println("No hay archivos en el directorio o los archivos no coinciden en el servidor");
            actualizarLabelProgreso("No hay archivos en el directorio o los archivos no coinciden en el servidor");
            JOptionPane.showMessageDialog(null, "No hay archivos en el directorio o los archivos no coinciden en el servidor");
            dispose();
        }
    }

    private JwtDTO loginBeta() {
        String url = "https://coterranea.net/api/api/";

        // Request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "api");
        map.add("password", "M@nz@nelli");
        map.add("action", "login");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<JwtDTO> response = null;
        try {
            response = this.restService.template().postForEntity(url, request, JwtDTO.class);
            System.out.println(response.getBody().getJWT());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }

    private JwtDTO login() {
        String url = "https://coterranea.net/api/api/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        LoginDTO login = new LoginDTO(username, pass);
        HttpEntity<LoginDTO> request = new HttpEntity<>(login, headers);
        ResponseEntity<JwtDTO> response = null;
        try {
            response = this.restService.template().postForEntity(url, request, JwtDTO.class);
            System.out.println(response.getBody().getJWT());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }

    private ServicioDTO view(String idServicio) {
        String url = "https://coterranea.net/api/api/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String jwt = login().getJWT();
        headers.add("X-Authorization","Bearer " + jwt);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("object", "servicios");
        map.add("action", "view");
        map.add("idservicio", idServicio);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<ServicioDTO> response = null;
        try {
            response = this.restService.template().postForEntity(url, request, ServicioDTO.class);
            System.out.println(response.getBody().getServicios().getObservacion());
            System.out.println(response.getBody().getServicios().getIdservicio());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }


    private JwtDTO loginTest() {
        String url = "https://coterranea.net/api/api/?action=login&password=M@nz@nelli&username=api";

        // Request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<JwtDTO> response = null;
        try {
            response = this.restService.template().getForEntity(url, JwtDTO.class);
            System.out.println(response.getBody().getJWT());
            /*String valor = response.getBody();
            ObjectMapper om = new ObjectMapper();
            try {
                JwtDTO a = om.readValue(valor, JwtDTO.class);
                System.out.println(a.getJWT());
            } catch (JsonProcessingException e1) {
                System.out.println(e1.getMessage());
                e1.printStackTrace();
                return null;
            }*/
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
        }
    }

    private ServicioDTO2 mati(ServicioDTO2 servicio, String jwt) {
        String url = "https://coterranea.net/api/api/?action=list&object=servicios";

        // Request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Authorization", "Bearer " + jwt);

        Map<String, String> headers1 = new HashMap<>();
		/*
		seteo de headers1

        for (Map.EntrySet<String, String> entry: headers.entrySet()) {
            headers.set(entry.key(), entry.value());
        }*/

        ServicioDTO2 servicioS = new ServicioDTO2();
        // build the request
        HttpEntity<ServicioDTO2> entity = new HttpEntity<>(servicioS, headers);

        // send POST request
        ResponseEntity<ServicioDTO2> response = null;
        try {
            response = this.restService.template().postForEntity(url, entity, ServicioDTO2.class);

            return response.getBody();

        } catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode() + "  " + e.getResponseBodyAsString());
            return null;
            // ObjectMapper om = new ObjectMapper();
            // try {
            // 	ClaseError om.readValue(e.getResponseBodyAsString(), ClaseError.class);
            // } catch (JsonProcessingException e1) {
            // 	return null;
            // }
        }
    }

    public void actualizarLabelProgreso(String texto){
        txtProgreso.setText(texto);
        super.update(this.getGraphics());
    }
    public void subirMasivamente(){
        int tamanio = administradores.size();
        double prog = 22 / tamanio;
        for(Administrador a : administradores){
            String dirFTP = a.getConsorcios().get(0).getDirectorioFtp();
            String usr = a.getConsorcios().get(0).getUsuarioFtp().getUsuario();
            String pass = a.getConsorcios().get(0).getUsuarioFtp().getPassword();
            uploadFileByFTP(dirFTP, usr, pass, a.getNombre(), prog);
        }
    }

    public void eliminarCarpetasFTP(String directorio) {
        ArrayList<String> directorios = getDirectorios(directorio);
        if(directorios.size() > 0){
            ArrayList<String> masters = new ArrayList<String>();
            ArrayList<String> slaves = new ArrayList<String>();
            for (int i = 0; i < directorios.size(); i++) {
                String dir = directorios.get(i) + "\\";
                if (dir.equals(directorio)) {
                    directorios.remove(i);
                    i--;
                } else {
                    dir = dir.replace(directorio, "").replace("\\", "/");
                    String[] dirs = dir.split("/");
                    if (dirs.length == 1) {
                        masters.add(directorios.get(i));
                    } else if (dirs.length == 2) {
                        slaves.add(directorios.get(i));
                    }
                }
            }
            for (String slave : slaves) {
                File archivo = new File(slave);
                if (archivo.delete()) {
                    System.out.println("Carpeta eliminada: " + slave);
                } else {
                    System.out.println("Problema eliminando carpeta: " + slave);
                }
            }
            for (String master : masters) {
                File archivo = new File(master);
                if (archivo.delete()) {
                    System.out.println("Carpeta eliminada: " + master);
                } else {
                    System.out.println("Problema eliminando carpeta: " + master);
                }
            }
        }
    }

    public void borrarArchivosFTP() {
        ArrayList<String> archivos = getArchivos(directorioFtp, "pdf");
        ArrayList<String> archivosMDB = getArchivos(directorioFtp, "mdb");
        ArrayList<String> archivosASP = getArchivos(directorioFtp, "asp");
        for (String a : archivosMDB) {
            archivos.add(a);
        }
        for (String a : archivosASP) {
            archivos.add(a);
        }
        if (archivos.size() > 0) {
            for (String a : archivos) {
                File archivo = new File(a);
                if (archivo.delete()) {
                    System.out.println("Archivo eliminado: " + a);
                } else {
                    System.out.println("Problema eliminando archivo: " + a);
                }
            }
        }
    }

    public void splittearPDFS() {
        for (Administrador admin : administradores) {
            ArrayList<Consorcio> cons = admin.getConsorcios();
            for (Consorcio c : cons) {
                String dir = c.getDirectorioDescargadoExpensas();
                if (!dir.equals("")) {
                    if (c.getDirectorioDescargadoExpensas().contains("\\salida\\")) {
                        splitPDF(dir);
                    }
                }
            }
        }
    }

    public void splitPDF(String directorio) {
        try {
            PDDocument document = PDDocument.load(new File(directorio));
            // Instantiating Splitter class
            Splitter splitter = new Splitter();

            // splitting the pages of a PDF document
            List<PDDocument> Pages = splitter.split(document);

            // Creating an iterator
            Iterator<PDDocument> iterator = Pages.listIterator();

            // Saving each page as an individual document
            int i = 1;

            while (iterator.hasNext()) {
                PDDocument pd = iterator.next();
                String numero = "" + i;
                if (numero.length() == 1) {
                    numero = "00" + i;
                } else if (numero.length() == 2) {
                    numero = "0" + i;
                } else {
                    numero = "" + i;
                }
                try {
                    pd.save(obtenerRuta(directorio) + parsearNombre(directorio) + "_" + numero + ".pdf");
                    System.out.println("Archivo cortado: " + obtenerRuta(directorio) + parsearNombre(directorio) + "_" + numero + ".pdf");
                    pd.close();
                } catch (COSVisitorException e) {
                    e.printStackTrace();
                }
                i++;
            }
            document.close();
        } catch (IOException e) {
            System.err.println("Exception while trying to read pdf document - " + e);
        }
    }

    public void cargarComboBoxEstilo(JComboBox cboo) {
        cboo.addItem("MANZUR");
        cboo.addItem("CASAS DIAZ");
        cboo.setSelectedItem("MANZUR");
        AutoCompleteDecorator.decorate(cboo);
    }

    public void setearDirectoriosAdministradoresAVacios() {
        for (Administrador admin : administradores) {
            ArrayList<Consorcio> cons = admin.getConsorcios();
            for (Consorcio c : cons) {
                c.setDirectorioDescargadoExpensas("");
                c.setDirectorioDescargadoLiquidacion("");
                c.setDirectorioDescargadoMora("");
            }
        }
    }

    public void moverYRenombrarArchivosMDB() {
        ArrayList<String> mdbsFTP = getArchivos(directorioFtp, "mdb");
        ArrayList<String> mdbsDescargados = getArchivos(directorioDescargas, "mdb");
        for (String descarga : mdbsDescargados) {
            String nombreAdmin = "";
            String nombreArchivo = "";
            for (String ftp : mdbsFTP) {
                String AdminDescarga = acc.getNombreAdministrador(descarga, "", "").trim();
                String AdminFtp = acc.getNombreAdministrador(ftp, "", "").trim();
                if (AdminDescarga.equals(AdminFtp)) {
                    String aux = ftp.split("ftp")[1];
                    aux = aux.substring(1);
                    aux = parsearBarraEscape(aux);
                    nombreAdmin = aux.split("/")[0];
                    String[] temp = aux.split("/");
                    nombreArchivo = temp[temp.length - 1];
                    break;
                }
            }
            if (!nombreAdmin.equals("") && !nombreArchivo.equals("")) {
                String directorioDescarga = descarga.split("\\.mdb")[0];
                String dirSalida = directorioSalida + nombreAdmin + "\\" + getNombreCarpetaMesEnCurso() + "\\" + nombreArchivo.split("\\.mdb")[0];
                try {
                    renombrarYMoverArchivo(directorioDescarga, dirSalida, "mdb");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void moverArchivosASP() {
        for (Administrador admin : administradores) {
            String dir = directorioFtp + admin.getNombre() + "\\" + getNombreCarpetaInternaMesAnterior(getFechaHoy()) + "\\";
            ArrayList<String> asps = getArchivos(dir, "asp");
            for (String s : asps) {
                try {
                    String nombreArchivoSinExtension = s.split("\\.asp")[0];
                    String[] arreglo = parsearBarraEscape(nombreArchivoSinExtension).split("/");
                    String nombreArchivo = arreglo[arreglo.length - 1];
                    renombrarYMoverArchivo(nombreArchivoSinExtension, directorioSalida + admin.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + nombreArchivo, "asp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void actualizarProgressBar() {
        progressBar.setValue(progreso);
        progressBar.update(progressBar.getGraphics());
    }

    public void reacomodarArchivosDescargados() {
        ArrayList<String> archivosftp = getArchivos(directorioDescargas, "pdf");
        ArrayList<Administrador> misAdmins = administradorBO.getAdministradores();
        for (String archivo : archivosftp) {
            for (Administrador adm : misAdmins) {
                if (pdfReader.leerDeUnPdf(archivo, adm.getNombre())) {
                    boolean b = checkearSiExisteDirectorioEnDescargas(adm.getNombre());
                    String dir = directorioDescargas + adm.getNombre();
                    if (b == false) {
                        File directorio = new File(dir);
                        directorio.mkdir();
                    }
                    Path temp = null;
                    try {
                        String[] subDirs = parsearBarraEscape(archivo).split("/");
                        temp = Files.move(Paths.get(archivo),
                                Paths.get(dir + "\\" + subDirs[subDirs.length - 1]));
                        if (temp != null) {
                            System.out.println("-----" + archivo + " -> " + dir + "\\" + subDirs[subDirs.length - 1]);
                        } else {
                            System.out.println("Falló en mover archivo: " + archivo);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean checkearSiExisteDirectorioEnDescargas(String nombre) {
        if (nombre.equals("")) {
            nombre = "-";
        }
        boolean bandera = false;
        File archivo = new File(directorioDescargas + nombre);
        if (archivo.exists()) {
            bandera = true;
        }
        return bandera;
    }

    public void descargaMasiva(String tipoArchivo) {
        int tamanio = administradores.size();
        double prog = 20 / tamanio;
        for (Administrador admin : administradores) {
            String url = admin.getConsorcios().get(0).getDirectorioFtp() + getNombreCarpetaInternaMesAnterior(getFechaHoy());
            String usuario = admin.getConsorcios().get(0).getUsuarioFtp().getUsuario();
            String password = admin.getConsorcios().get(0).getUsuarioFtp().getPassword();
            descargarArchivosDirectorioFTP(url, usuario, password, tipoArchivo, admin.getNombre(), prog);
        }
    }

    public void generarCarpetasParaPoderDescargarDelFtp() {
        for (Administrador admin : administradores) {
            crearCarpetaMesEnCursoEnDirectorio(directorioFtp + admin.getNombre());
            crearCarpetaMesEnCursoEnDirectorio(directorioFtp + admin.getNombre() + "\\" + getNombreCarpetaInternaMesAnterior(getFechaHoy()));
        }
    }

    public void descargarArchivosDirectorioFTP(String url, String usuario, String password, String tipoExtension, String nombreAdmin, double progress) {
        ArrayList<String> archivos = getNombresArchvivosPDFEnDirectorioFTP(url, usuario, password, tipoExtension);
        int tamanio = archivos.size();
        double val = progress / tamanio;
        String server = url.split("/")[0];
        int cont = 1;
        for (String a : archivos) {
            String[] b = a.split("/");
            downloadFileByFTP(usuario, password, directorioFtp + nombreAdmin + "/" + getNombreCarpetaInternaMesAnterior(getFechaHoy()) + "/" + b[b.length - 1], a, server);
            System.out.println("-----Archivo " + cont + " de " + archivos.size());
            actualizarLabelProgreso("Descargando archivo " + tipoExtension.toUpperCase() + " " + cont + " de " + archivos.size());
            cont++;
            progreso += val;
            actualizarProgressBar();
        }
        System.out.println();
    }

    //Descarga el archivo FTP a nuestra maquina
    public void downloadFileByFTP(String user, String pass, String localPath, String remotePath, String server) {
        FTPClient client = new FTPClient();//Iniciamos el cliente del FTP
        FileOutputStream stream = null;
        try {
            client.connect(server);
            client.login(user, pass);
            String archivo = remotePath;
            stream = new FileOutputStream(localPath);
            client.retrieveFile(archivo, stream);//pone el archivo en tu stream
            stream.close();
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Sube el archivo al FTP
    public void uploadFileByFTP(String url, String user, String pass, String nombreAdministrador, double progres) {
        ArrayList<String> archivos = getNombresCarpetasPDFEnDirectorioFTP(url, user, pass);
        boolean existeCarpeta = false;
        for (String a : archivos) {
            String[] partes = a.split("/");
            if (partes[partes.length - 1].trim().equals("testMatias")) { //------ACA CAMBIAMOS "testMatias" por getNombreCarpetaMesEnCurso() -------------------------------------------------
                existeCarpeta = true;
                break;
            }
        }

        String[] partes = url.split("/");
        String server = partes[0];
        String destino = "";
        for (int i = 1; i < partes.length; i++) {
            destino += partes[i] + "/";
        }
        ArrayList<String> archivosSubir = getArchivos(directorioSalida + nombreAdministrador + "\\" + getNombreCarpetaMesEnCurso(), "pdf");
        ArrayList<String> mdbSubir = getArchivos(directorioSalida + nombreAdministrador + "\\" + getNombreCarpetaMesEnCurso(), "mdb");
        ArrayList<String> aspSubir = getArchivos(directorioSalida + nombreAdministrador + "\\" + getNombreCarpetaMesEnCurso(), "asp");
        for (String mdb : mdbSubir) {
            archivosSubir.add(mdb);
        }
        for (String asp : aspSubir) {
            archivosSubir.add(asp);
        }
        int tamanio = archivosSubir.size();
        double miProgreso = progres / tamanio;
        int numero = 0;
        if (existeCarpeta == false) {
            FTPClient client = new FTPClient();//Iniciamos el cliente del FTP
            try {
                client.connect(server);
                client.login(user, pass);
                String nuevoDirectorio = destino + "testMatias"; //------ACA CAMBIAMOS "testMatias" por getNombreCarpetaMesEnCurso() -------------------------------------------------
                client.makeDirectory(nuevoDirectorio);

                for (String miArchivo : archivosSubir) {
                    String[] lasPartes = parsearBarraEscape(miArchivo).split("/");
                    String elNombre = lasPartes[lasPartes.length - 1];
                    //System.out.println(nuevoDirectorio + "/" + elNombre);
                    upload(server, nuevoDirectorio + "/" + elNombre, user, pass, miArchivo, miProgreso, tamanio, numero);
                    numero ++;
                }

                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Problemas de conexión subiendo archivos. Intente nuevamente más tarde.");
            }
        } else {
            ArrayList<String> archivosEnCarpetaFTP = getNombresArchvivosPDFEnDirectorioFTPConSplit(url + "testMatias", user, pass, "pdf");//------ACA CAMBIAMOS "testMatias" por getNombreCarpetaMesEnCurso() -------------------------------------------------
            ArrayList<String> archivosMDBEnCarpetaFTP = getNombresArchvivosPDFEnDirectorioFTP(url + "testMatias", user, pass, "mdb");//------ACA CAMBIAMOS "testMatias" por getNombreCarpetaMesEnCurso() -------------------------------------------------
            ArrayList<String> archivosASPEnCarpetaFTP = getNombresArchvivosPDFEnDirectorioFTP(url + "testMatias", user, pass, "asp");//------ACA CAMBIAMOS "testMatias" por getNombreCarpetaMesEnCurso() -------------------------------------------------
            for (String miArchivo : archivosMDBEnCarpetaFTP) {
                archivosEnCarpetaFTP.add(miArchivo);
            }
            for (String miArchivo : archivosASPEnCarpetaFTP) {
                archivosEnCarpetaFTP.add(miArchivo);
            }
            for (int p = 0; p < archivosSubir.size(); p++) {
                String[] nomAux1 = parsearBarraEscape(archivosSubir.get(p)).split("/");
                String nomAux = nomAux1[nomAux1.length - 1];
                //System.out.println(nomAux);
                for (String st : archivosEnCarpetaFTP) {
                    String[] nomAux2 = parsearBarraEscape(st).split("/");
                    String nomAuxx = nomAux2[nomAux2.length - 1];
                    if (nomAux.equals(nomAuxx)) {
                        archivosSubir.remove(p);
                        p--;
                        break;
                    }
                }
            }
            for(String g : archivosSubir){
                String[] lasPartes = parsearBarraEscape(g).split("/");
                String elNombre = lasPartes[lasPartes.length - 1];
                String nuevoDirectorio = destino + "testMatias"; //------ACA CAMBIAMOS "testMatias" por getNombreCarpetaMesEnCurso() -------------------------------------------------
                upload(server, nuevoDirectorio + "/" + elNombre, user, pass, g, miProgreso, tamanio, numero);
                numero ++;
            }
        }
    }

    public void upload(String server, String destino, String user, String pass, String localPath, double miProgreso, int tamanio, int numero) {
        int port = 21;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            File firstLocalFile = new File(localPath);
            String firstRemoteFile = destino;
            InputStream inputStream = new FileInputStream(firstLocalFile);
            System.out.println("--- Subiendo archivo: " + localPath);
            actualizarLabelProgreso("Subiendo archivo " + numero + " de " + tamanio);
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("Archivo subido correctamente.");
                if(firstLocalFile.delete()){
                    System.out.println("Archivo eliminado correctamente: " + localPath);
                }else{
                    System.out.println("Problema para eliminar el archivo: " + localPath);
                }
            }
            progreso += miProgreso;
            actualizarProgressBar();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //Se fija si tiene "_" y numeros para ver si es un archivo spliteado (_001). Si no tiene devuelve el nombre completo con extension.
    private String getNombreSiNoEsSplit(String nombreArchivo) {
        String nombre = nombreArchivo.replace(".pdf", "");
        String[] arreglo = nombre.split("_");
        nombre = arreglo[0];
        String numeros = arreglo[arreglo.length - 1];
        boolean bandera = true;
        boolean bandera2 = true;
        for (int i = 0; i < numeros.length(); i++) {
            char dig = numeros.charAt(i);
            if (!Character.isDigit(dig)) {
                bandera = false;
                break;
            }
        }
        if (bandera == true) {
            return "";
        } else {
            return nombreArchivo;
        }
    }

    //trae los nombres de los arhchivos en un directorio de servidor ftp
    private ArrayList<String> getNombresArchvivosPDFEnDirectorioFTP(String url, String usuario, String password, String extension) {
        ArrayList<String> listaDevolver = new ArrayList<String>();
        String[] arreglo = url.split("/");
        String dominio = arreglo[0];
        String directorio = "/";
        for (int i = 1; i < arreglo.length; i++) {
            directorio += arreglo[i] + "/";
        }
        FtpConnector connector = new FtpConnector();
        try {
            FTPClient client = connector.connect(dominio, usuario, password);
            String[] lista = client.listNames(directorio);
            for (String lis : lista) {
                if (lis.contains("." + extension) && !getNombreSiNoEsSplit(lis.replace(directorio, "")).equals("")) {
                    listaDevolver.add(lis);
                }
            }
            connector.disconnect(client);
        } catch (IOException e) {
            listaDevolver = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Problema de conexion con el servidor FTP.");
        }
        return listaDevolver;
    }

    private ArrayList<String> getNombresArchvivosPDFEnDirectorioFTPConSplit(String url, String usuario, String password, String extension) {
        ArrayList<String> listaDevolver = new ArrayList<String>();
        String[] arreglo = url.split("/");
        String dominio = arreglo[0];
        String directorio = "/";
        for (int i = 1; i < arreglo.length; i++) {
            directorio += arreglo[i] + "/";
        }
        FtpConnector connector = new FtpConnector();
        try {
            FTPClient client = connector.connect(dominio, usuario, password);
            String[] lista = client.listNames(directorio);
            for (String lis : lista) {
                if (lis.contains("." + extension)) {
                    listaDevolver.add(lis);
                }
            }
            connector.disconnect(client);
        } catch (IOException e) {
            listaDevolver = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Problema de conexion con el servidor FTP.");
        }
        return listaDevolver;
    }

    //trae los nombres de las carpetas en un directorio de servidor ftp
    private ArrayList<String> getNombresCarpetasPDFEnDirectorioFTP(String url, String usuario, String password) {
        ArrayList<String> listaDevolver = new ArrayList<String>();
        String[] arreglo = url.split("/");
        String dominio = arreglo[0];
        String directorio = "/";
        for (int i = 1; i < arreglo.length; i++) {
            directorio += arreglo[i] + "/";
        }
        FtpConnector connector = new FtpConnector();
        try {
            FTPClient client = connector.connect(dominio, usuario, password);
            String[] lista = client.listNames(directorio);
            for (String lis : lista) {
                String[] partes = lis.split("/");
                if (!partes[partes.length - 1].contains(".")) {
                    listaDevolver.add(lis);
                }
            }
            connector.disconnect(client);
        } catch (IOException e) {
            listaDevolver = null;
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Problema de conexion con el servidor FTP.");
        }
        return listaDevolver;
    }

    private void cambiarNombresDescargasAlDeFtp(ArrayList<Administrador> ftp) {
        for (Administrador descarga : administradores) {
            for (Administrador ftpAux : ftp) {
                if (descarga.getId() == ftpAux.getId()) {
                    ArrayList<Consorcio> consorciosDescarga = descarga.getConsorcios();
                    ArrayList<Consorcio> consorciosFtp = ftpAux.getConsorcios();
                    for (Consorcio consD : consorciosDescarga) {
                        for (Consorcio consF : consorciosFtp) {
                            if (consD.getId() == consF.getId()) {
                                try {
                                    if (!consD.getDirectorioDescargadoExpensas().equals("") && !consF.getDirectorioDescargadoExpensas().equals("")) {
                                        renombrarYMoverArchivo(extraerExtensionPDF(consD.getDirectorioDescargadoExpensas()), directorioSalida + descarga.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + parsearNombre(consF.getDirectorioDescargadoExpensas()), "pdf");
                                        consD.setDirectorioDescargadoExpensas(directorioSalida + descarga.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + parsearNombre(consF.getDirectorioDescargadoExpensas()) + ".pdf");
                                    }
                                    if (!consD.getDirectorioDescargadoMora().equals("") && !consF.getDirectorioDescargadoMora().equals("")) {
                                        renombrarYMoverArchivo(extraerExtensionPDF(consD.getDirectorioDescargadoMora()), directorioSalida + descarga.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + parsearNombre(consF.getDirectorioDescargadoMora()), "pdf");
                                        consD.setDirectorioDescargadoMora(directorioSalida + descarga.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + parsearNombre(consF.getDirectorioDescargadoMora()) + ".pdf");
                                    }
                                    if (!consD.getDirectorioDescargadoLiquidacion().equals("") && !consF.getDirectorioDescargadoLiquidacion().equals("")) {
                                        renombrarYMoverArchivo(extraerExtensionPDF(consD.getDirectorioDescargadoLiquidacion()), directorioSalida + descarga.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + parsearNombre(consF.getDirectorioDescargadoLiquidacion()), "pdf");
                                        consD.setDirectorioDescargadoLiquidacion(directorioSalida + descarga.getNombre() + "\\" + getNombreCarpetaMesEnCurso() + "\\" + parsearNombre(consF.getDirectorioDescargadoLiquidacion()) + ".pdf");
                                    } else {
                                        break;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String extraerExtensionPDF(String directorioArchivo) {
        return directorioArchivo.replace(".pdf", "");
    }

    private String parsearNombre(String directorioArchivo) {
        String[] directorio = directorioArchivo.replace("\\", "/").split("/");
        String nombre = directorio[directorio.length - 1].replace(".pdf", "");
        return nombre;
    }

    private String obtenerRuta(String directorioArchivo) {
        String[] directorio = directorioArchivo.replace("\\", "/").split("/");
        String ruta = "";
        for (int i = 0; i < directorio.length - 1; i++) {
            ruta += directorio[i] + "\\";
        }
        return ruta;
    }

    private String getNombreCarpetaMesEnCurso() {
        String[] miFecha = getFechaHoy().split("/");
        if (!chkFinMes.isSelected()) {
            return miFecha[1] + "-" + miFecha[2];
        } else {
            int mes = Integer.parseInt(miFecha[1]);
            int anio = Integer.parseInt(miFecha[2]);
            if (mes == 12) {
                mes = 1;
                anio++;
            } else {
                mes++;
            }
            String sMes = "" + mes;
            if (sMes.length() == 1) {
                sMes = "0" + sMes;
            }
            String sAnio = "" + anio;
            return (sMes + "-" + sAnio);
        }
    }

    private ArrayList<Administrador> getAdministradoresFtp() {
        String textoExpensa = "";
        String textoLiquidacion = "";
        String textoMora = "";
        if (cmbEstilo.getSelectedItem().toString().equals("MANZUR")) {
            textoExpensa = "TALON CONSORCITA";
            textoLiquidacion = "LIQUIDACION DE GASTOS Y ESTADO DE INGRESOS Y EGRESOS";
            textoMora = "LISTADO DE CONSORCISTAS EN MORA AL";
        } else if (cmbEstilo.getSelectedItem().toString().equals("CASAS DIAZ")) {
            textoExpensa = "Importes sobre los que se calcula la expensa";
            textoLiquidacion = "GASTOS PRESUPUESTADOS A RECAUDAR";
            textoMora = "DETALLE DE MOROSOS";
        }
        String nombreCarpetaMesAnterior = getNombreCarpetaInternaMesAnterior(getFechaHoy());
        ArrayList<Administrador> administradoresFtp = new ArrayList<Administrador>();
        for (Administrador adm : administradores) {
            Administrador admAux = new Administrador();
            admAux.setNombre(adm.getNombre());
            admAux.setId(adm.getId());
            ArrayList<Consorcio> cons = adm.getConsorcios();
            ArrayList<Consorcio> consAux = new ArrayList<Consorcio>();
            for (Consorcio c : cons) {
                String dir = directorioFtp + "\\" + adm.getNombre() + "\\" + nombreCarpetaMesAnterior + "\\";
                ArrayList<String> archivosftp = getArchivos(dir, "pdf");
                Consorcio CAux = new Consorcio();
                CAux.setNombre(c.getNombre());
                CAux.setId(c.getId());
                CAux.setDirectorioFtp(c.getDirectorioFtp());
                CAux.setAdministradores_id(c.getAdministradores_id());
                CAux.setUsuarioFtp(c.getUsuarioFtp());
                CAux.setDirectorioDescargadoIndices(c.getDirectorioDescargadoIndices());
                CAux.setDirectorioDescargadoExpensas("");
                CAux.setDirectorioDescargadoMora("");
                CAux.setDirectorioDescargadoLiquidacion("");
                for (String archivo : archivosftp) {
                    if (pdfReader.leerDeUnPdf(archivo, textoExpensa) && pdfReader.leerDeUnPdf(archivo, c.getNombre())) {
                        CAux.setDirectorioDescargadoExpensas(archivo);
                    }
                    //MORA
                    else if (pdfReader.leerDeUnPdf(archivo, textoMora) && pdfReader.leerDeUnPdf(archivo, c.getNombre())) {
                        CAux.setDirectorioDescargadoMora(archivo);
                    }
                    //LIQUIDACION
                    else if (pdfReader.leerDeUnPdf(archivo, textoLiquidacion) && pdfReader.leerDeUnPdf(archivo, c.getNombre())) {
                        CAux.setDirectorioDescargadoLiquidacion(archivo);
                    }
                }
                consAux.add(CAux);
            }
            admAux.setConsorcios(consAux);
            administradoresFtp.add(admAux);
        }
        return administradoresFtp;
    }

    private String getNombreCarpetaInternaMesAnterior(String fecha) {
        String[] miFecha = fecha.split("/");
        if (!chkFinMes.isSelected()) {
            int mes = Integer.parseInt(miFecha[1]);
            int anio = Integer.parseInt(miFecha[2]);
            if (mes == 1) {
                mes = 12;
                anio--;
            } else {
                mes--;
            }
            String sMes = "" + mes;
            if (sMes.length() == 1) {
                sMes = "0" + sMes;
            }
            String sAnio = "" + anio;
            return (sMes + "-" + sAnio);
        } else {
            return (miFecha[1] + "-" + miFecha[2]);
        }
    }

    private void generarCarpetaMesEnCurso(String fecha) {
        String[] miFecha = getFechaHoy().split("/");
        String nombreCarpetaInterna = "";
        if (!chkFinMes.isSelected()) {
            nombreCarpetaInterna = miFecha[1] + "-" + miFecha[2];
        } else {
            int mes = Integer.parseInt(miFecha[1]);
            int anio = Integer.parseInt(miFecha[2]);
            if (mes == 12) {
                mes = 1;
                anio++;
            } else {
                mes++;
            }
            String sMes = "" + mes;
            if (sMes.length() == 1) {
                sMes = "0" + sMes;
            }
            String sAnio = "" + anio;
            nombreCarpetaInterna = sMes + "-" + sAnio;
        }
        for (Administrador ad : administradores) {
            crearCarpetaMesEnCursoEnDirectorio(directorioSalida + ad.getNombre());
            crearCarpetaMesEnCursoEnDirectorio(directorioSalida + ad.getNombre() + "\\" + nombreCarpetaInterna);
        }
    }

    //busca carpetas vacias
    public List<File> buscarCarpetasVacias(File carpetaRaiz) {
        //siempre retornamos al menos una lista vacía
        List<File> resultado = new ArrayList<>();
        if (carpetaRaiz.isDirectory()) {
            File[] carpetas = carpetaRaiz.listFiles(File::isDirectory);
            for (File carpeta : carpetas) {
                if (carpeta.listFiles().length == 0) {
                    resultado.add(carpeta);
                } else {
                    resultado.addAll(buscarCarpetasVacias(carpeta));
                }
            }
        }
        return resultado;
    }

    //eilimina carpetas vacias
    public void eliminarCarpetasVacias(String directorio) {
        File file = new File(directorio);
        List<File> listaCarpetasVacias = buscarCarpetasVacias(file);
        for (File carpeta : listaCarpetasVacias) {
            carpeta.delete();
        }
    }

    private void quitarAdministradoresSinarchivos() {
        eliminarConsorciosSinArchivos();
        eliminarAdministradoresSinConsorcios();
    }

    //elimina los consorcios que no tienen archivos en la carpeta de descargas
    private void eliminarConsorciosSinArchivos() {
        for (int i = 0; i < administradores.size(); i++) {
            ArrayList<Consorcio> consorciosAdmin = administradores.get(i).getConsorcios();
            for (int j = 0; j < consorciosAdmin.size(); j++) {
                Consorcio con = consorciosAdmin.get(j);
                if (con.getDirectorioDescargadoExpensas() == "" && con.getDirectorioDescargadoMora() == "" && con.getDirectorioDescargadoLiquidacion() == "") {
                    administradores.get(i).getConsorcios().remove(j);
                    j--;
                }
            }
        }
    }

    //elimina de la lista de administradores (administradores) los administradores que no tienen consorcios cargados en la carpeta de descargas
    private void eliminarAdministradoresSinConsorcios() {
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getConsorcios().isEmpty()) {
                administradores.remove(i);
                i--;
            }
        }
    }

    private void reconocerTiposPDF() {
        String textoExpensa = "";
        String textoLiquidacion = "";
        String textoMora = "";
        if (cmbEstilo.getSelectedItem().toString().equals("MANZUR")) {
            textoExpensa = "TALON CONSORCITA";
            textoLiquidacion = "LIQUIDACION DE GASTOS Y ESTADO DE INGRESOS Y EGRESOS";
            textoMora = "LISTADO DE CONSORCISTAS EN MORA AL";
        } else if (cmbEstilo.getSelectedItem().toString().equals("CASAS DIAZ")) {
            textoExpensa = "Importes sobre los que se calcula la expensa";
            textoLiquidacion = "GASTOS PRESUPUESTADOS A RECAUDAR";
            textoMora = "DETALLE DE MOROSOS";
        }
        //para cada administrador entramos en la carpeta de descarga y obtenemos el nombre de cada archivo correspondiente
        // a expensas/liquidacion/mora dejando los indices para analizar mas adelante.
        for (int i = 0; i < administradores.size(); i++) {
            String directorio = directorioDescargas + administradores.get(i).getNombre() + "\\";
            ArrayList<String> archivos = getArchivos(directorio, "pdf");
            for (int j = 0; j < archivos.size(); j++) {
                boolean cons = false;
                ArrayList<Consorcio> consorciosAdmin = administradores.get(i).getConsorcios();
                //recorro todos los consorcios
                int indexConsorcioAEditar = -1;
                for (int k = 0; k < consorciosAdmin.size(); k++) {
                    cons = pdfReader.leerDeUnPdf(archivos.get(j), consorciosAdmin.get(k).getNombre());
                    if (cons == true) {
                        indexConsorcioAEditar = k;
                        break;
                    }
                }
                if (cons == true) {
                    //identificamos si es expensa, mora o liquidacion
                    //EXPENSAS
                    if (pdfReader.leerDeUnPdf(archivos.get(j), textoExpensa)) {
                        administradores.get(i).getConsorcios().get(indexConsorcioAEditar).setDirectorioDescargadoExpensas(archivos.get(j));
                    }
                    //MORA
                    else if (pdfReader.leerDeUnPdf(archivos.get(j), textoMora)) {
                        administradores.get(i).getConsorcios().get(indexConsorcioAEditar).setDirectorioDescargadoMora(archivos.get(j));
                    }
                    //LIQUIDACION
                    else if (pdfReader.leerDeUnPdf(archivos.get(j), textoLiquidacion)) {
                        administradores.get(i).getConsorcios().get(indexConsorcioAEditar).setDirectorioDescargadoLiquidacion(archivos.get(j));
                    } else {
                        archivosDesconocidos.add(archivos.get(j));
                    }
                    archivos.remove(j);
                    j--;
                } else {
                    archivosDesconocidos.add(archivos.get(j));
                    archivos.remove(j);
                    j--;
                }
            }
        }
    }

    private ArrayList<Administrador> getAdministradoresEnDirectorio(String directorio) {
        ArrayList<String> administradoresDescargados = getAdministradoresEnDirectorioPlano(directorio);
        ArrayList<Administrador> adminis = new ArrayList<Administrador>();

        for (int i = 0; i < administradoresDescargados.size(); i++) {
            Administrador adm = administradorBO.getAdministradorByNombre(administradoresDescargados.get(i));
            if (adm.getNombre() != null) {
                adminis.add(adm);
            }
        }

        for (int i = 0; i < adminis.size(); i++) {
            ArrayList<Consorcio> consorcios = consorcioBO.getConsorciosByIdAdministrador(adminis.get(i).getId());
            for (int k = 0; k < consorcios.size(); k++) {
                UsuarioFtp us = usuarioFtoBO.getUsuarioFtpByIdConsorcio(consorcios.get(k).getId());
                consorcios.get(k).setUsuarioFtp(us);
            }
            adminis.get(i).setConsorcios(consorcios);
        }
        return adminis;
    }

    private String parsearBarraEscape(String linea) {
        return linea.replace("\\", "/");
    }

    private String parsearBarraEscapeInverso(String linea) {
        return linea.replace("/", "\\");
    }

    //devuelve una lista de los nombres de administradores en la carpeta de descargas(devuelve los nombres de las carpetas)
    private ArrayList<String> getAdministradoresEnDirectorioPlano(String directorio) {
        ArrayList<String> archivos = getArchivos(directorio, "pdf");
        ArrayList<String> archivosReturn = new ArrayList<String>();
        for (int i = 0; i < archivos.size(); i++) {
            String linea = parsearBarraEscape(archivos.get(i));
            directorio = parsearBarraEscape(directorio);
            linea = linea.replace(directorio, "");
            linea = linea.split("/")[0];
            if (!archivosReturn.contains(linea)) {
                archivosReturn.add(linea);
            }
        }
        return archivosReturn;
    }

    //crea un directorio con el nombre especificado
    private void crearCarpetaMesEnCursoEnDirectorio(String directorioConNombreArchivo) {
        File directorio = new File(directorioConNombreArchivo);
        directorio.mkdir();
    }

    //renombra y mueve los archivos del directorio de descargas al directorio de salida
    private void renombrarYMoverArchivo(String directorioDescarga, String directorioSalida, String tipo) throws IOException {
        Path temp = Files.move(Paths.get(directorioDescarga + "." + tipo),
                Paths.get(directorioSalida + "." + tipo));
        if (temp != null) {
            System.out.println("-----" + directorioDescarga + "." + tipo + " -> " + directorioSalida + "." + tipo);
        } else {
            System.out.println("Falló en mover archivo: " + directorioDescarga);
        }
    }

    //Obtiene la fecha del dia.
    private String getFechaHoy() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    //trae el nombre de todos los archivos en un directorio
    private ArrayList<String> getArchivos(String directorio, String tipo) {
        ArrayList<String> archivos = new ArrayList<String>();
        try (Stream<Path> walk = Files.walk(Paths.get(directorio))) {
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("." + tipo)).collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                archivos.add(result.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archivos;
    }

    //trae de las carpetas que hay en un directorio
    private ArrayList<String> getArchivos2(String directorio) {
        ArrayList<String> archivos = new ArrayList<String>();
        try (Stream<Path> walk = Files.walk(Paths.get(directorio))) {
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("")).collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                String[] parts = parsearBarraEscape(result.get(i)).split("/");
                String theName = parts[parts.length - 1];
                String d = directorio.substring(0, directorio.length() - 1);
                if(!theName.contains(".pdf") && !theName.contains(".mdb") && !theName.contains(".asp") && !theName.contains(".txt") && !result.get(i).equals(d)){
                    archivos.add(result.get(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archivos;
    }

    private ArrayList<String> getDirectorios(String directorio) {
        ArrayList<String> archivos = new ArrayList<String>();
        try (Stream<Path> walk = Files.walk(Paths.get(directorio))) {
            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith("")).collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                archivos.add(result.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archivos;
    }

    private void createUIComponents() {
        imagen = new JLabel(new ImageIcon("images/coterranea.png"));
    }
}