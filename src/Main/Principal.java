package Main;

import Business.ConsorcioBO;
import Business.PDFReader;
import Business.UsuarioFtpBO;
import Model.Administrador;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Business.AdministradorBO;
import Model.Consorcio;
import Model.UsuarioFtp;

public class Principal extends javax.swing.JFrame {
    private JPanel panel1;
    private JTextField tfFtp;
    private JTextField tfDescargas;
    private JButton RENOMBRARButton;
    private JTextField tfSalida;
    private JTextField tfFecha;

    private final String directorioFtp = "D:\\Coterranea\\1- Marianela\\ftp\\";
    private final String directorioDescargas = "D:\\Coterranea\\1- Marianela\\descargados\\";
    private final String directorioSalida = "D:\\Coterranea\\1- Marianela\\salida\\";
    private AdministradorBO administradorBO = new AdministradorBO();
    private ConsorcioBO consorcioBO = new ConsorcioBO();
    private UsuarioFtpBO usuarioFtoBO = new UsuarioFtpBO();
    private PDFReader pdfReader = new PDFReader();
    //Traemnos todos los administradores que tienen una carpeta en la carpeta de archivos descargados
    private ArrayList<Administrador> administradores = getAdministradoresDescargados();
    //Guarda los archivos no reconocidos (expensas, mora, liquidacion)
    private ArrayList<String> archivosDesconocidos = new ArrayList<String>();

    public Principal() {

        super("SeparadorDeArchivos");
        setContentPane(panel1);
        //guardamos en la variable administradores toda la info (consorcios, usuarios, etc)
        eliminarCarpetasVacias();


        reconocerTiposPDF();
        quitarAdministradoresSinarchivos();





        RENOMBRARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
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
                    resultado.addAll( buscarCarpetasVacias(carpeta) );
                }
            }
        }
        return resultado;
    }

    //eilimina carpetas vacias
    public void eliminarCarpetasVacias() {
        File file = new File(directorioDescargas);
        List<File> listaCarpetasVacias = buscarCarpetasVacias(file);
        for (File carpeta : listaCarpetasVacias) {
            carpeta.delete();
        }
    }

    private void quitarAdministradoresSinarchivos(){
        eliminarConsorciosSinArchivos();
        eliminarAdministradoresSinConsorcios();
    }

    //elimina los consorcios que no tienen archivos en la carpeta de descargas
    private void eliminarConsorciosSinArchivos(){
        for (int i = 0; i < administradores.size(); i++) {
            boolean bandera = false;
            ArrayList<Consorcio> consorciosAdmin = administradores.get(i).getConsorcios();
            for (int j = 0; j < consorciosAdmin.size(); j++) {
                Consorcio con = consorciosAdmin.get(j);
                if(con.getDirectorioDescargadoExpensas() == null && con.getDirectorioDescargadoMora() == null && con.getDirectorioDescargadoLiquidacion() == null){
                    administradores.get(i).getConsorcios().remove(j);
                    j--;
                }
            }
        }
    }

    //elimina de la lista de administradores (administradores) los administradores que no tienen consorcios cargados en la carpeta de descargas
    private void eliminarAdministradoresSinConsorcios(){
        for (int i = 0; i < administradores.size(); i++) {
            if(administradores.get(i).getConsorcios().isEmpty()){
                administradores.remove(i);
                i--;
            }
        }
    }

    private void reconocerTiposPDF(){
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
                    if(cons == true){
                        indexConsorcioAEditar = k;
                        break;
                    }
                }
                if(cons == true){
                    //identificamos si es expensa, mora o liquidacion
                    //EXPENSAS
                    if(pdfReader.leerDeUnPdf(archivos.get(j), "TALON CONSORCITA")){
                        administradores.get(i).getConsorcios().get(indexConsorcioAEditar).setDirectorioDescargadoExpensas(archivos.get(j));
                    }
                    //MORA
                    else if(pdfReader.leerDeUnPdf(archivos.get(j), "LISTADO DE CONSORCISTAS EN MORA AL")){
                        administradores.get(i).getConsorcios().get(indexConsorcioAEditar).setDirectorioDescargadoMora(archivos.get(j));
                    }
                    //LIQUIDACION
                    else if(pdfReader.leerDeUnPdf(archivos.get(j), "LIQUIDACION DE GASTOS Y ESTADO DE INGRESOS Y EGRESOS")){
                        administradores.get(i).getConsorcios().get(indexConsorcioAEditar).setDirectorioDescargadoLiquidacion(archivos.get(j));
                    }else{
                        archivosDesconocidos.add(archivos.get(j));
                    }
                    archivos.remove(j);
                    j--;
                }else{
                    archivosDesconocidos.add(archivos.get(j));
                    archivos.remove(j);
                    j--;
                }
            }
        }
        /*for (int i = 0; i < administradores.size(); i++) {
            System.out.println("Administrador: " + administradores.get(i).getNombre());
            for (int j = 0; j < administradores.get(i).getConsorcios().size(); j++) {
                System.out.println("Consorcio: " + administradores.get(i).getConsorcios().get(j).getNombre());
                System.out.println(" EXPENSAS: " + administradores.get(i).getConsorcios().get(j).getDirectorioDescargadoExpensas());
                System.out.println(" MORA: " + administradores.get(i).getConsorcios().get(j).getDirectorioDescargadoMora());
                System.out.println(" LIQUIDACION:" + administradores.get(i).getConsorcios().get(j).getDirectorioDescargadoLiquidacion());
                System.out.println("---------------");
            }
            System.out.println("============================================");
        }
        System.out.println("ARCHIVOS DESCONOCIDOS");
        for (int i = 0; i < archivosDesconocidos.size(); i++) {
            System.out.println(archivosDesconocidos.get(i));
        }*/
    }

    private ArrayList<Administrador> getAdministradoresDescargados(){
        ArrayList<String> administradoresDescargados = getAdministradoresDescargadosPlano(directorioDescargas);
        ArrayList<Administrador> administradores = new ArrayList<Administrador>();

        for (int i = 0; i < administradoresDescargados.size(); i++) {
            Administrador adm =  administradorBO.getAdministradorByNombre(administradoresDescargados.get(i));
            if(adm.getNombre() != null){
                administradores.add(adm);
            }
        }

        for (int i = 0; i < administradores.size(); i++) {
            ArrayList<Consorcio> consorcios = consorcioBO.getComplejosByIdAdministrador(administradores.get(i).getId());
            administradores.get(i).setConsorcios(consorcios);
            UsuarioFtp usuario = usuarioFtoBO.getUsuarioFtpByIdAdministrador(administradores.get(i).getId());
            administradores.get(i).setUsuarioFtp(usuario);
        }
        return administradores;
    }

    private String parsearBarraEscape(String linea){
        return linea.replace("\\", "/");
    }

    private String parsearBarraEscapeInverso(String linea){
        return linea.replace("/", "\\");
    }

    private ArrayList<String> getAdministradoresDescargadosPlano(String directorio){
        ArrayList<String> archivos = getArchivos(directorio,"pdf");
        ArrayList<String> archivosReturn = new ArrayList<String>();
        for (int i = 0; i < archivos.size(); i++) {
            String linea = parsearBarraEscape(archivos.get(i));
            directorio = parsearBarraEscape(directorio);
            linea = linea.replace(directorio, "");
            linea = linea.split("/")[0];
            if(!archivosReturn.contains(linea)){
                archivosReturn.add(linea);
            }
        }
        return archivosReturn;
    }

    //crea un directorio con el nombre especificado
    private void crearCarpetaMesEnCursoEnServidor(String directorioConNombreArchivo) {
        File directorio = new File(directorioConNombreArchivo);
        directorio.mkdir();
    }

    //renombra y mueve los archivos del directorio de descargas al directorio de salida
    private void renombrarYMoverArchivo(String descargado, String ftp, String tipo) throws IOException {
        Path temp = Files.move(Paths.get(directorioDescargas + descargado + tipo),
                Paths.get(directorioSalida + ftp + tipo));
        if (temp != null) {
            System.out.println("File renamed and moved successfully");
        } else {
            System.out.println("Failed to move the file");
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
}
