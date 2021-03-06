package fdi.ucm.server.importparser.synthea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fdi.ucm.server.importparser.csv.CSVImporter;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;

public class SyntheaImporter {


	protected CompleteCollection CC;
	protected List<String> Log;

	public SyntheaImporter(List<String> log) {
		CC=new CompleteCollection("Synthea", "Synthea");
		Log=log;
	}

	public void ProcessFile(ArrayList<String> dateEntrada) {
		
		String valor= null;
		
		for (int i = 0; i < dateEntrada.size(); i++) {
			
		if (i % 2 == 0)
			valor = dateEntrada.get(i);
		else
		{
			
		
			
			try {
				CSVImporter CSVImporterIN = new CSVImporter(Log);
				CSVImporterIN.ProcessFile(valor,dateEntrada.get(i));
				CompleteCollection CinAdd = CSVImporterIN.getCollection();
				
				CC.getMetamodelGrammar().addAll(CinAdd.getMetamodelGrammar());
				CC.getEstructuras().addAll(CinAdd.getEstructuras());

				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		}
		
		
	}

	public CompleteCollection getCollection() {
		return CC;
	}

	public static void main(String[] args) {
		ArrayList<String> Filepath=new ArrayList<String>();
		Filepath.add(args[0]+"/patients.csv");
		Filepath.add("id");
		Filepath.add(args[0]+"/conditions.csv");
		Filepath.add("NO TIENE IDENTIFIER");
		
		
		LinkedList<String> Logs=new LinkedList<String>();
		SyntheaImporter SI=new SyntheaImporter(Logs);
		SI.ProcessFile(Filepath);
		CompleteCollection ccfin = SI.getCollection();
		
		for (CompleteDocuments docume : ccfin.getEstructuras()) {
			System.out.println("docu_>"+docume.getDescriptionText());
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
		
		 try {
				String FileIO = System.getProperty("user.home")+File.separator+System.currentTimeMillis()+".clavy";
				
				System.out.println(FileIO);
				
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FileIO));

				oos.writeObject(ccfin);

				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
}
