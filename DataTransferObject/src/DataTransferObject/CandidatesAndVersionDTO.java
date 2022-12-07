package DataTransferObject;

import java.util.List;

public class CandidatesAndVersionDTO {
    private List<CandidateDTO> candidates;
    private int version;

    public CandidatesAndVersionDTO(List<CandidateDTO> candidates, int version) {
        this.candidates = candidates;
        this.version = version;
    }

    public int getVersion() {return version;}

    public List<CandidateDTO> getCandidates() {return candidates;}
}

