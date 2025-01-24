package es.cragenomica.espiga.jobs;


import com.biotechvana.pipelinestools.utils.PipelineUtils;
import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.executer.BashBlock;
import com.biotechvana.workflow.executer.BashHelper;
import com.biotechvana.workflow.executer.GeneralCMLTemplate;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class CreatetFastaIndexFileJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "create_tfasta_index";
	static public final String JOB_NAME = "Create tFasta index";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	public CreatetFastaIndexFileJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
	
		this.jobDesc.addPublication(new Publication().setAuthors(
				"Sebastian E. Ramos-Onsins, Luca Ferretti, Emanuele Raineri, Giacomo Marmorini, William Burgos, Joan Jene and Gonzalo Vera")
				.setTitle(
						"Variability Analyses of multiple populations: Calculation and estimation of statistics and neutrality tests")
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
						.setTitle("tfa_index")
						.setUrl("https://github.com/CRAGENOMICA/mstatspop")
						.setType(JobLink.LinkType.Other));
		this.jobDesc.setDesc("tfa_index: create tFasta index.");

	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.Files,VariableRole.Input);
		varInputFile.isRequired = true;
		varInputFile.setVariableName("Input tFasta files");
		varInputFile.setShortHelpMsg("Drag input tFasta file");
		varInputFile.setHelpMsg("Input tFasta files");
		
		jobDesc.addVariable(varInputFile);
	}

	@Override
	protected void createOutputs() {
//		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES,VariableType.File,VariableRole.Output);
//		varOutputFile.isRequired = true;
//		varOutputFile.setVariableName("Output index File");
//		this.jobDesc.addVariable(varOutputFile);
		
	}

	@Override
	protected void createOptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createRules() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void generateCommand(StringBuilder builder) {
		BashBlock command_script = new BashBlock();


	
		JobVariable inputVariable = getVariable(INPUT_FILES);
		JobVariable outputVariable = getVariable(OUTPUT_FILES);
		
		
		
//		indexTFasta.getBefore().addStatment(st(
//				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
//				));
//		indexTFasta.getBefore().addStatment(st(
//				BashHelper.assign("inputFolder",BashHelper.getDirName(inputVariable))
//				));
//		indexTFasta.getBefore().addStatment(st(
//				BashHelper.assign("inputExt",BashHelper.getExt(inputVariable))
//				));

		String value = getValue(INPUT_FILES);
		String[] files = value.split("\n");
		for (String file : files) {
			file = PipelineUtils.sanitizeFilePathWithTelda(
					getWorkflowManager().getUserManager().getHostPathManager().getUserHomePath(),
					file,
					getWorkflowManager().getUserManager().getIsChRooted()
							);
			GeneralCMLTemplate indexTFasta = new GeneralCMLTemplate();
			indexTFasta.setBaseCommand("tfa_index");
			indexTFasta.setTrackInput(file);
			indexTFasta.addArgs("-f");
			indexTFasta.addArgs(file);
			command_script.addStatment(indexTFasta);
			
		}
		
		

		
		command_script.generate(builder);
	}
}
