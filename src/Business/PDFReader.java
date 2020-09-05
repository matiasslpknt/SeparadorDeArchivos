package Business;

import java.io.File;
import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFReader {
    public PDFReader() {
    }

    public boolean init(String directorio, String buscar) {
        PDFReader leerPDF = new PDFReader();
        return leerPDF.readPDFInFolder(directorio, buscar);
    }

    /**
     * busca un string en un archivo
     *
     * @param directorio : directorio del archivo donde se va buscar
     * @param buscar : cadena a buscar
     *
     * @retun boolean bandera : true si hay coincidencia de busqueda
     **/
    public boolean leerDeUnPdf(String directorio, String buscar) {
        PDFReader leerPDF = new PDFReader();
        return leerPDF.readPDF(directorio, buscar);
    }

    /**
     * busca un string en un archivo
     *
     * @param directory : directorio del archivo donde se va buscar
     * @param label : cadena a buscar
     *
     * @retun boolean bandera : true si hay coincidencia de busqueda
     **/
    public boolean readPDF(String directory, String label) {
        try {
            //recorro cada uno de los elementos
            //carga el archivo
            PDDocument pdf = PDDocument.load(new File(directory));
            //extramos su contenido
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setEndPage(1);
            String content = pdfTextStripper.getText(pdf);
            //cerramos el archivo
            pdf.close();
            //preguntamos si existe lo que se quiere buscar
            if (content.contains(label)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Metodo que permite abrir PDF y buscar si contiene una palabra Si la
     * contiene guarda en un archivo de texto el nombre del archivo
     *
     * @param directory la ruta absoluta del directorio que busca los PDF
     * @param label     La etiqueta a buscar
     */
    public boolean readPDFInFolder(String directory, String label) {
        File directoryFile = new File(directory); // carpeta donde estan los pdf
        String[] listFiles = directoryFile.list();//extrae los nombres de archivo
        String slash = System.getProperty("file.separator");
        if (listFiles.length == 0) {
            return false;
        } else {
            //creamos un flujo donde se pondran los nombres de los archivos que contienen los elementos
            try {
                //recorro cada uno de los elementos
                for (String file : listFiles) {
                    //preguntamos si es un pdf
                    if (!file.toLowerCase().endsWith(".pdf"))
                        continue;
                    //carga el archivo
                    PDDocument pdf = PDDocument.load(new File
                            (directory + slash + file));
                    //extramos su contenido
                    String content = new PDFTextStripper()
                            .getText(pdf);
                    //cerramos el archivo
                    pdf.close();
                    //preguntamos si existe lo que se quiere buscar
                    if (content.contains(label)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Metodo que permite mandar un JFileChooser que es una ventanita para poder
     * escoger el directorio
     *
     * @return la ruta especifica donde buscara el directorio
     */
    public String getDirectory() {
        String ruta = "";
        // componente de dialogo
        JFileChooser jfc = new JFileChooser();

        // configuramos para que solo sean directorios
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //mandamos el cuadro, esperamos una respuesta aprobatoria
        int dialogResult = jfc.showOpenDialog(null);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            ruta = jfc.getSelectedFile().getAbsolutePath();
        }
        return ruta;
    }
}
