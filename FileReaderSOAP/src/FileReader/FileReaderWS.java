package FileReader;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *  A web SOAP service to be used by client
 */

@WebService()
public class FileReaderWS {

    //An example of a file path to directory containing .txt files (database)
    private final String filesDirectoryPath = "C:\\Users\\AORUS\\Downloads\\Project9"; //specify your path here
    private final String fileExtension = ".txt"; //specify required extension here

    /**
     * Web method
     * @param SampleID the name of the text file (without the filename extension)
     * @implNote Reads only files with .txt extension
     * @implNote Reading files with size >= 2GB is not recommended
     * @return String consisting of the file's contents
     * @throws IOException, NotDirectoryException
     */
    @WebMethod
    public String readSample(String SampleID) throws IOException {
        File targetDirectory = new File(filesDirectoryPath);

        //checking validation of the specified directory
        if(targetDirectory.isFile()){
            throw new NotDirectoryException("Directory path is not valid!");
        }

        //constructing the file path
        Path filePath =  Paths.get(filesDirectoryPath+"\\"+SampleID+fileExtension);

        //checking for file existence in the specified directory
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("Such file does not exist!");
        }

        String result = null;

        //reading file content
        try {
            byte[] byteArray = Files.readAllBytes(filePath);
            result = new String(byteArray);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IOException("File cannot be read!");
        }

        // the result returned if the service executes properly represents the raw text content of the file
        return result;
    }

    public static void main(String[] args) {
        //specifying local server address and publishing the web service
        Object implementor = new FileReaderWS();
        String address = "http://localhost:9000/FileReader";
        Endpoint.publish(address, implementor);
    }
}
