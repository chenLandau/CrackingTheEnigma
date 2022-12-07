package Uboat.component.CodeConfigurationUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleEllipseController {
    @FXML private Label rotorNumberLabel;
    @FXML private Label positionAndNotchLabel;

    public void setRotorNumberLabel(String value){
        rotorNumberLabel.setText(value);
    }
    public void setPositionLabel(String value){
        positionAndNotchLabel.setText(value);
    }
}
