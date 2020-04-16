import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    // Node
    public static class Node {
        private static int count = 0;

        public Integer id;
        public Node parent;
        public String name;
        public String value;
        private ArrayList<Node> list;

        public Node(Node parent, String name, String value) {
            ++Node.count;
            this.id = Node.count;
            this.parent = parent;
            this.name = name;
            this.value = value;
            list = value == null ? new ArrayList<>() : null;
        }

        /**
         * add node child to node list
         * @param child
         */
        public void addChild(Node child) {
            list.add(child);
        }

        @Override
        public String toString() {
            String string = "(id=" + id +
                    ", parent_id=" + (parent == null ? "null" : parent.id) +
                    ", name=" + name +
                    ", value=" + (value == null ? "list" : value) + ")\n";
            if (list != null) {
                for (Node childNode : list) {
                    string += childNode.toString();
                }
            }
            return string;
        }
    }

    public static Node root = null;
    public static Node parent = root;
    public static Node node = null;

    public static void main(String[] args) {
        String inputFileName = "";
        String outputFileName = "";
        try {
            inputFileName = args[0];
            outputFileName = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Can't take files names from program arguments");
            System.exit(0);
        }

        FileReader fileReader = createFileReader(inputFileName);
        Scanner scanner = new Scanner(fileReader);

        String inputFileLine;
        String name, value;
        ArrayList<String> words;
        int openBracesCount = 0;

        while (scanner.hasNextLine()) {
            inputFileLine = scanner.nextLine();
            words = new ArrayList<>(Arrays.asList(inputFileLine.split("\\s")));
            while (!words.isEmpty()) {
                if (words.size() == 1) {
                    if (words.get(0).equals("")) break;
                    if (words.get(0).equals("}")) {
                        if (parent == null) wrongDataFormat();
                        parent = parent.parent;
                        --openBracesCount;
                        break;
                    }
                }
                if (words.size() < 3) wrongDataFormat(); // if there are less than 3 words in string
                name = words.get(0);
                value = words.get(2);
                if (!isCorrectNameFormat(name)) wrongDataFormat(); // check node name format
                if (!words.get(1).equals("=")) wrongDataFormat(); // check "="
                if (value.equals("{")) { // name = {
                    ++openBracesCount;
                    if (parent == null) {
                        if (root != null) wrongDataFormat();
                        root = new Node(null, name, null);
                        parent = root;
                    } else {
                        node = new Node(parent, name, null);
                        parent.addChild(node);
                        parent = node;
                    }
                    words.subList(0, 3).clear(); // remove first 3 words
                } else if (isCorrectValueFormat(value)) { // check node value format
                    node = new Node(parent, name, value);
                    parent.addChild(node);
                    words.subList(0, 3).clear();
                } else {
                    wrongDataFormat();
                }

            }
        }
        // if the quantity of "{" doesn't equal to the quantity of "}"
        if (openBracesCount != 0) wrongDataFormat();
        FileWriter fileWriter = createFileWriter(outputFileName);
        try {
            fileWriter.write(root.toString());
        } catch (IOException e) {
            System.out.println("Can't write to output file");
            System.exit(0);
        } catch (NullPointerException e) {
            System.exit(0);
        }
        closeFileStreams(fileReader, fileWriter);
    }

    // if data format is wrong
    public static void wrongDataFormat() {
        System.out.println("Неверный формат данных");
        System.exit(0);
    }

    /**
     * @param value
     * @return true if value format is correct
     */
    public static boolean isCorrectValueFormat(String value) {
        if (value.charAt(0) != '\"' || value.charAt(value.length() - 1) != '\"') {
            return false;
        }
        String substring = value.substring(1, value.length() - 2);
        return !substring.contains("\"") && !substring.contains("\n");
    }

    /**
     * @param name
     * @return true if name format is correct
     */
    public static boolean isCorrectNameFormat(String name) {
        if (name.charAt(0) >= '0' && name.charAt(0) <= '9') { // if the first chat is number
            return false;
        }
        char character;
        for (int i = 0; i < name.length(); i++) {
            character = name.charAt(i);
            if ((character >= '0' && character <= '9')
                    || (character >= 'A' && character <= 'Z')
                    || (character >= 'a' && character <= 'z')
                    || character == '_') {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * @param inputFileName
     * @return new FileReader
     */
    public static FileReader createFileReader(String inputFileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(inputFileName);
        } catch (FileNotFoundException e) {
            System.out.println("Input file does not exist");
            System.exit(0);
        }
        return fileReader;
    }

    /**
     * @param outputFileName
     * @return new FileWriter
     */
    public static FileWriter createFileWriter(String outputFileName) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputFileName);
        } catch (IOException e) {
            System.out.println("Output file does not exist");
            System.exit(0);
        }
        return fileWriter;
    }

    /**
     * Close all file streams
     * @param fileReader
     * @param fileWriter
     */
    public static void closeFileStreams(FileReader fileReader, FileWriter fileWriter) {
        try {
            fileReader.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Can't close file");
            System.exit(0);
        }
    }
}
