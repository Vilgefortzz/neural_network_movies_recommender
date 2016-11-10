package main;

import main.io.FileReader;
import main.neurons.McCullochPittsNeuron;
import main.neurons.Perceptron;

import java.util.ArrayList;

public class Controller{

    // All files - too much data
    private ArrayList<String> movies;
    private ArrayList<String> ratings;

    // Files which have cut ( to learn and validate )
    private ArrayList<String> moviesLearn;
    private ArrayList<String> moviesValidate;
    private ArrayList<String> users;

    private double[][] inputDataLearn;
    private double[][] inputDataValidate;

    // McCullochPitts

    public McCullochPittsNeuron mcCullochPittsNeuron;

    // Perceptron

    public Perceptron perceptron;

    public Controller() {

        this.movies = new ArrayList<>();
        this.ratings = new ArrayList<>();

        this.moviesLearn = new ArrayList<>();
        this.moviesValidate = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public double[][] getInputDataValidate() {
        return inputDataValidate;
    }

    public void readInputData() {

        FileReader file = new FileReader("/data_set/movies.dat");
        this.movies = file.getLines();

        file = new FileReader("/data_set/learning_set/movies.dat");
        this.moviesLearn = file.getLines();

        file = new FileReader("/data_set/validation_set/movies.dat");
        this.moviesValidate = file.getLines();

        file = new FileReader("/data_set/learning_set/users.dat");
        this.users = file.getLines();

        // Finally moviesLearn + users and their rates = learning set
    }

    public void generateLearningData(int moviesNumber, int usersNumber, int parameters) {

        String[] moviesRatings = new String[moviesNumber];
        String[] usersLikes = new String[usersNumber];
        inputDataLearn = new double[moviesNumber][parameters+1]; // o jeden więcej gdyż jeszcze wartość oczekiwana

        for (int i=0;i<moviesNumber;i++){

            String[] info1 = moviesLearn.get(i).split("::");
            moviesRatings[i] = info1[3];
        }

        for (int i=0;i<usersNumber;i++){

            String[] info2 = users.get(i).split("::");
            usersLikes[i] = info2[2];
        }

        int k=0;
        int index = 0;

        // Wartość oczekiwana jaka ma być
        for (int i = 0; i< inputDataLearn.length; i++){

            inputDataLearn[i][index++] = Double.parseDouble(usersLikes[k]);
            inputDataLearn[i][index++] = Double.parseDouble(moviesRatings[i]);

            if (Double.parseDouble(moviesRatings[i]) >= Double.parseDouble(usersLikes[k]))
                inputDataLearn[i][index] = 1;
            else
                inputDataLearn[i][index] = 0;

            index = 0;
        }
    }

    public void generateValidationData(int moviesNumber, int usersNumber, int parameters) {

        String[] moviesRatings = new String[moviesNumber];
        String[] usersLikes = new String[usersNumber];
        inputDataValidate = new double[moviesNumber][parameters]; // normalnie nie ma oczekiwanej wartości

        for (int i = 0; i < moviesNumber; i++) {

            String[] info1 = moviesValidate.get(i).split("::");
            moviesRatings[i] = info1[3];
        }

        for (int i = 0; i < usersNumber; i++) {

            String[] info2 = users.get(i).split("::");
            usersLikes[i] = info2[2];
        }

        int k = 0;
        int index = 0;

        // Wartości do testowania
        for (int i = 0; i < inputDataValidate.length; i++) {

            inputDataValidate[i][index++] = Double.parseDouble(usersLikes[k]);
            inputDataValidate[i][index] = Double.parseDouble(moviesRatings[i]);

            index = 0;
        }
    }

    public void createPerceptron() {
        perceptron = new Perceptron(inputDataLearn);
    }

    public void createPerceptron(double[][] inputData){

        perceptron = new Perceptron(inputData);
    }

    public void createMcCullochPittsNeuron() {
        mcCullochPittsNeuron = new McCullochPittsNeuron(inputDataLearn);
    }

    public void learningMcCullohPittsNeuron(int parameters, int attempts){

        if (parameters == 1)
            mcCullochPittsNeuron.applyLearningRuleOneParametr();
        else if (parameters == 2)
            mcCullochPittsNeuron.applyLearningRuleTwoParameters(attempts);

    }

    public void learningPerceptron(int parameters, int attempts){

        if (parameters == 1)
            perceptron.applyLearningRuleOneParametr();
        else if (parameters == 2)
            perceptron.applyLearningRuleTwoParameters(attempts);
    }
}