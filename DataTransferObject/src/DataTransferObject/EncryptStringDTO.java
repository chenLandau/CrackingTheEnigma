package DataTransferObject;

public class EncryptStringDTO {
    private String encryptedString;

    private CodeConfigurationOutputDTO codeConfigurationOutputDTO;

    public EncryptStringDTO(String encryptedString, CodeConfigurationOutputDTO codeConfigurationOutputDTO){
        this.encryptedString = encryptedString;
        this.codeConfigurationOutputDTO = codeConfigurationOutputDTO;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public CodeConfigurationOutputDTO getCodeConfigurationOutputDTO() {
        return codeConfigurationOutputDTO;
    }
}
