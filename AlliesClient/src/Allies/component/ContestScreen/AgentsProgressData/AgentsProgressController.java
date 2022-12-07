package Allies.component.ContestScreen.AgentsProgressData;

import DataTransferObject.AgentProgressDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;


public class AgentsProgressController implements Initializable {
    @FXML private TableView<AgentsProgressString> agentProgressListTableView;
    @FXML private TableColumn<AgentsProgressString, String> agentName;
    @FXML private TableColumn<AgentsProgressString, String> missionsOnQueue;
    @FXML private TableColumn<AgentsProgressString, String> totalAmountOfMissionTaken;
    @FXML private TableColumn<AgentsProgressString, String> missionsCompleted;
    @FXML private TableColumn<AgentsProgressString, String> totalAmountOfCandidatesFound;
    private Timer timer;
    private TimerTask agentProgressRefresher;

    private ObservableList<AgentsProgressString> agentProgressDataStrings = FXCollections.observableArrayList();

    private void updateAgentProgressList(List<AgentProgressDTO> agentProgressListDTO) {
        Platform.runLater(() -> {
                ObservableList<AgentsProgressString> items = agentProgressListTableView.getItems();
            items.clear();
            for (AgentProgressDTO agentProgressDTO : agentProgressListDTO) {
                addAgentProgressString(new AgentsProgressString(agentProgressDTO));
            }
        });
    }

    public void startAgentProgressListRefresher() {
        agentProgressRefresher = new AgentsProgressRefresher(
                this::updateAgentProgressList);
        timer = new Timer();
        timer.schedule(agentProgressRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @FXML //check
    public void initialize(URL location, ResourceBundle resources) {
        agentName.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        missionsOnQueue.setCellValueFactory(new PropertyValueFactory<>("missionsOnQueue"));
        totalAmountOfMissionTaken.setCellValueFactory(new PropertyValueFactory<>("totalAmountOfMissionTaken"));
        missionsCompleted.setCellValueFactory(new PropertyValueFactory<>("missionsCompleted"));
        totalAmountOfCandidatesFound.setCellValueFactory(new PropertyValueFactory<>("totalAmountOfCandidatesFound"));

        agentProgressListTableView.setItems(agentProgressDataStrings);

    }
    public void addAgentProgressString(AgentsProgressString agentsProgressString) {
        agentProgressListTableView.getItems().add(agentsProgressString);
    }

    public void resetComponent() {
        agentProgressListTableView.getItems().clear();
        agentProgressRefresher.cancel();
        timer.cancel();
    }
}
