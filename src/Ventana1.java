import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;

public class Ventana1 extends JFrame {
    public Ventana1(){
        setTitle("¡Bienvenido!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setSize(960, 540);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        JButton boton1 = new JButton(".");
        //creamos el elemento tipo imagen para luego mostrarlo añadiendolo en una etiqueta
        ImageIcon imageninicio = new ImageIcon("src/imgs/gestion_de_usuarios.png");
        //adaptamos el tamaño de la imagen para que quepa en la ventana sin problema
        Image imageninicioBuena = imageninicio.getImage().getScaledInstance(1000, 500, Image.SCALE_SMOOTH);
        //ahora hacemos un nuevo ImageIcon con esa imagen reescalada
        ImageIcon imagenReescalada = new ImageIcon(imageninicioBuena);
        JLabel imagen = new JLabel(imagenReescalada);
        //ajustamos su posicion
        imagen.setBounds(140, 10, 1000,500);
        
        //ajuste posicion de los botones y cambiamos de color el ultimo
        boton1.setBounds(750, 390, 60, 40);
        //hacemos el boton transparente
        boton1.setOpaque(false);                //le ponemos que sea opaco en false
        boton1.setContentAreaFilled(false);     //le ponemos el rellenar area en false
        boton1.setBorderPainted(false);         //le ponemos el pintar borde en false

        JLabel etiquetainicio = new JLabel("Presiona sobre el cohete del botón Comenzar para empezar");
        etiquetainicio.setBounds(470, 550, 400, 50);
        this.add(imagen);
        this.add(boton1);
        this.add(etiquetainicio);

        //ponemos la ventana como visible ya que será por la que empezará el programa
        this.setVisible(true);
        boton1.setVisible(true);
        etiquetainicio.setVisible(true);

        //funcionalidad de los botones
        boton1.addActionListener(e -> {
            this.setVisible(false);
            Ventana2 ventana2 = new Ventana2();
            ventana2.setVisible(true);
        });
    }
}
