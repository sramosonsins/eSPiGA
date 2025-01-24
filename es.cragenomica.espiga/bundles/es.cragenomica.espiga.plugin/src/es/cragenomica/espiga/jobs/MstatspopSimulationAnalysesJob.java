package es.cragenomica.espiga.jobs;

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

public class MstatspopSimulationAnalysesJob extends MstatspopJob {

	static public final String JOB_ID = "mstatspop_sa";
	static public final String JOB_NAME = "mstatspop - Simulation Analyses";

	public MstatspopSimulationAnalysesJob() {
		super(JOB_ID, JOB_NAME, true, false);

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
		this.jobDesc.addLink(new JobLink().setTitle("mstatspop").setUrl("https://github.com/CRAGENOMICA/mstatspop")
				.setType(JobLink.LinkType.Other));

		this.jobDesc.setDesc("mstatspop: perfrom Simulation Analyses using ms inputs.");

	}

	@Override
	protected void createInputs() {
		createInputMs();
		super.createInputs();

//		  PARAMETERS FOR MS INPUT (-f ms):'SIMULATION ANALYSIS OF A SINGLE REGION'
//	      -l [length]
		jobDesc.addVariable(
				VariableDescriptor.Int(OPTION_AUTO, VariableRole.Input)
				.setMinValue(0)
				.setVariableName("Length")
				.setHelpMsg("Length")
				.setCommandParamater("-l")
				);

////	      -i [path and name of the input file]  
//		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES, VariableType.File,
//				VariableRole.Input);
//		varInputFile.isRequired = true;
////		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
////		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
////		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
//		varInputFile.setVariableName("Input file ");
//		varInputFile.setShortHelpMsg("Drag an input file [ms, fasta or tfa]");
//		varInputFile.setHelpMsg("input file: can be a ms, fasta or tfa (gz file indexed)");
//		varInputFile.setCommandParamater("-i");
//		jobDesc.addVariable(varInputFile);
//
////	      -f [input format file: ms, fasta OR tfa (gz file indexed)]  
//		VariableDescriptor inputFormatVar = VariableDescriptor.SelectionList(INPUT_FILES_FORMAT, VariableRole.Input)
//				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_TFA,eSPiGAWorkflowConstants.FILE_FORMAT_TFA,"SLIDING WINDOW ANALYSIS OF EMPIRICAL DATA")
//				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_MS,eSPiGAWorkflowConstants.FILE_FORMAT_MS,"SIMULATION ANALYSIS OF A SINGLE REGION")
//				.addOptionValue(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,eSPiGAWorkflowConstants.FILE_FORMAT_FASTA,"WHOLE REGION ANALYSIS")
//				.setDefaultIndexValue(1)
//				.setCommandParamater("-f")
//				.setVariableName("input format file")
//				.setIsRequired(true)
//				.setReadOnly(true);
//		jobDesc.addVariable(inputFormatVar);

//	      -o [output format file: 0 (extended),  
//        1 (single line/window),  
//        2 (single line SFS/window),  
//        3 (dadi-like format),  
//        4 (single line pairwise distribution)  
//        5 (single line freq. variant per line/window)  
//        6 (SNP genotype matrix)  
//        7 (SweepFiinder format -only first pop-)  
//       10 (full extended)]
//		VariableDescriptor outputFormatVar = VariableDescriptor.SelectionList(OUTPUT_FILES_FORMAT, VariableRole.Input)
//				.addOptionValue(OUTPUT_FILES_FORMAT_0, "0", OUTPUT_FILES_FORMAT_0)
//				.addOptionValue(OUTPUT_FILES_FORMAT_1, "1", OUTPUT_FILES_FORMAT_1)
//				.addOptionValue(OUTPUT_FILES_FORMAT_2, "2", OUTPUT_FILES_FORMAT_2)
//				.addOptionValue(OUTPUT_FILES_FORMAT_3, "3", OUTPUT_FILES_FORMAT_3)
//				.addOptionValue(OUTPUT_FILES_FORMAT_4, "4", OUTPUT_FILES_FORMAT_4)
//				.addOptionValue(OUTPUT_FILES_FORMAT_5, "5", OUTPUT_FILES_FORMAT_5)
//				.addOptionValue(OUTPUT_FILES_FORMAT_6, "6", OUTPUT_FILES_FORMAT_6)
//				.addOptionValue(OUTPUT_FILES_FORMAT_7, "7", OUTPUT_FILES_FORMAT_7)
//				.addOptionValue(OUTPUT_FILES_FORMAT_10, "10", OUTPUT_FILES_FORMAT_10).setDefaultIndexValue(0)
//				.setCommandParamater("-o").setVariableName("Output file format").setIsRequired(true);
//		jobDesc.addVariable(outputFormatVar);

//	    	      -N [#_pops] [#samples_pop1] ... [#samples_popN]
//		// TODO :: we need ints list
//		// TODO :: fix OPTION_AUTO with inputs
//		VariableDescriptor nPopsVar = VariableDescriptor.Int(INPUT_N_POPULATION, VariableRole.Input)
//				// .setCommandParamater("-N")
//				.setMinValue(1).setVariableName("#N Population").setIsRequired(true)
//				.setHelpMsg("Numner of Populations");
//		jobDesc.addVariable(nPopsVar);
//
//		jobDesc.addVariable(VariableDescriptor.Strings(INPUT_POPULATIONS_SIZE, VariableRole.Input)
//				// .setCommandParamater("-N")
//				.setVariableName("N Populations").setIsRequired(true).setHelpMsg("N Samples per populations"));
////	      -n [name of a single scaffold to analyze. For tfa can be a list separated by commas(ex. -n chr1,chr2,chr3]
//
//		VariableDescriptor scaffoldNamesVar = VariableDescriptor.Strings(INPUT_SCAFFOLD_NAMES, VariableRole.Input)
//				// .setCommandParamater("-N")
//				.setVariableName("scaffold to analyze")
//				.setHelpMsg("A single name of a single scaffold to analyze, or a list for tfa");
//		jobDesc.addVariable(scaffoldNamesVar);

//	    	      -T [path and name of the output file]. DEFAULT stdout.

	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES, VariableType.FileList,
				VariableRole.Output);
		varInputFile.isRequired = true;
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_TFA, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_MS, false, false);
//		varInputFile.addAcceptedFileFormat(eSPiGAWorkflowConstants.FILE_FORMAT_FASTA, false, false);
		varInputFile.setVariableName("Input file");
		varInputFile.setShortHelpMsg("Drag an input file [ms, fasta or tfa]");
		varInputFile.setHelpMsg("input file: can be a ms, fasta or tfa (gz file indexed)");
		varInputFile.setCommandParamater("-i");
		jobDesc.addVariable(varInputFile);

	}

	public static String OPTIONAL_CUSTOM_ORDER = "OPTIONAL_CUSTOM_ORDER";

	@Override
	protected void createOptions() {
		// TODO Fix duplicate input and option var for such cases
		VariableDescriptor varOptions;

//		  PARAMETERS FOR MS INPUT (-f ms):'SIMULATION ANALYSIS OF A SINGLE REGION'
//		    Optional:
//		      -r [# ms iterations]. DEFAULT 1.
		jobDesc.addVariable(VariableDescriptor.Int(OPTION_AUTO, VariableRole.Option)
				.setMinValue(1)
				.setVariableName("ms iterations")
				.setHelpMsg("#ms iterations")
				.setCommandParamater("-r")
				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS")
				);
//		      -m [include mask_filename] DEFAULT -1 (all positions included).
		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option).setVariableName("Mask File")
				.setHelpMsg("mask_file format: 1st row with 'length' weights, next sample rows x lengths: missing 0, sequenced 1")
				.setCommandParamater("-m")
				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));
//		         [mask_file format: 1st row with 'length' weights, next sample rows x lengths: missing 0, sequenced 1)].
//		         DEFAULT no mask.
//		      -v [ratio transitions/transversions]. DEFAULT 0.5.
		jobDesc.addVariable(VariableDescriptor.Double(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("ratio transitions/transversions")
				.setHelpMsg("ratio transitions/transversions. DEFAULT 0.5.").setCommandParamater("-v")
				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));
//		      -F [force analysis to include outgroup (0/1) (0 in ms means ancestral)]. DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
				.setCommandPostfix(CheckedVariableDescriptor.p_0_1)
				.setVariableName("force analysis to include outgroup")
				.setHelpMsg("force analysis to include outgroup (0/1) (0 in ms means ancestral). DEFAULT 0")
				.setCommandParamater("-F")
				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));
//		      -q [frequency of reverted mutation] (only with -F 1). DEFAULT 0.
		jobDesc.addVariable(VariableDescriptor.Double(OPTION_AUTO, VariableRole.Option).setMinValue(0)
				.setVariableName("frequency of reverted mutation")
				.setHelpMsg("frequency of reverted mutation (only with -F 1). DEFAULT 0.").setCommandParamater("-q")
				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));

		
		super.createOptions();
		
//		   PARAMETERS FOR FASTA INPUT (-f fasta): 'WHOLE REGION ANALYSIS'
//		    Optional:
//		      -p [Number of lineages per sequence (1/2)]. DEFAULT 1.
//		jobDesc.addVariable(VariableDescriptor.Range(OPTION_AUTO, VariableRole.Option).setMinValue(1).setMaxValue(2)
//				.setVariableName("Number of lineages per sequence ")
//				.setHelpMsg("Number of lineages per sequence (1/2). DEFAULT 1").setCommandParamater("-p")
//				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));
////		      -g [GFF_file]
////		         [add also: coding,noncoding,synonymous,nonsynonymous,silent, others (or whatever annotated)]
////		         [if 'synonymous', 'nonsynonymous', 'silent' add: Genetic_Code: Nuclear_Universal,mtDNA_Drosophila,mtDNA_Mammals,Other]
////		         [if 'Other', introduce the code for the 64 triplets in the order UUU UUC UUA UUG ... etc.].
////		         DEFAULT no annotation.
//		jobDesc.addVariable(VariableDescriptor.File(OPTION_AUTO, VariableRole.Option)
//				.setVariableName("GFF annotation file").setHelpMsg("GFF annotation file. DEFAULT no annotation.")
//				.setCommandParamater("-g").setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));
////		      -c [in case use coding regions, criteria to consider transcripts (max/min/first/long)]. DEFAULT long.
//		jobDesc.addVariable(VariableDescriptor.SelectionList(OPTION_AUTO, VariableRole.Option).addOptionValue("long")
//				.addOptionValue("first").addOptionValue("max").addOptionValue("min")
//				.setVariableName("Criteria to consider transcripts").setCommandParamater("-c")
//				.setVariableGroup("SIMULATION ANALYSIS PARAMETERS")
//				.setHelpMsg("in case use coding regions, criteria to consider transcripts"));
////		      -K [make a MASK file with the valid positions for this fasta. Useful for running ms simulations (1/0)]. DEFAULT 0.
//		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_AUTO, VariableRole.Option)
//				.setCommandPostfix(CheckedVariableDescriptor.p_0_1)
//				.setVariableName("make a MASK file with the valid positions")
//				.setHelpMsg(
//						"make a MASK file with the valid positions for this fasta. Useful for running ms simulations (1/0). DEFAULT 0")
//				.setCommandParamater("-K").setVariableGroup("SIMULATION ANALYSIS PARAMETERS"));

//		   OPTIONAL GENERAL PARAMETERS:
//	      -G [outgroup (0/1)] (last population). DEFAULT 0.
		
	}


	@Override
	protected void createRules() {
		super.createRules();
	}
	@Override
	protected void generateCommand(StringBuilder builder) {
		BashBlock command_script = new BashBlock();

		String population_sizes_variable = "N_POP";
		
		command_script.addStatment(						
				st(BashHelper.assign(population_sizes_variable,"-N " + getVariable(INPUT_N_POPULATION).$() + " " + getVariable(INPUT_POPULATIONS_SIZE).get_cmdValue() ))
				);
		JobVariable inputVariable = getVariable(INPUT_FILES);
		JobVariable outputVariable = getVariable(OUTPUT_FILES);
		
		
		GeneralCMLTemplate mstatspop = new GeneralCMLTemplate();
		mstatspop.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				));
		
		mstatspop.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.txt"))
			);
		mstatspop.setBaseCommand("mstatspop");
		
		mstatspop.setTrackInput(inputVariable);
		
		mstatspop.addArgs(getOptionParamatersStrAs$());
		mstatspop.addArgs($(population_sizes_variable));
		mstatspop.addArgs("-T");
		mstatspop.addArgs(outputVariable.$());
		
		
		command_script.addStatment(mstatspop);
		command_script.generate(builder);
	}
}
