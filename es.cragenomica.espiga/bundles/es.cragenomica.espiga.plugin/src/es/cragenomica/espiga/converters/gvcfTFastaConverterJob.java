package es.cragenomica.espiga.converters;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.descriptors.CheckedVariableDescriptor;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.executer.BashBlock;
import com.biotechvana.workflow.executer.BashConditions;
import com.biotechvana.workflow.executer.BashHelper;
import com.biotechvana.workflow.executer.GeneralCMLTemplate;
import com.biotechvana.workflow.executer.IFObject;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class gvcfTFastaConverterJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "gvcf_to_tFasta";
	static public final String JOB_NAME = "gVCF to tFasta Converter";
	
	
	public static String INPUT_FILES = eSPiGAVariableBag.INPUT_FILES;
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	public gvcfTFastaConverterJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("gVCF2tFasta: Convert VCF/gVCF files to tFasta format.")
						.setUrl("https://github.com/CRAGENOMICA/gVCF2tFasta")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("gVCF2tFasta: Convert VCF/gVCF files to tFasta format.");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.File,VariableRole.Input);
		varInputFile.isRequired = true;
		varInputFile.setVariableName("Input VCF/gVCF file");
		varInputFile.setHelpMsg("Input VCF/gVCF file");
		varInputFile.setCommandParamater("-v");
		jobDesc.addVariable(varInputFile);
		
		jobDesc.addVariable(VariableDescriptor.File(WorkflowJob.OPTION_AUTO, VariableRole.Input)
				.setVariableName("Reference Fasta file")
				.setCommandParamater("-r")
				.setHelpMsg("Reference Fasta file"));
		
		jobDesc.addVariable(VariableDescriptor.File(WorkflowJob.OPTION_AUTO, VariableRole.Input)
		.setVariableName("Chromosome info file")
		.setCommandParamater("-n")
		.setHelpMsg("File with chromosome(s) to convert and its length"));
		
		jobDesc.addVariable(VariableDescriptor.Checked(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("Imputation")
				.setHelpMsg("Imputation (Only use with VCF files, not gVCF files). uncheck(0/false) if missing data in VCF is equal to N in tFasta. check(1/true) if missing data in VCF is equal to reference fasta in tFasta")
				.setCommandParamater("-i")
				);
	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES, VariableType.FileList,
				VariableRole.Output);
		varInputFile.isRequired = true;
		varInputFile.setVariableName("output file");
		varInputFile.setCommandParamater("-o");
		jobDesc.addVariable(varInputFile);

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
		
		
		GeneralCMLTemplate gVCF2tFasta = new GeneralCMLTemplate();
		gVCF2tFasta.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				));
		gVCF2tFasta.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.tfa.gz"))
			);
		gVCF2tFasta.getBefore().addStatment(st(
				BashHelper.assign("outputFileName","${outputFolder}/${outputFileName}")
				));
		gVCF2tFasta.setBaseCommand("gVCF2tFasta");
		gVCF2tFasta.setTrackInput(inputVariable);
		gVCF2tFasta.addArgs(getOptionParamatersStrAs$());
		gVCF2tFasta.addArgs("-o");
		gVCF2tFasta.addArgs($("outputFileName"));

		command_script.addStatment(gVCF2tFasta);
		command_script.generate(builder);
	}
	
}
