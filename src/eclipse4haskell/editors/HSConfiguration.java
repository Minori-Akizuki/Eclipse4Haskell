package eclipse4haskell.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class HSConfiguration extends SourceViewerConfiguration
{
	private HSDoubleClickStrategy doubleClickStrategy;
	private HSCommentScanner tagScanner;
	private HSScanner scanner;
	private ColorManager colorManager;

	public HSConfiguration(ColorManager colorManager)
	{
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
	{
		return new String[]{
			IDocument.DEFAULT_CONTENT_TYPE,
			HSPartitionScanner.HS_COMMENT_ML,
			HSPartitionScanner.HS_COMMENT_SL
			};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new HSDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected HSScanner getHSScanner() {
		if (scanner == null) {
			scanner = new HSScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IHSColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected HSCommentScanner getHSSLCommentScanner() {
		if (tagScanner == null) {
			tagScanner = new HSCommentScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IHSColorConstants.HS_COMMENT_SL))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getHSSLCommentScanner());
		reconciler.setDamager(dr, HSPartitionScanner.HS_COMMENT_SL);
		reconciler.setRepairer(dr, HSPartitionScanner.HS_COMMENT_SL);

		dr = new DefaultDamagerRepairer(getHSScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IHSColorConstants.HS_COMMENT_ML)));
		reconciler.setDamager(ndr, HSPartitionScanner.HS_COMMENT_ML);
		reconciler.setRepairer(ndr, HSPartitionScanner.HS_COMMENT_ML);

		return reconciler;
	}

	public IContentAssistant getContentAssistant(ISourceViewer sViewer)
	{
		ContentAssistant assistant = new ContentAssistant();

		HSContentAssistProcessor processor = new HSContentAssistProcessor();
		assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
		assistant.install(sViewer);

		return assistant;
	}

}