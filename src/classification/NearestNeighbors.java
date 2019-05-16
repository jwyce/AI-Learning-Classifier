package classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

public class NearestNeighbors extends ClassificationMethod {

    int fineness, k;
    HashMap<FeatureVector, Integer> training_points;
    
    public NearestNeighbors(int fineness, int k) {
        this.fineness = fineness;
        this.k = k;
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
        
        while ((double) training_points.size() / (training_data.size() + validation_data.size()) < trainPercent) {
            int i = rand.nextInt(data.size());
            training_points.put(new FeatureVector(data.get(i), fineness), labels.get(i));
            data.remove(i);
            labels.remove(i);
        }
        
    }

    @Override
    public int classify(char[][] data) {
        // k nearest neighbors using angle between vectors
        FeatureVector v = new FeatureVector(data, fineness);
        Map<Double, Integer> tree = new TreeMap<Double, Integer>();
        
        for (Entry<FeatureVector, Integer> p : training_points.entrySet()) {
            double angle = Math.acos(FeatureVector.dotproduct(v, p.getKey()) / 
                    (FeatureVector.norm(v)*FeatureVector.norm(p.getKey())));
            tree.put(angle, p.getValue());
        }
        
        int k = this.k;
        int[] k_nearest_neighbors = new int[k];
        Iterator<Integer> it = tree.values().iterator();
        while (k > 0) {
            k_nearest_neighbors[--k] = it.next();
        }
        
        return mostFrequent(k_nearest_neighbors);
    }
    
    private int mostFrequent(int[] arr) {
        if (Classifier.SHOW) System.out.println(Arrays.toString(arr));
        HashMap<Integer, Integer> freq = new HashMap<Integer, Integer>();
        for (int i : arr) {
            if (freq.containsKey(i))
                freq.put(i, freq.get(i)+1);
            else 
                freq.put(i, 1);
        }
        int max = 0, res = 0;
        for (Entry<Integer, Integer> entry : freq.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                res = entry.getKey();
            }
        }
        
        if (max == 1) return arr[arr.length-1];
        return res;
    }

}
