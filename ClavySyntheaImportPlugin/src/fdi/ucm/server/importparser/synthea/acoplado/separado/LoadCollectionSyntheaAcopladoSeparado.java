/**
 * 
 */
package fdi.ucm.server.importparser.synthea.acoplado.separado;

import java.util.ArrayList;

import fdi.ucm.server.importparser.synthea.LoadCollectionSynthea;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class LoadCollectionSyntheaAcopladoSeparado extends LoadCollectionSynthea {


	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> DateEntrada) {
		Log=new ArrayList<String>();
		syntheaImporter=new SyntheaImporterAcopaladoSeparado(Log);
		
		if (DateEntrada!=null)	
			syntheaImporter.ProcessFile(DateEntrada);
		
		
		
		
		
		return new CompleteCollectionAndLog(syntheaImporter.getCollection(),Log);
	}



	@Override
	public String getName() {
		return "Synthea import Acoplado Separado";
	}







}
