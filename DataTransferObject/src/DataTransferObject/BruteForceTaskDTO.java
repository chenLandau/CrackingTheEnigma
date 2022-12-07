package DataTransferObject;

public class BruteForceTaskDTO {
    private int amountOfAgents;
    private String level;
    private int missionSize;
    private String outputString;

    public BruteForceTaskDTO(int amountOfAgents, String level, int missionSize, String outputString){
        this.amountOfAgents = amountOfAgents;
        this.level = level;
        this.missionSize = missionSize;
        this.outputString = outputString;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public String getLevel() {
        return level;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public String getOutputString() {
        return outputString;
    }
}
