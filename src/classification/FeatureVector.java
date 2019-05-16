package classification;

import java.util.Arrays;

public class FeatureVector {
    
    int[] features;
    
    public FeatureVector(char[][] image, int fineness) {
        int x = image.length/fineness, y = image[0].length/fineness;
        int n = x*y;
        features = new int[n];
        
        for (int h = 0; h < x; h++) {
            for (int i = 0; i < y; i++) {
                boolean marked = false;
                for (int j = h*fineness; j < fineness*(h+1); j++) {
                    for (int k = i*fineness; k < fineness*(i+1); k++) {
                        if (image[j][k] != 32) marked = true;
                    }
                }
                if (marked) features[x*i+h] = 1;
            }
        }
    }
    
    public static double dotproduct(FeatureVector a, FeatureVector b) {
        if (a.features.length != b.features.length) throw new ArithmeticException();
        int dotproduct = 0;
        
        for (int i = 0; i < a.features.length; i++) {
            dotproduct += (a.features[i] * b.features[i]);
        }
        
        return dotproduct;
    }
    
    public static double norm(FeatureVector v) {
        double sum = 0;
        for (int i = 0; i < v.features.length; i++) {
            sum += (Math.pow(v.features[i], 2));
        }
        return Math.sqrt(sum);
    }
    
    public int size() {
        return features.length;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(features);
    }
    @Override
    public boolean equals(Object other) {
        if (other == null || !other.getClass().getSimpleName().equals("Position")) return false;
        return this.toString().equals(((FeatureVector) other).toString());
    }
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
