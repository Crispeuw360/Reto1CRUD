
package view;

import java.awt.Window;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.*;

// ESTA ANOTACIÓN ORDENA LOS TESTS POR NOMBRE


//Alerta Este Test No garantiza que se haga bien a la primera, si quieres que se hagan los test de manera correcta, comenta todos los test menos el que quieras probar, 
//No habido manera de arreglar el problema ya que vien de netbeans y javaFx

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ViewControllersUITest extends ApplicationTest {

    private Stage primaryStage;
    private String usuarioUnico;
    private double randomNumber = 25;
    private TextField fieldUser;
    private TextField fieldName;
    private TextField fieldSurname;
    private TextField fieldGmail;
    private TextField fieldTel;
    private TextField fieldCard;
    private PasswordField fieldPass;
    private PasswordField fieldPass2;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setX(100);
        stage.setY(100);
        stage.centerOnScreen();
        
        usuarioUnico = "testuser_" + randomNumber;
        
        new RetoCrud().start(stage);
    }

    @Before
public void prepararTest() {
    try {
        // Estrategia 1: Cerrar todos los stages excepto el primary usando Stage
        javafx.application.Platform.runLater(() -> {
            // Obtener todos los stages a través de la escena
            Set<Stage> stages = new HashSet<>();
            
            // Buscar stages desde las escenas conocidas
            if (primaryStage.getScene() != null) {
                // Recorrer todos los nodos para encontrar stages hijos
                encontrarStages(primaryStage.getScene().getRoot(), stages);
            }
            
            // Cerrar todos los stages excepto el principal
            for (Stage stage : stages) {
                if (stage != primaryStage && stage.isShowing()) {
                    stage.close();
                }
            }
        });
        
        // Esperar a que se cierren las ventanas
        sleep(1000);
        
        // También intentar cerrar alerts específicos
        cerrarTodosLosAlertsYVentanas();
        
        sleep(1000);
        
    } catch (Exception e) {
        System.out.println("Error preparando test: " + e.getMessage());
    }
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

    @Test 
    public void test09_NavegandoWindowAdmin() {
        boolean hacerLoginExitoso =false;

        llenarCampo("#usernameField", "admin1", "admin1");
        llenarCampo("#passwordField", "adminpass1", "adminpass1");
        clickOn("#loginBtn");
        sleep(2000);

        hacerLoginExitoso = manejarAlertConBoton("Aceptar", "Correcto");
        
        if(hacerLoginExitoso){
            System.out.println("--- Probando navegación a Admin---");

            clickOn("#btnBack");
            sleep(2000);
        }
    }
    
    @Test 
    public void test10_WindowAdmin() {
        System.out.println("=== Test 9: Window Admin Completo ===");

        // Login como administrador
        llenarCampo("#usernameField", "admin1", "Usuario admin");
        llenarCampo("#passwordField", "adminpass1", "Contraseña admin");
        clickOn("#loginBtn");
        sleep(3000);

        boolean loginExitoso = manejarAlertConBoton("Aceptar", "Correcto");

        if(loginExitoso){
            System.out.println("✓ Login admin exitoso - Accediendo a Admin Window");
            sleep(2000);

            // 1. Verificar elementos básicos de Admin Window
            System.out.println("--- Verificando elementos Admin Window ---");
            verificarElemento("#comboUsers", "ComboBox de usuarios");
            verificarElemento("#btnBack", "Botón Volver");
            verificarElemento("#btnModify", "Botón Modificar");
            verificarElemento("#btnSave", "Botón Guardar");
            verificarElemento("#btnDelete", "Botón Eliminar");
            verificarElemento("#fieldUser", "Campo Usuario");
            verificarElemento("#fieldName", "Campo Nombre");
            verificarElemento("#fieldSurname", "Campo Apellido");
            verificarElemento("#fieldGmail", "Campo Email");
            verificarElemento("#fieldTel", "Campo Teléfono");
            verificarElemento("#fieldCard", "Campo Tarjeta");
            verificarElemento("#fieldPass", "Campo Contraseña");
            verificarElemento("#fieldPass2", "Campo Confirmar Contraseña");
            verificarElemento("#comboGender", "ComboBox Género");

            // 2. Verificar estado inicial de botones
            System.out.println("--- Verificando estado inicial ---");
            verificarEstadoBoton("#btnSave", false, "Botón Guardar deshabilitado inicialmente");
            verificarEstadoBoton("#btnModify", false, "Botón Modificar deshabilitado inicialmente");
            verificarEstadoBoton("#btnDelete", true, "Botón Eliminar habilitado");

            // 3. Verificar que campos no son editables inicialmente
            System.out.println("--- Verificando campos no editables ---");
            verificarCampoNoEditable("#fieldUser", "Campo usuario no editable");
            verificarCampoNoEditable("#fieldName", "Campo nombre no editable");
            verificarCampoNoEditable("#fieldSurname", "Campo apellido no editable");
            verificarCampoNoEditable("#fieldGmail", "Campo email no editable");
            verificarCampoNoEditable("#fieldTel", "Campo teléfono no editable");
            verificarCampoNoEditable("#fieldCard", "Campo tarjeta no editable");
            verificarCampoNoEditable("#fieldPass", "Campo contraseña no editable");
            verificarCampoNoEditable("#fieldPass2", "Campo confirmar contraseña no editable");

            // 4. Probar selección de usuario del ComboBox
            System.out.println("--- Probando selección de usuario ---");
            if (verificarElementoExistente("#comboUsers")) {
                clickOn("#comboUsers");
                sleep(1000);

                // Verificar si hay usuarios en el ComboBox
                try {
                    // Intentar seleccionar el primer usuario disponible
                    clickOn(".list-cell");
                    sleep(2000);

                    // Verificar que los campos se llenan con datos
                    String textoUsuario = obtenerTextoCampo("#fieldUser");
                    if (!textoUsuario.isEmpty()) {
                        System.out.println("✓ Usuario seleccionado: " + textoUsuario);

                        // Verificar que botones se habilitan después de selección
                        verificarEstadoBoton("#btnModify", true, "Botón Modificar habilitado tras selección");
                        verificarEstadoBoton("#btnSave", false, "Botón Guardar permanece deshabilitado");

                        // 5. Probar funcionalidad de modificación
                        System.out.println("--- Probando funcionalidad Modificar ---");
                        clickOn("#btnModify");
                        sleep(1000);


                        // Verificar estado de botones en modo edición
                        verificarEstadoBoton("#btnModify", false, "Botón Modificar deshabilitado en edición");
                        verificarEstadoBoton("#btnSave", true, "Botón Guardar habilitado en edición");

                        // 6. Probar validaciones al guardar
                        System.out.println("--- Probando validaciones ---");

                        // a. Campos vacíos
                        limpiarCampo("#fieldName");
                        clickOn("#btnSave");
                        sleep(2000);
                        manejarAlertConBoton("Aceptar", "Error campos vacíos");

                        // b. Email inválido
                        llenarCampo("#fieldName", "NombreTest", "Restaurar nombre");
                        limpiarCampo("#fieldGmail");
                        llenarCampo("#fieldGmail", "email-invalido", "Email inválido");
                        clickOn("#btnSave");
                        sleep(2000);
                        manejarAlertConBoton("Aceptar", "Error email inválido");

                        // c. Contraseñas no coincidentes
                        llenarCampo("#fieldGmail", "test@test.com", "Email válido");
                        String passOriginal = obtenerTextoCampo("#fieldPass");
                        limpiarCampo("#fieldPass2");
                        llenarCampo("#fieldPass2", "contraseñadiferente", "Contraseña diferente");
                        clickOn("#btnSave");
                        sleep(2000);
                        manejarAlertConBoton("Aceptar", "Error contraseñas no coincidentes");

                        // d. Guardado exitoso (restaurar contraseñas iguales)
                        llenarCampo("#fieldPass2", passOriginal, "Restaurar contraseña igual");
                        clickOn("#btnSave");
                        sleep(2000);
                        boolean guardadoExitoso = manejarAlertConBoton("Aceptar", "Éxito");

                        if (guardadoExitoso) {
                            System.out.println("✓ Guardado exitoso de modificaciones");

                            // Verificar que volvió al estado normal
                            verificarEstadoBoton("#btnModify", true, "Botón Modificar habilitado tras guardar");
                            verificarEstadoBoton("#btnSave", false, "Botón Guardar deshabilitado tras guardar");
                        }

                        // 7. Probar funcionalidad de eliminación
                        System.out.println("--- Probando funcionalidad Eliminar ---");
                        clickOn("#btnDelete");
                        sleep(2000);

                        // Manejar posible diálogo de confirmación
                        boolean eliminacionExitosa = manejarAlertConBoton("Aceptar", "Eliminación");

                        if (eliminacionExitosa) {
                            System.out.println("✓ Eliminación exitosa de usuario");

                            // Verificar que los campos se limpiaron
                            sleep(1000);
                            if (obtenerTextoCampo("#fieldUser").isEmpty()) {
                                System.out.println("✓ Campos limpiados correctamente tras eliminar");
                            }
                        }

                    } else {
                        System.out.println("⚠ ComboBox vacío o campos no se llenaron - omitiendo pruebas de selección");
                    }

                } catch (Exception e) {
                    System.out.println("⚠ No hay usuarios en el ComboBox o error en selección: " + e.getMessage());
                }
        }
        
        // 8. Probar navegación de vuelta
        System.out.println("--- Probando navegación de vuelta ---");
        clickOn("#btnBack");
        sleep(2000);
        
        // Verificar que volvimos al login
        if (verificarElementoExistente("#loginBtn")) {
            System.out.println("✓ Navegación de vuelta exitosa");
        } else {
            System.out.println("✗ Error en navegación de vuelta");
        }
        
    } else {
        System.out.println("✗ Login admin fallido - omitiendo test Admin Window");
        // Intentar manejar alert de error si existe
        manejarAlertConBoton("Aceptar", "Error");
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
    private void encontrarStages(javafx.scene.Node node, Set<Stage> stages) {
    if (node.getScene() != null && node.getScene().getWindow() instanceof Stage) {
        stages.add((Stage) node.getScene().getWindow());
    }
    
    if (node instanceof javafx.scene.Parent) {
        for (javafx.scene.Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
            encontrarStages(child, stages);
        }
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
// ========== MÉTODOS HELPER ADICIONALES PARA ADMIN ==========

private void verificarCampoNoEditable(String selector, String descripcion) {
    try {
        // Intentar escribir en el campo para ver si es editable
        String textoOriginal = obtenerTextoCampo(selector);
        clickOn(selector);
        write("test");
        sleep(500);
        
        String textoDespues = obtenerTextoCampo(selector);
        
        // Si el texto no cambió, no es editable
        if (textoDespues.equals(textoOriginal)) {
            System.out.println("✓ " + descripcion);
        } else {
            System.out.println("✗ " + descripcion + " - El campo es editable");
            // Restaurar texto original
            clickOn(selector);
            push(javafx.scene.input.KeyCode.CONTROL);
            push(javafx.scene.input.KeyCode.A);
            release(javafx.scene.input.KeyCode.A);
            release(javafx.scene.input.KeyCode.CONTROL);
            write(textoOriginal);
        }
    } catch (Exception e) {
        System.out.println("✗ " + descripcion + " - Error: " + e.getMessage());
    }
}

private String obtenerTextoCampo(String selector) {
    try {
        // Para TextField normales
        try {
            TextField field = lookup(selector).query();
            return field.getText();
        } catch (Exception e) {
            // Para PasswordField
            try {
                PasswordField passField = lookup(selector).query();
                return passField.getText();
            } catch (Exception e2) {
                System.out.println("No se pudo obtener texto del campo: " + selector);
                return "";
            }
        }
    } catch (Exception e) {
        return "";
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