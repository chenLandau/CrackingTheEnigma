package Uboat.component.ContestScreen.AlliesTeamsDataArea;

import DataTransferObject.AllyTeamDTO;
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

public class AlliesTeamsListController implements Initializable {
    @FXML private TableView<alliesTeamsDataString> alliesTeamsListTableView;
    @FXML private TableColumn<alliesTeamsDataString, String> teamName;
    @FXML private TableColumn<alliesTeamsDataString, String> agentsAmount;
    @FXML private TableColumn<alliesTeamsDataString, String> missionSize;
    private ObservableList<alliesTeamsDataString> alliesTeamsDataStrings = FXCollections.observableArrayList();
    private Timer timer;
    private TimerTask listRefresher;

    private void updateUsersList(List<AllyTeamDTO> alliesTeamDTOList) {
        Platform.runLater(() -> {
                ObservableList<alliesTeamsDataString> items = alliesTeamsListTableView.getItems();
            items.clear();
            for (AllyTeamDTO alliesTeamDTO : alliesTeamDTOList) {
                addAlliesTeamString(new alliesTeamsDataString(alliesTeamDTO));
            }
        });
    }

    public void startListRefresher() {
        listRefresher = new AlliesTeamsListRefresher(
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        teamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        agentsAmount.setCellValueFactory(new PropertyValueFactory<>("agentsAmount"));
        missionSize.setCellValueFactory(new PropertyValueFactory<>("missionSize"));
        alliesTeamsListTableView.setItems(alliesTeamsDataStrings);
    }
    public void addAlliesTeamString(alliesTeamsDataString uboatString) {
        alliesTeamsListTableView.getItems().add(uboatString);
    }
}
