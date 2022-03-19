package fdi.ucm.server.importparser.synthea.transform;

import java.util.HashMap;
import java.util.LinkedList;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

public class SyntheaTRANSFORM1 {

	private CompleteCollection CC;
	
	public SyntheaTRANSFORM1(CompleteCollection cC) {
		CC=cC;
	}

	public CompleteCollection aplica() {
		CompleteCollection CCSalida = new CompleteCollection("Synthea", "Docuemnt Accopled");
		
		CompleteGrammar Gramatica_del_Paciente = CC.getMetamodelGrammar().get(0);
		CCSalida.getMetamodelGrammar().add(Gramatica_del_Paciente);
		
		LinkedList<CompleteTextElementType> Conditions=new LinkedList<CompleteTextElementType>();
		CompleteTextElementType CTET=new CompleteTextElementType("Condition", Gramatica_del_Paciente);
		CTET.setMultivalued(true);
		CTET.setBrowseable(true);
		
		Gramatica_del_Paciente.getSons().add(CTET);
		Conditions.add(CTET);
		
		HashMap<String, CompleteDocuments> patientIdHash=new HashMap<String, CompleteDocuments>();
		for (CompleteDocuments DocEntero : CC.getEstructuras()) {
			for (CompleteElement ElementoDoc : DocEntero.getDescription()) {
				if (ElementoDoc instanceof CompleteTextElement)
					if (ElementoDoc.getHastype().getCollectionFather().getNombre().toLowerCase().equals("patients.csv")
							&&ElementoDoc.getHastype().getName().toLowerCase().equals("id") )
								{
								patientIdHash.put(((CompleteTextElement) ElementoDoc).getValue(), DocEntero);
								CCSalida.getEstructuras().add(DocEntero);
								}
			} 
		}
		
		
		
		
		return CCSalida;
	}

}
