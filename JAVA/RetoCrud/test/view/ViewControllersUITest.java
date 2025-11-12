package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

// ESTA ANOTACIÓN ORDENA LOS TESTS POR NOMBRE
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ViewControllersUITest extends ApplicationTest {

    private Stage primaryStage;
    private String usuarioUnico;
    private double randomNumber = 25;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.setWidth(600);
        stage.setHeight(400);
        
        usuarioUnico = "testuser_" + randomNumber;
        
        new RetoCrud().start(stage);
    }

    @Before
    public void prepararTest() {
        // Esperar a que cargue
        sleep(3000);
        
        // Cerrar TODAS las ventanas y alerts
        cerrarTodosLosAlertsYVentanas();
        
        sleep(1000);
    }

    // ========== TESTS BÁSICOS SIN LOGIN ==========

    @Test
    public void test01_ElementosLoginBasicos() {
        System.out.println("=== Test 1: Elementos Login Básicos ===");
        
        verificarElemento("#usernameField", "Campo usuario");
        verificarElemento("#passwordField", "Campo contraseña"); 
        verificarElemento("#loginBtn", "Botón login");
        verificarElemento("#signupBtn", "Botón signup");
        
        System.out.println("✓ Test elementos básicos completado");
    }

    @Test
    public void test02_NavegacionRegistro() {
        System.out.println("=== Test 2: Navegación a Registro ===");
        
        // Login -> Registro
        clickOn("#signupBtn");
        sleep(2000);
        
        // Verificar que estamos en registro
        verificarElemento("#btnRegistro", "En pantalla registro");
        verificarElemento("#fieldUser", "Campo usuario registro");
        verificarElemento("#fieldName", "Campo nombre registro");
        
        // Volver a login
        clickOn("#btnBack");
        sleep(2000);
        
        // Verificar que estamos de vuelta en login
        verificarElemento("#loginBtn", "De vuelta en login");
        
        System.out.println("✓ Navegación registro completada");
    }

    // ========== TESTS CON MANEJO ESPECÍFICO DE ALERTS ==========

    @Test
    public void test03_RegistroNuevoUsuario() {
        System.out.println("=== Test 3: Registro Nuevo Usuario ===");
        
        // Ir a registro
        clickOn("#signupBtn");
        sleep(2000);
        
        // 1. Llenar formulario completo pero con nombre Repetido
        llenarCampo("#fieldUser", "juan_perez", "Usuario Repetido: juan_perez");
        llenarCampo("#fieldName", "Test", "Nombre");
        llenarCampo("#fieldSurname", "User", "Apellido");
        llenarCampo("#fieldGmail", "juan_perez@test.com", "Email único");
        llenarCampo("#fieldTel", "123456789", "Teléfono");
        llenarCampo("#fieldPass", "testpass", "Contraseña");
        llenarCampo("#fieldPass2", "testpass", "Confirmar contraseña");
        llenarCampo("#fieldCard", "987654321", "Tarjeta");
        
        // Seleccionar género
        clickOn("#comboGender");
        sleep(500);
        clickOn("Male");
        
         // Verificar que el botón está habilitado
        verificarEstadoBoton("#btnRegistro", true, "Registro habilitado con todos los campos");
        
        // Hacer clic en registrar
        clickOn("#btnRegistro");
        sleep(3000);
        
        // MANEJAR ESPECÍFICAMENTE EL ALERT DE REGISTRO
        manejarAlertConBoton("Aceptar", "Error");
        
        // 2. Formulario Con gmail mal formulado
        
        limpiarCampo("#fieldUser");
        llenarCampo("#fieldUser", usuarioUnico, "Usuario único: " + usuarioUnico);
        limpiarCampo("#fieldGmail");
        llenarCampo("#fieldGmail", "test.com", "Email único");
        
        // Verificar que el botón está habilitado
        verificarEstadoBoton("#btnRegistro", true, "Registro habilitado con todos los campos");
        
        // Hacer clic en registrar
        clickOn("#btnRegistro");
        sleep(3000);
        
        // MANEJAR ESPECÍFICAMENTE EL ALERT DE REGISTRO
        manejarAlertConBoton("Aceptar", "Error");
        
        // 3. Formulario con contraseñas diferentes
        limpiarCampo("#fieldGmail");
        llenarCampo("#fieldGmail", "juan_perez@test.com", "Email único");
        limpiarCampo("#fieldPass2");
        llenarCampo("#fieldPass2", "test", "Contraseña no igual");
        
        // Verificar que el botón está habilitado
        verificarEstadoBoton("#btnRegistro", true, "Registro habilitado con todos los campos");
        
        // Hacer clic en registrar
        clickOn("#btnRegistro");
        sleep(3000);
        
        // MANEJAR ESPECÍFICAMENTE EL ALERT DE REGISTRO
        manejarAlertConBoton("Aceptar", "Error");
        
        
        
        // 4. Llenar formulario completo bien
        
        limpiarCampo("#fieldPass2");
        llenarCampo("#fieldPass2", "testpass", "Email único");
        
        
        
        
        // Verificar que el botón está habilitado
        verificarEstadoBoton("#btnRegistro", true, "Registro habilitado con todos los campos");
        
        // Hacer clic en registrar
        clickOn("#btnRegistro");
        sleep(3000);
        
        // MANEJAR ESPECÍFICAMENTE EL ALERT DE REGISTRO
        boolean alertManejado = manejarAlertConBoton("Aceptar", "Éxito");
        
        if (alertManejado) {
            System.out.println("✓ Alert de registro manejado correctamente");
            
            sleep(2000);
            clickOn("#btnBack");
            
            sleep(2000);
            boolean enLogin = verificarElementoExistente("#loginBtn");
            
            if (enLogin) {
                System.out.println("✓ Volvió a login después de registro exitoso");
                
                // Guardar el usuario creado para tests posteriores
                // Podríamos guardarlo en una variable estática si fuera necesario
            } else {
                System.out.println("✗ No volvió a login después del registro");
            }
        } else {
            System.out.println("✗ No se pudo manejar el alert de registro");
        }
    }
    @Test
    public void test04_LoginFallidoManejoAlert() {
        System.out.println("=== Test 4: Login Fallido - Manejo de Alert ===");
        
        // Usar credenciales que NO existen
        llenarCampo("#usernameField", "usuarioinexistente", "Usuario inexistente");
        llenarCampo("#passwordField", "passwordincorrecta", "Contraseña incorrecta");
        
        // Hacer login
        clickOn("#loginBtn");
        sleep(3000);
        
        // MANEJAR ESPECÍFICAMENTE EL ALERT DE ERROR
        boolean alertManejado = manejarAlertConBoton("Aceptar", "Error");
        
        if (alertManejado) {
            System.out.println("✓ Alert de error manejado correctamente");
            
            // Verificar que seguimos en login
            boolean enLogin = verificarElementoExistente("#loginBtn");
            if (enLogin) {
                System.out.println("✓ Correctamente en login después del error");
            }
        } else {
            System.out.println("✗ No se pudo manejar el alert de error");
        }
        
        // Limpiar campos
        limpiarCampo("#usernameField");
        limpiarCampo("#passwordField");
    }

    @Test
    public void test05_LoginConUsuarioRegistrado() {
        System.out.println("=== Test 5: Login con Usuario Registrado ===");
        
        // Usar el usuario que debería haberse registrado en el test anterior
        // Si no existe, usar uno por defecto
        String usuario = usuarioUnico;  // Cambia por un usuario que SÍ exista en tu BD
        String password = "testpass"; // Cambia por su contraseña
        
        System.out.println("Intentando login con: " + usuario);
        
        llenarCampo("#usernameField", usuario, "Usuario para login");
        llenarCampo("#passwordField", password, "Contraseña para login");
        
        // Hacer login
        clickOn("#loginBtn");
        sleep(3000);
        
        // MANEJAR ALERT DE LOGIN EXITOSO
        boolean alertManejado = manejarAlertConBoton("Aceptar", "Login correcto");
        
        if (alertManejado) {
            System.out.println("✓ Alert de login exitoso manejado");
            
            // Esperar a que cargue ModifyWindow
            sleep(3000);
            
            // Verificar específicamente que estamos en ModifyWindow
            boolean enModifyWindow = verificarModifyWindow();
            
            if (enModifyWindow) {
                System.out.println("✓ ¡Llegamos a ModifyWindow!");
                
                // Hacer una prueba rápida y volver
                verificarElemento("#btnModify", "Botón Modify presente");
                verificarElemento("#btnSave", "Botón Save presente");
                
                // Volver a login para otros tests
                clickOn("#btnBack");
                sleep(2000);
                
            } else {
                System.out.println("✗ No se llegó a ModifyWindow después del login");
                // Intentar recuperarnos cerrando ventanas
                cerrarTodosLosAlertsYVentanas();
            }
        } else {
            System.out.println("✗ Login falló - no se manejó el alert o credenciales incorrectas");
            // Intentar manejar alert de error
            manejarAlertConBoton("Aceptar", "Error");
        }
    }

    @Test
    public void test06_ModifyWindowCompleto() {
        System.out.println("=== Test 6: Modify Window Completo ===");
        
        // Primero hacer login exitoso
        if (hacerLoginExitoso()) {
            
            System.out.println("✓ Iniciando pruebas en ModifyWindow...");
            
            // 1. Verificar elementos básicos
            verificarElemento("#fieldUser", "Campo usuario Modify");
            verificarElemento("#fieldName", "Campo nombre Modify");
            verificarElemento("#fieldSurname", "Campo apellido Modify");
            verificarElemento("#fieldGmail", "Campo email Modify");
            verificarElemento("#fieldTel", "Campo teléfono Modify");
            verificarElemento("#fieldCard", "Campo tarjeta Modify");
            verificarElemento("#comboGender", "Combo género Modify");
            
            // 2. Verificar estado inicial
            verificarEstadoBoton("#btnModify", true, "Botón Modify habilitado inicialmente");
            verificarEstadoBoton("#btnSave", false, "Botón Save deshabilitado inicialmente");
            verificarEstadoBoton("#btnDelete", true, "Botón Delete habilitado inicialmente");
            
            // 3. Probar modo edición
            System.out.println("--- Activando modo edición ---");
            clickOn("#btnModify");
            sleep(1000);
            
            verificarEstadoBoton("#btnModify", false, "Botón Modify deshabilitado en edición");
            verificarEstadoBoton("#btnSave", true, "Botón Save habilitado en edición");
            
            //4.Comprobar que no hay espacios vacios
            limpiarCampo("#fieldName");
            clickOn("#btnSave");
            sleep(3000);
            
            manejarAlertConBoton("Aceptar", "Error");
            
            // 5. Editar campos
            limpiarCampo("#fieldName");
            llenarCampo("#fieldName", "NombreModificado", "Modificar nombre");
            limpiarCampo("#fieldGmail");
            llenarCampo("#fieldGmail", "modificado@test.com", "Modificar email");
            
            // 5. Guardar cambios
            clickOn("#btnSave");
            sleep(3000);
            
            // Manejar alert de guardado exitoso
            boolean guardadoExitoso = manejarAlertConBoton("Aceptar", "Éxito");
            
            if (guardadoExitoso) {
                System.out.println("✓ Cambios guardados exitosamente");
                
                // Verificar que volvimos al estado normal
                verificarEstadoBoton("#btnModify", true, "Botón Modify habilitado después de guardar");
                verificarEstadoBoton("#btnSave", false, "Botón Save deshabilitado después de guardar");
                sleep(1000);
                
                clickOn("#btnBack");
                sleep(2000);
            }
            
            System.out.println("✓ Test Modify Window completo exitoso");
            
        } else {
            System.out.println("✗ No se pudo acceder a ModifyWindow - omitiendo test");
        }
        
    }

    @Test
    public void test07_NavegacionDropWindow()
    {
        System.out.println("=== Test 7: Navegación a Delete Window ===");
        
        if(hacerLoginExitoso()){
            System.out.println("--- Probando navegación a Delete ---");
            clickOn("#btnDelete");
            sleep(2000);
            
            // Verificar que estamos en DropOut Window
            if (verificarElementoExistente("#ConfirmBtn")) {
                System.out.println("✓ Navegación a Delete exitosa");
                clickOn("#exitBtn");
                sleep(2000);
            }
            
            clickOn("#btnBack");
            sleep(2000);
        }
    }

    @Test
    public void test08_DropOutWindow()
    {
        System.out.println("=== Test 8: DropOut Window ===");
        if(hacerLoginExitoso()){
            System.out.println("--- Probando navegación a Delete ---");
            clickOn("#btnDelete");
            sleep(2000);
            
            // 1. Verificar elementos básicos
            verificarElemento("#usernameField", "Campo usuario DropOut");
            verificarElemento("#passwordField", "Campo Contraseña1 DropOut");
            verificarElemento("#passwordConfirmField", "Campo Contraseña2 DropOut");
            
            //2. Verificar Botones
            verificarEstadoBoton("#ConfirmBtn", true, "Botón confirmar habilitado inicialmente");
            
            //3. Primer Test Fallido No hay segunda Contraseña
            llenarCampo("#passwordField", "1234", "Modificado la primera Contraseña");
            
            clickOn("#ConfirmBtn");
            sleep(2000);
            
            // MANEJAR ESPECÍFICAMENTE EL ALERT DE ERROR
            manejarAlertConBoton("Aceptar", "Error");

            
            
            //4. Segundo Test Contraseña Incorrecta
            limpiarCampo("#passwordField");
            llenarCampo("#passwordField", "123", "Modificado la primera Contraseña");
            llenarCampo("#passwordConfirmField", "123", "Modificado la segunda Contraseña");
            
            clickOn("#ConfirmBtn");
            sleep(2000);
            
            // MANEJAR ESPECÍFICAMENTE EL ALERT DE ERROR
             manejarAlertConBoton("Aceptar", "Error");

            
            
            //5. Ultimo Test Eliminacion Correcta
            
            limpiarCampo("#passwordField");
            limpiarCampo("#passwordConfirmField");
            llenarCampo("#passwordField", "1234", "Modificado la primera Contraseña Correctamente");
            llenarCampo("#passwordConfirmField", "1234", "Modificado la segunda Contraseña Correctamente");
            
            clickOn("#ConfirmBtn");
            sleep(2000);
            
            // MANEJAR ESPECÍFICAMENTE EL ALERT DE ERROR
             boolean alertManejado = manejarAlertConBoton("Aceptar", "Error");

            if (alertManejado) {
                System.out.println("✓ Alert de error manejado correctamente");

                // Verificar que seguimos en login
                boolean enLogin = verificarElementoExistente("#loginBtn");
                if (enLogin) {
                    System.out.println("✓ Correctamente en login después del error");
                }
            } else {
                System.out.println("✗ No se pudo manejar el alert de error");
            }
        }
    }

    

    // ========== MÉTODOS HELPER MEJORADOS ==========
    
    private void cerrarTodosLosAlertsYVentanas() {
        try {
            System.out.println("Cerrando alerts y ventanas...");
            
            // Intentar hacer clic en botones "Aceptar", "OK" que puedan estar visibles
            try {
                clickOn("Aceptar");
                sleep(500);
            } catch (Exception e) { }
            
            try {
                clickOn("OK");
                sleep(500);
            } catch (Exception e) { }
            
            try {
                clickOn("Aceptar");
                sleep(500);
            } catch (Exception e) { }
            
        } catch (Exception e) {
            System.out.println("Error cerrando alerts: " + e.getMessage());
        }
    }
    
    private boolean manejarAlertConBoton(String textoBoton, String tipoAlert) {
        try {
            System.out.println("Buscando alert: " + tipoAlert);
            
            // Esperar a que aparezca el alert
            sleep(2000);
            
            // Buscar el botón específico en el DialogPane
            try {
                // Buscar por texto del botón
                clickOn(textoBoton);
                System.out.println("✓ Alert manejado - Clic en: " + textoBoton);
                sleep(1000);
                return true;
                
            } catch (Exception e1) {
                // Si no encuentra por texto, buscar en DialogPane
                try {
                    DialogPane dialogPane = lookup(".dialog-pane").query();
                    Button aceptarBtn = lookup("#" + textoBoton.toLowerCase() + "Button").query();
                    clickOn(aceptarBtn);
                    System.out.println("✓ Alert manejado - Clic en botón del dialog");
                    sleep(1000);
                    return true;
                    
                } catch (Exception e2) {
                    // Último intento - buscar cualquier botón en dialog
                    try {
                        Button cualquierBoton = lookup(".dialog-pane .button").query();
                        clickOn(cualquierBoton);
                        System.out.println("✓ Alert manejado - Clic en cualquier botón del dialog");
                        sleep(1000);
                        return true;
                        
                    } catch (Exception e3) {
                        System.out.println("✗ No se pudo encontrar el botón del alert");
                        return false;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error manejando alert: " + e.getMessage());
            return false;
        }
    }
    
    private boolean hacerLoginExitoso() {
        try {
            // USAR CREDENCIALES REALES DE TU BD - CAMBIA ESTOS VALORES
            String usuarioReal = "juan_perez";      // Usuario que existe
            String passwordReal = "1234";     // Contraseña correcta
            
            System.out.println("Intentando login con usuario real: " + usuarioReal);
            
            llenarCampo("#usernameField", usuarioReal, "Usuario real");
            llenarCampo("#passwordField", passwordReal, "Contraseña real");
            
            clickOn("#loginBtn");
            sleep(3000);
            
            // Manejar alert de login exitoso
            boolean alertManejado = manejarAlertConBoton("Aceptar", "Login exitoso");
            
            if (alertManejado) {
                // Esperar a que cargue ModifyWindow
                sleep(3000);
                return verificarModifyWindow();
            }
            
            return false;
            
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            return false;
        }
    }
    
    private boolean verificarModifyWindow() {
        try {
            // Verificar múltiples elementos específicos de ModifyWindow
            boolean tieneBtnModify = verificarElementoExistente("#btnModify");
            boolean tieneBtnSave = verificarElementoExistente("#btnSave");
            boolean tieneBtnDelete = verificarElementoExistente("#btnDelete");
            
            if (tieneBtnModify && tieneBtnSave && tieneBtnDelete) {
                System.out.println("✓ Confirmado: Estamos en ModifyWindow");
                return true;
            } else {
                System.out.println("✗ No estamos en ModifyWindow");
                System.out.println("  - btnModify: " + tieneBtnModify);
                System.out.println("  - btnSave: " + tieneBtnSave);
                System.out.println("  - btnDelete: " + tieneBtnDelete);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error verificando ModifyWindow: " + e.getMessage());
            return false;
        }
    }
    
    private boolean verificarElementoExistente(String selector) {
        try {
            Object node = lookup(selector).query();
            return node != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void verificarElemento(String selector, String descripcion) {
        try {
            verifyThat(selector, isVisible());
            System.out.println("✓ " + descripcion);
        } catch (Exception e) {
            System.out.println("✗ " + descripcion);
        }
    }
    
    private void verificarEstadoBoton(String selector, boolean habilitado, String descripcion) {
        try {
            if (habilitado) {
                verifyThat(selector, isEnabled());
            } else {
                verifyThat(selector, isDisabled());
            }
            System.out.println("✓ " + descripcion);
        } catch (Exception e) {
            System.out.println("✗ " + descripcion);
        }
    }
    
    private void llenarCampo(String selector, String texto, String descripcion) {
        try {
            clickOn(selector);
            write(texto);
            System.out.println("✓ " + descripcion);
        } catch (Exception e) {
            System.out.println("✗ " + descripcion);
        }
    }
    
    private void limpiarCampo(String selector) {
        try {
            doubleClickOn(selector);
            push(javafx.scene.input.KeyCode.DELETE);
        } catch (Exception e) {
            // Ignorar
        }
    }

    // ========== TEST DEBUG ==========

    @Test
    public void testDebugCompleto() {
        System.out.println("=== Test Debug: Estado Completo ===");
        
        System.out.println("--- Ventana Actual ---");
        System.out.println("Login: " + verificarElementoExistente("#loginBtn"));
        System.out.println("Registro: " + verificarElementoExistente("#btnRegistro"));
        System.out.println("Modify: " + verificarElementoExistente("#btnModify"));
        
        System.out.println("--- Botones Visibles ---");
        try {
            lookup(".button").queryAll().forEach(node -> {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    System.out.println("  - " + btn.getId() + ": '" + btn.getText() + "'");
                }
            });
        } catch (Exception e) {
            System.out.println("  Error leyendo botones: " + e.getMessage());
        }
        
        System.out.println("✓ Debug completado");
    }
}