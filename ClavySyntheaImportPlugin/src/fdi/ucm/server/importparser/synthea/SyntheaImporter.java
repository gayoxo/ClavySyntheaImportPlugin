package fdi.ucm.server.importparser.synthea;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fdi.ucm.server.importparser.csv.CSVImporter;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;

public class SyntheaImporter {

	private CompleteCollection CC;
	private List<String> Log;

	public SyntheaImporter(List<String> log) {
		CC=new CompleteCollection("Synthea", "Synthea");
		Log=log;
	}

	public void ProcessFile(ArrayList<String> dateEntrada) {
		for (String string : dateEntrada) {
			
			try {
				CSVImporter CSVImporterIN = new CSVImporter(Log);
				CSVImporterIN.ProcessFile(string);
				CompleteCollection CinAdd = CSVImporterIN.getCollection();
				
				CC.getMetamodelGrammar().addAll(CinAdd.getMetamodelGrammar());
				CC.getEstructuras().addAll(CinAdd.getEstructuras());

				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

	public CompleteCollection getCollection() {
		return CC;
	}

	public static void main(String[] args) {
		ArrayList<String> Filepath=new ArrayList<String>();
		Filepath.add("ejemplo/patients.csv");
		Filepath.add("ejemplo/conditions.csv");

		LinkedList<String> Logs=new LinkedList<String>();
		SyntheaImporter SI=new SyntheaImporter(Logs);
		SI.ProcessFile(Filepath);
		CompleteCollection ccfin = SI.getCollection();
		
		for (CompleteDocuments docume : ccfin.getEstructuras()) {
			System.out.println("docu"+docume.getDescriptionText());
			for (CompleteElement eleme : docume.getDescription()) {
				System.out.println("-----"+eleme.getHastype().getName()+
						":"+((CompleteTextElement)eleme).getValue());
			}
		}
		
		for (String string : Logs) {
			System.err.println(string);
		}
		if (Logs.isEmpty())
			System.out.println("Correcto");
	}
	
}
