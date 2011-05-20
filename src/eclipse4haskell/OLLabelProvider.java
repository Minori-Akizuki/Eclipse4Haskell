package eclipse4haskell;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

//TreeNodeの中にある物をどうやって文字列
//(とアイコン)にしたらいいのかを指定する為のクラス

public class OLLabelProvider extends LabelProvider
{
	public String getText(Object obj)
	{
		if( obj instanceof TreeNode)
		{
			return ((HSElement)((TreeNode)obj).getValue()).toString();
		}
		return "No implementation";
	}

	public Image getImage(Object obj)
	{
		if( obj instanceof TreeNode)
		{
			return ((HSElement)((TreeNode)obj).getValue()).toGetIcon();
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}
