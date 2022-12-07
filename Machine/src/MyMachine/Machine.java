package MyMachine;

import jaxbEnigma.*;
import MyExceptions.MachineLogicException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine implements Cloneable {
    private List<Character> ABC;
    private int rotorsCount;
    private List<Rotor> rotorsInUse;
    private Reflector reflectorInUse;
    private Map<Integer, Rotor> totalRotors;
    private Map<Reflector.RomanNumber, Reflector> totalReflectors;
    private final PlugBoard plugBoard;
    private int decryptedMessagesAmount;

    public Machine() {
        this.totalRotors = new HashMap<>();
        this.rotorsInUse = new ArrayList<>();
        this.totalReflectors = new HashMap<>();
        this.plugBoard = new PlugBoard();
        this.ABC = new ArrayList<>();
        this.decryptedMessagesAmount = 0;
    }

    @Override
    public Machine clone() {
        try {
            Machine newMachine = (Machine) super.clone();
            newMachine.ABC = new ArrayList<>(ABC);
//            newMachine.setReflector(reflectorInUse.getId().toString());
            newMachine.reflectorInUse = reflectorInUse;
            newMachine.rotorsInUse = new ArrayList<>(rotorsInUse.size());
            for (Rotor currentRotor : rotorsInUse) {
                newMachine.rotorsInUse.add(currentRotor.clone());
            }
            newMachine.totalRotors = new HashMap<>(totalRotors.size());
            for (Integer rotorId : totalRotors.keySet()) {
                newMachine.totalRotors.put(rotorId, totalRotors.get(rotorId).clone());
            }
            newMachine.totalReflectors = new HashMap<>(totalReflectors.size());
            for (Reflector.RomanNumber reflectorId : totalReflectors.keySet()) {
                newMachine.totalReflectors.put(reflectorId, totalReflectors.get(reflectorId).clone());
            }

            return newMachine;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public List<Character> getABC() {
        return ABC;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public List<Rotor> getRotorsInUse() {
        return rotorsInUse;
    }

    public Map<Reflector.RomanNumber, Reflector> getTotalReflectors() {
        return totalReflectors;
    }

    public Map<Integer, Rotor> getTotalRotors() {
        return totalRotors;
    }

    public int getDecryptedMessagesAmount() {
        return decryptedMessagesAmount;
    }

    public Reflector getReflectorInUse() {
        return reflectorInUse;
    }

    public void setAlphabet(String value) {
        ABC.clear();
        for (int i = 0; i < value.length(); i++) {
            this.ABC.add(value.charAt(i));
        }
    }

    public void setRotorsMap(Map<Integer, Rotor> value) {
        this.totalRotors = value;
    }

    public void setRotorsCount(int value) {
        this.rotorsCount = value;
    }

    public void setReflectorsMap(Map<Reflector.RomanNumber, Reflector> value) {
        this.totalReflectors = value;
    }

    public void setRotorsInUse(List<Integer> RotorsNumber) {
        this.rotorsInUse.clear();
        Rotor currentRotor;
        for (Integer integer : RotorsNumber) {
            currentRotor = totalRotors.get(integer);
            this.rotorsInUse.add(currentRotor);
        }
    }

    public void setRotorsPositions(List<Character> firstPositions) {
        int j = 0;
        for (Rotor rotor : rotorsInUse) {
            rotor.setFirstPosition(firstPositions.get(j));
            rotor.setRotorFirstPosition();
            j++;
        }
    }

    public void setPlugs(List<String> plugsConnections) {
        char firstCharPair, secondCharPair;
        if (plugsConnections != null) {
            for (String connection : plugsConnections) {
                firstCharPair = connection.charAt(0);
                secondCharPair = connection.charAt(1);
                this.plugBoard.connectPlug(firstCharPair, secondCharPair);
            }
        }
    }

    public void setDecryptedMessagesAmount(int amount) {
        this.decryptedMessagesAmount = amount;
    }

    public void setReflector(String reflectorNumber) {
        try {
            this.reflectorInUse = totalReflectors.get(Reflector.RomanNumber.fromStrToEnumRomanNumber(reflectorNumber));
        } catch (MachineLogicException e) {
            System.out.println("machine:" + reflectorNumber);
            e.printStackTrace();
        }
    }

    public void chargeCteMachineToMyMachine(CTEEnigma cteEnigma) throws MachineLogicException {
        setAlphabet(cteEnigma.getCTEMachine().getABC().trim());
        setRotorsCount(cteEnigma.getCTEMachine().getRotorsCount());
        setRotorsMap(chargeRotorsFromCteMachine(cteEnigma.getCTEMachine().getCTERotors()));
        setReflectorsMap(chargeReflectorsFromCteMachine(cteEnigma.getCTEMachine().getCTEReflectors()));
        setDecryptedMessagesAmount(0);
    }

    public Map<Integer, Rotor> chargeRotorsFromCteMachine(CTERotors cteRotors) {
        Map<Integer, Rotor> myRotorMap = new HashMap<>();
        Rotor curr;
        for (CTERotor cteRotor : cteRotors.getCTERotor()) {
            curr = new Rotor(cteRotor.getId(), cteRotor.getNotch());
            for (CTEPositioning position : cteRotor.getCTEPositioning()) {
                curr.getRotorPositions().add(new Rotor.Position(position.getRight().charAt(0), position.getLeft().charAt(0)));
            }
            myRotorMap.put(curr.getId(), curr);
            curr.setNotchRightPosition(curr.getRotorPositions().get(curr.getNotch() - 1).right);
        }
        return myRotorMap;
    }

    public Map<Reflector.RomanNumber, Reflector> chargeReflectorsFromCteMachine(CTEReflectors cteReflectors) throws MachineLogicException {
        Map<Reflector.RomanNumber, Reflector> myReflectors = new HashMap<>();
        Reflector curr;
        for (CTEReflector cteReflector : cteReflectors.getCTEReflector()) {
            Map<Integer, Integer> reflections = new HashMap<>();
            for (CTEReflect reflect : cteReflector.getCTEReflect()) {
                reflections.put(reflect.getInput(), reflect.getOutput());
                reflections.put(reflect.getOutput(), reflect.getInput());
            }
            curr = new Reflector(Reflector.RomanNumber.fromStrToEnumRomanNumber(cteReflector.getId()), reflections);

            myReflectors.put(curr.getId(), curr);
        }
        return myReflectors;
    }

    public void resetMachine() {
        for (Rotor rotor : rotorsInUse) {
            rotor.setRotorFirstPosition();
        }
    }

    public String getEncryptedString(String strToEncrypt) {
        StringBuilder decodedString = new StringBuilder();
        for (int i = 0; i < strToEncrypt.length(); i++) {
            decodedString.append(charInputProcessor(strToEncrypt.charAt(i)));
        }
        setDecryptedMessagesAmount(decryptedMessagesAmount + 1);
        return decodedString.toString();
    }

    public char charInputProcessor(char input) {
            int rotorIndex;
            Rotor currentRotor;
            Rotor nextRotor;
            Rotor firstRotor = rotorsInUse.get(0);
            int output = ABC.indexOf(input);

        try {
            firstRotor.getRotorPositions().add(firstRotor.getRotorPositions().remove(0));

            for (rotorIndex = 0; rotorIndex < rotorsInUse.size(); rotorIndex++) {
                currentRotor = rotorsInUse.get(rotorIndex);

                if ((rotorIndex != rotorsInUse.size() - 1) && currentRotor.getNotchRightPosition() == currentRotor.getRotorPositions().get(0).right) {
                    currentRotor.getRotorPositions().add(currentRotor.getRotorPositions().remove(0));
                    nextRotor = rotorsInUse.get(rotorIndex + 1);
                    nextRotor.getRotorPositions().add(nextRotor.getRotorPositions().remove(0));
                }
                output = currentRotor.RotorOutput(output, Rotor.Direction.FORWARD);
            }
            if(reflectorInUse == null)
                System.out.println("char inpuy proccessor nullll" + rotorsInUse);

            output = reflectorInUse.reflectorOutput(output + 1) - 1;

            for (rotorIndex = rotorsInUse.size() - 1; rotorIndex >= 0; rotorIndex--) {
                currentRotor = rotorsInUse.get(rotorIndex);
                output = currentRotor.RotorOutput(output, Rotor.Direction.BACKWARD);
            }

        } catch (MachineLogicException e) {
            e.printStackTrace();
        }
            return ABC.get(output);
  }
    public List<Integer> getTotalRotorsIntegerList(){
        List<Integer> totalRotorsNumberList = new ArrayList<>();
        for(int i=0; i < totalRotors.size(); i++)
            totalRotorsNumberList.add(i + 1);
        return totalRotorsNumberList;
    }
    public List<Integer> getRotorsInUseIntegerList(){
        List<Integer> rotorsNumberList = new ArrayList<>();
        for(int i=0; i < rotorsInUse.size(); i++)
            rotorsNumberList.add(rotorsInUse.get(i).getId());
        return rotorsNumberList;
    }
    public List<Integer> getNotchPositionList() {
        List<Integer> notchPositionList = new ArrayList<>();
        int currentNotchPosition;
        int toBegin = 0;
            try {
                for (int i = 0; i < rotorsInUse.size(); i++) {
                    currentNotchPosition = rotorsInUse.get(i).getCurrentNotchRightPosition();
                    notchPositionList.add(toBegin, currentNotchPosition);
                }
            } catch (MachineLogicException e){
                e.printStackTrace();
            }
     return notchPositionList;
    }

    public void setCodeConfiguration(List<Integer> rotorsNumber, List<Character> rotorsPosition, String reflectorNumber, List<String> plugBoardConnection) throws MachineLogicException {
        setRotorsInUse(rotorsNumber);
        setRotorsPositions(rotorsPosition);
        setReflector(reflectorNumber);
        setPlugs(plugBoardConnection);
    }
}


