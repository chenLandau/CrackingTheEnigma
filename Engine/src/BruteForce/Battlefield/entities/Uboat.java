package BruteForce.Battlefield.entities;


import java.util.HashSet;
import java.util.Set;

public class Uboat {
    private String uboatUserName;
    private String outputString;
    private String inputString;
    private boolean isUserReady = false;
    private String fileContentString;

    public Uboat(String uboatName) {
        this.uboatUserName = uboatName;
    }
    public String getOutputString() {
        return outputString;
    }

    public String getInputString() {
        return inputString;
    }

    public boolean isUserReady(){
        return isUserReady;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }

    public void setFileContentString(String fileContentString) {
        this.fileContentString = fileContentString;
    }
    public String getFileContentString() {
        return fileContentString;
    }
    public void setUserReady(boolean userReady) {
        isUserReady = userReady;
    }
    public String getUboatName() {
        return uboatUserName;
    }

    public void ResetCurrentContest() {
        outputString = "";
        inputString = "";
        isUserReady = false;
    }
}
