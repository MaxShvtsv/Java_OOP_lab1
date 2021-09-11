// Lab. 1
// Max Shevtsov
// KM-01

package src.firstlab.Java_OOP_lab1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ReadingCSV{
    public static void main(String[] args) throws Exception{
        
        /* Input class */

        Input inputArgs = new Input(args[0], args[1], args[2].charAt(0), args[3].charAt(0));

        inputArgs.printInputVars();

        /* Reading CSV */

        Read csvReader = new Read(inputArgs.pathToCsv, inputArgs.csvDelimiter);

        String lineArray[] = csvReader.readCsv().toArray(new String[0]);
        
        /* Algorithm */

        Divide divider = new Divide(lineArray, inputArgs.csvDelimiter);

        // tokensArray: array of strings, array of divided tokens.
        String tokensArray[] = divider.getTokens();

        // tokenLengths: array of int, array of lengths of divided tokens.
        int tokenLengths[] = divider.getLengths();

        /* Output */

        Output outputResult = new Output(tokensArray, tokenLengths, inputArgs.outDelimiter, inputArgs.pathToOutFile);

        outputResult.output();
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
        // csvReader: BufferedReader, reader of file provided by Java.
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));

        // row: string, current row in .csv file.
        String row;
        while ((row = csvReader.readLine()) != null) {
            lineArray.add(row);
        }
        
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
    
    // tokenArray: list of tokens of CSV file.
    List<String> tokenArray = new ArrayList<>();

    /* Returns lenghts of values in CSV file.
    * returns: int array, lenthgs of tokens.
    */
    String[] getTokens(){
        
        // lineCount: int, count of lines in csv.
        int lineCount = input.length;
        
        for (int currentLine = 0; currentLine < lineCount; currentLine++){

            // currentLineLength: int, length of current line in .csv file
            int currentLineLength = input[currentLine].length();

            for (int currentCharIndex = 0; currentCharIndex < currentLineLength; currentCharIndex++){

                if (input[currentLine].charAt(currentCharIndex) == '\"' && inQuotes == false){
                    inQuotes = true;
                } else if ((input[currentLine].charAt(currentCharIndex) == '\"' && inQuotes == true)){
                    inQuotes = false;
                }

                if (input[currentLine].charAt(currentCharIndex) == csvDelimiter && inQuotes == false){
                    // currentToken: string, current token in a line.
                    String currentToken = input[currentLine].substring(start, currentCharIndex);

                    tokenArray.add(currentToken);

                    // Move token pointer.
                    start = currentCharIndex + 1;
                }
            }
            // Add the last token in line.
            tokenArray.add(input[currentLine].substring(start));

            // In the end of the line turn off inQuotes variable.
            inQuotes = false;
            start = 0;
        }

        return tokenArray.toArray(String[]::new);
    }
    
    int[] getLengths(){
        // tokenCount: int, count of values in CSV file.
        int tokenCount = tokenArray.size();

        // lengthArray: int array, array of length of every token.
        int lengthArray[] = new int[tokenCount];
    
        /* Count lengths of tokens */
    
        for (int i = 0; i < tokenCount; i++){
            lengthArray[i] = tokenArray.get(i).length();

            if (tokenArray.get(i).charAt(0) == '"'){
                // Remove quote chars in lengths.
                lengthArray[i] -= 2;
            }
        }
        return lengthArray;
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

        /* Writing to the given file */

        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToOutFile));

        for (int i = 0; i < lengthArray.length; i++){
            writer.write(tokenArray[i] + String.format(" %c ", outDelimiter) + lengthArray[i] + "\n");
        }

        writer.close();
    }
}