package DataTransferObject;

import java.util.Set;

public class DictionaryWordsDTO {
    private Set<String> dictionaryWords;
    public DictionaryWordsDTO(Set<String> dictionaryWords){
        this.dictionaryWords = dictionaryWords;
    }
    public Set<String> getDictionaryWords(){
        return dictionaryWords;
    }
}
