package model;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import view.LoginWindowController;
import view.ModifyWindowController;

import java.io.IOException;

/**
 * Hilo para manejar el login de usuarios en segundo plano
 */
public class HiloLogin implements Runnable {

    private final ImplementsBD userDAO;
    private final String username;
    private final String password;
    private final Stage stage;
    private final LoginWindowController loginController; // Referencia al controlador

    public HiloLogin(ImplementsBD userDAO, String username, String password, Stage stage,
            LoginWindowController loginController) {
        this.userDAO = userDAO;
        this.username = username;
        this.password = password;
        this.stage = stage;
        this.loginController = loginController;
    }

    @Override
    public void run() {
        try {
            System.out.println("🧵 Hilo " + Thread.currentThread().getName() + " ejecutando login...");
            
            // Verificar si el usuario existe
            boolean existe = userDAO.existUser(username);
            
            if (!existe) {
                Platform.runLater(() -> {
                    showErrorMessage("Usuario no encontrado", 
                        "El nombre de usuario no existe. Por favor, regístrate primero.");
                });
                return;
            }

            // Validar contraseña
            boolean valido = userDAO.validatePassword(username, password);
            
            Platform.runLater(() -> {
                if (valido) {
                    showSuccessMessage();
                    System.out.println("Usuario encontrado: " + username);
                    
                    // Verificar si es administrador
                    boolean isAdmin = userDAO.isAdmin(username);
                    
                    if (isAdmin) {
                        abrirVentanaAdministracion();
                    } else {
                        User_ user = userDAO.getUserByUsername(username);
                        if (user != null) {
                            abrirVentanaModificacion(user);
                        } else {
                            showErrorMessage("Error", "No se pudieron obtener los datos del usuario.");
                        }
                    }
                } else {
                    showErrorMessage("Contraseña incorrecta", 
                        "La contraseña ingresada es incorrecta. Inténtalo de nuevo.");
                }
            });
            
        } catch (Exception e) {
            System.out.println("⚠️ Error en hilo de login: " + e.getMessage());
            Platform.runLater(() -> {
                showErrorMessage("Error inesperado", 
                    "Ocurrió un error al procesar el login: " + e.getMessage());
            });
        }
    }

    /**
     * Abre la ventana de administración
     */
    private void abrirVentanaAdministracion() {
        try {
            // 🔸 Cargar el FXML de la ventana de administración
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
            Parent root = loader.load();

            // Crear nueva escena
            Scene scene = new Scene(root);

            // Configurar el stage con la nueva escena
            stage.setScene(scene);
            stage.setTitle("Panel de Administración");
            stage.setResizable(true); // Puede ser true para admin
            stage.show();

            System.out.println("✅ Ventana de administración cargada para: " + username);

        } catch (IOException e) {
            System.out.println("❌ Error al cargar ventana de administración: " + e.getMessage());
            showErrorMessage("Error", "No se pudo abrir el panel de administración: " + e.getMessage());
            if (loginController != null) {
                loginController.enableButtons();
            }
        }
    }

    /**
     * Abre la ventana de modificación después del login exitoso
     */
    private void abrirVentanaModificacion(User_ user) {
        try {
            // Cargar el FXML de la ventana de modificación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyWindow.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el usuario
            ModifyWindowController modifyController = loader.getController();
            modifyController.setUser(user);

            // Crear nueva escena
            Scene scene = new Scene(root);

            // Configurar el stage con la nueva escena
            stage.setScene(scene);
            stage.setTitle("Modificar Usuario");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.out.println("❌ Error al cargar ventana de modificación: " + e.getMessage());
            showErrorMessage("Error", "No se pudo abrir la ventana de modificación: " + e.getMessage());
            enableLoginButtons();
        }
    }

    /**
     * Re-habilita los botones de login en caso de error
     */
    private void enableLoginButtons() {
        Platform.runLater(() -> {
            try {
                // Si el stage todavía muestra la ventana de login, re-habilitar
                if (stage.getTitle().contains("Login") || stage.getTitle().contains("LogIn")) {
                    // Buscar el controlador de login
                    Scene currentScene = stage.getScene();
                    if (currentScene != null && currentScene.getRoot() != null) {
                        currentScene.getRoot().setDisable(false);

                        // También podemos intentar encontrar los botones específicos
                        javafx.scene.Node loginBtn = currentScene.lookup("#loginBtn");
                        javafx.scene.Node signupBtn = currentScene.lookup("#signupBtn");

                        if (loginBtn != null)
                            loginBtn.setDisable(false);
                        if (signupBtn != null)
                            signupBtn.setDisable(false);
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo re-habilitar la interfaz: " + e.getMessage());
            }
        });
    }

    /**
     * Muestra un mensaje de éxito cuando el login es correcto
     */
    private void showSuccessMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Login exitoso");
        alert.setHeaderText("✅ ¡Bienvenido " + username + "!");
        alert.setContentText("Has iniciado sesión correctamente.\n\n" +
                "Serás redirigido a tu perfil...");
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de error
     */
    private void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error de login");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}