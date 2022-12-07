package Agent.AgentUI.component.LoadAgentDetailsScreen;

import Agent.AgentLogic.AgentLogic;
import Agent.AgentUI.component.LoadAgentDetailsScreen.AlliesTeamsDataArea.AlliesTeamsDataString;
import Agent.AgentUI.component.LoadAgentDetailsScreen.AlliesTeamsDataArea.AlliesTeamsListController;
import Agent.AgentUI.component.main.AgentMainController;
import DataTransferObject.AgentLoginDTO;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;

import static Agent.AgentUI.main.utils.Constants.ADD_AGENT_TO_ALLY_TEAM;
import static utils.Constants.*;

public class LoadAgentDetailsScreenController {
    @FXML private Label errorMessageLabel;
    @FXML private AnchorPane alliesTeamsListDataComponent;
    @FXML private AlliesTeamsListController alliesTeamsListDataComponentController;
    @FXML private Slider threadsAmountSlider;
    @FXML private TextField missionsAmountTextField;
    @FXML private Button chooseButton;
    private AgentMainController agentMainController;
    private AgentLogic agentLogic;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();


    @FXML public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    @FXML
    void chooseButtonClicked(ActionEvent event) {
        try {
            AlliesTeamsDataString alliesTeamsDataString = alliesTeamsListDataComponentController.getAlliesTeamsDataStringSelection();
            Double doubleAmountOfAgents = threadsAmountSlider.getValue();
            int threadsAmount = doubleAmountOfAgents.intValue();
            int missionsAmount = Integer.parseInt(missionsAmountTextField.getText());

            if (alliesTeamsDataString == null)
                errorMessageProperty.set("Please choose team first!");
            else if (missionsAmount <= 0)
                errorMessageProperty.set("Please type an Integer greater than 0!");
            else{
                addAgentToAllyRequest(alliesTeamsDataString.getTeamName(), threadsAmount, missionsAmount);
                agentLogic.setAgentLogicDetails(threadsAmount, missionsAmount, alliesTeamsDataString.getTeamName());
                agentMainController.setAllyTeamLabel(alliesTeamsDataString.getTeamName());
            }
        }catch (NumberFormatException e){
            errorMessageProperty.set("Please type an Integer greater than 0!");
        }
    }

    private void addAgentToAllyRequest(String allyName, int threadsAmount, int missionsAmount){
        AgentLoginDTO agentLoginDTO = new AgentLoginDTO(allyName, threadsAmount, missionsAmount);
        String json = GSON_INSTANCE.toJson(agentLoginDTO);
        RequestBody requestBody = HttpClientUtil.createRequestBody("text/plain", json);

        String finalUrl = HttpUrl
                .parse(ADD_AGENT_TO_ALLY_TEAM)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: add agent to ally team");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() == 200) {
                    Platform.runLater(() -> {
                        agentMainController.switchToContestScreen();
                        agentMainController.startContestDataAreaRefresher();
                    });
                } else {
                    System.out.println("Something went wrong!!!");
                }
            }
        });
    }
    public void setAgentLogic(AgentLogic agentLogic) {
        this.agentLogic = agentLogic;
    }

    public void activeAlliesTeamsListRefresher() {
        alliesTeamsListDataComponentController.startListRefresher();
    }

    public void setAgentMainController(AgentMainController agentMainController) {
        this.agentMainController = agentMainController;
    }
}
