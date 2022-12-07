package utils.Candidates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CandidateController {
    @FXML private Label StringCandidate;
    @FXML private Label codeConfiguration;
    @FXML private Label founderName;

    public void setStringCandidateLabel(String value){
        StringCandidate.setText(value);
    }
    public void setCodeConfigurationLabel(String value){
        codeConfiguration.setText(value);
    }
    public void setFounderName(String founderName){
        this.founderName.setText(founderName);
    }
}
