package src.firstlab.Java_OOP_lab1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingCSV{
    public static void main(String[] args) throws Exception{
        
        /* Input class */

        Input inputArgs = new Input(args[0], args[1], args[2].charAt(0), args[3].charAt(0));

        inputArgs.printInputVars();

        /* Reading CSV */

        Read csvReader = new Read(inputArgs.pathToCsv, inputArgs.csvDelimiter);

        // String input = "";

        String lineArray[] = csvReader.readCsv().toArray(new String[0]);

        // try {
        //     input = csvReader.readCsv();
        // } catch (Exception Ex) {
        //     Ex.printStackTrace();
        // }
        
        /* Algorithm */

        Divide divider = new Divide(lineArray, inputArgs.csvDelimiter);

        String dataTokens[] = divider.getTokens();

        /* Output */

        // Output outputResult = new Output(tokenArray, lengthArray, inputArgs.outDelimiter, inputArgs.pathToOutFile);

        // try {
        //     outputResult.output();
        // } catch (Exception Ex) {
        //     Ex.printStackTrace();
        // }
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

    // csvDelimiter: char, sign which seperates tokens in csv.
    char csvDelimiter;

    // tokenArray: list of strings, array of values in csv.
    List<String> lineArray = new ArrayList<>();

    /* Constructor of Read class */

    Read(String givenPathToCsv, char givenCsvDelimiter){
        pathToCsv = givenPathToCsv;
        csvDelimiter = givenCsvDelimiter;
    }

    /* Gets text into string from given CSV file. */
    List<String> readCsv() throws Exception{
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));

        String row;
        while ((row = csvReader.readLine()) != null) {
            lineArray.add(row);
            // do something with the data
        }
        // String input = csvReader.lines().collect(Collectors.joining());
        
        csvReader.close();

        return lineArray;
    }
}

/* Class Divide */
class Divide{
    // input: list of strings, lines from CSV file.
    String input[];
    // csvDelimiter: char, char acter as a delimiter in CSV file.
    char csvDelimiter;

    /* Constructor of Divide class */

    Divide(String givenInput[], char givenCsvDelimiter){
        input = givenInput;
        csvDelimiter = givenCsvDelimiter;
    }
    
    // start: int, position of token to be added in tokenArray.
    int start = 0;
    
    // inQuotes: bool, switches to the state where to append data in quotes.
    Boolean inQuotes = false;
    
    /* Returns lenghts of values in CSV file.
    * returns: int array, lenthgs of tokens.
    */
    String[] getTokens(){
        // inputLength: int, length of CSV file.
        // int inputLength = input.length();
        
        // tokenArray: list of tokens of CSV file.
        List<String> tokenArray = new ArrayList<>();
        
        // lineCount: int, count of lines in csv.
        int lineCount = input.length;
        
        for (int currentLine = 0; currentLine < lineCount; currentLine++){

            for (int currentCharIndex = 0; currentCharIndex < input[currentLine].length(); currentCharIndex++){

                if (input[currentLine].charAt(currentCharIndex) == '"'){
                    inQuotes = inQuotes ? false : true;
                }

                if (input[currentLine].charAt(currentCharIndex) == csvDelimiter && inQuotes == false){
                    // Simple case with no quotes.
                    tokenArray.add(input[currentLine].substring(start, currentCharIndex));
                    // Move token sample.
                    start = currentCharIndex + 1;
                }
            }
            // Add the last token in line.
            tokenArray.add(input[currentLine].substring(start));
            inQuotes = false;
            start = 0;
        }

    
        // // tokenCount: int, count of values in CSV file.
        // int tokenCount = tokenArray.size();

        // // lengthArray: int array, array of length of every token.
        // int lengthArray[] = new int[tokenCount];
    
        // /* Count lengths of tokens */
    
        // for (int i = 0; i < tokenCount; i++){
        //     lengthArray[i] = tokenArray.get(i).length();
        //     System.out.println(tokenArray.get(i));
        //     if (tokenArray.get(i).charAt(0) == '"'){
        //         // Remove quote chars in lengths.
        //         lengthArray[i] -= 2;
        //     }
        // }

        // Object[] tokenArrayWithLengths = new Object[2];
        // tokenArrayWithLengths[0] = tokenArray;
        // tokenArrayWithLengths[1] = lengthArray;

        // return tokenArrayWithLengths;
        // return new Object[]{tokenArray, lengthArray};
        // String data[] = tokenArray.toArray(new String[0]);

        for (String i : tokenArray){
            System.out.println(i);
        }
        return tokenArray.toArray(String[]::new);
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
        // String result = Arrays.stream(lengthArray)
        //         .mapToObj(String::valueOf)
        //         .collect(Collectors.joining(Character.toString(outDelimiter)));

        /* Writing to the given file */

        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToOutFile));

        for (int i = 0; i < lengthArray.length; i++){
            System.out.printf("%d. '%s', '%d'", i + 1, tokenArray[i], lengthArray[i]);
            writer.write(tokenArray[i] + ", " + lengthArray[i]);
        }

        writer.close();
    }
}