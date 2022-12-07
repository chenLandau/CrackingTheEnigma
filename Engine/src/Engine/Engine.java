package Engine;

import BruteForce.Battlefield.Battlefield;
import BruteForce.DecryptionManager.DecryptionManager;
import BruteForce.Dictionary;
import BruteForce.Battlefield.entities.Allies;
import DataTransferObject.*;
import MyExceptions.*;
import jaxbEnigma.CTEEnigma;
import MyMachine.Machine;
import MyMachine.Reflector;
import ValidityChecker.ValidityChecker;
import usersManager.UsersManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.*;

public class Engine implements EngineManager {
    private final static String JAXB_XML_CTE_ENIGMA_PACKAGE_NAME = "jaxbEnigma";
    private Map<String, Battlefield> battleFieldMap = new HashMap<>();
    private Map<String, DecryptionManager> decryptionManagerMap = new HashMap<>();
    private Map<String, String> uboatToBattleFieldMap = new HashMap<>();
    private Map<String, String> allyToBattleFieldMap = new HashMap<>();
    private UsersManager usersManager = new UsersManager();

    public Engine() {}

    public UsersManager getUsersManager() {
        return usersManager;
    }

    @Override
    public CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_CTE_ENIGMA_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    @Override
    public synchronized void addAllyToDecryptionManagerMap(String allyUserName, int missionSize, String battleFieldName) {
        Battlefield battlefield = battleFieldMap.get(battleFieldName);
        decryptionManagerMap.put(allyUserName, new DecryptionManager(missionSize, battlefield.getMachine(),
                battlefield.getLevel()));
    }

    @Override
    public String readCteMachineFromXml(InputStream inputStream, String uboatName) throws JAXBException, XmlException, MachineLogicException {
        CTEEnigma cteEnigma = deserializeFrom(inputStream);
        ValidityChecker.checkCteEnigmaValidity(cteEnigma, battleFieldMap.keySet());
        Battlefield battlefield = new Battlefield(cteEnigma, uboatName);
        battleFieldMap.put(battlefield.getBattleName(), battlefield);
        uboatToBattleFieldMap.put(uboatName,battlefield.getBattleName());
        return battlefield.getBattleName();
    }

    @Override
    public synchronized String encryptInputString(String inputString, String uboatUserName) throws StringContainInvalidWordsException {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName));
        inputString = inputString.toUpperCase();
        String inputStringWithoutExcludedChars = inputString.replaceAll(battlefield.getDictionary().getExcludedChars(), "");
        checkInputStringValidation(inputStringWithoutExcludedChars, battlefield.getDictionary());
        String outputString = battlefield.encryptInputString(inputString,inputStringWithoutExcludedChars);
        return outputString;

    }

    private void checkInputStringValidation(String inputString, Dictionary dictionary) throws StringContainInvalidWordsException {
        String[] inputStringArr = inputString.split(" ");
        String exceptionString = "";

        for (String str : inputStringArr) {
            if (!dictionary.getValidDictionaryWords().contains(str)) {
                exceptionString += str + " ";
            }
        }

        if (!exceptionString.isEmpty())
            throw new StringContainInvalidWordsException(exceptionString);
    }

    @Override
    public void startTaskGeneratorServlet(String allyName) {
        String battleFieldName = allyToBattleFieldMap.get(allyName);
        Battlefield battlefield = battleFieldMap.get(battleFieldName);
        DecryptionManager decryptionManager = decryptionManagerMap.get(allyName);
        decryptionManager.setDecryptedString(battlefield.getUboat().getOutputString());
        decryptionManager.startTaskGeneratorServlet();
    }

    @Override
    public List<AgentMissionDTO> getAgentMissionsList(String agentName, String allyName) {
        return decryptionManagerMap.get(allyName).getAgentMissionsList(usersManager.getAgent(agentName).getMissionsAmount());
    }

    @Override
    public synchronized void initializeManualCodeConfiguration(CodeConfigurationInputDTO codeConfiguration, String userName) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(userName));
        battlefield.initializeManualCodeConfiguration(codeConfiguration);
    }

    @Override
    public synchronized void initializeAutomaticallyCodeConfiguration(String userName) {
        try {
            Machine machine = battleFieldMap.get(uboatToBattleFieldMap.get(userName)).getMachine();
            initializeManualCodeConfiguration(new CodeConfigurationInputDTO(lotteryRotorsNumber(machine), lotteryRotorsPosition(machine),
                    lotteryReflectorNumber(machine), lotteryPlugsConnections(machine)), userName);
        } catch (MachineLogicException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized DictionaryWordsDTO getDictionaryWordsDTO(String uboatUserName) {
        return battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName)).getDictionaryWordsDTO();
    }

    @Override
    public synchronized MachineSpecificationsDTO getMachineSpecificationsDTO(String uboatUserName) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName));
        return battlefield.getMachineSpecificationsDTO();
    }

    @Override
    public synchronized ManualCodeConfigurationDTO getManualCodeConfigurationDTO(String uboatUserName) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName));
        return battlefield.getManualCodeConfigurationDTO();
    }

    @Override
    public synchronized CodeConfigurationOutputDTO getCurrentCodeConfigurationOutputDTO(String uboatUserName) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName));
        return battlefield.getCurrentCodeConfigurationOutputDTO();
    }

    @Override
    public void resetMachine(String uboatUserName) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName));
        battlefield.resetMachine();
    }

    private List<Integer> lotteryRotorsNumber(Machine machine) {
        List<Integer> rotorsNumber = new ArrayList<>();
        int range = machine.getTotalRotors().size();
        int i = 0;

        while (i < machine.getRotorsCount()) {
            int rand = (int) (Math.random() * range) + 1;
            if (!rotorsNumber.contains(rand)) {
                rotorsNumber.add(rand);
                i++;
            }
        }
        return rotorsNumber;
    }

    private List<Character> lotteryRotorsPosition(Machine machine) {
        List<Character> rotorsPosition = new ArrayList<>();
        int range = machine.getABC().size();
        char position;
        int i = 0;

        while (i < machine.getRotorsCount()) {
            int rand = (int) (Math.random() * range);
            position = machine.getABC().get(rand);
            if (!rotorsPosition.contains(position)) {
                rotorsPosition.add(position);
                i++;
            }
        }
        return rotorsPosition;
    }

    private String lotteryReflectorNumber(Machine machine) throws MachineLogicException {
        int range = machine.getTotalReflectors().size();
        int rand = (int) (Math.random() * range) + 1;

        return Reflector.RomanNumber.fromIntegerToEnumRomanNumber(rand).toString();
    }

    private List<String> lotteryPlugsConnections(Machine machine) {
        List<String> plugBoardConnection = new ArrayList<>();
        Set<Character> plugsChars = new HashSet<>();
        int plugsAmountRange = machine.getABC().size() / 2 + 1;
        int plugsAmountRand = (int) (Math.random() * plugsAmountRange);
        int range = machine.getABC().size();
        int i = 0;
        StringBuilder connection = new StringBuilder();
        char firstCharInPair;
        char secondCharInPair;

        while (i < plugsAmountRand) {
            int firstCharRand = (int) (Math.random() * range);
            int secondCharRand = (int) (Math.random() * range);

            if (firstCharRand == secondCharRand)
                continue;

            firstCharInPair = machine.getABC().get(firstCharRand);
            secondCharInPair = machine.getABC().get(secondCharRand);

            if (!plugsChars.contains(firstCharInPair) && !plugsChars.contains(secondCharInPair)) {
                plugsChars.add(firstCharInPair);
                plugsChars.add(secondCharInPair);
                connection.append(firstCharInPair).append(secondCharInPair);
                plugBoardConnection.add(connection.toString());
                connection.delete(0, connection.length());
                i++;
            }
        }
        return plugBoardConnection;
    }

    @Override
    public synchronized void addAgentToAllyTeam(AgentLoginDTO agentLoginDTO, String agentUsername) {
        if(allyToBattleFieldMap.containsKey(agentLoginDTO.getAllyName())){
            usersManager.addAgentToAllyTeam(agentLoginDTO, agentUsername,
                    battleFieldMap.get(allyToBattleFieldMap.get(agentLoginDTO.getAllyName())).checkIfGameStatusActive());
        }else{
            usersManager.addAgentToAllyTeam(agentLoginDTO, agentUsername, false);
        }
    }

    @Override
    public Set<AllyTeamDTO> getAlliesTeamsListDTO(String userType,String userName) {
        Set<AllyTeamDTO> AlliesTeamsListDTO = null;

        switch (userType){
            case "Uboat":
                AlliesTeamsListDTO = battleFieldMap.get(uboatToBattleFieldMap.get(userName)).getAlliesTeamsListDTO();
                break;
            case "Allies":
                AlliesTeamsListDTO = battleFieldMap.get(allyToBattleFieldMap.get(userName)).getAlliesTeamsListDTO();
                break;
        }
        return AlliesTeamsListDTO;
    }

    @Override
    public UboatDTO getUboatDTO(String allyName) throws NoContestSelectionException {
        String battleFieldName = allyToBattleFieldMap.get(allyName);
        if(battleFieldName != null)
            return battleFieldMap.get(battleFieldName).getUboatDTO();
        else
            throw new NoContestSelectionException();
    }

    @Override
    public synchronized Set<UboatDTO> getUboatListDTO() {
        Set<UboatDTO> usersList = new HashSet<>();
        for (Battlefield battlefield : battleFieldMap.values()) {
            usersList.add(new UboatDTO(battlefield));
        }

        return usersList;
    }

    @Override
    public void setFileContentString(String fileContentString, String battleFieldName) {
        Battlefield battlefield = battleFieldMap.get(battleFieldName);
        battlefield.setFileContentString(fileContentString);
    }

    @Override
    public synchronized String getFileContentString(String allyName) {
        Battlefield battlefield = battleFieldMap.get(allyToBattleFieldMap.get(allyName));
        return battlefield.getFileContentString();
    }

    @Override
    public synchronized void addNewCandidates(String allyName, List<CandidateDTO> candidatesDTOList) {
        decryptionManagerMap.get(allyName).addNewCandidates(candidatesDTOList);
        battleFieldMap.get(allyToBattleFieldMap.get(allyName)).addNewCandidates(candidatesDTOList);
    }

    @Override
    public synchronized void addAllyToUboat(String battleFieldName, String allyName, int allyMissionSize) throws ContestIsFullException {
        Battlefield battlefield = battleFieldMap.get(battleFieldName);
        battlefield.addAllyToUboat(usersManager.getAlliesMap().get(allyName), allyMissionSize);
        allyToBattleFieldMap.put(allyName,battleFieldName);
    }

    @Override
    public Set<AgentDTO> getActiveAgentsDTOSet(String allyName) {
        return usersManager.getActiveAgentsDTOSet(allyName);
    }

    @Override
    public Boolean checkIfGameStatusActive(String userTypeFromSession, String userName) {
        Boolean isGameStatusActive = false;
        Battlefield battlefield = null;
        switch (userTypeFromSession){
            case "Uboat":
                battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(userName));
                break;
            case "Allies":
                battlefield = battleFieldMap.get(allyToBattleFieldMap.get(userName));
                break;
        }
        if(battlefield != null) {
            battlefield.setGameActive();
            isGameStatusActive = battlefield.checkIfGameStatusActive();
        }
        return isGameStatusActive;
    }

    @Override
    public boolean isUserExists(String usernameFromParameter) {
        return usersManager.isUserExists(usernameFromParameter);
    }

    @Override
    public void addUserToMap(String userType, String userName) {
        usersManager.addUserToMap(userType, userName);
    }

    @Override
    public void addUboatToMap(String battleFieldName) {
        usersManager.addUboatToMap(battleFieldMap.get(battleFieldName).getUboat());
    }

    @Override
    public void setUserReady(boolean isReady, String userType, String username) {
        usersManager.setUserReady(isReady, userType, username);
    }

    @Override
    public UboatDTO getUboatDTOForAgent(String allyName) throws NoContestSelectionException {
        if (allyToBattleFieldMap.containsKey(allyName)) {
            return battleFieldMap.get(allyToBattleFieldMap.get(allyName)).getUboatDTO();
        } else
            throw new NoContestSelectionException();
    }

    @Override
    public Set<AllyTeamDTO> getAlliesTeamsListDTOForAgent() {
        Set<AllyTeamDTO> alliesTeamsList = new HashSet<>();
        for (Allies allyTeam : usersManager.getAlliesMap().values()) {
            alliesTeamsList.add(new AllyTeamDTO(allyTeam.getAlliesUserName(), allyTeam.getAgentsAmount(), allyTeam.getMissionSize()));
        }
        return alliesTeamsList;
    }

    @Override
    public CandidatesAndVersionForUboatDTO getCandidatesAndVersionDTOForUboat(String uboatUserName, int candidateVersion) {
        CandidatesAndVersionForUboatDTO candidatesAndVersionForUboatDTO = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName))
                .getCandidatesAndVersionDTO(candidateVersion);
        if (candidatesAndVersionForUboatDTO.isWinnerExist()) {
            for (DecryptionManager dm : decryptionManagerMap.values()) {
                dm.setIsTaskCompleted(true);
            }
        }

        return candidatesAndVersionForUboatDTO;
    }

    @Override
    public CandidatesAndVersionDTO getCandidatesAndVersionDTOForAllies(String allyName, int candidateVersion) {
        return decryptionManagerMap.get(allyName).getCandidatesAndVersionDTO(candidateVersion);
    }

    @Override
    public CandidateDTO getWinnerTeamDetails(String userName) {
        return battleFieldMap.get(uboatToBattleFieldMap.get(userName)).getWinnerTeamDetails();
    }

    @Override
    public ContestStatusDTO getContestStatusDTO(String allyName) {
         return decryptionManagerMap.get(allyName).getContestStatusDTO();
    }

    @Override
    public synchronized void removeUser(String uboatUsername) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUsername));
        for(Allies allies : battlefield.getActiveAlliesSet())
            allyToBattleFieldMap.remove(allies.getAlliesUserName());
        battleFieldMap.remove(uboatToBattleFieldMap.get(uboatUsername));
        usersManager.removeUboat(uboatUsername);
        uboatToBattleFieldMap.remove(uboatUsername);
    }

    @Override
    public void setAgentProgress(String username, AgentProgressDTO agentProgressDTO) {
        usersManager.setAgentProgress(username, agentProgressDTO);
    }

    @Override
    public Set<AgentProgressDTO> getAgentsProgressDTOs(String username) {
       return usersManager.getAgentsProgressDTOs(username);
    }

    @Override
    public void setAllyContestOver(String allyUsername) {
        usersManager.setAllyContestOver(allyUsername, true);
    }

    @Override
    public Boolean isContestOverConfirmed(String allyName) {
        Boolean isContestOverConfirmed = usersManager.isContestOverConfirmed(allyName);
        if(isContestOverConfirmed){
            decryptionManagerMap.remove(allyName);
        }

        return isContestOverConfirmed;
    }

    @Override
    public void ResetCurrentContest(String username) {
        battleFieldMap.get(uboatToBattleFieldMap.get(username)).ResetCurrentContest();
    }

    @Override
    public CodeConfigurationOutputDTO getCodeConfigurationOutputDTO(String uboatUserName) {
        Battlefield battlefield = battleFieldMap.get(uboatToBattleFieldMap.get(uboatUserName));
        return battlefield.getCurrentCodeConfigurationOutputDTO();
    }
}


