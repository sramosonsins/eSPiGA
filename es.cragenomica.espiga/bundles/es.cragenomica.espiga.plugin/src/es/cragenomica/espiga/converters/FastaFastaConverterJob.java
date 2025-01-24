package es.cragenomica.espiga.converters;


import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class FastaFastaConverterJob  extends FastaConvtr { 

	


	
	static public final String JOB_ID = "fasta_to_fasta";
	static public final String JOB_NAME = "fasta to fasta Section";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public FastaFastaConverterJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
		this.jobDesc.addPublication(
				new Publication()
				.setAuthors("Sebastian E. Ramos-Onsins, Luca Ferretti, Emanuele Raineri, Giacomo Marmorini, William Burgos, Joan Jene and Gonzalo Vera")
				.setTitle("Variability Analyses of multiple populations: Calculation and estimation of statistics and neutrality tests")
//				.setJournal("Genome research")
//				.setVolume(22)
//				.setIssue(3)
//				.setYear(2012)
//				.setPages("568-576")
//				.setDOI("10.1101/gr.129684.111")
//				.setPubmed("22300766")
				);
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("fastaconvtr")
						.setUrl("https://github.com/CRAGENOMICA/fastaconvtr")
						.setType(JobLink.LinkType.Other)
				);
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.File,VariableRole.Input);
		varInputFile.isRequired = true;
		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA,false,false);
		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS,false,false);
		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,false,false);
		varInputFile.setVariableName("Input file");
		varInputFile.setShortHelpMsg("Drag an input file [ms, fasta or tfa]");
		varInputFile.setHelpMsg("input file: can be a ms, fasta or tfa (gz file indexed)");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);
	}


		// super.createOutputs();
		// TODO Auto-generated method stub
		
//		this.jobDesc.addCLITool( 
//				new CLIToolInfo()
//				.setId("mstatspop-")
//				.setName("mstatspop")
//				.setBaseCLI("mstatspop")
//				.setMainInputVariable()
//				.setGeneratedOutputVariable()
//				);
		




}
