package eclipse4haskell;

//import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import eclipse4haskell.editors.HSEditor;

public class OutLine extends ViewPart
{
	private TreeViewer viewer;
	private HSEditor editor;

	public OutLine()
	{
		super();
	}

	public OutLine(HSEditor edt)
	{
		super();
		editor = edt;
	}

	@Override
	public void createPartControl(Composite parent)
	{
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new TreeNodeContentProvider());
		viewer.setLabelProvider(new OLLabelProvider());
		//親子を指定したTreeNodeをsetInputに渡すと
		//ビューが生成される.
		viewer.setInput(makeData());
		getSite().setSelectionProvider(viewer);
	}

	@Override
	public void setFocus()
	{
		viewer.getControl().setFocus();
	}

	public Object getAdapter(Class cls)
	{
		if(cls.isInstance(this))
		{
			return this;
		}
		else
		{
			return super.getAdapter(cls);
		}
	}

	private TreeNode[] makeData()
	{
		//適当にサンプルデータを用意
		//TreeNode temp = null;
		TreeNode[] tree = new TreeNode[2];
		tree[0] = new TreeNode(new HSElement("func1"));
		tree[1] = new TreeNode(new HSElement("func2"));
		TreeNode[] c1 = new TreeNode[1];
		TreeNode[] c2 = new TreeNode[2];
		c1[0] = new TreeNode(new HSElement("func1-1"));
		c2[0] = new TreeNode(new HSElement("func2-1"));
		c2[1] = new TreeNode(new HSElement("func2-2"));
		c1[0].setParent(tree[0]);
		c2[0].setParent(tree[1]);
		c2[1].setParent(tree[1]);
		tree[0].setChildren(c1);
		tree[1].setChildren(c2);
		return tree;
	}

}
