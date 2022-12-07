package DataTransferObject;

import java.util.List;

public class AgentMissionDTO {
    private List<List<Character>> taskList;
    private List<Integer> rotorsInUse;
    private String reflectorInUse;
    private String decryptedString;

    public AgentMissionDTO(List<List<Character>> taskList, List<Integer> rotorInUseList,
                           String reflectorInUse, String decryptedString) {
        this.taskList = taskList;
        this.rotorsInUse = rotorInUseList;
        this.reflectorInUse = reflectorInUse;
        this.decryptedString = decryptedString;
    }

    public List<List<Character>> getTaskList() {
        return taskList;
    }

    public List<Integer> getRotorsInUse() {
        return rotorsInUse;
    }

    public String getReflectorInUse() {
        return reflectorInUse;
    }
    public String getDecryptedString() { return decryptedString; }

}
