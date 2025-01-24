package es.cragenomica.espiga.workflow.manager;



import com.biotechvana.shared.jobs.SharedCatalog;
import com.biotechvana.workflow.manager.JobsCatalog;
import com.biotechvana.workflow.ui.menus.JobsToolBox;
import com.biotechvana.workflow.ui.menus.JobsToolBoxItemList;
import com.biotechvana.workflow.ui.menus.JobsToolBoxJobItemTool;

import es.cragenomica.espiga.converters.FastaFastaConverterJob;
import es.cragenomica.espiga.converters.FastaTFastaConverterJob;
import es.cragenomica.espiga.converters.TfastaFastaConverterJob;
import es.cragenomica.espiga.converters.TfastaMsConverterJob;
import es.cragenomica.espiga.converters.msGenotypeConverterJob;
import es.cragenomica.espiga.jobs.CreateWeightsFileJob;
import es.cragenomica.espiga.jobs.CreatetFastaIndexFileJob;
import es.cragenomica.espiga.jobs.MergetFastaFilesJob;
import es.cragenomica.espiga.jobs.MstatspopGenomeAnalysesJob;
import es.cragenomica.espiga.jobs.MstatspopJob;
import es.cragenomica.espiga.jobs.MstatspopPostScriptJob;
import es.cragenomica.espiga.jobs.NpstatJob;
import es.cragenomica.espiga.jobs.NpstatPostScriptJob;
import es.cragenomica.espiga.jobs.SingleRegionAnalysesJob;


/**
 * Catolog will store all pipelines in it 
 * Any request to any store pipelines should be through it.
 * @author ahafez
 *
 */
public class eSPiGAJobsCatalog extends  JobsCatalog {





	public static final String ESPIGA_MAIN_PRTOCOL = "ESPIGA_MAIN_PRTOCOL";


	public eSPiGAJobsCatalog(ClassLoader classLoader) {
		super(classLoader);
	}


	@Override
	protected void initCatalog() {
		
		

		
		//     Mstatspop
		JobCatalogItem jobItem = new JobCatalogItem(
				MstatspopGenomeAnalysesJob.JOB_NAME,
				MstatspopGenomeAnalysesJob.JOB_ID, 
				MstatspopGenomeAnalysesJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		 jobItem = new JobCatalogItem(
				SingleRegionAnalysesJob.JOB_NAME,
				SingleRegionAnalysesJob.JOB_ID, 
				SingleRegionAnalysesJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		 jobItem = new JobCatalogItem(
				SingleRegionAnalysesJob.JOB_NAME,
				SingleRegionAnalysesJob.JOB_ID, 
				SingleRegionAnalysesJob.class.getName()  
				);
		jobsItems.add(jobItem );

		
		//	 ********************    
		
		
		//     Mstatspop

		
		jobItem = new JobCatalogItem(
				FastaFastaConverterJob.JOB_NAME,
				FastaFastaConverterJob.JOB_ID, 
				FastaFastaConverterJob.class.getName()  
				);
		jobsItems.add(jobItem );

		

		jobItem = new JobCatalogItem(
				TfastaFastaConverterJob.JOB_NAME,
				TfastaFastaConverterJob.JOB_ID, 
				TfastaFastaConverterJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		jobItem = new JobCatalogItem(
				FastaTFastaConverterJob.JOB_NAME,
				FastaTFastaConverterJob.JOB_ID, 
				FastaTFastaConverterJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		jobItem = new JobCatalogItem(
				TfastaMsConverterJob.JOB_NAME,
				TfastaMsConverterJob.JOB_ID, 
				TfastaMsConverterJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		
		
		jobItem = new JobCatalogItem(
				CreateWeightsFileJob.JOB_NAME,
				CreateWeightsFileJob.JOB_ID, 
				CreateWeightsFileJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		
		
		jobItem = new JobCatalogItem(
				msGenotypeConverterJob.JOB_NAME,
				msGenotypeConverterJob.JOB_ID, 
				msGenotypeConverterJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		
		jobItem = new JobCatalogItem(
				CreatetFastaIndexFileJob.JOB_NAME,
				CreatetFastaIndexFileJob.JOB_ID, 
				CreatetFastaIndexFileJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		jobItem = new JobCatalogItem(
				MergetFastaFilesJob.JOB_NAME,
				MergetFastaFilesJob.JOB_ID, 
				MergetFastaFilesJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		jobItem = new JobCatalogItem(
				NpstatJob.JOB_NAME,
				NpstatJob.JOB_ID, 
				NpstatJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		
		jobItem = new JobCatalogItem(
				NpstatPostScriptJob.JOB_NAME,
				NpstatPostScriptJob.JOB_ID, 
				NpstatPostScriptJob.class.getName()  
				);
		jobsItems.add(jobItem );
		
		
		jobItem = new JobCatalogItem(
				MstatspopPostScriptJob.JOB_NAME,
				MstatspopPostScriptJob.JOB_ID, 
				MstatspopPostScriptJob.class.getName()  
				);
		jobsItems.add(jobItem );
	}



	static {
		toolboxPerApp.put(ESPIGA_MAIN_PRTOCOL,createMaineSPiGAToolbox()); 
//		toolboxPerApp.put(MAPPING_COUNTING_PRTOCOL,createBBEDmRNAToolbox()); 
		defaultToolBoxId = ESPIGA_MAIN_PRTOCOL;
	}



	/**
	 * Main protocols
	 * @return
	 */
	public static JobsToolBox createMaineSPiGAToolbox()
	{
		JobsToolBox mRNAToolbox = new JobsToolBox();
		mRNAToolbox.setName("eSPiGA Protocol");

		
		
		
		JobsToolBoxItemList eSPiGATasks = new JobsToolBoxItemList("eSPiGA"); 
		
		JobsToolBoxJobItemTool item = new JobsToolBoxJobItemTool(
				MstatspopJob.JOB_NAME,
				MstatspopJob.class
				);
		eSPiGATasks.addItem(item);

		

		mRNAToolbox.addItem(eSPiGATasks);


		return mRNAToolbox;
	}


	


	


}
