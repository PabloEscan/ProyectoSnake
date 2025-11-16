import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// Clase principal que contiene la ventana y la lógica del juego
public class Inicio extends JFrame {
    private GamePanel panelJuego; // Panel donde se dibuja el juego
    private JButton btnIniciarObjetos;
    private JButton btnClonarComida;
    private JButton btnActualizar;
    private JButton btnIniciar;
    private JTextField txtId, txtX, txtY;
    private PrototypeRegistry registro; // Registro de prototipos para clonación
    private Snake snake;
    private Comida comidaBase; // Prototipo de comida base
    private Map<Integer, Comida> comidasClonadas; // Almacena manzanas clonadas con su id
    private AtomicInteger contadorIds; // Para asignar IDs únicos
    private boolean movimientoActivo = false; // Controla si la serpiente puede moverse

    public Inicio() {
        setTitle("Juego Snake - Patrón Prototype");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        registro = new PrototypeRegistry();
        comidasClonadas = new LinkedHashMap<>();
        contadorIds = new AtomicInteger(1);

        // Panel central del juego
        panelJuego = new GamePanel();
        add(panelJuego, BorderLayout.CENTER);

        // Panel lateral con controles y campos para actualizar
        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBackground(new Color(11, 61, 11));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        btnIniciarObjetos = new JButton("Iniciar objetos");
        btnClonarComida = new JButton("Clonar comida");
        btnActualizar = new JButton("Actualizar");
        btnIniciar = new JButton("Iniciar (Movimiento)");

        // Estilos para botones
        JButton[] botones = {btnIniciarObjetos, btnClonarComida, btnActualizar, btnIniciar};
        for (JButton b : botones) {
            b.setBackground(new Color(30, 130, 76));
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
        }

        JLabel lblId = new JLabel("ID:");
        JLabel lblX = new JLabel("X:");
        JLabel lblY = new JLabel("Y:");
        lblId.setForeground(Color.WHITE);
        lblX.setForeground(Color.WHITE);
        lblY.setForeground(Color.WHITE);

        JLabel lblActualizar = new JLabel("Actualizar posición:");
        lblActualizar.setForeground(Color.WHITE);

        txtId = new JTextField(5);
        txtX = new JTextField(5);
        txtY = new JTextField(5);

        // Agregar componentes al panel controles
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelControles.add(btnIniciarObjetos, gbc);
        gbc.gridy++;
        panelControles.add(btnClonarComida, gbc);
        gbc.gridy++;
        panelControles.add(lblActualizar, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panelControles.add(lblId, gbc);
        gbc.gridx = 1;
        panelControles.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelControles.add(lblX, gbc);
        gbc.gridx = 1;
        panelControles.add(txtX, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelControles.add(lblY, gbc);
        gbc.gridx = 1;
        panelControles.add(txtY, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panelControles.add(btnActualizar, gbc);
        gbc.gridy++;
        panelControles.add(btnIniciar, gbc);

        add(panelControles, BorderLayout.EAST);

        // Eventos para botones
        btnIniciarObjetos.addActionListener(e -> iniciarObjetos());
        btnClonarComida.addActionListener(e -> clonarComida());
        btnActualizar.addActionListener(e -> actualizarComida());
        btnIniciar.addActionListener(e -> toggleMovimiento());
    }

    // Método para inicializar los objetos base (comida y serpiente)
    private void iniciarObjetos() {
        comidasClonadas.clear();
        contadorIds.set(1);

        // Crear prototipo de comida en la celda (0,0)
        comidaBase = new Comida(0, 0, 0);
        registro.registrarPrototipo("comidaBase", comidaBase);

        // Crear serpiente en el centro de la pantalla en coordenadas de celda
        int startX = (panelJuego.getWidth() / 2) / 20;
        int startY = (panelJuego.getHeight() / 2) / 20;
        snake = new Snake(0, startX, startY, "serpiente", registro);

        // Crear y registrar segmento base para crecer la serpiente
        ElementosSnake segmentoBase = new ElementosSnake(0, 0, 0, "segmento") {
            @Override
            public void dibujar(Graphics g) {
                // Dibuja segmento de cola multiplicando x,y por 20 (pixeles)
                g.setColor(new Color(0, 100, 0));
                g.fillRect(x * 20, y * 20, 20, 20);
            }
        };
        registro.registrarPrototipo("segmentoBase", segmentoBase);

        JOptionPane.showMessageDialog(this, "Objetos base iniciados correctamente.");
        panelJuego.repaint();
    }

    // Método para clonar comida (manzana) agregándola una celda abajo de la última
    private void clonarComida() {
        ElementosSnake prototipo = registro.obtenerPrototipo("comidaBase");
        if (prototipo == null) {
            JOptionPane.showMessageDialog(this, "Debes presionar 'Iniciar objetos' primero.");
            return;
        }

        Comida clon = (Comida) prototipo.clone();
        int nuevoId = contadorIds.getAndIncrement();
        clon.setId(nuevoId);

        // Posición en celdas: misma columna X, y aumenta 1 en Y por cada clon
        int posX = comidaBase.getX();
        int posY = comidaBase.getY() + (comidasClonadas.size() + 1);

        // Validar que no se salga del panel
        int maxCeldasY = panelJuego.getHeight() / 20;
        if (posY >= maxCeldasY) {
            JOptionPane.showMessageDialog(this, "No se puede clonar: la comida aparecería fuera del panel.");
            return;
        }

        clon.setX(posX);
        clon.setY(posY);

        comidasClonadas.put(nuevoId, clon);
        panelJuego.repaint();
    }

    // Método para actualizar la posición de una comida clonada según entrada del usuario
    private void actualizarComida() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            int x = Integer.parseInt(txtX.getText().trim());
            int y = Integer.parseInt(txtY.getText().trim());

            Comida c = comidasClonadas.get(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No existe comida con ID " + id);
                return;
            }

            int maxCeldasX = panelJuego.getWidth() / 20;
            int maxCeldasY = panelJuego.getHeight() / 20;

            // Validar que la posición esté dentro de la cuadrícula
            if (x < 0 || x >= maxCeldasX || y < 0 || y >= maxCeldasY) {
                JOptionPane.showMessageDialog(this, "Posición fuera del área del juego.");
                return;
            }

            // Actualizar posición en coordenadas de celda
            c.setX(x);
            c.setY(y);
            panelJuego.repaint();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.");
        }
    }

    // Activa o desactiva el movimiento de la serpiente
    private void toggleMovimiento() {
        movimientoActivo = !movimientoActivo;
        btnIniciar.setText(movimientoActivo ? "Detener" : "Iniciar (Movimiento)");
        if (movimientoActivo) panelJuego.requestFocusInWindow();
    }

    // Panel donde se dibuja el juego y se capturan teclas
    private class GamePanel extends JPanel implements KeyListener {

        // Verifica si el movimiento a la celda (x,y) es válido dentro del panel
        private boolean movimientoPermitido(int x, int y) {
            int maxCeldasX = getWidth() / 20;
            int maxCeldasY = getHeight() / 20;
            return x >= 0 && y >= 0 && x < maxCeldasX && y < maxCeldasY;
        }

        public GamePanel() {
            setBackground(Color.WHITE);
            setFocusable(true);
            addKeyListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dibuja la cuadrícula estilo ajedrez
            Color c1 = new Color(210, 166, 121);
            Color c2 = new Color(255, 138, 117);

            for (int y = 0; y < getHeight(); y += 20) {
                for (int x = 0; x < getWidth(); x += 20) {
                    g.setColor(((x + y) / 20) % 2 == 0 ? c1 : c2);
                    g.fillRect(x, y, 20, 20);
                }
            }

            // Dibuja la comida base y todas las comidas clonadas
            if (comidaBase != null) comidaBase.dibujar(g);
            for (Comida c : comidasClonadas.values()) c.dibujar(g);

            // Dibuja la serpiente
            if (snake != null) snake.dibujar(g);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!movimientoActivo || snake == null) return;

            // Movimiento por teclas con validación dentro de la cuadrícula
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {
                    if (movimientoPermitido(snake.getX(), snake.getY() - 1))
                        snake.moverArriba();
                }
                case KeyEvent.VK_DOWN -> {
                    if (movimientoPermitido(snake.getX(), snake.getY() + 1))
                        snake.moverAbajo();
                }
                case KeyEvent.VK_LEFT -> {
                    if (movimientoPermitido(snake.getX() - 1, snake.getY()))
                        snake.moverIzquierda();
                }
                case KeyEvent.VK_RIGHT -> {
                    if (movimientoPermitido(snake.getX() + 1, snake.getY()))
                        snake.moverDerecha();
                }
            }

            checkColisiones();
            repaint();
        }

        // Verifica colisiones entre la cabeza de la serpiente y la comida o cola
        private void checkColisiones() {
            if (snake == null) return;

            // Crea un rectángulo para la cabeza en pixeles
            Rectangle cabeza = new Rectangle(snake.getX() * 20, snake.getY() * 20, 20, 20);
            Integer idColision = null;

            // Recorre todas las comidas clonadas para detectar colisión
            for (Map.Entry<Integer, Comida> entry : comidasClonadas.entrySet()) {
                Comida c = entry.getValue();
                Rectangle r = new Rectangle(c.getX() * 20, c.getY() * 20, 20, 20);
                if (cabeza.intersects(r)) {
                    idColision = entry.getKey();
                    break;
                }
            }

            if (idColision != null) {
                // Si colisiona, elimina la comida y crece la serpiente
                comidasClonadas.remove(idColision);
                snake.crecer();
                panelJuego.repaint();
                return;
            }

            // Evita auto-colisión si acaba de crecer
            if (snake.isAcabaDeCrecer()) {
                snake.setAcabaDeCrecer(false);
                return;
            }

            // Verifica si la serpiente se mordió a sí misma
            try {
                var colaField = snake.getClass().getDeclaredField("cola");
                colaField.setAccessible(true);
                java.util.List<ElementosSnake> cola = (java.util.List<ElementosSnake>) colaField.get(snake);

                for (ElementosSnake segmento : cola) {
                    Rectangle r = new Rectangle(segmento.getX() * 20, segmento.getY() * 20, 20, 20);
                    if (cabeza.intersects(r)) {
                        movimientoActivo = false;
                        btnIniciar.setText("Iniciar (Movimiento)");
                        JOptionPane.showMessageDialog(null, "¡Game Over! La serpiente se mordió a sí misma.");
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }

    // Método main para arrancar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inicio().setVisible(true));
    }
}
