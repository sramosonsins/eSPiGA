package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class MlcoalsimJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "mlcoalsim";
	static public final String JOB_NAME = "mlcoalsim v2";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public MlcoalsimJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
//		this.jobDesc.addPublication(
//				new Publication()
//				.setAuthers("Sebastian E. Ramos-Onsins, Luca Ferretti, Emanuele Raineri, Giacomo Marmorini, William Burgos, Joan Jene and Gonzalo Vera")
//				.setTitle("Variability Analyses of multiple populations: Calculation and estimation of statistics and neutrality tests")
////				.setJournal("Genome research")
////				.setVolume(22)
////				.setIssue(3)
////				.setYear(2012)
////				.setPages("568-576")
////				.setDOI("10.1101/gr.129684.111")
////				.setPubmed("22300766")
//				);
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("mlcoalsim-v2")
						.setUrl("https://github.com/CRAGENOMICA/mlcoalsim-v2")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("mlcoalsim-v2: coalescent simulator for multiple genes, include variable recombination within and complex demography");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.Files,VariableRole.Input);
		varInputFile.isRequired = true;
		varInputFile.setVariableName("Input config files");
		varInputFile.setHelpMsg("Input config files");
		jobDesc.addVariable(varInputFile);
	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createOptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createRules() {
		// TODO Auto-generated method stub
		
	}
   // TODO :: validate at least two files.
}
