import java.awt.Graphics;

public abstract class ElementosSnake implements Cloneable {

    protected int id;
    protected int x;
    protected int y;
    protected String tipo;

    public ElementosSnake(int id, int x, int y, String tipo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }
    
    /*Método abstracto para dibujar cada clase debe sobrescribirlo dependiendo el uso por ejemplo la
     * manzana debe ser un circulo o una figura con su respectivo color, y el snake un cuadro.
    */
    public abstract void dibujar(Graphics g);
    
    //ID
    public int getId(){ 
        return id; 
    }
    public void setId(int id){
        this.id = id; 
    }

    //X
    public int getX(){ 
        return x; 
    }  
    public void setX(int x){ 
        this.x = x; 
    }

    //Y
    public int getY(){ 
        return y; 
    }
    public void setY(int y){ 
        this.y = y; 
    }
    
    //TIPO
    public String getTipo(){ 
        return tipo; 
    }
    public void setTipo(String tipo){ 
        this.tipo = tipo; 
    }

    /*Esta parte aun esta de probar la hize a ciegas ya que no se aun como sera la clase snake o la manzana ni 
     * como manejaremos el espacio..
    */
    
    @Override
    public ElementosSnake clone() {
        try {
            ElementosSnake copia = (ElementosSnake) super.clone();
            switch (tipo.toLowerCase()) {
                case "snake":
                    /*El nuevo segmento va detrás: ajustamos Y o X este metodo aun no esta termiado 
                     * faltaria ver una forma para que el matron le ponga atras de Y o X sin que se ponga
                     * mal o que deje de seguir a la orginal esto se hara en la parte grafica o en la clase
                     * tambien porque dependiendo del tamaño del snake se debe poner sin que se vean espacios
                     * blancos
                    */
                    copia.setY(this.y + 10);
                    break;
                case "manzana":
                    /*La nueva manzana se pone abajo sumamos y ya que asi se colocara más abajo de la pantalla
                     * tambien este metodo esta por decir asi terminado ya que no hay mucho más que hacer solo
                     * ajustar lo que es si funciona o si en dado caso debe estar restando dependiendo de como 
                     * configuremos las coordenadas
                    */
                    copia.setY(this.y + 30);
                    break;
                default:
                    // por si se clona algo que no necesita de algo extra
                    break;
            }

            // Cambiamos el ID para distinguir los clones sumando 1
            copia.setId(this.id + 1);

            return copia;

        } catch (CloneNotSupportedException e) {
            System.out.println("Error: la clonación no es posible");
            e.printStackTrace();
            return null;
        }
    }
}

