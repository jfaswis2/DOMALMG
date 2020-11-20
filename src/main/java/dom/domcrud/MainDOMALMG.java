
package dom.domcrud;

/**
 *
 * @author jfas
 */
public class MainDOMALMG {

    public static void main(String[] args) {
        //Ruta del xml en nuestro proyecto
        String Direccion= "src\\main\\java\\XML\\LibroXML.xml";
        //Se instancia un objeto de tipo ClaseDOMALMG 
        ClaseDOMALMG dom = new ClaseDOMALMG(Direccion);
        //Se abre el XML de la ruta establecida
        dom.AbrirXMLDOM();
        //Lee el XML
        dom.LeerXMLDOM();
        //Regresa el contenido del XML (String)
        dom.MostrarXML();
        //Añade un nuevo nodo al XML
        dom.AñadirNodo("Don Quijote de la Mancha","Miguel de Cervantes","1605");
        //Guarda el nuevo nodo en el XML
        dom.guardarDOMcomoFILE();
    }    
}
