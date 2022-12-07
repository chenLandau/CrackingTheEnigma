package DataTransferObject;

import java.util.List;

public class CandidatesAndVersionForUboatDTO {
     private List<CandidateDTO> candidates;
     private int version;
     private boolean isWinnerExist;

    public CandidatesAndVersionForUboatDTO(List<CandidateDTO> candidates, int version, boolean isWinnerExist) {
        this.candidates = candidates;
        this.version = version;
        this.isWinnerExist = isWinnerExist;
    }

    public int getVersion() {return version;}

    public List<CandidateDTO> getCandidates() {return candidates;}

    public boolean isWinnerExist() {
        return isWinnerExist;
    }
}
