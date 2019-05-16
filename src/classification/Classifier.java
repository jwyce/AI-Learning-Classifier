package classification;

import java.util.ArrayList;
import java.util.Scanner;

public class Classifier {
    
    public static final boolean SHOW = true;

    public static void main(String[] args) {
        Samples samples = new Samples();
        boolean running = true;
        Scanner sc = new Scanner(System.in);
        
        while (running) {
            System.out.print("enter classifier method < nearest, bayes, perceptron > "
                    + "or q to quit: ");
            String input = sc.nextLine();
            switch(input) {
            case "nearest":
                System.out.print("enter data type < digit, face >: ");
                input = sc.nextLine();
                System.out.print("enter feature fineness: ");
                int fineness = Integer.parseInt(sc.nextLine());
                System.out.print("enter number of neighbors to consider: ");
                int k = Integer.parseInt(sc.nextLine());
                System.out.print("enter training percent: ");
                double trainPercent = Double.parseDouble(sc.nextLine());
                
                if (input.equals("digit"))
                    testNearestNeighbors(samples.training_digits, samples.digit_training_labels, samples.validation_digits,
                            samples.digit_validation_labels, trainPercent, fineness, k, samples.test_digits, samples.digit_test_labels);
                else if (input.equals("face"))
                    testNearestNeighbors(samples.training_faces, samples.face_training_labels, samples.validation_faces,
                            samples.face_validation_labels, trainPercent, fineness, k, samples.test_faces, samples.face_test_labels);
                break;
            case "bayes":
                System.out.print("enter data type < digit, face >: ");
                input = sc.nextLine();
                System.out.print("enter feature fineness: ");
                fineness = Integer.parseInt(sc.nextLine());
                System.out.print("enter training percent: ");
                trainPercent = Double.parseDouble(sc.nextLine());
                
                if (input.equals("digit"))
                    testNaiveBayes(samples.training_digits, samples.digit_training_labels, samples.validation_digits,
                            samples.digit_validation_labels, trainPercent, fineness, 10, samples.test_digits, samples.digit_test_labels);
                else if (input.equals("face"))
                    testNaiveBayes(samples.training_faces, samples.face_training_labels, samples.validation_faces,
                            samples.face_validation_labels, trainPercent, fineness, 2, samples.test_faces, samples.face_test_labels);
                break;
            case "perceptron":
                System.out.print("enter data type < digit, face >: ");
                input = sc.nextLine();
                System.out.print("enter feature fineness: ");
                fineness = Integer.parseInt(sc.nextLine());
                System.out.print("enter training percent: ");
                trainPercent = Double.parseDouble(sc.nextLine());
                
                if (input.equals("digit"))
                    testPerceptron(samples.training_digits, samples.digit_training_labels, samples.validation_digits,
                            samples.digit_validation_labels, trainPercent, fineness, 10, samples.test_digits, samples.digit_test_labels);
                else if (input.equals("face"))
                    testPerceptron(samples.training_faces, samples.face_training_labels, samples.validation_faces,
                            samples.face_validation_labels, trainPercent, fineness, 2, samples.test_faces, samples.face_test_labels);
                break;
            case "q":
                running = false;
            }
        }
        sc.close();
    }
    
    private static void testPerceptron(ArrayList<char[][]> training_data, int[] training_labels, ArrayList<char[][]> validation_data,
            int[] validation_labels, double trainPercent, int fineness, int range, ArrayList<char[][]> test_data, int[] test_labels) {
        if (trainPercent > 1) trainPercent = 1;
        if (trainPercent <= 0) trainPercent = 0.1;
        Perceptron p = new Perceptron(fineness, range);
        p.train(training_data, training_labels, validation_data, validation_labels, trainPercent);
        
        int count = 0;
        int correct = 0;
        for (char[][] s : test_data) {
            if (SHOW) Samples.showSample(s);
            int guess = p.classify(s);
            int ans = test_labels[count++];
            if (SHOW) System.out.println(guess + "\tans: " + ans);
            if (guess == ans) correct++;
        }
        
        System.out.println("Perceptron w/ " + fineness + "x" + fineness + " features");
        System.out.println(100*trainPercent + "% of training data used");
        System.out.println(correct + "/" + count + " = " 
                + String.format("%.1f", (double)100*correct/count) + "%");
    }

    private static void testNaiveBayes(ArrayList<char[][]> training_data, int[] training_labels, ArrayList<char[][]> validation_data,
            int[] validation_labels, double trainPercent, int fineness, int range, ArrayList<char[][]> test_data, int[] test_labels) {
        if (trainPercent > 1) trainPercent = 1;
        if (trainPercent <= 0) trainPercent = 0.1;
        NaiveBayes nb = new NaiveBayes(fineness, range);
        nb.train(training_data, training_labels, validation_data, validation_labels, trainPercent);
        
        int count = 0;
        int correct = 0;
        for (char[][] s : test_data) {
            if (SHOW) Samples.showSample(s);
            int guess = nb.classify(s);
            int ans = test_labels[count++];
            if (SHOW) System.out.println(guess + "\tans: " + ans);
            if (guess == ans) correct++;
        }
        
        System.out.println("Naive Bayes w/ " + fineness + "x" + fineness + " features");
        System.out.println(100*trainPercent + "% of training data used");
        System.out.println(correct + "/" + count + " = " 
                + String.format("%.1f", (double)100*correct/count) + "%");
    }

    private static void testNearestNeighbors(ArrayList<char[][]> training_data, int[] training_labels, ArrayList<char[][]> validation_data,
            int[] validation_labels, double trainPercent, int fineness, int k, ArrayList<char[][]> test_data, int[] test_labels) {
        if (trainPercent > 1) trainPercent = 1;
        if (trainPercent <= 0) trainPercent = 0.1;
        NearestNeighbors nn = new NearestNeighbors(fineness, k);
        nn.train(training_data, training_labels, validation_data, validation_labels, trainPercent);
        
        int count = 0;
        int correct = 0;
        for (char[][] s : test_data) {
            if (SHOW) Samples.showSample(s);
            int guess = nn.classify(s);
            int ans = test_labels[count++];
            if (SHOW) System.out.println(guess + "\tans: " + ans);
            if (guess == ans) correct++;
        }
        
        System.out.println(k + "-Nearest Neighbors w/ " + fineness + "x" + fineness + " features");
        System.out.println(100*trainPercent + "% of training data used");
        System.out.println(correct + "/" + count + " = " 
                + String.format("%.1f", (double)100*correct/count) + "%");
    }
    
}
