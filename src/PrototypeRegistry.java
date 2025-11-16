import java.util.HashMap;
import java.util.Map;

// Clase para registrar y obtener prototipos
public class PrototypeRegistry {

    private Map<String, ElementosSnake> prototipos = new HashMap<>();

    // Registra un prototipo con clave
    public void registrarPrototipo(String clave, ElementosSnake obj) {
        prototipos.put(clave, obj);
    }

    // Obtiene el prototipo asociado a la clave
    public ElementosSnake obtenerPrototipo(String clave) {
        return prototipos.get(clave);
    }
}
