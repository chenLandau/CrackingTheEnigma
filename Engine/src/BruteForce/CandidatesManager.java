package BruteForce;

import DataTransferObject.CandidateDTO;
import DataTransferObject.CandidatesAndVersionDTO;
import DataTransferObject.CandidatesAndVersionForUboatDTO;

import java.util.ArrayList;
import java.util.List;

public class CandidatesManager {
    private final List<CandidateDTO> candidateDecodedStringList = new ArrayList<>();

    public CandidatesManager(){}

    public synchronized List<CandidateDTO> getCandidates(int fromIndex){
        if (fromIndex < 0 || fromIndex > candidateDecodedStringList.size()) {
            fromIndex = 0;
        }
        return candidateDecodedStringList.subList(fromIndex, candidateDecodedStringList.size());
    }

    public synchronized void addNewCandidates(List<CandidateDTO> candidatesDTOList) {
        candidateDecodedStringList.addAll(candidatesDTOList);
    }

    public synchronized CandidatesAndVersionDTO getCandidatesAndVersionDTO(int candidateVersion) {
        int candidateManagerVersion = candidateDecodedStringList.size();
        List<CandidateDTO> candidates = getCandidates(candidateVersion);

        return new CandidatesAndVersionDTO(candidates, candidateManagerVersion);
    }

    public void ResetCurrentContest(){ candidateDecodedStringList.clear(); }
}
