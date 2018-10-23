package classifier;

import java.util.*;

public class ProbabilisticTopicModel extends NaiveBayesClassifier{
    private HashMap<String, Double> idfValue = new HashMap<>();
    public ProbabilisticTopicModel(HashMap<String, String> dataSet) {
        super(dataSet);
        generateIDFValue();
    }

    public void generateIDFValue() {
        double dataSetSize = dataSet.size();
        for(String str : distinctWordSet) {
            double repetitionCounter = 0;
            for(Map.Entry<String, String> map : dataSet.entrySet()) {
                if(map.getKey().contains(str))
                    repetitionCounter++;
            }

            idfValue.put(str, Math.log(dataSetSize / repetitionCounter));
        }
    }

    public HashMap<String, Double> analyze(String text) {
        StringTokenizer stk = new StringTokenizer(text);
        String arr[] = new String[stk.countTokens()];
        HashMap<String, Double> emoList = new HashMap<>();

        int i = 0;
        while (stk.hasMoreTokens())
            arr[i++] = stk.nextToken();


        for(i = 0; i < arr.length; i++) {
            double idf = 1.0;
            if(idfValue.containsKey(arr[i]))
                idf = idfValue.get(arr[i]);

            if(probability.containsKey(arr[i])) {
                for(Map.Entry<String, Double> map : probability.get(arr[i]).entrySet()) {
                    if(emoList.containsKey(map.getKey())) {
                        double priv = emoList.get(map.getKey());
                        emoList.put(map.getKey(), priv*map.getValue()*idf);
                    } else
                        emoList.put(map.getKey(), map.getValue()*idf);
                }
            }
        }

        return emoList;
    }

    public void articleAnalyzer(List<HashMap<String, Double>> lst) {
        HashMap<String, Double> combineResult = new HashMap<>();

        for(String str : distinctDecisionSet) {
            double result = 0;

            for(HashMap<String, Double> map : lst)
                if(map.containsKey(str))
                    result += map.get(str);

            combineResult.put(str, result);
        }

        List<Map.Entry<String, Double>> value = new LinkedList<>(combineResult.entrySet());
        Collections.sort(value, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        System.out.println("[Probabilistic Topic Model] Decission = "+value.get(value.size()-1).getKey()
                +" and score "+value.get(value.size()-1).getValue());
    }

    public void printMap(List<Map.Entry<String, Double>> mm) {
        for (Map.Entry<String, Double> m : mm) {
            System.out.println(m.getKey()+" value = "+m.getValue());
        }
    }
}
