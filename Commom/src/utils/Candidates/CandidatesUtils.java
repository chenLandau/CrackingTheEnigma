package utils.Candidates;

import DataTransferObject.BruteForceCodeConfiguration;
import DataTransferObject.CandidateDTO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CandidatesUtils {

    public Node createNewCandidateStringTileForUboat(CandidateDTO candidateDTO){
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url1 = getClass().getResource("Candidate.fxml");
            loader.setLocation(url1);
            Node singleCandidateTile = loader.load();
            CandidateController candidateController = loader.getController();
            candidateController.setStringCandidateLabel(candidateDTO.getEncryptedString());
            candidateController.setCodeConfigurationLabel(createCandidateLabelCodeConfiguration(candidateDTO));
            candidateController.setFounderName(candidateDTO.getAllyName());

            return singleCandidateTile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Node createNewCandidateStringTileForAlly(CandidateDTO candidateDTO){
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url1 = getClass().getResource("Candidate.fxml");
            loader.setLocation(url1);
            Node singleCandidateTile = loader.load();
            CandidateController candidateController = loader.getController();
            candidateController.setStringCandidateLabel(candidateDTO.getEncryptedString());
            candidateController.setCodeConfigurationLabel(createCandidateLabelCodeConfiguration(candidateDTO));
            candidateController.setFounderName(candidateDTO.getAgentName());

            return singleCandidateTile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String createCandidateLabelCodeConfiguration(CandidateDTO candidateDTO) {
        StringBuilder CodeConfiguration = new StringBuilder();
        String RotorsNumber = BuildRotorsNumberString(candidateDTO.getRotorsNumber());
        String RotorsFirstPosition = BuildRotorsPositionStringForCandidate(candidateDTO.getRotorsPosition(), candidateDTO.getNotchPosition());
        CodeConfiguration.append(RotorsNumber).append(RotorsFirstPosition);
        CodeConfiguration.append("<").append(candidateDTO.getReflectorNumber()).append(">");

        return CodeConfiguration.toString();
    }

    private String BuildRotorsNumberString(List<Integer> rotorsNumber) {
        StringBuilder rotorsNumberStr;
        rotorsNumberStr = new StringBuilder("<");
        for (int i = rotorsNumber.size() - 1; i >= 0; i--) {
            rotorsNumberStr.append(rotorsNumber.get(i).toString());
            if (i == 0)
                rotorsNumberStr.append(">");
            else
                rotorsNumberStr.append(",");
        }
        return rotorsNumberStr.toString();
    }
    private String BuildRotorsPositionStringForCandidate(List<Character> rotorsPosition, List<Integer> notchPosition) {
        StringBuilder rotorsPositionStr;
        rotorsPositionStr = new StringBuilder("<");
        for (int i = rotorsPosition.size() - 1; i >= 0; i--) {
            rotorsPositionStr.append(rotorsPosition.get(i).toString());
        }
        rotorsPositionStr.append(">");
        return rotorsPositionStr.toString();
    }
}
