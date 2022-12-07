package Uboat.component.LoginScreen;

import Uboat.component.main.UboatMainController;
import Uboat.main.utils.Constants;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;

import static utils.Constants.LOGIN_PAGE;

public class LoginScreenController {
    private UboatMainController uboatMainController;
    @FXML private TextField UboatNameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;
    @FXML private Button loadButton;
    private Stage primaryStage;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();


    @FXML public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        HttpClientUtil.set();
    }

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    @FXML void loginButtonClicked(ActionEvent event) {

        String userName = UboatNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("userType", "Uboat")
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        errorMessageProperty.set("Login succeeded! Please upload a contest file.");
                        UboatNameTextField.setEditable(false);
                        loadButton.setDisable(false);
                        loginButton.setDisable(true);
                    });
                }
            }
        });
    }

    @FXML void loadButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file","",
                        RequestBody.create(selectedFile,MediaType.parse("application/octet-stream")))
                .build();
        String finalUrl = HttpUrl
                .parse(Constants.FILE_UPLOAD_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runUploadFile(finalUrl,body, new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Fail on upload file: " + e.getMessage())
                );
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() ->
                            errorMessageProperty.set("File is invalid: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        errorMessageProperty.set("File upload successfully");
                        uboatMainController.loadButtonClicked();
                        uboatMainController.switchToMachineScreen();
                        loadButton.setDisable(true);
                    });
                }
            }
        });
    }

    public void setUboatMainController(UboatMainController uboatMainController) {
        this.uboatMainController = uboatMainController;
    }

    public void resetScreen() {
        UboatNameTextField.setEditable(true);
        loginButton.setDisable(false);
        loadButton.setDisable(true);
        UboatNameTextField.setText("");
        errorMessageProperty.set("");
    }
}
