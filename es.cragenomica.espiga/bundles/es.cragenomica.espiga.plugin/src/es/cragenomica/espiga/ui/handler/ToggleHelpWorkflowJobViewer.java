 
package es.cragenomica.espiga.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;

import es.cragenomica.espiga.ui.views.TaskWorkspaceView;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class ToggleHelpWorkflowJobViewer {
	@Execute
	public void execute(TaskWorkspaceView taskWorkspaceView) {
		taskWorkspaceView.toggleHelpWorkflowTask();
	}
	
	
	@CanExecute
	public boolean canExecute(TaskWorkspaceView taskWorkspaceView) {
		if(taskWorkspaceView != null) {
			// Other checks
			return true;
		}
		return false;
	}
		
}