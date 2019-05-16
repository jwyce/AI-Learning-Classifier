package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class NaiveBayes extends ClassificationMethod {

    int fineness, range, numTrainPoints;
    HashMap<FeatureVector, Integer>[] training_points;
    
    @SuppressWarnings("unchecked")
    public NaiveBayes(int fineness, int range) {
        this.fineness = fineness;
        this.range = range;
        numTrainPoints = 0;
        training_points = new HashMap[range];
        for (int i = 0; i < range; i++)
            training_points[i] = new HashMap<FeatureVector, Integer>();
    }
    
    @Override
    public void train(ArrayList<char[][]> training_data, int[] training_labels, ArrayList<char[][]> validation_data,
            int[] validation_labels, double trainPercent) {
        
        Random rand = new Random();
        
        ArrayList<char[][]> data = new ArrayList<char[][]>();
        data.addAll(training_data);
        data.addAll(validation_data);
        
        ArrayList<Integer> labels = new ArrayList<Integer>();
        for (int i = 0; i < training_labels.length; i++) 
            labels.add(training_labels[i]);
        for (int i = 0; i < validation_labels.length; i++)
            labels.add(validation_labels[i]);
        
        while ((double) numTrainPoints / (training_data.size() + validation_data.size()) < trainPercent) {
            int i = rand.nextInt(data.size());
            training_points[labels.get(i)].put(new FeatureVector(data.get(i), fineness), labels.get(i));
            numTrainPoints++;
            data.remove(i);
            labels.remove(i);
        }
    }

    @Override
    public int classify(char[][] data) {
        FeatureVector v = new FeatureVector(data, fineness);
        ArrayList<Double> probabilities = new ArrayList<Double>();
        
        for (int i = 0; i < range; i++) {
            double pr = 0;
            for (int j = 0; j < v.size(); j++) {
                int m = 0;
                for (Entry<FeatureVector, Integer> p : training_points[i].entrySet()) {
                    if (p.getKey().features[j] == v.features[j]) m++;
                }
                pr += Math.log((double) m / training_points[i].size());
            }
            probabilities.add(pr);
        }
        
        if (Classifier.SHOW) showProbs(probabilities);
        return argmax(probabilities);
    }
    
    private void showProbs(ArrayList<Double> probabilities) {
        System.out.print("[");
        
        for (int i = 0; i < probabilities.size()-1; i++) {
            System.out.print(String.format("%.3f", probabilities.get(i)) + ", ");
        }
        
        System.out.println(probabilities.get(probabilities.size()-1) + "]");
    }

    private int argmax(ArrayList<Double> probabilities) {
        double max = probabilities.get(0);
        int argmax = 0;
        
        for (int i = 1; i < probabilities.size(); i++) {
            if (probabilities.get(i) > max) {
                max = probabilities.get(i);
                argmax = i;
            }
        }
        
        return argmax;
    }

}
