package it.polito.tdp.borders.model;

import java.util.List;

public class TestModel {
	public static void main(String args[]) {
		
		Model m = new Model() ;
		
		m.creaGrafo(1950);
		
		List<Country> vertici = m.getVertex();
		
		m.setSimulazione(vertici.get(0));
		System.out.println(m.getCountryMigranti());
		System.out.println(m.getT());

		
	}

}
