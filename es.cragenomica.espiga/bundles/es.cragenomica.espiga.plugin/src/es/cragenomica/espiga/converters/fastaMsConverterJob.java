package es.cragenomica.espiga.converters;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.SelectionListVariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class fastaMsConverterJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "fasta_to_ms";
	static public final String JOB_NAME = "fasta to ms Converter";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public fastaMsConverterJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("fastaconvtr")
						.setUrl("https://github.com/CRAGENOMICA/fastaconvtr")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("fastaconvtr: Convert Fasta to ms format.");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor inVar = FastaconvrtrHelper.getInputFile();
		inVar.setVariableName("Input Fasta file");
		this.jobDesc.addVariable(inVar);
		SelectionListVariableDescriptor inputFileFormat = FastaconvrtrHelper.getInputFileFormatList();
		this.jobDesc.addVariable(inputFileFormat);
		inputFileFormat.setDefaultIndexValue(1);
		inputFileFormat.setReadOnly(true);
		
		
		
		SelectionListVariableDescriptor outputFileFormat = FastaconvrtrHelper.getOutputFileFormatList();
		this.jobDesc.addVariable(outputFileFormat);
		outputFileFormat.setDefaultIndexValue(2);
		outputFileFormat.setReadOnly(true);
		this.jobDesc.addVariable(FastaconvrtrHelper.getInputScaffoldInfoFile());
	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createOptions() {
//	      -w [window size]. DEFAULT: Total_length
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("window size")
				.setCommandParamater("-w")
				.setHelpMsg("window size. DEFAULT: Total_length")
				.setVariableGroup("output ms format"));
//	      -s [slide size]. DEFAULT: Total_length
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("slide size")
				.setCommandParamater("-s")
				.setHelpMsg("slide size. DEFAULT: Total_length")
				.setVariableGroup("output ms format"));
	      
	      FastaconvrtrHelper.addSharedOptions(this);
		FastaconvrtrHelper.addSharedOptions2(this.jobDesc);	
		
	}

	@Override
	protected void createRules() {
		// TODO Auto-generated method stub
		
	}

}
