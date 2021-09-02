package SampleProccessing;

/**
 * Represents basic sequence types used in the application
 * @implNote if the sequence source is in incorrect format the type should be invalid
 * @implNote every sequence has QUESTIONABLE type until it gets determined by the analyzer
 */

public enum SequenceType {
    DNA("DNA"),
    RNA("RNA"),
    PROTEIN("Protein"),
    //if the sequence source is in incorrect format the type should be invalid
    INVALID("Invalid Sequence! Type has not been determined!"),
    //every sequence has QUESTIONABLE type until it gets determined by the analyzer
    QUESTIONABLE("Sequence type has not been determined yet!");

    private final String type;

    SequenceType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
