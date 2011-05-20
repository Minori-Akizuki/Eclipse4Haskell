package eclipse4haskell;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import eclipse4haskell.editors.HSEditor;

public class HSOutlinePage extends ContentOutlinePage
{
	private HSEditor editor;
	private TreeNode[] hsInput;

	public HSOutlinePage(HSEditor ed)
	{
		editor = ed;
	}

	public void update()
	{
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		hsInput = new HSParser().parse(doc.get());
		getTreeViewer().setInput(hsInput);
		getTreeViewer().refresh();
	}

	public void createControl(Composite parent)
	{
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new TreeNodeContentProvider());
		viewer.setLabelProvider(new OLLabelProvider());
		viewer.addSelectionChangedListener(
			new ISelectionChangedListener()
			{
				public void selectionChanged(SelectionChangedEvent event)
				{
					IStructuredSelection sel = (IStructuredSelection) event.getSelection();
					Object element = sel.getFirstElement();
					if(element instanceof TreeNode)
					{
						int offset = ((HSElement)((TreeNode)element).getValue()).getOffset();
						int end = ((HSElement)((TreeNode)element).getValue()).getEnd();
						editor.selectAndReveal(offset, end-offset);
					}
				}
			}
		);
		update();
	}

}
