/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarclock.calendar.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author sebastien
 */
public class DrawTools {
    
    /**
     * Affiche un texte (via drawString) en faisant en sorte que le texte remplisse au maximum le rectangle englobant
     * @param g le contexte graphique
     * @param txt   le texte a dessiner
     * @param font  la police a utiliser
     * @param bound le rectangle englobant
     */
     public static void drawString(final Graphics g, final String txt, final Font font, final Rectangle2D bound) {
        if (txt == null || txt.isEmpty() || bound == null) return; // Pas besoin de dessin
        final FontMetrics fm = g.getFontMetrics(font);
        final Rectangle2D rTxt = fm.getStringBounds(txt, g);
        final double kw = bound.getWidth()  / rTxt.getWidth(),
                     kh = bound.getHeight() / (rTxt.getHeight()),
                     k = (kw<kh)?kw:kh;
        // rq: on pourrait aussi le faire en faisant une transformation affine sur g
        g.setFont(font.deriveFont((float)(k*font.getSize2D())));  // Create a new font
        g.drawString(txt,                                          // Center text on rectangle
                (int)(bound.getX() + .5*(bound.getWidth() - k*rTxt.getWidth())),
                (int)(bound.getY() + .5*bound.getHeight() + k*(fm.getAscent() - .5*rTxt.getHeight())));
    }
     
    public static Rectangle2D drawStringCenter(final Graphics g, final String txt, final Point2D pt) {
        return drawStringCenter(g, txt, pt.getX(), pt.getY());
    }

    public static Rectangle2D drawStringCenter(final Graphics g, final String txt, final double x, final double y) {
        if (txt == null || txt.isEmpty()) {
            return null;
        } 
        // Pas besoin de dessin
        final FontMetrics fm = g.getFontMetrics();
        final Rectangle2D rTxt = fm.getStringBounds(txt, g);
        // rq: on pourrait aussi le faire en faisant une transformation affine sur g
        g.drawString(txt,                                          // Center text on rectangle
                (int)(x - .5*rTxt.getWidth()),
                (int)(y + fm.getAscent() - .5*rTxt.getHeight()));
        rTxt.setRect(x - .5*rTxt.getWidth(), y + fm.getAscent() - .5*rTxt.getHeight(), rTxt.getWidth(), rTxt.getHeight());
        return rTxt;
    }
    
    public static void drawStringCenterH(final Graphics2D g, final String txt, final double x, final double y) {
        if (txt == null || txt.isEmpty()) return; // Pas besoin de dessin
        g.drawString(txt, (int)(x - .5*g.getFontMetrics().stringWidth(txt)), (int)y);
    }
    public static void drawStringCenter(final Graphics2D g2, final String txt, final double x, final double y, final double angle, final Color txtColor, final Color rectColor, final Color recLineColor) {
        if (txt == null || txt.isEmpty()) {
            return;
        } // Pas besoin de dessin
        final FontMetrics fm = g2.getFontMetrics();
        final Rectangle2D rTxt = fm.getStringBounds(txt, g2);
        final AffineTransform orig = g2.getTransform();
        g2.rotate(angle, x, y);   
        Shape shape;
        if (rectColor != null) {
            g2.setColor(rectColor);
            shape = new RoundRectangle2D.Double(x - .5*rTxt.getWidth()-3, y - .5*rTxt.getHeight()+1, rTxt.getWidth()+6,rTxt.getHeight(), 3,3);
            g2.fill(shape);
            if (recLineColor != null) {
                g2.setColor(recLineColor);
                g2.draw(shape);
            }
        }
        g2.setColor(txtColor);
        g2.drawString(txt,                                          // Center text on rectangle
                (int)(x - .5*rTxt.getWidth()),
                (int)(y + fm.getAscent() - .5*rTxt.getHeight()));
        g2.setTransform(orig);
    }
    
    public static void drawStringCenter(final Graphics2D g2, final String txt, final double x, final double y, final double angle, final boolean clearRect) {
        if (txt == null || txt.isEmpty()) {
            return;
        } // Pas besoin de dessin
        final FontMetrics fm = g2.getFontMetrics();
        final Rectangle2D rTxt = fm.getStringBounds(txt, g2);
        final AffineTransform orig = g2.getTransform();
        g2.rotate(angle, x, y);       

        if (clearRect) {
            //rTxt.setRect(x - .5*rTxt.getWidth(), y + fm.getAscent() - .5*rTxt.getHeight(), rTxt.getWidth(), rTxt.getHeight());
            g2.setBackground(new Color(255,255,255,0));
            g2.clearRect((int)(x - .5*rTxt.getWidth())-3, (int)(y - .5*rTxt.getHeight()), (int)rTxt.getWidth()+6, (int)rTxt.getHeight());
        }
     //   g2.setTransform(AffineTransform.getRotateInstance(angle,x, y));
        // rq: on pourrait aussi le faire en faisant une transformation affine sur g
        g2.drawString(txt,                                          // Center text on rectangle
                (int)(x - .5*rTxt.getWidth()),
                (int)(y + fm.getAscent() - .5*rTxt.getHeight()));
        g2.setTransform(orig);
    }
    
     public static void drawStringCenter(final Path2D path, final FontRenderContext frc, Font font, final String txt, final double x, final double y, final double angle) {
        if (txt == null || txt.isEmpty()) {
            return;
        } // Pas besoin de dessin
        final GlyphVector v = font.createGlyphVector(frc, txt);  
        final Rectangle2D rTxt = v.getLogicalBounds();
        AffineTransform af = AffineTransform.getRotateInstance(angle, x, y);
        path.append(af.createTransformedShape(v.getOutline((int)(x - .5*rTxt.getWidth()), (int)(y /*+ fm.getAscent()*/ - .5*rTxt.getHeight()))), false);
    }
     
    public static void drawString(final Path2D path, final FontRenderContext frc, Font font, final String txt, final double x, final double y, final double angle) {
        if (txt == null || txt.isEmpty()) {
            return;
        } // Pas besoin de dessin
        final GlyphVector v = font.createGlyphVector(frc, txt);  
        final Rectangle2D rTxt = v.getLogicalBounds();
        AffineTransform af = AffineTransform.getRotateInstance(angle, x, y);
        path.append(af.createTransformedShape(v.getOutline((int)(x), (int)(y /*+ fm.getAscent()*/ - .5*rTxt.getHeight()))), false);
    }
    
    /**
     * Cree un path ferme a partir d'une liste de points
     * @param tab les valeurs sous la forme x1,y1,  x2,y2,  x3,y3
     * @return 
     */
    public static Path2D createClosedPath(final double... tab) {
        final Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO, tab.length>>1);
        path.moveTo(tab[0],tab[1]);
        for (int i=2; i<tab.length; i+=2) {
            path.lineTo(tab[i],tab[i+1]);
        }
        path.closePath();
        return path;
    }   
    
    public static Path2D createPath(final double... tab) {
        final Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO, tab.length>>1);
        path.moveTo(tab[0],tab[1]);
        for (int i=2; i<tab.length; i+=2) {
            path.lineTo(tab[i],tab[i+1]);
        }
        return path;
    }  
    
    public static Path2D createPath(final Point2D ... tab) {
        final Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO, tab.length>>1);
        path.moveTo(tab[0].getX(),tab[0].getY());
        for (int i=1; i<tab.length; i++) {
            path.lineTo(tab[i].getX(),tab[i].getY());
        }
        return path;
    }   
    public static Path2D createClosedPath(final Point2D ... tab) {
        final Path2D.Double path = new Path2D.Double(Path2D.WIND_NON_ZERO, tab.length);
        path.moveTo(tab[0].getX(),tab[0].getY());
        for (int i=1; i<tab.length; i++) {
            path.lineTo(tab[i].getX(),tab[i].getY());
        }
        path.closePath();
        return path;
    }      
    
    public static RoundRectangle2D createRoundRect(final Rectangle2D rec, final double round) {
        return new RoundRectangle2D.Double(rec.getMinX(), rec.getMinY(), rec.getWidth(), rec.getHeight(), round, round);
    }
    
    public static List<GlyphVector> createGlyphVectorMultiline(final Font font, final FontRenderContext frc, final String text) {
        final String[] lst = text.split("\n");
        final List<GlyphVector> lstGlyph = new ArrayList<>();
        for (String s : lst) {        
            lstGlyph.add(font.layoutGlyphVector(frc, s.toCharArray(), 0, s.length(), Font.LAYOUT_NO_LIMIT_CONTEXT));
        }
        return lstGlyph;
    }
    
    public static void appendGlyphVectorMultiline(Path2D path, List<GlyphVector> glyphs, double x, double y, double dy, boolean center) {
        for (GlyphVector v : glyphs) {
            path.append(v.getOutline((float) (center ? x - .5*v.getLogicalBounds().getWidth() : x), (float) y), false);
            y += dy;
        }
    }
    
    public static void appendGlyphVectorMultiline(final Font font, final FontRenderContext frc, Path2D path, String txt, double x, double y, boolean center) {
        final LineMetrics lm = font.getLineMetrics(txt, frc);
        appendGlyphVectorMultiline(path, DrawTools.createGlyphVectorMultiline(font, frc, txt), x, y, lm.getAscent(), center);
    }


    /**
     * REtrouve le font a utiliser pour que tous les textes passent dnas la case prevues 
     * @param g
     * @param txt
     * @param font
     * @param bound
     * @return 
     */
    public static Float getBestFontSize(final Graphics g, final String[] txt, final Font font, double w, double h) {
        if (txt == null || txt.length == 0) {
            return null;
        }
        final FontMetrics fm = g.getFontMetrics(font);
        double maxX = 0, maxY = 0;
        for (int i=0; i<txt.length; i++) {
            Rectangle2D rTxt = fm.getStringBounds(txt[i], g);
            if (rTxt.getWidth()>maxX) {
                maxX = rTxt.getWidth();
            }
            if (rTxt.getHeight()>maxY) {
                maxY = rTxt.getHeight();
            }
        }
        final double kw = w  / maxX,
                     kh = h / maxY,
                     k = (kw<kh)?kw:kh;
        // rq: on pourrait aussi le faire en faisant une transformation affine sur g
        return (float)(k*font.getSize2D()); 
    }
    
    /**
     * Decoupe un texte en lignes et pages en fonciton d'une Font
     * Rq: Ne gere pas le cas ou un mot seul ne contient pas sur la ligne
     * @param fullText
     * @param pageWidth
     * @param pageHeight
     * @param font
     * @return 
     */
    public static List<List<List<String>>> splitText(final String fullText, double pageWidth, double pageHeight, final Font font, double lineSpacing, double paragraphSpacing) {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        
        List<List<List<String>>> lstPages = new ArrayList<>();
        String[] lstParagraph = fullText.split("\\r?\\n");
        
        List<List<String>> page = new ArrayList<>();
       
        double wSpace = font.getStringBounds(" ", frc).getWidth();
        double hLine = font.getLineMetrics("#", frc).getHeight() + lineSpacing;
        double posLine = hLine;
        
        // Pour chaque paragraphe
        for (String txtParagraph : lstParagraph) {
            List<String> paragraph = new ArrayList<>();
            // on recherche les coupures entre les mots
            final StringTokenizer tok = new StringTokenizer(txtParagraph, " ");
            int lineWidth = 0;   
            StringBuilder output = new StringBuilder();                     
            while (tok.hasMoreTokens()) {
                // Pour chaque mot
                String word = tok.nextToken();
                double wordWidth = font.getStringBounds(word, frc).getWidth();
                if (lineWidth + wSpace + wordWidth > pageWidth) { 
                    // Il n'y a plus assez de place, on change de ligne
                    paragraph.add(output.toString()); // 
                    posLine += hLine; // on passe a la ligne suivante
                    lineWidth = 0;
                    output = new StringBuilder(word);
                    if (posLine > pageHeight) {
                        page.add(paragraph);
                        lstPages.add(page);
                        // On doit commencer une nouvelle page et donc un nouveau paragraph
                        posLine = 0;
                        page = new ArrayList<>();
                        paragraph = new ArrayList<>();
                    }
                } else {
                    // Il reste assez de place, on ajoute le mot a la ligne
                    output.append(" ").append(word); 
                    lineWidth += wSpace + wordWidth;
                }
            }
            // Ne pas oublier le dernier bout de texte
            if (!output.toString().isEmpty()) {               
                paragraph.add(output.toString());
                page.add(paragraph);
            }
            // On change de paragraphe
            posLine += hLine + paragraphSpacing;
        }   
        if (!page.isEmpty()) {
           lstPages.add(page);                
        }
        return lstPages;
    }
    
    public static void drawPage(Graphics g2, List<List<String>> lstParagraphs, final Font font, double posX, double posY, double lineSpacing, double paragraphSpacing) {
        final FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        final double hLine = font.getLineMetrics("#", frc).getHeight() + lineSpacing;
        g2.setFont(font);
        double posLine = hLine;
        for (List<String> lines : lstParagraphs) {
            for (String line : lines) {
                g2.drawString(line, (int)posX, (int)(posY+posLine));
                posLine += hLine;
            }
            posLine += lineSpacing;
        }
    }
}
