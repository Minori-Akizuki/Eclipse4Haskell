package eclipse4haskell.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	public SampleAction()
	{

	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		String path = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();

		/*
		Boolean a = MessageDialog.openQuestion(
			window.getShell(),
			"Eclipse4Haskell",
			"read only this file?" );
		//*/
//*
		try {
			InputStream st = Runtime.getRuntime().exec(new String[] {"echo","abs"} ).getInputStream();
			StringBuffer sb = new StringBuffer();
			int c = st.read();
			while(c!=-1)
			{
				sb.append(c);
				c = st.read();
			}

			MessageConsole console = new MessageConsole("Test", null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
			MessageConsoleStream consoleStream = console.newMessageStream();
			consoleStream.print(sb.toString());
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
//*/
	}

	/**
	 * Selection in the workbench has been changed. We
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose()
	{
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window)
	{
		this.window = window;
	}
}