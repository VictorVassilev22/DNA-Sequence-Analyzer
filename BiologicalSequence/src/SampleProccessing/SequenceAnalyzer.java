package SampleProccessing;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aims to analyze a biological sequence given in the constructor or passed in the set method
 * @implNote Every sequence object has QUESTIONABLE (undetermined) type until its type gets
 * analyzed by the public void calculateSequenceType(String source) method!
 */
public class SequenceAnalyzer {
    private static final double SEQ_THRESHOLD = 0.85;

    private Sequence sequence;
    private String formattedSource; //Analyzer needs the formatted source of the sequence to do proper calculations

    public SequenceAnalyzer(){

    }

    public SequenceAnalyzer(Sequence sequence) {
        setSequence(sequence);
    }

    public String getFormattedSource() {
        return formattedSource;
    }

    public void setFormattedSource() {
        this.formattedSource = sequence.formatSource();
    }

    public Sequence getSequence() throws UnsupportedEncodingException {
        return new Sequence(sequence);
    }

    /**
     * Sets the sequence to be analyzed;
     * can switch to another sequence with this method at anytime.
     * It is the user responsibility do determine the type of the sequence
     * @param sequence should not be null
     */
    public void setSequence(@NotNull Sequence sequence) {
        this.sequence = sequence;
        setFormattedSource();
        calculateSequenceType();
    }

    /**
     * gets called by setSequence(@NotNull Sequence sequence)
     * to set its type field. It is the user responsibility do determine the type of the sequence
     * @implNote Sequence type gets determined by the algorithm described in the paper
     */
    public void calculateSequenceType(){
        SequenceType st;

        Pattern pattern = Pattern.compile("[atgcudefhiklmnpqrsvwy]+");
        Matcher matcher = pattern.matcher(formattedSource);

        if(!matcher.matches()){
            st = SequenceType.INVALID;
            sequence.setType(st);
            return;
        }

        double averageATGC = getAverageATGC();
        double averageATGCU = getAverageATGCU();

        if(averageATGC > SEQ_THRESHOLD){
            st = SequenceType.DNA;
        }else if(averageATGCU > SEQ_THRESHOLD){
            st=SequenceType.RNA;
        }else{
            st = SequenceType.PROTEIN;
        }

        sequence.setType(st);
    }

    /**
     * @return the total number of characters in the sequence
     */
    public int getSequenceLength(){
        return formattedSource.length();
    }

    /**
     * @return the average occurrence of A's, T's, G's, and C's
     */
    public double getAverageATGC(){
        String withoutATGC = formattedSource.replaceAll("[actgn]+", "");
        int totalLength = getSequenceLength();
        int ATGCCount = totalLength - withoutATGC.length();

        return ATGCCount/(double)totalLength;
    }

    /**
     * @return the average occurrence of A's, T's, G's, C's and U's
     */
    public double getAverageATGCU(){
        String withoutATGCU = formattedSource.replaceAll("[actgun]+", "");
        int totalLength = getSequenceLength();
        int ATGCCount = totalLength - withoutATGCU.length();

        return ATGCCount/(double)totalLength;
    }

    /**
     * @param base The NucleoBase needed
     * @return the longest sequence of characters of that NucleoBase
     */
    public String getLongestBaseSequence(@NotNull NucleoBase base){
        Character symbol = base.getLetter();
        int counter = 0;
        int maxSubLength = 0;
        char[] charArr = formattedSource.toCharArray();
        int length = charArr.length;

        for (int i = 0; i < length; i++) {
            if(charArr[i]==symbol){
                counter++;
            }else if(charArr[i]!=symbol || i == length-1){
                if(counter>maxSubLength)
                    maxSubLength = counter;
                counter = 0;
            }
        }
        String result = new String(new char[maxSubLength]);
        result = result.replace('\0', symbol);
        return result;
    }

    /**
     * @param substring a substring of identical characters (NucleoBases)
     * @return the number of times the substring appears in the source
     */
    public int getRepetitionCount(String substring){
        Pattern pattern = Pattern.compile(substring);
        Matcher matcher = pattern.matcher(formattedSource);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
