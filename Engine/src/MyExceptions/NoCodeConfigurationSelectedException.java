package MyExceptions;

public class NoCodeConfigurationSelectedException extends Exception{
    public NoCodeConfigurationSelectedException(){
        super("You need to select initial code configuration first!");
    }
}
