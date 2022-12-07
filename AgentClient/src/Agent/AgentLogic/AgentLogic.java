package Agent.AgentLogic;

import Agent.AgentUI.main.utils.UIAdapter;
import BruteForce.Dictionary;
import DataTransferObject.AgentMissionDTO;
import DataTransferObject.CandidateDTO;
import DataTransferObject.ContestStatusDTO;
import MyExceptions.MachineLogicException;
import MyMachine.Machine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jaxbEnigma.CTEEnigma;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgentLogic {
    private final static String JAXB_XML_CTE_ENIGMA_PACKAGE_NAME = "jaxbEnigma";
    private Machine enigmaMachine;
    private Dictionary dictionary;
    private String userName;
    private String allyName;
    private int threadsAmount;
    private int missionsAmount;
    private ExecutorService threadExecutor;
    private BlockingQueue<List<CandidateDTO>> candidateDecodedString;
    private Timer timer;
    private Timer timer1;
    private TimerTask GetMissionsRefresher;
    private TimerTask SetAgentProgressRefresher;
    private TimerTask CheckGameStatusRefresher;
    private BooleanProperty shouldUpdateMissions;
    private BooleanProperty shouldUpdateCandidates;
    private AgentMissionCounter agentMissionCounter;
    private UIAdapter uiAdapter;


    public AgentLogic() {
        this.enigmaMachine = new Machine();
        this.dictionary = new Dictionary();
        this.candidateDecodedString = new ArrayBlockingQueue<>(1000);
        this.shouldUpdateMissions = new SimpleBooleanProperty();
        this.shouldUpdateCandidates = new SimpleBooleanProperty();
        this.agentMissionCounter = new AgentMissionCounter();
    }

    public void setAgentLogicDetails(int threadsAmount, int missionsAmount, String allyName) {
        this.threadsAmount = threadsAmount;
        this.missionsAmount = missionsAmount;
        this.allyName = allyName;
    }

    private void updateAgentMissions(List<AgentMissionDTO> agentMissionDTOList) {
       setThreadExecutor(agentMissionDTOList);
    }

    private void updateGameStatus(ContestStatusDTO contestStatusDTO) {
       shouldUpdateMissions.set(!contestStatusDTO.isDoneGenerateTasks() && !contestStatusDTO.isTaskCompleted());
       shouldUpdateCandidates.set(!contestStatusDTO.isTaskCompleted());
    }

    public void startGetMissionsRefresher() {
        this.threadExecutor = Executors.newFixedThreadPool(threadsAmount);
        GetMissionsRefresher = new GetMissionsRefresher(this::updateAgentMissions
                ,shouldUpdateMissions, agentMissionCounter);
        timer = new Timer();
        timer.schedule(GetMissionsRefresher, 4000, 4000);
    }

    public void startCheckGameStatusRefresher() {
        CheckGameStatusRefresher = new CheckGameStatusRefresher(this::updateGameStatus
                ,shouldUpdateMissions);
        timer = new Timer();
        timer.schedule(CheckGameStatusRefresher, 4000, 4000);
    }

    public void startSetAgentProgressRefresher() {
        SetAgentProgressRefresher = new SetAgentProgressRefresher(agentMissionCounter, userName);
        timer1 = new Timer();
        timer1.schedule(SetAgentProgressRefresher, 4000, 4000);
    }

    public void setThreadExecutor(List<AgentMissionDTO> agentsMissions) {
        agentMissionCounter.addMissionsToTotalAmountOfMissionTaken(agentsMissions.size());
        uiAdapter.setTotalNumberOfMissionsTakenFromQueue(agentMissionCounter.getTotalAmountOfMissionTaken());
        agentMissionCounter.setMissionsCounter(agentsMissions.size());
        for (AgentMissionDTO agentMissionDTO : agentsMissions) {
            threadExecutor.submit(new AgentMission(enigmaMachine.clone(), dictionary, agentMissionDTO,
                    candidateDecodedString, allyName, agentMissionCounter, uiAdapter, userName));
        }
    }

    public void readCteMachineFromXml(String inputStream) throws JAXBException, MachineLogicException {
        CTEEnigma cteEnigma = deserializeFrom(inputStream);
        enigmaMachine.chargeCteMachineToMyMachine(cteEnigma);
        dictionary.chargeDictionary(cteEnigma.getCTEDecipher().getCTEDictionary(), enigmaMachine);
    }

    public CTEEnigma deserializeFrom(String in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_CTE_ENIGMA_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(new StringReader(in));
    }

    public void gameOn(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        this.agentMissionCounter.setUiAdapter(uiAdapter);
        shouldUpdateMissions.set(true);
        shouldUpdateCandidates.set(true);
        new Thread(new DecodedStringTask(candidateDecodedString, uiAdapter, shouldUpdateCandidates)).start();
        startGetMissionsRefresher();
        startCheckGameStatusRefresher();
        startSetAgentProgressRefresher();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void contestOverConfirmed() {
        candidateDecodedString.clear();
        agentMissionCounter.contestOverConfirmed();

    }
}