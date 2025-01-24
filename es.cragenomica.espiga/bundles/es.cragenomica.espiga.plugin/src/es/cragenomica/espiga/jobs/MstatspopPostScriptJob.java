package es.cragenomica.espiga.jobs;


import java.util.Set;

import com.biotechvana.workflow.ISWFJob;
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
import com.biotechvana.workflow.variables.JobVariable;

import es.cragenomica.espiga.workflow.manager.eSPiGAVariableBag;
import es.cragenomica.espiga.workflow.manager.eSPiGAWorkflowConstants;

public class MstatspopPostScriptJob  extends ISWFJob{ 

	


	
	static public final String JOB_ID = "mstatspop_plot";
	static public final String JOB_NAME = "Visualize mstatspop results";
	
	
	public static String OUTPUT_FILES = eSPiGAVariableBag.OUTPUT_FILES;

	
	public static String INPUT_FILES= eSPiGAVariableBag.INPUT_FILES;
	public static String INPUT_NPOP= "input_npop";

	
	public MstatspopPostScriptJob() {
		super(JOB_ID, JOB_NAME, true,false);
		

		
		this.jobDesc.setDesc("mstatspop-plot: Visualize mstatspop results");
	}

	@Override
	protected void createInputs() {
		
		VariableDescriptor varInputFile = VariableDescriptor.createDescriptorFor(INPUT_FILES,VariableType.File,VariableRole.Input);
		varInputFile.isRequired = true;
		
		varInputFile.setVariableName("Input mstatspop stats file");
		varInputFile.setHelpMsg("Input mstatspop stats file, output from  mstatspop analysis tools");
		jobDesc.addVariable(varInputFile);
		
		
		// npop
		jobDesc.addVariable(VariableDescriptor.Int(INPUT_NPOP, VariableRole.Input)
				.setMinValue(1)
				.setVariableName("Number of populations").setIsRequired(true).setHelpMsg("Number of populations"));
		
	}

	@Override	protected void createOutputs() {
		VariableDescriptor varOutputFile = VariableDescriptor.createDescriptorFor(OUTPUT_FILES, VariableType.File,
				VariableRole.Output);
		varOutputFile.isRequired = true;
		varOutputFile.setVariableName("output file");
		jobDesc.addVariable(varOutputFile);
		
	}

	static public final String OPTION_ALL_COLUMNS = "option_all_columns";
	static private Set<String> ALL_CLM_KEYS = new java.util.HashSet<String>();
	@Override
	protected void createOptions() {
		
		jobDesc.addVariable(VariableDescriptor.Checked(OPTION_ALL_COLUMNS, VariableRole.Option)
				.setVariableName("Collect all columns")
				.setHelpMsg("Collect all columns. DEFAULT : yes")
				.setDefaultValue(JobVariable.TRUE_VALUE)
				.setCommandParamater("all")
				.setVariableGroup("msstatspop columns options"));
		

//		scaffold_name: chr10
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_scaffold_name", VariableRole.Option)
				.setVariableName("Include scaffold name").setHelpMsg("add scaffold name column. DEFAULT : yes")
				.setCommandParamater("scaffold_name")
				.setDefaultValue(JobVariable.TRUE_VALUE)
				.setReadOnly(true)
				.setVariableGroup("mstatspop columns options"));
		// ALL_CLM_KEYS.add("option_clm_scaffold_name");
//		start_window: 1
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_start_window", VariableRole.Option)
				.setVariableName("Include start window").setHelpMsg("add start window column. DEFAULT : yes")
				.setCommandParamater("start_window")
				.setDefaultValue(JobVariable.TRUE_VALUE).setReadOnly(true).setVariableGroup("mstatspop columns options"));
		// ALL_CLM_KEYS.add("option_clm_start_window");
//		end_window: 100
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_end_window", VariableRole.Option)
				.setVariableName("Include end window").setHelpMsg("add end window column. DEFAULT : yes")
				.setCommandParamater("end_window")
				.setDefaultValue(JobVariable.TRUE_VALUE).setReadOnly(true).setVariableGroup("mstatspop columns options"));
		// ALL_CLM_KEYS.add("option_clm_end_window");
		
		
//		infile: ./100Kallchr.tfa.gz
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_infile", VariableRole.Option)
				.setVariableName("Include infile")
				.setCommandParamater("infile")
				.setHelpMsg("add infile column. DEFAULT : no")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_infile");
//		missing: 1
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_missing", VariableRole.Option)
				.setVariableName("Include missing").setHelpMsg("add missing column. DEFAULT : no")
				.setCommandParamater("missing")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_missing");
//		iteration: 0
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_iteration", VariableRole.Option)
				.setVariableName("Include iteration").setHelpMsg("add iteration column. DEFAULT : no")
				.setCommandParamater("iteration")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_iteration");
//		npermutations: 0
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_npermutations", VariableRole.Option)
				.setVariableName("Include npermutations").setHelpMsg("add npermutations column. DEFAULT : no")
				.setCommandParamater("npermutations")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_npermutations");
//		seed: 123456
//		jobDesc.addVariable(
//				VariableDescriptor.Checked("option_clm_seed", VariableRole.Option).setVariableName("Include seed")
//		.setCommandParamater("seed")
//						.setHelpMsg("add seed column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
//		Length: 0.00
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_length", VariableRole.Option).setVariableName("Include Length")
				.setCommandParamater("length")
						.setHelpMsg("add Length column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_length");
//		Lengtht: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_lengtht", VariableRole.Option).setVariableName("Include Lengtht")
				.setCommandParamater("lengtht")
						.setHelpMsg("add Lengtht column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_lengtht");
//		mh: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_mh", VariableRole.Option).setVariableName("Include mh")
				.setCommandParamater("mh")
						.setHelpMsg("add mh column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_mh");
//		Ratio_S/V: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_ratio_sv", VariableRole.Option)
				.setVariableName("Include Ratio_S/V").setHelpMsg("add Ratio_S/V column. DEFAULT : no")
				.setCommandParamater("Ratio_S/V")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_ratio_sv");
//		Ratio_Missing: 1.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_ratio_missing", VariableRole.Option)
				.setVariableName("Include Ratio_Missing").setHelpMsg("add Ratio_Missing column. DEFAULT : no")
				.setCommandParamater("Ratio_Missing")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_ratio_missing");
//		Variants: 0
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_variants", VariableRole.Option)
				.setVariableName("Include Variants").setHelpMsg("add Variants column. DEFAULT : no")
				.setCommandParamater("Variants")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_variants");
//		npops: 3
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_npops", VariableRole.Option).setVariableName("Include npops")
				.setCommandParamater("npops")
						.setHelpMsg("add npops column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_npops");
//		nsam
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_nsam", VariableRole.Option).setVariableName("Include nsam")
				.setCommandParamater("nsam")
						.setHelpMsg("add nsam column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_nsam");
//		Eff_length1_pop_outg[0]: 0.00
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_eff_length1_pop_outg", VariableRole.Option)
				.setVariableName("Include Eff_length1_pop_outg")
				.setHelpMsg("add Eff_length1_pop_outg column. DEFAULT : no")
				.setCommandParamater("Eff_length1_pop_outg")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_eff_length1_pop_outg");
//		Eff_length2_pop_outg[0]: 0.00
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_eff_length2_pop_outg", VariableRole.Option)
				.setVariableName("Include Eff_length2_pop_outg")
				.setHelpMsg("add Eff_length2_pop_outg column. DEFAULT : no")
				.setCommandParamater("Eff_length2_pop_outg")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_eff_length2_pop_outg");
//		Eff_length3_pop_outg[0]: 0.00
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_eff_length3_pop_outg", VariableRole.Option)
				.setVariableName("Include Eff_length3_pop_outg")
				.setHelpMsg("add Eff_length3_pop_outg column. DEFAULT : no")
				.setCommandParamater("Eff_length3_pop_outg")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_eff_length3_pop_outg");

//		S[0]: 0
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_s", VariableRole.Option).setVariableName("Include S")
				.setCommandParamater("S")
				.setHelpMsg("add S column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_s");
//		Theta(Wat)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_wat", VariableRole.Option)
				.setVariableName("Include Theta(Wat)").setHelpMsg("add Theta(Wat) column. DEFAULT : no")
				.setCommandParamater("Theta(Wat)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_wat");
//		Theta(Taj)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_taj", VariableRole.Option)
				.setVariableName("Include Theta(Taj)").setHelpMsg("add Theta(Taj) column. DEFAULT : no")
				.setCommandParamater("Theta(Taj)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_taj");
//		Theta(Fu&Li)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_fu_li", VariableRole.Option)
				.setVariableName("Include Theta(Fu&Li)").setHelpMsg("add Theta(Fu&Li) column. DEFAULT : no")
				.setCommandParamater("Theta(Fu&Li)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_fu_li");
//		Theta(Fay&Wu)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_fay_wu", VariableRole.Option)
				.setVariableName("Include Theta(Fay&Wu)").setHelpMsg("add Theta(Fay&Wu) column. DEFAULT : no")
				.setCommandParamater("Theta(Fay&Wu)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_fay_wu");
//		Theta(Zeng)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_zeng", VariableRole.Option)
				.setVariableName("Include Theta(Zeng)").setHelpMsg("add Theta(Zeng) column. DEFAULT : no")
				.setCommandParamater("Theta(Zeng)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_zeng");
//		Theta(Achaz,Wat)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_achaz_wat", VariableRole.Option)
				.setVariableName("Include Theta(Achaz,Wat)").setHelpMsg("add Theta(Achaz,Wat) column. DEFAULT : no")
				.setCommandParamater("Theta(Achaz,Wat)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_achaz_wat");
//		Theta(Achaz,Taj)[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_achaz_taj", VariableRole.Option)
				.setVariableName("Include Theta(Achaz,Taj)").setHelpMsg("add Theta(Achaz,Taj) column. DEFAULT : no")
				.setCommandParamater("Theta(Achaz,Taj)")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_achaz_taj");
//		Theta/nt(Taj)HKY[0]: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_theta_nt_taj_hky", VariableRole.Option)
				.setVariableName("Include Theta/nt(Taj)HKY").setHelpMsg("add Theta/nt(Taj)HKY column. DEFAULT : no")
				.setCommandParamater("Theta/nt(Taj)HKY")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_theta_nt_taj_hky");
//		an_xo[0]: 0.000000
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_an_xo", VariableRole.Option).setVariableName("Include an_xo")
				.setCommandParamater("an_xo")
						.setHelpMsg("add an_xo column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_an_xo");
//		bn_xo[0]: 0.000000
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_bn_xo", VariableRole.Option).setVariableName("Include bn_xo")
				.setCommandParamater("bn_xo")
						.setHelpMsg("add bn_xo column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_bn_xo");
//		Divergence[0]: 0.000000
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_divergence", VariableRole.Option)
				.setVariableName("Include Divergence").setHelpMsg("add Divergence column. DEFAULT : no")
				.setCommandParamater("Divergence")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_divergence");
//		Divergence/nt_HKY[0]: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_divergence_nt_hky", VariableRole.Option)
				.setVariableName("Include Divergence/nt_HKY").setHelpMsg("add Divergence/nt_HKY column. DEFAULT : no")
				.setCommandParamater("Divergence/nt_HKY")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_divergence_nt_hky");
//		TajimaD[0]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_tajimad", VariableRole.Option).setVariableName("Include TajimaD")
				.setCommandParamater("TajimaD")
						.setHelpMsg("add TajimaD column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_tajimad");
//		Fu&LiD[0]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_fu_li_d", VariableRole.Option).setVariableName("Include Fu&LiD")
				.setCommandParamater("Fu&LiD")
						.setHelpMsg("add Fu&LiD column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_fu_li_d");
//		Fu&LiF[0]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_fu_li_f", VariableRole.Option).setVariableName("Include Fu&LiF")
				.setCommandParamater("Fu&LiF")
						.setHelpMsg("add Fu&LiF column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_fu_li_f");
//		Fay&WunormH[0]: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_fay_wunormh", VariableRole.Option)
				.setVariableName("Include Fay&WunormH").setHelpMsg("add Fay&WunormH column. DEFAULT : no")
				.setCommandParamater("Fay&WunormH")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_fay_wunormh");
//		ZengE[0]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_zeng_e", VariableRole.Option).setVariableName("Include ZengE")
				.setCommandParamater("ZengE")
						.setHelpMsg("add ZengE column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_zeng_e");
//		AchazY[0]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_achaz_y", VariableRole.Option).setVariableName("Include AchazY")
				.setCommandParamater("AchazY")
						.setHelpMsg("add AchazY column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_achaz_y");
//		FerrettiL[0]: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_ferretti_l", VariableRole.Option)
				.setVariableName("Include FerrettiL").setHelpMsg("add FerrettiL column. DEFAULT : no")
				.setCommandParamater("FerrettiL")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_ferretti_l");
//		R2[0]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_r2", VariableRole.Option).setVariableName("Include R2")
				.setCommandParamater("R2")
						.setHelpMsg("add R2 column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_r2");

//		Sx[0]: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_sx", VariableRole.Option).setVariableName("Include Sx")
				.setCommandParamater("Sx")
						.setHelpMsg("add Sx column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_sx");
//		Sf[0]: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_sf", VariableRole.Option).setVariableName("Include Sf")
				.setCommandParamater("Sf")
						.setHelpMsg("add Sf column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_sf");
//		Sxf[0,rest]: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_sxf", VariableRole.Option).setVariableName("Include Sxf")
				.setCommandParamater("Sxf")
						.setHelpMsg("add Sxf column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_sxf");
//		Ss[0]: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_ss", VariableRole.Option).setVariableName("Include Ss")
				.setCommandParamater("Ss")
						.setHelpMsg("add Ss column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_ss");
//		MD_SDev
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_md_sdev", VariableRole.Option).setVariableName("Include MD_SDev")
				.setCommandParamater("MD_SDev")
						.setHelpMsg("add MD_SDev column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_md_sdev");
//		MD_Skewness[0]: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_md_skewness", VariableRole.Option)
				.setVariableName("Include MD_Skewness").setHelpMsg("add MD_Skewness column. DEFAULT : no")
				.setCommandParamater("MD_Skewness")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_md_skewness");
//		MD_Kurtosis[0]: NA
		jobDesc.addVariable(VariableDescriptor.Checked("option_clm_md_kurtosis", VariableRole.Option)
				.setVariableName("Include MD_Kurtosis").setHelpMsg("add MD_Kurtosis column. DEFAULT : no")
				.setCommandParamater("MD_Kurtosis")
				.setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_md_kurtosis");

//		Fst[0,1]: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_fst", VariableRole.Option).setVariableName("Include Fst")
				.setCommandParamater("Fst")
						.setHelpMsg("add Fst column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_fst");
//		P-value: NA
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_p_value", VariableRole.Option).setVariableName("Include P-value")
				.setCommandParamater("P-value")
						.setHelpMsg("add P-value column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_p_value");
//		PiA[0,1]: 0.000000
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_pia", VariableRole.Option).setVariableName("Include PiA")
				.setCommandParamater("PiA")
						.setHelpMsg("add PiA column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_pia");
//		PiT[0,1]: 0.000000
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_pit", VariableRole.Option).setVariableName("Include PiT")
				.setCommandParamater("PiT")
						.setHelpMsg("add PiT column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS	.add("option_clm_pit");
//		len[0,1]: 0.000000
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_len", VariableRole.Option).setVariableName("Include len")
				.setCommandParamater("len")
						.setHelpMsg("add len column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_len");
//		fr[0,1]: 0
		jobDesc.addVariable(
				VariableDescriptor.Checked("option_clm_fr", VariableRole.Option).setVariableName("Include fr")
				.setCommandParamater("fr")
						.setHelpMsg("add fr column. DEFAULT : no").setVariableGroup("mstatspop columns options"));
		ALL_CLM_KEYS.add("option_clm_fr");


	}

	@Override
	public boolean notifyVarValueChanged(String varKey, String value) {
		
		if( ALL_CLM_KEYS.contains(varKey) ) {
			if (value.equals(JobVariable.TRUE_VALUE)) {
				getVariable(OPTION_ALL_COLUMNS).setValue(JobVariable.FALSE_VALUE);
			}
		}
		if (varKey.equals(OPTION_ALL_COLUMNS) && value.equals(JobVariable.TRUE_VALUE)) {
			for (String key : ALL_CLM_KEYS) {
				if (!key.equals(OPTION_ALL_COLUMNS)) {
					getVariable(key).setValue(JobVariable.FALSE_VALUE);
				}
			}
		}
		
		return super.notifyVarValueChanged(varKey, value);
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

		
		command_script.addStatment(st(BashHelper.assign("clms_inputs","${outputFolder}/clms.txt")));
		

		// for each column key 
		for (String key : ALL_CLM_KEYS) {
			command_script
					.addStatment(
							BashHelper.If(BashConditions.True(getVariable(key))).Then(
									st("echo \"${key}\" >> ${clms_inputs}")
									));
							
		}
		command_script
			.addStatment(BashHelper.If(BashConditions.True(getVariable(OPTION_ALL_COLUMNS))).Then(st("echo \":\" > ${clms_inputs}")));
		
		
		GeneralCMLTemplate collect_data = new GeneralCMLTemplate();
		collect_data.getBefore().addStatment(st(
				BashHelper.assign("outputFileName",BashHelper.getBaseName(inputVariable))
				
				)
				
				
				);
		collect_data.getBefore().addStatment(st(BashHelper.assign("data_file","${outputFolder}/${outputFileName}.csv")));
		
		
		
		
		collect_data.getBefore().addStatment(
				st(BashHelper.assign(outputVariable,"${outputFolder}/${outputFileName}.pdf"))
				);
		collect_data.setBaseCommand("collect_data_columns.pl");

		collect_data.setTrackInput(inputVariable);

		collect_data.addArgs("-in");
		collect_data.addArgs(inputVariable.$());
		collect_data.addArgs("-fc");
		collect_data.addArgs("${clms_inputs}");
		collect_data.setStdForward("${data_file}");


		command_script.addStatment(collect_data);
		
		
		
		GeneralCMLTemplate plot_data = new GeneralCMLTemplate();
		plot_data.setBaseCommand("plot_mstatspop_general.R");
		plot_data.setTrackInput("${data_file}");
		plot_data.addArgs("${data_file}");
		plot_data.addArgs(outputVariable.$());
		plot_data.addArgs(getVariable(INPUT_NPOP).$());
		command_script.addStatment(plot_data);
		
		command_script.generate(builder);
	}
}
