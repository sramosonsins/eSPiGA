 
package es.cragenomica.espiga.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import es.cragenomica.espiga.eSPiGAUI;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class ShowExperimentConfigurationViewHandler {
	@Execute
	public void execute(EPartService partService,EModelService eModelService, MWindow window) {
		MPart part =  (MPart) eModelService.find(eSPiGAUI.EXPERIMENT_CONFIGURATION_VIEW_ID,window); 
		
		if(part == null ) {
			part = partService.createPart(eSPiGAUI.EXPERIMENT_CONFIGURATION_VIEW_ID);
			if(part != null) {
				partService.showPart(part, PartState.VISIBLE);
			}
		}
		if(part == null )
			return;
         // the provided part is be shown
        partService.showPart(part, PartState.ACTIVATE);
	}
	
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}