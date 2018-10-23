package handler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class DataPreProcessing {
    static final String FILE_PATH ="/home/bechitra/IdeaProjects/Java/Program/src/file/trainingSet.bat";
    static final String FILE_PATH_TEST ="/home/bechitra/IdeaProjects/Java/Program/src/file/input.bat";
    static final String FILE_PATH_ARTICLE ="/home/bechitra/IdeaProjects/Java/Program/src/file/article.bat";

    private HashMap<String, String> dataSet;

    public DataPreProcessing() throws IOException {

        InputStreamReader isr = new InputStreamReader(new FileInputStream(FILE_PATH));
        BufferedReader br = new BufferedReader(isr);
        dataSet = new HashMap<>();

        String data = "";

        while((data=br.readLine()) != null) {
            StringTokenizer token = new StringTokenizer(data.trim());

            int i = 0;
            String[] str = new String[token.countTokens()];

            while(token.hasMoreTokens())
                str[i++] = token.nextToken();

            StringBuilder stringBuilder = new StringBuilder("");
            for(int k = 0; k < str.length-1; k++)
                stringBuilder.append(str[k]).append(" ");

            dataSet.put(stringBuilder.toString().trim(), str[str.length-1]);
        }
    }

    public List<String> getTestList() throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(FILE_PATH_TEST));
        BufferedReader br = new BufferedReader(isr);
        List<String> test = new ArrayList<>();

        String data = "";

        while((data=br.readLine()) != null)
            test.add(data);

        return test;

    }

    public List<String> RequestArticleProcessor() throws IOException {

        InputStreamReader isr = new InputStreamReader(new FileInputStream(FILE_PATH_ARTICLE));
        BufferedReader br = new BufferedReader(isr);
        ArrayList<String> line = new ArrayList<>();
        String inputData = "";

        while((inputData = br.readLine()) != null)
            line.add(inputData);

        String plainString = GeneratePlainString(line).toString().trim();
        StringTokenizer stk = new StringTokenizer(plainString, "।");
        line.clear();
        while(stk.hasMoreTokens())
            line.add(stk.nextToken().trim());

        return line;
    }

    public void addToDataSet(String text, String decision) { dataSet.put(text, decision); }

    public HashMap<String, String> getDataSet() { return dataSet; }

    private StringBuilder GeneratePlainString(List<String> lineList) {
        StringBuilder bigString = new StringBuilder("");

        for(String str : lineList)
            bigString.append(str).append(" ");

        return bigString;
    }

}
