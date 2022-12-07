package BruteForce;

import MyMachine.Machine;
import jaxbEnigma.CTEDictionary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Dictionary {
    private String excludedChars;
    private Set<String> dictionaryWords = new HashSet<>();
    private Set<String> validDictionaryWords;
    public String getExcludedChars(){return  excludedChars;}
    public Set<String> getValidDictionaryWords() {return validDictionaryWords;}
    public void chargeDictionary(CTEDictionary cteDictionary, Machine machine){
        String allWords = cteDictionary.getWords().trim().toUpperCase();
        excludedChars = "[" + cteDictionary.getExcludeChars() + "]";
        String validWords = allWords.replaceAll(excludedChars, "");
        String[] validWordsArr = validWords.split(" ");

        dictionaryWords = new HashSet<>(Arrays.asList(validWordsArr));
        createValidDictionaryWords(machine.getABC());
    }

    public void createValidDictionaryWords(List<Character> ABC){
        validDictionaryWords = dictionaryWords.stream().collect(Collectors.toSet());

        for (String word : validDictionaryWords) {
            for (int i =0; i< word.length(); i ++){
                if(!ABC.contains(word.charAt(i))){
                    validDictionaryWords.remove(word);
                    break;
                }
            }
        }
    }




}
