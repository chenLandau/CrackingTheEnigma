package BruteForce.DecryptionManager;

import BruteForce.CandidatesManager;
import BruteForce.Lock;
import DataTransferObject.*;
import MyMachine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DecryptionManager {
    private Thread taskGeneratorThread;
    private TaskGenerator taskGenerator;
    private BlockingQueue<AgentMissionDTO> agentBlockingQueue = new ArrayBlockingQueue<>(1000);
    private Lock isTaskCompleted = new Lock(false);
    private Lock doneGenerateTasksLocker = new Lock(false);
    private String decryptedString;
    private CandidatesManager candidatesManager = new CandidatesManager();

    public void setIsTaskCompleted(Boolean isTaskCompleted) {
        this.isTaskCompleted.setLock(isTaskCompleted);
    }

    public DecryptionManager(int missionSize, Machine enigmaMachine, String level){
        this.taskGenerator = new TaskGenerator(agentBlockingQueue, isTaskCompleted, doneGenerateTasksLocker,
                missionSize, enigmaMachine, level);
    }
    public void startTaskGeneratorServlet() {
       this.taskGeneratorThread = new Thread(taskGenerator);
       this.taskGeneratorThread.start();
    }

    public List<AgentMissionDTO> getAgentMissionsList(int missionAmount) {
        List<AgentMissionDTO> agentMissionList = new ArrayList<>();
        if(!agentBlockingQueue.isEmpty() && !isTaskCompleted.getLock()) {
            synchronized (agentBlockingQueue) {
                for (int i = 0; i < missionAmount && !agentBlockingQueue.isEmpty(); i++) {
                    try {
                        agentMissionList.add(agentBlockingQueue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(agentBlockingQueue.size() <= 200)
                    agentBlockingQueue.notifyAll();
            }
        }
            return agentMissionList;
    }



    public void addNewCandidates(List<CandidateDTO> candidatesDTOList) {
        candidatesManager.addNewCandidates(candidatesDTOList);
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
        this.taskGenerator.setDecryptedString(decryptedString);
    }

    public CandidatesAndVersionDTO getCandidatesAndVersionDTO(int candidateVersion) {
        return candidatesManager.getCandidatesAndVersionDTO(candidateVersion);
    }

    public ContestStatusDTO getContestStatusDTO() {
        return new ContestStatusDTO(isTaskCompleted.getLock(), doneGenerateTasksLocker.getLock());
    }
}
