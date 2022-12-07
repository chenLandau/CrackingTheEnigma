package DataTransferObject;

import java.util.List;

public class CandidateDTO {
    private List<Integer> rotorsNumberList;
    private List<Character> rotorsPositionList;
    private List<Integer> notchPositionList;
    private String reflectorId;
    private String encryptedString;
    private String allyName;
    private String agentName;

    public CandidateDTO(CandidateDTO candidateDTO){
        this.rotorsNumberList = candidateDTO.getRotorsNumber();
        this.rotorsPositionList = candidateDTO.getRotorsPosition();
        this.notchPositionList = candidateDTO.getNotchPosition();
        this.reflectorId = candidateDTO.getReflectorNumber();
        this.encryptedString = candidateDTO.getEncryptedString();
        this.allyName = candidateDTO.getAllyName();
        this.agentName = candidateDTO.getAgentName();
    }

    public CandidateDTO(String encryptedString, String allyName, List<Integer> rotorsNumberList,
                        List<Character> rotorsPositionList,List<Integer> notchPositionList,String reflectorId, String agentName){
        this.rotorsNumberList = rotorsNumberList;
        this.rotorsPositionList = rotorsPositionList;
        this.notchPositionList = notchPositionList;
        this.reflectorId = reflectorId;
        this.encryptedString = encryptedString;
        this.allyName = allyName;
        this.agentName = agentName;
    }
    public List<Integer> getRotorsNumber() { return rotorsNumberList; }
    public List<Character> getRotorsPosition() { return rotorsPositionList; }
    public List<Integer> getNotchPosition() { return notchPositionList; }
    public String getReflectorNumber() { return reflectorId; }
    public String getEncryptedString(){
        return encryptedString;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getAgentName() {return agentName;}
}
