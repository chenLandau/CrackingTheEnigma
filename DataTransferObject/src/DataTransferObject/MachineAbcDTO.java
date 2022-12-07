package DataTransferObject;

import java.util.List;

public class MachineAbcDTO {

    List<Character> ABC;

    public MachineAbcDTO(List<Character> ABC) {
        this.ABC = ABC;
    }
    public List<Character> getABC() { return ABC; }
}
