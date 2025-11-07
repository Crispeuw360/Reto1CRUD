package model;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Hilo para manejar la creación de usuarios en segundo plano
 */
public class HiloCrear implements Runnable {
    private static final int HOLD_TIME_MS = 3000; // 3 segundos para mostrar el mensaje de éxito
    
    private final ImplementsBD userDAO;
    private final User_ usuario;
    private final Stage stage;

    public HiloCrear(ImplementsBD userDAO, User_ usuario, Stage stage) {
        this.userDAO = userDAO;
        this.usuario = usuario;
        this.stage = stage;
    }

    @Override
    public void run() {
        try {
            System.out.println("🧵 Hilo " + Thread.currentThread().getName() + " ejecutando creación asíncrona...");
            
            // Verificar primero si el usuario ya existe
            boolean usuarioExiste = userDAO.existUser(usuario.getUser_name());
            
            if (usuarioExiste) {
                Platform.runLater(() -> {
                    showErrorMessage("Usuario existente", 
                        "El nombre de usuario ya existe. Por favor, elige otro.");
                });
                return;
            }

            // Intentar insertar el usuario
            boolean exito = userDAO.insertUser(usuario);
            
            // Actualizar la interfaz en el hilo de JavaFX
            Platform.runLater(() -> {
                if (exito) {
                    showSuccessMessage();
                    // Volver al login después de un tiempo
                    new Thread(() -> {
                        try {
                            Thread.sleep(HOLD_TIME_MS);
                            Platform.runLater(() -> {
                                volverALogin();
                            });
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                } else {
                    showErrorMessage("Error al crear usuario", 
                        "No se pudo crear el usuario. Inténtalo de nuevo.");
                }
            });
            
        } catch (Exception e) {
            System.out.println("⚠️ Error en hilo de creación: " + e.getMessage());
            Platform.runLater(() -> {
                showErrorMessage("Error inesperado", 
                    "Ocurrió un error al procesar la solicitud: " + e.getMessage());
            });
        }
    }
    
    /**
     * Método para volver a la ventana de login
     */
    private void volverALogin() {
        try {
            // Cargar el FXML de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginWindow.fxml"));
            Parent root = loader.load();
            
            // Crear nueva escena
            Scene scene = new Scene(root);
            
            // Configurar el stage actual con la nueva escena
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            System.out.println("❌ Error al cargar la ventana de login: " + e.getMessage());
            // Fallback: mostrar error si no se puede cargar el login
            showErrorMessage("Error", "No se pudo cargar la ventana de login: " + e.getMessage());
        }
    }
    
    /**
     * Muestra un mensaje de éxito cuando se crea el usuario
     */
    private void showSuccessMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Operación exitosa");
        alert.setHeaderText("✅ Usuario creado exitosamente");
        alert.setContentText("Usuario: " + usuario.getUser_name() + "\n" +
                          "Correo: " + usuario.getEmail() + "\n\n" +
                          "Creado correctamente en la base de datos.\n" +
                          "Serás redirigido al login en 3 segundos.\n\n" +
                          "💡 Ahora puedes iniciar sesión con tus credenciales.");
        alert.showAndWait();
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}