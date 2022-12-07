package Uboat.component.MachineScreen;

import DataTransferObject.CodeConfigurationOutputDTO;
import DataTransferObject.MachineSpecificationsDTO;
import Uboat.component.CodeConfigurationUtils.CodeConfigurationController;
import Uboat.component.CodeConfigurationUtils.CodeConfigurationUtils;
import Uboat.component.MachineScreen.subStages.InitializeCodeConfigurationManuallyController;
import Uboat.component.main.UboatMainController;
import Uboat.main.utils.Constants;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;

import static utils.Constants.GSON_INSTANCE;

public class MachineScreenController {
    @FXML private VBox machineDetailsVBox;
    @FXML private Label rotorsDetailsLabel;
    @FXML private Label reflectorDetailsLabel;
    @FXML private Label decryptedMessagesAmountLabel;
    @FXML private VBox codeConfigurationVBox;
    @FXML private StackPane codeCalibrationStackPane;
    @FXML private Label codeCalibrationLabel;
    @FXML private Button setManualCodeBtn;
    @FXML private Button setRandomCodeButton;
    private UboatMainController uboatMainController;
    private Parent currentCodeConfigurationFxml;
    private SimpleIntegerProperty decryptedMessagesAmount = new SimpleIntegerProperty();
    private InitializeCodeConfigurationManuallyController codeConfigurationManuallyController;
    private CodeConfigurationController codeCalibrationController;


    @FXML public void initialize(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url1 = getClass().getResource("/Uboat/component/CodeConfigurationUtils/CodeConfiguration.fxml");
            currentCodeConfigurationFxml = fxmlLoader.load(url1.openStream());
            codeCalibrationController = fxmlLoader.getController();
            codeCalibrationController.setStyle();
            decryptedMessagesAmountLabel.textProperty().bind(decryptedMessagesAmount.asString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUboatMainController(UboatMainController uboatMainController) {
        this.uboatMainController = uboatMainController;
    }

    @FXML void setManualCodeBtnClicked(ActionEvent event) throws IOException{
        start();
        codeConfigurationManuallyController.getMachineConfigurationStage().getSubStage().show();
    }

    private void start()throws IOException{
        Stage subStage = new Stage();
        subStage.setTitle("Set Manually Code");
        subStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/Uboat/component/MachineScreen/subStages/CodeConfigurationManuallyNewStage.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        codeConfigurationManuallyController = fxmlLoader.getController();
        codeConfigurationManuallyController.setDataMembers(this, subStage);
        Scene scene = new Scene(root,770,480);
        subStage.setScene(scene);
    }

    @FXML void setRandomCodeButtonClicked(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(Constants.INITIALIZE_CODE_CONFIGURATION_PAGE)
                .newBuilder()
                .addQueryParameter("Initialization-Type", "AUTOMATIC")
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, RequestBody.create(new byte[]{}), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: Initialize code configuration")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Platform.runLater(() -> {
                    codeConfigurationChanged();
                });
            }
        });
    }

    public void setMachineDetails(){
        String finalUrl = HttpUrl
                .parse(Constants.MACHINE_DETAILS_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl,new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: Get machine details")
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Platform.runLater(() -> {
                        String jsonMachineSpecificationDTO = null;
                        try {
                            jsonMachineSpecificationDTO = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MachineSpecificationsDTO machineSpecificationsDTO = GSON_INSTANCE.fromJson(jsonMachineSpecificationDTO,
                                                                                                    MachineSpecificationsDTO.class);
                    rotorsDetailsLabel.setText(machineSpecificationsDTO.getRotorsInUseAmount() + " / " + machineSpecificationsDTO.getRotorsAmount());
                    reflectorDetailsLabel.setText(String.valueOf(machineSpecificationsDTO.getReflectorAmount()));
                    decryptedMessagesAmount.set(machineSpecificationsDTO.getDecryptedMessagesAmount());
                    });
                }
        });
    }

    public void codeConfigurationChanged() {
        uboatMainController.codeConfigurationChanged();
    }

    public void setCodeConfigurationStackPane(){
        codeCalibrationStackPane.getChildren().clear();
        String finalUrl = HttpUrl
                .parse(Constants.CODE_CONFIGURATION_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl,new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: Get code configuration")
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    String jsonCodeConfigurationOutputDTO = null;
                    try {
                        jsonCodeConfigurationOutputDTO = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CodeConfigurationOutputDTO codeConfigurationOutputDTO = GSON_INSTANCE.fromJson(jsonCodeConfigurationOutputDTO,
                            CodeConfigurationOutputDTO.class);
                    codeCalibrationController.setCurrentCodeConfiguration(codeConfigurationOutputDTO);
                    codeCalibrationStackPane.getChildren().add(codeCalibrationController.getCurrentCodeConfigurationHBox());
                    codeCalibrationLabel.setText(CodeConfigurationUtils.createLabelTextCodeConfiguration(codeConfigurationOutputDTO));
                });
            }
        });
    }

    public void LoadFileClicked() {
        setMachineDetails();
        setCodeButtonDisable(false);
    }

    private void setCodeButtonDisable(boolean disable) {
        setRandomCodeButton.setDisable(disable);
        setManualCodeBtn.setDisable(disable);
    }

    public void readyButtonClicked() {
        setCodeButtonDisable(true);
    }

    public void addDecryptedString() {
        decryptedMessagesAmount.set(decryptedMessagesAmount.getValue() + 1);
    }

    public void resetCurrentContest() {
        setCodeButtonDisable(false);
    }

    public void reset(){
        resetCurrentContest();
        codeCalibrationStackPane.getChildren().clear();
        rotorsDetailsLabel.setText("");
        reflectorDetailsLabel.setText("");
        decryptedMessagesAmount.set(0);
        codeCalibrationLabel.setText("");
    }
}
