import java.util.HashMap;
import java.util.Map;

public class PrototypeRegistry {
    private Map<String, ElementosSnake> prototipos = new HashMap<>();

    // Registrar un prototipo puede ser tanto una parte de un snake o una manzana
    public void registrarPrototipo(String clave, ElementosSnake obj) {
        prototipos.put(clave, obj);
    }

    /*Aqui podemos obtener los elementos de un snake podria ayudar a saber si la snake
     * toco una parte de si misma o si toco un objeto manzana o si en dado caso
     * toco una pared.
    */
    public ElementosSnake obtenerPrototipo(String clave) {
        ElementosSnake prototipo = prototipos.get(clave);
        if (prototipo != null) {
            return prototipo.clone();
        }
        return null;
    }
}