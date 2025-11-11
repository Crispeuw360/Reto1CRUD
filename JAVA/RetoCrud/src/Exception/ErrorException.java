package Exception;

import javafx.scene.control.Alert;

public class ErrorException extends Exception {
    public ErrorException(String title1,String message1) {
        super(message1);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title1);
        alert.setHeaderText(null);
        alert.setContentText(message1);
        alert.showAndWait();
    }
}
