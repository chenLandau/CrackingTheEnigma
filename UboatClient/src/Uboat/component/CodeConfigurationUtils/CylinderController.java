package Uboat.component.CodeConfigurationUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CylinderController {
    @FXML private Label reflectorNumber;

    public void setReflectorNumberLabel(String value){
        reflectorNumber.setText(value);
    }
}
