 
package es.cragenomica.espiga.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.Dialog;

import es.cragenomica.espiga.ui.PreferencesSSHDialog;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class ShowServerSettingsDialog {
	@Execute
	public void execute(IEclipseContext context) {
		PreferencesSSHDialog dialog = ContextInjectionFactory.make(PreferencesSSHDialog.class, context);

		if (dialog.open() == Dialog.CANCEL) {
			return;
		}

		dialog.close();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}