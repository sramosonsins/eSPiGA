package es.cragenomica.espiga.converters;


import com.biotechvana.workflow.VariablesRule;
import com.biotechvana.workflow.VariablesRule.Action;
import com.biotechvana.workflow.VariablesRule.Condition;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.SelectionListVariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.executer.BashBlock;
import com.biotechvana.workflow.executer.BashConditions;
import com.biotechvana.workflow.executer.BashHelper;
import com.biotechvana.workflow.executer.GeneralCMLTemplate;
import com.biotechvana.workflow.executer.IFObject;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;

public class FastaTFastaConverterJob  extends FastaConvtr { 

	


	
	static public final String JOB_ID = "fasta_to_tFasta";
	static public final String JOB_NAME = "fasta to tFasta Converter";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public FastaTFastaConverterJob() {
		super(JOB_ID, JOB_NAME, true,false);
		

		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("fastaconvtr")
						.setUrl("https://github.com/CRAGENOMICA/fastaconvtr")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("fastaconvtr: Convert Fasta to tFasta.");
	}

	@Override
	protected void createInputs() {
		

		SelectionListVariableDescriptor inputFileFormat = FastaconvrtrHelper.getInputFileFormatList(VariableRole.Internal);
		this.jobDesc.addVariable(inputFileFormat);
		inputFileFormat.setDefaultIndexValue(1);
//		inputFileFormat.setGUIVisible(false);
		inputFileFormat.setReadOnly(true);
		
		SelectionListVariableDescriptor outputFileFormat = FastaconvrtrHelper.getOutputFileFormatList(VariableRole.Internal);
		this.jobDesc.addVariable(outputFileFormat);
		outputFileFormat.setDefaultIndexValue(0);
//		outputFileFormat.setGUIVisible(false);
		outputFileFormat.setReadOnly(true);
		
		VariableDescriptor inVar = FastaconvrtrHelper.getInputFile();
		inVar.setVariableName("Input fasta file");
		this.jobDesc.addVariable(inVar);
		
		this.jobDesc.addVariable( VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Input)
		.addOptionValue("haplotype","1","single sequence")
		.addOptionValue("genotype","2","two diploid mixed sequences in IUPAC format")
		.setDefaultIndexValue(0)
		.setHelpMsg("Type if input is fasta,\n"
				+ " haplotype: 1 (single sequence)\n"
				+ " genotype:  2 (two diploid mixed sequences in IUPAC format. WARNING! lowercase will be considered as one haplotype missing!). DEFAULT: 1")
		.setIsRequired(true)
		.setCommandParamater("-p").setVariableName("fasta input type").setIsRequired(true));
		
		

		this.jobDesc.addVariable(FastaconvrtrHelper.getInputScaffoldInfoFile());
		this.jobDesc.addVariable(
				VariableDescriptor.Checked(COMPRESS_OUTPUT, VariableRole.Input)
				.setVariableName("compress output .gz format"));
	}

	@Override
	protected void createOutputs() {
		super.createOutputs();
	}

	
	
	@Override
	protected void generateCommand(StringBuilder builder) {
		// Handle custom variables
		BashBlock command_script = new BashBlock();
		
		JobVariable custom_order = getVariable(eSPiGAVariableBag.OPTIONAL_CUSTOM_ORDER);
		String custom_order_variable = "CUSTOM_ORDER";
		command_script.addStatment(
				new IFObject(BashConditions.True(custom_order))
				.Then(
						st(BashHelper.assign(custom_order_variable,"-O " + getVariable(eSPiGAVariableBag.OPTIONAL_N_SAMPLES).$() + " " + getVariable(eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER).get_cmdValue() ))
					)
				);
		
		
		
		JobVariable inputVariable = getVariable(INPUT_FILES);
		JobVariable outputVariable = getVariable(eSPiGAVariableBag.OUTPUT_FILES);
		
		
		GeneralCMLTemplate fastaconvtr = new GeneralCMLTemplate();
		
		
		fastaconvtr.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				));
		
		fastaconvtr.getBefore().addStatment(
				BashHelper.If(BashConditions.True(getVariable(COMPRESS_OUTPUT)))
				.Then(st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.tfa.gz")))
				.Else(st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.tfa")))
			);
		
		fastaconvtr.setBaseCommand("fastaconvtr");
		
		fastaconvtr.setTrackInput(inputVariable);
		
		fastaconvtr.addArgs(getOptionParamatersStrAs$());
		fastaconvtr.addArgs("-o ");
		fastaconvtr.addArgs(outputVariable.$());
		fastaconvtr.addArgs($(custom_order_variable));
		
		
		command_script.addStatment(fastaconvtr);

		command_script.generate(builder);
		// command need checker of output 
		super.generateCommand(builder);
	}

}
