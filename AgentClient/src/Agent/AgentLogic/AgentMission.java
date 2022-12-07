package Agent.AgentLogic;

import Agent.AgentUI.main.utils.UIAdapter;
import BruteForce.Dictionary;
import DataTransferObject.AgentMissionDTO;
import DataTransferObject.CandidateDTO;
import MyMachine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AgentMission implements Runnable {
    private BlockingQueue<List<CandidateDTO>> candidateDecodedString;
    private String decryptedString;
    private List<List<Character>> taskList;
    private Machine enigma;
    private Dictionary dictionary;
    private String allyName;
    private List<Integer> rotorInUse;
    private String reflectorInUse;
    private AgentMissionCounter agentMissionCounter;
    private UIAdapter uiAdapter;
    private  String agentName;
    public AgentMission(Machine enigma, Dictionary dictionary, AgentMissionDTO agentMissionDTO,
                        BlockingQueue<List<CandidateDTO>> candidateDecodedString, String allyName,
                        AgentMissionCounter agentMissionCounter, UIAdapter uiAdapter, String agentName) {
        this.decryptedString = agentMissionDTO.getDecryptedString();
        this.taskList = agentMissionDTO.getTaskList();
        this.enigma = enigma;
        this.dictionary = dictionary;
        this.candidateDecodedString = candidateDecodedString;
        this.rotorInUse = agentMissionDTO.getRotorsInUse();
        this.reflectorInUse = agentMissionDTO.getReflectorInUse();
        this.allyName = allyName;
        this.agentMissionCounter = agentMissionCounter;
        this.uiAdapter = uiAdapter;
        this.agentName = agentName;
    }

    @Override
    public void run() {
        try {
            enigma.setRotorsInUse(rotorInUse);
            enigma.setReflector(reflectorInUse);
            List<CandidateDTO> candidateDecodedStringList = new ArrayList<>();
            String outputString;
            for (int i = 0; i < taskList.size(); i++) {
                List<Character> singleTask = taskList.get(i);
                enigma.setRotorsPositions(singleTask);
                outputString = enigma.getEncryptedString(decryptedString);
                String[] outputStringArray = outputString.split(" ");
                boolean isExist = true;

                for (String str : outputStringArray) {
                    if (!dictionary.getValidDictionaryWords().contains(str)) {
                        isExist = false;
                        break;
                    }
                }

                if (isExist) {
                    CandidateDTO decodedStringData = new CandidateDTO(outputString, allyName,
                            enigma.getRotorsInUseIntegerList(), singleTask, enigma.getNotchPositionList(),
                            enigma.getReflectorInUse().getId().toString(), agentName);
                    candidateDecodedStringList.add(decodedStringData);
                }
                enigma.resetMachine();
            }

            if(!candidateDecodedStringList.isEmpty()) {
                synchronized (candidateDecodedString) {
                    candidateDecodedString.put(candidateDecodedStringList);
                }
            }

            agentMissionCounter.missionDone(candidateDecodedStringList.size());
            uiAdapter.setTotalMissionsCompletedDelegate(agentMissionCounter.getMissionsCompleted());
            uiAdapter.setCurrentAmountOfMissionOnQueueDelegate(agentMissionCounter.getMissionsOnQueue());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
