package es.cragenomica.espiga.converters;


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

public class msGenotypeConverterJob  extends ISWFJob{ 

	

	static public final String INPUT_CHROM_LENGTH = "INPUT_CHROM_LENGTH";
	static public final String INPUT_NSAM = "INPUT_NSAM";
	static public final String INPUT_NITERATIONS = "INPUT_NITERATIONS";

	static public final String JOB_ID = "ms_to_genotype";
	static public final String JOB_NAME = "ms to genotype Converter";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;
	public msGenotypeConverterJob() {
		super(JOB_ID, JOB_NAME, true,false);
		
		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("ms2geno")
						.setUrl("https://github.com/CRAGENOMICA/ms2geno")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("ms2geno: converting ms files into genotype files");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.Files,VariableRole.Input);
		varInputFile.isRequired = true;
		
		varInputFile.setVariableName("Input ms files");
		varInputFile.setShortHelpMsg("Drag an input ms files");
		varInputFile.setHelpMsg("Input ms files to convert");
		jobDesc.addVariable(varInputFile);
	}

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
		// TODO Auto-generated method stub
		jobDesc.addVariable(VariableDescriptor.Int(INPUT_CHROM_LENGTH, VariableRole.Option)
				.setVariableName("Chromosome length")
				.setHelpMsg("Chromosomelength")
				);
		jobDesc.addVariable(VariableDescriptor.Int(INPUT_NSAM, VariableRole.Option)
				.setVariableName("#N of samples")
				.setHelpMsg("Number of samples")
				);
		jobDesc.addVariable(VariableDescriptor.Int(INPUT_NITERATIONS, VariableRole.Option)
				.setVariableName("#N of iterations")
				.setHelpMsg("Number of iterations")
				);
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
		
		
		GeneralCMLTemplate ms2geno = new GeneralCMLTemplate();
		ms2geno.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				));
		ms2geno.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.geno.txt"))
			);
	
		ms2geno.setBaseCommand("ms2geno");
		ms2geno.setTrackInput(inputVariable);
		ms2geno.addArgs(getVariable(INPUT_CHROM_LENGTH).$());
		ms2geno.addArgs(getVariable(INPUT_NSAM).$());
		ms2geno.addArgs(getVariable(INPUT_NITERATIONS).$());
		ms2geno.addArgs(inputVariable.$());
		ms2geno.setStdForward(outputVariable.$());

		command_script.addStatment(ms2geno);
		command_script.generate(builder);
	}

}
