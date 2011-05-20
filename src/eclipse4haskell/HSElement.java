package eclipse4haskell;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

//ビュー用のクラス
public class HSElement
{
	public static enum Icon
	{
		FUNC (PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW)),
		IMPORT (PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK)),
		DATA (PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK)),
		ELEM (PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT));

		private Image img;

		Icon(Image _img)
		{
			img = _img;
		}

		Image toImage(){ return img; }
	}
	private String name;
	private String type;
	private int offsetSt;
	private int offsetEd;
	private Image icon;

	HSElement(String n)
	{
		name = n;
		offsetEd = offsetSt = 0;
		icon = null;
		type = "";
	}

	HSElement(String n, String a)
	{
		this(n);
	}


	public HSElement(String n, Icon i, int c, int e)
	{
		this(n,i,c,e,"");
	}

	public HSElement(String n, Icon i, int c, int e, String t)
	{
		type = t;
		icon = i.toImage();
		name = n;
		offsetSt = c;
		offsetEd = e;
	}

	public HSElement(String n, String a,int c, int e)
	{
		name = n;
		offsetSt = c;
		offsetEd = e;
		type = "";
		if(a=="function")
		{
			icon = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW);
		}
		else if(a == "import")
		{
			icon = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK);
		}
		else if(a=="data")
		{
			icon = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
		}
		else if(a=="elem")
		{
			icon = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
		else
		{
			icon = null;
		}
	}

	//toStringで帰ってくる値が
	//ビューに表示される.
	public String toString()
	{
		if (type.equals(""))
		{
			return name;
		}
		else
			return name + " :: " + type;
	}

	public int getOffset()
	{
		return offsetSt;
	}

	public int getEnd()
	{
		return offsetEd;
	}

	public Image toGetIcon()
	{
		return icon;
	}
}
