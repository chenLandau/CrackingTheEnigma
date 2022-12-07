package DataTransferObject;

import java.util.List;

public class CodeConfigurationOutputDTO {
    private List<Integer> rotorsNumber;
    private List<Character> rotorsPosition;
    private List<Integer> notchPosition;
    private String reflectorNumber;
    private List<String> plugBoardConnection;


    public CodeConfigurationOutputDTO(List<Integer> rotorsNumber, List<Character> rotorsPosition, List<Integer> notchPosition, String reflectorNumber, List<String> plugBoardConnection){
        this.rotorsNumber = rotorsNumber;
        this.rotorsPosition = rotorsPosition;
        this.notchPosition = notchPosition;
        this.reflectorNumber = reflectorNumber;
        this.plugBoardConnection = plugBoardConnection;
    }

    public List<Integer> getRotorsNumber() { return rotorsNumber; }
    public List<Character> getRotorsPosition() { return rotorsPosition; }
    public List<Integer> getNotchPosition() { return notchPosition; }
    public String getReflectorNumber() { return reflectorNumber; }
    public List<String> getPlugBoardConnection() { return plugBoardConnection; }


}
