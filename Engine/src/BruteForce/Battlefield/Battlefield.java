package BruteForce.Battlefield;

import BruteForce.CandidatesManager;
import BruteForce.Dictionary;
import BruteForce.Battlefield.entities.Agent;
import DataTransferObject.*;
import MyExceptions.ContestIsFullException;
import MyExceptions.MachineLogicException;
import MyMachine.Machine;
import MyMachine.Reflector;
import jaxbEnigma.CTEEnigma;
import BruteForce.Battlefield.entities.Allies;
import BruteForce.Battlefield.entities.Uboat;

import java.util.*;

public class Battlefield {


    public enum GameStatus{
        WAITING,
        ACTIVE,
        DONE
    }
    private String battleName;
    private String level;
    private Uboat uboat;
    private Machine machine = new Machine();
    private CodeConfigurationInputDTO lastCodeConfiguration;
    private CodeConfigurationInputDTO lastCodeConfigurationFromUser;
    private Dictionary dictionary = new Dictionary();
    private Set<Allies> activeAlliesSet = new HashSet<>();
    private GameStatus gameStatus = GameStatus.WAITING;
    private int totalAlliesAmount;
    private CandidatesManager uboatCandidatesManager = new CandidatesManager();
    private CandidateDTO winnerTeamDetails = null;
    private CodeConfigurationInputDTO currentCodeConfiguration;


    public Battlefield(CTEEnigma cteEnigma, String usernameFromSession)throws MachineLogicException {
        this.battleName = cteEnigma.getCTEBattlefield().getBattleName();
        this.level = cteEnigma.getCTEBattlefield().getLevel();
        this.totalAlliesAmount = cteEnigma.getCTEBattlefield().getAllies();
        this.uboat = new Uboat(usernameFromSession);
        machine.chargeCteMachineToMyMachine(cteEnigma);
        dictionary.chargeDictionary(cteEnigma.getCTEDecipher().getCTEDictionary(), machine);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
    public int getTotalAlliesAmount() {
        return totalAlliesAmount;
    }
    public String getBattleName() {
        return battleName;
    }

    public String getLevel() {
        return level;
    }

    public Uboat getUboat() {
        return uboat;
    }

    public Machine getMachine() {
        return machine;
    }
    public Dictionary getDictionary() {
        return dictionary;
    }
    public CandidateDTO getWinnerTeamDetails() {
        return winnerTeamDetails;
    }
//    public CodeConfigurationInputDTO getCodeConfigurationInputDTO() {
//        return lastCodeConfiguration;
//    }
    public void setCodeConfigurationInputDTO(CodeConfigurationInputDTO codeConfiguration) { lastCodeConfiguration = codeConfiguration ; }

    public Set<Allies> getActiveAlliesSet() {return activeAlliesSet;}
    public boolean isContestFull() {
        return activeAlliesSet.size() == totalAlliesAmount;
    }

    public synchronized void resetMachine() {
        machine.resetMachine();
        lastCodeConfiguration = lastCodeConfigurationFromUser;
        currentCodeConfiguration = lastCodeConfigurationFromUser;
    }
    public String encryptInputString(String inputString, String inputStringWithoutExcludedChars) {
        lastCodeConfiguration = getLastCodeConfigurationInputDTO();
        String outputString = machine.getEncryptedString(inputStringWithoutExcludedChars);
        currentCodeConfiguration = getLastCodeConfigurationInputDTO();
        uboat.setInputString(inputString);
        uboat.setOutputString(outputString);
       // battlefield.stringEncrypted();
        return outputString;
    }

//    public void stringEncrypted() {
//        lastCodeConfiguration = currentCodeConfiguration;
//        CodeConfigurationOutputDTO codeConfigurationOutputDTO = getCurrentCodeConfigurationOutputDTO();
//        currentCodeConfiguration = new CodeConfigurationInputDTO(codeConfigurationOutputDTO.getRotorsNumber(),
//                codeConfigurationOutputDTO.getRotorsPosition(), codeConfigurationOutputDTO.getReflectorNumber(),
//                codeConfigurationOutputDTO.getPlugBoardConnection());
//    }

    public ManualCodeConfigurationDTO getManualCodeConfigurationDTO() {
        Map<Integer,String> reflectorsData = new HashMap<>();

        for(Map.Entry<Reflector.RomanNumber, Reflector> pair: machine.getTotalReflectors().entrySet()) {
            reflectorsData.put(pair.getKey().ordinal() + 1, pair.getKey().toString());
        }
        return new ManualCodeConfigurationDTO(machine.getRotorsCount(),machine.getTotalRotors().size(),
                machine.getTotalReflectors().size(),reflectorsData,machine.getABC());
    }

    public CodeConfigurationInputDTO getLastCodeConfigurationInputDTO() {
        List<Integer> notchPosition = new ArrayList<>();
        List<Character> rotorsPosition = new ArrayList<>();
        char currentRotorPosition;
        try {
            for (int i = 0; i < machine.getRotorsInUse().size(); i++) {
                currentRotorPosition = machine.getRotorsInUse().get(i).getRotorPositions().get(0).right;
                rotorsPosition.add(currentRotorPosition);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return new CodeConfigurationInputDTO(lastCodeConfigurationFromUser.getRotorsNumber(),rotorsPosition,
                lastCodeConfigurationFromUser.getReflectorNumber(),lastCodeConfigurationFromUser.getPlugBoardConnection());
    }
    public CodeConfigurationOutputDTO getCurrentCodeConfigurationOutputDTO() {
        List<Integer> notchPosition = new ArrayList<>();
        List<Character> rotorsPosition = new ArrayList<>();
        int currentNotchPosition;
        char currentRotorPosition;
        try {
            for (int i = 0; i < machine.getRotorsInUse().size(); i++) {
                currentNotchPosition = machine.getRotorsInUse().get(i).getCurrentNotchRightPosition();
                currentRotorPosition = machine.getRotorsInUse().get(i).getRotorPositions().get(0).right;
                notchPosition.add(currentNotchPosition);
                rotorsPosition.add(currentRotorPosition);
            }
        } catch (MachineLogicException e){
            e.printStackTrace();
        }

        return new CodeConfigurationOutputDTO(lastCodeConfigurationFromUser.getRotorsNumber(),rotorsPosition,notchPosition,
                lastCodeConfigurationFromUser.getReflectorNumber(),lastCodeConfigurationFromUser.getPlugBoardConnection());
    }

    public DictionaryWordsDTO getDictionaryWordsDTO() {
        return new DictionaryWordsDTO(dictionary.getValidDictionaryWords());
    }

    public void initializeManualCodeConfiguration(CodeConfigurationInputDTO codeConfiguration) {
        try {
            machine.setCodeConfiguration(codeConfiguration.getRotorsNumber(), codeConfiguration.getRotorsPosition(),
                    codeConfiguration.getReflectorNumber(), codeConfiguration.getPlugBoardConnection());
            lastCodeConfigurationFromUser = codeConfiguration;
            lastCodeConfiguration = codeConfiguration;
            currentCodeConfiguration = codeConfiguration;
        }
        catch (MachineLogicException e) {
            e.printStackTrace();
        }
    }

    public MachineSpecificationsDTO getMachineSpecificationsDTO() {
        return new MachineSpecificationsDTO(machine.getTotalRotors().size(),machine.getRotorsCount(),
                machine.getTotalReflectors().size(),machine.getDecryptedMessagesAmount());
    }

    public synchronized Set<AllyTeamDTO> getAlliesTeamsListDTO() {
        Set<AllyTeamDTO> alliesTeamsList = new HashSet<>();
        for (Allies allyTeam : activeAlliesSet) {
            alliesTeamsList.add(new AllyTeamDTO(allyTeam.getAlliesUserName(), allyTeam.getAgentsAmount(), allyTeam.getMissionSize()));
        }
        return alliesTeamsList;
    }

    public synchronized UboatDTO getUboatDTO() {
          return new UboatDTO(this);
    }
    public void setFileContentString(String fileContentString) {
        uboat.setFileContentString(fileContentString);
    }
    public String getFileContentString() {
        return uboat.getFileContentString();
    }

    public synchronized void addAllyToUboat(Allies ally, int allyMissionSize) throws ContestIsFullException {
        ally.setContestOverConfirmed(false);
        if(!isContestFull()){
            ally.setMissionSize(allyMissionSize);
            activeAlliesSet.add(ally);
        }else {
            throw new ContestIsFullException();
        }
    }

    public synchronized Boolean checkIfGameStatusActive() {
        return gameStatus == GameStatus.ACTIVE;
    }
    public synchronized void setGameActive() {
        boolean isAllAlliesReady = true;
        if(uboat.isUserReady() && totalAlliesAmount == activeAlliesSet.size()){
            for(Allies ally : activeAlliesSet){
                if(!ally.isUserReady()){
                    isAllAlliesReady = false;
                    break;
                }
            }
            if(isAllAlliesReady)
                gameStatus = GameStatus.ACTIVE;
        }
    }

    public void addNewCandidates(List<CandidateDTO> candidatesDTOList) {
        uboatCandidatesManager.addNewCandidates(candidatesDTOList);
    }

    public CandidatesAndVersionForUboatDTO getCandidatesAndVersionDTO(int candidateVersion) {
        CandidatesAndVersionDTO candidatesAndVersionDTO = uboatCandidatesManager.getCandidatesAndVersionDTO(candidateVersion);
        Boolean isWinnerExist = false;
        for(CandidateDTO candidate : candidatesAndVersionDTO.getCandidates()){
            if(checkIfWinnerExist(candidate)){
                winnerTeamDetails = new CandidateDTO(candidate);
                gameStatus = GameStatus.DONE;
                agentsUnionOnAlliesTeams();
                isWinnerExist = true;
                break;
            }
        }

        return new CandidatesAndVersionForUboatDTO(candidatesAndVersionDTO.getCandidates(), candidatesAndVersionDTO.getVersion(),
                isWinnerExist);
    }

    private void agentsUnionOnAlliesTeams() {
        for (Allies allyTeam : activeAlliesSet) {
            allyTeam.agentUnion();

        }
    }

    private boolean checkIfWinnerExist(CandidateDTO candidate){
        return candidate.getEncryptedString().equals(uboat.getInputString())
            && candidate.getRotorsNumber().equals(lastCodeConfiguration.getRotorsNumber())
                && candidate.getRotorsPosition().equals(lastCodeConfiguration.getRotorsPosition())
            && candidate.getReflectorNumber().equals(lastCodeConfiguration.getReflectorNumber());
    }

    public void addAgentToAllyTeam(AgentLoginDTO agentLoginDTO, Allies ally, Agent agent) {
        agent.setAgentDetails(agentLoginDTO.getAllyName(), agentLoginDTO.getThreadsAmount(), agentLoginDTO.getMissionsAmount());
        ally.addAgentToAllyTeam(agent, gameStatus == GameStatus.ACTIVE);
    }

    public void ResetCurrentContest() {
        for(Allies ally : activeAlliesSet)
            ally.setUserReady(false);
        activeAlliesSet.clear();
        gameStatus = GameStatus.WAITING;
        uboat.ResetCurrentContest();
        uboatCandidatesManager.ResetCurrentContest();
        winnerTeamDetails = null;
    }

}

