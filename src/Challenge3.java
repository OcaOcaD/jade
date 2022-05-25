package examples.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

class MatrixHelper {
	MatrixHelper() {
	}

	public static double[][] matrixMultiplication(
			double[][] matrix1, int rows1, int cols1,
			double[][] matrix2, int rows2, int cols2)
	/* throws Exception */
	{

		// Required condition for matrix multiplication
		if (cols1 != rows2) {
			// throw new Exception("Invalid matrix given.");
			System.out.println("Invalid size");
		}

		// create a result matrix
		double resultMatrix[][] = new double[rows1][cols2];

		// Core logic for 2 matrices multiplication
		for (int i = 0; i < resultMatrix.length; i++) {
			for (int j = 0; j < resultMatrix[i].length; j++) {
				for (int k = 0; k < cols1; k++) {
					resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
				}
			}
		}
		return resultMatrix;
	}

	public static double[][] inversematrix(
			double[][] matrix, int order)
	/* throws Exception */
	{
		int iinvert, jinvert, kinvert, ninvert;
		ninvert = order;
		double Matrix2Invert[][] = new double[ninvert][ninvert];
		Matrix2Invert = matrix;
		double IdentMatrix[][] = new double[ninvert][ninvert];
		double ResultMatrix[][] = new double[ninvert][ninvert];
		double aux, pivote;
		for (iinvert = 0; iinvert < ninvert; iinvert++) {
			for (jinvert = 0; jinvert < ninvert; jinvert++) {
				if (iinvert == jinvert) {
					IdentMatrix[iinvert][jinvert] = 1.0;
				} else {
					IdentMatrix[iinvert][jinvert] = 0.0;
				}
			}
		}
		/* Printing values of matrix identidad inicializada */
		/*
		 * for (iinvert=0;iinvert<ninvert;iinvert++){
		 * for (jinvert=0;jinvert<ninvert;jinvert++){
		 * System.out.println("valores: "+ IdentMatrix[iinvert][jinvert]);
		 * }
		 * }
		 */
		/* Printing values of matrix identidad inicializada */

		// reduccion por renglones
		for (iinvert = 0; iinvert < ninvert; iinvert++) {
			pivote = Matrix2Invert[iinvert][iinvert];
			// convertir el privote a 1 y aplicar la operación sobre toda la fila.
			for (kinvert = 0; kinvert < ninvert; kinvert++) {
				Matrix2Invert[iinvert][kinvert] = Matrix2Invert[iinvert][kinvert] / pivote;
				IdentMatrix[iinvert][kinvert] = IdentMatrix[iinvert][kinvert] / pivote;
			}
			for (jinvert = 0; jinvert < ninvert; jinvert++) {
				if (iinvert != jinvert) {// no esta en la diagonal
					aux = Matrix2Invert[jinvert][iinvert];
					for (kinvert = 0; kinvert < ninvert; kinvert++) {
						Matrix2Invert[jinvert][kinvert] = Matrix2Invert[jinvert][kinvert]
								- aux * Matrix2Invert[iinvert][kinvert]; // ver si no debe ser aux *A[j][k] en lugar de
																			// A[i][k]
						IdentMatrix[jinvert][kinvert] = IdentMatrix[jinvert][kinvert]
								- aux * IdentMatrix[iinvert][kinvert]; // ver si no debe ser aux *I[j][k] en lugar de
																		// I[i][k]
					}
				}
			}
		}

		/* Printing values of matrix identidad ahora como matrix inversa */
		/*
		 * for (iinvert=0;iinvert<ninvert;iinvert++){
		 * for (jinvert=0;jinvert<ninvert;jinvert++){
		 * System.out.println("valores: "+ IdentMatrix[iinvert][jinvert]);
		 * }
		 * }
		 */
		/* Printing values of matrix2invert ahora como matrix inversa */
		/*
		 * for (iinvert=0;iinvert<ninvert;iinvert++){
		 * for (jinvert=0;jinvert<ninvert;jinvert++){
		 * System.out.println("valores: "+ Matrix2Invert[iinvert][jinvert]);
		 * }
		 * }
		 */

		return IdentMatrix;
	}
}

public class Challenge3 extends Agent {

	protected void setup() {
		System.out.println("Agent " + getLocalName() + " started.");
		addBehaviour(new MyOneShotBehaviour());
	}

	private class MyOneShotBehaviour extends OneShotBehaviour {

		public void action() {
			System.out.println("Agent's action method executed");
			PrivateImpl impl = new PrivateImpl();
			impl.regression();

		}

		public int onEnd() {
			myAgent.doDelete();
			return super.onEnd();
		}
	} // END of inner class ...Behaviour
}

class PrivateImpl {
	int implementationData;

	// System.out.println("Hello World! estoy dentro de otra clase My name is");
	public void regression() {
		System.out.println("Iniciando Regresion MLR ...");
		double sumax = 0, sumay = 0, sumaxy = 0, sumaxCuadrada = 0, sumayCuadrada = 0;
		double B0 = 0, B1 = 0, B2 = 0, B3 = 0, B4 = 0, B5 = 0;
		int matrixJsize = 0, matrixIsize = 0;
		double matrixX[][] = new double[100][100];
		double matrixY[][] = new double[100][100];
		double matrixXTranspuesta[][] = new double[100][100];
		// double matrixInvDeTranspByX[][]= new double[100][100];
		// Declaramos y poblamos los arreglos
		double arrayX[];
		arrayX = new double[] { 41.9, 43.4, 43.9, 44.5, 47.3, 47.5, 47.9, 50.2, 52.8, 53.2, 56.7, 57, 63.5, 65.3, 71.1,
				77, 77.8 };
		double arrayX2[];
		arrayX2 = new double[] { 29.1, 29.3, 29.5, 29.7, 29.9, 30.3, 30.5, 30.7, 30.8, 30.9, 31.5, 31.7, 31.9, 32, 32.1,
				32.5, 32.9 };

		double arreglox3[] = null;
		// arreglox3 = new double []{ 23,26,30,34,43,48,52,57,58 };
		double arreglox4[] = null;
		// arreglox4 = new double []{ 23,26,30,34,43,48,52,57,58 };
		double arrayY[];
		arrayY = new double[] { 251.3, 251.3, 248.3, 267.5, 273, 276.5, 270.3, 274.9, 285, 290, 297, 302.5, 304.5,
				309.3, 321.7, 330.7, 349 };
		// int arregloxy[];
		// arregloxy = new int [10];

		// Checar cuantos arreglos X y Y no son vacios o nulos
		if (arrayX != null) {
			// System.out.println("Inside arrayX and not null");
			matrixJsize++;
			for (int i = 0; i < arrayX.length; i++) {
				matrixX[i][0] = 1;
				matrixX[i][1] = arrayX[i];
			}
		}
		if (arrayX2 != null) {
			// System.out.println("Inside arrayX2 and not null");
			matrixJsize++;
			for (int i = 0; i < arrayX2.length; i++) {
				matrixX[i][2] = arrayX2[i];
			}
		}
		if (arreglox3 != null) {
			// System.out.println("estoy dentro de arreglox3 not null");
			matrixJsize++;
			for (int i = 0; i < arreglox3.length; i++) {
				matrixX[i][3] = arreglox3[i];
			}
		}
		if (arreglox4 != null) {
			// System.out.println("estoy dentro de arreglox4 not null");
			matrixJsize++;
			for (int i = 0; i < arreglox4.length; i++) {
				matrixX[i][4] = arreglox4[i];
			}
		}
		if (arrayY != null) {
			// System.out.println("estoy dentro de arrayY not null");
			for (int i = 0; i < arrayX.length; i++) {
				matrixY[i][0] = arrayY[i];
				matrixY[i][1] = 1;
			}
		}

		System.out.println("MatrixJsize= " + matrixJsize);
		// Checar todos los arreglos X no nulos son del mismo tamano

		System.out.println("This is the final MATRIX X");
		for (int i = 0; i < arrayX.length; i++) {
			for (int j = 0; j <= matrixJsize; j++) {
				System.out.print("\tmatrix X: [" + i + "][" + j + "] = " + matrixX[i][j]);
			}
			System.out.print("\n");
		}

		// System.out.println("Filling Transposed Matrix X");
		for (int i = 0; i < arrayX.length; i++) {
			for (int j = 0; j <= matrixJsize; j++) {
				// System.out.println("matrix x pos i= "+i+" matrix x pos j= "+j+" matrix x pos
				// i j valor= "+matrixX[i][j]);
				matrixXTranspuesta[j][i] = matrixX[i][j];

			}
		}

		System.out.println("Tranposed X");
		for (int i = 0; i <= matrixJsize; i++) {
			for (int j = 0; j < arrayX.length; j++) {
				// System.out.println("matrix x pos i= "+i+" matrix x pos j= "+j+" matrix x pos
				// i j valor= "+matrixX[i][j]);
				// matrixXTranspuesta[j][i]=matrixX[i][j];
				System.out.print("   " + matrixXTranspuesta[i][j] + "  ");
				// System.out.print(" t_X: ["+i+"]["+j+"] = "+matrixXTranspuesta[i][j]+" ");
			}
			System.out.print("\n");
		}
		// Envia matrices y tamanos a multiplicar Matrix XTranspuesta * Matrix X
		double resultMatrix[][] = MatrixHelper.matrixMultiplication(matrixXTranspuesta, matrixJsize + 1, arrayX.length,
				matrixX, arrayX.length, matrixJsize + 1);

		// Printing values of Xtranspuesta por matrix X
		System.out.println();
		System.out.println("Matrix X * Transposed matrix X:");
		for (int i = 0; i < resultMatrix.length; i++) {
			for (int j = 0; j < resultMatrix[i].length; j++) {
				System.out.print("\t" + resultMatrix[i][j]);
			}
			System.out.print("\n");
		}

		double resultMatrix1[][] = MatrixHelper.inversematrix(resultMatrix, resultMatrix.length);
		System.out.println();
		System.out.println("Inverse Transposed matrix X * Matrix X:");
		for (int i = 0; i < resultMatrix1.length; i++) {
			for (int j = 0; j < resultMatrix1[i].length; j++) {
				System.out.print("\t" + resultMatrix1[i][j]);
			}
			System.out.print("\n");
		}

		// Envia matrices y tamanos a multiplicar Matrix XTranspuesta * Matrix Y
		double resultMatXtranspProductMatY[][] = MatrixHelper.matrixMultiplication(matrixXTranspuesta, matrixJsize + 1,
				arrayX.length, matrixY, arrayX.length, 1);
		// Printing values of Xtranspuesta por matrix y
		System.out.println();
		System.out.println("Result from  Transposed matrix X * Matrix Y:");
		for (int i = 0; i < resultMatXtranspProductMatY.length; i++) {
			for (int j = 0; j < resultMatXtranspProductMatY[i].length; j++) {
				System.out.print(resultMatXtranspProductMatY[i][j] + "\t");
			}
			System.out.print("\n");
		}

		// lAST MULTIPLICATION ((Xtransp*X)^-1)*(Xtransp*Y)
		double resultMatFinal[][] = MatrixHelper.matrixMultiplication(resultMatrix1, resultMatrix1.length,
				resultMatrix1[0].length, resultMatXtranspProductMatY, resultMatXtranspProductMatY.length,
				resultMatXtranspProductMatY[0].length);
		// Printing values of Xtranspuesta por matrix y
		System.out.println();
		System.out.println("\t|||\t Final Result \t|||");
		System.out.println();
		for (int i = 0; i < resultMatFinal.length; i++) {
			for (int j = 0; j < resultMatFinal[i].length; j++) {
				System.out.print(resultMatFinal[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("End of final regression...");

		System.out.println();
		System.out.println("Let's foresight");
		System.out.println();
		System.out.println("If x1=82.5 and x2=31.2, then Y="
				+ ((resultMatFinal[0][0]) + (resultMatFinal[1][0] * 82.5) + (resultMatFinal[2][0] * 31.2)));
		System.out.println();
		System.out.println("If x1=82.4 and x2=32.3, then Y="
				+ ((resultMatFinal[0][0]) + (resultMatFinal[1][0] * 82.4) + (resultMatFinal[2][0] * 32.3)));
		System.out.println();
		System.out.println("If x1=81.2 and x2=33.2, then Y="
				+ ((resultMatFinal[0][0]) + (resultMatFinal[1][0] * 81.2) + (resultMatFinal[2][0] * 33.2)));

	}

}
