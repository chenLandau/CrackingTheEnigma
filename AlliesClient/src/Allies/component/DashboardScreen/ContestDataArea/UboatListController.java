package Allies.component.DashboardScreen.ContestDataArea;

import DataTransferObject.UboatDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static utils.Constants.REFRESH_RATE;


public class UboatListController implements Initializable {
    @FXML private TableView<UboatString> uboatListTableView;
    @FXML private TableColumn<UboatString, String> battleFieldName;
    @FXML private TableColumn<UboatString, String> uboatName;
    @FXML private TableColumn<UboatString, String> gameStatus;
    @FXML private TableColumn<UboatString, String> level;
    @FXML private TableColumn<UboatString, String> alliesAmount;
    private ObservableList<UboatString> uboatStrings = FXCollections.observableArrayList();
    private UboatString currentChoose;
    private Timer timer;
    private TimerTask uboatListRefresher;
    private final BooleanProperty autoUpdate;

    @FXML void getTableChoose(MouseEvent event) {
        currentChoose = uboatListTableView.getSelectionModel().getSelectedItem();
    }

    public UboatString getUboatStringSelection() {
        return currentChoose;
    }

    public UboatListController() {
        autoUpdate = new SimpleBooleanProperty();
    }

    private void updateUboatList(List<UboatDTO> usersNames) {
        Platform.runLater(() -> {
                ObservableList<UboatString> items = uboatListTableView.getItems();
            items.clear();
            for (UboatDTO uboat : usersNames) {
                addUboatString(new UboatString(uboat));
            }
        });
    }

    public void startListRefresher() {
        uboatListRefresher = new UboatListRefresher(this::updateUboatList);
        timer = new Timer();
        timer.schedule(uboatListRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        battleFieldName.setCellValueFactory(new PropertyValueFactory<>("battleFieldName"));
        uboatName.setCellValueFactory(new PropertyValueFactory<>("uboatName"));
        gameStatus.setCellValueFactory(new PropertyValueFactory<>("gameStatus"));
        level.setCellValueFactory(new PropertyValueFactory<>("level"));
        alliesAmount.setCellValueFactory(new PropertyValueFactory<>("alliesAmount"));
        uboatListTableView.setItems(uboatStrings);
    }
    public void addUboatString(UboatString uboatString) {
        uboatListTableView.getItems().add(uboatString);
    }

}
