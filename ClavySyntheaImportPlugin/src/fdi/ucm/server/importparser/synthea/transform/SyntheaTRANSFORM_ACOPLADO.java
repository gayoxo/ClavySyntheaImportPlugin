package fdi.ucm.server.importparser.synthea.transform;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

public class SyntheaTRANSFORM_ACOPLADO {

	private CompleteCollection CC;
	
	public SyntheaTRANSFORM_ACOPLADO(CompleteCollection cC) {
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
		CTET.setClassOfIterator(CTET);
		
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
		
		HashMap<String, List<CompleteDocuments>> patient_conditionIdHash=new HashMap<String, List<CompleteDocuments>>();
		
		for (CompleteDocuments DocEntero : CC.getEstructuras()) {
			for (CompleteElement ElementoDoc : DocEntero.getDescription()) {
				if (ElementoDoc instanceof CompleteTextElement)
					if (ElementoDoc.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
							&&ElementoDoc.getHastype().getName().toLowerCase().equals("patient") )
								{
								List<CompleteDocuments> lista=patient_conditionIdHash.get(((CompleteTextElement) ElementoDoc).getValue());
								if (lista==null)
									lista=new LinkedList<CompleteDocuments>();
								lista.add(DocEntero);
								patient_conditionIdHash.put(((CompleteTextElement) ElementoDoc).getValue(), lista);
								}
			} 
		}
		
		
		for (Entry<String, List<CompleteDocuments>> patient_conditList : patient_conditionIdHash.entrySet()) {
			
			CompleteDocuments pacienteReal = patientIdHash.get(patient_conditList.getKey());
			if (pacienteReal!=null)
			{
				List<CompleteDocuments> condicionespat=patient_conditList.getValue();
				
				while (Conditions.size()<condicionespat.size())
				{
					CompleteTextElementType CTETo=new CompleteTextElementType("Condition", Gramatica_del_Paciente);
					CTETo.setMultivalued(true);
					CTETo.setBrowseable(true);
					CTETo.setClassOfIterator(CTET);
					Gramatica_del_Paciente.getSons().add(CTET);
					Conditions.add(CTET);
				}
				
				for (int i = 0; i < condicionespat.size(); i++) {
					for (CompleteElement textElementValues : condicionespat.get(i).getDescription()) {
						if (textElementValues instanceof CompleteTextElement)
							if (textElementValues.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
									&&textElementValues.getHastype().getName().toLowerCase().equals("description") )
										{
										String Value=((CompleteTextElement) textElementValues).getValue().replace(" ", "_").trim();
										CompleteTextElement TE=new CompleteTextElement(Conditions.get(i), Value);
										pacienteReal.getDescription().add(TE);
										}
					}
				}
				
			}
			
		}
		
		return CCSalida;
	}

}
