package fdi.ucm.server.importparser.synthea.conditionsonly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fdi.ucm.server.importparser.synthea.SyntheaImporter;
import fdi.ucm.server.importparser.synthea.transform.SyntheaTRANSFORM_ACOPLADO;
import fdi.ucm.server.importparser.synthea.transform.SyntheaTRANSFORM_REMOVE_DISORDER;
import fdi.ucm.server.importparser.synthea.transform.SyntheaTRANSFORM_REMOVE_FINDING;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;

public class SyntheaImporterConditionsOnly extends SyntheaImporter{



	public SyntheaImporterConditionsOnly(List<String> log) {
		super(log);
	}

	public void ProcessFile(ArrayList<String> dateEntrada) {
		
		super.ProcessFile(dateEntrada);
		
		CC=new SyntheaTRANSFORM_REMOVE_FINDING(CC).aplica();
		CC=new SyntheaTRANSFORM_REMOVE_DISORDER(CC).aplica();
		
		CC=new SyntheaTRANSFORM_ACOPLADO(CC).aplica();
		
	}

	

	public static void main(String[] args) {
		ArrayList<String> Filepath=new ArrayList<String>();
		Filepath.add(args[0]+"/patients.csv");
		Filepath.add("id");
		Filepath.add(args[0]+"/conditions.csv");
		Filepath.add("NO TIENE IDENTIFIER");
		
		
		LinkedList<String> Logs=new LinkedList<String>();
		SyntheaImporterConditionsOnly SI=new SyntheaImporterConditionsOnly(Logs);
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
