package SampleProccessing;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This object represents a single biological sequence
 */

public class Sequence {
    private String sequenceID;
    private String sequenceSource;
    private SequenceType type;

    public Sequence(String sequenceID, String sequenceSource) throws UnsupportedEncodingException{
       setSequenceID(sequenceID);
       setSequenceSource(sequenceSource);
       setType(SequenceType.QUESTIONABLE);
    }

    public Sequence(@NotNull Sequence other) throws UnsupportedEncodingException{
        setSequenceID(other.getSequenceID());
        setSequenceSource(other.getSequenceSource());
        setType(other.getType());
    }

    public String getSequenceID() {
        return sequenceID;
    }

    /**
     * @param sequenceID represents the name of the file without the file extension
     * @throws UnsupportedEncodingException when the sequenceID is not a valid filename
     */
    public void setSequenceID(String sequenceID) throws UnsupportedEncodingException {
        Pattern filePattern = Pattern.compile("[A-Za-z0-9_-]+"); //file names can usually be empty ("") but the application requires some non-empty name
        Matcher matcher = filePattern.matcher(sequenceID);

        if(!matcher.matches()){
            throw new UnsupportedEncodingException("The sample ID does not belong to a file name!");
        }

        if(sequenceID!=null)
            this.sequenceID = sequenceID;
    }

    public String getSequenceSource() {
        return sequenceSource;
    }

    public void setSequenceSource(String sequenceSource) {
        if(sequenceSource!=null)
            this.sequenceSource = sequenceSource;
    }

    public SequenceType getType() {
        return type;
    }

    /**
     * !!! First a SequenceAnalyzer object must be created with this sequence passed in the constructor (or set manually)
     *  Do not call this method without a reason. Instead create SequenceAnalyzer object.
     * @param type the type to be set
     */
    protected void setType(SequenceType type) {
        if(type!=null)
            this.type = type;
    }


    /**
     * @implNote Does not modify the original source of the sequence
     * @return returns a String containing the formatted source of the sequence
     */
    public String formatSource(){
        String sourceCopy = sequenceSource.replaceAll("\\s+", "");
        Stream<Character> charStream = sourceCopy.chars().mapToObj(x->(char)x).map(Character::toLowerCase);
        String result = charStream.map(String::valueOf).collect(Collectors.joining());
        return result;
    }
}
