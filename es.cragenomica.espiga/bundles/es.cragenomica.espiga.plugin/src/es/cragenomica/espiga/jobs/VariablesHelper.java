package es.cragenomica.espiga.jobs;

import java.util.HashMap;

import com.biotechvana.workflow.Tracking;
import com.biotechvana.workflow.WorkflowConstants;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.descriptors.JobDescriptor;
import com.biotechvana.workflow.descriptors.SelectionListVariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableRole;
import com.biotechvana.workflow.descriptors.VariableDescriptor.VariableType;
import com.biotechvana.workflow.executer.BashHelper;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;

public class VariablesHelper {


	public static final String R_SCRIPT_COMMON_OPTIONAL = "Optional";
	
	public static final String R_SCRIPT_COMMON_INPUT_OPTIONAL = "Input Files Arguments";

	public static final String R_SCRIPT_COMMON_OUTPUT_OPTIONAL = "Output Files Arguments";

	
	
	
	/**
	 * Create an input var for csv files. 
	 * <br> Key : <b>STAToolsVariableBag.INPUT_CSV_FILES</b>
	 * @return
	 */
	public static VariableDescriptor createInputCSVFilesVar()
	{
		VariableDescriptor varReadInputFile = VariableDescriptor.createDescriptorFor(eSPiGAVariableBag.INPUT_CSV_FILES,VariableType.Files,VariableRole.Input);
		varReadInputFile.isRequired = true;
		varReadInputFile.setVariableName("Input CSV/TSV Files");
		varReadInputFile.setHelpMsg("Input CSV/TSV Files.");
		varReadInputFile.addAcceptedFileFormat(WorkflowConstants.FILE_FORMAT_CSV);
		varReadInputFile.addAcceptedFileFormat(WorkflowConstants.FILE_FORMAT_TSV);
		varReadInputFile.addAcceptedFileFormat(WorkflowConstants.FILE_FORMAT_TXT);


		return varReadInputFile;
	}
	

	/**
	 * Create an input var for csv file. 
	 * <br> Key : <b>STAToolsVariableBag.INPUT_CSV_FILE</b>
	 * @return
	 */
	public static VariableDescriptor createInputCSVFileVar()
	{
		VariableDescriptor varReadInputFile = VariableDescriptor.createDescriptorFor(eSPiGAVariableBag.INPUT_CSV_FILE,VariableType.File,VariableRole.Input);
		varReadInputFile.isRequired = true;
		varReadInputFile.setVariableName("Input CSV/TSV File");
		varReadInputFile.setHelpMsg("Input CSV/TSV File.");
		varReadInputFile.addAcceptedFileFormat(WorkflowConstants.FILE_FORMAT_CSV);
		varReadInputFile.addAcceptedFileFormat(WorkflowConstants.FILE_FORMAT_TSV);
		varReadInputFile.addAcceptedFileFormat(WorkflowConstants.FILE_FORMAT_TXT);
		return varReadInputFile;
	}

	
	public static void createSharedOptions(JobDescriptor jobDesc, String  key , String sharedVarGroup) {
		createSharedOptions(jobDesc, new String[]{key} ,  sharedVarGroup);
	}
	
	public static void createAllOptional(JobDescriptor jobDesc) {
		createSharedInputArgsOptions(jobDesc);
		createSharedOutputArsOptions(jobDesc);
	}
	
	public static void createSharedInputArgsOptions(JobDescriptor jobDesc) {
		
		String[] keys = new String []{ 
			R_SCRIPT_OPTION_IN_CSV_SEP,
			R_SCRIPT_OPTION_IN_CSV_DEC,
			R_SCRIPT_OPTION_IN_CSV_NO_HEADER,
			
		};
		createSharedOptions(jobDesc,keys ,  R_SCRIPT_COMMON_INPUT_OPTIONAL);
	}
	public static void createSharedOutputArsOptions(JobDescriptor jobDesc) {
		String[] keys = new String []{ 
				R_SCRIPT_OPTION_OUT_CSV_SEP,
				R_SCRIPT_OPTION_OUT_IMAGE_WIDTH,
				R_SCRIPT_OPTION_OUT_IMAGE_HEIGHT,
				R_SCRIPT_OPTION_OUT_IMAGE_RESOLUTION,
				R_SCRIPT_OPTION_OUT_FONT_POINTSIZE,

			};
			createSharedOptions(jobDesc,keys ,  R_SCRIPT_COMMON_OUTPUT_OPTIONAL);
	}
	
	
	
	public static void createSharedOptions(JobDescriptor jobDesc, String[]  keys ,  String sharedVarGroup) {
		VariableDescriptor optionalVar = null;

		for( String key : keys)
		{
			optionalVar = null;
			
			if(optionToAction.containsKey(key)){ 
				optionalVar = optionToAction.get(key).createOption();
				optionalVar.setVariableGroup(sharedVarGroup);
			}
			
			if(optionalVar!= null)
			{
				jobDesc.addVariable(optionalVar);
			}
			else
			{
				// FIXME :: TODO :: what is key is not listed
			}

		}
	
	
		
	}
	public static void addRules(WorkflowJob job) {
		
		for(String key : optionToAction.keySet()) {
			if(job.getVariableByCMDSwitch(key) != null) {
				optionToAction.get(key).addRules(job);
			}
		}
		
	}
	
	
	
	
	
	// ###################### Shared common optional flags
	
	interface CreateOptionCallBack 
	{
		VariableDescriptor createOption();
		void addRules(WorkflowJob job);
	}
	static abstract class  NoRulesCreateOptionCallBack implements  CreateOptionCallBack {

		

		@Override
		public void addRules(WorkflowJob job) {
			// TODO Auto-generated method stub
			
		}
		
	}
	static private HashMap<String, CreateOptionCallBack> optionToAction = new HashMap<String, VariablesHelper.CreateOptionCallBack>();
	
	/* ########################################################################################################## */

	public static String R_SCRIPT_OPTION_OUT_IMAGE_WIDTH = "--img-width";
	static {
		optionToAction.put(R_SCRIPT_OPTION_OUT_IMAGE_WIDTH,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionOutputImageWidth();
			}
		});
	}

	public static VariableDescriptor createOptionOutputImageWidth()
	{
		VariableDescriptor optionalVar = VariableDescriptor.createDescriptorFor(WorkflowJob.OPTION_AUTO,VariableType.Int,VariableRole.Option);
		optionalVar.setVariableName("Image width");
		optionalVar.setHelpMsg("Set the output image width.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_OUT_IMAGE_WIDTH);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		optionalVar.setDefaultValue("2500");
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 

	
	/* ########################################################################################################## */

	public static String R_SCRIPT_OPTION_OUT_IMAGE_HEIGHT = "--img-height";
	static {
		optionToAction.put(R_SCRIPT_OPTION_OUT_IMAGE_HEIGHT,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionOutputImageHeight();
			}
		});
	}

	public static VariableDescriptor createOptionOutputImageHeight()
	{
		VariableDescriptor optionalVar = VariableDescriptor.createDescriptorFor(WorkflowJob.OPTION_AUTO,VariableType.Int,VariableRole.Option);
		optionalVar.setVariableName("Image height");
		optionalVar.setHelpMsg("Set the output image height.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_OUT_IMAGE_HEIGHT);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		optionalVar.setDefaultValue("2500");
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 

	
	/* ########################################################################################################## */

	public static String R_SCRIPT_OPTION_OUT_IMAGE_RESOLUTION = "--img-resolution";
	static {
		optionToAction.put(R_SCRIPT_OPTION_OUT_IMAGE_RESOLUTION,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionOutputImageResolution();
			}
		});
	}

	public static VariableDescriptor createOptionOutputImageResolution()
	{
		VariableDescriptor optionalVar = VariableDescriptor.createDescriptorFor(WorkflowJob.OPTION_AUTO,VariableType.Int,VariableRole.Option);
		optionalVar.setVariableName("Image resolution");
		optionalVar.setHelpMsg("Set the output image resolution.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_OUT_IMAGE_RESOLUTION);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		optionalVar.setDefaultValue("300");
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 

	
	
	
	/* ########################################################################################################## */

	public static String R_SCRIPT_OPTION_OUT_FONT_POINTSIZE = "--font-pointsize";
	static {
		optionToAction.put(R_SCRIPT_OPTION_OUT_FONT_POINTSIZE,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionOutputFontPointSize();
			}
		});
	}

	public static VariableDescriptor createOptionOutputFontPointSize()
	{
		VariableDescriptor optionalVar = VariableDescriptor.createDescriptorFor(WorkflowJob.OPTION_AUTO,VariableType.Int,VariableRole.Option);
		optionalVar.setVariableName("Imgae pointsize");
		optionalVar.setHelpMsg("Set the output image pointsize.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_OUT_FONT_POINTSIZE);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		optionalVar.setDefaultValue("8");
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 

	/* ########################################################################################################## */

	public static String R_SCRIPT_OPTION_OUT_CSV_SEP = "--out-csvSep";
	static {
		optionToAction.put(R_SCRIPT_OPTION_OUT_CSV_SEP,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionOutputCSVSeparator();
			}
		});
	}

	public static VariableDescriptor createOptionOutputCSVSeparator()
	{
		SelectionListVariableDescriptor optionalVar = VariableDescriptor.SelectionList(WorkflowJob.OPTION_AUTO,VariableRole.Option);
		optionalVar.isRequired = true;
		optionalVar.setVariableName("Output CSV Separator");
		optionalVar.setHelpMsg("Set the the separator for the output CSV file.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_OUT_CSV_SEP);
		optionalVar.addOptionValue("Same","same","Same as input files");
		optionalVar.addOptionValue("Tab","tab","Tab Separator ");
		optionalVar.addOptionValue("Semicolon","semicolon","Semicolon (;) Separator");
		optionalVar.addOptionValue("Comma","comma","Comma (,) Separator");
		optionalVar.addOptionValue("Space","space","WhiteSpace Separator");
		optionalVar.setDefaultIndexValue(0);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 

	
	/* ########################################################################################################## */

	
	public static String R_SCRIPT_OPTION_IN_CSV_SEP = "--in-csvSep";
	static {
		optionToAction.put(R_SCRIPT_OPTION_IN_CSV_SEP,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionInputCSVSeparator();
			}
		});
	}

	public static VariableDescriptor createOptionInputCSVSeparator()
	{
		SelectionListVariableDescriptor optionalVar = VariableDescriptor.SelectionList(WorkflowJob.OPTION_AUTO,VariableRole.Option);
		optionalVar.setVariableName("Input CSV Separator");
		optionalVar.setHelpMsg("Set the the separator for the input CSV file.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_IN_CSV_SEP);
		optionalVar.addOptionValue("Tab","tab","Tab Separator ");
		optionalVar.addOptionValue("Semicolon","semicolon","Semicolon (;) Separator");
		optionalVar.addOptionValue("Comma","comma","Comma (,) Separator");
		optionalVar.addOptionValue("Space","space","WhiteSpace Separator");
		optionalVar.setDefaultIndexValue(1);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 

	
	
	
	
	/* ########################################################################################################## */

	
	public static String R_SCRIPT_OPTION_IN_CSV_DEC = "--in-csvDec";
	static {
		optionToAction.put(R_SCRIPT_OPTION_IN_CSV_DEC,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionInputCSVDecPoint();
			}
		});
	}

	public static VariableDescriptor createOptionInputCSVDecPoint()
	{
		SelectionListVariableDescriptor optionalVar = VariableDescriptor.SelectionList(WorkflowJob.OPTION_AUTO,VariableRole.Option);
		optionalVar.setVariableName("Input CSV decimal separator");
		optionalVar.setHelpMsg("Set the the decimal separator for the input CSV file.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_IN_CSV_DEC);
		optionalVar.addOptionValue("Point","point","Point (.)");
		optionalVar.addOptionValue("Comma","comma","Comma (,)");
		optionalVar.setDefaultIndexValue(0);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */ 
	
	
	
	
	/* ########################################################################################################## */

	
	public static String R_SCRIPT_OPTION_IN_CSV_NO_HEADER = "--in-csv-no-header";
	static {
		optionToAction.put(R_SCRIPT_OPTION_IN_CSV_NO_HEADER,  new NoRulesCreateOptionCallBack() {
			
			@Override
			public VariableDescriptor createOption() {
				return createOptionInputCSVHasNOHeader();
			}
		});
	}

	public static VariableDescriptor createOptionInputCSVHasNOHeader()
	{
		VariableDescriptor optionalVar = VariableDescriptor.createDescriptorFor(WorkflowJob.OPTION_AUTO,VariableType.Checked,VariableRole.Option);
		optionalVar.setVariableName("Input CSV without header");
		optionalVar.setHelpMsg("Specify if the input csv has header or not.");
		optionalVar.setCommandParamater(R_SCRIPT_OPTION_IN_CSV_NO_HEADER);
		optionalVar.setVariableGroup(R_SCRIPT_COMMON_OPTIONAL);
		return optionalVar;
	}
	/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ */


	public static void addCompressFolderCmd(StringBuilder builder, String arcName,    String folder) {
		

		builder.append(BashHelper.st(Tracking.trackLog("Compressing Result")));
		builder.append(BashHelper.st("tar -czvf " + folder + "/"+ arcName + ".tar.gz   -C " +  folder +" *"));
		
	}


	// move to server
	

}
