/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handson3;

/**
 *
 * @author vdgp_
 */
public class multipleLinearRegression {
    float matriz[][];                        //Datos originales dados por el profesor
    dataSet data;                            //Objeto que hace operaciones a la matriz
    double[][] matrizA;                      //multiplicacion de X transpuesta x X
    double[][] matrizAInversa;               //La inversa de la matriz A
    double[]vectorG;                         //matriz Transpuesta por Y
    double []vectorFinal;                    //contiene los valores de B0, B1, B2
    double B0,B1,B2;
    public  multipleLinearRegression() {
        matriz = new float[3][17];
        llenandoMatrizOriginal();   //llena de datos la matriz original
       
        data= new dataSet(matriz);
        matrizA= new double[3][3];
        matrizAInversa= new double[3][3];
        //llenando la matriz de la multiplicacion de X transpuesta * X
        llenandoMatrizXTxY();
        
        //imprimirMatriz(matrizA);
        vectorG= new double[3];
        
        //llenando la matriz de X transpuesta * Y
        vectorG[0]=data.sumaY;
        vectorG[1]=data.sumaYxX1;
        vectorG[2]=data.sumaYxX2;
        vectorFinal=new double[3];
        
        //se crea la inversa de la matriz, sin no tiene inversa retorna negativo
        if(inversaMatriz()){
            //imprimirMatriz(matrizAInversa);
            
            //multiplica la matriz inversa * la matriz G para obtener B0, B1, B2
            multiplicarMatrices(matrizAInversa,vectorG);
            obteniendoBs(1,1,1);
            predecir(82.5,31.2);
            predecir(82.4,32.3);
            predecir(81.2,33.2);
        }
        
        
       }
    public void llenandoMatrizOriginal(){
        //llenando la matriz original 
        //********************************************************************************
        //x1
        matriz[0][0]=41.9f;
        matriz[0][1]=43.4f;
        matriz[0][2]=43.9f;
        matriz[0][3]=44.5f;
        matriz[0][4]=47.3f;
        matriz[0][5]=47.5f;
        matriz[0][6]=47.9f;
        matriz[0][7]=50.2f;
        matriz[0][8]=52.8f;
        matriz[0][9]=53.2f;
        matriz[0][10]=56.7f;
        matriz[0][11]=57.0f;
        matriz[0][12]=63.5f;
        matriz[0][13]=65.3f;
        matriz[0][14]=71.1f;
        matriz[0][15]=77.0f;
        matriz[0][16]=77.8f;
        
        //x2
        matriz[1][0]=29.1f;
        matriz[1][1]=29.3f;
        matriz[1][2]=29.5f;
        matriz[1][3]=29.7f;
        matriz[1][4]=29.9f;
        matriz[1][5]=30.3f;
        matriz[1][6]=30.5f;
        matriz[1][7]=30.7f;
        matriz[1][8]=30.8f;
        matriz[1][9]=30.9f;
        matriz[1][10]=31.5f;
        matriz[1][11]=31.7f;
        matriz[1][12]=31.9f;
        matriz[1][13]=32.0f;
        matriz[1][14]=32.1f;
        matriz[1][15]=32.5f;
        matriz[1][16]=32.9f;
        
        //y
        matriz[2][0]=251.3f;
        matriz[2][1]=251.3f;
        matriz[2][2]=248.3f;
        matriz[2][3]=267.5f;
        matriz[2][4]=273.0f;
        matriz[2][5]=276.5f;
        matriz[2][6]=270.3f;
        matriz[2][7]=274.9f;
        matriz[2][8]=285.0f;
        matriz[2][9]=290.0f;
        matriz[2][10]=297.0f;
        matriz[2][11]=302.5f;
        matriz[2][12]=304.5f;
        matriz[2][13]=309.3f;
        matriz[2][14]=321.7f;
        matriz[2][15]=330.7f;
        matriz[2][16]=349.0f;
        //**********************************************************************
    }
    
    public  void llenandoMatrizXTxY(){
        matrizA[0][0]=data.datos[0].length;
        matrizA[0][1]=data.sumaX1;
        matrizA[0][2]=data.sumaX2;
        matrizA[1][0]=data.sumaX1;
        matrizA[2][0]=data.sumaX2;
        matrizA[1][1]=data.sumaX1Squear;
        matrizA[2][1]=data.sumaX1xX2;
        matrizA[1][2]=data.sumaX1xX2;
        matrizA[2][2]=data.sumaX2Squear;
        //  0 1 2
        //0[][][]
        //1[][][]
        //2[][][]
    }
    
    public boolean inversaMatriz(){
        //Declaramos variables
        double determinante,x00,x01,x02,x10,x11,x12,x20,x21,x22;
        boolean value=true;
        
        //Hallamos determinante de nuestra matriz principal
        determinante=(matrizA[0][0]*((matrizA[1][1]*matrizA[2][2])-(matrizA[1][2]*matrizA[2][1])))-(matrizA[0][1]*((matrizA[1][0]*matrizA[2][2])-(matrizA[2][0]*matrizA[1][2])))+(matrizA[0][2]*((matrizA[1][0]*matrizA[2][1])-(matrizA[2][0]*matrizA[1][1])));
        
        // Calculamos los valores de la matriz inversa ya dividido entre la determinante
        if(determinante!=0){
            x00=((matrizA[1][1]* matrizA[2][2] - matrizA[2][1]* matrizA[1][2]))/determinante;
            x01=(-(matrizA[1][0]* matrizA[2][2] - matrizA[2][0]* matrizA[1][2]))/determinante;
            x02=((matrizA[1][0]* matrizA[2][1] - matrizA[2][0]* matrizA[1][1]))/determinante;
            x10=(-(matrizA[0][1]* matrizA[2][2] - matrizA[2][1]* matrizA[0][2]))/determinante;
            x11=((matrizA[0][0]* matrizA[2][2] - matrizA[2][0]* matrizA[0][2]))/determinante;
            x12=(-(matrizA[0][0]* matrizA[2][1] - matrizA[2][0]* matrizA[0][1]))/determinante;
            x20=((matrizA[0][1]* matrizA[1][2] - matrizA[1][1]* matrizA[0][2]))/determinante;
            x21=(-(matrizA[0][0]* matrizA[1][2] - matrizA[1][0]* matrizA[0][2]))/determinante;
            x22=((matrizA[0][0]* matrizA[1][1] - matrizA[1][0]* matrizA[0][1] ))/determinante;
            // Hacemos el intercambio de que las filas pasen como columnas
            matrizAInversa[0][0]=x00;
            matrizAInversa[0][1]=x10;
            matrizAInversa[0][2]=x20;
            matrizAInversa[1][0]=x01;
            matrizAInversa[1][1]=x11;
            matrizAInversa[1][2]=x21;
            matrizAInversa[2][0]=x02;
            matrizAInversa[2][1]=x12;
            matrizAInversa[2][2]=x22;
             
        }
        else{
            System.out.print("Erros la matriz no tiene inversa");
            value=false;
        }
        return value;
    }
    private void multiplicarMatrices(double [][]matrizA, double []matrizB){
        double valor=0;
        for (int i = 0; i < matrizA.length; i++) {
            for (int j = 0; j < matrizA[i].length; j++) {
                valor += matrizA[i][j] * matrizB[j];
            }
            vectorFinal[i]= valor;
            valor=0;
        }
        
//        for (int i = 0; i < 10; i++) {
//           System.out.println(vectorFinal[i]);
//        }
    }
    
    public void imprimirMatriz(double m[][]){
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j]+" ");
            }
            System.out.println();
            
        }
        
    }

    public void obteniendoBs(double v1, double v2, double v3){
        B0=vectorFinal[0];
        B1=vectorFinal[1];
        B2=vectorFinal[2];
        
        System.out.println("Formula: "+B0+" + "+B1+" * x1 + "+B2+" * x2" );
        
        
    }
    public void predecir(double x1, double x2){
        System.out.println("x1 = "+x1);
        System.out.println("x2 = "+x2);
        double resultado = B0 + (B1*x1)+(B2 * x2);
        System.out.println("Resultado:"+ resultado );
         System.out.println();
        
    }
}



