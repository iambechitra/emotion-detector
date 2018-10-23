package utility;

import classifier.NaiveBayesClassifier;
import classifier.ProbabilisticTopicModel;
import handler.DataPreProcessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Support {
    protected double RequestMeanValue(ArrayList<Double> data) {
        double mean = 0;
        for(Double d : data)
            mean += d;

        return (mean / data.size());
    }

    public List<HashMap<String, Double>> requestArticleAnalyzer(DataPreProcessing preProcessing, NaiveBayesClassifier classifier) throws IOException {
        List<HashMap<String, Double>> decisionSet = new ArrayList<>();

        for(String str : preProcessing.RequestArticleProcessor())
            decisionSet.add(classifier.analyze(str));

        return decisionSet;
    }

    public List<HashMap<String, Double>> requestArticleAnalyzer(DataPreProcessing preProcessing, ProbabilisticTopicModel classifier) throws IOException {
        List<HashMap<String, Double>> decisionSet = new ArrayList<>();

        for(String str : preProcessing.RequestArticleProcessor())
            decisionSet.add(classifier.analyze(str));

        return decisionSet;
    }

    public double getNormalizedValue(double value, double min, double max) {
        return ((value-min)/(max-min))*5+1; //normalized in the range of 1-6;
    }
}
