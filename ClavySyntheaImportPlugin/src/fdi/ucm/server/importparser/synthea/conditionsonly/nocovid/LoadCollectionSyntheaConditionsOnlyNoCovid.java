/**
 * 
 */
package fdi.ucm.server.importparser.synthea.conditionsonly.nocovid;

import java.util.ArrayList;

import fdi.ucm.server.importparser.synthea.LoadCollectionSynthea;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class LoadCollectionSyntheaConditionsOnlyNoCovid extends LoadCollectionSynthea {


	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> DateEntrada) {
		Log=new ArrayList<String>();
		syntheaImporter=new SyntheaImporterConditionsOnlyNoCovid(Log);
		
		if (DateEntrada!=null)	
			syntheaImporter.ProcessFile(DateEntrada);
		
		
		
		
		
		return new CompleteCollectionAndLog(syntheaImporter.getCollection(),Log);
	}



	@Override
	public String getName() {
		return "Synthea import Solo Conditions y sin Covid";
	}







}
