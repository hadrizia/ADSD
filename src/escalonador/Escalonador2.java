package escalonador;
import java.io.PrintWriter;
import java.util.List;


public class Escalonador2 extends Thread {

	private GeradorNumerosAleatorios gerador;
	private List<Integer> numerosAleatorios;
	private int tempo;
	private int chegada;
	private int saida;
	private int filaChegada1;
	private int filaChegada2;
	private int a;
	private int m;
	private int indice;
	private boolean servico;
	private PrintWriter writer;

	/**
	 * Construtor
	 * @param tempo de execucao
	 */
	public Escalonador2(int tempo) {
		this.tempo = tempo;
		this.chegada = 0;
		this.saida = 0;
		this.filaChegada1 = 0;
		this.filaChegada2 = 0;
		this.servico = false;
		this.gerador = new GeradorNumerosAleatorios(7);
		this.a = 5;
		this.m = 32;
		this.numerosAleatorios = gerador.metodoMultiplicativo(a, m);
		this.indice = 0;
		this.escalonaChegadaFila1(0);
		this.escalonaChegadaFila2(0);
		

		try {
			writer = new PrintWriter("saida/escalonador", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		writer.println("-*-");
		writer.println("Tempo de escalonamento" + this.tempo + "'");
		writer.println(" Chegada de fregues em " + this.chegada + "'");
		writer.println("-*-");
		writer.println();
	}

	@Override
	public void run() {
		int segundos = 0;
		while (segundos < this.tempo) {
			try {
				this.sleep(1);
				segundos++;
				checaEventosCriados(segundos);
//				//checaEventosCriadosFila2(segundos);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writer.close();
	}
	
	
	public void checaEventosCriados(int segundos) {
		boolean houveAlteracao = false;
		if (segundos == this.chegada) {
			houveAlteracao = true;
			writer.println("Evento 1 > Chegada de fregues da fila 1 aos " + segundos + "'");
			if (!this.servico) {
				escalonaSaida(segundos);
				this.servico = true;
				writer.println("atendimento da barbearia iniciado em "	+ segundos + "'");
				writer.println("atendimento da barbearia encerrado em " + (this.saida - segundos)	+ "'");
			} else {
				this.filaChegada1++;
			}
			escalonaChegadaFila1(segundos);
			writer.println("Proximo fregues da fila 1 em " + (this.chegada - segundos) + "'");
		}

		if (segundos == this.saida) {
			houveAlteracao = true;
			writer.println("Evento 3 > atendimento da barbearia encerrado em " + segundos + "'");
			if (this.filaChegada1 != 0) {
				this.filaChegada1--;
				this.escalonaSaida(segundos);
				this.servico = true;
				writer.println("Evento 2 > atendimento da barbearia iniciado em "	+ segundos + "'");
				writer.println("atendimento da barbearia  encerrado em " + (saida - segundos)	+ "'");
			} else {
				this.servico = false;
			}
		}
		if (houveAlteracao) {
			if (this.servico) {
				writer.println("Barbearia ocupada em " + segundos + "'");
			} else {
				writer.println("Barbearia livre em " + segundos + "'");
			}
			writer.println("Tamanho da fila 1 em " + segundos + "'" + filaChegada1);
			writer.println("-*-");
		}
	}


	public void geraNumerosAleatorios(){
		if (indice == (numerosAleatorios.size()-1)){
			this.a++;
			this.m++;
			this.numerosAleatorios = gerador.metodoMultiplicativo(a, m);
			this.indice = 0;
		}
	}
	
	
	public void escalonaChegadaFila1(int segundos) {
		this.geraNumerosAleatorios();
		this.chegada = segundos + numerosAleatorios.get(indice);
		this.indice++;
	}
	
	public void escalonaChegadaFila2(int segundos) {
		this.geraNumerosAleatorios();
		this.chegada = segundos + numerosAleatorios.get(indice);
		this.indice++;
	}

	
	public void escalonaSaida(int segundos) {
		this.geraNumerosAleatorios();
		this.saida = segundos + numerosAleatorios.get(indice);
		this.indice++;
	}

	public static void main(String[] args) {
		int tempoDeEscalonamento = 300; //em segundos
		Escalonador2 barbearia = new Escalonador2(tempoDeEscalonamento);
		
		//inicia a thread
		barbearia.start();
	}
}