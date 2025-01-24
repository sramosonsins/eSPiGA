package es.cragenomica.espiga.workflow.manager;


import com.biotechvana.workflow.manager.WorkflowCatalog;


/**
 * Catolog will store all pipelines in it 
 * Any request to any store pipelines should be through it.
 * @author ahafez
 *
 */
public class eSPiGAWorkflowCatalog extends WorkflowCatalog {



	@Override
	public void initCatalog() {
		eSPiGAPipelinesCreatorHelper.manager = this.workflowManager;
		WorkflowCatalog catalog = this;

//		catalog.addtemplateWorkflow( eSPiGAPipelinesCreatorHelper.createWorkflow_Preprocessing_CP(false) );
//		catalog.addtemplateWorkflow( eSPiGAPipelinesCreatorHelper.createWorkflow_Preprocessing_CP(true) );
//
//		catalog.addtemplateWorkflow( eSPiGAPipelinesCreatorHelper.createWorkflow_Preprocessing_T(false) );
//		catalog.addtemplateWorkflow( eSPiGAPipelinesCreatorHelper.createWorkflow_Preprocessing_T(true) );
//

		// #########################     preprocessing and mapping (SE)   ##################################################

		String[][] pmms = new String[][] {eSPiGAPipelinesCreatorHelper.PrePocessingModules_CP,
			eSPiGAPipelinesCreatorHelper.PrePocessingModules_T};
		Integer[] mapers = new Integer[]{
					eSPiGAPipelinesCreatorHelper.MAPPER_BWA,
					eSPiGAPipelinesCreatorHelper.MAPPER_Bowtie2,
					eSPiGAPipelinesCreatorHelper.MAPPER_Hisat2,
					eSPiGAPipelinesCreatorHelper.MAPPER_STAR,
					eSPiGAPipelinesCreatorHelper.MAPPER_Tophat
		};
		
		
		
	}


	@Override
	protected void setConfigurationImlp() {
		
	}


}
