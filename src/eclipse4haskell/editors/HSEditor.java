package eclipse4haskell.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import eclipse4haskell.HSOutlinePage;
import eclipse4haskell.OutLine;

public class HSEditor extends TextEditor
{

	public static final String ASSIST_ACTIN_ID = "HSEditor.Assist";
	public static String editingFile;

	private ColorManager colorManager;
	private HSOutlinePage outline;
	public HSEditor()
	{
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new HSConfiguration(colorManager));
		setDocumentProvider(new HSDocumentProvider());
	}

	public Object getAdapter(Class adapter)
	{
		if(IContentOutlinePage.class.equals(adapter))
		{
			if(outline==null)
			{
				outline = new HSOutlinePage(this);
			}
			return outline;
		}
		return super.getAdapter(adapter);
	}

	public void doSave(IProgressMonitor ipm)
	{
		super.doSave(ipm);
		outline.update();
	}

	public void doSaveAs()
	{
		super.doSaveAs();
		outline.update();
	}

	public void dispose()
	{
		colorManager.dispose();
		super.dispose();
	}

}
