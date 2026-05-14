import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.mindrot.jbcrypt.BCrypt;


public class Ventana2 extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private final String url = "jdbc:mysql://localhost:3306/programacion";
    private final String user = "root";
    private final String contra = "";

    public Ventana2() {
        setTitle("Gestión de Usuarios");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

  
        String[] columnas = {"ID", "Usuario", "Contraseña", "Perfil", "Estado", "Email"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        JButton botonRefrescar = new JButton("Refrescar tabla");
        JButton botonCrear = new JButton("Crear usuario");
        JButton botonActualizar = new JButton("Actualizar datos");
        JButton botonBorrar = new JButton("Borrar");

        panelBotones.add(botonRefrescar);
        panelBotones.add(botonCrear);
        panelBotones.add(botonActualizar);
        panelBotones.add(botonBorrar);
        add(panelBotones, BorderLayout.SOUTH);

        botonRefrescar.addActionListener(e -> cargarDatos());
        botonCrear.addActionListener(e -> crearUsuario());
    
        botonActualizar.addActionListener(e -> {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla para modificarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                //extraemos los datos EXACTOS según las columnas de nuestra tabla (0 al 5)
                int idActual = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                String usuarioActual = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
                String contraseñaActual = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
                String perfilActual = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
                String estadoActual = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
                String emailActual = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
                 

                editarUsuario(idActual, usuarioActual, contraseñaActual, perfilActual, estadoActual, emailActual);
            }
        });

        botonBorrar.addActionListener(e -> {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla para borrarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                
                int confirmacion = JOptionPane.showConfirmDialog(this, "Seguro que quieres borrar el usuario con ID " + idUsuario + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    eliminarUsuario(idUsuario);
                    cargarDatos();
                }
            }
        });

        //cargamos los datos nada mas se inicie la ventana
        cargarDatos();
    }

    /**
     * Método para cargar todos los datos de la base de datos en la tabla de la ventana2
     */
    private void cargarDatos() {
        modeloTabla.setRowCount(0);

        try (Connection conexion = DriverManager.getConnection(url, user, contra);
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery("SELECT id_usuario, usuario, password, perfil, estado, email FROM usuarios")) {

            while (resultado.next()) {
                modeloTabla.addRow(new Object[]{
                        resultado.getInt("id_usuario"),
                        resultado.getString("usuario"),
                        resultado.getString("password"),
                        resultado.getString("perfil"),
                        resultado.getString("estado"),
                        resultado.getString("email")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    /** Método para eliminar usuario de la base de datos
     * @param id
     */
    private void eliminarUsuario(int id) {
        try (Connection conexion = DriverManager.getConnection(url, user, contra);
             PreparedStatement sentenciaSQL = conexion.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?")) {
            
            sentenciaSQL.setInt(1, id);
            sentenciaSQL.executeUpdate();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Método que crea una ventana para introducir los datos del usuario que se desea crear en la base de datos
     */
    private void crearUsuario() {
        JTextField campoUsuario = new JTextField();
        JPasswordField campoPassword = new JPasswordField();
        JTextField campoEmail = new JTextField();
        JComboBox<String> campoPerfil = new JComboBox<>(new String[]{"admin", "trabajador", "responsable", "socio"});
        JComboBox<String> campoEstado = new JComboBox<>(new String[]{"activo", "inactivo", "suspendido"});

        Object[] mensaje = {
            "Usuario:", campoUsuario,
            "Contraseña:", campoPassword,
            "Email:", campoEmail,
            "Perfil:", campoPerfil,
            "Estado:", campoEstado
        };

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Crear nuevo usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String textoPassword = new String(campoPassword.getPassword());
            if (campoUsuario.getText().isEmpty() || textoPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Faltan datos necesarios");
                return;
            }
            
            //encriptamos la contraseña antes de guardarla y le pasamos los datos al metodo insertarUsuario
            String passwordHash = BCrypt.hashpw(textoPassword, BCrypt.gensalt(12));
            insertarUsuario(campoUsuario.getText(), passwordHash, (String) campoPerfil.getSelectedItem(), (String) campoEstado.getSelectedItem(), campoEmail.getText());
            cargarDatos();
        }
    }

    /**
     * Método para crear un nuevo usuario en la base de datos
     * @param usuario
     * @param password
     * @param perfil
     * @param estado
     * @param email
     */
    private void insertarUsuario(String usuario, String password, String perfil, String estado, String email) {
        try (Connection conexion = DriverManager.getConnection(url, user, contra);
             PreparedStatement consulta = conexion.prepareStatement("INSERT INTO usuarios (usuario, password, perfil, estado, email) VALUES (?, ?, ?, ?, ?)")) {
            
            consulta.setString(1, usuario);
            consulta.setString(2, password);
            consulta.setString(3, perfil);
            consulta.setString(4, estado);
            consulta.setString(5, email);
            consulta.executeUpdate();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar un usuario: " + ex.getMessage());
        }
    }
    /** 
     * Método que muestra la ventana para actualizar/editar los datos de un usuario ya existente
     * @param id
     * @param usuarioActual
     * @param contraseñaActual
     * @param perfilActual
     * @param estadoActual
     * @param emailActual
     */
    private void editarUsuario(int id, String usuarioActual, String contraseñaActual, String perfilActual, String estadoActual, String emailActual) {
        //creamos los campos y los rellenamos con los datos que ya tenemos de antes
        JTextField campoUsuario = new JTextField(usuarioActual);
         JTextField campoPassword = new JTextField(contraseñaActual);
        JTextField campoEmail = new JTextField(emailActual);
        
        JComboBox<String> campoPerfil = new JComboBox<>(new String[]{"admin", "trabajador", "responsable", "socio"});
        campoPerfil.setSelectedItem(perfilActual);
        
        JComboBox<String> campoEstado = new JComboBox<>(new String[]{"activo", "inactivo", "suspendido"});
        campoEstado.setSelectedItem(estadoActual);

        Object[] mensaje = {
            "ID Usuario: " + id + " (NO se pude moficar)",
            "Nuevo Usuario:", campoUsuario,
            "Nueva Contraseña (Campo obligatorio):", campoPassword,
            "Nuevo Email:", campoEmail,
            "Nuevo Perfil:", campoPerfil,
            "Nuevo Estado:", campoEstado
        };

        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Modificar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            if (campoUsuario.getText().isEmpty() || campoPassword.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de usuario y la contraseña no pueden estar vacias.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //encriptamos la contraseña antes de guardarla y le pasamos los datos al metodo actualizarDatosUsuario
            String passwordHash = BCrypt.hashpw(campoPassword.getText(), BCrypt.gensalt(12));
            actualizarDatosUsuario(id, campoUsuario.getText(), passwordHash, (String) campoPerfil.getSelectedItem(), (String) campoEstado.getSelectedItem(), campoEmail.getText());
            cargarDatos();//refrescamos para ver los cambios
        }
    }

    /**
     * Método el cual actualiza los datos de un usuario ya existente en la base de datos 
     * @param id
     * @param usuario
     * @param password
     * @param perfil
     * @param estado
     * @param email
     */
    private void actualizarDatosUsuario(int id, String usuario, String password, String perfil, String estado, String email) {
        try (Connection conexion = DriverManager.getConnection(url, user, contra);
             PreparedStatement consulta = conexion.prepareStatement(
                     "UPDATE usuarios SET usuario = ?, password = ?, perfil = ?, estado = ?, email = ? WHERE id_usuario = ?")) {
            consulta.setString(1, usuario);
            consulta.setString(2, password);
            consulta.setString(3, perfil);
            consulta.setString(4, estado);
            consulta.setString(5, email);
            consulta.setInt(6, id);
            
            consulta.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos del usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }    
}