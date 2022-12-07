package Allies.component.ContestScreen.AlliesTeamsDataArea;

import Allies.component.ContestScreen.CandidatesArea.CandidateAreaRefresher;
import DataTransferObject.AllyTeamDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    @FXML private TableView<AlliesTeamsDataString> alliesTeamsListTableView;
    @FXML private TableColumn<AlliesTeamsDataString, String> teamName;
    @FXML private TableColumn<AlliesTeamsDataString, String> agentsAmount;
    @FXML private TableColumn<AlliesTeamsDataString, String> missionSize;
    private final BooleanProperty shouldUpdate;
    private Timer timer;
    private TimerTask listRefresher;
    private ObservableList<AlliesTeamsDataString> alliesTeamsDataStrings = FXCollections.observableArrayList();

    public AlliesTeamsListController() {
        shouldUpdate = new SimpleBooleanProperty();
    }

    public void resetComponent() {
        alliesTeamsListTableView.getItems().clear();
        listRefresher.cancel();
        timer.cancel();
    }

    private void updateUsersList(List<AllyTeamDTO> alliesTeamDTOList) {
        Platform.runLater(() -> {
                ObservableList<AlliesTeamsDataString> items = alliesTeamsListTableView.getItems();
            items.clear();
            for (AllyTeamDTO alliesTeamDTO : alliesTeamDTOList) {
                addAlliesTeamString(new AlliesTeamsDataString(alliesTeamDTO));
            }
        });
    }

    public void startListRefresher() {
        shouldUpdate.set(true);
        listRefresher = new AlliesTeamsListRefresher(this::updateUsersList,shouldUpdate);
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
    public void addAlliesTeamString(AlliesTeamsDataString uboatString) {
        alliesTeamsListTableView.getItems().add(uboatString);
    }

    public void gameOn() {
        shouldUpdate.set(false);
    }

    public void contestOver() {
        listRefresher.cancel();
    }
}
