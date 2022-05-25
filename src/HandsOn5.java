package handson5;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import javax.swing.*;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.List;

class GD {

    public ArrayList<Integer> x = new ArrayList<Integer>(List.of(23,26,30, 34,43 ,48 ,52 ,57, 58));
    public ArrayList<Integer> y = new ArrayList<Integer>(List.of(651, 762,856,1063,1190, 1298, 1421, 1440, 1518));
    public double beta0 = 0;
    public double beta1 = 0;
    public double Result = 0;
    public double xValue = 0;
    public double n = Double.valueOf(x.size());

    public void LinearRegression() {
        Result = beta0 + (beta1 * xValue);
    }

    public void gradientDescent() {
        beta0 = 0.0;
        beta1 = 0.0;
        // learning step
        double alpha = 0.0003;
        // Untill small enough
        while (calculateValues() > 0.0001) {    // Stop if this is a good enough guess. beta0 and beta1 values will remain the best
            // derivadas
            double tempB0 = 0.0;
            double tempB1 = 0.0;
            for (int i = 0; i < n; i++) {
                tempB0 += (y.get(i) - (beta0 + beta1 * x.get(i)));
                tempB1 += ((x.get(i) * (y.get(i) - (beta0 + beta1 * x.get(i)))));
            }

            tempB0 *= (-2 / n);
            tempB1 *= (-2 / n);

            beta0 -= (alpha * tempB0);
            beta1 -= (alpha * tempB1);
        }
        // Best guess found
    }

    public void setXvalue(double x) {
        this.xValue = x;
    }

    // se calculan los datos ya existentes
    public double calculateValues() {
        double temp = 0.0;
        for (int i = 0; i < n; i++) {
            temp += (y.get(i) - (beta0 + (beta1 * x.get(i)))); // Primero la sumatoria
        }
        // Al cuadrado entre n
        return (temp * temp) * (1.0 / n);   // Epsilon o la funcion de loss
    }

    public double getResult() {
        return Result;
    }

    public double getxValue() {
        return xValue;
    }

    public double getBeta0() {
        return beta0;
    }

    public double getBeta1() {
        return beta1;
    }

}

public class HandsOn5 extends Agent {

    
    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        addBehaviour(new MyOneShotBehaviour());
    }

    private class MyOneShotBehaviour extends OneShotBehaviour {

        public void action() {
            GD  linear = new GD ();
            // Generate initial point (Could be random)
            double x= 0 + Math.random() * (5 - 0);
            linear.setXvalue( x );
            linear.gradientDescent();
            // linear.setXvalue(Integer.valueOf(JOptionPane.showInputDialog("Insert value for X: ")));
            linear.LinearRegression();
            System.out.println("''Best''' betas found: ");
            System.out.println("\t Beta 0 ="+String.valueOf(linear.getBeta0()));
            System.out.println("\t Beta 1 ="+String.valueOf(linear.getBeta1()));
            System.out.println("y = (" + String.valueOf(linear.getBeta0()) +") + ( "+String.valueOf(linear.getBeta1()) +
                    ")("+String.valueOf(linear.getxValue()) + ")");
            System.out.println("y = " + String.valueOf(linear.getResult()));
        }

        public int onEnd() {
            myAgent.doDelete();
            return super.onEnd();
        }

    }    // END of inner class ...Behaviour
}