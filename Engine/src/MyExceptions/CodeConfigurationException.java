package MyExceptions;

import java.util.List;

public class CodeConfigurationException extends Exception{

    private List<String> errorsList;

    public CodeConfigurationException(List<String> errors){
        this.errorsList = errors;
    }

    public List<String> getErrorsList(){return errorsList;}
}
