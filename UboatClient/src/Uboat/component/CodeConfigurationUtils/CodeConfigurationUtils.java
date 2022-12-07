package Uboat.component.CodeConfigurationUtils;

import DataTransferObject.CodeConfigurationOutputDTO;

import java.util.List;

public class CodeConfigurationUtils {

    public static String createLabelTextCodeConfiguration(CodeConfigurationOutputDTO currentCodeConfiguration) {
        StringBuilder CodeConfiguration = new StringBuilder();
        String RotorsNumber = BuildRotorsNumberString(currentCodeConfiguration.getRotorsNumber());
        String RotorsFirstPosition = BuildRotorsPositionString(currentCodeConfiguration.getRotorsPosition(), currentCodeConfiguration.getNotchPosition());
        CodeConfiguration.append(RotorsNumber).append(RotorsFirstPosition);
        CodeConfiguration.append("<").append(currentCodeConfiguration.getReflectorNumber()).append(">");

        return CodeConfiguration.toString();
    }

    private static String BuildRotorsNumberString(List<Integer> rotorsNumber) {
        StringBuilder rotorsNumberStr;
        rotorsNumberStr = new StringBuilder("<");
        for (int i = rotorsNumber.size() - 1; i >= 0; i--) {
            rotorsNumberStr.append(rotorsNumber.get(i).toString());
            if (i == 0)
                rotorsNumberStr.append(">");
            else
                rotorsNumberStr.append(",");
        }

        return rotorsNumberStr.toString();
    }

    private static String BuildRotorsPositionString(List<Character> rotorsPosition, List<Integer> notchPosition) {
        StringBuilder rotorsPositionStr;
        rotorsPositionStr = new StringBuilder("<");
        for (int i = rotorsPosition.size() - 1; i >= 0; i--) {
            rotorsPositionStr.append(rotorsPosition.get(i).toString()).append("(").append(notchPosition.get(i).toString()).append(")");
        }
        rotorsPositionStr.append(">");
        return rotorsPositionStr.toString();
    }
}
