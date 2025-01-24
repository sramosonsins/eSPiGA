package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.executer.BashBlock;
import com.biotechvana.workflow.executer.BashConditions;
import com.biotechvana.workflow.executer.BashHelper;
import com.biotechvana.workflow.executer.GeneralCMLTemplate;
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;

public class CreateWeightsFileJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "create_weight_file";
	static public final String JOB_NAME = "Create Weight File";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	
	public static String INPUT_GFF_FILE = eSPiGAVariableBag.INPUT_GFF_FILE;
	public static String INPUT_GFF_TYPE = eSPiGAVariableBag.INPUT_GFF_TYPE;
	public static String INPUT_GENETIC_CODE = eSPiGAVariableBag.INPUT_GENETIC_CODE;
	public static String INPUT_CUSTOM_GENETIC_CODE = "INPUT_CUSTOM_GENETIC_CODE";

	public CreateWeightsFileJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
		
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("weight4tfa")
						.setUrl("https://github.com/CRAGENOMICA/weight4tfa")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("weight4tfa: Calculates weights for tfasta files.");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.File,VariableRole.Input);
		varInputFile.isRequired = true;
		
		varInputFile.setVariableName("Input tfa files");
		varInputFile.setShortHelpMsg("Drag an input tfa files");
		varInputFile.setHelpMsg("input tfa file: must be (.gz and indexed)");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);
		
//	      -g [path of the GFF_file]
//        [add also: coding,noncoding,synonymous,nonsynonymous,silent, others (whatever annotated)]
//        [if 'synonymous', 'nonsynonymous', 'silent' add: Genetic_Code: Nuclear_Universal,mtDNA_Drosophila,mtDNA_Mammals,Other]
//        [if 'Other', introduce the single letter code for the 64 triplets in the order UUU UUC UUA UUG ... etc.]

		jobDesc.addVariable(VariableDescriptor.File(INPUT_GFF_FILE, VariableRole.Input)
				.setVariableName("GFF annotation file")
				.setHelpMsg("GFF annotation file -g option. DEFAULT no annotation.")
				.setIsRequired(true)
				// .setCommandParamater("-g")
				);
		
		jobDesc.addVariable(VariableDescriptor.SelectionList(INPUT_GFF_TYPE, VariableRole.Input)
				.addOptionValue(eSPiGAVariableBag.OPTION_GFF_TYPE_CODING)
				.addOptionValue(eSPiGAVariableBag.OPTION_GFF_TYPE_NONCODING)
				.addOptionValue(eSPiGAVariableBag.OPTION_GFF_TYPE_SYNONYMOUS)
				.addOptionValue(eSPiGAVariableBag.OPTION_GFF_TYPE_NONSYNONYMOUS)
				.addOptionValue(eSPiGAVariableBag.OPTION_GFF_TYPE_SILENT)
				.setVariableName("Annotation type in GTF")
				.setHelpMsg("Annotation type in -g option.")
				.setIsRequired(true)
				// .setCommandParamater("-g")
				);
		jobDesc.addVariable(VariableDescriptor.SelectionList(INPUT_GENETIC_CODE, VariableRole.Input)
				.addOptionValue(eSPiGAVariableBag.OPTION_GENETIC_CODE_NUCLEAR_UNIVERSAL)
				.addOptionValue(eSPiGAVariableBag.OPTION_GENETIC_CODE_MTDNA_DROSOPHILA)
				.addOptionValue(eSPiGAVariableBag.OPTION_GENETIC_CODE_MTDNA_MAMMALS)
//				.addOptionValue(eSPiGAVariableBag.OPTION_GENETIC_CODE_OTHER)
				.setVariableName("Genetic Code")
				.setHelpMsg("Genetic Code in case Annotation type is 'synonymous', 'nonsynonymous' or 'silent'.")
//				.setIsRequired(true)

				// .setCommandParamater("-g")
				);
		
//		jobDesc.addVariable(VariableDescriptor.String(INPUT_CUSTOM_GENETIC_CODE, VariableRole.Input)
//				.setVariableName("Custom Genetic Code")
//				.setHelpMsg("Introduce the single letter code for the 64 triplets in the order UUU UUC UUA UUG ... etc.")
//				);
		
//	      -n [name of the file containing the name(s) of scaffold(s) and their length (separated by a tab), one per line (ex. fai file)]

		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Input)
				.setVariableName("scaffold(s) names/lengths file")
				.setCommandParamater("-n")
				.setIsRequired(true)
				.setHelpMsg("file containing the name(s) of scaffold(s) and their length (separated by a tab), one per line (ex. fai file)"));

	}

//	Flags:
//	      -i [path and name of the tfa file (gz file indexed)]
//	      -o [path and name of the output weighted file (.gz extension will be included)]

	@Override
	protected void createOutputs() {
		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES,VariableType.File,VariableRole.Output);
		varOutputFile.isRequired = true;
		varOutputFile.setVariableName("Output File");
		varOutputFile.setCommandParamater("-o");
		this.jobDesc.addVariable(varOutputFile);
		
	}

	@Override
	protected void createOptions() {
//		   OPTIONAL PARAMETERS:
//	      -h [help and exit]
//	      -c [in case use coding regions, criteria to consider transcripts (max/min/first/long)]. DEFAULT: long
		jobDesc.addVariable(VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option)
				.addOptionValue("long")
				.addOptionValue("first")
				.addOptionValue("max")
				.addOptionValue("min")
				.setVariableName("Criteria to consider transcripts")
				.setCommandParamater("-c")
				.setHelpMsg("in case use coding regions, criteria to consider transcripts"));
//	      -m [masking regions: file indicating the start and the end of regions to be masked by 0 weights]. DEFAULT: NONE
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("Masking regions file")
				.setCommandParamater("-m")
				.setHelpMsg("masking regions: file indicating the start and the end of regions to be masked by 0 weights."));
//	      -G [number of samples in the outgroup (if exist. Only allowed the last samples in the list)]. DEFAULT: 0
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setVariableName("number of samples in the outgroup")
				.setCommandParamater("-G")
				.setHelpMsg("Number of samples in the outgroup (if exist. Only allowed the last samples in the list). DEFAULT: 0"));
//	      -C [coordinates of regions: file indicating the start and the end of regions to be weighted (rest would be weighted as 0 if the file is included)]. DEFAULT: NONE
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
				.setVariableName("Regions' coordinates file")
				.setCommandParamater("-C")
				.setHelpMsg("Coordinates of regions: file indicating the start and the end of regions to be weighted (rest would be weighted as 0 if the file is included)]. DEFAULT: NONE"));

	}

	@Override
	public boolean notifyVarValueChanged(String varKey, String value) {
	
		if(varKey.equals(INPUT_GFF_TYPE)) {
			if (value.equals(eSPiGAVariableBag.OPTION_GFF_TYPE_SYNONYMOUS) ||
					value.equals(eSPiGAVariableBag.OPTION_GFF_TYPE_NONSYNONYMOUS) ||
					value.equals(eSPiGAVariableBag.OPTION_GFF_TYPE_SILENT) 
					) {
				getVariable(INPUT_GENETIC_CODE).setRequired(true);
			}
			else {
				getVariable(INPUT_GENETIC_CODE).setRequired(false);

			}
		}
		
		return super.notifyVarValueChanged(varKey, value);
	}
	
	@Override
	protected void createRules() {
		
//		new VariablesRule(getVariable(INPUT_GFF_TYPE), getVariable(INPUT_GENETIC_CODE), Condition.EqualTo,
//				Action.Enable, eSPiGAVariableBag.OPTION_GFF_TYPE_SYNONYMOUS,  true);
//		new VariablesRule(getVariable(INPUT_GFF_TYPE), getVariable(INPUT_GENETIC_CODE), Condition.EqualTo,
//				Action.Enable, eSPiGAVariableBag.OPTION_GFF_TYPE_NONSYNONYMOUS,  true);
//		new VariablesRule(getVariable(INPUT_GFF_TYPE), getVariable(INPUT_GENETIC_CODE), Condition.EqualTo,
//				Action.Enable, eSPiGAVariableBag.OPTION_GFF_TYPE_SILENT,  true);
//		
//		new VariablesRule(getVariable(INPUT_GFF_TYPE), getVariable(INPUT_GENETIC_CODE), Condition.EqualTo,
//				Action.SetToRequired, eSPiGAVariableBag.OPTION_GFF_TYPE_SYNONYMOUS,  false);
//		new VariablesRule(getVariable(INPUT_GFF_TYPE), getVariable(INPUT_GENETIC_CODE), Condition.EqualTo,
//				Action.SetToRequired, eSPiGAVariableBag.OPTION_GFF_TYPE_NONSYNONYMOUS,  false);
//		new VariablesRule(getVariable(INPUT_GFF_TYPE), getVariable(INPUT_GENETIC_CODE), Condition.EqualTo,
//				Action.SetToRequired, eSPiGAVariableBag.OPTION_GFF_TYPE_SILENT,  false);
	}
	@Override
	protected void generateCommand(StringBuilder builder) {
		// Handle custom variables
		BashBlock command_script = new BashBlock();
		
		String gff_input_variable  = "gff_input_variable";
	
		
		
		
		JobVariable inputVariable = getVariable(INPUT_FILES);
		JobVariable outputVariable = getVariable(OUTPUT_FILES);
		
		
		GeneralCMLTemplate weight4tfa = new GeneralCMLTemplate();
		
		
		weight4tfa.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				));
		weight4tfa.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.tfa_weights.gz"))
			);
		weight4tfa.getBefore().addStatment(st(
				BashHelper.assign("outputFileName","${outputFolder}/${outputFileName}.tfa_weights")
				));
		
		
		weight4tfa.getBefore().addStatment(st(st(BashHelper.assign(gff_input_variable,
						"-g " + getVariable(INPUT_GFF_FILE).$() + " " + eSPiGAVariableBag.OPTION_GFF_TYPE_SYNONYMOUS))));

				weight4tfa.getBefore()
						.addStatment(BashHelper
								.If(BashConditions.Equal(getVariable(INPUT_GFF_TYPE),
										eSPiGAVariableBag.OPTION_GFF_TYPE_SYNONYMOUS))
								.Then(st(BashHelper.assign(gff_input_variable,
										$(gff_input_variable) + " " + getVariable(INPUT_GENETIC_CODE).$()))

								).Else(

										BashHelper
												.If(BashConditions.Equal(getVariable(INPUT_GFF_TYPE),
														eSPiGAVariableBag.OPTION_GFF_TYPE_NONSYNONYMOUS))
												.Then(st(BashHelper.assign(gff_input_variable,
														$(gff_input_variable) + " "
																+ getVariable(INPUT_GENETIC_CODE).$())))
												.Else(BashHelper
														.If(BashConditions.Equal(getVariable(INPUT_GFF_TYPE),
																eSPiGAVariableBag.OPTION_GFF_TYPE_SILENT))
														.Then(st(BashHelper.assign(gff_input_variable,
																$(gff_input_variable) + " "
																		+ getVariable(INPUT_GENETIC_CODE).$())))
														.toString()

												).toString()

								));
		
		weight4tfa.setBaseCommand("weight4tfa");
		
		weight4tfa.setTrackInput(inputVariable);
		weight4tfa.addArgs("-o ");
		weight4tfa.addArgs($("outputFileName"));
		
		weight4tfa.addArgs(getOptionParamatersStrAs$());
		weight4tfa.addArgs($(gff_input_variable));
		
		
		command_script.addStatment(weight4tfa);

		command_script.generate(builder);
		// command need checker of output 
		super.generateCommand(builder);
	}

}
