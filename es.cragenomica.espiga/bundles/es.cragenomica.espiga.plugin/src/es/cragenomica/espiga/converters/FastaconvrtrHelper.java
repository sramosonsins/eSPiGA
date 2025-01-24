package es.cragenomica.espiga.converters;

import com.biotechvana.workflow.CmdListMergeStrategy;
import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.VariableCustomAction;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.descriptors.CheckedVariableDescriptor;
import com.biotechvana.workflow.descriptors.FileVariableDescriptor;
import com.biotechvana.workflow.descriptors.JobDescriptor;
import com.biotechvana.workflow.descriptors.SelectionListVariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class FastaconvrtrHelper {

	public static VariableDescriptor getInputFile() {
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(eSPiGAVariableBag.INPUT_FILES,VariableType.File,VariableRole.Input);
		varInputFile.isRequired = true;
		
		varInputFile.setVariableName("Input files");
		varInputFile.setShortHelpMsg("Drag an input files");
		varInputFile.setHelpMsg("the input file (text or gz indexed)");
		varInputFile.setCommandParamater("-i");
		return varInputFile;
	}
	public static FileVariableDescriptor getInputScaffoldInfoFile() 
	{
		return  (FileVariableDescriptor) VariableDescriptor.File(WorkflowJob.OPTION_AUTO, VariableRole.Input)
				.setVariableName("scaffold(s) names/lengths file")
				.setCommandParamater("-n")
				.setHelpMsg("file containing the name(s) of scaffold(s) and their length (separated by a tab), one per line (ex. fai file)");

	}
	
	
	public static SelectionListVariableDescriptor getInputFileFormatList() {
		return getInputFileFormatList(VariableRole.Input);
	}

	
	/**
	 * 0 > t > tfasta
	 * 1 > f > fasta
	 * @return
	 */
	public static SelectionListVariableDescriptor getInputFileFormatList(VariableRole variableRole) {
		return (SelectionListVariableDescriptor) VariableDescriptor.SelectionList(eSPiGAVariableBag.INPUT_FILES_FORMAT, variableRole)
		.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFASTA,"t","tFasta format")
		.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,"f","Fasta format")
		.setDefaultIndexValue(0)
		.setIsRequired(true)
		.setCommandParamater("-F").setVariableName("input file format").setIsRequired(true);
	}
	
	
	public static SelectionListVariableDescriptor getOutputFileFormatList() { 
		return getOutputFileFormatList(VariableRole.Input); 
	}
	
	/**
	 * 0 > t > tfasta
	 * 1 > f > fasta
	 * 2 > m > ms
	 * 3 > 0 > nothing
	 * @return
	 */
	public static SelectionListVariableDescriptor getOutputFileFormatList(VariableRole variableRole) {
		return (SelectionListVariableDescriptor) VariableDescriptor.SelectionList(eSPiGAVariableBag.OUTPUT_FILES_FORMAT, variableRole)
		.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFASTA,"t","tfasta")
		.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,"f","fasta")
		.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS,"m","ms")
		.addOptionValue("nothing","0","nothing")
		.setDefaultIndexValue(0)
		.setIsRequired(true)
		.setCommandParamater("-f").setVariableName("Output file format").setIsRequired(true);
	}
	public static void addSharedOptions(ISWFJob converterJob) {
//		  -P [define window lengths in 'physical' positions (1) or in 'effective' positions (0)]. DEFAULT: 1
		converterJob.getJobDesc().
		addVariable(VariableDescriptor.SelectionList(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.addOptionValue("physical", "1", "define window lengths in physical positions")
				.addOptionValue("effective", "0", "define window lengths in effective positions")
				//.setDefaultIndexValue(0)
				.setCommandParamater("-P").setVariableName("Window Lengths Definition"));
		
		
		// TODO :: fix
//	      -O [#_nsam] [Reorder samples: number order of first sample, number 0 is the first sample] [second sample] ...etc.
		converterJob.getJobDesc().addVariable(VariableDescriptor.Checked(eSPiGAVariableBag.OPTIONAL_CUSTOM_ORDER, VariableRole.Option)
				.setVariableName("Provide Custom samples order"));
		converterJob.getJobDesc().addVariable(
				VariableDescriptor.Int(eSPiGAVariableBag.OPTIONAL_N_SAMPLES, VariableRole.Option)
				.setVariableName("Number of Samples")
				);
		converterJob.getJobDesc().addVariable(
				VariableDescriptor.Strings(eSPiGAVariableBag.OPTIONAL_SAMPLES_OEDER, VariableRole.Option)
				.setCmdListMergerSep(" ")
				.setCmdListMergeStrategy(CmdListMergeStrategy.Repeat)
				.setVariableName("Samples order")
				//.setCommandParamater("-O")
				.setHelpMsg("Samples custom Order. Number 0 is the first sample")
				.addCustomAction(new VariableCustomAction("Reset", VariableCustomAction.Button, 
						new VariableCustomAction.CustomActionCallback() {

							@Override
							public void call(JobVariable var) {
								WorkflowJob workflowJob = var.getJob();
								String value = workflowJob.getValue(eSPiGAVariableBag.OPTIONAL_N_SAMPLES);
								if(value.isEmpty()) {
									var.setValue("");
								} else {
									int n_samples = Integer.parseInt(value);
									String samples_ids = "";
									for(int i=0;i<n_samples;i++ ) {
										samples_ids+=i+ JobVariable.LISTITEM_VALUE_EOL;
									}
									var.setValue( samples_ids);
								}
							}
					
				}))
				);
		
//	      -W [for ms and fasta outputs, file with the coordinates of each window: (one header plus nlines with init end]
		// TODO :: add
//	      -N [#_pops] [#samples_pop1] ... [#samples_popN] (necessary in case to indicate the outgroup population)
		// TODO :: add
//	      -G [outgroup included (1) or not (0), last population (1/0)]. DEFAULT: 0
		converterJob.getJobDesc().
		addVariable(VariableDescriptor.Checked(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("outgroup [last population]")
				.setHelpMsg("outgroup is included (1) or not (0), last population (1/0). DEFAULT 0")
				.setCommandParamater("-G")
				);
		
//	      -u [Missing counted (1) or not (0) in weights given GFF annotation]. DEFAULT: 0
		converterJob.getJobDesc().
		addVariable(VariableDescriptor.Checked(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1).setVariableName("Missing counted")
				.setHelpMsg("Missing counted (1) or not (0) in weights given GFF annotation. DEFAULT 0")
				.setCommandParamater("-u")
				);
//	      -m [masking regions: file indicating the start and the end of regions to be masked by Ns] 
		converterJob.getJobDesc().addVariable(VariableDescriptor.File(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setVariableName("Masking regions file")
				.setCommandParamater("-m")
				.setHelpMsg("masking regions: file indicating the start and the end of regions to be masked by Ns."));
	}
	public static void addSharedOptions2(JobDescriptor jobDesc) {
		jobDesc.addVariable(VariableDescriptor.File(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setVariableName("GFF annotation file")
				.setHelpMsg("GFF annotation file. Add also: coding,noncoding,synonymous,nonsynonymous,silent, others (whatever annotated)\n"
						+ "if 'synonymous', 'nonsynonymous', 'silent' add: Genetic_Code: Nuclear_Universal,mtDNA_Drosophila,mtDNA_Mammals,Other\n"
						+ "if 'Other', introduce the single letter code for the 64 triplets in the order UUU UUC UUA UUG ... etc.")
				.setCommandParamater("-g").setVariableGroup("Annotation file and weight options"));
		jobDesc.addVariable(VariableDescriptor.SelectionList(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.addOptionValue("long")
				.addOptionValue("first")
				.addOptionValue("max")
				.addOptionValue("min")
				//.setDefaultIndexValue(0)
				.setVariableName("Criteria to consider transcripts")
				.setCommandParamater("-c")
				.setHelpMsg("in case use coding regions, criteria to consider transcripts. DEFAULT: long")
				.setVariableGroup("Annotation file and weight options"));
		jobDesc.addVariable(VariableDescriptor.File(WorkflowJob.OPTION_AUTO, VariableRole.Option)
				.setVariableName("input file with weights for positions")
				.setHelpMsg("instead -g & -c, input file with weights for positions: include three columns with a header, first the physical positions (1...end), second the weight for positions and third a boolean weight for the variant (eg. syn variant but nsyn position)")
				.setCommandParamater("-E")
				.setVariableGroup("Annotation file and weight options"));
	}

}
