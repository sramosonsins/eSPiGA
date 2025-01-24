package es.cragenomica.espiga.workflow.manager;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;

import com.biotechvana.workflow.template.WorkflowTemplate;
import com.biotechvana.workflow.ui.BasicWorkflowSelectionViewfilters;

@Creatable
public class eSPiGAWorkflowFilters  extends BasicWorkflowSelectionViewfilters {






	
	public eSPiGAWorkflowFilters () {
		System.err.println("eSPiGAWorkflowFilters");
	}



	/**
	 * add reads layout, preprocssing tools and mappers
	 * @param filterGroup
	 */
	@Override
	protected void addCommonsFilters(Group filterGroup) {

		{
			Composite row = new Composite(filterGroup, SWT.None);
//			GridData layoutData = new GridData(GridData.FILL_BOTH);
//			row.setLayoutData(layoutData);
//			row.setLayout(new GridLayout(2,false));
			RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
			rowLayout.fill = true;
			rowLayout.justify = true;
			rowLayout.marginWidth = rowLayout.marginHeight = 5;
			row.setLayout(rowLayout);
			addReadLayoutFilter(row);
			addBlank(row);
			addPreprocessingToolsFilter(row);
			addBlank(row);
			addMappingToolsFilter(row);
			addBlank(row);

		}

		
		
	}

	


	

	

	



	




	


	


	





	


	@Override
	public void addSelectionTableViewerColumns(TableViewer tableViewer) {
		
		// 4
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.PUSH);
		TableColumn tblclmn = tableViewerColumn.getColumn();
		tblclmn.setText("Diff. Expression");
		tblclmn.setWidth(100);
		tblclmn.setResizable(true);
		tblclmn.setMoveable(false);
		tblclmn.setAlignment(SWT.CENTER);
		
	}


	@Override
	public String getColumnText(WorkflowTemplate wf, int columnIndex) {
		switch (columnIndex) {
		case 4:
			
			break;
		}
		return "";
		
	}



	








	

	
}
