package it.polito.tdp.borders.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> graph ;
	private Map<Integer,Country> countriesMap ;
	private Simulatore sim;
	
	public Model() {
		this.countriesMap = new HashMap<>() ;
		sim = new Simulatore();

	}
	
	public void creaGrafo(int anno) {
		
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		dao.getCountriesFromYear(anno,this.countriesMap) ;
		Graphs.addAllVertices(graph, this.countriesMap.values()) ;
		
		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno) ;
		for( Adiacenza c: archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
	}
	
	public List<CountryAndNumber> getCountryAndNumbers(){
		List<CountryAndNumber> result = new LinkedList<>();
		
		for(Country c: this.graph.vertexSet()) {	
			result.add(new CountryAndNumber(c,this.graph.degreeOf(c)));
		}
		
		Collections.sort(result);
		return result;
	}
	
	public void setSimulazione(Country c) {
		if(graph != null) {
			sim.init(this.graph, c);
			sim.run();
		}

	}
	
	public List<CountryAndNumber> getCountryMigranti(){
		List<CountryAndNumber> result = new LinkedList<>();
		Map<Country, Integer> mappaCountry = sim.getStanziali();
		for(Country c: mappaCountry.keySet()) {
			if(mappaCountry.get(c) > 0) {
				result.add(new CountryAndNumber(c,mappaCountry.get(c)));				
			}
		}
		Collections.sort(result);
		return result;
	}
	
	public Integer getT() {
		return sim.getT();
	}
	

	public List<Country> getVertex(){
		return new LinkedList<Country>(graph.vertexSet());
	}
}
