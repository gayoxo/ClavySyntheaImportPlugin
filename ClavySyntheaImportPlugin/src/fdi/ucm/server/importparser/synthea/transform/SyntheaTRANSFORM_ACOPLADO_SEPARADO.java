package fdi.ucm.server.importparser.synthea.transform;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

public class SyntheaTRANSFORM_ACOPLADO_SEPARADO {

private CompleteCollection CC;
	
	public SyntheaTRANSFORM_ACOPLADO_SEPARADO(CompleteCollection cC) {
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
		

		Conditions.add(CTET);
		
		LinkedList<CompleteTextElementType> Disorders=new LinkedList<CompleteTextElementType>();
		CompleteTextElementType DTET=new CompleteTextElementType("Disorders", Gramatica_del_Paciente);
		DTET.setMultivalued(true);
		DTET.setBrowseable(true);
		DTET.setClassOfIterator(DTET);
		
		Disorders.add(DTET);
		
		
		
		LinkedList<CompleteTextElementType> Findings=new LinkedList<CompleteTextElementType>();
		CompleteTextElementType FTET=new CompleteTextElementType("Findings", Gramatica_del_Paciente);
		FTET.setMultivalued(true);
		FTET.setBrowseable(true);
		FTET.setClassOfIterator(FTET);
		
		Findings.add(FTET);
		
		
		
		
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
				
				List<CompleteDocuments> condicionesSolas=new LinkedList<CompleteDocuments>();
				List<CompleteDocuments> disorderSolas=new LinkedList<CompleteDocuments>();
				List<CompleteDocuments> findingsSolas=new LinkedList<CompleteDocuments>();
				

					for (CompleteDocuments completeDocuments : condicionespat) {
						for (CompleteElement completeelemento2Doc : completeDocuments.getDescription()) {
							if (completeelemento2Doc instanceof CompleteTextElement
									&&
									completeelemento2Doc.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
									&&
									completeelemento2Doc.getHastype().getName().toLowerCase().equals("description")
									)
								
								if (Pattern.compile("(disorder)").matcher(((CompleteTextElement) completeelemento2Doc).getValue().toLowerCase()).find())
									disorderSolas.add(completeDocuments);
								else if (Pattern.compile("(finding)").matcher(((CompleteTextElement) completeelemento2Doc).getValue().toLowerCase()).find())
									findingsSolas.add(completeDocuments);
								else
									condicionesSolas.add(completeDocuments);
								
						}
					}
				
				
				
				
				while (Conditions.size()<condicionesSolas.size())
				{
					CompleteTextElementType CTETo=new CompleteTextElementType("Condition", Gramatica_del_Paciente);
					CTETo.setMultivalued(true);
					CTETo.setBrowseable(true);
					CTETo.setClassOfIterator(CTET);
					Conditions.add(CTETo);
				}

				for (int i = 0; i < condicionesSolas.size(); i++) {
					for (CompleteElement textElementValues : condicionesSolas.get(i).getDescription()) {
						if (textElementValues instanceof CompleteTextElement)
							if (textElementValues.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
									&&textElementValues.getHastype().getName().toLowerCase().equals("description") )
										{
										String Value=((CompleteTextElement) textElementValues).getValue()
												.trim().replace(" ", "_").replace("-", "_minus_").replace("+", "_plus_");
										CompleteTextElement TE=new CompleteTextElement(Conditions.get(i), Value);
										pacienteReal.getDescription().add(TE);
										}
					}
				}
				
				
				
				
				
				while (Findings.size()<findingsSolas.size())
				{
					CompleteTextElementType FTETo=new CompleteTextElementType("Findings", Gramatica_del_Paciente);
					FTETo.setMultivalued(true);
					FTETo.setBrowseable(true);
					FTETo.setClassOfIterator(FTET);
					Findings.add(FTETo);
				}

				for (int i = 0; i < findingsSolas.size(); i++) {
					for (CompleteElement textElementValues : findingsSolas.get(i).getDescription()) {
						if (textElementValues instanceof CompleteTextElement)
							if (textElementValues.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
									&&textElementValues.getHastype().getName().toLowerCase().equals("description") )
										{
										String Value=((CompleteTextElement) textElementValues).getValue();
										Value=Value.replace("(finding)","").trim();
										Value=Value.replace(" ", "_").replace("-", "_minus_").replace("+", "_plus_");
										CompleteTextElement TE=new CompleteTextElement(Findings.get(i), Value);
										pacienteReal.getDescription().add(TE);
										}
					}
				}
				
				while (Disorders.size()<disorderSolas.size())
				{
					CompleteTextElementType DTETo=new CompleteTextElementType("Disorders", Gramatica_del_Paciente);
					DTETo.setMultivalued(true);
					DTETo.setBrowseable(true);
					DTETo.setClassOfIterator(DTET);
					Disorders.add(DTETo);
				}

				for (int i = 0; i < disorderSolas.size(); i++) {
					for (CompleteElement textElementValues : disorderSolas.get(i).getDescription()) {
						if (textElementValues instanceof CompleteTextElement)
							if (textElementValues.getHastype().getCollectionFather().getNombre().toLowerCase().equals("conditions.csv")
									&&textElementValues.getHastype().getName().toLowerCase().equals("description") )
										{
										String Value=((CompleteTextElement) textElementValues).getValue();
										Value=Value.replace("(disorder)","").trim();
										Value=Value.replace(" ", "_").replace("-", "_minus_").replace("+", "_plus_");
										CompleteTextElement TE=new CompleteTextElement(Disorders.get(i), Value);
										pacienteReal.getDescription().add(TE);
										}
					}
				}
			}
			
		}
		
		
		Gramatica_del_Paciente.getSons().add(CTET);
		for (CompleteTextElementType completeTextElementType : Conditions) 
			if (CTET!=completeTextElementType)
				Gramatica_del_Paciente.getSons().add(completeTextElementType);
		
		Gramatica_del_Paciente.getSons().add(DTET);
		for (CompleteTextElementType completeTextElementType : Disorders) 
			if (DTET!=completeTextElementType)
				Gramatica_del_Paciente.getSons().add(completeTextElementType);
		
		Gramatica_del_Paciente.getSons().add(FTET);
		for (CompleteTextElementType completeTextElementType : Findings) 
			if (FTET!=completeTextElementType)
				Gramatica_del_Paciente.getSons().add(completeTextElementType);
		
		
		return CCSalida;
	}
	
	public static void main(String[] args) {
		
		{
		String Value="Diabetic renal disease (disorder)";
		Value=Value.replace("(disorder)","").trim();
		Value=Value.replace(" ", "_");
		
		System.out.println(Value);
		}
		
		{
			String Value="Loss of taste (finding)";
			Value=Value.replace("(finding)","").trim();
			Value=Value.replace(" ", "_");
			
			System.out.println(Value);
			}
		
		
		
	}
}
