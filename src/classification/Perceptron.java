package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Perceptron extends ClassificationMethod {

    int fineness, range;
    // w[i][j] where i is the ith perceptron and j is the dimension of the vector
    double[][] w;
    HashMap<FeatureVector, Integer> training_points;
    
    public Perceptron(int fineness, int range) {
        this.fineness = fineness;
        this.range = range;
        training_points = new HashMap<FeatureVector, Integer>();
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
        
        // create random training set of a certain percent of all training data
        while ((double) training_points.size() / (training_data.size() + validation_data.size()) < trainPercent) {
            int i = rand.nextInt(data.size());
            training_points.put(new FeatureVector(data.get(i), fineness), labels.get(i));
            data.remove(i);
            labels.remove(i);
        }
       
        // initialize weights
        w = new double[range][training_points.keySet().iterator().next().size()+1];
        
        for (int r = 0; r < range; r++) {
            // stop after 50 iterations (time limit in case no linear decision boundary exists)
            for (int i = 0; i < 50; i++) {
                boolean updated = false;
                
                for (Entry<FeatureVector, Integer> p : training_points.entrySet()) {
                    double f = w[r][w.length-1];
                    
                    for (int j = 0; j < w[r].length-1; j++) {
                        f += (w[r][j] * p.getKey().features[j]);
                    }
                    
                    // update weights
                    if (!((f >= 0 && p.getValue() == r) || (f < 0 && p.getValue() != r))) {
                        updated = true;
                        if (f < 0 && p.getValue() == r) {
                            for (int k = 0; k < w[r].length-1; k++) {
                                w[r][k] += p.getKey().features[k];
                            }
                            w[r][w.length-1]++;
                        }
                        if (f >= 0 && p.getValue() != r) {
                            for (int k = 0; k < w[r].length-1; k++) {
                                w[r][k] -= p.getKey().features[k];
                            }
                            w[r][w.length-1]--;
                        }
                    }
                }
                
                if (!updated) break;
            }
        }
    }

    @Override
    public int classify(char[][] data) {
        FeatureVector v = new FeatureVector(data, fineness);
        double[] f = new double[range];
        
        for (int r = 0; r < range; r++) {
            f[r] = w[r][w.length-1];
            for (int i = 0; i < v.size(); i++) {
                f[r] += (w[r][i] * v.features[i]);
            }
        }
        
        if (Classifier.SHOW) System.out.println(Arrays.toString(f));
        
        return argmax(f);
    }
    
    private int argmax(double[] arr) {
        double max = arr[0];
        int argmax = 0;
        
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                argmax = i;
            }
        }
        
        return argmax;
    }

}
