package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class HKAdirectJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "HKAdirect";
	static public final String JOB_NAME = "HKAdirect";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public HKAdirectJob() {
		super(JOB_ID, JOB_NAME, true,false);
		

		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("HKAdirect")
						.setUrl("https://github.com/CRAGENOMICA/HKAdirect")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("HKAdirect: This program computes the HKA from a dataset table for a sampled population \n"
				+ "and a single outgroup.\n"
				+ "\n"
				+ "The program computes the expected polymorphism and divergence as well as the \n"
				+ "theta values per nucleotide, the Time to the ancestor, the partial HKA for each\n"
				+ "locus (window), the Chi-square and the P-value. \n"
				+ "\n"
				+ "NOTE: Check the final Chi-square result by simulation methods. The final result follows approximately a Chi-square distribution.\n"
				+ "");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.Files,VariableRole.Input);
		varInputFile.isRequired = true;
		
		varInputFile.setVariableName("Input config file");
		varInputFile.setHelpMsg("Input config file. The input file must be the following format\n"
				+ "\n"
				+ "1st line: Title\n"
				+ "2nd line: nloci \n"
				+ "3er line: header: IDlocus nsam SegSites Divergence length_pol length_div factor_chrn [%missing]\n"
				+ "4th and rest: name and values for each locus \n"
				+ "Note: [] is  an optional value.");
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
