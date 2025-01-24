package es.cragenomica.espiga.converters;


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

public class TfastaMsConverterJob  extends FastaConvtr { 

	


	
	static public final String JOB_ID = "tfasta_to_ms";
	static public final String JOB_NAME = "tFasta to ms Converter";
	
	
	
	public TfastaMsConverterJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("fastaconvtr")
						.setUrl("https://github.com/CRAGENOMICA/fastaconvtr")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("fastaconvtr: Convert tFasta to ms format.");
	}

	@Override
	protected void createInputs() {
		SelectionListVariableDescriptor inputFileFormat = FastaconvrtrHelper.getInputFileFormatList(VariableRole.Internal);
		inputFileFormat.setDefaultIndexValue(0);
		inputFileFormat.setReadOnly(true);
		this.jobDesc.addVariable(inputFileFormat);

		
		
		SelectionListVariableDescriptor outputFileFormat = FastaconvrtrHelper.getOutputFileFormatList(VariableRole.Internal);
		outputFileFormat.setDefaultIndexValue(2);
		outputFileFormat.setReadOnly(true);
		this.jobDesc.addVariable(outputFileFormat);
		
		
		VariableDescriptor inVar = FastaconvrtrHelper.getInputFile();
		inVar.setVariableName("Input fFasta file");
		this.jobDesc.addVariable(inVar);
		
		this.jobDesc.addVariable(FastaconvrtrHelper.getInputScaffoldInfoFile());
//	      -w [window size]. DEFAULT: Total_length
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setVariableName("window size")
				.setCommandParamater("-w")
				.setHelpMsg("window size. DEFAULT: Total_length")
				.setVariableGroup("Output ms format"));
//	      -s [slide size]. DEFAULT: Total_length
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setVariableName("slide size")
				.setCommandParamater("-s")
				.setHelpMsg("slide size. DEFAULT: Total_length")
				.setVariableGroup("Output ms format"));
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
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.ms"))
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
