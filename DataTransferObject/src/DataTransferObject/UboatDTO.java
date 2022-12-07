package DataTransferObject;

import BruteForce.Battlefield.Battlefield;

public class UboatDTO {
    private String battleName;
    private String uboatName;
    private String gameStatus;
    private String level;
    private int totalAlliesAmount;
    private int activeAlliesAmount;

    public UboatDTO(String battleName, String uboatName, String gameStatus, String level, int totalAlliesAmount, int activeAlliesAmount) {
        this.battleName = battleName;
        this.uboatName = uboatName;
        this.gameStatus = gameStatus;
        this.level = level;
        this.totalAlliesAmount = totalAlliesAmount;
        this.activeAlliesAmount = activeAlliesAmount;
        //this.AlliesAmount = activeAlliesAmount + "/" + totalAlliesAmount;
    }
    public UboatDTO(Battlefield battlefield) {
        this.battleName = battlefield.getBattleName();
        this.uboatName = battlefield.getUboat().getUboatName();
        this.gameStatus = battlefield.getGameStatus().toString();
        this.level = battlefield.getLevel();
        this.totalAlliesAmount =battlefield.getTotalAlliesAmount();
        this.activeAlliesAmount = battlefield.getActiveAlliesSet().size();
    }

    public String getBattleName() { return battleName; }
    public String getUboatName() {
        return uboatName;
    }
    public String getGameStatus() {
        return gameStatus;
    }
    public String getLevel() { return level; }
    public int getActiveAlliesAmount() { return activeAlliesAmount; }
    public int getTotalAlliesAmount() { return totalAlliesAmount; }
}
