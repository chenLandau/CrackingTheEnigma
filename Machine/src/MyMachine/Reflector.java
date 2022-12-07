package MyMachine;
import MyExceptions.MachineLogicException;

import java.util.*;

public class Reflector implements Cloneable{
    private RomanNumber reflectorId;
    private Map<Integer, Integer> reflections;
    public Reflector(RomanNumber reflectorId,Map<Integer,Integer> reflections) {
        this.reflectorId = reflectorId;
        this.reflections = reflections;
    }

    @Override
    public Reflector clone() {
        try {
            Reflector newReflector = (Reflector) super.clone();
            newReflector.reflections = new HashMap<>(reflections);
            newReflector.reflectorId = reflectorId;
            return newReflector;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    public enum RomanNumber {
        I, II, III, IV, V;
        public static RomanNumber fromIntegerToEnumRomanNumber(int id) throws MachineLogicException {
            switch (id) {
                case 1:
                    return RomanNumber.I;
                case 2:
                    return RomanNumber.II;
                case 3:
                    return RomanNumber.III;
                case 4:
                    return RomanNumber.IV;
                case 5:
                    return RomanNumber.V;
            }
            throw new MachineLogicException("Reflector number is not recognized in the system.");
        }

        public static RomanNumber fromStrToEnumRomanNumber(String id) throws MachineLogicException {
            switch (id) {
                case "I":
                    return RomanNumber.I;
                case "II":
                    return RomanNumber.II;
                case "III":
                    return RomanNumber.III;
                case "IV":
                    return RomanNumber.IV;
                case "V":
                    return RomanNumber.V;
            }
            throw new MachineLogicException("Reflector number is not recognized in the system.");
        }
    }
    public RomanNumber getId() { return this.reflectorId; }
    public int reflectorOutput(int reflectorInput){
        if(!reflections.containsKey(reflectorInput))
            System.out.println("does not contains REFLECTOR INPUT" + reflectorInput);
            return reflections.get(reflectorInput);
    }


}
