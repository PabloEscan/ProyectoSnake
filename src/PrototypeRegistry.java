import java.util.HashMap;
import java.util.Map;

// Se encarga de almacenar y recuperar los prototipos 
public class PrototypeRegistry {
    
    // HashMap para guardar los prototipos con una clave (String)
    private Map<String, ElementosSnake> prototipos = new HashMap<>();

    // Registra un prototipo en el mapa
    public void registrarPrototipo(String clave, ElementosSnake obj) {
        prototipos.put(clave, obj);
    }

    // Obtiene un prototipo (molde) del registro

    //Este método solo debe DEVOLVER el prototipo.
    // NO debe clonarlo.

    public ElementosSnake obtenerPrototipo(String clave) {
        return prototipos.get(clave);
    }
}