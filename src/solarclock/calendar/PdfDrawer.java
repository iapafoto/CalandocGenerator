package solarclock.calendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * This class helps to generate Calendar for given Year.
 *
 * @author dsahu1
 *
 */
public class PdfDrawer extends JPanel {


    int width, height;
    int nbPage;
    
    final PDDocument document;
    final PDFRenderer pdfRenderer;

//                PDFRenderer pdfRenderer = new PDFRenderer(document);
//                pdfRenderer.renderPageToGraphics(0, g2, scale);
    
    public PdfDrawer(File file) throws IOException {
        super();  
            document = Loader.loadPDF(file);
            pdfRenderer = new PDFRenderer(document);

            PDPageTree pageTree = document.getDocumentCatalog().getPages();
            nbPage = pageTree.getCount();
            for (PDPage page : pageTree) {
                PDRectangle rec = page.getMediaBox();
                width = (int)rec.getWidth();
                height = (int)rec.getHeight();
                break;
            }
//                pdfRenderer.renderPageToGraphics(0, g2, scale);
    }

    public static JComponent generateCalendarUI(File file) {
        PdfDrawer textArea1 = null;
        try {
            textArea1 = new PdfDrawer(file);
        } catch (IOException ex) {
            Logger.getLogger(PdfDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return textArea1;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) width + 20, (int) (height + 20) * nbPage);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension((int) width + 20, (int) (height + 20) * nbPage);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension((int) width + 100, (int) (height + 20) * nbPage);
    }

    public void paintPage(Graphics2D g2, PDPage page, int pageId) {
        try {
            pdfRenderer.renderPageToGraphics(pageId, g2, 1f);

            drawRect(g2, page.getMediaBox(), Color.blue, 2);
            drawRect(g2, page.getBleedBox(), Color.green, 1);
            drawRect(g2, page.getTrimBox(), Color.red, 2);
            drawRect(g2, page.getCropBox(), Color.orange, 1);
            drawRect(g2, page.getArtBox(), Color.black, 1);
            
        } catch (IOException ex) {
            Logger.getLogger(PdfDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method helps to generate Calendar.
     *
     * @param year
     * @param args
     * @return
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);

        g2.setColor(Color.lightGray);
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));

        g2.translate(10, 10);

      //  PDPageTree pagetree = document.getDocumentCatalog().getPages();
        for (int pageId = 0; pageId < nbPage; pageId++) {
            PDPage page = document.getPage(pageId);
            
     //       g2.setClip(new Rectangle2D.Double(0, 0,  page.getMediaBox().getWidth(), page.getMediaBox().getHeight()));
            paintPage(g2, page, pageId);
            g2.translate(0, page.getMediaBox().getHeight() + 20);
        }
    }


    private void drawRect(Graphics2D g2, PDRectangle mediaBox, Color c, float ep) {
        if (mediaBox != null) {
            g2.setColor(c);
            g2.setStroke(new BasicStroke(ep));
            g2.draw(new Rectangle2D.Double(mediaBox.getLowerLeftX(), mediaBox.getLowerLeftY(),mediaBox.getWidth(), mediaBox.getHeight()));
        }
    }
    
    
}
