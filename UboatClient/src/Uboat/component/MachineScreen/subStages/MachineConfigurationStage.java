package Uboat.component.MachineScreen.subStages;

import DataTransferObject.*;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MachineConfigurationStage {
    private InitializeCodeConfigurationManuallyController codeConfigurationManuallyController;
    private Stage subStage;
    private HBox rotorsHBox;
    private HBox rotorsPositionHBox;
    private HBox reflectorHBox;
    private HBox plugsHBox;
    private FlowPane rotorsNumberFlowPane;
    private FlowPane rotorsPositionFlowPane;
    private FlowPane reflectorFlowPane;
    private FlowPane plugBoardFlowPane;
    private List<Integer> rotorsNumberList = new ArrayList<>();
    private List<Character> rotorsPositionList = new ArrayList<>();
    private String reflectorNumber;
    private List<String> plugBoardConnectionList = new ArrayList<>();
    private int rotorsCounter = 0;
    private int positionCounter = 0;
    private int reflectorCounter = 0;

    public Stage getSubStage() {
        return subStage;
    }
    public FlowPane getRotorsNumberFlowPane() { return rotorsNumberFlowPane; }
    public FlowPane getRotorsPositionFlowPane() {
        return rotorsPositionFlowPane;
    }
    public FlowPane getReflectorFlowPane() {
        return reflectorFlowPane;
    }
    public FlowPane getPlugBoardFlowPane() {
        return plugBoardFlowPane;
    }
    public HBox getRotorsHBox() {return rotorsHBox; }
    public HBox getRotorsPositionHBox() {
        return rotorsPositionHBox;
    }
    public HBox getReflectorHBox() {
        return reflectorHBox;
    }
    public HBox getPlugsHBox() {
        return plugsHBox;
    }
    public List<Integer> getRotorsNumberList() { return rotorsNumberList; }
    public List<Character> getRotorsPositionList() { return rotorsPositionList; }
    public String getReflectorNumber() { return reflectorNumber; }
    public List<String> getPlugBoardConnectionList() { return plugBoardConnectionList; }

    public Boolean CheckCodeConfigurationValidityAndSetData(Label warningLabel) {
        rotorsNumberList.clear();
        rotorsPositionList.clear();
        reflectorNumber = null;
        Boolean codeConfigurationIsValid = true;

        for (int i = rotorsHBox.getChildren().size() - 1; i > 0  && codeConfigurationIsValid; i--) {
            Button btn = (Button) rotorsHBox.getChildren().get(i);
            codeConfigurationIsValid = !btn.getText().equals("");
            if (codeConfigurationIsValid)
                rotorsNumberList.add(Integer.parseInt(btn.getText()));
            else
                warningLabel.setText("Set all rotors!");
        }
        System.out.println(rotorsNumberList);

        for (int i =  rotorsPositionHBox.getChildren().size() - 1; i > 0 && codeConfigurationIsValid; i--) {
            Button btn = (Button) rotorsPositionHBox.getChildren().get(i);
            codeConfigurationIsValid = !btn.getText().equals("");
            if (codeConfigurationIsValid) {
                rotorsPositionList.add(btn.getText().charAt(0));
                codeConfigurationManuallyController.getChooseReflectorNumberBtn().setDisable(false);
            }
            else
                warningLabel.setText("Set all rotors positions!");
        }
        System.out.println(rotorsPositionList);

        if(codeConfigurationIsValid){
            Button btn = (Button) reflectorHBox.getChildren().get(1);
            codeConfigurationIsValid = !btn.getText().equals("");
            if (codeConfigurationIsValid)
                reflectorNumber = btn.getText();
            else
                warningLabel.setText("Choose reflector!");
        }

        if(plugsHBox != null && codeConfigurationIsValid) {
            StringBuilder connection = new StringBuilder();
            for (int i = 1; i < plugsHBox.getChildren().size(); i++) {
                HBox hbox = (HBox) plugsHBox.getChildren().get(i);
                Button firstBtn = (Button) hbox.getChildren().get(0);
                Button SecondBtn = (Button) hbox.getChildren().get(1);
                codeConfigurationIsValid = firstBtn.getText() != "" && SecondBtn.getText() != "";
                if (codeConfigurationIsValid) {
                    connection.append(firstBtn.getText()).append(SecondBtn.getText());
                    plugBoardConnectionList.add(connection.toString());
                    connection.delete(0, 2);
                } else
                    warningLabel.setText("Make sure each connection is between 2!");
            }
        }

        return codeConfigurationIsValid;
    }

    public MachineConfigurationStage(Stage stage, InitializeCodeConfigurationManuallyController codeConfigurationManuallyController) {
        this.subStage = stage;
        this.codeConfigurationManuallyController = codeConfigurationManuallyController;
    }

    public void setCodeConfigurationDetails(ManualCodeConfigurationDTO manualCodeConfigurationDTO){
        setRotorsNumberFlowPane(manualCodeConfigurationDTO.getTotalRotorsNumber());
        setRotorsPositionFlowPane(manualCodeConfigurationDTO.getABC());
        setReflectorFlowPane(manualCodeConfigurationDTO.getReflectorAmount(), manualCodeConfigurationDTO.getNumeralNumbers());
        //setPlugBoardFlowPane(mainController.getEngine().createMachineAbcDTO());

        setRotorsHBox(manualCodeConfigurationDTO.getRotorsCount(), manualCodeConfigurationDTO.getABC());
        setReflectorHBox();
    }

    private void setRotorsNumberFlowPane(int totalRotorsNumber) {
        rotorsNumberFlowPane = createFlowPane();
        rotorsNumberFlowPane.setAlignment(Pos.TOP_LEFT);
        for (int i = 0; i < totalRotorsNumber; i++) {
            rotorsNumberFlowPane.getChildren().add(createRotorNumberCell(String.valueOf(i+1)));
        }
    }
    private void setRotorsPositionFlowPane(List<Character> ABC){
        rotorsPositionFlowPane = createFlowPane();
        rotorsPositionFlowPane.setAlignment(Pos.TOP_LEFT);

        for (int i = 0; i < ABC.size(); i++) {
            rotorsPositionFlowPane.getChildren().add(createRotorPositionCell(ABC.get(i).toString()));
        }
    }
    private void setReflectorFlowPane(int reflectorsAmount, Map<Integer, String> numeralNumbersMap){
        reflectorFlowPane = createFlowPane();
        reflectorFlowPane.setAlignment(Pos.TOP_LEFT);

        for (int i = 1; i <= reflectorsAmount; i ++) {
            reflectorFlowPane.getChildren().add(createReflectorCell(numeralNumbersMap.get(i)));
        }
    }

    private Button createRotorNumberCell(String rotorNumber) {
        final Button cell = new Button(rotorNumber);
        cell.setId(rotorNumber);
        cell.setPrefSize(50, 50);
        cell.setAlignment(Pos.CENTER);
        cell.setStyle("-fx-border-color: gray; -fx-border-width: 3");
        //cell.getStyleClass()
        cell.setOnDragDetected((event) -> {
            WritableImage snapshot = cell.snapshot(new SnapshotParameters(), null);
            Dragboard db = cell.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(rotorNumber );
            db.setContent(content);
            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

            event.consume();
        });

        cell.setOnDragDone((event) -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                cell.setText("");
            }

            event.consume();
       });
        return cell;
    }
    private Button createReflectorCell(String reflectorId) {
        final Button cell = new Button(reflectorId);
        cell.setId(reflectorId);
        cell.setPrefSize(50, 50);
        cell.setAlignment(Pos.CENTER);
        cell.setStyle("-fx-border-color: gray; -fx-border-width: 3");
        cell.setOnDragDetected((event) -> {
            WritableImage snapshot = cell.snapshot(new SnapshotParameters(), null);
            Dragboard db = cell.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(reflectorId);
            db.setContent(content);
            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

            event.consume();
        });

        cell.setOnDragDone((event) -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                cell.setText("");
            }
            event.consume();
        });
        return cell;
    }

    private Button createRotorPositionCell(String position) {
        final Button label = new Button(position);
        label.setPrefSize(50, 50);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-border-color: gray; -fx-border-width: 3");
        label.setOnDragDetected((event) -> {
            WritableImage snapshot = label.snapshot(new SnapshotParameters(), null);
            Dragboard db = label.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(position);
            db.setContent(content);
            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

            event.consume();
        });

        label.setOnDragDone((event) -> {
            event.consume();
        });
        return label;
    }

    public FlowPane createFlowPane(){
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10.0);
        flowPane.setVgap(10.0);
        flowPane.setPrefHeight(309.0);
        flowPane.setPrefWidth(342.0);
        flowPane.setStyle("-fx-background-color: #969494");
        return flowPane;
    }

    public Button createSetBtn(){
        Button button = new Button();
        button.setId("setBtn");
        button.setText("Set");
        button.setDisable(true);
        return button;
    }
    public void setPlugsHBox(){
        plugsHBox = new HBox(20);
        plugsHBox.setAlignment(Pos.CENTER);
        plugsHBox.setStyle("-fx-background-color: #969494");

        Label HBoxData = new Label("Plugs connection: ");
        HBoxData.setStyle( "-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: Arial");
        plugsHBox.getChildren().add(HBoxData);
    }

    public void creatPlugConnection(int connectionsNumber, List<Character> machineAbc){
        HBox hBox = new HBox(10);
        Button first= createNumberNode(connectionsNumber);
        Button Second= createNumberNode(connectionsNumber);
        setOnMouseClickedPlugsConnection(first,machineAbc);
        setOnMouseClickedPlugsConnection(Second,machineAbc);
        hBox.getChildren().add(first);
        hBox.getChildren().add(Second);
        plugsHBox.getChildren().add(hBox);
    }
    public void setRotorsHBox(int rotorsCount, List<Character> ABC) {
        rotorsHBox = new HBox(10);
        rotorsHBox.setAlignment(Pos.CENTER);
        Label rotorsHBoxLabel = new Label("Rotor's numbers: \n from right to left");
        rotorsHBoxLabel.setStyle( "-fx-font-size: 16px; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: Arial");
        rotorsHBox.getChildren().add(rotorsHBoxLabel);
        rotorsHBox.setStyle("-fx-background-color: #969494");

        rotorsPositionHBox = new HBox(10);
        rotorsPositionHBox.setAlignment(Pos.CENTER);
        Label rotorsPositionHBoxLabel = new Label("Rotor's positions: \n from right to left");
        rotorsHBoxLabel.setStyle( "-fx-font-size: 16px; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: Arial");
        rotorsPositionHBox.getChildren().add(rotorsPositionHBoxLabel);
        rotorsPositionHBox.setStyle("-fx-background-color: #969494");

        for( int i = 0; i < rotorsCount ; i++) {
            final Button cell = createNumberNode(i+1);
            setOnMouseClickedRotorsNumbers(cell);
            rotorsHBox.getChildren().add(cell);
            final Button positionCell = createNumberNode(i+1);
            setOnMouseClickedRotorsPositions(positionCell, ABC);
            rotorsPositionHBox.getChildren().add(positionCell);
        }
    }
    public void setReflectorHBox(){
        reflectorHBox = new HBox(10);
        reflectorHBox.setAlignment(Pos.CENTER);
        reflectorHBox.getChildren().add(new Text("Reflector number: "));
        reflectorHBox.setStyle("-fx-background-color: #969494");

        final Button cell = createNumberNode(1);
        setOnMouseClickedReflectorNumber(cell);
        reflectorHBox.getChildren().add(cell);
    }

    private void setOnMouseClickedReflectorNumber(Button cell){
        cell.setOnMouseClicked((event) -> {
            if(cell.getText() != "") {
                Button newCell = (Button) reflectorFlowPane.getChildren().get(1);
                newCell.setText(cell.getText());
                cell.setText("");
            }
            event.consume();
        });
    }

    private void setOnMouseClickedRotorsNumbers(Button cell){
        cell.setOnMouseClicked((event) -> {
            if(cell.getText() != "") {
                Button newCell = (Button) rotorsNumberFlowPane.getChildren().get(Integer.parseInt(cell.getText()) - 1);
                newCell.setText(cell.getText());
                cell.setText("");
            }
            event.consume();
        });
    }
    private void setOnMouseClickedPlugsConnection(Button cell,List<Character> ABC){
        cell.setOnMouseClicked((event) -> {
            if(cell.getText() != "") {
                int index = ABC.indexOf(cell.getText().charAt(0));
                Button newCell = (Button) plugBoardFlowPane.getChildren().get(index);
                newCell.setText(cell.getText());
                cell.setText("");
            }
            event.consume();
        });
    }

    private void setOnMouseClickedRotorsPositions(Button cell, List<Character> ABC){
        cell.setOnMouseClicked((event) -> {
            if(cell.getText() != "") {
                int index = ABC.indexOf(cell.getText().charAt(0));
                Button newCell = (Button) rotorsPositionFlowPane.getChildren().get(index);
                newCell.setText(cell.getText());
                cell.setText("");
            }
            event.consume();
        });
    }

    private Button createNumberNode(int number) {
        final Button cell = new Button();
        cell.setId(String.valueOf(number));
        cell.setPrefSize(70 ,70);
        cell.setAlignment(Pos.CENTER);
        cell.setStyle("-fx-border-color: gray; -fx-border-width: 3;");


        cell.setOnDragOver((event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        cell.setOnDragEntered((event) -> {
            if (event.getDragboard().hasString()) {
                cell.setStyle("-fx-background-color: #695e5e");
            }
            event.consume();
        });

        cell.setOnDragExited((event) -> {
            cell.setStyle("-fx-border-color: gray; -fx-border-width: 3");
            event.consume();
        });

        cell.setOnDragDropped((event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                cell.setText(db.getString());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        return cell;
    }

    public void buttonClearClicked(MachineAbcDTO machineAbcDTO) {
        resetRotorsNumberHBox();
        resetRotorsPositionHBox(machineAbcDTO);
    }


    public void resetRotorsNumberHBox(){
        Button cell;
        for (int i = 1; i < rotorsHBox.getChildren().size(); i++){
            cell = (Button) rotorsHBox.getChildren().get(i);
            if(cell.getText() != "") {
                Button newCell = (Button) rotorsNumberFlowPane.getChildren().get(Integer.parseInt(cell.getText()) - 1);
                newCell.setText(cell.getText());
                cell.setText("");
            }
        }
    }

    public void resetRotorsPositionHBox(MachineAbcDTO machineAbcDTO){
        Button cell;
        for (int i = 1; i < rotorsPositionHBox.getChildren().size(); i++) {
            cell = (Button) rotorsPositionHBox.getChildren().get(i);
            if(cell.getText() != "") {
                int index = machineAbcDTO.getABC().indexOf(cell.getText().charAt(0));
                Button newCell = (Button) rotorsPositionFlowPane.getChildren().get(index);
                newCell.setText(cell.getText());
                cell.setText("");
            }
        }
    }

    public CodeConfigurationInputDTO getCodeConfigurationInputDTO(){

        return new CodeConfigurationInputDTO(rotorsNumberList, rotorsPositionList,
                reflectorNumber, plugBoardConnectionList);
    }
}
