package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.CmdListMergeStrategy;
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

public class MergetFastaFilesJob  extends ISWFJob { 

	


	
	static public final String JOB_ID = "merge_tfasta_files";
	static public final String JOB_NAME = "Merge tFasta Files";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	public static String INPUT_MERGED_NAME= "INPUT_MERGED_NAME";

	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	public MergetFastaFilesJob() {
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
						.setTitle("tfa_merge")
						.setUrl("https://github.com/CRAGENOMICA/mstatspop")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("Merge tFasta files from different populations");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.Files(INPUT_FILES,VariableRole.Input).setCmdListMergeStrategy(CmdListMergeStrategy.Repeat);
		varInputFile.isRequired = true;
		varInputFile.setVariableName("Input tFasta files");
		varInputFile.setShortHelpMsg("Drag an input tFasta files to merge");
		varInputFile.setHelpMsg("Input tFasta files to merge");
		varInputFile.setCommandParamater("-i");
		
		jobDesc.addVariable(varInputFile);
		
		
		jobDesc.addVariable(VariableDescriptor.String(INPUT_MERGED_NAME, VariableRole.Input)
				.setVariableName("Output compressed tFasta filename prefix")
				.setDefaultValue("merged_tfasta")
				.setIsRequired(true)
				);
	}

	@Override
	protected void createOutputs() {
		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES,VariableType.File,VariableRole.Output);
		varOutputFile.isRequired = true;
		varOutputFile.setVariableName("Output merged tfasta file");
		this.jobDesc.addVariable(varOutputFile);
		
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
		JobVariable outputMergedName  = getVariable(INPUT_MERGED_NAME);

		String list_tFastas = "${outputFolder}/list_tFastas.txt";
		GeneralCMLTemplate mergedTFastaName = new GeneralCMLTemplate();
		mergedTFastaName.getBefore().addStatment(st("echo \"\" > " + list_tFastas));
		
		mergedTFastaName.getBefore().addStatment(
		BashHelper.forEachValue("_File" , inputVariable.$("@") ,
				st("echo \"${_File}\" >> " +  list_tFastas )
				));
		
		
		mergedTFastaName.getBefore().addStatment(st(
				BashHelper.assign("outputFileName","${outputFolder}/"+outputMergedName.$())
				));
		mergedTFastaName.getBefore().addStatment(
				st(BashHelper.assign(outputVariable, $("outputFileName") + ".tfa.gz"))
			);
		mergedTFastaName.setBaseCommand("tfa_merge");
		mergedTFastaName.setTrackInput(inputVariable);
		mergedTFastaName.addArgs(getOptionParamatersStrAs$());
		mergedTFastaName.addArgs("-f");
		mergedTFastaName.addArgs("-o");
		mergedTFastaName.addArgs($("outputFileName"));
		command_script.addStatment(mergedTFastaName);
		command_script.generate(builder);
	}
	
	
}
