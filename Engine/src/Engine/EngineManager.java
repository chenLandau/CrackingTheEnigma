package Engine;

//import Application.AppController.UIAdapter;
import DataTransferObject.*;
import MyExceptions.*;
import jaxbEnigma.CTEEnigma;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface EngineManager {
    CTEEnigma deserializeFrom(InputStream in) throws JAXBException;
    String readCteMachineFromXml(InputStream inputStream, String usernameFromSession)  throws JAXBException, XmlException, MachineLogicException;
    void initializeAutomaticallyCodeConfiguration(String uboatUserName);
    void initializeManualCodeConfiguration(CodeConfigurationInputDTO codeConfiguration, String uboatUserName);
    MachineSpecificationsDTO getMachineSpecificationsDTO(String uboatUserName);
    CodeConfigurationOutputDTO getCurrentCodeConfigurationOutputDTO(String uboatUserName);
    void resetMachine(String userNameFromSession);
//    void bruteForce(Consumer<Long> totalTasksDelegate, Consumer<Long> taskExecutionTimeDelegate,
//                                BruteForceTaskDTO bruteForceTaskDTO, UIAdapter uiAdapter, Runnable onFinish);
    void addAllyToDecryptionManagerMap(String allyUserName, int missionSize, String uboatName);
    DictionaryWordsDTO getDictionaryWordsDTO(String uboatUserName);
    String encryptInputString(String inputString, String uboatUserName) throws StringContainInvalidWordsException ;
     ManualCodeConfigurationDTO getManualCodeConfigurationDTO(String uboatUserName);
    Set<AllyTeamDTO> getAlliesTeamsListDTO(String userType,String userName);
     UboatDTO getUboatDTO(String uboatName) throws NoContestSelectionException;
     Set<UboatDTO> getUboatListDTO();
    void setFileContentString(String fileContentString, String uboatName);
    String getFileContentString(String uboatName);
    void startTaskGeneratorServlet(String allyName);
    List<AgentMissionDTO> getAgentMissionsList(String agentName, String allyName);
    void addNewCandidates(String allyName, List<CandidateDTO> candidatesDTOList);
    UboatDTO getUboatDTOForAgent(String allyName) throws NoContestSelectionException;
    void addAgentToAllyTeam(AgentLoginDTO agentLoginDTO, String usernameFromSession);
    void addAllyToUboat(String battleFieldName, String allyName, int allyMissionSize) throws ContestIsFullException;
    Set<AgentDTO> getActiveAgentsDTOSet(String allyName);
    Boolean checkIfGameStatusActive(String userTypeFromSession, String userName);
    boolean isUserExists(String usernameFromParameter);
    void addUserToMap(String userType, String userName);
    void addUboatToMap(String battleFieldName);
    void setUserReady(boolean isReady, String userType, String username);
    Set<AllyTeamDTO> getAlliesTeamsListDTOForAgent();
    CandidatesAndVersionForUboatDTO getCandidatesAndVersionDTOForUboat(String uboatUserName, int candidateVersion);
    CandidatesAndVersionDTO getCandidatesAndVersionDTOForAllies(String allyName, int candidateVersion);
    CandidateDTO getWinnerTeamDetails(String userName);
    ContestStatusDTO getContestStatusDTO(String allyName);
    void removeUser(String uboatUsername);
    void setAgentProgress(String username, AgentProgressDTO agentProgressDTO);
    Set<AgentProgressDTO> getAgentsProgressDTOs(String username);
    void setAllyContestOver(String allyUsername);
    Boolean isContestOverConfirmed(String allyName);
    void ResetCurrentContest(String username);
    CodeConfigurationOutputDTO getCodeConfigurationOutputDTO(String uboatUserName);
}
