import classifier.NaiveBayesClassifier;
import classifier.ProbabilisticTopicModel;
import handler.DataPreProcessing;
import utility.Support;

import java.io.*;

public class Main{

    public static void main(String[] args) throws IOException {
        System.out.println("Processing");
        DataPreProcessing dataPreProcessing = new DataPreProcessing();
        ProbabilisticTopicModel classifier = new ProbabilisticTopicModel(dataPreProcessing.getDataSet());
        NaiveBayesClassifier bayesClassifier = new NaiveBayesClassifier(dataPreProcessing.getDataSet());

        //classifier.showZeroProbabilityMap();
        Support support = new Support();

        classifier.articleAnalyzer(support.requestArticleAnalyzer(dataPreProcessing, classifier));
        bayesClassifier.articleAnalyzer(support.requestArticleAnalyzer(dataPreProcessing, bayesClassifier));
        //bayesClassifier.showProbability();
    }
}