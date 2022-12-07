package Allies.component.DashboardScreen.ContestDataArea;

import DataTransferObject.UboatDTO;
import javafx.beans.property.SimpleStringProperty;

public class UboatString {
        private SimpleStringProperty battleFieldName;
        private SimpleStringProperty uboatName;
        private SimpleStringProperty gameStatus;
        private SimpleStringProperty level;
        private SimpleStringProperty alliesAmount;

    public UboatString(UboatDTO uboat) {
        this.battleFieldName = new SimpleStringProperty(uboat.getBattleName());
        this.uboatName = new SimpleStringProperty(uboat.getUboatName());
        this.gameStatus = new SimpleStringProperty(uboat.getGameStatus());
        this.level = new SimpleStringProperty(uboat.getLevel());
        this.alliesAmount = new SimpleStringProperty(uboat.getActiveAlliesAmount() +"/" + uboat.getTotalAlliesAmount());
    }

    public String getBattleFieldName() { return battleFieldName.get(); }

    public void setBattleFieldName(String battleFieldName) { this.battleFieldName.set(battleFieldName); }

    public String getUboatName() { return uboatName.get(); }

    public void setUboatName(String uboatName) { this.uboatName.set(uboatName); }

    public String getGameStatus() { return gameStatus.get(); }

    public void setGameStatus(String gameStatus) { this.gameStatus.set(gameStatus); }

    public String getLevel() { return level.get(); }

    public void setLevel(String level) { this.level.set(level); }

    public String getAlliesAmount() { return alliesAmount.get(); }

    public void setAlliesAmount(String alliesAmount) { this.alliesAmount.set(alliesAmount); }
}
