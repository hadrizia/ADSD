package escalonador;
import java.io.PrintWriter;
import java.util.List;


public class Escalonador extends Thread {

	private GeradorNumerosAleatorios gerador;
	private List<Integer> numerosAleatoriosFila1;
	private List<Integer> numerosAleatoriosFila2;
	private int tempo;
	private int chegada1;
	private int chegada2;
	private int saida1;
	private int saida2;
	private int fila1;
	private int fila2;
	private int a;
	private int mod1;
	private int mod2;
	private int c;
	private int indice1;
	private int indice2;
	private boolean servico;
	private PrintWriter writer;

	/**
	 * Construtor
	 * @param tempo de execucao
	 */
	public Escalonador(int tempo) {
		this.tempo = tempo;
		this.chegada1 = 0;
		this.chegada2 = 0;
		this.saida1 = 0;
		this.saida2 = 0;
		this.fila1 = 0;
		this.fila2 = 0;
		this.servico = false;
		this.gerador = new GeradorNumerosAleatorios(7);
		this.a = 1;
		this.c = 3;
		this.mod1 = 10;
		this.mod2 = 5;
		this.numerosAleatoriosFila1 = gerador.metodoMisto(a, c, mod1);
		this.numerosAleatoriosFila2 = gerador.metodoMisto(a, c, mod2);
		this.indice1 = 0;
		this.indice2 = 0;
		
		this.escalonaChegadaFila1(0);
		this.escalonaChegadaFila2(0);

		try {
			writer = new PrintWriter("saida/escalonador", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		writer.println("-*-");
		writer.println("Tempo de escalonamento: " + this.tempo + "'");
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
				checaEventosCriadosFila1(segundos);
				checaEventosCriadosFila2(segundos);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writer.close();
	}
	
	
	public void checaEventosCriadosFila1(int segundos) {
		boolean houveAlteracao = false;
		if (segundos == chegada1) {
			houveAlteracao = true;
			writer.println("FILA 1 Evento 1 > Chegada de fregues na Fila 1 aos " + segundos + "'");
			if (!this.servico) {
				escalonaSaidaFila1(segundos);
				this.servico = true;
				writer.println("FILA 1 Evento 2 > atendimento do Sistema iniciado em "	+ segundos + "'");
				writer.println("FILA 1 atendimento do Sistema encerrado em " + (this.saida1 - segundos)	+ "'");
			} else {
				this.fila1++;
				writer.println("FILA 1 Evento 4 > esperando atendimento");
				writer.println("FILA 1 Tamanho da fila 1 em " + segundos + "'" + this.fila1);
			}
			escalonaChegadaFila1(segundos);
			writer.println("FILA 1 Proximo fregues em " + (this.chegada1 - segundos) + "'");
		}

		if (segundos == this.saida1) {
			houveAlteracao = true;
			writer.println("FILA 1 Evento 3 > atendimento do Sistema encerrado em " + segundos + "'");
			if (this.fila1 != 0) {
				this.fila1--;
				this.escalonaSaidaFila1(segundos);
				this.servico = true;
				writer.println("FILA 1 Evento 2 > atendimento do Sistema iniciado em "	+ segundos + "'");
				writer.println("FILA 1 atendimento do Sistema  encerrado em " + (saida1 - segundos)	+ "'");
			} else {
				this.servico = false;
			}
		}
		if (houveAlteracao) {
			if (this.servico) {
				writer.println("FILA 1 Sistema ocupada em " + segundos + "'");
			} else {
				writer.println("FILA 1 Sistema livre em " + segundos + "'");
			}
			writer.println("FILA 1 Tamanho da fila 1 em " + segundos + "'" + this.fila1);
			writer.println("-*-");
		}
	}
	
	public void checaEventosCriadosFila2(int segundos) {
		boolean houveAlteracao = false;
		if (segundos == chegada2) {
			houveAlteracao = true;
			writer.println("FILA 2 Evento 1 > Chegada de fregues na Fila 2 aos " + segundos + "'");
			if (!this.servico && this.fila1 == 0) {
				escalonaSaidaFila2(segundos);
				this.servico = true;
				writer.println("FILA 2 Evento 2 > atendimento do Sistema iniciado em "	+ segundos + "'");
				writer.println("FILA 2 atendimento do Sistema encerrado em " + (this.saida2 - segundos)	+ "'");
			} else {
				this.fila2++;
				writer.println("FILA 2 Evento 4 > esperando atendimento");
				writer.println("FILA 2 Tamanho da fila 2 em " + segundos + "'" + this.fila2);
			}
			escalonaChegadaFila2(segundos);
			writer.println("FILA 2 Proximo fregues em " + (this.chegada2 - segundos) + "'");
		}

		if (segundos == this.saida2) {
			houveAlteracao = true;
			writer.println("FILA 2 Evento 3 > atendimento do Sistema encerrado em " + segundos + "'");
			if (this.fila2 != 0 && this.fila1 == 0) {
				this.fila2--;
				this.escalonaSaidaFila2(segundos);
				this.servico = true;
				writer.println("FILA 2 Evento 2 > atendimento do Sistema iniciado em "	+ segundos + "'");
				writer.println("FILA 2 atendimento do Sistema  encerrado em " + (saida2 - segundos)	+ "'");
			} else {
				this.servico = false;
				
			}
		}
		if (houveAlteracao) {
			if (this.servico && this.fila2 != 0 && fila1 != 0) {
				writer.println("FILA 2 Sistema ocupada em " + segundos + "'");
			} else {
				writer.println("FILA 2 Sistema livre em " + segundos + "'");
			}
			writer.println("FILA 2 Tamanho da fila 2 em " + segundos + "'" + this.fila2);
			writer.println("-*-");
		}
	}
	

	public void geraNumerosAleatoriosFila1(){
		if (indice1 == (numerosAleatoriosFila1.size()-1)){
			this.a++;
			this.mod1++;
			this.numerosAleatoriosFila1 = gerador.metodoMisto(a, c, mod1);
			this.indice1 = 0;
		}
	}
	
	public void geraNumerosAleatoriosFila2(){
		if (indice2 == (numerosAleatoriosFila2.size()-1)){
			this.a++;
			this.mod2++;
			this.numerosAleatoriosFila2 = gerador.metodoMisto(a, c, mod2);
			this.indice2 = 0;
		}
	}
	
	
	public void escalonaChegadaFila1(int segundos) {
		this.geraNumerosAleatoriosFila1();
		this.chegada1 = segundos + numerosAleatoriosFila1.get(indice1);
		this.indice1++;
	}
	

	public void escalonaChegadaFila2(int segundos) {
		this.geraNumerosAleatoriosFila2();
		this.chegada2 = segundos + numerosAleatoriosFila2.get(indice2);
		this.indice2++;
	}
	
	
	public void escalonaSaidaFila1(int segundos) {
		this.geraNumerosAleatoriosFila1();
		this.saida1 = segundos + numerosAleatoriosFila1.get(indice1);
		this.indice1++;
	}
	
	public void escalonaSaidaFila2(int segundos) {
		this.geraNumerosAleatoriosFila2();
		this.saida2 = segundos + numerosAleatoriosFila2.get(indice2) + numerosAleatoriosFila1.get(indice1);
		this.indice2++;
	}

	public static void main(String[] args) {
		int tempoDeEscalonamento = 300; //em segundos
		Escalonador barbearia = new Escalonador(tempoDeEscalonamento);
		
		//inicia a thread
		barbearia.start();
	}
}