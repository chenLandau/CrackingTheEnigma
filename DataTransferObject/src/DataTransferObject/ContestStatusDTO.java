package DataTransferObject;

import BruteForce.Lock;

public class ContestStatusDTO {
    private boolean isTaskCompleted;
    private boolean doneGenerateTasks;

    public ContestStatusDTO(boolean isTaskCompleted, boolean doneGenerateTasks){
        this.isTaskCompleted = isTaskCompleted;
        this.doneGenerateTasks = doneGenerateTasks;
    }

    public boolean isDoneGenerateTasks() {
        return doneGenerateTasks;
    }

    public boolean isTaskCompleted() {
        return isTaskCompleted;
    }
}
