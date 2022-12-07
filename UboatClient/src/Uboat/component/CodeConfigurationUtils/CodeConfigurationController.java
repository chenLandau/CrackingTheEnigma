package Uboat.component.CodeConfigurationUtils;

import DataTransferObject.CodeConfigurationOutputDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CodeConfigurationController {
    @FXML private HBox currentCodeConfigurationHBox;
    @FXML private FlowPane rotorsCurrentCodeConfigurationFlowPane;
    @FXML private HBox reflectorCurrentCodeConfigurationHBox;
    @FXML private FlowPane plugsCurrentCodeConfigurationFlowPane;
    public HBox getCurrentCodeConfigurationHBox() {
        return currentCodeConfigurationHBox;
    }

    public void setStyle(){
        currentCodeConfigurationHBox.setStyle("-fx-border-color: white; -fx-background-color: rgba(51,46,46,0.96);");
        rotorsCurrentCodeConfigurationFlowPane.setStyle("-fx-background-color: rgba(51,46,46,0.96);");
        reflectorCurrentCodeConfigurationHBox.setStyle("-fx-background-color: rgba(51,46,46,0.96);");
    }

    public void setCurrentCodeConfiguration(CodeConfigurationOutputDTO codeConfigurationOutputDTO){
        createRotorsEllipse(codeConfigurationOutputDTO.getRotorsNumber(),codeConfigurationOutputDTO.getRotorsPosition(), codeConfigurationOutputDTO.getNotchPosition());
        createReflectorCylinder(codeConfigurationOutputDTO.getReflectorNumber());
    }

    public void createRotorsEllipse(List<Integer> rotorsNumber, List<Character> rotorsPosition, List<Integer> notchPosition ){
        rotorsCurrentCodeConfigurationFlowPane.getChildren().clear();

        for(int i = rotorsNumber.size() - 1 ; i>=0; i--) {
            try {
                FXMLLoader loader = new FXMLLoader();
                URL url = getClass().getResource("/Uboat/component/CodeConfigurationUtils/ellipse.fxml");
                loader.setLocation(url);
                Node singleWordTile = loader.load();
                SingleEllipseController ellipseController = loader.getController();
                ellipseController.setRotorNumberLabel(rotorsNumber.get(i).toString());
                ellipseController.setPositionLabel(rotorsPosition.get(i).toString()+"(" + notchPosition.get(i).toString()+")");
                rotorsCurrentCodeConfigurationFlowPane.getChildren().add(singleWordTile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void createReflectorCylinder(String reflectorNumber ){
        reflectorCurrentCodeConfigurationHBox.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("/Uboat/component/CodeConfigurationUtils/cylinder.fxml");
            loader.setLocation(url);
            Node singleWordTile = loader.load();
            CylinderController ellipseController = loader.getController();
            ellipseController.setReflectorNumberLabel(reflectorNumber);
            reflectorCurrentCodeConfigurationHBox.getChildren().add(singleWordTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
