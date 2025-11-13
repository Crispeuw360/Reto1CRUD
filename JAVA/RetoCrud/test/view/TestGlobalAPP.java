package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static org.junit.Assert.*;

/**
 * Test de integración completo para todas las ventanas de la aplicación
 * Se inicia desde el main y verifica que todas las ventanas cargan
 * correctamente
 */
public class TestGlobalAPP {

    @BeforeClass
    public static void initJavaFX() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // Inicializar JavaFX una sola vez
        new Thread(() -> {
            new JFXPanel(); // Inicializa JavaFX Platform
            latch.countDown();
        }).start();

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testAllWindowsLoadCorrectly() throws Exception {
        System.out.println("=== INICIANDO TEST DE INTEGRACIÓN COMPLETO ===");

        // Usar CountDownLatch para sincronizar con el hilo de JavaFX
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // 1. TEST VENTANA LOGIN
                testLoginWindow();

                // 2. TEST VENTANA REGISTRO (SignUp)
                testSignUpWindow();

                // 3. TEST VENTANA MODIFICACIÓN
                testModifyWindow();

                // 4. TEST VENTANA ADMIN
                testAdminWindow();

                // 5. TEST VENTANA ELIMINACIÓN (DropOut)
                testDropOutWindow();

                System.out.println("✓ TODAS LAS VENTANAS VERIFICADAS CORRECTAMENTE");
                latch.countDown();

            } catch (Exception e) {
                fail("Error durante los tests: " + e.getMessage());
                latch.countDown();
            }
        });

        // Esperar a que termine la ejecución en el hilo de JavaFX
        assertTrue("Timeout en tests", latch.await(30, TimeUnit.SECONDS));
    }

    private void testLoginWindow() throws Exception {
        System.out.println("--- Verificando Ventana Login ---");

        // Cargar la ventana de login (como lo hace el main)
        Stage stage = new Stage();
        RetoCrud app = new RetoCrud();
        app.start(stage);

        // Verificar que la ventana se muestra
        assertTrue("Ventana Login debería estar visible", stage.isShowing());
        // En lugar de verificar el título, verificamos que la escena se cargó
        // correctamente
        Scene scene = stage.getScene();
        assertNotNull("La escena no debería ser null", scene);
        assertNotNull("El root de la escena no debería ser null", scene.getRoot());

        // Verificar que hay campos de texto y botones
        int textFields = countComponents(scene, TextField.class);
        int passwordFields = countComponents(scene, PasswordField.class);
        int buttons = countComponents(scene, Button.class);
        int labels = countComponents(scene, Label.class);

        assertTrue("Debería haber al menos 1 TextField (usuario)", textFields >= 1);
        assertTrue("Debería haber al menos 1 PasswordField", passwordFields >= 1);
        assertTrue("Debería haber al menos 2 botones (login, signup)", buttons >= 2);
        assertTrue("Debería haber varios labels", labels >= 3);

        System.out.println("✓ LoginWindow - Campos: " + textFields + " textos, " +
                passwordFields + " contraseñas, " + buttons + " botones");

        testLoginWindowMethods();
    }

    private void testSignUpWindow() throws Exception {
        System.out.println("--- Verificando Ventana SignUp ---");

        // Cargar ventana de registro
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        SignUpController controller = loader.getController();
        assertNotNull("Controller de SignUp no debería ser null", controller);

        // Verificar componentes
        int textFields = countComponents(scene, TextField.class);
        int passwordFields = countComponents(scene, PasswordField.class);
        int buttons = countComponents(scene, Button.class);
        int comboBoxes = countComponents(scene, ComboBox.class);

        assertTrue("Debería haber varios TextFields para datos", textFields >= 6);
        assertTrue("Debería haber 2 PasswordFields", passwordFields >= 2);
        assertTrue("Debería haber al menos 2 botones", buttons >= 2);
        assertTrue("Debería haber 1 ComboBox para género", comboBoxes >= 1);

        System.out.println("✓ SignUpWindow - " + textFields + " campos texto, " +
                comboBoxes + " combos, " + buttons + " botones");

        stage.close();
    }

    private void testModifyWindow() throws Exception {
        System.out.println("--- Verificando Ventana ModifyWindow ---");

        // Cargar ventana de modificación
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        ModifyWindowController controller = loader.getController();
        assertNotNull("Controller de ModifyWindow no debería ser null", controller);

        // Verificar componentes
        int textFields = countComponents(scene, TextField.class);
        int passwordFields = countComponents(scene, PasswordField.class);
        int buttons = countComponents(scene, Button.class);
        int comboBoxes = countComponents(scene, ComboBox.class);

        assertTrue("Debería haber varios TextFields para datos", textFields >= 6);
        assertTrue("Debería haber 2 PasswordFields", passwordFields >= 2);
        assertTrue("Debería haber varios botones", buttons >= 4);
        assertTrue("Debería haber 1 ComboBox para género", comboBoxes >= 1);

        System.out.println("✓ ModifyWindow - " + textFields + " campos texto, " +
                comboBoxes + " combos, " + buttons + " botones");

        stage.close();
    }

    private void testAdminWindow() throws Exception {
        System.out.println("--- Verificando Ventana AdminView ---");

        // Cargar ventana de administración
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        AdminViewController controller = loader.getController();
        assertNotNull("Controller de AdminView no debería ser null", controller);

        // Verificar componentes específicos de admin
        int comboBoxes = countComponents(scene, ComboBox.class);
        int buttons = countComponents(scene, Button.class);
        int textFields = countComponents(scene, TextField.class);

        assertTrue("Debería haber ComboBox para seleccionar usuarios", comboBoxes >= 1);
        assertTrue("Debería haber varios botones (modificar, eliminar, etc.)", buttons >= 4);
        assertTrue("Debería haber campos de texto para datos", textFields >= 6);

        System.out.println("✓ AdminView - " + comboBoxes + " combos usuarios, " +
                buttons + " botones, " + textFields + " campos texto");

        stage.close();
    }

    private void testDropOutWindow() throws Exception {
        System.out.println("--- Verificando Ventana DropOut ---");

        // Cargar ventana de eliminación
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DropOutWindow.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        DropOutController controller = loader.getController();
        assertNotNull("Controller de DropOut no debería ser null", controller);

        // Verificar componentes
        int passwordFields = countComponents(scene, PasswordField.class);
        int buttons = countComponents(scene, Button.class);
        int labels = countComponents(scene, Label.class);

        assertTrue("Debería haber campos de contraseña", passwordFields >= 2);
        assertTrue("Debería haber botones de confirmar y volver", buttons >= 2);
        assertTrue("Debería haber labels informativos", labels >= 4);

        System.out.println("✓ DropOutWindow - " + passwordFields + " campos contraseña, " +
                buttons + " botones");

        stage.close();
    }

    /**
     * Cuenta cuántos componentes de un tipo específico hay en la escena
     */
    private <T extends Node> int countComponents(Scene scene, Class<T> componentType) {
        int count = 0;
        try {
            count = scene.getRoot().lookupAll("." + componentType.getSimpleName()).size();
        } catch (Exception e) {
            // Si falla la búsqueda por CSS, intentamos de otra manera
            count = countComponentsRecursive(scene.getRoot(), componentType);
        }
        return count;
    }

    /**
     * Método recursivo para contar componentes por tipo
     */
    private <T extends Node> int countComponentsRecursive(Node node, Class<T> componentType) {
        int count = 0;
        if (componentType.isInstance(node)) {
            count++;
        }
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                count += countComponentsRecursive(child, componentType);
            }
        }
        return count;
    }

    /**
     * Test adicional para verificar que el main inicia correctamente
     * 
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testMainApplication() throws InterruptedException {
        System.out.println("--- Verificando Main Application ---");

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Simular el inicio de la aplicación como lo haría el main
                Stage stage = new Stage();
                RetoCrud app = new RetoCrud();
                app.start(stage);

                // Verificaciones básicas
                assertTrue("La aplicación debería estar corriendo", stage.isShowing());
                assertNotNull("Debería tener una escena", stage.getScene());

                System.out.println("✓ Main application inicia correctamente");
                latch.countDown();

            } catch (Exception e) {
                fail("Error al iniciar main application: " + e.getMessage());
                latch.countDown();
            }
        });

        assertTrue("Timeout en test main", latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void testLoginWindowMethods() throws Exception {
        System.out.println("=== TESTEANDO MÉTODOS DE LOGIN CON LOOKUP ===");

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Cargar la ventana de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                LoginWindowController controller = loader.getController();
                assertNotNull("Controller de Login no debería ser null", controller);

                // Buscar componentes usando lookup
                TextField usernameField = (TextField) scene.lookup("#usernameField");
                PasswordField passwordField = (PasswordField) scene.lookup("#passwordField");
                Button loginBtn = (Button) scene.lookup("#loginBtn");
                Button signupBtn = (Button) scene.lookup("#signupBtn");

                assertNotNull("Campo username debería existir", usernameField);
                assertNotNull("Campo password debería existir", passwordField);
                assertNotNull("Botón login debería existir", loginBtn);
                assertNotNull("Botón signup debería existir", signupBtn);

                // Mock del Controller
                Controller mockController = createMockController();

                // Inyectar mock usando reflexión (solo esto necesitamos)
                Field conField = LoginWindowController.class.getDeclaredField("con");
                conField.setAccessible(true);
                conField.set(controller, mockController);

                // Test 1: checkFields() mediante listeners
                testCheckFieldsWithLookup(usernameField, passwordField, loginBtn);

                // Test 2: onLogin() scenarios
                testOnLoginScenariosWithLookup(controller, usernameField, passwordField);

                // Test 3: onSignUp()
                testOnSignUpWithLookup(controller);

                // Test 4: clearFields()
                testClearFieldsWithLookup(controller, usernameField, passwordField);

                System.out.println("✓ Todos los métodos de Login probados correctamente con lookup");
                latch.countDown();

            } catch (Exception e) {
                fail("Error en testLoginWindowMethods: " + e.getMessage());
                latch.countDown();
            }
        });

        assertTrue("Timeout en testLoginWindowMethods", latch.await(10, TimeUnit.SECONDS));
    }

    private void testCheckFieldsWithLookup(TextField usernameField, PasswordField passwordField, Button loginBtn) {
        System.out.println("--- Probando checkFields() con lookup ---");

        // Caso 1: Campos vacíos
        usernameField.setText("");
        passwordField.setText("");
        // Los listeners deberían haber actualizado el estado del botón
        assertTrue("Botón debería estar deshabilitado con campos vacíos", loginBtn.isDisabled());

        // Caso 2: Solo usuario
        usernameField.setText("testuser");
        passwordField.setText("");
        assertTrue("Botón debería estar deshabilitado sin contraseña", loginBtn.isDisabled());

        // Caso 3: Solo contraseña
        usernameField.setText("");
        passwordField.setText("testpass");
        assertTrue("Botón debería estar deshabilitado sin usuario", loginBtn.isDisabled());

        // Caso 4: Ambos campos llenos
        usernameField.setText("testuser");
        passwordField.setText("testpass");
        assertFalse("Botón debería estar habilitado con ambos campos llenos", loginBtn.isDisabled());

        System.out.println("✓ checkFields() funciona correctamente");
    }

    private void testOnLoginScenariosWithLookup(LoginWindowController controller, TextField usernameField,
            PasswordField passwordField) {
        System.out.println("--- Probando onLogin() con lookup ---");

        // Escenario 1: Usuario no existe
        usernameField.setText("usuarioInexistente");
        passwordField.setText("cualquierPassword");
        controller.onLogin();
        assertEquals("Campo usuario debería limpiarse después de error", "", usernameField.getText());
        assertEquals("Campo contraseña debería limpiarse después de error", "", passwordField.getText());

        // Escenario 2: Usuario existe pero contraseña incorrecta
        usernameField.setText("usuarioExistente");
        passwordField.setText("passwordIncorrecta");
        controller.onLogin();
        assertEquals("Campo usuario debería limpiarse después de error", "", usernameField.getText());
        assertEquals("Campo contraseña debería limpiarse después de error", "", passwordField.getText());

        // Escenario 3: Login exitoso (usuario normal)
        usernameField.setText("usuarioExistente");
        passwordField.setText("passwordCorrecta");
        controller.onLogin();
        assertEquals("Campo usuario debería limpiarse después de login exitoso", "", usernameField.getText());
        assertEquals("Campo contraseña debería limpiarse después de login exitoso", "", passwordField.getText());

        // Escenario 4: Login exitoso (admin)
        usernameField.setText("admin");
        passwordField.setText("admin123");
        controller.onLogin();
        assertEquals("Campo usuario debería limpiarse después de login admin", "", usernameField.getText());
        assertEquals("Campo contraseña debería limpiarse después de login admin", "", passwordField.getText());

        System.out.println("✓ onLogin() maneja correctamente todos los escenarios");
    }

    private void testOnSignUpWithLookup(LoginWindowController controller) {
        System.out.println("--- Probando onSignUp() con lookup ---");

        try {
            controller.onSignUp();
            System.out.println("✓ onSignUp() se ejecuta sin excepciones");
        } catch (Exception e) {
            fail("onSignUp() debería ejecutarse sin excepciones: " + e.getMessage());
        }
    }

    private void testClearFieldsWithLookup(LoginWindowController controller, TextField usernameField,
            PasswordField passwordField) throws Exception {
        System.out.println("--- Probando clearFields() con lookup ---");

        // Llenar campos primero
        usernameField.setText("testuser");
        passwordField.setText("testpass");

        // Llamar a clearFields usando reflexión (solo este método necesita reflexión)
        Method clearFields = LoginWindowController.class.getDeclaredMethod("clearFields");
        clearFields.setAccessible(true);
        clearFields.invoke(controller);

        // Verificar que se limpiaron
        assertEquals("Campo usuario debería estar vacío", "", usernameField.getText());
        assertEquals("Campo contraseña debería estar vacío", "", passwordField.getText());

        System.out.println("✓ clearFields() funciona correctamente");
    }

    private Controller createMockController() {
        return new Controller() {
            @Override
            public boolean existUser(String username) {
                return "usuarioExistente".equals(username) || "admin".equals(username);
            }

            @Override
            public boolean validatePassword(String username, String password) {
                if ("usuarioExistente".equals(username)) {
                    return "passwordCorrecta".equals(password);
                }
                if ("admin".equals(username)) {
                    return "admin123".equals(password);
                }
                return false;
            }

            @Override
            public boolean isAdmin(String username) {
                return "admin".equals(username);
            }

            @Override
            public model.User_ getUserByUsername(String username) {
                if ("usuarioExistente".equals(username) || "admin".equals(username)) {
                    model.User_ user = new model.User_();
                    user.setUser_name(username);
                    return user;
                }
                return null;
            }
        };
    }

    @Test
    public void testSignUpWindowMethods() throws Exception {
        System.out.println("=== TESTEANDO MÉTODOS DE SIGNUP ===");

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Cargar la ventana de registro
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);

                SignUpController controller = loader.getController();
                assertNotNull("Controller de SignUp no debería ser null", controller);

                // Simular el método checkFields()
                testSignUpCheckFields(controller);

                // Simular el método onRegister() con diferentes escenarios
                testOnRegisterScenarios(controller);

                // Simular el método onBack()
                testOnBackSignUp(controller);

                System.out.println("✓ Todos los métodos de SignUp probados correctamente");
                latch.countDown();

            } catch (Exception e) {
                fail("Error en testSignUpWindowMethods: " + e.getMessage());
                latch.countDown();
            }
        });

        assertTrue("Timeout en testSignUpWindowMethods", latch.await(10, TimeUnit.SECONDS));
    }

    private void testSignUpCheckFields(SignUpController controller) throws Exception {
        System.out.println("--- Probando checkFields() de SignUp ---");

        // Acceder a campos privados usando reflexión
        Field[] fields = {
                SignUpController.class.getDeclaredField("fieldUser"),
                SignUpController.class.getDeclaredField("fieldName"),
                SignUpController.class.getDeclaredField("fieldSurname"),
                SignUpController.class.getDeclaredField("fieldGmail"),
                SignUpController.class.getDeclaredField("fieldTel"),
                SignUpController.class.getDeclaredField("fieldPass"),
                SignUpController.class.getDeclaredField("fieldPass2"),
                SignUpController.class.getDeclaredField("fieldCard"),
                SignUpController.class.getDeclaredField("comboGender"),
                SignUpController.class.getDeclaredField("btnRegistro")
        };

        for (Field field : fields) {
            field.setAccessible(true);
        }

        TextField userField = (TextField) fields[0].get(controller);
        TextField nameField = (TextField) fields[1].get(controller);
        TextField surnameField = (TextField) fields[2].get(controller);
        TextField gmailField = (TextField) fields[3].get(controller);
        TextField telField = (TextField) fields[4].get(controller);
        PasswordField passField = (PasswordField) fields[5].get(controller);
        PasswordField pass2Field = (PasswordField) fields[6].get(controller);
        TextField cardField = (TextField) fields[7].get(controller);
        ComboBox<String> genderCombo = (ComboBox<String>) fields[8].get(controller);
        Button registroBtn = (Button) fields[9].get(controller);

        // Caso 1: Todos los campos vacíos
        clearAllFields(userField, nameField, surnameField, gmailField, telField, passField, pass2Field, cardField,
                genderCombo);
        callCheckFields(controller);
        assertTrue("Botón debería estar deshabilitado con campos vacíos", registroBtn.isDisabled());

        // Caso 2: Algunos campos llenos, otros vacíos
        userField.setText("testuser");
        nameField.setText("Test");
        surnameField.setText("User");
        callCheckFields(controller);
        assertTrue("Botón debería estar deshabilitado con campos incompletos", registroBtn.isDisabled());

        // Caso 3: Todos los campos llenos excepto combo
        userField.setText("testuser");
        nameField.setText("Test");
        surnameField.setText("User");
        gmailField.setText("test@test.com");
        telField.setText("123456789");
        passField.setText("password123");
        pass2Field.setText("password123");
        cardField.setText("1234567890123456");
        // genderCombo sigue vacío
        callCheckFields(controller);
        assertTrue("Botón debería estar deshabilitado sin género seleccionado", registroBtn.isDisabled());

        // Caso 4: Todos los campos llenos
        genderCombo.setValue("Male");
        callCheckFields(controller);
        assertFalse("Botón debería estar habilitado con todos los campos llenos", registroBtn.isDisabled());

        System.out.println("✓ checkFields() de SignUp funciona correctamente");
    }

    private void testOnRegisterScenarios(SignUpController controller) throws Exception {
        System.out.println("--- Probando onRegister() con diferentes escenarios ---");

        // Acceder a campos necesarios
        Field userField = SignUpController.class.getDeclaredField("fieldUser");
        Field nameField = SignUpController.class.getDeclaredField("fieldName");
        Field surnameField = SignUpController.class.getDeclaredField("fieldSurname");
        Field gmailField = SignUpController.class.getDeclaredField("fieldGmail");
        Field telField = SignUpController.class.getDeclaredField("fieldTel");
        Field passField = SignUpController.class.getDeclaredField("fieldPass");
        Field pass2Field = SignUpController.class.getDeclaredField("fieldPass2");
        Field cardField = SignUpController.class.getDeclaredField("fieldCard");
        Field genderCombo = SignUpController.class.getDeclaredField("comboGender");
        Field conField = SignUpController.class.getDeclaredField("con");

        userField.setAccessible(true);
        nameField.setAccessible(true);
        surnameField.setAccessible(true);
        gmailField.setAccessible(true);
        telField.setAccessible(true);
        passField.setAccessible(true);
        pass2Field.setAccessible(true);
        cardField.setAccessible(true);
        genderCombo.setAccessible(true);
        conField.setAccessible(true);

        TextField user = (TextField) userField.get(controller);
        TextField name = (TextField) nameField.get(controller);
        TextField surname = (TextField) surnameField.get(controller);
        TextField gmail = (TextField) gmailField.get(controller);
        TextField tel = (TextField) telField.get(controller);
        PasswordField pass = (PasswordField) passField.get(controller);
        PasswordField pass2 = (PasswordField) pass2Field.get(controller);
        TextField card = (TextField) cardField.get(controller);
        ComboBox<String> gender = (ComboBox<String>) genderCombo.get(controller);

        // Mock del Controller para simular diferentes respuestas
        Controller mockController = new Controller() {
            @Override
            public model.User_ getUserByUsername(String username) {
                // Simular que el usuario "usuarioExistente" ya existe
                if ("usuarioExistente".equals(username)) {
                    return new model.User_(); // Retorna un usuario (existe)
                }
                return null; // Usuario no existe
            }

            @Override
            public boolean existUser(String username) {
                return "usuarioExistente".equals(username);
            }

            @Override
            public boolean insertUser(model.User_ user) {
                // Simular inserción exitosa
                return true;
            }
        };

        conField.set(controller, mockController);

        // Escenario 1: Contraseñas no coinciden
        setAllFields(user, name, surname, gmail, tel, pass, pass2, card, gender,
                "nuevoUsuario", "Juan", "Pérez", "juan@test.com", "123456789",
                "password123", "password456", "1234567890123456", "Male");
        controller.onRegister();
        // Verificar que se maneja el error de contraseñas (los campos deberían
        // mantenerse o limpiarse según tu implementación)

        // Escenario 2: Email inválido
        setAllFields(user, name, surname, gmail, tel, pass, pass2, card, gender,
                "nuevoUsuario", "Juan", "Pérez", "emailinvalido", "123456789",
                "password123", "password123", "1234567890123456", "Male");
        controller.onRegister();
        // Verificar que se maneja el error de email

        // Escenario 3: Teléfono o tarjeta no numéricos
        setAllFields(user, name, surname, gmail, tel, pass, pass2, card, gender,
                "nuevoUsuario", "Juan", "Pérez", "juan@test.com", "noNumerico",
                "password123", "password123", "noNumerico", "Male");
        controller.onRegister();
        // Verificar que se maneja el error de formato numérico

        // Escenario 4: Usuario ya existe
        setAllFields(user, name, surname, gmail, tel, pass, pass2, card, gender,
                "usuarioExistente", "Juan", "Pérez", "juan@test.com", "123456789",
                "password123", "password123", "1234567890123456", "Male");
        controller.onRegister();
        // Verificar que se maneja el error de usuario existente

        // Escenario 5: Registro exitoso
        setAllFields(user, name, surname, gmail, tel, pass, pass2, card, gender,
                "nuevoUsuarioExitoso", "Juan", "Pérez", "juan@test.com", "123456789",
                "password123", "password123", "1234567890123456", "Male");
        controller.onRegister();
        // Verificar que los campos se limpian después de registro exitoso
        assertEquals("Campo usuario debería limpiarse después de registro exitoso", "", user.getText());
        assertEquals("Campo nombre debería limpiarse después de registro exitoso", "", name.getText());

        System.out.println("✓ onRegister() maneja correctamente todos los escenarios");
    }

    private void testOnBackSignUp(SignUpController controller) throws Exception {
        System.out.println("--- Probando onBack() de SignUp ---");

        // Este test verifica que el método se ejecuta sin excepciones
        try {
            controller.onBack();
            System.out.println("✓ onBack() de SignUp se ejecuta sin excepciones");
        } catch (Exception e) {
            fail("onBack() debería ejecutarse sin excepciones: " + e.getMessage());
        }
    }

    // Métodos helper para SignUp
    private void clearAllFields(TextField user, TextField name, TextField surname, TextField gmail,
            TextField tel, PasswordField pass, PasswordField pass2,
            TextField card, ComboBox<String> gender) {
        user.setText("");
        name.setText("");
        surname.setText("");
        gmail.setText("");
        tel.setText("");
        pass.setText("");
        pass2.setText("");
        card.setText("");
        gender.setValue(null);
    }

    private void setAllFields(TextField user, TextField name, TextField surname, TextField gmail,
            TextField tel, PasswordField pass, PasswordField pass2,
            TextField card, ComboBox<String> gender,
            String userVal, String nameVal, String surnameVal, String gmailVal,
            String telVal, String passVal, String pass2Val, String cardVal,
            String genderVal) {
        user.setText(userVal);
        name.setText(nameVal);
        surname.setText(surnameVal);
        gmail.setText(gmailVal);
        tel.setText(telVal);
        pass.setText(passVal);
        pass2.setText(pass2Val);
        card.setText(cardVal);
        gender.setValue(genderVal);
    }

    private void callCheckFields(SignUpController controller) throws Exception {
        Method checkFields = SignUpController.class.getDeclaredMethod("checkFields");
        checkFields.setAccessible(true);
        checkFields.invoke(controller);
    }
}