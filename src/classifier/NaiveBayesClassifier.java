package classifier;

import utility.Support;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class NaiveBayesClassifier extends Support {
    protected HashMap<String, String>dataSet;
    protected List<String>distinctWordSet;
    protected List<String>distinctDecisionSet;
    protected HashMap<String, HashMap<String, Double>> probability;
    protected HashMap<String, Integer> repetitionOfDecisionParameter;
    protected HashMap<String, Double> probabilityOfDecisionParameter;


    public NaiveBayesClassifier(HashMap<String, String> dataSet) {
        this.dataSet = dataSet;

        distinctWordSet = new ArrayList<>();
        distinctDecisionSet = new ArrayList<>();
        repetitionOfDecisionParameter = new HashMap<>();
        probabilityOfDecisionParameter = new HashMap<>();
        probability = new HashMap<>();

        generateWordSet();
        generateDecisionSet();
        generateRepetitionOfDecisionParameter();
        generateProbabilityOfDecisionParameter();
        calculateProbability();
    }

    private void generateWordSet() {
        for(Map.Entry<String, String>map : dataSet.entrySet()) {
            StringTokenizer stk = new StringTokenizer(map.getKey());
            while(stk.hasMoreTokens()) {
                String next = stk.nextToken();
                if(!distinctWordSet.contains(next))
                    distinctWordSet.add(next);
            }
        }

    }

    private void generateDecisionSet() {
        for(Map.Entry<String, String>map : dataSet.entrySet()) {
            String next = map.getValue();

            if(!distinctDecisionSet.contains(next))
                distinctDecisionSet.add(next);

        }

    }

    private void generateRepetitionOfDecisionParameter() {
        for(String str : distinctDecisionSet) {
            int counter = 0;
            for(Map.Entry<String, String> map : dataSet.entrySet()) {
                if(str.equals(map.getValue()))
                    counter++;
            }
            repetitionOfDecisionParameter.put(str, counter);
        }
    }

    private void generateProbabilityOfDecisionParameter() {
        int row = dataSet.size();
        for(Map.Entry<String, Integer>map : repetitionOfDecisionParameter.entrySet()) {
            double probability = (double)map.getValue() / (double) row;
            probabilityOfDecisionParameter.put(map.getKey(), probability);
        }
    }

    private void calculateProbability() {
        for(String word : distinctWordSet) {
            probability.put(word, new HashMap<>());
            for(String decision : distinctDecisionSet) {
                int counter = 0;
                for(Map.Entry<String, String> map : dataSet.entrySet()) {
                    if(map.getKey().contains(word) && map.getValue().equals(decision))
                        counter++;
                }
                double prob = (double)counter / (double)repetitionOfDecisionParameter.get(decision);

                probability.get(word).put(decision, prob);
            }
        }
    }

    public HashMap<String, Double> analyze(String text) {
        ArrayList<Double> valueSetter = new ArrayList<>();

        StringTokenizer token = new StringTokenizer(text.toLowerCase());
        String[]array = new String[token.countTokens()];

        int i = 0;
        while(token.hasMoreTokens()) {
            array[i] = token.nextToken();
            i++;
        }

        HashMap<String, Double> list = new HashMap<>();

        for(Map.Entry<String, Integer> map : repetitionOfDecisionParameter.entrySet()) {
            double estimateProbability = 1.0;
            for(int k = 0; k < array.length; k++) {
                double value = 1;

                if(probability.containsKey(array[k])) {
                    value = probability.get(array[k]).get(map.getKey());
                    valueSetter.add(value);
                }

                estimateProbability *= value;
            }

            if(valueSetter.size() != array.length && valueSetter.size() > 0) {

                double difference = Math.abs(array.length - valueSetter.size());
                double mean = RequestMeanValue(valueSetter);
                difference *= mean;
                estimateProbability *= difference;
            }

            estimateProbability *= probabilityOfDecisionParameter.get(map.getKey());
            list.put(map.getKey(), estimateProbability);
        }

        List<Map.Entry<String, Double>> sortedList = new LinkedList(list.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        list.clear();
        for(Map.Entry<String, Double> map : sortedList) {
            list.put(map.getKey(), map.getValue());
        }

        return list;
    }

    public void showProbabilityMap() {
        for(Map.Entry<String, HashMap<String, Double>> map : probability.entrySet())

            for(Map.Entry<String, Double>m : map.getValue().entrySet())
                System.out.println("P("+map.getKey()+"|"+m.getKey()+") = "+m.getValue());

        System.out.println();
    }

    public void articleAnalyzer(List<HashMap<String, Double>> lst) {
        HashMap<String, Double> combineResult = new HashMap<>();
        for(String str : distinctDecisionSet) {
            double result = 1;

            for(HashMap<String, Double> map : lst) {
               // System.out.println(map.size()+" map size "+map.);
                double value = map.get(str);
                result *= (value*Math.pow(10, 3));
            }

            combineResult.put(str, result*probabilityOfDecisionParameter.get(str));
        }

        List<Map.Entry<String, Double>> value = new LinkedList<>(combineResult.entrySet());
        Collections.sort(value, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        System.out.println("[Naive Bayes Classifier] Decission = "+value.get(value.size()-1).getKey()
                +" and prob "+value.get(value.size()-1).getValue());
    }

    public void showZeroProbabilityMap() {
        int counter = 0;
        for(Map.Entry<String, HashMap<String, Double>> map : probability.entrySet()) {

            for (Map.Entry<String, Double> m : map.getValue().entrySet()) {
                if (m.getValue() <= 0.0) {
                    System.out.println("P(" + map.getKey() + "|" + m.getKey() + ") = " + m.getValue());
                    counter++;
                }
            }
        }

        System.out.println();
        System.out.println("Probability doesn't exists for "+counter+" words");
    }

    public void showProbability() {
        for(Map.Entry<String, Integer> m : repetitionOfDecisionParameter.entrySet())
            System.out.println(m.getKey()+" "+m.getValue());

        for(Map.Entry<String, Double> m : probabilityOfDecisionParameter.entrySet())
            System.out.println(m.getKey()+" -> "+m.getValue());
    }

}
