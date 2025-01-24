package es.cragenomica.espiga.ui.views;

import java.net.URL;


import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.biotechvana.commons.SharedImages;
import com.biotechvana.workflow.WorkflowJob;
import com.biotechvana.workflow.manager.IWorkflowManager;
import com.biotechvana.workflow.views.WorkflowJobBasicComposite;
import com.biotechvana.workflow.views.WorkflowJobClassicComposite;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

public class TaskWorkspaceView {
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean isMac() {
        return (OS.contains("mac"));
    }
	@Inject
	private MPart part;

	@Inject
	IWorkflowManager workflowManager;

	private ImageDescriptor PLACEHOLDER_DESC = getImageDescriptor("icons/SPiGA_icon_t.png");
	private Image PLACEHOLDER = PLACEHOLDER_DESC.createImage();
	private Image oldImage;
	Composite mainBody;

	Composite mainHolder;

	WorkflowJobBasicComposite jobCompsite;

	WorkflowJob activeTask;
	String curTaskId = "";

	private FormToolkit toolkit;
	private Image flipImageVertically(Image image) {
	    ImageData imageData = image.getImageData();
	    int width = imageData.width;
	    int height = imageData.height;
	    int[] pixels = new int[width];
	    byte[] alpha = new byte[width];
	    for (int y = 0; y < height / 2; y++) {
	        imageData.getPixels(0, y, width, pixels, 0);
	        imageData.getAlphas(0, y, width, alpha, 0);
	        int[] tempPixels = new int[width];
	        byte[] tempAlpha = new byte[width];
	        imageData.getPixels(0, height - 1 - y, width, tempPixels, 0);
	        imageData.getAlphas(0, height - 1 - y, width, tempAlpha, 0);
	        imageData.setPixels(0, y, width, tempPixels, 0);
	        imageData.setAlphas(0, y, width, tempAlpha, 0);
	        imageData.setPixels(0, height - 1 - y, width, pixels, 0);
	        imageData.setAlphas(0, height - 1 - y, width, alpha, 0);
	    }
	    return new Image(image.getDevice(), imageData);
	}
	@PostConstruct
	public void postConstruct(Composite parent, MApplication app) {
		app.getContext().set(TaskWorkspaceView.class, this);
		mainBody = new Composite(parent, SWT.TRANSPARENCY_ALPHA);
		mainBody.setBackgroundMode(SWT.INHERIT_DEFAULT);
		FillLayout layout1 = new FillLayout(SWT.VERTICAL);
		layout1.marginWidth = layout1.marginHeight = 10;
		mainBody.setLayout(layout1);
		toolkit = new FormToolkit(mainBody.getDisplay());

//		mainBody.addListener(SWT.Resize, event -> {
//			Rectangle rect = mainBody.getClientArea();
//			ImageData data = PLACEHOLDER.getImageData().scaledTo(rect.width, rect.height);
//			Image newImage = new Image(parent.getDisplay(), data);
//			mainBody.setBackgroundImage(newImage);
//			if (oldImage != null) oldImage.dispose();
//			oldImage = newImage;
//		});

		if (isMac())
			mainBody.setBackgroundImage(flipImageVertically(PLACEHOLDER));
		else
			mainBody.setBackgroundImage(PLACEHOLDER);
//		mainBody.addPaintListener(e -> {
//		    Image image = PLACEHOLDER;
//		    if (isMac()) {
//		        image = flipImageVertically(PLACEHOLDER);
//		    }
//		    int imgWidth = image.getBounds().width;
//		    int imgHeight = image.getBounds().height;
//		    int x = (mainBody.getClientArea().width - imgWidth) / 2;
//		    int y = (mainBody.getClientArea().height - imgHeight) / 2;
//		    e.gc.drawImage(image, x, y);
//		});
		
		setInit();

	}

	public static ImageDescriptor getImageDescriptor(String path) {
		Bundle bundle = FrameworkUtil.getBundle(TaskWorkspaceView.class);
		// use the org.eclipse.core.runtime.Path as import
		URL url = FileLocator.find(bundle, new Path(path), null);
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		return imageDescriptor;
	}

	void setInit() {
		mainHolder = new Composite(mainBody, SWT.None);
		mainHolder.setLayout(new FillLayout());
		Label label = new Label(mainHolder, SWT.CENTER);
		label.setText("Tasks Workspace, select a task from the menu to run.");
		FontData[] fD = label.getFont().getFontData();
		fD[0].setHeight(10);
		fD[0].setStyle(SWT.BOLD);
		label.setFont(new Font(mainBody.getDisplay(), fD[0]));
	}

	@Inject
	public void setWorkflowJobJob(@Optional WorkflowJob task) {
		// Working here means a dedicated view for the job
		this.activeTask = task;
		updateContainer(task);
	}

	private void updateContainer(WorkflowJob task) {
		updateContainer(task, true);

	}

	private void updateContainer(WorkflowJob task, boolean clearCurrent) {
		if (task == null) {
			return;
		}
		this.mainBody.setLayoutDeferred(true);
		if (jobCompsite != null && !jobCompsite.isDisposed()) {
			jobCompsite.dispose();
			jobCompsite = null;
		}
		if (mainHolder != null && !mainHolder.isDisposed()) {
			mainHolder.dispose();
			mainHolder = null;
		}

		String jobClassID = task.getClass().getName();
		if (!clearCurrent && curTaskId.equals(jobClassID))
			return;
		curTaskId = jobClassID;
		mainHolder = new Composite(mainBody, SWT.NONE);
		mainHolder.setLayout(new GridLayout(1, false));

		jobCompsite = new WorkflowJobClassicComposite(workflowManager, task, mainHolder, SWT.BORDER, toolkit, true,
				false);
		jobCompsite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite controlsComposite = new Composite(mainHolder, SWT.NONE);
		controlsComposite.setLayout(new GridLayout(1, true));
		controlsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Button runButton = new Button(controlsComposite, SWT.NONE);
		runButton.setText("Run Task");
		runButton.setImage(SharedImages.RUN_EXC);
		runButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));

		runButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean res = jobCompsite.runJob();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		this.mainBody.setLayoutDeferred(false);
		this.mainBody.layout();
	}

	public void resetWorkflowTask() {
		// TODO Auto-generated method stub

	}

	public void runWorkflowTask() {
		// TODO Auto-generated method stub
		boolean res = jobCompsite.runJob();
	}

	public void toggleHelpWorkflowTask() {
		// TODO Auto-generated method stub
		jobCompsite.toggleVarHelpMsg();
		
		

	}
}
