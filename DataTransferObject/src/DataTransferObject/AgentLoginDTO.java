package DataTransferObject;

    public class AgentLoginDTO {
    private String allyName;
    private int threadsAmount;
    private int missionsAmount;

    public AgentLoginDTO(String allyName, int threadsAmount, int missionsAmount) {
        this.allyName = allyName;
        this.threadsAmount = threadsAmount;
        this.missionsAmount = missionsAmount;
    }

    public String getAllyName() {
        return allyName;
    }

    public int getThreadsAmount() {
        return threadsAmount;
    }

    public int getMissionsAmount() {
        return missionsAmount;
    }
}
