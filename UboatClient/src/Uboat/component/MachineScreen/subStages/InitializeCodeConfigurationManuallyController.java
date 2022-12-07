package Uboat.component.MachineScreen.subStages;

import DataTransferObject.MachineSpecificationsDTO;
import DataTransferObject.ManualCodeConfigurationDTO;
import DataTransferObject.CodeConfigurationInputDTO;
import Uboat.component.MachineScreen.MachineScreenController;
import Uboat.main.utils.Constants;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;
import static utils.Constants.GSON_INSTANCE;

public class InitializeCodeConfigurationManuallyController {
    @FXML private Button ChooseRotorsBtn;
    @FXML private Button ChooseFirstPositionBtn;
    @FXML private Button ChooseReflectorNumberBtn;
    @FXML private Button ChoosePlugBoardBtn;
    @FXML private ScrollPane scrollPane;
    @FXML private Button setCodeBtn;
    @FXML private Button clearBtn;
    @FXML private HBox codeConfigurationHBox;
    @FXML private ScrollPane hBoxScrollPane;
    @FXML private Label warningLabel;
    private int connectionsNumber = 0;
    private MachineScreenController machineScreenController;
    private MachineConfigurationStage machineConfigurationStage;

    public MachineConfigurationStage getMachineConfigurationStage() { return machineConfigurationStage;}
    public Button getChooseReflectorNumberBtn() {
        return ChooseReflectorNumberBtn;
    }


   public void setDataMembers(MachineScreenController machineScreenController, Stage stage){
        this.machineScreenController = machineScreenController;
        this.machineConfigurationStage = new MachineConfigurationStage(stage,this);
        setMachineConfigurationStage();
    }

    public void setMachineConfigurationStage(){
        String finalUrl = HttpUrl
                .parse(Constants.CODE_CONFIGURATION_DETAILS_PAGE
                )
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl,new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               Platform.runLater(() ->
                       System.out.println("Fail on Uboat: Get code configuration details")
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonManualCodeConfigurationDTO = response.body().string();
                Platform.runLater(() -> {
                        ManualCodeConfigurationDTO manualCodeConfigurationDTO = GSON_INSTANCE.fromJson(jsonManualCodeConfigurationDTO,
                                ManualCodeConfigurationDTO.class);
                        machineConfigurationStage.setCodeConfigurationDetails(manualCodeConfigurationDTO);
                    }
               );
            }
        });
    }
    @FXML
    void ClickMeChooseRotorsBtnListener(ActionEvent event) {
        this.codeConfigurationHBox = machineConfigurationStage.getRotorsHBox();
        hBoxScrollPane.setContent(codeConfigurationHBox);
        scrollPane.setContent(machineConfigurationStage.getRotorsNumberFlowPane());
    }

    @FXML
    void ClickMeChooseFirstPositionBtnListener(ActionEvent event) {
        codeConfigurationHBox = machineConfigurationStage.getRotorsPositionHBox();
        hBoxScrollPane.setContent(codeConfigurationHBox);
        scrollPane.setContent(machineConfigurationStage.getRotorsPositionFlowPane());
    }

    @FXML
    void ClickMeChooseReflectorBtnListener(ActionEvent event) {
        codeConfigurationHBox = machineConfigurationStage.getReflectorHBox();
        hBoxScrollPane.setContent(codeConfigurationHBox);
        scrollPane.setContent(machineConfigurationStage.getReflectorFlowPane());

    }

    @FXML
    void ClickMeChoosePlugBoardBtnListener(ActionEvent event) {
//        machineConfigurationStage.getPlugBoardFlowPane().getChildren().get(machineConfigurationStage.getPlugBoardFlowPane().getChildren().size() - 1 ).setDisable(false);
//        List<Character> ABC = bodyController.getMainController().getEngine().createMachineAbcDTO().getABC();
//
//        if(connectionsNumber == 0) {
//            machineConfigurationStage.setPlugsHBox();
//            codeConfigurationHBox = machineConfigurationStage.getPlugsHBox();
//        }
//        else if(connectionsNumber == (ABC.size()/2) - 1) {
//             ChoosePlugBoardBtn.setDisable(true);
//        }
//
//        machineConfigurationStage.getPlugsHBox().getChildren().get(connectionsNumber).setDisable(true);
//        hBoxScrollPane.setContent(codeConfigurationHBox);
//        scrollPane.setContent(machineConfigurationStage.getPlugBoardFlowPane());
//        connectionsNumber++;
//
//        machineConfigurationStage.creatPlugConnection(connectionsNumber,ABC);
    }

    @FXML void ClickMeSetCodeBtnListener(ActionEvent event){
        boolean codeConfigurationIsValid = false;
        warningLabel.setText("");
        codeConfigurationIsValid = machineConfigurationStage.CheckCodeConfigurationValidityAndSetData(warningLabel);
        if(codeConfigurationIsValid){
            CodeConfigurationInputDTO codeConfigurationInputDTO = machineConfigurationStage.getCodeConfigurationInputDTO();

            String json = GSON_INSTANCE.toJson(codeConfigurationInputDTO);

            RequestBody requestBody = HttpClientUtil.createRequestBody("text/plain", json);

            String finalUrl = HttpUrl
                    .parse(Constants.INITIALIZE_CODE_CONFIGURATION_PAGE)
                    .newBuilder()
                    .addQueryParameter("Initialization-Type", "MANUAL")
                    .build()
                    .toString();

            HttpClientUtil.runRequestWithBody(finalUrl, requestBody, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            System.out.println("Fail on Uboat: initialize code configuration: " + e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseBody = response.body().string();
                        Platform.runLater(() -> {
                            machineScreenController.codeConfigurationChanged();
                            machineConfigurationStage.getSubStage().close();
                        });
                }
            });
        }
        else{
            warningLabel.setVisible(true);
        }
    }
    @FXML void ClickMeClearBtnListener(ActionEvent event) {

    }

    public String PrintMachineSpecifications(MachineSpecificationsDTO machineSpecifications ) {
        StringBuilder machineText = new StringBuilder();
        machineText.append("amount of rotors in use/total amount of rotors: " + machineSpecifications.getRotorsInUseAmount()
                        + "/" + machineSpecifications.getRotorsAmount()).append("\n")
                .append("Reflectors amount: " + machineSpecifications.getReflectorAmount()).append("\n")
                .append("Decrypted messages amount: " + machineSpecifications.getDecryptedMessagesAmount());
        return machineText.toString();
    }

}

