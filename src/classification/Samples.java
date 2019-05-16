package classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Samples {

    ArrayList<char[][]> test_digits;
    ArrayList<char[][]> training_digits;
    ArrayList<char[][]> validation_digits;
    int[] digit_test_labels;
    int[] digit_training_labels;
    int[] digit_validation_labels;
    
    ArrayList<char[][]> test_faces;
    ArrayList<char[][]> training_faces;
    ArrayList<char[][]> validation_faces;
    int[] face_test_labels;
    int[] face_training_labels;
    int[] face_validation_labels;
    
    public Samples() {
        test_digits = loadImageData("digitdata/testimages", 28, 28);
        training_digits = loadImageData("digitdata/trainingimages", 28, 28);
        validation_digits = loadImageData("digitdata/validationimages", 28, 28);
        digit_test_labels = loadLabelData("digitdata/testlabels", 1000);
        digit_training_labels = loadLabelData("digitdata/traininglabels", 5000);
        digit_validation_labels = loadLabelData("digitdata/validationlabels", 1000);
        
        test_faces = loadImageData("facedata/facedatatest", 60, 70);
        training_faces = loadImageData("facedata/facedatatrain", 60, 70);
        validation_faces = loadImageData("facedata/facedatavalidation", 60, 70);
        face_test_labels = loadLabelData("facedata/facedatatestlabels", 150);
        face_training_labels = loadLabelData("facedata/facedatatrainlabels", 451);
        face_validation_labels = loadLabelData("facedata/facedatavalidationlabels", 301);
    }
    
    public static void showSample(char[][] sample) {
        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample[0].length; j++) {
                System.out.print(sample[i][j]);
            }
            System.out.println();
        }
    }
    
    public static void showAllSamples(ArrayList<char[][]> samples) {
        for (int i = 0; i < samples.size(); i++) {
            for (int j = 0; j < samples.get(0).length; j++) {
                for (int k = 0; k < samples.get(0)[0].length; k++) {
                    System.out.print(samples.get(i)[j][k]);
                }
                System.out.println();
            }
        }
    }
    
    private int[] loadLabelData(String filename, int size) {
        int[] labels = new int[size];
        String line = "";
            
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
            int i = 0;
            while ((line = reader.readLine()) != null) {
                labels[i++] = Integer.parseInt(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return labels;
    }
    
    private ArrayList<char[][]> loadImageData(String filename, int w, int h) {
        ArrayList<char[][]> images = new ArrayList<char[][]>();
        char[][] image = new char[h][w];
        String line = "";
            
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (row == h) {
                    row = 0;
                    images.add(image);
                    image = new char[h][w];
                }
                
                for (int col = 0; col < w; col++) {
                    image[row][col] = line.charAt(col);
                }
                row++;
            }
            images.add(image);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return images;
    }
}
