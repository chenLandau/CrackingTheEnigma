package BruteForce.DecryptionManager;

import BruteForce.Lock;
import DataTransferObject.AgentMissionDTO;
import MyMachine.Machine;
import MyMachine.Reflector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;

public class TaskGenerator implements Runnable {
    private BlockingQueue<AgentMissionDTO> agentBlockingQueue;
    private int missionSize;
    private Machine enigma;
    private String level;
    private long totalTasksAmount;
    private Lock isTaskCompleted;
    private Lock doneGenerateTasksLocker;
    private String decryptedString;

    public TaskGenerator(BlockingQueue<AgentMissionDTO> agentBlockingQueue,
                         Lock isTaskCompleted,Lock doneGenerateTasksLocker,
                         int missionSize, Machine enigmaMachine, String level){
        this.isTaskCompleted = isTaskCompleted;
        this.agentBlockingQueue = agentBlockingQueue;
        this.missionSize = missionSize;
        this.enigma = enigmaMachine;
        this.level = level;
        this.doneGenerateTasksLocker = doneGenerateTasksLocker;
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    @Override
    public void run() {
                switch (level) {
                    case "Easy":
                        setEasyLevelTasks();
                        break;
                    case "Medium":
                        setMediumLevelTasks();
                        break;
                    case "Hard":
                        setHardLevelTasks();
                        break;
                    case "Impossible":
                        setImpossibleLevelTasks();
                        break;
                }

        doneGenerateTasksLocker.setLock(true);
    }


    public void putTaskInQueue(List<Integer> rotorInUseList, String reflectorInUse) {
        List<Character> endPositioning = new ArrayList<>();
        List<Character> startPositioning = new ArrayList<>();
        Boolean exit = false;

        for (int i = 0; i < enigma.getRotorsCount(); i++) {
            startPositioning.add(enigma.getABC().get(0));
            endPositioning.add(enigma.getABC().get(enigma.getABC().size() - 1));
        }

            do {
                try {
                    synchronized (agentBlockingQueue) {
                        while(agentBlockingQueue.size() < 500 && !exit) {
                            AgentMissionDTO lastTaskDTO = createSingleAgentMissionDTO(rotorInUseList, reflectorInUse, startPositioning, endPositioning);
                            exit = lastTaskDTO.getTaskList().get(lastTaskDTO.getTaskList().size() - 1).equals(endPositioning);
                            agentBlockingQueue.put(lastTaskDTO);
                            startPositioning = lastTaskDTO.getTaskList().get(lastTaskDTO.getTaskList().size() - 1);
                            startPositioning = new ArrayList<>(advancePositionList(startPositioning, enigma.getABC()));
                        }
                        agentBlockingQueue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!exit && !isTaskCompleted.getLock());
       }


    public AgentMissionDTO createSingleAgentMissionDTO(List<Integer> rotorInUseList, String reflectorInUse, List<Character> startPositioning, List<Character> endPositioning) {
        List<Character> currentMissionPositioning = new ArrayList<>(startPositioning);
        List<List<Character>> missionList = new ArrayList<>();
        int currentMission = 1;
        missionList.add(currentMissionPositioning);

        while (currentMission < missionSize && !currentMissionPositioning.equals(endPositioning)) {
            currentMissionPositioning = advancePositionList(currentMissionPositioning, enigma.getABC());
            missionList.add(currentMissionPositioning);
            currentMission++;
        }

        return new AgentMissionDTO(missionList,rotorInUseList, reflectorInUse,decryptedString);
    }

    public List<Character> advancePositionList(List<Character> currentPositionList,List<Character> ABC){
        List<Character> newPositionList = new ArrayList<>(currentPositionList);
        for (int i = currentPositionList.size() - 1; i >= 0; i--) {
            Character currentChar = newPositionList.get(i);
            int index = ABC.indexOf(currentChar) + 1;
            if (index ==ABC.size()) {
                index = 0;
                newPositionList.remove(i);
                Character newChar = ABC.get(index);
                newPositionList.add(i, newChar);
            } else {
                Character newChar = ABC.get(index);
                newPositionList.remove(i);
                newPositionList.add(i, newChar);
                break;
            }
        }
        return newPositionList;
    }

    public void setEasyLevelTasks() {
        calculateTotalTasks("Easy", missionSize);
        putTaskInQueue(enigma.getRotorsInUseIntegerList(), enigma.getReflectorInUse().getId().toString());
    }

    public void setMediumLevelTasks() {
        calculateTotalTasks("Medium", missionSize);
        Machine enigmaMachine = enigma.clone();

        for (Reflector.RomanNumber reflector : enigma.getTotalReflectors().keySet()) {
                enigmaMachine.setReflector(reflector.toString());
                putTaskInQueue(enigmaMachine.getRotorsInUseIntegerList(), enigmaMachine.getReflectorInUse().getId().toString());
        }
    }

    public void setHardLevelTasks()  {
        List<List<Integer>> permutations = new ArrayList<>();
        calculateTotalTasks("Hard", missionSize);
        permute(permutations, enigma.getRotorsInUseIntegerList(), 0, enigma.getRotorsInUseIntegerList().size() - 1);
        Machine enigmaMachine = enigma.clone();

        for (Reflector.RomanNumber reflector : enigma.getTotalReflectors().keySet()) {
                enigmaMachine.setReflector(reflector.toString());
                for (List<Integer> currentPermutation : permutations) {
                    enigmaMachine.setRotorsInUse(currentPermutation);
                    putTaskInQueue(enigmaMachine.getRotorsInUseIntegerList(), enigmaMachine.getReflectorInUse().getId().toString());
                }
        }
    }

    private static void permute(List<List<Integer>> permutations, List<Integer> numbers, int l, int r)
    {
        if (l == r) {
            permutations.add(new ArrayList<>(numbers));
        }
        else
        {
            for (int i = l; i <= r; i++)
            {
                numbers = swap(numbers,l,i);
                permute(permutations, numbers, l+1, r);
                numbers = swap(numbers,l,i);
            }
        }
    }

    public static List<Integer> swap(List<Integer> numbers, int i, int j)
    {
        List<Integer> newNumbers = new ArrayList<>(numbers);
        int temp;
        temp = newNumbers.get(i);
        newNumbers.set(i, newNumbers.get(j));
        newNumbers.set(j, temp);

        return newNumbers;
    }

    public void setImpossibleLevelTasks()  {
        List<List<Integer>> rotorsInUseCombinations = createCombinations(enigma.getTotalRotorsIntegerList(), enigma.getRotorsCount());
        calculateTotalTasks("Impossible", missionSize);
        Machine enigmaMachine = enigma.clone();

        for (List<Integer> currentRotorsInUseCombination : rotorsInUseCombinations) {
                List<List<Integer>> permutations = new ArrayList<>();
                String rotorsInUseStr = "";
                for (Integer currRotor : currentRotorsInUseCombination) {
                    rotorsInUseStr += currRotor.toString();
                }

                Boolean[] used = new Boolean[enigma.getRotorsCount()];
                Arrays.fill(used, Boolean.FALSE);
                createPermutationsList(permutations, rotorsInUseStr, used);

                for (Reflector.RomanNumber reflector : enigma.getTotalReflectors().keySet()) {
                        enigmaMachine.setReflector(reflector.toString());
                        for (List<Integer> currentPermutation : permutations) {
                            enigmaMachine.setRotorsInUse(currentPermutation);
                            putTaskInQueue(enigmaMachine.getRotorsInUseIntegerList(), enigmaMachine.getReflectorInUse().getId().toString());
                        }
                }
        }
    }
    private void calculateTotalTasks(String level, int missionSize) {
        long firstRotorsPositionCombinationsNumber = (long) Math.ceil((Math.pow(enigma.getABC().size(), enigma.getRotorsCount())));
        int reflectorsNumber = enigma.getTotalReflectors().size();
        long firstRotorsInUseOrderNumber = factorial(enigma.getRotorsCount());
        long rotorsInUseCombinationsNumber = factorial(enigma.getTotalRotorsIntegerList().size()) /
                (factorial(enigma.getRotorsCount()) * factorial(enigma.getTotalRotorsIntegerList().size() - enigma.getRotorsCount()));

        switch (level) {
            case "Easy":
                totalTasksAmount = (long) Math.ceil(firstRotorsPositionCombinationsNumber / missionSize );
                break;
            case "Medium":
                totalTasksAmount = (long) Math.ceil((firstRotorsPositionCombinationsNumber * reflectorsNumber) / missionSize);
                break;
            case "Hard":
                totalTasksAmount = (long) Math.ceil((firstRotorsPositionCombinationsNumber * reflectorsNumber * firstRotorsInUseOrderNumber) / missionSize);
                break;
            case "Impossible":
                totalTasksAmount = (long) Math.ceil((firstRotorsPositionCombinationsNumber * reflectorsNumber * firstRotorsInUseOrderNumber
                        * rotorsInUseCombinationsNumber) / missionSize);
                break;
        }

        System.out.println("Total tasks amount amount: " + totalTasksAmount);
    }

    public long factorial(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorial(n - 1);
    }

    public List<List<Integer>> createCombinations(List<Integer> inputSet, int k) {
        List<List<Integer>> results = new ArrayList<>();
        combinationsInternal(inputSet, k, results, new ArrayList<>(), 0);
        return results;
    }

    private static void combinationsInternal(List<Integer> inputSet, int k, List<List<Integer>> results, ArrayList<Integer> accumulator, int index) {
        int needToAccumulate = k - accumulator.size();
        int canAccumulate = inputSet.size() - index;

        if (accumulator.size() == k) {
            results.add(new ArrayList<>(accumulator));
        } else if (needToAccumulate <= canAccumulate) {
            combinationsInternal(inputSet, k, results, accumulator, index + 1);
            accumulator.add(inputSet.get(index));
            combinationsInternal(inputSet, k, results, accumulator, index + 1);
            accumulator.remove(accumulator.size() - 1);
        }
    }

    public void createPermutationsList(List<List<Integer>> permutations, String rotorsNumbersStr, Boolean[] used) {
        List<Integer> output = new ArrayList<>();
        createPermutations(0, permutations, output, used, rotorsNumbersStr);
    }

    public void createPermutations(int level, List<List<Integer>> permutations, List<Integer> currentPermutation, Boolean[] used, String orig) {
        int len = orig.length();
        if (level == len) {
            permutations.add(new ArrayList<>(currentPermutation));
        } else {
            for (int i = 0; i < len; i++) {
                if (!used[i]) {
                    currentPermutation.add(Integer.parseInt(String.valueOf(orig.charAt(i))));
                    used[i] = true;
                    createPermutations(level + 1, permutations, currentPermutation, used, orig);
                    currentPermutation.remove(currentPermutation.size() - 1);
                    used[i] = false;
                }
            }
        }
    }
}

