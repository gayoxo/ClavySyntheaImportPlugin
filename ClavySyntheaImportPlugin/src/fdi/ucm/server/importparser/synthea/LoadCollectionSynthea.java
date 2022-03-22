/**
 * 
 */
package fdi.ucm.server.importparser.synthea;

import java.util.ArrayList;
import fdi.ucm.server.modelComplete.ImportExportDataEnum;
import fdi.ucm.server.modelComplete.ImportExportPair;
import fdi.ucm.server.modelComplete.LoadCollection;
import fdi.ucm.server.modelComplete.collection.CompleteCollectionAndLog;

/**
 * @author Joaquin Gayoso-Cabada
 *
 */
public class LoadCollectionSynthea extends LoadCollection {

	protected static ArrayList<ImportExportPair> Parametros;
	protected ArrayList<String> Log;
	protected SyntheaImporter syntheaImporter;
	


	public ArrayList<ImportExportPair> getConfiguracion() {
		if (Parametros==null)
		{
			ArrayList<ImportExportPair> ListaCampos=new ArrayList<ImportExportPair>();
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.File, "conditions CSV"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "conditions identiffier"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.File, "patiens CSV"));
			ListaCampos.add(new ImportExportPair(ImportExportDataEnum.Text, "patiens identiffier"));
			Parametros=ListaCampos;
			return ListaCampos;
		}
		else return Parametros;
	}



	@Override
	public CompleteCollectionAndLog processCollecccion(ArrayList<String> DateEntrada) {
		Log=new ArrayList<String>();
		syntheaImporter=new SyntheaImporter(Log);
		
		if (DateEntrada!=null)	
			syntheaImporter.ProcessFile(DateEntrada);
		
		
		
		
		
		return new CompleteCollectionAndLog(syntheaImporter.getCollection(),Log);
	}



	@Override
	public String getName() {
		return "Synthea import";
	}



	@Override
	public boolean getCloneLocalFiles() {
		return false;
	}
	




}
