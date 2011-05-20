package eclipse4haskell.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class HSDocumentProvider extends FileDocumentProvider
{

	protected IDocument createDocument(Object element) throws CoreException
	{
		IDocument document = super.createDocument(element);
		if (document != null)
		{
			IDocumentPartitioner partitioner =
				new FastPartitioner(
						new HSPartitionScanner(),
						new String[]{ HSPartitionScanner.HS_COMMENT_SL, HSPartitionScanner.HS_COMMENT_ML }
				);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}