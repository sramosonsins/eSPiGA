package es.cragenomica.espiga.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;



import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.biotechvana.commons.SharedImages;
import com.biotechvana.commons.model.RemoteServerInfo;
import com.biotechvana.commons.model.ServiceStatus;
import com.biotechvana.commons.model.UserLoginInfo;
import com.biotechvana.servercommons.dialogs.preferences.DockerHelper;
import com.biotechvana.servercommons.dialogs.preferences.ServicesStatusComposite;
import com.biotechvana.servercommons.dialogs.preferences.DockerHelper.NTryRestartEx;
import com.biotechvana.users.IUsersService;
import com.biotechvana.users.UserManager;
import com.biotechvana.utils.Constants;
import com.biotechvana.utils.EmailValidator;

import jakarta.inject.Inject;


public class PreferencesSSHDialog extends TitleAreaDialog {
	
	
	
	
	public class OnlineCheckDialog extends Dialog {
		private static final String CLOSE_LABEL = "Close";
		ServicesStatusComposite statusComposite;
		List<ServiceStatus> services;
		protected OnlineCheckDialog(Shell parentShell, List<ServiceStatus> services) {
			super(parentShell);
			this.services =services;
		}

		
		@Override
	    protected Control createDialogArea(Composite parent) {
	        Composite container = (Composite) super.createDialogArea(parent);
	        
	        statusComposite = new ServicesStatusComposite(container, SWT.None);
	        statusComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	        
			/*
			 * Button button = new Button(container, SWT.PUSH); button.setLayoutData(new
			 * GridData(SWT.BEGINNING, SWT.CENTER, false, false));
			 * button.setText("Press me"); button.addSelectionListener(new
			 * SelectionAdapter() {
			 * 
			 * @Override public void widgetSelected(SelectionEvent e) {
			 * System.out.println("Pressed"); } });
			 */
	        // statusComposite.start();
	        statusComposite.start(services);
	        return container;
	    }
		
	    // overriding this methods allows you to set the
	    // title of the custom dialog
	    @Override
	    protected void configureShell(Shell newShell) {
	        super.configureShell(newShell);
	        newShell.setText("Server Status");
	    }
	    
	    

	    @Override
	    protected Point getInitialSize() {
	        return new Point(450, 300);
	    }
		
	    
		protected void createButtonsForButtonBar(Composite parent) {
			// create OK and Cancel buttons by default
			Button btn = createButton(parent, IDialogConstants.CLOSE_ID, CLOSE_LABEL, true);
			btn.setToolTipText("Cancel Check and Close the Window");
		}
	    
		
		@Override
		protected void buttonPressed(int buttonId) {
			if(IDialogConstants.CLOSE_ID == buttonId) {
				statusComposite.cancel();
				close();
			}
		}


		public boolean getCheckStatus() {
			for (ServiceStatus s : services) {
				if(!s.getStatus().equals(ServiceStatus.OK_STATUS)) {
					return false;
				}
			}
			return true;
			
		}


		
	}
	
	
	
	
	public static final String TRUE_CNST = "true"; 
	public static final String FALSE_CNST = "false"; 

	public static final String USE_LOCAL_GPRO_PROP = "USE_LOCAL_GPRO_PROP"; 

	String preferenceEmail =  "";
	String preferenceHost =  "";
	int preferenceSSHPort ;
	int preferenceAPIPort ;

	String preferenceUser =  "";
	String preferencePassword =  "";

	private Text textEmail;
	private Text textHost;
	private Text textUser;
	private Text textPassword;
	private Text textSSHPort;
	private Text textAPIPort;
	private Button buttonTest;

	boolean localDockerMode = false;
	DockerHelper dockerHelper ;
	@Inject
	IUsersService usersService;

//	@Inject Provider<IRunnableContext> runnableContext;

	@Inject
	public PreferencesSSHDialog(Shell parentShell) {
		super(parentShell);

	}
	@Override
	protected boolean isResizable() {
		return true;
	}


	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}
	public void create() {
		super.create();

		setTitle("Server connection settings");
		setMessage("Set your Server connection settings");

		getButton(OK).setEnabled(false);

		getShell().setText("Server Configuration");
		if(!localDockerMode)
			validate();
	}


	protected Control createDialogArea(Composite parent) {
		if(dockerHelper == null)
			dockerHelper = new DockerHelper();

		if(usersService.getUserManager().get(USE_LOCAL_GPRO_PROP, FALSE_CNST).equals(TRUE_CNST)) {
			localDockerMode = true;
		}



		Composite compositeParent = (Composite) super.createDialogArea(parent);
		compositeParent.setLayout(new GridLayout());

		createGroupCredentials(compositeParent);


		return compositeParent;
	}

	Composite localParent;
	private Composite createGroupCredentials(Composite parent) {
		if(localParent != null && !localParent.isDisposed() ) {
			localParent.dispose();
			localParent = null;
		}
		localParent = new Composite(parent, SWT.NONE);
		localParent.setLayout(new GridLayout(3, false));
		localParent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));


		if(! localDockerMode ) {
			Group group = new Group(localParent, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setText("Server login credentials");
			group.setLayout(new GridLayout(3, false));
			GridLayout layout = (GridLayout) group.getLayout();
			layout.marginWidth = 20;
			layout.marginHeight = 20;

			GridData layoutData;



			//row host
			{
				Label labelHost = new Label(group, SWT.NONE);
				labelHost.setText("Host / IP:");
				layoutData = new GridData();
				labelHost.setLayoutData(layoutData);

				textHost = new Text(group, SWT.SINGLE | SWT.BORDER);
				layoutData = new GridData();
				layoutData.widthHint = 200;
				textHost.setLayoutData(layoutData);
				textHost.setMessage("Host IP or domain");


				Label label = new Label(group, SWT.NONE);
				//label.setText("Use 'biotechvana.uv.es' as default");
				layoutData = new GridData();
				label.setLayoutData(layoutData);
			}



			//row user
			{
				Label labelUser = new Label(group, SWT.NONE);
				labelUser.setText("User:");
				layoutData = new GridData();
				labelUser.setLayoutData(layoutData);

				textUser = new Text(group, SWT.SINGLE | SWT.BORDER);
				layoutData = new GridData();
				layoutData.widthHint = 200;
				textUser.setLayoutData(layoutData);

				Label spacer = new Label(group, SWT.NONE);
				spacer.setText("");
				layoutData = new GridData();
				spacer.setLayoutData(layoutData);
			}

			//row password
			{
				Label labelPassword = new Label(group, SWT.NONE);
				labelPassword.setText("Password:");
				layoutData = new GridData();
				labelPassword.setLayoutData(layoutData);

				textPassword = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.PASSWORD);
				layoutData = new GridData();
				layoutData.widthHint = 200;
				textPassword.setLayoutData(layoutData);

				Label spacer = new Label(group, SWT.NONE);
				spacer.setText("");
				layoutData = new GridData();
				spacer.setLayoutData(layoutData);
			}

			//row email
			{
				Label labelEmail = new Label(group, SWT.NONE);
				labelEmail.setText("E-mail address:");
				layoutData = new GridData();
				labelEmail.setLayoutData(layoutData);

				textEmail = new Text(group, SWT.SINGLE | SWT.BORDER);
				layoutData = new GridData();
				layoutData.widthHint = 200;
				layoutData.horizontalSpan = 2;
				textEmail.setLayoutData(layoutData);
			}

			//row port
			{

				Group portsGroup = new Group(group, SWT.NONE);
				portsGroup.setText("Ports");
				portsGroup.setLayout(new GridLayout(4,false));
				layoutData = new GridData(SWT.FILL,SWT.FILL,false,false,2,1);
				portsGroup.setLayoutData(layoutData);


				Label labelPort = new Label(portsGroup, SWT.NONE);
				labelPort.setText("API Port:");
				layoutData = new GridData();
				labelPort.setLayoutData(layoutData);

				textAPIPort = new Text(portsGroup, SWT.SINGLE | SWT.BORDER);
				textAPIPort.setText("");
				textAPIPort.setMessage("80 as default");
				layoutData = new GridData();
				layoutData.widthHint = 150;
				textAPIPort.setLayoutData(layoutData);

				//				Label label = new Label(portsGroup, SWT.NONE);
				//				label.setText("Use '22' as default");
				//				layoutData = new GridData();
				//				label.setLayoutData(layoutData);

				labelPort = new Label(portsGroup, SWT.NONE);
				labelPort.setText("SSH Port:");
				layoutData = new GridData();
				labelPort.setLayoutData(layoutData);

				textSSHPort = new Text(portsGroup, SWT.SINGLE | SWT.BORDER);
				textSSHPort.setText("");
				textSSHPort.setMessage("22 as default");
				layoutData = new GridData();
				layoutData.widthHint = 150;
				textSSHPort.setLayoutData(layoutData);

			}


			//row test
			{
				Label spacer = new Label(group, SWT.NONE);
				spacer.setText("");
				layoutData = new GridData();
				spacer.setLayoutData(layoutData);

				buttonTest = new Button(group, SWT.PUSH);
				buttonTest.setText("Test connection settings");
				layoutData = new GridData();
				layoutData.horizontalSpan = 2;
				buttonTest.setLayoutData(layoutData);
			}

			{
				Label spacer = new Label(group, SWT.NONE);
				spacer.setText("");
				layoutData = new GridData();
				layoutData.horizontalSpan = 3;
				spacer.setLayoutData(layoutData);
				spacer = new Label(group, SWT.NONE);
				spacer.setText("");
				layoutData = new GridData();
				layoutData.horizontalSpan = 3;
				spacer.setLayoutData(layoutData);
			}
		}
		{
			Button useDocker = new Button(localParent,SWT.CHECK) ;
			GridData layoutData = new GridData();
			layoutData.horizontalSpan = 3;
			useDocker.setLayoutData(layoutData);

			useDocker.setText("Run Server locally using Docker.");
			useDocker.setSelection(localDockerMode);
			SelectionListener selectionListener = new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

					localDockerMode = useDocker.getSelection();
					useDocker.removeSelectionListener(this);
					createGroupCredentials(parent);



				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}
			};
			useDocker.addSelectionListener(selectionListener );

			if(localDockerMode) {

				Group dockerGroup = new Group(localParent, SWT.BORDER);
				dockerGroup.setText("Container Options");
				layoutData = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 3, 1);
				layoutData.horizontalSpan = 3;
				dockerGroup.setLayoutData(layoutData);
				dockerGroup.setLayout(new GridLayout(3,false));
				Button startContainer = new Button(dockerGroup, SWT.PUSH);
				startContainer.setText("Start Server Container");
				startContainer.setToolTipText("Start Server Container");
				try {
					boolean isRunninng = dockerHelper.isRunning() ;
					if(isRunninng)
						startContainer.setToolTipText("Container is running");
				} catch (NTryRestartEx e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				startContainer.setImage(SharedImages.RUN_EXC);
				startContainer.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub



						BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {

							public void run(boolean checkAgain) {
								// TODO Auto-generated method stub
								try {
									if(!dockerHelper.isRunning() ) {
										dockerHelper.startGpro();
										if(dockerHelper.isRunning())
											saveDokcerSettings();
									}
								} catch (NTryRestartEx e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									if(checkAgain) { 
										dockerHelper.removeContainer();
										run(false);

									} else {
										MessageDialog.openError(getShell(), "Error", "can not start docker container");

									}
								}

							}

							@Override
							public void run() {
								run(true);
							}
						});

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});

				Label spacer = new Label(dockerGroup, SWT.NONE);
				//spacer.setText("");
				layoutData = new GridData();
				layoutData.horizontalSpan = 2;
				spacer.setLayoutData(layoutData);


				buttonTest = new Button(dockerGroup, SWT.PUSH);
				buttonTest.setText("Test connection settings");
				layoutData = new GridData();
				layoutData.horizontalSpan = 2;
				buttonTest.setLayoutData(layoutData);


				spacer = new Label(dockerGroup, SWT.NONE);
				//spacer.setText("");
				layoutData = new GridData();
				layoutData.horizontalSpan = 2;
				spacer.setLayoutData(layoutData);
			}

			Label spacer = new Label(localParent, SWT.NONE);
			//spacer.setText("");
			layoutData = new GridData();
			layoutData.horizontalSpan = 3;
			spacer.setLayoutData(layoutData);

			if(!dockerHelper.dockerInstalled) {
				useDocker.setEnabled(false);
				spacer.setText("To use this feature you need to install docker first.");
			}

		}



		initForm();

		addListeners();

		if(!localDockerMode)
			textEmail.setFocus();

		parent.layout(true);
		parent.computeSize(SWT.DEFAULT,SWT.DEFAULT);
		return localParent;
	}

	UserLoginInfo currentLoginInfo = null; 

	private void initForm() {
		//		String preferenceEmail = PreferenceManager.getInstance().get(Constants.EMAIL, "");
		//		String preferenceHost = PreferenceManager.getInstance().get(Constants.SSH_HOST, "");
		//		String preferencePort = PreferenceManager.getInstance().get(Constants.SSH_PORT, "22");
		//		String preferenceUser = PreferenceManager.getInstance().get(Constants.SSH_USER, "");
		//		String preferencePassword = PreferenceManager.getInstance().get(Constants.SSH_PASSWORD, "");
		UserLoginInfo loginInfo = usersService.getActiveLogin();
		currentLoginInfo = loginInfo;
		if(localDockerMode) 
		{

			// Default value
			preferenceEmail = "g_user@localhost.com";
			preferenceHost = "localhost";
			preferenceSSHPort = RemoteServerInfo.DEFAULT_SSH_PORT;
			preferenceAPIPort = RemoteServerInfo.DEFAULT_API_PORT;
			preferenceUser = "g_user" ;
			preferencePassword = "g_user";
		} else
		{


			if(loginInfo != null) {
				preferenceEmail = loginInfo.getEmailAddress();
				preferenceHost = loginInfo.getHostInfo().getServerURL();
				preferenceSSHPort = loginInfo.getHostInfo().getSshPort();
				preferenceAPIPort = loginInfo.getHostInfo().getHttpPort();
				preferenceUser = loginInfo.getUserName() ;
				preferencePassword = loginInfo.getPassword();

			}




			if (preferenceEmail.isEmpty() == false) {
				textEmail.setText(preferenceEmail);
			}

			if (preferenceHost.isEmpty() == false) {
				textHost.setText(preferenceHost);
			}
			else {
				textHost.setText("");
			}

			if (preferenceSSHPort != RemoteServerInfo.DEFAULT_SSH_PORT ) {
				textSSHPort.setText(Integer.toString(preferenceSSHPort));
			}
			//			else {
			//				// textSSHPort.setText("22");
			//			}

			if (preferenceAPIPort != RemoteServerInfo.DEFAULT_API_PORT ) {
				textAPIPort.setText(Integer.toString(preferenceAPIPort));
			}
			//			else {
			//				// textAPIPort.setText("80");
			//			}

			if (preferenceUser.isEmpty() == false) {
				textUser.setText(preferenceUser);
			}

			if (preferencePassword.isEmpty() == false) {
				textPassword.setText(preferencePassword);
			}
		}

	}


	protected void okPressed() {
		updateInputs();

		if(localDockerMode) 
		{
			// need to start docker before we save 
			IRunnableWithProgress runnable = new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					// TODO Auto-generated method stub
					monitor.beginTask("Starting Docker Container", IProgressMonitor.UNKNOWN);

					boolean started = DockerHelper.checkStart(getShell().getDisplay(),true);
					if(!started)
						throw new InvocationTargetException(new Exception("Can not start docker container"));
					monitor.done();
				}
			}; 
			ProgressMonitorDialog pm = new ProgressMonitorDialog(getShell());
			try {
				pm.run(true, false, runnable);
			} catch (InvocationTargetException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MessageDialog.openWarning(getShell(), "Save configuration", "Error saving configuration: Can not start server docker container" );
				return;
			}


			//usersService.getUserManager().put(USE_LOCAL_GPRO_PROP, TRUE_CNST);
			//usersService.getUserManager().savePreferences();
		}
		else {
			updateInputs();
		}

		// Save ssh preferences
		boolean connectionOk =  false;
		try {


			// TODO :: Need migration
			//			connectionOk = usersService.getUserManager().changeConnection(textUser.getText(),textHost.getText() , textPort.getText(), textPassword.getText());
			UserLoginInfo loginInfo = new UserLoginInfo();
			loginInfo.setPassword(preferencePassword);
			loginInfo.setUserName(preferenceUser);
			loginInfo.setHostInfo(new RemoteServerInfo(preferenceHost, preferenceHost));
			loginInfo.getHostInfo().setScheme(RemoteServerInfo.DEFAULT_FTP_SCHEME);
			loginInfo.getHostInfo().setSshPort(preferenceSSHPort);
			loginInfo.getHostInfo().setFtpPort(preferenceSSHPort);
			loginInfo.getHostInfo().setHttpPort(preferenceAPIPort);
			loginInfo.setEmailAddress(preferenceEmail);
			connectionOk = usersService.changeLogin(loginInfo);
			if(connectionOk) {
				UserManager userManager = usersService.getUserManager();
				if(userManager.get(Constants.SERVER_DEMO_MODE, Constants.DEMO_MODE_FALSE).equals(Constants.DEMO_MODE_TRUE)) {
					MessageDialog.openInformation(getShell(), "DEMO MODE", "Please, note that this software is running in demo mode. Some functions may be disabled.");
				}
			}
			else {
				MessageDialog.openWarning(getShell(), "Save configuration", "Error saving configuration: Invalid Login" );
				return;
			}
			//			PreferenceManager.getInstance().put(Constants.EMAIL, textEmail.getText());

		} 
		catch (Exception e1) {
			e1.printStackTrace();
			connectionOk = false;
			MessageDialog.openWarning(getShell(), "Save configuration", "Error saving configuration: " + e1.getMessage());
			return;
		}


		if(connectionOk) {
			if(!localDockerMode) {
				usersService.getUserManager().put(USE_LOCAL_GPRO_PROP, FALSE_CNST);
				usersService.getUserManager().savePreferences();
			}
			else {
				usersService.getUserManager().put(USE_LOCAL_GPRO_PROP, TRUE_CNST);
				usersService.getUserManager().savePreferences();

			}
		}


		// Fetch and save database connection settings
		//		try {
		//			try {
		//				ServerSettingsUtils.fetchDatabaseSettings(textHost.getText(), textUser.getText(), PipelineCommander.md5Hash(textPassword.getText()));
		//				System.out.println("bin_path:" + ServerSettingsUtils.getBinPath());
		//				System.out.println("demo mode: " + PreferenceManager.getInstance().get(Constants.SERVER_DEMO_MODE, Constants.DEMO_MODE_FALSE));
		//				
		//				if (PreferenceManager.getInstance().get(Constants.SERVER_DEMO_MODE, Constants.DEMO_MODE_FALSE).equals(Constants.DEMO_MODE_TRUE)) {
		//					MessageDialog.openInformation(getShell(), "DEMO MODE", "Please, note that this software is running in demo mode. Some functions may be disabled.");
		//				}
		//			}
		//			catch (NoSuchAlgorithmException e) {
		//				MessageDialog.openError(getShell(), "Pipeline database settings", "Cannot retrieve database settings");
		//			}
		//		} 
		//		catch (IOException e) {
		//			MessageDialog.openWarning(getShell(), "Pipeline settings", "Error: cannot fetch database connection settings");
		//		}

		super.okPressed();
	}

	private void savePreferences() throws BackingStoreException {
		//		PreferenceManager.getInstance().put(Constants.EMAIL, textEmail.getText());

		// TODO :: need migration
		//		UserManager.getDefault().changeConnection(textUser.getText(),textHost.getText() , textPort.getText(), textPassword.getText());
	}


	private void saveDokcerSettings() {
		if(localDockerMode) {

			usersService.getUserManager().put(USE_LOCAL_GPRO_PROP, TRUE_CNST);
			usersService.getUserManager().savePreferences();
		}
		// Save ssh preferences
		boolean connectionOk =  false;
		try {


			// TODO :: Need migration
			//			connectionOk = usersService.getUserManager().changeConnection(textUser.getText(),textHost.getText() , textPort.getText(), textPassword.getText());
			UserLoginInfo loginInfo = getNewLogin();
			
			connectionOk = usersService.changeLogin(loginInfo);
			if(connectionOk) {
				UserManager userManager = usersService.getUserManager();
				if(userManager.get(Constants.SERVER_DEMO_MODE, Constants.DEMO_MODE_FALSE).equals(Constants.DEMO_MODE_TRUE)) {
					MessageDialog.openInformation(getShell(), "DEMO MODE", "Please, note that this software is running in demo mode. Some functions may be disabled.");
				}
			}
			else {
				MessageDialog.openWarning(getShell(), "Save configuration", "Error saving configuration: Invalid Login" );
				return;
			}
			//			PreferenceManager.getInstance().put(Constants.EMAIL, textEmail.getText());

		} 
		catch (Exception e1) {
			e1.printStackTrace();
			connectionOk = false;
			MessageDialog.openWarning(getShell(), "Save configuration", "Error saving configuration: " + e1.getMessage());
			return;
		}

	}

	/* ************************* listeners ************************* */

	private void addListeners() {


		buttonTest.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				getButton(OK).setEnabled(true);
				//				try {
				//					savePreferences();
				//				} 
				//				catch (BackingStoreException e1) {
				//					e1.printStackTrace();
				//					MessageDialog.openWarning(getShell(), "Save configuration", "Error saving configuration: " + e1.getMessage());
				//				}
				ArrayList<ServiceStatus>  servicesToCheck = new ArrayList<ServiceStatus>();
				if(localDockerMode) {
					servicesToCheck.add(new ServiceStatus() {
						{
							this.name = "Docker Started";
						}
						@Override
						public boolean runCheck() {
							// TODO Auto-generated method stub
							return DockerHelper.checkStart(null, false);
						}
					});
				}
				
				updateInputs();
				servicesToCheck.addAll(usersService.getUserManager().getCheckStatus(getNewLogin()));
				OnlineCheckDialog onlineDialog = new OnlineCheckDialog(getParentShell(),servicesToCheck );
				
				onlineDialog.open( );
				boolean res = onlineDialog.getCheckStatus();
				// boolean res = usersService.getUserManager().testConnection(preferenceHost, preferenceSSHPort, preferenceUser, preferencePassword);
				if(localDockerMode) {
					if (res) {
						MessageDialog.openInformation(getShell(), 
								"Pipeline connection settings", "Connected successfully to Local Server ");
					}
					else {
						MessageDialog.openError(getShell(), 
								"Pipeline connection settings", "Cannot connect to Localhost");
					}
				}
				else {
					if (res) {
						MessageDialog.openInformation(getShell(), 
								"Pipeline connection settings", "Connected successfully to " + textHost.getText());
					}
					else {
						MessageDialog.openError(getShell(), 
								"Pipeline connection settings", "Cannot connect to " + textHost.getText());
					}
				}

			}
		});

		if(!localDockerMode) {


			textEmail.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validate();
				}
			});

			textHost.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validate();
				}
			});

			textUser.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validate();
				}
			});

			textPassword.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validate();
				}
			});
		}
	}



	/* ************************* validation ************************* */


	private boolean validate() {
		setErrorMessage(null);
		getButton(OK).setEnabled(false);

		if(!textEmail.isDisposed()) {
			if ( textEmail.getText().isEmpty()) {
				setErrorMessage("E-mail address is required");
				return false;
			}
			else
			{
				if (!EmailValidator.validateEmail(textEmail.getText()))
				{
					setErrorMessage("Invalid e-mail address");
					return false;
				}
			}
		}

		if (!textHost.isDisposed()) {
			if (textHost.getText().isEmpty()) {
				setErrorMessage("Host name is required");
				return false;
			}
		}
		
		if(!textUser.isDisposed()) {
			if (textUser.getText().isEmpty()) {
				setErrorMessage("User name is required");
				return false;
			}
		}
		
		if(!textPassword.isDisposed()) {
			if (textPassword.getText().isEmpty()) {
				setErrorMessage("Password is required");
				return false;
			}
		}
		

		getButton(OK).setEnabled(true);
		return true;
	}

	private void updateInputs() {
		// TODO :: make sure it is valid
		if(validate()) {
			if(localDockerMode) 
			{
				preferenceEmail = "g_user@localhost.com";
				preferenceHost = "localhost";
				preferenceSSHPort = RemoteServerInfo.DEFAULT_SSH_PORT;
				preferenceAPIPort = RemoteServerInfo.DEFAULT_API_PORT;
				preferenceUser = "g_user" ;
				preferencePassword = "g_user";
			}
			else {
				if (textHost.getText().isEmpty()) {
					MessageDialog.openWarning(getShell(), "Pipeline settings", "Host cannot be empty");
					return;
				}
				if (textUser.getText().isEmpty()) {
					MessageDialog.openWarning(getShell(), "Pipeline settings", "Username cannot be empty");
					return;

				}
				if (textPassword.getText().isEmpty()) {
					MessageDialog.openWarning(getShell(), "Pipeline settings", "Password cannot be empty");
					return;
				}

				preferenceEmail = textEmail.getText();
				preferenceHost = textHost.getText();
				//				preferenceSSHPort = Integer.parseInt(textSSHPort.getText());
				preferenceUser = textUser.getText();
				preferencePassword = textPassword.getText();

				try {
					preferenceSSHPort = Integer.parseInt(textSSHPort.getText());
				} catch (Exception e) {
					preferenceSSHPort = RemoteServerInfo.DEFAULT_SSH_PORT;
					textSSHPort.setText("");
				}
				try {
					preferenceAPIPort = Integer.parseInt(textAPIPort.getText());
				} catch (Exception e) {
					preferenceAPIPort = RemoteServerInfo.DEFAULT_API_PORT;
				}

			}
		}


	}

	private UserLoginInfo getNewLogin() {
		UserLoginInfo loginInfo = new UserLoginInfo();
		loginInfo.setPassword(preferencePassword);
		loginInfo.setUserName(preferenceUser);
		loginInfo.setHostInfo(new RemoteServerInfo(preferenceHost, preferenceHost));
		loginInfo.getHostInfo().setScheme(RemoteServerInfo.DEFAULT_FTP_SCHEME);
		loginInfo.getHostInfo().setSshPort(preferenceSSHPort);
		loginInfo.getHostInfo().setFtpPort(preferenceSSHPort);
		loginInfo.getHostInfo().setHttpPort(preferenceAPIPort);
		loginInfo.setEmailAddress(preferenceEmail);
		return loginInfo;
	}


	public void setUsersService(IUsersService usersServicer) {
		this.usersService =usersServicer;

	}


}
