package MyExceptions;

import java.util.List;

public class StringContainsInvalidCharactersException extends Exception{
    private List<String> invalidCharacters;

    public StringContainsInvalidCharactersException(List<String> invalidCharacters){
        this.invalidCharacters = invalidCharacters;
    }

    public List<String> getInvalidCharacters(){return invalidCharacters;}
}
