package ValidityChecker;

import jaxbEnigma.*;
import MyExceptions.MachineLogicException;
import MyExceptions.XmlException;
import MyMachine.Reflector;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidityChecker {
    public static void checkCteEnigmaValidity(CTEEnigma cteEnigma, Set<String> battleFieldNames) throws XmlException, MachineLogicException {
        String abc = cteEnigma.getCTEMachine().getABC().trim();
        int abcSize = abc.length();
        int rotorCount = cteEnigma.getCTEMachine().getRotorsCount();
        int physicalRotorSize = cteEnigma.getCTEMachine().getCTERotors().getCTERotor().size();
        if (abcSize % 2 != 0) {
            throw new XmlException("The alphabet size of the machine is not even!");
        }
        if(rotorCount > physicalRotorSize)
            throw new XmlException("There are not enough rotors in the machine!");
        if(rotorCount < 2)
            throw new XmlException("The machine rotors count must be greater than 2.");

        checkMachineRotorsValidity(abcSize, cteEnigma.getCTEMachine().getCTERotors());
        checkMachineReflectorsValidity(abcSize, cteEnigma.getCTEMachine().getCTEReflectors());
        checkBattleFieldValidity(cteEnigma.getCTEBattlefield(), battleFieldNames);
    }

    private static void checkBattleFieldValidity(CTEBattlefield cteBattlefield, Set<String> battleFieldNames) throws XmlException {
        if(battleFieldNames.contains(cteBattlefield.getBattleName()))
            throw new XmlException("Battle field name is already exists!!!");
    }

    public static void checkMachineRotorsValidity(Integer abcSize, CTERotors cteRotors) throws XmlException{
        Set<Integer> rotorsId = new HashSet<>();

        for (CTERotor rotor: cteRotors.getCTERotor()) {
            if(rotor.getNotch() > abcSize)
                throw new XmlException("The notch must be set within the rotor size range.");
            if(rotorsId.contains(rotor.getId()))
                throw new XmlException("Each rotor must have a unique Id.");
            else
                rotorsId.add(rotor.getId());
            checkRotorMapping(rotor);
        }

        for (int i = 1; i <= rotorsId.size(); i++) {
            if (!rotorsId.contains(i))
                throw new XmlException("rotors Id are not a running counter starting from 1.");
        }
    }
    public static void checkRotorMapping(CTERotor cteRotor) throws XmlException {
        Set<String> rotorsMapping = new HashSet<>();
        for (CTEPositioning position: cteRotor.getCTEPositioning()) {
            if(rotorsMapping.contains(position.getRight()))
                throw new XmlException("There is a double mapping in rotor number " + cteRotor.getId());
            else
                rotorsMapping.add(position.getRight());
        }
        for (CTEPositioning position: cteRotor.getCTEPositioning()) {
            if(!rotorsMapping.remove(position.getLeft()))
                throw new XmlException("There is a double mapping in rotor number " + cteRotor.getId());
        }
    }
    public static void checkMachineReflectorsValidity(Integer abcSize, CTEReflectors cteReflectors) throws XmlException, MachineLogicException {
        Set<Reflector.RomanNumber> reflectorsId = new HashSet<>();

        for (CTEReflector reflector : cteReflectors.getCTEReflector()) {
            Reflector.RomanNumber romanNumber = Reflector.RomanNumber.fromStrToEnumRomanNumber(reflector.getId());
            if (reflector.getCTEReflect().size() != abcSize / 2)
                throw new XmlException("The amount of mappings in the reflectors should be half the size of the abc machine.");
            if (reflectorsId.contains(romanNumber))
                throw new XmlException("Each rotor must have a unique Id.");
            else
                reflectorsId.add(romanNumber);

            checkReflectorMapping(reflector);
        }
        for (int i = 1; i <= reflectorsId.size(); i++) {
            if (!reflectorsId.contains(Reflector.RomanNumber.fromIntegerToEnumRomanNumber(i))) {
                throw new XmlException("Reflector's Id are not a running counter starting from I.");
            }
        }
    }
        public static void checkReflectorMapping(CTEReflector cteReflector) throws XmlException {
            Set<Integer> reflectorMapping = new HashSet<>();

            for (CTEReflect position: cteReflector.getCTEReflect()) {
                if(position.getInput() == position.getOutput())
                    throw new XmlException("A signal cannot be mapped to itself in the reflector!");
                if(reflectorMapping.contains(position.getInput()) || reflectorMapping.contains(position.getOutput()))
                    throw new XmlException("There is a double mapping in reflector number" + cteReflector.getId());
                else
                    reflectorMapping.add(position.getInput());
            }
    }
    public static void checkStringValidity(List<Character> ABC, String input) throws MachineLogicException {
        StringBuilder charsNotValid = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if(!ABC.contains(input.charAt(i))) {
                charsNotValid.append(input.charAt(i)).append(" ");
            }
        }
        if (charsNotValid.toString().length() != 0) {
            throw new MachineLogicException("Invalid decode string! The following characters do not belong to the alphabet of the machine: " + charsNotValid );
        }
    }
}


