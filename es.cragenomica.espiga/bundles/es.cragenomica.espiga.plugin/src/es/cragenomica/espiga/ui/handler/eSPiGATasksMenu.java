 
package es.cragenomica.espiga.ui.handler;

import java.util.List;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import es.cragenomica.espiga.jobs.MstatspopJob;
import es.cragenomica.espiga.jobs.MstatspopPostScriptJob;
import es.cragenomica.espiga.jobs.MstatspopSimulationAnalysesJob;
import es.cragenomica.espiga.jobs.NpstatJob;
import es.cragenomica.espiga.jobs.NpstatPostScriptJob;
import es.cragenomica.espiga.jobs.PFcallerJob;
import es.cragenomica.espiga.jobs.RecombinationSplineJob;
import es.cragenomica.espiga.jobs.SingleRegionAnalysesJob;
import es.cragenomica.espiga.jobs.CreateWeightsFileJob;
import es.cragenomica.espiga.jobs.CreatetFastaIndexFileJob;
import es.cragenomica.espiga.jobs.HKAdirectJob;
import es.cragenomica.espiga.jobs.MergetFastaFilesJob;
import es.cragenomica.espiga.jobs.MlcoalsimJob;
import es.cragenomica.espiga.jobs.MstatspopGeneralJob;
import es.cragenomica.espiga.jobs.MstatspopGenomeAnalysesJob;
import es.cragenomica.espiga.UIConstants;
import es.cragenomica.espiga.converters.*;
public class eSPiGATasksMenu {
	
	MMenuElement createItem (EModelService modelService , MApplication application ,
			String task_name,String task_class) {
		MHandledMenuItem handledMenuItem = modelService .createModelElement(MHandledMenuItem.class);
		handledMenuItem.setLabel(task_name);
		MCommand cmd = application.getCommand(UIConstants.SHOW_WORKFLOW_TASK_COMMAND_ID);
		handledMenuItem.setCommand(cmd);
		
		MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
	    parameter.setName(UIConstants.SHOW_WORKFLOW_TASK_CP_TASK_ID);
	    parameter.setValue(task_class);
	    handledMenuItem.getParameters().add(parameter);
		
		return handledMenuItem;
		// MMenuSeparator
	}
	
	MMenuElement createPreprocessingMenu(EModelService modelService , MApplication application) {
		MMenu preprocessingMenu =  modelService .createModelElement(MMenu.class);
		preprocessingMenu.setLabel("Pre-processing");
		
		MMenu fileFormatConvertions =  modelService .createModelElement(MMenu.class);
		fileFormatConvertions.setLabel("Formats Convertions");
		preprocessingMenu.getChildren().add(fileFormatConvertions);
		
		fileFormatConvertions.getChildren().add( createItem(modelService,application,
				FastaTFastaConverterJob.JOB_NAME,
				FastaTFastaConverterJob.class.getName()));
		fileFormatConvertions.getChildren().add( createItem(modelService,application,
				gvcfTFastaConverterJob.JOB_NAME,
				gvcfTFastaConverterJob.class.getName()));
		
		fileFormatConvertions.getChildren().add(  modelService .createModelElement(MMenuSeparator.class));
		
		fileFormatConvertions.getChildren().add( createItem(modelService,application,
				TfastaFastaConverterJob.JOB_NAME,
				TfastaFastaConverterJob.class.getName()));
//		fileFormatConvertions.getChildren().add( createItem(modelService,application,
//				fastaFastaConverterJob.JOB_NAME,
//				fastaFastaConverterJob.class.getName()));
		
		fileFormatConvertions.getChildren().add(  modelService .createModelElement(MMenuSeparator.class));

		fileFormatConvertions.getChildren().add( createItem(modelService,application,
				TfastaMsConverterJob.JOB_NAME,
				TfastaMsConverterJob.class.getName()));
		fileFormatConvertions.getChildren().add( createItem(modelService,application,
				fastaMsConverterJob.JOB_NAME,
				fastaMsConverterJob.class.getName()));
		
		fileFormatConvertions.getChildren().add(  modelService .createModelElement(MMenuSeparator.class));
		fileFormatConvertions.getChildren().add( createItem(modelService,application,
				msGenotypeConverterJob.JOB_NAME,
				msGenotypeConverterJob.class.getName()));
		
//		preprocessingMenu.getChildren().add( 
//				createItem(modelService,application,
//						CreateWeightsFileJob.JOB_NAME,
//						CreateWeightsFileJob.class.getName()));
		
		preprocessingMenu.getChildren().add( 
				createItem(modelService,application,
						CreatetFastaIndexFileJob.JOB_NAME,
						CreatetFastaIndexFileJob.class.getName()));
		
		preprocessingMenu.getChildren().add( 
				createItem(modelService,application,
						MergetFastaFilesJob.JOB_NAME,
						MergetFastaFilesJob.class.getName()));
		
		
		
		return preprocessingMenu;
	}
	
	
	MMenuElement createVariabilityAnalysesMenu(EModelService modelService , MApplication application) {
		MMenu variabilityAnalysesMenu =  modelService .createModelElement(MMenu.class);
		variabilityAnalysesMenu.setLabel("Variability Analyses");
		
//		variabilityAnalysesMenu.getChildren().add( 
//				createItem(modelService,application,
//						MstatspopGeneralJob.JOB_NAME,
//						MstatspopGeneralJob.class.getName()));

//		variabilityAnalysesMenu.getChildren().add(  modelService .createModelElement(MMenuSeparator.class));

		
		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						MstatspopGenomeAnalysesJob.JOB_NAME,
						MstatspopGenomeAnalysesJob.class.getName()));
		
		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						MstatspopSimulationAnalysesJob.JOB_NAME,
						MstatspopSimulationAnalysesJob.class.getName()));
		
		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						SingleRegionAnalysesJob.JOB_NAME,
						SingleRegionAnalysesJob.class.getName()));
		
		variabilityAnalysesMenu.getChildren().add(  modelService .createModelElement(MMenuSeparator.class));

		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						NpstatJob.JOB_NAME,
						NpstatJob.class.getName()));
		
		
		return variabilityAnalysesMenu;
	}
	
	MMenuElement createPostprocessingMenu(EModelService modelService , MApplication application) {
		MMenu postprocessingMenu =  modelService .createModelElement(MMenu.class);
		postprocessingMenu.setLabel("Post-processing");
		
		postprocessingMenu.getChildren()
				.add(createItem(modelService, application, MstatspopPostScriptJob.JOB_NAME, MstatspopPostScriptJob.class.getName()));
		
		postprocessingMenu.getChildren().add( 
				createItem(modelService, application, NpstatPostScriptJob .JOB_NAME,
						NpstatPostScriptJob.class.getName())
				);
		
		return postprocessingMenu;
	}
	
	MMenuElement createOthersMenu(EModelService modelService , MApplication application) {
		MMenu variabilityAnalysesMenu =  modelService .createModelElement(MMenu.class);
		variabilityAnalysesMenu.setLabel("Other Analyses");
		
//		variabilityAnalysesMenu.getChildren().add( 
//				createItem(modelService,application,
//						MstatspopGeneralJob.JOB_NAME,
//						MstatspopGeneralJob.class.getName()));

//		variabilityAnalysesMenu.getChildren().add(  modelService .createModelElement(MMenuSeparator.class));

		
		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						HKAdirectJob.JOB_NAME,
						HKAdirectJob.class.getName()));
		
		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						MlcoalsimJob.JOB_NAME,
						MlcoalsimJob.class.getName()));
		
//		variabilityAnalysesMenu.getChildren().add( 
//				createItem(modelService,application,
//						RecombinationSplineJob.JOB_NAME,
//						RecombinationSplineJob.class.getName()));
		variabilityAnalysesMenu.getChildren().add( 
				createItem(modelService,application,
						PFcallerJob.JOB_NAME,
						PFcallerJob.class.getName()));
		return variabilityAnalysesMenu;
	}
	
	@AboutToShow
	public void aboutToShow(List<MMenuElement> items, EModelService modelService , MApplication application) {
//		MMenuElement dynamicItem = createItem(modelService,application);
//		items.add(dynamicItem); 
//		dynamicItem = createItem(modelService,application);
//		items.add(dynamicItem); 
//		dynamicItem = createItem(modelService,application);
		items.add(createPreprocessingMenu(modelService,application)); 

		items.add(createVariabilityAnalysesMenu(modelService,application)); 
		items.add(createPostprocessingMenu(modelService,application));
		items.add(createOthersMenu(modelService,application)); 
	}
		
}