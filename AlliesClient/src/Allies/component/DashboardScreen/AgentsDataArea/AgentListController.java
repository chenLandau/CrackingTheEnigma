package Allies.component.DashboardScreen.AgentsDataArea;

import Allies.component.DashboardScreen.ContestDataArea.UboatString;
import DataTransferObject.AgentDTO;
import DataTransferObject.UboatDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;


public class AgentListController implements Initializable {
    @FXML private TableView<AgentString> agentListTableView;
    @FXML private TableColumn<AgentString, String> agentsName;
    @FXML private TableColumn<AgentString, String> threadsAmount;
    @FXML private TableColumn<AgentString, String> missionsAmount;;
    private ObservableList<AgentString> agentStrings = FXCollections.observableArrayList();
    private Timer timer;
    private TimerTask agentListRefresher;

    private void updateAgentList(List<AgentDTO> agentsList) {
        Platform.runLater(() -> {
            ObservableList<AgentString> items = agentListTableView.getItems();
            items.clear();
            for (AgentDTO agentDTO : agentsList) {
                addAgentString(new AgentString(agentDTO));
            }
        });
    }

    public void startListRefresher() {
        agentListRefresher = new AgentListRefresher(
                this::updateAgentList);
        timer = new Timer();
        timer.schedule(agentListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        agentsName.setCellValueFactory(new PropertyValueFactory<>("agentsName"));
        threadsAmount.setCellValueFactory(new PropertyValueFactory<>("threadsAmount"));
        missionsAmount.setCellValueFactory(new PropertyValueFactory<>("missionsAmount"));
        agentListTableView.setItems(agentStrings);
    }
    public void addAgentString(AgentString agentString) {
        agentListTableView.getItems().add(agentString);
    }
}
