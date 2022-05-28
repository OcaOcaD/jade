package hands.on8;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import javax.swing.*;
import java.util.*;



public class HandsOn8 extends Agent {

    //    INICIO DE CLASE Sepal, DONDE SE ALMACENARA LOS DATOS DE LA TABLA QUE NOS PROPORCIONO EN CLASSROOM
    public class Sepal implements Comparable<Sepal> {
        //Target
        private String specie     = "";
        
        private double length   = 0.0;
        private double width     = 0.0;
        private Double distance = 0.0;

        public Sepal(double length, double width, String specie) {
            this.width   = width;
            this.length = length;
            this.specie   = specie;
        }

        public Sepal() {
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public void setHeigth(double length) {
            this.length = length;
        }

        public void setSpecie(String specie) {
            this.specie = specie;
        }

        public double getWidth() {
            return width;
        }

        public double getLength() {
            return length;
        }

        public String getSpecie() {
            return specie;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public Double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(Sepal x) {
            return distance.compareTo(x.getDistance());
        }
        
    }
    // FIN DE CLASE Sepal

    // KNN Class

    public class KNearestNeighbors {

        public ArrayList<Sepal> AllSepal = new ArrayList<Sepal>(
            List.of(
                new Sepal(5.3, 3.7, "Setosa"), new Sepal(5.1, 3.8, "Setosa"),
                new Sepal(7.2, 3.0, "Virginica"), new Sepal(5.4, 3.4, "Setosa"),
                new Sepal(5.1, 3.3, "Setosa"), new Sepal(5.4, 3.9, "Setosa"),
                new Sepal(7.4, 2.8, "Virginica"), new Sepal(6.1, 2.8, "Verscicolor"),
                new Sepal(7.3, 2.9, "Virginica"), new Sepal(6.0, 2.7, "Verscicolor"),
                new Sepal(5.8, 2.8, "Virginica"),new Sepal(6.3, 2.3, "Verscicolor"),
                new Sepal(5.1, 2.5, "Verscicolor"),new Sepal(5.3, 2.5,"Verscicolor"),
                new Sepal(5.5, 2.4, "Verscicolor")
        ));
        public ArrayList<Sepal> NormalizedSepals = new ArrayList<Sepal>(
            List.of(
                new Sepal(), new Sepal(), new Sepal(),
                new Sepal(), new Sepal(), new Sepal(),
                new Sepal(), new Sepal(), new Sepal(), 
                new Sepal(), new Sepal(),new Sepal(),
                new Sepal(), new Sepal(), new Sepal()
        
        ));
        public double k     = 2;
        public double length = 0.0;
        public String specie  ="";
        public double width  = 0.0;
        public double minWidth  = 99999.0;
        public double maxWidth  = 0.0;
        public double minLength  = 99999.0;
        public double maxLength  = 0.0;
        public int setosa=0;
        public int virginica=0;
        public int verscicolor=0;

        public void KNearestNeighbors(double length, double width, double k) {
            this.k = k;
            this.length = length;
            this.width = width;
            
            
            double sum = 0.0;
            normalize();
            normalizeGivenValues();
            setDistances();
            Collections.sort(NormalizedSepals);
            System.out.println("Nearest plants :");
            // Printing the full plant list
            for (int i = 0; i < 15; i++) {
                System.out.println("Ranking: "+i+". Plant type: "+NormalizedSepals.get(i).getSpecie());
            }
            findResult();
            // Printing result
            if(this.setosa > this.virginica && this.setosa > this.verscicolor )
                System.out.println("Plant type: Setosa");
            if( this.virginica > this.setosa && this.virginica >this. verscicolor )
                System.out.println("Plant type: Virginica");
            if( this.verscicolor > this.setosa && this.verscicolor > this.virginica )
                System.out.println("Plant type: Verscicolor");
        }

        public void findResult(){
            for (int i = 0; i < k; i++) {
                if(NormalizedSepals.get(i).getSpecie().equalsIgnoreCase("Setosa"))
                    this.setosa++;
                if(NormalizedSepals.get(i).getSpecie().equalsIgnoreCase("Virginica"))
                    this.virginica++;
                if(NormalizedSepals.get(i).getSpecie().equalsIgnoreCase("Verscicolor"))
                    this.verscicolor++;
            }
        }

        public void setDistances() {
            for (int i = 0; i < 15; i++) {
                double d = euclideanDistance(NormalizedSepals.get(i).getLength(), NormalizedSepals.get(i).getWidth());
                NormalizedSepals.get(i).setDistance(d);
            }
        }

        public double euclideanDistance(double length, double width) {
            return Math.sqrt((Math.pow(this.length - length, 2) + Math.pow(this.width - width, 2)));
        }

        public String getSpecie() {
            return specie;
        }

        public void calculateMinMax() {
            for (int i = 0; i < 15; i++) {
                double width = AllSepal.get(i).getWidth();
                double length = AllSepal.get(i).getLength();
                if (width < minWidth)
                    minWidth = width;
                if (width > maxWidth)
                    maxWidth = width;
                if (length < minLength)
                    minLength = length;
                if (width > maxLength)
                    maxLength = length;
            }
        }

        public void normalize() {
            calculateMinMax();
            for (int i = 0; i < 15; i++) {
                Sepal planta = new Sepal();
                planta.setWidth((AllSepal.get(i).getWidth() - minWidth) / (maxWidth - minWidth));
                planta.setHeigth((AllSepal.get(i).getLength() - minLength) / (maxLength - minLength));
                planta.setSpecie(AllSepal.get(i).getSpecie());
                NormalizedSepals.set(i, planta);
            }
        }

        public void normalizeGivenValues() {
            for (int i = 0; i < 15; i++) {
                length = ((length - minLength) / (maxLength - minLength));
                width = ((width - minWidth) / (maxWidth - minWidth));
            }
        }
    }

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");
        addBehaviour(new MyOneShotBehaviour());
    }

    private class MyOneShotBehaviour extends OneShotBehaviour {

        public void action() {
            KNearestNeighbors KNN = new KNearestNeighbors();
            KNN.KNearestNeighbors(
                Double.valueOf(JOptionPane.showInputDialog("Length : ")),
                Double.valueOf(JOptionPane.showInputDialog("Width   : ")),
                Double.valueOf(JOptionPane.showInputDialog("K      : ")));
            
        }

        public int onEnd() {
            myAgent.doDelete();
            return super.onEnd();
        }

    }    // END of inner class ...Behaviour
}