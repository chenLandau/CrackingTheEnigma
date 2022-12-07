package Allies.component.ContestScreen;

import Allies.component.ContestScreen.AgentsProgressData.AgentsProgressController;
import Allies.component.ContestScreen.AlliesTeamsDataArea.AlliesTeamsListController;
import Allies.component.ContestScreen.CandidatesArea.CandidatesAreaController;
import Allies.component.ContestScreen.SelectedContestDataArea.SelectedContestDataController;
import Allies.component.main.AlliesMainController;
import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import okhttp3.*;
import utils.http.HttpClientUtil;

import java.io.IOException;

import static Allies.main.util.Constants.CONFIRM_CONTEST_OVER;

public class ContestScreenController {
    @FXML private Button okButton;
    @FXML private VBox contestOverVbox;
    @FXML private StackPane selectedContestDataComponent;
    @FXML private SelectedContestDataController selectedContestDataComponentController;
    @FXML private AnchorPane alliesTeamsListDataComponent;
    @FXML private AlliesTeamsListController alliesTeamsListDataComponentController;
    @FXML private ScrollPane candidatesScrollPaneComponent;
    @FXML private CandidatesAreaController candidatesScrollPaneComponentController;
    @FXML private AnchorPane agentsProgressAnchorPaneComponent;
    @FXML private AgentsProgressController agentsProgressAnchorPaneComponentController;
    private AlliesMainController alliesMainController;


    @FXML public void initialize() {
        selectedContestDataComponentController.setContestScreenController(this);
    }

    public void setUboatMainController(AlliesMainController alliesMainController) {
        this.alliesMainController = alliesMainController;
    }

    public void activeSelectedContestDataRefresher() {
        selectedContestDataComponentController.SelectedContestDataRefresher();
    }
    public void activeAlliesTeamsListRefresher() {
        alliesTeamsListDataComponentController.startListRefresher();
    }

    public void gameOn(){
        alliesMainController.gameOn();
        alliesTeamsListDataComponentController.gameOn();
        candidatesScrollPaneComponentController.startCandidateAreaRefresher();
        agentsProgressAnchorPaneComponentController.startAgentProgressListRefresher();
    }

    @FXML
    void okButtonClicked(ActionEvent event) {
        confirmContestOver();
        contestOverVbox.setVisible(false);
        alliesMainController.resetDashboardSScreen();
        selectedContestDataComponentController.resetComponent();
        alliesTeamsListDataComponentController.resetComponent();
        candidatesScrollPaneComponentController.resetComponent();
        agentsProgressAnchorPaneComponentController.resetComponent();
    }

    private void confirmContestOver() {
        String finalUrl = HttpUrl
                .parse(CONFIRM_CONTEST_OVER)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runRequestWithBody(finalUrl, RequestBody.create(new byte[]{}), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Ally: Confirm contest over");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
            }
        });
    }
    public void contestOver() {
        contestOverVbox.setVisible(true);
        alliesTeamsListDataComponentController.contestOver();
    }
}
