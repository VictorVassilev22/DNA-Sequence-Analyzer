package SampleProccessing;

/**
 * Representns the basic DNA and RNA nucleobases and their alphabet characters.
 * @implNote Note that the characters are given in lowercase!
 */
public enum NucleoBase {
    A('a'),
    T('t'),
    G('g'),
    C('c'),
    U('u');

    private final Character letter;

    NucleoBase(Character letter){
        this.letter = letter;
    }

    public Character getLetter() {
        return letter;
    }
}
