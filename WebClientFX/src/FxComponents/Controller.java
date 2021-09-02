package FxComponents;

import SampleProccessing.NucleoBase;
import SampleProccessing.Sequence;
import SampleProccessing.SequenceAnalyzer;
import SampleProccessing.SequenceType;
import javafx.beans.property.Property;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import webSrc.FileReaderWS;
import webSrc.FileReaderWSService;
import webSrc.IOException_Exception;

import java.io.UnsupportedEncodingException;
import java.util.Optional;


public class Controller {

    private webSrc.FileReaderWS service; //SOAP web service to read file from directory (database)
    private SequenceAnalyzer analyzer;
    private TextInputDialog sampleRequestDialog; //sampleID prompt
    private Alert fileErrorAlert;
    Task<Void> loadFile;

    @FXML
    private TextArea informationTxt;

    @FXML
    private Button loadSequenceBtn;

    @FXML
    private Button clearSequenceBtn;

    @FXML
    private TextArea sampleSourceTxt;

    @FXML
    private Button formatSequenceBtn;

    @FXML
    private Button quitBtn;

    @FXML
    private Button computeSequencePropertiesBtn;

    public Controller() {
        service = new FileReaderWSService().getPort(FileReaderWS.class);
        initializeEmptyAnalyzer();
        sampleRequestDialog = new TextInputDialog();
        sampleRequestDialog.setTitle("Input");
        sampleRequestDialog.setHeaderText("Enter Sequence ID:");
        fileErrorAlert = new Alert(Alert.AlertType.ERROR);
        fileErrorAlert.setTitle("Error Occurred");
        fileErrorAlert.setHeaderText("Application cannot read from file or file is not found!");
    }

    /**
     * clears the sample source text area and sets the analyzer to an empty sequence
     * Analyzer functions stop working
     * @param event
     */
    @FXML
    void clearSequenceBtnOnAction(ActionEvent event) {
        sampleSourceTxt.setText(""); //clearing text field
        initializeEmptyAnalyzer(); //clearing the analyzer
    }

    /**
     * Displays information for the sequence as required in the paper.
     * @param event
     * @implNote Analyzer must be properly constructed with a valid sequence from existing txt file
     */
    @FXML
    void computeSequencePropertiesBtnOnAction(ActionEvent event) {
        Sequence sequence;
        SequenceType type;
        try {
            sequence = analyzer.getSequence();
        } catch (UnsupportedEncodingException uee) {
            fileErrorAlert.setHeaderText(uee.getMessage());
            fileErrorAlert.showAndWait();
            return;
        }

        type = sequence.getType();
        if (type == null || type == SequenceType.INVALID) {
            informationTxt.setText("Load a valid biological sequence!");
            return;
        }

        if (type == SequenceType.QUESTIONABLE) {
            informationTxt.setText("Sequence Analyzer has failed determining the sequence type!");
            return;
        }

        String idStr = String.format("%-20s%s", "SequenceID:", sequence.getSequenceID());
        String lengthStr = String.format("%-24s%s", "Length:", String.valueOf(analyzer.getSequenceLength()));
        String typeStr = String.format("%-25s%s", "Type:", type);

        if (type == SequenceType.PROTEIN) {
            String result = idStr + "\n" + lengthStr + "\n" + typeStr;
            informationTxt.setText(result);
            return;
        }

        String avgATGCStr = String.format("%-30s%.2f", "Average Nucleotides(A,T,G,C):", analyzer.getAverageATGC());
        String avgATGCUStr = String.format("%-30s%.2f", "Average A,T,G,C,U:", analyzer.getAverageATGCU());


        String longestAMsg = getLongestOccurrenceMessage(NucleoBase.A);
        String longestTMsg = getLongestOccurrenceMessage(NucleoBase.T);
        String longestGMsg = getLongestOccurrenceMessage(NucleoBase.G);
        String longestCMsg = getLongestOccurrenceMessage(NucleoBase.C);

        String result = idStr + "\n" + lengthStr + "\n" + typeStr + "\n" + avgATGCStr + "\n" + avgATGCUStr + "\n"
                + longestAMsg + "\n" + longestTMsg + "\n" + longestGMsg + "\n" + longestCMsg;

        informationTxt.setText(result);
    }

    /**
     * all letters to lowercase no white spaces
     * @param event
     */
    @FXML
    void formatSequenceBtnOnAction(ActionEvent event) {
        sampleSourceTxt.setText(analyzer.getFormattedSource());
    }

    /**
     * Makes user request to the SOAP service for the file content via thread
     * thread displays error in the text area field and in error alert if exception occurs
     * @param event
     */
    @FXML
    void loadSequenceBtnOnAction(ActionEvent event) {
        Optional<String> result = sampleRequestDialog.showAndWait();
        if (result.isPresent()) {
            initializeTask();
            new Thread(loadFile).start();
        }
    }

    /**
     * quits the application
     * @param event
     */
    @FXML
    void quitBtnOnAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * gets longest occurrence of specified nucleobase letter as specified in the paper
     * @param base the nucleobase given
     * @return String depicting the longest sub-sequence of identical letters
     */
    private String getLongestOccurrenceMessage(NucleoBase base) {
        String longestBase = analyzer.getLongestBaseSequence(base);

        if (longestBase.length() <= 0)
            return String.valueOf(base) + " not found!";

        String longestTMsg = String.format("Longest occurrence of %s's is %s and it appears %d times.", String.valueOf(base), longestBase,
                analyzer.getRepetitionCount(longestBase));
        return longestTMsg;
    }

    /**
     * initializes the task to be executed by the additional thread
     */
    private void initializeTask() {
        loadFile = new Task<Void>() {
            @Override
            protected Void call() throws UnsupportedEncodingException, IOException_Exception {
                try {
                    String sequenceId = sampleRequestDialog.getResult();
                    String sequenceSource = service.readSample(sequenceId);
                    Sequence sequence = new Sequence(sequenceId, sequenceSource);
                    analyzer.setSequence(sequence);
                    updateMessage(sequenceSource);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    initializeEmptyAnalyzer();
                    updateMessage("File selected does not exist or file is unable to read!" + "\n" + exception.getMessage());
                    throw exception;
                }
                return null;
            }
        };

        sampleSourceTxt.textProperty().bindBidirectional((Property<String>) loadFile.messageProperty());
        loadFile.setOnFailed(workerStateEvent -> {
            Throwable throwable = loadFile.getException();
            fileErrorAlert.setHeaderText(throwable.getMessage());
            fileErrorAlert.showAndWait();
        });
        loadFile.setOnCancelled(workerStateEvent -> {
            sampleSourceTxt.setText("File loading has been canceled!");
        });
        loadFile.setOnRunning(workerStateEvent -> {
            sampleSourceTxt.setText("File is loading, please wait...");
        });
        loadFile.setOnScheduled(workerStateEvent -> {
            sampleSourceTxt.setText("File loading is scheduled");
        });
    }

    /**
     * Clears the analyzer. After this method analyzer is in valid but non-functional state
     */
    private void initializeEmptyAnalyzer() {
        try {
            //setting it to a non existing empty sequence with a valid file name
            //so no exceptions occur
            analyzer = new SequenceAnalyzer(new Sequence("file", ""));
        } catch (UnsupportedEncodingException uee) {
            fileErrorAlert.setHeaderText(uee.getMessage());
            fileErrorAlert.showAndWait();
        }
    }

}
