package es.cragenomica.espiga.jobs;


import com.biotechvana.workflow.ISWFJob;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.descriptors.JobLink;
import com.biotechvana.workflow.descriptors.Publication;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class RecombinationSplineJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "recombination_spline";
	static public final String JOB_NAME = "RecombinationSpline";
	
	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	
	public RecombinationSplineJob() {
		super(JOB_ID, JOB_NAME, true,false);
		

		this.jobDesc.addLink(
				 new JobLink()
						.setTitle("RecombinationSpline")
						.setUrl("https://github.com/CRAGENOMICA/RecombinationSpline")
						.setType(JobLink.LinkType.Other)
				);
		this.jobDesc.setDesc("RecombinationSpline: this script calculates the\n"
				+ "recombination rates per position given Genetic and Physic\n"
				+ "maps:\n"
				+ "First, the genetic map was plotted against the physical map\n"
				+ "and incompatible values were discarded (that is, those\n"
				+ "points that in the cumulative genetic map were not equal or\n"
				+ "higher than the previous values). Cumulative recombination\n"
				+ "curve for each chromosome were estimated from the Genetic\n"
				+ "and physic maps using a monotonic cubic spline interpolation\n"
				+ "method (implemented in the standard library of R,\n"
				+ "http://www.r-project.org) using Hyman filtering (Hyman,\n"
				+ "1983) over windows of sizes 50Kb, 100Kb, 500Kb, 1Mb and 5Mb.\n"
				+ "The recombination value per position was obtained\n"
				+ "calculating the slope per window (that is, the derivative)");
	}

	@Override
	protected void createInputs() {
		
		
		jobDesc.addVariable(VariableDescriptor.Int("nchromosomes", VariableRole.Input)
				.setVariableName("#N Chromosomes")
				.setHelpMsg("#N Chromosomes"));
		jobDesc.addVariable(VariableDescriptor.File("chromosomes_len", VariableRole.Input)
				.setVariableName("Chromosomes length file")
				.setHelpMsg("file with the total length of each chromosome, per rows."));
		jobDesc.addVariable(VariableDescriptor.File("markers", VariableRole.Input)
				.setVariableName("Markers file")
				.setHelpMsg("file with the name of marker, number of chromosome, cM and Physical position (in bp)"));
		jobDesc.addVariable(VariableDescriptor.Strings("sizes", VariableRole.Input)
				.setVariableName("Windows' sizes")
				.setHelpMsg("size or sizes (in bp) of the windows."));
	
	}

	@Override
	protected void createOutputs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createOptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createRules() {
		// TODO Auto-generated method stub
		
	}
   // TODO :: validate at least two files.
}
