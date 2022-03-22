package fdi.ucm.server.importparser.synthea.transform;

import java.util.regex.Pattern;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;

public class SyntheaTRANSFORM_REMOVE_DISORDER {

	private CompleteCollection CC;

	public SyntheaTRANSFORM_REMOVE_DISORDER(CompleteCollection cC) {
		CC=cC;
	}

	public CompleteCollection aplica() {
		CompleteCollection CCSalida = new CompleteCollection("Synthea", "RemoveFinding Accopled");
		
		CCSalida.getMetamodelGrammar().addAll(CC.getMetamodelGrammar());
		CCSalida.getEstructuras().addAll(CC.getEstructuras());

		
//		List<CompleteDocuments> A_Borrar=new LinkedList<CompleteDocuments>();
		
		System.out.println("TB0="+CCSalida.getEstructuras().size());
		
		for (CompleteDocuments completeDocuments : CC.getEstructuras()) {
			for (CompleteElement completeelemento2Doc : completeDocuments.getDescription()) {
				if (completeelemento2Doc instanceof CompleteTextElement
						&&
						completeelemento2Doc.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
						&&
						completeelemento2Doc.getHastype().getName().toLowerCase().equals("description")
						&&
						Pattern.compile("(disorder)").matcher(((CompleteTextElement) completeelemento2Doc).getValue().toLowerCase()).find()
						)
					CCSalida.getEstructuras().remove(completeDocuments);
					
			}
		}
		
		System.out.println("TB1="+CCSalida.getEstructuras().size());
		
		
		
		
		
		return CCSalida;
	}
	
	public static void main(String[] args) {
		System.out.println(Pattern.compile("(disorder)").matcher("Viral sinusitis (disorder)").find());
		System.out.println(Pattern.compile("(disorder)").matcher("Suspected COVID-19").find());
	}
}
