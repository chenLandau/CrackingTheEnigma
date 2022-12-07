package Uboat.component.ContestScreen;

import DataTransferObject.CandidateDTO;
import DataTransferObject.CodeConfigurationOutputDTO;
import DataTransferObject.DictionaryWordsDTO;
import Uboat.component.CodeConfigurationUtils.CodeConfigurationController;
import Uboat.component.CodeConfigurationUtils.CodeConfigurationUtils;
import Uboat.component.ContestScreen.AlliesTeamsDataArea.AlliesTeamsListController;
import Uboat.component.ContestScreen.CandidatesArea.CandidatesAreaController;
import Uboat.component.main.UboatMainController;
import Uboat.main.utils.Constants;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static Uboat.main.utils.Constants.*;
import static utils.Constants.*;
import static utils.Constants.REFRESH_RATE;


public class ContestScreenController {
    @FXML private Label errorMessageLabel;
    @FXML private Button processButton;
    @FXML private Button resetMachineButton;
    @FXML private TextField outputStringTextField;
    @FXML private Button readyButton;
    @FXML private Button logOutButton;
    @FXML private StackPane currentCodeConfigurationStackPane;
    @FXML private Label codeCalibrationLabel;
    @FXML private Label winnerNameLabel;
    @FXML private TextField inputStringTextField;
    @FXML private VBox tableViewVbox;
    @FXML private VBox dictionaryWordsVbox;
    @FXML private AnchorPane alliesTeamsListDataComponent;
    @FXML private AlliesTeamsListController alliesTeamsListDataComponentController;
    @FXML private HBox contestOverHBox;
    @FXML private ScrollPane candidatesScrollPaneComponent;
    @FXML private CandidatesAreaController candidatesScrollPaneComponentController;
    @FXML private AnchorPane winnerFoundAnchorPane;
    @FXML private Button okButton;
    private Timer timer;
    private TimerTask gameStatusRefresher;
    private UboatMainController uboatMainController;
    private Parent currentCodeConfigurationFxml;
    private CodeConfigurationController currentCodeConfigurationController;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private BooleanProperty shouldUpdate;


    public ContestScreenController() {
        shouldUpdate = new SimpleBooleanProperty();
    }

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        candidatesScrollPaneComponentController.setContestScreenController(this);
        errorMessageProperty.set("please double click on a dictionary word to add it to the input string!");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url1 = getClass().getResource("/Uboat/component/CodeConfigurationUtils/CodeConfiguration.fxml");
            currentCodeConfigurationFxml = fxmlLoader.load(url1.openStream());
            currentCodeConfigurationController = fxmlLoader.getController();
            currentCodeConfigurationController.setStyle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGameStatusRefresher() {
        shouldUpdate.set(true);
        gameStatusRefresher = new GameStatusRefresher(shouldUpdate);
        timer = new Timer();
        timer.schedule(gameStatusRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setUboatMainController(UboatMainController uboatMainController) {
        this.uboatMainController = uboatMainController;
    }

    public void activeAlliesTeamsListRefresher() {
        alliesTeamsListDataComponentController.startListRefresher();
    }

    public void setCodeConfigurationStackPane() {
        currentCodeConfigurationStackPane.getChildren().clear();
        String finalUrl = HttpUrl
                .parse(CODE_CONFIGURATION_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
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
                    currentCodeConfigurationStackPane.getChildren().clear();
                    currentCodeConfigurationController.setCurrentCodeConfiguration(codeConfigurationOutputDTO);
                    currentCodeConfigurationStackPane.getChildren().add(currentCodeConfigurationController.getCurrentCodeConfigurationHBox());
                    codeCalibrationLabel.setText(CodeConfigurationUtils.createLabelTextCodeConfiguration(codeConfigurationOutputDTO));
                });
            }
        });
    }

    public void setDictionaryWords() {
        String finalUrl = HttpUrl
                .parse(Constants.DICTIONARY_WORDS_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: Set dictionary words")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    String jsonDictionaryWordsDTO = null;
                    try {
                        jsonDictionaryWordsDTO = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    DictionaryWordsDTO dictionaryWordsDTO = GSON_INSTANCE.fromJson(jsonDictionaryWordsDTO,
                            DictionaryWordsDTO.class);

                    for (String word : dictionaryWordsDTO.getDictionaryWords()) {
                        Label wordLabel = new Label(word);
                        wordLabel.setOnMouseClicked(event -> {
                            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                                    inputStringTextField.setText(inputStringTextField.getText() + " " + wordLabel.getText());
                            }
                        });
                        wordLabel.setStyle("-fx-font-size: 12px;-fx-text-fill: black;-fx-font-family: Arial Black;");
                        dictionaryWordsVbox.getChildren().add(wordLabel);
                    }
                });
            }
        });
    }

    @FXML
    void resetMachineButtonClicked(ActionEvent event) {
        HttpClientUtil.runAsync(RESET_MACHINE, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: Reset machine")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonCodeConfigurationOutputDTO = response.body().string();
                CodeConfigurationOutputDTO codeConfigurationOutputDTO = GSON_INSTANCE.fromJson(jsonCodeConfigurationOutputDTO,
                        CodeConfigurationOutputDTO.class);
                Platform.runLater(() -> {
                    currentCodeConfigurationStackPane.getChildren().clear();
                    currentCodeConfigurationController.setCurrentCodeConfiguration(codeConfigurationOutputDTO);
                    currentCodeConfigurationStackPane.getChildren().add(currentCodeConfigurationController.getCurrentCodeConfigurationHBox());
                    codeCalibrationLabel.setText(CodeConfigurationUtils.createLabelTextCodeConfiguration(codeConfigurationOutputDTO));
                });
            }
        });
    }

    @FXML
    void processButtonClicked(ActionEvent event) {
        String json = GSON_INSTANCE.toJson(inputStringTextField.getText().trim());
        RequestBody requestBody = HttpClientUtil.createRequestBody("text/plain", json);

        String finalUrl = HttpUrl
                .parse(ENCRYPT_STRING)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: encrypt string")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string().trim();
                    if (response.code() != 200) {
                        Platform.runLater(() ->
                                errorMessageProperty.set("Invalid input string: " + responseBody)
                        );
                    } else {
                    Platform.runLater(() -> {
                    errorMessageProperty.set("");
                    outputStringTextField.setText(responseBody);
                    readyButton.setDisable(false);
                    uboatMainController.addDecryptedString();
                    setCodeConfigurationStackPane();
                    });
           }
        }
    });
  }

    @FXML
    void readyButtonClicked(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(SET_USER_READY)
                .newBuilder()
                .addQueryParameter("userType", "Uboat")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: set user ready")
                );
            }

            @Override
            public void onResponse(@org.jetbrains.annotations.NotNull Call call, @org.jetbrains.annotations.NotNull Response response) throws IOException {
                    Platform.runLater(() -> {
                        errorMessageProperty.set("response success");
                        processButton.setDisable(true);
                        readyButton.setDisable(true);
                        startGameStatusRefresher();
                        uboatMainController.readyButtonClicked();
                        candidatesScrollPaneComponentController.startCandidateAreaRefresher();
                    });
            }
        });
    }

    public void codeConfigurationChanged() {
        setCodeConfigurationStackPane();
        processButton.setDisable(false);
    }

    public void showWinnerTeamDetails() {
        HttpClientUtil.runAsync(GET_WINNER_TEAM_DETAILS, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: get winner team details")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonWinnerTeam = response.body().string();
                CandidateDTO winnerTeam = GSON_INSTANCE.fromJson(jsonWinnerTeam, CandidateDTO.class);
                Platform.runLater(() -> {
                    winnerNameLabel.setText(winnerTeam.getAllyName());
                    winnerFoundAnchorPane.setVisible(true);
                });
            }
        });
    }

    @FXML
    void okButtonClicked(ActionEvent event) {
        String finalUrl = HttpUrl
                .parse(RESET_CURRENT_CONTEST)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, RequestBody.create(new byte[]{}), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("Fail on Uboat: reset current contest")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            System.out.println("Something went wrong in reset current contest: " + responseBody)
                    );
                }
            }
        });
        resetCurrentContest();
    }

    @FXML
    void logOutButtonClicked(ActionEvent event) {
        reset();
        uboatMainController.logOutButtonClicked();
    }

    public void resetCurrentContest() {
        inputStringTextField.setText("");
        outputStringTextField.setText("");
        readyButton.setDisable(true);
        processButton.setDisable(false);
        candidatesScrollPaneComponentController.reset();
        winnerNameLabel.setText("");
        winnerFoundAnchorPane.setVisible(false);
        uboatMainController.resetCurrentContest();
    }

    private void reset(){
        resetCurrentContest();
        dictionaryWordsVbox.getChildren().clear();
        currentCodeConfigurationStackPane.getChildren().clear();
    }
}


