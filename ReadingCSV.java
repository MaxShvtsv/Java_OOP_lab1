package src.firstlab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingCSV{
    public static void main(String[] args){
        
        /* Input class */

        Input inputArgs = new Input(args[0], args[1], args[2].charAt(0), args[3].charAt(0));

        inputArgs.printInputVars();

        /* Reading CSV */

        Read csvReader = new Read(inputArgs.pathToCsv);

        String input = "";
        try {
            input = csvReader.readCsv();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        
        /* Algorithm */

        Divide divider = new Divide(input, inputArgs.csvDelimiter);

        Object[] arrayOfObjects = divider.getTokensLength();
        String[] tokenArray = arrayOfObjects[0].;
        int[] lengthArray = arrayOfObjects[1];

        /* Output */

        Output outputResult = new Output(tokenArray, lengthArray, inputArgs.outDelimiter, inputArgs.pathToOutFile);

        try {
            outputResult.output();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
    }
}

/* Input class */
class Input{
    // pathToCsv: String, path to file with CSV format.
    String pathToCsv;
    // pathToOutFile: String, path to file where to write the result.
    String pathToOutFile;
    // csvDelimiter: char, character as a delimiter in CSV file.
    char csvDelimiter;
    // outDelimiter: char, character as a delimiter in output file.
    char outDelimiter;

    /* Constructor of Input class */

    Input(String givenPathToCsv, String givenPathToOutFile,
          char givenCsvDelimiter, char givenOutDelimiter){
        pathToCsv = givenPathToCsv;
        pathToOutFile = givenPathToOutFile;
        csvDelimiter = givenCsvDelimiter;
        outDelimiter =  givenOutDelimiter;
    }

    /* Initial variables */
    
    // String pathToCsv = "C:\\Users\\PC\\Desktop\\Development\\Java\\src\\firstlab\\test.csv";
    // String pathToOutFile = "C:\\Users\\PC\\Desktop\\Development\\Java\\src\\firstlab\\csv_result.txt";
    // char csvDelimiter = ',';
    // char outDelimiter = '+';
    
    /* Print given arguments onto the screen. */
    void printInputVars(){
        System.out.printf("Path to CSV: '%s',\n" +
                            "Path to resulting file: '%s',\n" +
                            "Delimiter in CSV: '%c',\n" +
                            "Delimiter in resulting file: '%c'\n",
                            pathToCsv, pathToOutFile, csvDelimiter, outDelimiter);
    }
}

/* Read class */
class Read{
    // pathToCsv: String, path to file with CSV format.
    String pathToCsv;

    /* Constructor of Read class */

    Read(String givenPathToCsv){
        pathToCsv = givenPathToCsv;
    }

    /* Gets text into string from given CSV file. */
    String readCsv() throws Exception{
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        
        String input = csvReader.lines().collect(Collectors.joining());
        
        csvReader.close();

        return input;
    }
}

/* Class Divide */
class Divide{
    // input: String, text from CSV file.
    String input;
    // csvDelimiter: char, character as a delimiter in CSV file.
    char csvDelimiter;

    /* Constructor of Divide class */

    Divide(String givenInput, char givenCsvDelimiter){
        input = givenInput;
        csvDelimiter = givenCsvDelimiter;
    }

    // tokenArray: list of tokens of CSV file.
    List<String> tokenArray = new ArrayList<>();

    // start: int, position of token to be added in tokenArray.
    int start = 0;
    // inQuotes: bool, switches to the state where to append data in quotes.
    Boolean inQuotes = false;

    /* Returns lenghts of values in CSV file.
     * returns: int array, lenthgs of tokens.
     */
    Object[] getTokensLength(){
        // inputLength: int, length of CSV file.
        int inputLength = input.length();

        for (int current = 0; current < inputLength; current++) {
            // We check entire inputted string from CSV.

            // If we get a quote char - turn on/off quote state.
            if (input.charAt(current) == '"' && inQuotes == false){
                inQuotes = true;
            }
            try {
                if (input.charAt(current) == '"' && input.charAt(current + 1) == csvDelimiter){
                    inQuotes = false;
                }
            } catch (Exception Ex) {
                inQuotes = false;
            }

            if (input.charAt(current) == csvDelimiter && inQuotes == false){
                // Simple case with no quotes.
                tokenArray.add(input.substring(start, current));
                // Move token sample.
                start = current + 1;
            }
        }
        // Add the last token.
        tokenArray.add(input.substring(start));
    
        // tokenCount: int, count of values in CSV file.
        int tokenCount = tokenArray.size();
        // lengthArray: int array, array of length of every token.
        int lengthArray[] = new int[tokenCount];
    
        /* Count lengths of tokens */
    
        for (int i = 0; i < tokenCount; i++){
            lengthArray[i] = tokenArray.get(i).length();
            System.out.println(tokenArray.get(i));
            if (tokenArray.get(i).charAt(0) == '"'){
                // Remove quote chars in lengths.
                lengthArray[i] -= 2;
            }
        }

        Object[] tokenArrayWithLengths = new Object[2];
        tokenArrayWithLengths[0] = tokenArray;
        tokenArrayWithLengths[1] = lengthArray;

        return tokenArrayWithLengths;
    }
}

/* Class Output */
class Output{
    /* Collecting a string result */

    // lengthArray: int array, array of length of every token.
    int lengthArray[];
    // outDelimiter: char, character as a delimiter in output file.
    char outDelimiter;
    // pathToOutFile: String, path to file where to write the result.
    String pathToOutFile;
    // tokenArray: array of String, array of divided tokens.
    String tokenArray[];

    /* Constructor of Output class */
    Output(String givenTokenArray[], int givenLengthArray[], char givenOutDelimiter, String givenPathToOutFile){
        tokenArray = givenTokenArray;
        lengthArray = givenLengthArray;
        outDelimiter = givenOutDelimiter;
        pathToOutFile = givenPathToOutFile;
    }

    // Print and write lengths of given tokens.
    void output() throws Exception{

        // Get from int array - string.
        String result = Arrays.stream(lengthArray)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(Character.toString(outDelimiter)));

        /* Writing to the given file */

        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToOutFile));

        for (int i = 0; i < lengthArray.length; i++){
            System.out.printf("%d. '%s', '%d'", i + 1, tokenArray[i], lengthArray[i]);
            writer.write(tokenArray[i] + ", " + lengthArray[i]);
        }

        writer.close();
    }
}