package solarclock.calendar;

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import static solarclock.calendar.CalendarCalculator.getImage;
import static solarclock.calendar.CalendarDrawer2023.findTransformToEnterShapeInRect;
import static solarclock.calendar.CalendarDrawer2023.svgCross;
import solarclock.calendar.tools.DrawTools;

/**
 *
 * @author sebastien.durand
 */
public class PdfWriter {

    static boolean IS_TEST_MODE = false;

    static final double //
            lostBorderMM = 3,
            wPdf = 841.8898, hPdf = 595.27563;

    static final public double //
            wPdfmm = 297., hPdfmm = 210.,
            mmToPx = wPdf / wPdfmm, // 2.83480453972;
            pxTomm = wPdfmm / wPdf,
            wh_ratio = wPdfmm / hPdfmm;

    static final float //
            bleedPdf = (float) (lostBorderMM * mmToPx);  // 3mm de bord perdu

    // Dessin (pour conserver les polices
    static final public double width = 1124, // 297*mmToPx; =  842   /1.33
            height = width / wh_ratio, // 800; // 210*mmToPx; =  595
            bleedBorder = bleedPdf * (width / wPdf);

    final static String pathPinSvg = "m 213.285,0 h -0.608 C 139.114,0 79.268,59.826 79.268,133.361 c 0,48.202 21.952,111.817 65.246,189.081 32.098,57.281 64.646,101.152 64.972,101.588 0.906,1.217 2.334,1.934 3.847,1.934 0.043,0 0.087,0 0.13,-0.002 1.561,-0.043 3.002,-0.842 3.868,-2.143 0.321,-0.486 32.637,-49.287 64.517,-108.976 43.03,-80.563 64.848,-141.624 64.848,-181.482 C 346.693,59.825 286.846,0 213.285,0 Z";
    static Shape pathPin = SimpleSvgTools.svgPathToPath2D(pathPinSvg);

    final static String[] NAMES = {
        "Volcans d'Auvèrnha",
        "Calancas",
        "Canal del Miègjorn",
        "Ciutat de Carcassona",
        "Colonjas la Roja",
        "Ciutat episcopala de Condòm",
        "Valada de la Dordonha",
        "Lorda",
        "Mercantor",
        "Castèl de Montsegur",
        "Arenas de Nimes",
        "Pic de Mieidia"
    };
        
//        "Volcans d’Auvergne", "Calanques",
//        "Canal du midi", "Cité de Carcassonne",
//        "Collonges la rouge", "⁠Cité épiscopale de Condom",
//        "Vallée de la Dordogne", "Lourdes",
//        "Mercantour", "⁠Château de Montsègur",
//        "⁠Arènes de Nîmes", "Pic du midi"};
    
    final static String[] NAMES_IMG = {
        "Auvergne.png", "Calanques.jpg",
        "CanalDuMidi.png", "Carcassonne.png",
        "CollongesLaRouge.png", "Condom.jpg",
        "Dordogne.png", "Lourdes.png",
        "Mercantour.png", "Montsegur.png",
        "Nimes.png", "PicDuMidi.png"};
    
    static int nb = 0;
    static final int //
            Auvergne = nb++,
            Calanques = nb++,
            CanalDuMidi = nb++,
            Carcassonne = nb++,
            Collonges = nb++,
            Condom = nb++,
            Dordogne = nb++,
            Lourdes = nb++,
            Mercantour = nb++,
            Montsegur = nb++,
            Nimes = nb++,
            PicDuMidi = nb++;
    
//    static final int[] LOC_ID = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    static final Map<Integer,Point> mapLocation = new HashMap<>();
    static {
        mapLocation.put(Auvergne,new Point(415, 180));
        mapLocation.put(Calanques, new Point(625, 440));
        mapLocation.put(CanalDuMidi, new Point(310, 430));
        mapLocation.put(Carcassonne, new Point(370, 450));
        mapLocation.put(Collonges, new Point(285, 320));
        mapLocation.put(Condom, new Point(210, 375));
        mapLocation.put(Dordogne, new Point(230, 330));
        mapLocation.put(Lourdes, new Point(180, 460));
        mapLocation.put(Mercantour, new Point(740, 335));
        mapLocation.put(Montsegur, new Point(325, 490));
        mapLocation.put(Nimes, new Point(525, 375));
        mapLocation.put(PicDuMidi, new Point(210, 480));
    }


            
    static final int[] PAGE_ORDER = {
        Nimes,
        CanalDuMidi,
        Auvergne,
        Lourdes,
        Mercantour,
        Collonges,
        PicDuMidi,
        Montsegur,
        Carcassonne,
        Calanques,
        Dordogne,
        Condom
    };
    
  
    public static void directoryToPdf(File folder) throws IOException {
        try (PDDocument fullDoc = new PDDocument()) {
            PDDocumentInformation pdd = fullDoc.getDocumentInformation();
            pdd.setTitle("Calendoc 2023");

            File[] files = folder.listFiles();
            Arrays.sort(files);

            for (final File file : files) {
                if (!file.isDirectory() && file.getAbsolutePath().endsWith(".pdf")) {
                    PDDocument document = Loader.loadPDF(file);
                    PDPage page = document.getDocumentCatalog().getPages().get(0);
                    fullDoc.addPage(page);
                }
            }
            fullDoc.save(new File("C:\\Users\\sebastien.durand\\Desktop\\calendoc_2022.pdf"));
        } catch (Exception e) {

        }
    }

    public static void testPdf() throws IOException {
        //try (   InputStream resource = getClass().getResourceAsStream("C:\\Users\\sebastien.durand\\Desktop\\Calendoc\\10_Octòbre.pdf.pdf")  )
        //{
        File folder = new File("C:\\Users\\sebastien.durand\\Desktop\\Calendoc\\Trimbox");

        for (final File file : folder.listFiles()) {
            if (!file.isDirectory() && file.getAbsolutePath().endsWith(".pdf")) {
                PDDocument document = Loader.loadPDF(file);
                PDPage page = document.getDocumentCatalog().getPages().get(0);

//                PDFRenderer pdfRenderer = new PDFRenderer(document);
//                pdfRenderer.renderPageToGraphics(0, g2, scale);
                PDRectangle rec = page.getTrimBox();
                if (rec == null) {
                    page.setTrimBox(new PDRectangle(bleedPdf, bleedPdf, (float) wPdf, (float) hPdf));
                }
                /*
        PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.PREPEND, false, false);
        Matrix matrix = Matrix.getRotateInstance(Math.toRadians(45), 0, 0);
        cs.transform(matrix);
        cs.close();

        PDRectangle cropBox = page.getCropBox();
        float cx = (cropBox.getLowerLeftX() + cropBox.getUpperRightX()) / 2;
        float cy = (cropBox.getLowerLeftY() + cropBox.getUpperRightY()) / 2;
        Point2D.Float newC = matrix.transformPoint(cx, cy);
        float tx = (float) newC.getX() - cx;
        float ty = (float) newC.getY() - cy;
        page.setCropBox(new PDRectangle(cropBox.getLowerLeftX() + tx, cropBox.getLowerLeftY() + ty, cropBox.getWidth(), cropBox.getHeight()));
        PDRectangle mediaBox = page.getMediaBox();
        page.setMediaBox(new PDRectangle(mediaBox.getLowerLeftX() + tx, mediaBox.getLowerLeftY() + ty, mediaBox.getWidth(), mediaBox.getHeight()));
                 */
            }
        }
    }

    static protected PDPage createPage(double w, double h) {
        final PDPage page = new PDPage(new PDRectangle((float) w, (float) h));
        page.setMediaBox(new PDRectangle((float) w, (float) h));
        //  page.setBleedBox(new PDRectangle(bleedPdf,bleedPdf,wPdf,hPdf));
        page.setTrimBox(new PDRectangle(bleedPdf, bleedPdf, (float) wPdf, (float) hPdf));
        return page;
    }

    static public PDPage defaultToPdfPage(PDDocument document) throws IOException {
        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        int ww = (int) (width + 2 * bleedBorder);
        int hh = (int) (height + 2 * bleedBorder);

        PDPage page = createPage(w, h);
        PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));
        g2.scale(wPdf / width, hPdf / height);
        g2.setColor(Color.gray); //new Color(0xFFC687BB)); //new Color(121, 28, 15));
        g2.fillRect(0, 0, ww, hh);

        int r = 200;
        g2.setColor(new Color(0xFFB77AB1).darker()); // yellow
        AffineTransform af = findTransformToEnterShapeInRect(svgCross.getBounds2D(), new Rectangle2D.Double((int) (ww / 2 - r), (int) (hh / 2 - r), (int) (r * 2), (int) (r * 2)));

        Shape path = af.createTransformedShape(svgCross);
        CalendarDrawer2025.drawNeon(g2, Color.magenta, Color.magenta, path);

        g2.fill(af.createTransformedShape(svgCross));
        g2.dispose();
        PDFormXObject xform = g2.getXFormObject();

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawForm(xform);
        contentStream.close();
        return page;
    }

    static public PDPage mementoDocToPdfPage(PDDocument document) throws IOException {
        String txtTitle = "Mémento d’Òc";
        String[] lstTxt = {
            "Il y a un accent tonique, comme en italien ou en espagnol",
            "Le 'e' sans accent se prononce [é]",
            "Le 'o' se prononce[ou]",
            "Le 'ò' se prononce[o]",
            "Le 'a' se prononce comme un [o] lorsqu’il se trouve à la fin d’un mot",
            "Le 'v' se dit [b] en occitan",
            "Le 'j' et le 'g' suivis d’une voyelle se prononcent [dj] comme dans \"Djibouti\"",
            "Le 'ch' se prononce [tch] comme dans \"tcharer\"",
            "L’association de consonnes 'lh' se prononce[ill]",
            "L’association de consonnes 'nh' se prononce comme le [gn] de la langue française",
            "Le 'n' et le 'r' placés à la fin d’un mot ne se prononcent pas"};

        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        PDPage page = createPage(w, h);

        PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));
        g2.scale(wPdf / width, hPdf / height);

        int ww = (int) (width + 2 * bleedBorder);
        int hh = (int) (height + 2 * bleedBorder);

        g2.setColor(Color.white); //new Color(121, 28, 15));
        g2.fillRect(0, 0, ww, hh);
        int r = 80;
        g2.setColor(new Color(121, 28, 15).brighter()/*new Color(237, 208, 106)*/); // yellow
        AffineTransform af = findTransformToEnterShapeInRect(svgCross.getBounds2D(), new Rectangle2D.Double((int) (920 - r), (int) (256 - r), (int) (r * 2), (int) (r * 2)));
        g2.fill(af.createTransformedShape(svgCross));

        g2.setColor(Color.black);

        g2.setFont(new Font("ParmaPetit", Font.PLAIN, 70));
        DrawTools.drawStringCenter(g2, txtTitle, ww * .5, 88);

        g2.setFont(new Font("ParmaPetit", Font.PLAIN, 28));

        h = 200;
        for (String txt : lstTxt) {
            g2.drawString(txt, 110, (int) h);
            h += 50;
        }

        g2.dispose();
        PDFormXObject xform = g2.getXFormObject();

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawForm(xform);
        contentStream.close();
        return page;
    }

    /**
     * Dessine les zone interdites
     *
     * @param document
     * @param page
     * @param ispairPage
     * @return
     * @throws IOException
     */
    static public PDPage addBorderToPage(PDDocument document, PDPage page, boolean ispairPage) throws IOException {
        int nbLoop = 34;        // nombre d'anneaux standard
        double deltaMM = 8.47;  // intervals standard 3:1 
        double rMM = 2;         // rayon du trou standard

        PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1.f));
        g2.draw(new Rectangle2D.Double(bleedPdf, bleedPdf, wPdf, hPdf));
        double r = rMM * mmToPx;
        double dy = 5. * mmToPx;
        double cy1 = ispairPage ? dy : (hPdf - dy);
        double cy2 = ispairPage ? (hPdf - 6 * mmToPx) : 6 * mmToPx;
        double dxMM = ((wPdfmm - deltaMM * (nbLoop - 1.)) / 2.); // center
        for (int i = 0; i < nbLoop; i++) {
            double cx = (dxMM + i * deltaMM) * mmToPx;
            Ellipse2D ell = new Ellipse2D.Double(bleedPdf + cx - r, bleedPdf + cy1 - r, r + r, r + r);
            g2.fill(ell);
        }
        Ellipse2D ell = new Ellipse2D.Double(bleedPdf + wPdf / 2. - r, bleedPdf + cy2 - r, r + r, r + r);
        g2.fill(ell);
        g2.dispose();

        PDFormXObject xform = g2.getXFormObject();
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
        contentStream.drawForm(xform);
        contentStream.close();
        return page;
    }

    static public PDPage presentationToPdfPage(PDDocument document) throws IOException {
        String txtTitle = "Calandreta qu’esaquò";
        String[] lstTxt = {
            "Les Calandretas (en occitan, petite alouette) sont des établissements scolaires occitan,",
            "sous contrat avec l’éducation nationale.",
            "La première école Calandreta a vu le jour à Pau (Pyrénées-Atlantiques) en 1979.",
            "Le premier collège Calandreta ouvre en septembre 1997 à Lattes (Hérault).",
            " ",
            "Elles sont l'équivalent occitande Diwan pour les Bretons, Ikastola pour les Basques,",
            "La Bressole pour les Catalans ou ABCM-Zweisprachigkeit pour les Alsaciens-Mosellans.",
            " ",
            "En 2021, il existait 64 écoles, 4 collèges et 1 lycée,",
            "répartis sur 19 départements, pour 3937 élèves scolarisés.",
            " ",
            "Les Calandretas sont des écoles associatives laïques.",
            "La méthode pratiquée est celle de l'immersion linguistique précoce.",
            "L'enseignement dispensé suit les programmes de l'Éducation nationale.",
            "La pédagogie Calandreta, inspirée des techniques institutionelles accompagne",
            "l'enfant vers l’autonomie, le partage et la citoyenneté."
        };

        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        PDPage page = createPage(w, h);

        PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));
        g2.scale(wPdf / width, hPdf / height);

        int ww = (int) (width + 2 * bleedBorder);
        int hh = (int) (height + 2 * bleedBorder);

        g2.setColor(Color.white);
        g2.fillRect(0, 0, ww, hh);

        BufferedImage img = getImage("Calandrettes");
        g2.drawImage(img, 820, 200, 781 / 3, 1041 / 3, null); // yellow

        g2.setColor(Color.black);

        g2.setFont(new Font("ParmaPetit", Font.PLAIN, 70));
        DrawTools.drawStringCenter(g2, txtTitle, ww * .5, 80);

        g2.setFont(new Font("ParmaPetit", Font.PLAIN, 26));

        h = 170;
        for (String txt : lstTxt) {
            g2.drawString(txt, 70, (int) h);
            h += 36;
        }

        g2.dispose();
        PDFormXObject xform = g2.getXFormObject();

        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawForm(xform);
        contentStream.close();
        return page;
    }

    static public PDPage monthToPdfPage(PDDocument document, CalendarDrawer2025 drawer, int month) throws IOException {
        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        PDPage page = createPage(w, h);

        PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));
        g2.scale(wPdf / width, hPdf / height);

        double extraBorder = .5 * bleedBorder;
        double borderTot = bleedBorder + extraBorder;

        drawer.paintMonth(g2, month, width - 2. * extraBorder, height - 2. * extraBorder, borderTot);
        g2.dispose();
        PDFormXObject xform = g2.getXFormObject();
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawForm(xform);
        contentStream.close();
        return page;
    }



    public static void drawTextWithShadow(PdfBoxGraphics2D g2, Font font, String text, int x, int y, double angle, double stroke, Color colorFill, Color colorStroke) {
        g2.setFont(font);
        Path2D pathTxt = new Path2D.Double();
        DrawTools.drawString(pathTxt, g2.getFontRenderContext(), font, text, x, y, angle);
        CalendarDrawer2025.drawNeon(g2, colorFill, Color.white, pathTxt);
/*        g2.setColor(new Color(255, 255, 255, 64));
        g2.setStroke(new BasicStroke((float) stroke + 2));
        g2.draw(pathTxt);
        g2.setColor(colorFill);
        g2.fill(pathTxt);
        if (colorStroke != null) {
            g2.setColor(colorStroke);
            g2.setStroke(new BasicStroke((float) stroke));
            g2.draw(pathTxt);
        }
*/    }

    static public PDPage mapToPdfPage(PDDocument document, String path, String text, int x, int y, double angle, Font font, Color colorFill, Color colorStroke, double stroke) {
        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        PDPage page = createPage(w, h);
        try {
            // Ajout de texte qu dessus de l'image
            PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));
            
            Font fontNb = new Font("Impact", Font.PLAIN, 10);
            g2.setFont(fontNb);
            g2.setStroke(new BasicStroke(2f));

            for (int monthId = 0; monthId < 12; monthId++) {
                int pageId = PAGE_ORDER[monthId]; // id de nime en 1er
                Point pt = mapLocation.get(pageId);
                Shape sh = AffineTransform.getTranslateInstance(pt.x, pt.y).createTransformedShape(pathPin);
                g2.setColor(new Color(0, 0, 0, 64));
                g2.fillOval(pt.x - 3, pt.y - 2, 10, 4);
                g2.setColor(CalendarDrawer2025.monthColors[monthId]);
                g2.fill(sh);
                g2.setColor(Color.black);
                g2.draw(sh);
                DrawTools.drawStringCenter(g2, "" + (monthId + 1), pt.x, pt.y - 16);
            }
            
            g2.setStroke(new BasicStroke(1f));
            Font fontSmall = new Font("ParmaPetit", Font.BOLD, 13);

            fontNb = new Font("Impact", Font.PLAIN, 9);
            for (int monthId = 0; monthId < 12; monthId++) {
                int pageId = PAGE_ORDER[monthId]; // id de nime en 1er
                int px, py;
                if (monthId < 4) {
                    px = 20 + 160 * (monthId / 2);
                    py = 550 + 25 * (monthId % 2);
                } else {
                    px = 480 + 160 * ((monthId - 4) / 4);
                    py = 550 - 25 * 2 + 25 * ((monthId - 4) % 4);
                }
                Shape sh = AffineTransform.getScaleInstance(.7, .7).createTransformedShape(pathPin);
                sh = AffineTransform.getTranslateInstance(px, py).createTransformedShape(sh);
                g2.setColor(CalendarDrawer2025.monthColors[monthId]);
                g2.fill(sh);
                g2.setColor(Color.black);
                g2.draw(sh);
                g2.setFont(fontNb);
                DrawTools.drawStringCenter(g2, "" + (/*LOC_ID[*/monthId/*]*/ + 1), px, py - 16 * .7);
                g2.setFont(fontSmall);
                g2.drawString(NAMES[pageId], px + 12, py - 5);
            }
            g2.dispose();
            
            PDFormXObject xform = g2.getXFormObject();
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
            PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
            contentStream.drawImage(pdImage, 0, 0, (float) w, (float) h);
            contentStream.drawForm(xform);
            contentStream.close();
            return page;
        } catch (IOException e) {
            return null;
        }
    }

    static public PDPage pictureToPdfPage(PDDocument document, String path) {
        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        PDPage page = createPage(w, h);
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
            PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
            contentStream.drawImage(pdImage, 0, 0, (float) w, (float) h);
            contentStream.close();
            return page;
        } catch (IOException e) {
            return null;
        }
    }
    
    static public PDPage pictureToPdfPage(PDDocument document, String path, String text, int x, int y, double angle, Font font, Color colorFill, Color colorStroke, double stroke) {
        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        PDPage page = createPage(w, h);
        try {
            // Ajout de texte au dessus de l'image
            PdfBoxGraphics2D g2 = new PdfBoxGraphics2D(document, (float) (wPdf + bleedPdf * 2), (float) (hPdf + bleedPdf * 2));
            drawTextWithShadow(g2, font, text, x, y, angle, stroke, colorFill, colorStroke);
            g2.dispose();
            PDFormXObject xform = g2.getXFormObject();

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
            PDImageXObject pdImage = PDImageXObject.createFromFile(path, document);
            contentStream.drawImage(pdImage, 0, 0, (float) w, (float) h);
            contentStream.drawForm(xform);
            contentStream.close();
            return page;
        } catch (IOException e) {
            return null;
        }
    }
    
    static public PDPage pdfPictureToPdfPage(PDDocument document, String file) {
        double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;
        //     final PDPage pageNew = new PDPage(new PDRectangle((float) w, (float) h));
        //     pageNew.setMediaBox(new PDRectangle((float) w, (float) h));
        //  page.setBleedBox(new PDRectangle(bleedPdf,bleedPdf,wPdf,hPdf));
        //     pageNew.setTrimBox(new PDRectangle(bleedPdf, bleedPdf, (float) wPdf, (float) hPdf));

        try {
            // Ouverture du document pdf image
            PDDocument doc2 = Loader.loadPDF(new File(file));
            //      PDFRenderer pdfRend2 = new PDFRenderer(doc2);

            PDPageTree pageTree2 = document.getDocumentCatalog().getPages();
            for (PDPage page2 : pageTree2) {
                PDPageContentStream cs = new PDPageContentStream(document, page2, PDPageContentStream.AppendMode.PREPEND, false, false);
                page2.setMediaBox(new PDRectangle((float) w, (float) h));
                //  page.setBleedBox(new PDRectangle(bleedPdf,bleedPdf,wPdf,hPdf));
                page2.setTrimBox(new PDRectangle(bleedPdf, bleedPdf, (float) wPdf, (float) hPdf));
                return page2;
                //     Matrix matrix = Matrix.getScaleInstance(bleedPdf, bleedPdf);//RotateInstance(Math.toRadians(45), 0, 0);
                //     cs.transform(matrix);
                //     cs.close();
                /*
                Iterator<PDStream> it2 = page2.getContentStreams();
                while (it2.hasNext()) {
                    PDStream stream2 = it2.next();
                    stream2.get
                }
                for (PDStream stream : it2..getContentStreams()) {
                    
                }
                PDRectangle rec = page.getMediaBox();
                int width = (int)rec.getWidth();
                int height = (int)rec.getHeight();
                break;
                 */
            }
        } catch (IOException ex) {
            Logger.getLogger(PdfWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    static void createCalandoc2025() {
        
        CalendarGeneration.registerAllFonts();
        
        pathPin = AffineTransform.getScaleInstance(24. / 426., 24. / 426.).createTransformedShape(pathPin);
        pathPin = AffineTransform.getTranslateInstance(-12, -24).createTransformedShape(pathPin);              

        final String pathPictures = "C:\\Users\\sebastien.durand\\Desktop\\Calandreta\\Calandoc\\2025\\";
        final String pathWithText = "C:\\Users\\sebastien.durand\\Desktop\\Calandreta\\Calandoc\\2025\\WithText\\";

        try (PDDocument document = new PDDocument()) {
            PDDocumentInformation pdd = document.getDocumentInformation();
            pdd.setTitle("Calandoc 2025");

            CalendarDrawer2025 calandocDrawer = new CalendarDrawer2025();

            double w = wPdf + bleedPdf * 2, h = hPdf + bleedPdf * 2;

            Font fontTites = new Font("Zen Loop Regular", Font.PLAIN, 48);
            Font fontSmall = new Font("Zen Loop Regular", Font.PLAIN, 12);

        //     Font fontTites = new Font("ParmaPetit", Font.PLAIN, 48);
        //    Font fontSmall = new Font("ParmaPetit", Font.PLAIN, 12);

            
            List<PDPage> pages = new ArrayList<>();

            pages.add(pictureToPdfPage(document, pathWithText + "Couverture.png"));
            pages.add(pictureToPdfPage(document, pathPictures + "Sponsors2025.png"));

            pages.add(presentationToPdfPage(document));

            for (int m = 0; m < 12; m++) {
                int p = PAGE_ORDER[m];
                int px = (int) 50/*w / 4*/, py = (int) h - 20;
                Color cText = Color.white;
                float stroke = .5f;
                if (m == 5) {
                    py = 80;
                }
                if (m == 1) {
                    py = 80;
                    px = (int)w/2+80;
                }
                if (m == 11) {
               //     fontTites = fontTites.deriveFont(36.f);
                    px = (int)w/2+30;
                }
//                pages.add(pictureToPdfPage(document, pathWithText + NAMES_IMG[p]));
                pages.add(pictureToPdfPage(document, pathWithText +  NAMES_IMG[p], NAMES[p], px, py, 0, fontTites, Color.black, Color.white, stroke));
                pages.add(monthToPdfPage(document, calandocDrawer, m));

            }

//            for (int i = 0; i < 12; i++) {
//                pages.add(pictureToPdfPage(document, pathPictures + pictures[i]));
//                pages.add(monthToPdfPage(document, calandocDrawer, i));
//            }
//          
            pages.add(mapToPdfPage(document, pathPictures + "CarteLanguesRelief.png", "", (int) w / 2, (int) h - 50, 0, fontSmall, Color.white, Color.black, 1));

//            pages.add(pictureToPdfPage(document, pathPictures + "Traductions1.png"));
//            pages.add(pictureToPdfPage(document, pathPictures + "Traductions2.png"));
            pages.add(mementoDocToPdfPage(document));
//            pages.add(pictureToPdfPage(document, pathPictures + "4EME_COUV.png"));
            pages.add(defaultToPdfPage(document));

            for (int pg = 0; pg < pages.size(); pg++) {
                PDPage page = pages.get(pg);
                if (page != null) {
                    if (IS_TEST_MODE) {
                        addBorderToPage(document, page, pg % 2 == 0);
                    }
                    document.addPage(page);
                }
            }
            String path = "C:\\Users\\sebastien.durand\\Desktop\\Calandreta\\Calandoc\\calandoc2025" + (IS_TEST_MODE ? "_testMode.pdf" : ".pdf");
            document.save(new File(path));
            document.close();
            System.out.println(path);

        } catch (IOException ex) {
            Logger.getLogger(PdfWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void pdfToPng(File pdfFile, String outDir) {
        // Attention resultat tres moche, pas d'anti aliasing entre le texte et les images
        if (pdfFile.isDirectory()) {
            for (File file : pdfFile.listFiles()) {
                pdfToPng(file, outDir);
            }
        } else {
            if (pdfFile.getName().endsWith("pdf")) {
                try (PDDocument document = Loader.loadPDF(pdfFile)) {
                    PDFRenderer pdfRenderer = new PDFRenderer(document);
                    int nbPage = document.getNumberOfPages();
                    for (int page = 0; page < nbPage; ++page) {
                        BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 600, ImageType.RGB);
                        //BufferedImage bim = pdfRenderer.renderImage(page);
                        // suffix in filename will be used as the file format
                        ImageIO.write(bim, "png", new File(outDir + "\\" + pdfFile.getName().replace(".pdf", "") + (nbPage > 1 ? "-" + (page + 1) : "") + ".png"));
                        //      ImageIOUtil.writeImage(bim, outDir + "\\" + pdfFile.getName().replace(".pdf", ".png") + (nb>1 ? "-" + (page+1) : "") + ".png", 300);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PdfWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // String path = "C:\\Users\\sebastien.durand\\Desktop\\Calandreta\\Calandoc\\2024\\WithText";
        // pdfToPng(new File(path), path + "\\"); 

        createCalandoc2025();
    }
}
