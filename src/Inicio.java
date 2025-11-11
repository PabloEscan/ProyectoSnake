import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Inicio extends JFrame {

    private GamePanel panelJuego;
    private JButton btnIniciarObjetos;
    private JButton btnClonarComida;
    private JButton btnActualizar;
    private JButton btnIniciar;
    private JTextField txtId, txtX, txtY;

    private PrototypeRegistry registro;
    private Snake snake;
    private Comida comidaBase;
    private Map<Integer, Comida> comidasClonadas;
    private AtomicInteger contadorIds;

    private boolean movimientoActivo = false;

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

        // Panel lateral de controles
        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        btnIniciarObjetos = new JButton("Iniciar objetos");
        btnClonarComida = new JButton("Clonar comida");
        btnActualizar = new JButton("Actualizar");
        btnIniciar = new JButton("Iniciar (Movimiento)");

        JLabel lblId = new JLabel("ID:");
        JLabel lblX = new JLabel("X:");
        JLabel lblY = new JLabel("Y:");
        txtId = new JTextField(5);
        txtX = new JTextField(5);
        txtY = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelControles.add(btnIniciarObjetos, gbc);
        gbc.gridy++; panelControles.add(btnClonarComida, gbc);
        gbc.gridy++; panelControles.add(new JLabel("Actualizar posición:"), gbc);
        gbc.gridwidth = 1;
        gbc.gridy++; panelControles.add(lblId, gbc); gbc.gridx = 1; panelControles.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy++; panelControles.add(lblX, gbc); gbc.gridx = 1; panelControles.add(txtX, gbc);
        gbc.gridx = 0; gbc.gridy++; panelControles.add(lblY, gbc); gbc.gridx = 1; panelControles.add(txtY, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; panelControles.add(btnActualizar, gbc);
        gbc.gridy++; panelControles.add(btnIniciar, gbc);

        add(panelControles, BorderLayout.EAST);

        // === Eventos ===
        btnIniciarObjetos.addActionListener(e -> iniciarObjetos());
        btnClonarComida.addActionListener(e -> clonarComida());
        btnActualizar.addActionListener(e -> actualizarComida());
        btnIniciar.addActionListener(e -> toggleMovimiento());
    }

    // ====================== MÉTODOS PRINCIPALES ======================

    private void iniciarObjetos() {
        comidasClonadas.clear();
        contadorIds.set(1);

        // Crear y registrar la comida prototipo
        comidaBase = new Comida(0, 5, 5);
        registro.registrarPrototipo("comidaBase", comidaBase);

        // Crear y registrar el segmento base y la serpiente
        snake = new Snake(0, panelJuego.getWidth() / 2, panelJuego.getHeight() / 2, "segmentoBase", registro);
        registro.registrarPrototipo("segmentoBase", snake.clone());

        JOptionPane.showMessageDialog(this, "Objetos base iniciados correctamente.");
        panelJuego.repaint();
    }

    private void clonarComida() {
        ElementosSnake prototipo = registro.obtenerPrototipo("comidaBase");
        if (prototipo == null) {
            JOptionPane.showMessageDialog(this, "Debes presionar 'Iniciar objetos' primero.");
            return;
        }

        Comida clon = (Comida) prototipo.clone();
        int nuevoId = contadorIds.getAndIncrement();
        clon.setId(nuevoId);
        clon.setX(50);
        clon.setY(100 + comidasClonadas.size() * 30);

        comidasClonadas.put(nuevoId, clon);
        panelJuego.repaint();
    }

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
            c.setX(x);
            c.setY(y);
            panelJuego.repaint();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.");
        }
    }

    private void toggleMovimiento() {
        movimientoActivo = !movimientoActivo;
        btnIniciar.setText(movimientoActivo ? "Detener" : "Iniciar (Movimiento)");
        if (movimientoActivo) panelJuego.requestFocusInWindow();
    }

    // ====================== PANEL DE JUEGO ======================

    private class GamePanel extends JPanel implements KeyListener {

        public GamePanel() {
            setBackground(Color.WHITE);
            setFocusable(true);
            addKeyListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Dibujar la comida base (id = 0)
            if (comidaBase != null) {
                comidaBase.dibujar(g);
            }

            // Dibujar las comidas clonadas
            for (Comida c : comidasClonadas.values()) {
                c.dibujar(g);
            }

            // Dibujar la serpiente
            if (snake != null) {
                snake.dibujar(g);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!movimientoActivo || snake == null) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> snake.moverArriba();
                case KeyEvent.VK_DOWN -> snake.moverAbajo();
                case KeyEvent.VK_LEFT -> snake.moverIzquierda();
                case KeyEvent.VK_RIGHT -> snake.moverDerecha();
            }

            // Verificar colisiones
            checkColisiones();

            repaint();
        }

        private void checkColisiones() {
            if (snake == null || comidasClonadas.isEmpty()) return;

            Rectangle cabeza = new Rectangle(snake.getX(), snake.getY(), 20, 20);
            Integer idColision = null;

            for (Map.Entry<Integer, Comida> entry : comidasClonadas.entrySet()) {
                Comida c = entry.getValue();
                Rectangle rComida = new Rectangle(c.getX(), c.getY(), 20, 20);
                if (cabeza.intersects(rComida)) {
                    idColision = entry.getKey();
                    break;
                }
            }

            if (idColision != null) {
                comidasClonadas.remove(idColision);
                snake.crecer();
            }
        }

        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    // ====================== MAIN ======================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inicio().setVisible(true));
    }
}
