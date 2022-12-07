package Agent.AgentUI.component.ContestScreen.AgentCandidates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StringCandidateController {
    @FXML private Label StringCandidate;
    @FXML private Label codeConfiguration;
    @FXML private Label allyName;

    public void setStringCandidateLabel(String value){
        StringCandidate.setText(value);
    }
    public void setCodeConfigurationLabel(String value){
        codeConfiguration.setText(value);
    }
    public void setAllyNameLabel(String value){
        allyName.setText(value);
    }
}
