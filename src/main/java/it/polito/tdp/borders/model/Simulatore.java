package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {

	//modello -> qual è lo stato del sistema ad ogni passo
	private Graph<Country, DefaultEdge> grafo;
	
	
	//tipi di evento -> coda prioritaria
	private PriorityQueue<Evento> queue;
	
	//parametri simulazione
	private int N_MIGRANTI = 1000;
	private Country partenza;
	
	//valori in output
	private int T = -1;
	private Map<Country, Integer> stanziali;
	
	public void init(Graph<Country, DefaultEdge> grafo, Country country) {
		partenza = country;
		this.grafo = grafo;
		
		//impostare stato iniziale
		this.T = 1;
		stanziali = new HashMap<>();
		for(Country c : this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		
		//creo coda
		this.queue = new PriorityQueue<>();
		queue.add(new Evento(T, partenza, N_MIGRANTI));
		
	}
	
	public void run() {
		Evento e;
		while((e = this.queue.poll())!=null) {
			int nPersone = e.getN();
			Country stato = e.getCountry();
			this.T = e.getT();
			
			//ottengo i vicini di stato
			List<Country> vicini = Graphs.neighborListOf(this.grafo, stato);
			int migrantiPerStato = (nPersone/2)/vicini.size();
			
			if(migrantiPerStato > 0) {
				//le persone si possono muovere
				for(Country confinante: vicini) {
					queue.add(new Evento(T + 1,confinante,migrantiPerStato));
				}
			}
			
			int nStanziali = nPersone - (migrantiPerStato*vicini.size());
			this.stanziali.put(stato, nStanziali + this.stanziali.get(stato));
			
		}
		
	}
	
	public Map<Country, Integer> getStanziali(){
		return this.stanziali;
	}

	public Integer getT() {
		return T;
	}
}
