package DataTransferObject;

import java.util.List;

public class BruteForceCodeConfiguration {
    private List<Integer> rotorsNumberList;
    private List<Character> rotorsPositionList;
    private List<Integer> notchPositionList;
    private String reflectorId;

    public BruteForceCodeConfiguration(List<Integer> rotorsNumberList,List<Character> rotorsPositionList,List<Integer> notchPositionList,String reflectorId){
        this.rotorsNumberList = rotorsNumberList;
        this.rotorsPositionList = rotorsPositionList;
        this.notchPositionList = notchPositionList;
        this.reflectorId = reflectorId;
    }
    public List<Integer> getRotorsNumber() { return rotorsNumberList; }
    public List<Character> getRotorsPosition() { return rotorsPositionList; }
    public List<Integer> getNotchPosition() { return notchPositionList; }
    public String getReflectorNumber() { return reflectorId; }

}
