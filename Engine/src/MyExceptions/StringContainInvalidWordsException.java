package MyExceptions;

public class StringContainInvalidWordsException extends Exception{

    public StringContainInvalidWordsException(String s) {
        super(s);
    }
}
