package MyMachine;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Cloneable{
    private Map<Character,Character> plugBoard;
    public PlugBoard() {
       this.plugBoard = new HashMap<>();
    }
    public void connectPlug(char first,char second) {
       plugBoard.put(first,second);
       plugBoard.put(second,first);
    }
    public char plugBoardWiringRes(char input) {
        char output = input;

        if(plugBoard.containsKey(input))
            output = plugBoard.get(input);
        return output;
    }
    @Override
    public PlugBoard clone() {
        try {
            PlugBoard clone = (PlugBoard) super.clone();
            clone.plugBoard = new HashMap<>(plugBoard);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
