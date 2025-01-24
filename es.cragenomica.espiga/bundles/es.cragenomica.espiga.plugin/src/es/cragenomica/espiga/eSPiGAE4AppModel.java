package es.cragenomica.espiga;

import java.util.Collections;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.biotechvana.commons.GRPOApplication;
import com.biotechvana.commons.IGproApp;
import com.biotechvana.workflow.ui.WorkflowUI;

import es.cragenomica.espiga.workflow.manager.eSPiGAJobsCatalog;

public class eSPiGAE4AppModel {

	
	@Execute
	public void init(MApplication application, EModelService modelSer , GRPOApplication app) {

		
		for(MPartDescriptor x : application.getDescriptors()) {
			if(x.getElementId().equals(WorkflowUI.JOBS_MAIN_VIEW_ID)) {
				x.setIconURI(IGproApp.getIconURI(Activator.BUNDLE_ID, "icons/16x16.png"));
			}
		}

		// Change main title
		MTrimmedWindow window = (MTrimmedWindow)modelSer.find(IGproApp.UI_MAIN_WINDOW_ID, application);
		window.setLabel(app.getAppName() + " " + app.getVersion());

		// Setup main menu
		MMenu mainMenu =  window.getMainMenu();

		MMenuElement firstMenu = mainMenu.getChildren().get(0);
		int menuIndx = 0;
		if(firstMenu.getElementId().equals(IGproApp.UI_DIRECTORY_MENU_ID  )) {
			menuIndx = 1;
		}
		
		
		
		MMenu menuItemeSPiGAProtocols = MMenuFactory.INSTANCE.createMenu();
		menuItemeSPiGAProtocols.setLabel("eSPiGA Protocols");
		menuItemeSPiGAProtocols.setElementId("eSPiGA.protocols");
		mainMenu.getChildren().add(menuIndx,menuItemeSPiGAProtocols);
		
		
		
		

		
		
		// Show all pipelines action
//		MHandledMenuItem showAllPipelinesItem = MMenuFactory.INSTANCE.createHandledMenuItem();
//		showAllPipelinesItem.setLabel("Pipeline Mode");
//		MCommand mCommand = (MCommand) modelSer.findElements(application, WorkflowUI.ALL_PIPELINES_VIEW_SHOW_COMMAND_ID, MCommand.class, Collections.EMPTY_LIST).get(0);
//		showAllPipelinesItem.setCommand(mCommand);
//		menuItemeSPiGAProtocols.getChildren().add(showAllPipelinesItem);
		
		
		MMenu menuItemStepByStepMode = MMenuFactory.INSTANCE.createMenu();
		menuItemStepByStepMode.setLabel("Step by Step Mode");
		menuItemeSPiGAProtocols.getChildren().add(menuItemStepByStepMode);
		
		
		{ 
			// TODO :: refactor this to a method 
			MHandledMenuItem protocalAItem = MMenuFactory.INSTANCE.createHandledMenuItem();
			protocalAItem.setLabel("eSPiGA Protocol");


			MCommand cmd = application.getCommand(WorkflowUI.JOBS_MAIN_VIEW_SHOW_COMMAND_ID);
			protocalAItem.setCommand(cmd);
	        MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
	        parameter.setName(WorkflowUI.JOBS_MAIN_VIEW_SHOW_COMMAND_PARAMETER_TOOLBOX_ID);
	        parameter.setValue(eSPiGAJobsCatalog.ESPIGA_MAIN_PRTOCOL);
	        protocalAItem.getParameters().add(parameter);
			menuItemStepByStepMode.getChildren().add(protocalAItem);

		}
		
		
		// get Update Menu Item and set it up
		// com.biotechvana.ui.basics.directmenuitem.update
//		Object mItem = (Object)modelSer.find("com.biotechvana.ui.basics.directmenuitem.update", application);
//		MDirectMenuItem updateMItem = (MDirectMenuItem) modelSer.findElements(mainMenu, IGPROApp.UI_UPDATE_MENU_ITEM_ID, MDirectMenuItem.class);
	}
	
}
