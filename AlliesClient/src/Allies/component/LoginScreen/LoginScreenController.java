package Allies.component.LoginScreen;

import Allies.component.main.AlliesMainController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.http.HttpClientUtil;

import java.io.IOException;

import static utils.Constants.LOGIN_PAGE;

public class LoginScreenController {
    @FXML private TextField alliesNameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private AlliesMainController alliesMainController;


    @FXML public void initialize() {errorMessageLabel.textProperty().bind(errorMessageProperty);}

    public void setAlliesMainController(AlliesMainController alliesMainController) {
        this.alliesMainController = alliesMainController;
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {

        String userName = alliesNameTextField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("userType", "Allies")
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
                        alliesMainController.setButtonHBoxVisible();
                        alliesMainController.switchToDashboardScreen();
                        alliesMainController.activeUboatListRefresher();
                        alliesMainController.activeAgentListRefresher();
                    });
                }
            }
        });

    }

}
