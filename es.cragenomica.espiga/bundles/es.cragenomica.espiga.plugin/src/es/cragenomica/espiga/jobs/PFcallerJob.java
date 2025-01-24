package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.descriptors.CheckedVariableDescriptor;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.SelectionListVariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class PFcallerJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "PFcaller";
	static public final String JOB_NAME = "PFcaller";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public PFcallerJob() {
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
						.setTitle("PFcaller")
						.setUrl("https://github.com/CRAGENOMICA/PFcaller")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("PFcaller: A Frequency Caller for Polyploid and Pooled Sequences.");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(eSPiGAVariableBag.INPUT_FILES,VariableType.File,VariableRole.Input);
		varInputFile.isRequired = true;
		varInputFile.setVariableName("Input pileup file");
		varInputFile.setHelpMsg("Input pileup file");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);
		
		jobDesc.
		addVariable(VariableDescriptor.Checked("compress_output", VariableRole.Input)
				.setDefaultValue(true)
				.setVariableName("Compress output file")
				.setHelpMsg("Compress output file and result is .gz zipped")
				);
		
		jobDesc.addVariable(
			VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
			.setMinValue(0)
			.setVariableName("Minimum read depth")
			.setCommandParamater("-m")
			.setIsRequired(true)
			.setHelpMsg("Minimum read depth")
		);
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setMinValue(0)
				.setVariableName("Maximum read depth")
				.setCommandParamater("-M")
				.setIsRequired(true)
				.setHelpMsg("Maximum read depth")
			);
		
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setMinValue(0)
				.setVariableName("Minimum Base Quality. (Phred)")
				.setCommandParamater("-q")
				.setIsRequired(true)
				.setHelpMsg("Minimum Base Quality. (Phred)")
			);
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setMinValue(0)
				.setVariableName("ploidy")
				.setCommandParamater("-p")
				.setIsRequired(true)
			);
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setVariableName("seed")
				.setCommandParamater("-s")
				.setIsRequired(true)
			);
		jobDesc.addVariable(
				VariableDescriptor.File(OPTION_AUTO, VariableRole.Input)
				.setVariableName("Scaffold(s) length information")
				.setCommandParamater("-S")
				.setHelpMsg("File containing the name(s) of scaffold(s) and their length (separated by a tab), one per line (ex. fai file)")
				.setIsRequired(true)
			);
	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		jobDesc.addVariable(	
			VariableDescriptor.SelectionList(eSPiGAVariableBag.OUTPUT_FILES_FORMAT, VariableRole.Option)
			.addOptionValue("numeric","0","numeric format")
			.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,"f","fasta format")
			.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFASTA,"t","tFasta format")
			.addOptionValue("gVCF","g","gVCF format")
			.setDefaultIndexValue(2)
			.setIsRequired(true)
			.setCommandParamater("-f").setVariableName("Output file format").setIsRequired(true)
		);
		jobDesc.addVariable(	
				VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option)
				.addOptionValue("snm","1","snm")
				.addOptionValue("uniform","2","uniform")
				.addOptionValue("exponential","3","exponential")
				.addOptionValue("uniform-no-theta","4","uniform-no-theta")
				.setDefaultIndexValue(0)
				.setIsRequired(true)
				.setCommandParamater("-r").setVariableName("Prior dist").setIsRequired(true)
			);
		
		
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("prior value: theta/nt")
				.setCommandParamater("-t")
				.setHelpMsg("prior value: theta/nt (-1 means auto-inferred from whole data). DEFAULT: -1")
			);
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("# MC iterations")
				.setCommandParamater("-n")
				.setHelpMsg("# MC iterations. DEFAULT: 100")
			);
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("Sections divided the SNPs in relation to BaseQuality")
				.setCommandParamater("-e")
				.setHelpMsg("Sections in which are divided the SNPs in relation to BaseQuality (higher means lower precision). DEFAULT: 1")
			);
		
		jobDesc.
		addVariable(VariableDescriptor.Checked(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("Test reads at both strands")
				.setHelpMsg("Test reads at both strands (1/0). DEFAULT: 0")
				.setCommandParamater("-d")
				);
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
