package MyMachine;
import MyExceptions.MachineLogicException;
import java.util.ArrayList;
import java.util.List;

public class Rotor implements Cloneable{
    private final int id;
    private int notch;
    private char notchRightPosition;
    private char initialPosition;
    private List<Position> rotorPositions;
    @Override
    public Rotor clone() {
     try {
        Rotor newRotor = (Rotor) super.clone();
        newRotor.rotorPositions = new ArrayList<>(rotorPositions.size());
        for(Position currentPosition : rotorPositions){
                       newRotor.rotorPositions.add(currentPosition.clone());
        }
           return newRotor;
         } catch (CloneNotSupportedException e) {
           throw new AssertionError();
     }
}

        public static class Position implements Cloneable{
                public char right;
               public char left;
                public Position(char right, char left) {
                        this.right = Character.toUpperCase(right);
                        this.left = Character.toUpperCase(left);
                }

                @Override
                public Position clone() {
                        try {
                                Position clone = (Position) super.clone();
                                return clone;
                        } catch (CloneNotSupportedException e) {
                                throw new AssertionError();
                        }
                }
        }
        public int getId() { return id; }
        public int getNotch() { return notch; }
        public char getNotchRightPosition() { return notchRightPosition; }
        public List<Position> getRotorPositions() { return rotorPositions; }
        public void setFirstPosition(char value) {
                this.initialPosition = value;
        }

        public void setNotchRightPosition(char value) {
                this.notchRightPosition = value;
        }
        public int getCurrentNotchRightPosition() throws MachineLogicException {
                for (int i = 0; i < rotorPositions.size(); i++) {
                        if (rotorPositions.get(i).right == notchRightPosition) {
                                return i;
                        }
                }
                throw new MachineLogicException("unknown notch position in rotor number: " + this.id);
        }
        public enum Direction {
                FORWARD, BACKWARD;
        }
        public Rotor(int id, int notch) {
                this.notch = notch;
                this.id = id;
                this.rotorPositions = new ArrayList<>();
        }
        public void setRotorFirstPosition() {
                int StartOfList = 0;
                while (rotorPositions.get(StartOfList).right != initialPosition) {
                        rotorPositions.add(rotorPositions.remove(StartOfList));
                }
        }
        public int RotorOutput(int rotorInput, Direction direction) throws MachineLogicException {
                if (direction == Direction.FORWARD) {
                        for (int i = 0; i < rotorPositions.size(); i++) {
                                if (rotorPositions.get(rotorInput).right == rotorPositions.get(i).left)
                                        return i;
                        }
                }
                else {
                        for (int i = 0; i < rotorPositions.size(); i++) {
                                if (rotorPositions.get(i).right == rotorPositions.get(rotorInput).left)
                                        return i;
                        }
                }
                throw new MachineLogicException("unknown rotor input in rotor number: " + this.id);
        }


}



