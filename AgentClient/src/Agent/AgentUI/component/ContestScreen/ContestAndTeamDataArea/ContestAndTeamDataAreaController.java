package Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea;
import Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea.ContestDataArea.ContestDataAreaController;
import Agent.AgentUI.component.ContestScreen.ContestScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ContestAndTeamDataAreaController {
    @FXML private Label allyTeamLabel;
    @FXML private Label noContestSelectedLabel;
    @FXML private AnchorPane selectedContestDataAnchorPane;
    @FXML private HBox contestDataAreaComponent;
    @FXML private ContestDataAreaController contestDataAreaComponentController;
    private ContestScreenController contestScreenController;


    @FXML public void initialize() {
        contestDataAreaComponentController.setContestAndTeamDataAreaController(this);
    }

    public void gameOn(String responseBody) {contestScreenController.gameOn(responseBody);}

    public void setContestScreenController(ContestScreenController contestScreenController) {
        this.contestScreenController = contestScreenController;
    }

    public void startContestDataAreaRefresher() {
        noContestSelectedLabel.setVisible(false);
        selectedContestDataAnchorPane.setVisible(true);
        contestDataAreaComponentController.startContestDataAreaRefresher();
    }
    public void setAllyTeamLabel(String allyName){
        allyTeamLabel.setText(allyName);
    }

    public void contestOver() {
        contestScreenController.contestOver();
    }

    public void resetComponent() {
        contestDataAreaComponentController.resetComponent();
        noContestSelectedLabel.setVisible(true);
        selectedContestDataAnchorPane.setVisible(false);

    }
}







