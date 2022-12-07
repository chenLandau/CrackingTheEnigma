package DataTransferObject;

import java.util.List;
import java.util.Map;

public class ManualCodeConfigurationDTO {
    private int rotorsCount;
    private int totalRotorsNumber;

    private int reflectorAmount;
    private Map<Integer,String> numeralNumbersMap;

    private List<Character> ABC;

    public ManualCodeConfigurationDTO(int rotorsCount,int totalRotorsNumber,int reflectorAmount,
                                      Map<Integer,String> numeralNumbers, List<Character> ABC) {
        this.rotorsCount = rotorsCount;
        this.totalRotorsNumber = totalRotorsNumber;
        this.reflectorAmount = reflectorAmount;
        this.numeralNumbersMap = numeralNumbers;
        this.ABC = ABC;
    }
    public int getRotorsCount() { return rotorsCount; }
    public int getTotalRotorsNumber() { return totalRotorsNumber; }

    public int getReflectorAmount() { return reflectorAmount; }
    public Map<Integer, String> getNumeralNumbers() { return numeralNumbersMap; }

    public List<Character> getABC() { return ABC; }
}
