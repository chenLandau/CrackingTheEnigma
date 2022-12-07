package DataTransferObject;

public class RotorsCountDTO {
    private int rotorsCount;
    private int totalRotorsNumber;
    public RotorsCountDTO(int rotorsCount,int totalRotorsNumber) {
        this.rotorsCount = rotorsCount;
        this.totalRotorsNumber = totalRotorsNumber;
    }
    public int getRotorsCount() { return rotorsCount; }
    public int getTotalRotorsNumber() { return totalRotorsNumber; }
}
