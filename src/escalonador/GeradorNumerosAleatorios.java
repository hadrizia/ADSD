package escalonador;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GeradorNumerosAleatorios {

	private List<Integer> valores;
	private int semente;

	public GeradorNumerosAleatorios(int semente) {
		this.valores = new ArrayList<Integer>();
		this.semente = semente;
	}
	
	
	public List<Integer> metodoMisto(int a, int c, int mod) {
		valores = new ArrayList<Integer>();
		valores.add(semente);
		if (a < mod && c < mod) {
			for (int i = 0; i < mod - 1; i++) {
				int xn = valores.get(i);
				int xn1 = ((a * xn) + c) % mod;
				valores.add(xn1);
			}
		}
		return valores;
	}

	
	public List<Integer> metodoMultiplicativo(int a, int mod) {
		return metodoMisto(a, 0, mod);
	}

	
	public List<Integer> metodoAditivo(List<Integer> sequenciaInicial, int mod) {
		valores.clear();
		valores.addAll(sequenciaInicial);
		for (int i = 0; i <= sequenciaInicial.size(); i++) {
			int novoNum = (valores.get(valores.size() - 1) + valores.get(i)) % mod;
			valores.add(novoNum);
		}
		return valores;
	}
	
	public void setSemente(int novaSemente){
		this.semente = novaSemente;
	}
	


	public static void gerarArquivo(String metodo, String valores) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("saida/" + metodo, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.println(valores);
		writer.close();
	}
	
	public static void main(String[] args) {
		GeradorNumerosAleatorios gerador = new GeradorNumerosAleatorios(5);
		gerarArquivo("metodo misto_fila1",Arrays.toString(gerador.metodoMisto(1, 3, 10).toArray()));
		gerarArquivo("metodo misto_fila2",Arrays.toString(gerador.metodoMisto(1, 3, 5).toArray()));
		gerarArquivo("metodo multiplicativo",Arrays.toString(gerador.metodoMultiplicativo(7, 10).toArray()));
		gerarArquivo("metodo aditivo",Arrays.toString(gerador.metodoAditivo(new ArrayList<Integer>(Arrays.asList(25, 55, 78, 87, 32)), 100).toArray()));
	}
}