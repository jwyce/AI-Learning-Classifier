package classification;

import java.util.ArrayList;

public abstract class ClassificationMethod {

    public abstract void train(ArrayList<char[][]> training_data, int[] training_labels, ArrayList<char[][]> validation_data,
            int[] validation_labels, double trainPercent);
    
    public abstract int classify(char[][] data);
}
