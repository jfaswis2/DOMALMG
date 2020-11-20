package dom.domcrud;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jfas
 */
public class ClaseDOMALMG {
    
    //Atributos
    private Document Doc;
    private File Archivo;
    private String Directorio;
    private boolean ValidacionDOcumento;
    private boolean ExistenciaDocumento;
    private boolean AbiertoCerradoXML;
    private String ContenidoXML;
    private boolean Leido;
    
    //Constructor 
    public ClaseDOMALMG(String Directorio) {
        this.Directorio = Directorio;
        this.ExistenciaDocumento = Existir();
        this.ValidacionDOcumento = Validar();
    }

    public boolean Validar() {
        //Usamos expresiones regulares con el metodo matches de String 
        //para comprobar que el XML tenga una extensión .xml. 
        //Regresa true si la contiene, si no; false.
        if (this.ExistenciaDocumento == true) {
            if (Directorio.matches(".*.xml$")) {
                this.ValidacionDOcumento = true;
                System.out.println("El XML si existe");
                return true;
            } else {
                this.ValidacionDOcumento = false;
                return false;
            }
        } else {
            System.out.println("No se puede validar ya que el xml no existe");
            return false;
        }
    }

    public boolean Existir() {
        //Usamos el metodo exists de File para comprobar si el directorio
        //existe. Regresa true si existe, si no; false.
        Archivo = new File(this.Directorio);
        if (!Archivo.exists()) {
            this.ExistenciaDocumento = false;
            System.out.println("El directorio no existe");
            return false;
        }else {
            this.ExistenciaDocumento = true;
            System.out.println("El directorio si existe");
            return true;
        }
    }

    public Document AbrirXMLDOM() {
        if (this.ExistenciaDocumento == true && this.ValidacionDOcumento == true) {
            try {
                //Se crea un objeto DocumentBuiderFactory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                //Indica que el modelo DOM no debe contemplar los comentarios
                factory.setIgnoringComments(true);
                //Ignora los espacios en blanco que tenga el documento 
                factory.setIgnoringElementContentWhitespace(true);
                //Se crea un objeto DocumentBuilder para cargar en él la 
                //estructura de árbol DOM a partir del XML
                //seleccionado
                DocumentBuilder builder = factory.newDocumentBuilder();
                //Interpreta (parsear) el documento XML (file) y genera el DOM 
                this.Doc = builder.parse(this.Archivo);
                //El valor del atributo es true ya que se ha podido abrir el XML
                this.AbiertoCerradoXML = true;
                System.out.println("El XML se ha abierto correctamente (return Document)");
                
                return Doc;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("return null");
                //El valor del atributo es true ya que no se ha podido abrir el
                //XML
                this.AbiertoCerradoXML = false;
                
                return null;
            }
        } else {
            this.AbiertoCerradoXML = false;
            System.out.println("No se puede abrir el XML ya que no existe el documento y/o no es un XML (return null)");
            return null;
        }
    }

    public boolean LeerXMLDOM() {
        if (this.AbiertoCerradoXML == true) {
            String datos_nodo[] = null;
            String salida = "";
            Node node;
            //Obtiene el primero nodo del DOM (primer hijo)
            Node raiz = this.Doc.getFirstChild();
            Node ntemp = null;
            //Obtiene una lista de nodos con todos los nodos hijo del raíz.
            NodeList nodelist = raiz.getChildNodes();
            //El tamaño del array es por la cantidad de nodos que hay en el XML
            String datos[] = new String[nodelist.getLength()];
            //Procesa los nodos hijo
            for (int i = 0; i < nodelist.getLength(); i++) {
                node = nodelist.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    //Es un nodo libro
                    int contador = 1;
                    //Obtiene el valor del primer atributo del nodo
                    datos[0] = node.getAttributes().item(0).getNodeValue();
                    //Obtiene los hijos del Libro (titulo y autor)
                    NodeList nodos = node.getChildNodes();
                    for (int j = 0; j < nodos.getLength(); j++) {
                        ntemp = nodos.item(j);

                        if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                            datos[contador] = ntemp.getChildNodes().item(0).getNodeValue();
                            contador++;
                        }

                    }
                    //Se guarda titulo, autor y año en el array datos[].
                    salida = salida + "\n" + "Publicado en: " + datos[0];
                    salida = salida + "\n" + "El titulo es: " + datos[1];
                    salida = salida + "\n" + "El autor es: " + datos[2];
                }
            }
            //Se guarda en el atributo contenidoXML
            this.ContenidoXML = salida;
            //True para comprobar de que se ha leido el contenido del XML
            this.Leido = true;
            System.out.println("Se ha leido correctamente");
            return true;
        } else {
            System.out.println("No se ha leido correctamente");
            //False ya que no se ha podido leer el contenido del XML
            this.Leido = false;
            return false;
        }

    }

    public String MostrarXML() {
        //Al leer el XML con la funcion LeeerXMLDOM() se guarda lo leido
        //en el atributo ContenidoXML, y aqui lo retornamos
        if (this.Leido == true) {
            System.out.println(this.ContenidoXML);
            return this.ContenidoXML;
        }else {
            return "No se ha podido mostrar el XML";
        }
    }

    public boolean AñadirNodo(String Titulo, String Autor, String anno) {

        if (this.AbiertoCerradoXML == true) {//Comprobamos que el XML está 
                                             //abierto
            try {
                //Se crea un nodo tipo Element y nombre "Titulo"
                Node ntitulo = this.Doc.createElement("Titulo");
                //Se crea un nodo tipo texto con el título del libro
                Node ntitulo_text = this.Doc.createTextNode(Titulo);
                //Se añade el nodo de texto con el título como
                //hijo del elemento Titulo 
                ntitulo.appendChild(ntitulo_text);

                //Se crea un nodo tipo Element y nombre "Autor"
                Node nautor = this.Doc.createElement("Autor");
                //Se crea un nodo tipo texto con el autor del libro
                Node nautor_text = this.Doc.createTextNode(Autor);
                //Se añade el nodo de texto con el autor como
                //hijo del elemento Autor 
                nautor.appendChild(nautor_text);
                
                //Se crea un nodo tipo Element y nombre "Autor"
                Node nlibro = this.Doc.createElement("Libro");
                //Se le añade un atributo al libro con el año de publicacion
                ((Element) nlibro).setAttribute("Publicado_en", anno);
                //Se le añade al nodo Libro los hijos Titulo y Autor
                nlibro.appendChild(ntitulo);
                nlibro.appendChild(nautor);
                //Se obtiene el primer nodo del documento
                Node raiz = this.Doc.getChildNodes().item(0);
                //Se añade como hijo el nodo libro que ya tiene colagando 
                //todos sus hijos y atributos
                raiz.appendChild(nlibro);

                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            //Si no esta abierto el XML 
            return false;
        }
    }

    public boolean guardarDOMcomoFILE() {
        try {
            //Crea un fichero llamado donde se guardara el xml con sus 
            //modificaciones, en este caso sera el mismo XML que abrimos al
            //al principio
            File archivo_XML = new File("src\\main\\java\\XML\\LibroXML.xml");
            //Especifica el formato de salida
            OutputFormat format = new OutputFormat(this.Doc);
            //Especifica que la salida esté indentada. 
            format.setIndenting(true);
            //Escribe el contenido en el FILE
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(archivo_XML), format);
            serializer.serialize(this.Doc);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
