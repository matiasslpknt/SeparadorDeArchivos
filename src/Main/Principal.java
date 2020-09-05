package Main;

import Business.*;
import Model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    private JCheckBox chkSubidaDirecta;

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
    private ApiServices apiServices = new ApiServices();
    private ArrayList<String> consorciosFallidosWeb = new ArrayList<String>();
    private String nombreCarpetaMesEnCurso = "";

    public Principal() {
        super("COTERRANEA");
        setContentPane(panel1);
        progressBar.setStringPainted(true);
        progressBar.setValue(progreso);
        cargarComboBoxEstilo(cmbEstilo);
        chkFinMes.setSelected(false);
        eliminarCarpetasFTP(directorioFtp);
        eliminarCarpetasFTP(directorioSalida);
        cmbEstilo.setSelectedItem("--Seleccione--");
        chkSubidaDirecta.setSelected(false);
        chkSubidaDirecta.setVisible(false);

        btnEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (cmbEstilo.getSelectedItem().toString().equals("--Seleccione--")) {
                    JOptionPane.showMessageDialog(null, "Seleccione un estilo.");
                } else {
                    nombreCarpetaMesEnCurso = getNombreCarpetaMesEnCurso(); // "testMatias" para test y getNombreCarpetaMesEnCurso() para produccion
                    if (chkSubidaDirecta.isSelected()) {
                        if (cmbEstilo.getSelectedItem().toString().equals("CASAS DIAZ")) {
                            mainCasasDiaz();
                        } else if (cmbEstilo.getSelectedItem().toString().equals("ZIDARICH") || cmbEstilo.getSelectedItem().toString().equals("CUADRADO")) {
                            mainZidarichCuadrado();
                        }
                    } else {
                        main();
                    }

                }
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
        cmbEstilo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (cmbEstilo.getSelectedItem().toString().equals("CASAS DIAZ")) {
                    chkSubidaDirecta.setVisible(true);
                    chkSubidaDirecta.setSelected(false);
                    chkSubidaDirecta.setEnabled(true);
                } else if (cmbEstilo.getSelectedItem().toString().equals("ZIDARICH") || cmbEstilo.getSelectedItem().toString().equals("CUADRADO")) {
                    chkSubidaDirecta.setVisible(true);
                    chkSubidaDirecta.setSelected(true);
                    chkSubidaDirecta.setEnabled(false);
                } else {
                    chkSubidaDirecta.setVisible(false);
                    chkSubidaDirecta.setSelected(false);
                    chkSubidaDirecta.setEnabled(false);
                }
            }
        });
    }

    public void main() {
        System.out.println();
        System.out.println("Moviendo archivos descargados: ");
        actualizarLabelProgreso("Moviendo archivos descargados: ");
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
//            if (!cmbEstilo.getSelectedItem().toString().equals("CASAS DIAZ")) {
//                System.out.println("Descargando archivos PDF:");
//                actualizarLabelProgreso("Descargando archivos PDF:");
//                descargaMasiva("pdf");
//            }
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
//            System.out.println("Descargando archivos MDB:");
//            actualizarLabelProgreso("Descargando archivos MDB:");
//            descargaMasiva("mdb");
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
            System.out.println("Actualizando Web");
            actualizarLabelProgreso("Actualizando web...");
            actualizarWeb();
            System.out.println();
            progreso = 100;
            actualizarProgressBar();
            System.out.println();
            if (consorciosFallidosWeb.size() > 0) {
                String consorciosFallados = "";
                for (String idCons : consorciosFallidosWeb) {
                    consorciosFallados += idCons + ", ";
                }
                consorciosFallados = consorciosFallados.trim();
                consorciosFallados = consorciosFallados.substring(0, consorciosFallados.length() - 2);
                actualizarLabelProgreso("FINALIZADO!!! Algunos consorcios fallaron en la actualizacion web: " + consorciosFallados);
                JOptionPane.showMessageDialog(null, "FINALIZADO!!! Algunos consorcios fallaron en la actualizacion web: " + consorciosFallados);
            } else {
                actualizarLabelProgreso("FINALIZADO!!!");
                JOptionPane.showMessageDialog(null, "FINALIZADO!!!");
            }
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

    public void mainCasasDiaz() {
        String urlAlt = "ftp.casasdiaz.com.ar/public_html/Expensas/";
        String usrAlt = "casasdiaz";
        String passAlt = "EzZg2NdAgWv8";
        crearCarpetaEnServidorFTP(urlAlt, usrAlt, passAlt);
        subidaDirectaCasasDiaz();
        actualizarLabelProgreso("FINALIZADO!!!");
        JOptionPane.showMessageDialog(null, "FINALIZADO!!!");
    }

    public void mainZidarichCuadrado() {
        String urlAlt = "";
        String usrAlt = "";
        String passAlt = "";
        Administrador adminCargado = new Administrador();
        ArrayList<Consorcio> consCargados = new ArrayList<Consorcio>();
        ArrayList<Consorcio> listaActualizarWeb = new ArrayList<Consorcio>();
        String nombreAdmin = "";
        if (cmbEstilo.getSelectedItem().toString().equals("ZIDARICH")) {
            urlAlt = "ftp.coterranea.net/httpdocs/imagendigital/Zidarich/Expensas/";
            usrAlt = "coterranea.net";
            passAlt = "CO1181@nea.ar";
            nombreAdmin = "ZIDARICH";
        }
        if (cmbEstilo.getSelectedItem().toString().equals("CUADRADO")) {
            urlAlt = "ftp.admcuadrado.com.ar/httpdocs/Expensas/";
            usrAlt = "admcuadrado.com";
            passAlt = "AD1056@.com.ar";
            nombreAdmin = "CUADRADO";
        }

        System.out.println();
        ArrayList<String> archivos = getArchivos(directorioDescargas, "pdf");
        ArrayList<String> archivos2 = getArchivos(directorioDescargas, "PDF");
        for (String aaa : archivos2) {
            archivos.add(aaa);
        }
        adminCargado = administradorBO.getAdministradorByNombre(nombreAdmin);
        consCargados = consorcioBO.getConsorciosByIdAdministrador(adminCargado.getId());
        for (Consorcio cons : consCargados) {
            for (String arch : archivos) {
                if (pdfReader.leerDeUnPdf(arch, cons.getNombre())) {
                    boolean bande = false;
                    for (Consorcio c : listaActualizarWeb) {
                        if (c.getNombre().equals(cons.getNombre())) {
                            bande = true;
                            break;
                        }
                    }
                    if (bande == false) {
                        listaActualizarWeb.add(cons);
                    }
                }
            }
        }

        for (Consorcio cons : listaActualizarWeb) {
            String idConsorcioWeb = cons.getIdConsorcioWeb();
            ServicioDTO servicioDTO = apiServices.listService(idConsorcioWeb);
            String miFecha = parsearFechaHoyParaWeb();
            String observacion = servicioDTO.getServicios()[0].getObservacion();
            System.out.println(observacion);
            String mesCurso = getNombreCarpetaMesEnCurso();
            String mesAnterior = getNombreCarpetaInternaMesAnterior(getFechaHoy());
            String observacionNueva = "";
            String idAdministrador = servicioDTO.getServicios()[0].getIdAdministrador();
            if (observacion.contains(mesAnterior) && observacion.isEmpty() == false) {
                observacionNueva = observacion.replace(mesAnterior, mesCurso);
            } else {
                observacionNueva = "";
            }
            System.out.println(observacionNueva);
            ServicioDTO2 serv = new ServicioDTO2();
            serv.setIdTipoServicio("5");
            serv.setResumen("Gastos " + getNombreMes() + " " + getAnioASubir());
            serv.setIdCountry(idConsorcioWeb);
            serv.setIdAdministrador(idAdministrador);
            serv.setObservacion(observacionNueva);
            serv.setFechaDesde(miFecha);
            serv.setFechaHasta(miFecha);
            serv.setFechaAlta(miFecha);
            serv.setRespondido("SI");
            serv.setFechaRespondido(miFecha);
            serv.setOrigen("ADMINISTRADOR");
            serv.setIdServicioPadre("0");
            serv.setIdestado("0");
            serv.setServicios_tipo_ticket("1");
            serv.setPrioridad("4-BAJA");
            serv.setAction("add");
            serv.setObject("servicios");
            ServicioDTOAdd response = apiServices.addService(serv);
            if (response.getSucces().equals(false)) {
                consorciosFallidosWeb.add(idConsorcioWeb);
            }
        }
        crearCarpetaEnServidorFTP(urlAlt, usrAlt, passAlt);
        if (cmbEstilo.getSelectedItem().toString().equals("CUADRADO")) {
            subidaDirectaCuadrado();
        } else if (cmbEstilo.getSelectedItem().toString().equals("ZIDARICH")) {
            subidaDirectaZidarich(listaActualizarWeb);
        }

        if (consorciosFallidosWeb.size() > 0) {
            String consorciosFallados = "";
            for (String idCons : consorciosFallidosWeb) {
                consorciosFallados += idCons + ", ";
            }
            consorciosFallados = consorciosFallados.trim();
            consorciosFallados = consorciosFallados.substring(0, consorciosFallados.length() - 2);
            actualizarLabelProgreso("FINALIZADO!!! Algunos consorcios fallaron en la actualizacion web: " + consorciosFallados);
            JOptionPane.showMessageDialog(null, "FINALIZADO!!! Algunos consorcios fallaron en la actualizacion web: " + consorciosFallados);
        } else {
            actualizarLabelProgreso("FINALIZADO!!!");
            JOptionPane.showMessageDialog(null, "FINALIZADO!!!");
        }
    }

    /**
     * Actualiza las url de descarga de archivos del panel web mediante API REST
     **/
    public void actualizarWeb() {
        for (Administrador admin : administradores) {
            for (Consorcio cons : admin.getConsorcios()) {
                System.out.println("ADMINISTRADOR: " + admin.getNombre());
                System.out.println("CONSORCIO: " + cons.getNombre());
                String idConsorcioWeb = cons.getIdConsorcioWeb();
                ServicioDTO servicioDTO = apiServices.listService(idConsorcioWeb);
                String miFecha = parsearFechaHoyParaWeb();
                String observacion = servicioDTO.getServicios()[0].getObservacion();
                String mesCurso = getNombreCarpetaMesEnCurso();
                String mesAnterior = getNombreCarpetaInternaMesAnterior(getFechaHoy());
                String observacionNueva = "";
                String idAdministrador = servicioDTO.getServicios()[0].getIdAdministrador();
                try {
                    if (observacion.contains(mesAnterior) && observacion.isEmpty() == false) {
                        observacionNueva = observacion.replace(mesAnterior, mesCurso);
                    } else {
                        observacionNueva = observacion;
                    }
                } catch (Exception e) {
                    observacionNueva = observacion;
                }
                System.out.println(observacionNueva);
                ServicioDTO2 serv = new ServicioDTO2();
                serv.setIdTipoServicio("5");
                serv.setResumen("Gastos " + getNombreMes() + " " + getAnioASubir());
                serv.setIdCountry(idConsorcioWeb);
                serv.setIdAdministrador(idAdministrador);
                serv.setObservacion(observacionNueva);
                serv.setFechaDesde(miFecha);
                serv.setFechaHasta(miFecha);
                serv.setFechaAlta(miFecha);
                serv.setRespondido("SI");
                serv.setFechaRespondido(miFecha);
                serv.setOrigen("ADMINISTRADOR");
                serv.setIdServicioPadre("0");
                serv.setIdestado("0");
                serv.setServicios_tipo_ticket("1");
                serv.setPrioridad("4-BAJA");
                serv.setAction("add");
                serv.setObject("servicios");
                ServicioDTOAdd response = apiServices.addService(serv);
                if (response.getSucces().equals(false)) {
                    consorciosFallidosWeb.add(idConsorcioWeb);
                }
                System.out.println("======================================================================================");
                System.out.println();
            }
        }
    }

    /**
     * Obtiene el año en que se desea subir los archivos
     *
     * @return String anio
     **/
    public String getAnioASubir() {
        String carpeta = getNombreCarpetaMesEnCurso();
        String[] componentes = carpeta.split("-");
        return componentes[1];
    }

    /**
     * Obtiene la fecha de hoy parseada anio-mes-dia
     *
     * @return String fecha
     **/
    public String parsearFechaHoyParaWeb() {
        String fecha = getFechaHoy();
        String[] componentes = fecha.split("/");
        String anio = componentes[2];
        String mes = componentes[1];
        String dia = componentes[0];
        return anio + "-" + mes + "-" + dia;
    }

    /**
     * Devuelve el nombre escrito del mes en curso.
     *
     * @return String mes_actual
     **/
    public String getNombreMes() {
        String fecha = getFechaHoy();
        String[] componentes = fecha.split("/");
        String mes = componentes[1];
        if (chkFinMes.isSelected()) {
            int mesInt = Integer.parseInt(mes);
            mesInt++;
            if (mesInt == 13) {
                mesInt = 1;
            }
            mes = mesInt + "";
            if (mes.length() == 1) {
                mes = "0" + mes;
            }
        }
        String mesEscrito = "";
        if (mes.equals("01")) {
            mesEscrito = "enero";
        } else if (mes.equals("02")) {
            mesEscrito = "febrero";
        } else if (mes.equals("03")) {
            mesEscrito = "marzo";
        } else if (mes.equals("04")) {
            mesEscrito = "abril";
        } else if (mes.equals("05")) {
            mesEscrito = "mayo";
        } else if (mes.equals("06")) {
            mesEscrito = "junio";
        } else if (mes.equals("07")) {
            mesEscrito = "julio";
        } else if (mes.equals("08")) {
            mesEscrito = "agosto";
        } else if (mes.equals("09")) {
            mesEscrito = "septiembre";
        } else if (mes.equals("10")) {
            mesEscrito = "octubre";
        } else if (mes.equals("11")) {
            mesEscrito = "noviembre";
        } else if (mes.equals("12")) {
            mesEscrito = "diciembre";
        }
        return mesEscrito;
    }

    /**
     * Actualiza el label de la progress bar de la pantalla principal.
     *
     * @param texto : texto a actualizar en label
     **/
    public void actualizarLabelProgreso(String texto) {
        txtProgreso.setText(texto);
        super.update(this.getGraphics());
    }

    /**
     * Sube los archivos masivamente al directorio FTP
     **/
    public void subirMasivamente() {
        int tamanio = administradores.size();
        double prog = 0;
        if (tamanio > 0) {
            prog = 20 / tamanio;
        } else {
            prog = 20 / 1;
        }

        for (Administrador a : administradores) {
            String dirFTP = a.getConsorcios().get(0).getDirectorioFtp();
            String usr = a.getConsorcios().get(0).getUsuarioFtp().getUsuario();
            String pass = a.getConsorcios().get(0).getUsuarioFtp().getPassword();
            uploadFileByFTP(dirFTP, usr, pass, a.getNombre(), prog);
        }
    }

    /**
     * Sube los archivos sin realizar ninguna validacion
     * al directorio FTP de Casas DIaz
     **/
    public void subidaDirectaCasasDiaz() {
        actualizarProgressBarCero();
        ArrayList<String> archivos = getArchivos(directorioDescargas, "pdf");
        int septimoParam = archivos.size();
        int contador = 1;
        for (String arch : archivos) {
            arch = parsearBarraEscape(arch);
            String partes[] = arch.split("/");
            String ultimoNombre = partes[partes.length - 1];
            String primerParam = "ftp.casasdiaz.com.ar";
            String segundoParam = "public_html/Expensas/" + nombreCarpetaMesEnCurso + "/" + ultimoNombre;
            String tercerParam = "casasdiaz";
            String cuartoParam = "EzZg2NdAgWv8";
            String quintoParam = arch;
            Double sextoParam = 100.0 / archivos.size();
            int octavoParam = contador;
            upload(primerParam, segundoParam, tercerParam, cuartoParam, quintoParam, sextoParam, septimoParam, octavoParam);
            contador++;
        }
    }

    /**
     * Sube los archivos sin realizar ninguna validacion
     * al directorio FTP de Cuadrado / Zidarich
     **/
    public void subidaDirectaCuadrado() {
        System.out.println();
        actualizarProgressBarCero();
        ArrayList<String> archivos = getArchivos(directorioDescargas, "pdf");
        ArrayList<String> archivos2 = getArchivos(directorioDescargas, "PDF");
        for (String aaa : archivos2) {
            archivos.add(aaa);
        }
        int septimoParam = archivos.size();
        int contador = 1;
        for (String arch : archivos) {
            arch = parsearBarraEscape(arch);
            String partes[] = arch.split("/");
            String ultimoNombre = partes[partes.length - 1];
            if (cmbEstilo.getSelectedItem().toString().equals("ZIDARICH")) {
                String primerParam = "ftp.coterranea.net";
                String segundoParam = "/httpdocs/imagendigital/Zidarich/Expensas/" + nombreCarpetaMesEnCurso + "/" + ultimoNombre;
                String tercerParam = "coterranea.net";
                String cuartoParam = "CO1181@nea.ar";
                String quintoParam = arch;
                Double sextoParam = 100.0 / archivos.size();
                int octavoParam = contador;
                upload(primerParam, segundoParam, tercerParam, cuartoParam, quintoParam, sextoParam, septimoParam, octavoParam);
                contador++;
            } else if (cmbEstilo.getSelectedItem().toString().equals("CUADRADO")) {
                String primerParam = "ftp.admcuadrado.com.ar";
                String segundoParam = "/httpdocs/Expensas/" + nombreCarpetaMesEnCurso + "/" + ultimoNombre;
                String tercerParam = "admcuadrado.com";
                String cuartoParam = "AD1056@.com.ar";
                String quintoParam = arch;
                Double sextoParam = 100.0 / archivos.size();
                int octavoParam = contador;
                upload(primerParam, segundoParam, tercerParam, cuartoParam, quintoParam, sextoParam, septimoParam, octavoParam);
                contador++;
            }

        }
    }

    /**
     * Sube los archivos sin realizar ninguna validacion al directorio FTP de Zidarich
     * creando las carpetas correspondientes a cada consorcio
     *
     * @param consorcios : lista de consorcios a procesar
     **/
    public void subidaDirectaZidarich(ArrayList<Consorcio> consorcios) {
        System.out.println();
        actualizarProgressBarCero();
        ArrayList<String> archivos = getArchivos(directorioDescargas, "pdf");
        ArrayList<String> archivos2 = getArchivos(directorioDescargas, "PDF");
        for (String aaa : archivos2) {
            archivos.add(aaa);
        }
        int septimoParam = archivos.size();
        int contador = 1;
        for (String arch : archivos) {
            String nombreCarpetaConsorcio = "";
            for (Consorcio ccc : consorcios) {
                if (pdfReader.leerDeUnPdf(arch, ccc.getNombre())) {
                    nombreCarpetaConsorcio = ccc.getNombre();
                    break;
                }
            }
            arch = parsearBarraEscape(arch);
            String partes[] = arch.split("/");
            String ultimoNombre = partes[partes.length - 1];
            if (cmbEstilo.getSelectedItem().toString().equals("ZIDARICH")) {
                crearCarpetaEnServidorFTPZidarich("/httpdocs/imagendigital/Zidarich/Expensas/" + nombreCarpetaMesEnCurso + "/", "coterranea.net", "CO1181@nea.ar", nombreCarpetaConsorcio);
                String primerParam = "ftp.coterranea.net";
                String segundoParam = "/httpdocs/imagendigital/Zidarich/Expensas/" + nombreCarpetaMesEnCurso + "/" + nombreCarpetaConsorcio + "/" + ultimoNombre;
                String tercerParam = "coterranea.net";
                String cuartoParam = "CO1181@nea.ar";
                String quintoParam = arch;
                Double sextoParam = 100.0 / archivos.size();
                int octavoParam = contador;
                upload(primerParam, segundoParam, tercerParam, cuartoParam, quintoParam, sextoParam, septimoParam, octavoParam);
                contador++;
            } else if (cmbEstilo.getSelectedItem().toString().equals("CUADRADO")) {
                String primerParam = "ftp.admcuadrado.com.ar";
                String segundoParam = "/httpdocs/Expensas/" + nombreCarpetaMesEnCurso + "/" + ultimoNombre;
                String tercerParam = "admcuadrado.com";
                String cuartoParam = "AD1056@.com.ar";
                String quintoParam = arch;
                Double sextoParam = 100.0 / archivos.size();
                int octavoParam = contador;
                upload(primerParam, segundoParam, tercerParam, cuartoParam, quintoParam, sextoParam, septimoParam, octavoParam);
                contador++;
            }
        }
    }

    /**
     * Elimina los archivos que no corresponden a ninguna carpeta del directorio
     * FTP y eliminando carpetas vacias.
     *
     * @param directorio : directorio a eliminar
     **/
    public void eliminarCarpetasFTP(String directorio) {
        ArrayList<String> directorios = getDirectorios(directorio);
        if (directorios.size() > 0) {
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

    /**
     * Elimina los archivos despues de procesados del directorio local FTP
     **/
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

    /**
     * Splitea las paginas de los archivos pdf de los cupones de expensas
     * de todos los consorcios cargados.
     **/
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

    /**
     * Splitea por pagina cada archivo pdf de expensa pasado como parametro
     * utilizando secuencia de 3 digitos para denotar el numero de pagina del pdf
     *
     * @param directorio : directorio pdf a procesar
     **/
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

    /**
     * Carga el comboBox con los estilos disponibles
     **/
    public void cargarComboBoxEstilo(JComboBox cboo) {
        cboo.addItem("MANZUR");
        cboo.addItem("CASAS DIAZ");
        cboo.addItem("ZIDARICH");
        cboo.addItem("CUADRADO");
        AutoCompleteDecorator.decorate(cboo);
    }

    /**
     * Inicializa los directorios de la carpeta descargados a vacios.
     **/
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

    /**
     * Renombra y mueve los archivos MDB desde "descargados" a "salida"
     **/
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

    /**
     * Mueve los archivos ASP descargados desde "ftp" a "salida"
     **/
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

    /**
     * Actualiza el valor de la progressBar
     **/
    public void actualizarProgressBar() {
        progressBar.setValue(progreso);
        progressBar.update(progressBar.getGraphics());
    }

    /**
     * Pone en 0 la progressBar
     **/
    public void actualizarProgressBarCero() {
        progressBar.setValue(0);
        progressBar.update(progressBar.getGraphics());
    }

    /**
     * Crea en descargados una carpeta correspondiente a cada Administrador y reorganiza los archivos
     * puestos en "descargados" en la carpeta correspondiente a cada administrador.
     **/
    public void reacomodarArchivosDescargados() {
        ArrayList<String> archivosftp = getArchivos(directorioDescargas, "pdf");
        ArrayList<Administrador> misAdmins = administradorBO.getAdministradores();
        for (String archivo : archivosftp) {
            for (Administrador adm : misAdmins) {
                String nombreCapitalize = "";
                String[] compos = adm.getNombre().split(" ");
                for (int h = 0; h < compos.length; h++) {
                    nombreCapitalize += compos[h].substring(0, 1).toUpperCase() + compos[h].substring(1).toLowerCase() + " ";
                }
                nombreCapitalize = nombreCapitalize.trim();
                if (pdfReader.leerDeUnPdf(archivo, adm.getNombre()) || pdfReader.leerDeUnPdf(archivo, nombreCapitalize)) {
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

    /**
     * Devuelve una bandera si existe el directorio para un administrador
     *
     * @param nombre : nomre del administrador
     *
     * @return boolean bandera : true si existe una carpeta con el nombre del administrador
     *                           false si no existe una carpeta con el nombre del administrador
     **/
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

    /**
     * Descarga masivamente los archivos del tipo indicado del directorio del servidor FTP
     *
     * @param tipoArchivo : tipo / extension de los archivos que queremos descargar

     **/
    public void descargaMasiva(String tipoArchivo) {
        int tamanio = administradores.size();
        double prog = 0;
        if (tamanio > 0) {
            prog = 20 / tamanio;
        } else {
            prog = 20 / 1;
        }
        for (Administrador admin : administradores) {
            String url = admin.getConsorcios().get(0).getDirectorioFtp() + getNombreCarpetaInternaMesAnterior(getFechaHoy());
            System.out.println(url);
            String usuario = admin.getConsorcios().get(0).getUsuarioFtp().getUsuario();
            String password = admin.getConsorcios().get(0).getUsuarioFtp().getPassword();
            descargarArchivosDirectorioFTP(url, usuario, password, tipoArchivo, admin.getNombre(), prog);
        }
    }

    /**
     * Genera las carpetas del administrador y la carpeta del mes anterior al procesar
     * en el directorio FTP
     **/
    public void generarCarpetasParaPoderDescargarDelFtp() {
        for (Administrador admin : administradores) {
            crearCarpetaMesEnCursoEnDirectorio(directorioFtp + admin.getNombre());
            crearCarpetaMesEnCursoEnDirectorio(directorioFtp + admin.getNombre() + "\\" + getNombreCarpetaInternaMesAnterior(getFechaHoy()));
        }
    }

    /**
     * Descarga los archivos del servidor FTP segun la extension
     *
     * @param url : url del servidor FTP del administrador
     * @param usuario : usuario del servidor FTP del administrador
     * @param password : contraseña del servidor FTP del administrador
     * @param tipoExtension : tipo de archivo a descargar del servidor FTP
     * @param nombreAdmin : nombre del administrador a descargar
     * @param progress : Leva la cuenta para ir actualizando la progressBar
     **/
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

    /**
     * Descarga 1 archivo de servidor FTP
     *
     * @param user : usuario servidor FTP
     * @param pass : contraseña servidor FTP
     * @param localPath : directorio donde se guardará el archivo
     * @param remotePath : directorio del archivo a descargar
     * @param server : url del administrador en FTP
     **/
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

    /**
     * Crea la carpeta del mes correspondiente a subir al servidor FTP
     *
     * @param url : url de la carpeta destino en servidor FTP
     * @param user : usuario servidor FTP
     * @param pass : contraseña servidor FTP
     **/
    public void crearCarpetaEnServidorFTP(String url, String user, String pass) {
        ArrayList<String> archivos = getNombresCarpetasPDFEnDirectorioFTP(url, user, pass);
        boolean existeCarpeta = false;
        for (String a : archivos) {
            String[] partes = a.split("/");
            if (partes[partes.length - 1].trim().equals(nombreCarpetaMesEnCurso)) {
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
        if (existeCarpeta == false) {
            FTPClient client = new FTPClient();//Iniciamos el cliente del FTP
            try {
                client.connect(server);
                client.login(user, pass);
                String nuevoDirectorio = destino + nombreCarpetaMesEnCurso;
                client.makeDirectory(nuevoDirectorio);
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Problemas de conexión subiendo archivos. Intente nuevamente más tarde.");
            }
        }
    }

    /**
     * Crea la carpeta del mes correspondiente a subir al servidor FTP de Zidarich
     * y una carpeta para cada consorcio
     *
     * @param url : url de la carpeta destino en servidor FTP
     * @param user : usuario servidor FTP
     * @param pass : contraseña servidor FTP
     * @param nombreConsorcio : nombre del consorcio para generar su carpeta
     **/
    public void crearCarpetaEnServidorFTPZidarich(String url, String user, String pass, String nombreConsorcio) {
        ArrayList<String> archivos = getNombresCarpetasPDFEnDirectorioFTP(url, user, pass);
        boolean existeCarpeta = false;
        for (String a : archivos) {
            String[] partes = a.split("/");
            if (partes[partes.length - 1].trim().equals(nombreConsorcio)) {
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
        if (existeCarpeta == false) {
            FTPClient client = new FTPClient();//Iniciamos el cliente del FTP
            try {
                client.connect(server);
                client.login(user, pass);
                String nuevoDirectorio = destino + nombreConsorcio;
                client.makeDirectory(nuevoDirectorio);
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Problemas de conexión subiendo archivos. Intente nuevamente más tarde.");
            }
        }
    }

    /**
     * Sube un archivo al servidor FTP
     *
     * @param url : url destino en servidor FTP
     * @param user : usuario servidor FTP
     * @param pass : contraseña servidor FTP
     * @param nombreAdministrador : nombre del administrador
     * @param progres : cuenta de la progressBar para calcular su actualizacion
     **/
    public void uploadFileByFTP(String url, String user, String pass, String nombreAdministrador, double progres) {
        ArrayList<String> archivos = getNombresCarpetasPDFEnDirectorioFTP(url, user, pass);
        boolean existeCarpeta = false;
        for (String a : archivos) {
            String[] partes = a.split("/");
            if (partes[partes.length - 1].trim().equals(nombreCarpetaMesEnCurso)) {
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
                String nuevoDirectorio = destino + nombreCarpetaMesEnCurso;
                client.makeDirectory(nuevoDirectorio);

                for (String miArchivo : archivosSubir) {
                    String[] lasPartes = parsearBarraEscape(miArchivo).split("/");
                    String elNombre = lasPartes[lasPartes.length - 1];
                    //System.out.println(nuevoDirectorio + "/" + elNombre);
                    upload(server, nuevoDirectorio + "/" + elNombre, user, pass, miArchivo, miProgreso, tamanio, numero);
                    numero++;
                }

                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Problemas de conexión subiendo archivos. Intente nuevamente más tarde.");
            }
        } else {
            ArrayList<String> archivosEnCarpetaFTP = getNombresArchvivosPDFEnDirectorioFTPConSplit(url + nombreCarpetaMesEnCurso, user, pass, "pdf");
            ArrayList<String> archivosMDBEnCarpetaFTP = getNombresArchvivosPDFEnDirectorioFTP(url + nombreCarpetaMesEnCurso, user, pass, "mdb");
            ArrayList<String> archivosASPEnCarpetaFTP = getNombresArchvivosPDFEnDirectorioFTP(url + nombreCarpetaMesEnCurso, user, pass, "asp");
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
            for (String g : archivosSubir) {
                String[] lasPartes = parsearBarraEscape(g).split("/");
                String elNombre = lasPartes[lasPartes.length - 1];
                String nuevoDirectorio = destino + nombreCarpetaMesEnCurso;
                upload(server, nuevoDirectorio + "/" + elNombre, user, pass, g, miProgreso, tamanio, numero);
                numero++;
            }
        }
    }

    /**
     * Sube 1 archivo al FTP
     *
     * @param server : servidor FTP del administraor a subir
     * @param destino : directorio destino en servidor FTP
     * @param user : usuario servidor FTP
     * @param pass : contraseña servidor FTP
     * @param localPath : directorio local del archivo a subir
     * @param miProgreso : progreso actual
     * @param tamanio : cantidad de archivos a subir
     * @param numero : lleva la cuenta del numero de archivo que se esta subiendo
     **/
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
                if (firstLocalFile.delete()) {
                    System.out.println("Archivo eliminado correctamente: " + localPath);
                } else {
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

    /**
     * Se fija si tiene "_" y numeros para ver si es un archivo
     * spliteado (_001). Si no tiene devuelve el nombre completo con extension.
     *
     * @param nombreArchivo : nombre del archivo
     *
     * @return String nombre_archivo : nombre del archivo con su extension
     **/
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

    /**
     * trae los nombres de los arhchivos en un directorio de servidor ftp
     *
     * @param url : url del directorio de la carpeta
     * @param usuario : usuario del servidor FTP
     * @param password : contraseña del servidor FTP
     * @param extension : extension de los archivos
     *
     * @return ArrayList<String> nombres_archivos : lista de los nombres de los archivos en el directorio
     **/
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

    /**
     * trae los nombres de los arhchivos en un directorio de servidor ftp con la extension solicitada
     *
     * @param url : url del directorio de la carpeta
     * @param usuario : usuario del servidor FTP
     * @param password : contraseña del servidor FTP
     * @param extension : extension de los archivos
     *
     * @return ArrayList<String> nombres_archivos : lista de los nombres de los archivos en el directorio
     **/
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

    /**
     * trae los nombres de las carpetas en un directorio de servidor ftp
     *
     * @param url : url del directorio de la carpeta
     * @param usuario : usuario del servidor FTP
     * @param password : contraseña del servidor FTP
     *
     * @return ArrayList<String> nombres_archivos : lista de los nombres de los archivos en el directorio
     **/
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

    /**
     * Cambia los nombres de los archivos en "descargados" al nombre correspondiente
     * del archivo de la carpeta "ftp"
     *
     * @param ftp : lista de archivos en carpeta "ftp"
     **/
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

    /**
     * extrae la extension ".pdf" de un archivo
     *
     * @param directorioArchivo : directorio del archivo
     *
     * @return String nombre_archivo : devuelve el nombre del archivo sin extension
     **/
    private String extraerExtensionPDF(String directorioArchivo) {
        return directorioArchivo.replace(".pdf", "");
    }

    /**
     * extrae la extension ".pdf" de un archivo que viene con barras invertidas en el path
     *
     * @param directorioArchivo : directorio del archivo
     *
     * @return String nombre_archivo : devuelve el nombre del archivo sin extension
     **/
    private String parsearNombre(String directorioArchivo) {
        String[] directorio = directorioArchivo.replace("\\", "/").split("/");
        String nombre = directorio[directorio.length - 1].replace(".pdf", "");
        return nombre;
    }

    /**
     * obtiene la ruta a un archivo especifico
     *
     * @param directorioArchivo : directorio del archivo
     *
     * @return String path : devuelve el path del archivo sin su nombre
     **/
    private String obtenerRuta(String directorioArchivo) {
        String[] directorio = directorioArchivo.replace("\\", "/").split("/");
        String ruta = "";
        for (int i = 0; i < directorio.length - 1; i++) {
            ruta += directorio[i] + "\\";
        }
        return ruta;
    }

    /**
     * devuelve el nombre de la carpeta correspondiente al mes a procesar
     *
     * @return String nombre_carpeta : nombre de la carpeta correspondiente al mes a procesar
     **/
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

    /**
     * Crea un arreglo de administradores de todos los administradores dentro de la carpeta "ftp"
     *
     * @return ArrayList<Administrador> administradores_ftp : lista de administradores en la carpeta "ftp"
     **/
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

    /**
     * devuelve el nombre de la carpeta correspondiente al mes a anterior procesar
     *
     * @return String nombre_carpeta : nombre de la carpeta correspondiente al mes anterior a procesar
     **/
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

    /**
     * Genera la carpeta mes-anio segun la fecha pasada
     *
     * @param fecha : fecha para crear carpeta correspondiente mes-anio
     **/
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

    /**
     * busca carpetas vacias
     *
     * @param carpetaRaiz : carpeta donde buscara archivos
     *
     * @return List<File> archivos : devuelve una lista de carpetas que no tienen archivos
     **/
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

    /**
     * eilimina carpetas vacias
     *
     * @param directorio : directorio de la carpeta
     **/
    public void eliminarCarpetasVacias(String directorio) {
        File file = new File(directorio);
        List<File> listaCarpetasVacias = buscarCarpetasVacias(file);
        for (File carpeta : listaCarpetasVacias) {
            carpeta.delete();
        }
    }

    /**
     * de la lista de administraores de la carpeta de "descargados", elimina
     * todos aquellos que no tienen archivos y que no tienen consorcios cargados.
     **/
    private void quitarAdministradoresSinarchivos() {
        eliminarConsorciosSinArchivos();
        eliminarAdministradoresSinConsorcios();
    }

    /**
     * elimina los consorcios de la lista de archivos de "descargados" que no tienen
     * asignado ningun tipo de archivo (expensa, mora o liquidacion)
     **/
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


    /**
     * elimina de la lista de administradores (administradores) los administradores
     * que no tienen consorcios cargados en la carpeta de descargas
     **/
    private void eliminarAdministradoresSinConsorcios() {
        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getConsorcios().isEmpty()) {
                administradores.remove(i);
                i--;
            }
        }
    }

    /**
     * Reconoce los distintos tipos de archivos (expensas/liquidacion/mora) y asigna
     * a la lista de administradores de "descargados" el directorio correspondiente a
     * cada archivo.
     **/
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

    /**
     * Genera la lista de administradores en "descargados"
     **/
    private ArrayList<Administrador> getAdministradoresEnDirectorio(String directorio) {
        ArrayList<String> administradoresDescargados = getAdministradoresEnDirectorioPlano(directorio);
        System.out.println(administradoresDescargados.size());
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

    /**
     * reemplaza la barra invertida por barra comun /
     *
     * @return String linea : lina parseada sin barras invertidas
     **/
    private String parsearBarraEscape(String linea) {
        return linea.replace("\\", "/");
    }

    /**
     * devuelve una lista de los nombres de administradores en la carpeta
     * "descargas" (devuelve los nombres de las carpetas)
     *
     * @return ArrayList<String> carpetas : lista de nombres de carpetas de administradores
     **/
    private ArrayList<String> getAdministradoresEnDirectorioPlano(String directorio) {
        ArrayList<String> archivos = getArchivos(directorio, "pdf");
        ArrayList<String> archivos2 = getArchivos(directorio, "PDF");
        for (String aaa : archivos2) {
            archivos.add(aaa);
        }
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

    /**
     * crea un directorio con el nombre especificado
     *
     * @param directorioConNombreArchivo : file a crear
     **/
    private void crearCarpetaMesEnCursoEnDirectorio(String directorioConNombreArchivo) {
        File directorio = new File(directorioConNombreArchivo);
        directorio.mkdir();
    }

    /**
     * renombra y mueve los archivos del directorio de descargas al directorio de salida
     *
     * @param directorioDescarga : directorio origen (en "descargados")
     * @param directorioSalida : directorio destino (en "salida")
     * @param tipo : tipo de archivo
     *
     * @throws IOException
     **/
    private void renombrarYMoverArchivo(String directorioDescarga, String directorioSalida, String tipo) throws IOException {
        Path temp = Files.move(Paths.get(directorioDescarga + "." + tipo),
                Paths.get(directorioSalida + "." + tipo));
        if (temp != null) {
            System.out.println("-----" + directorioDescarga + "." + tipo + " -> " + directorioSalida + "." + tipo);
        } else {
            System.out.println("Falló en mover archivo: " + directorioDescarga);
        }
    }

    /**
     * Obtiene la fecha del dia de hoy.
     *
     * @retun String fecha : devuelve la fecha de hoy con formato dd/MM/yyyy
     **/
    private String getFechaHoy() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * trae el nombre de todos los archivos en un directorio
     *
     * @retun ArrayList<String> archivos : lista de archivos en un directorio
     **/
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

    /**
     * develve los directorios internos
     *
     * @retun ArrayList<String> directorios : lista de directorios
     **/
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

    /**
     * crea la imagen de coterranea para mostrar en el panel principal
     **/
    private void createUIComponents() {
        imagen = new JLabel(new ImageIcon("images/coterranea.png"));
    }
}