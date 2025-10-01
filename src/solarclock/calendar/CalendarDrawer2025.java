package solarclock.calendar;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joda.time.DateTime;
import solarclock.calendar.CalendarCalculator.DayInfo;
import solarclock.calendar.CalendarCalculator.Info;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import solarclock.calendar.tools.DrawTools;

/**
 * This class helps to generate Calendar for given Year.
 *
 * @author dsahu1
 *
 */
public class CalendarDrawer2025 extends JPanel {

    private final static String //
            monthNames[] = {"Genièr", "Febrièr", "Març", "Abril", "Mai", "Junh", "Julhet", "Agost", "Setembre", "Octòbre", "Novembre", "Décembre"},
            dayNames[] = {"Diluns", "Dimars", "Dimècres", "Dijòus", "Divendres", "Dissabte", "Dimenge"};

    private final static String BASE_URL = "https://calandreta-cintegabelle.fr/"; 
    private final String[] lstUrl = {
        BASE_URL + "calandoc-2025-01/",
        BASE_URL + "calandoc-2025-02/",
        BASE_URL + "calandoc-2025-03/",
        BASE_URL + "calandoc-2025-04/",
        BASE_URL + "calandoc-2025-05/",
        BASE_URL + "calandoc-2025-06/",
        BASE_URL + "calandoc-2025-07/",
        BASE_URL + "calandoc-2025-08/",
        BASE_URL + "calandoc-2025-09/",
        BASE_URL + "calandoc-2025-10/",
        BASE_URL + "calandoc-2025-11/",
        BASE_URL + "calandoc-2025-12/"
    };

    public static final Color[] monthColors = {
        new Color(0xDB00EC), // janvier Dame Carcasse
        new Color(0xAA0CFF), // fev
        new Color(0x6960FF), // maars  Drac
        new Color(0x019CFF), // abril  jean de l'ours
        new Color(0x01C8EC), // mai  Pimpounet
        new Color(0x00EAD0), // juin (tarasque)
        new Color(0x01FC9A), // juillet  volo biou
        new Color(0x03FD54), // aout pont du diable
        new Color(0xBFE701), // sept  Marseille
        new Color(0xF6D310),//0xFF6155), // oct  gevaudan
        new Color(0xFF009D), // nov  etan de thau
        new Color(0xFF00D6) // dec  loupgarrou
    };

    protected final static Shape svgCross = createOccitranCrossPath(); //getSVGIcon("Flag_of_Occitania");

    public static Shape createOccitranCrossPath() {
        final String pathCrossSvg
                =//
                "m 183.175,267 9.125,7.138 c 14.633,11.443 38.502,35.909 45.015,77.3 l 1.722,10.924 8.36,-7.312 c 14.038,-12.277 55.754,-44.434 116.757,-53.224 -8.792,61.003 -40.948,102.718 -53.224,116.756 l -7.312,8.361 10.924,1.721 c 41.392,6.515 65.857,30.383 77.301,45.015 l 7.137,9.126 7.138,-9.126 c 11.442,-14.632 35.907,-38.5 77.299,-45.015 l 10.924,-1.721 -7.312,-8.361 c -12.276,-14.038 -44.432,-55.753 -53.224,-116.756 61.003,8.79 102.719,40.947 116.756,53.224 l 8.361,7.312 1.721,-10.924 c 6.514,-41.391 30.383,-65.857 45.014,-77.3 l 9.127,-7.138 -9.127,-7.138 c -14.631,-11.443 -38.5,-35.908 -45.014,-77.299 l -1.721,-10.925 -8.361,7.312 c -14.037,12.277 -55.753,44.434 -116.756,53.225 8.792,-61.003 40.948,-102.719 53.224,-116.756 l 7.312,-8.362 -10.924,-1.72 C 442.025,98.821 417.56,74.953 406.118,60.321 l -7.138,-9.126 -7.137,9.126 c -11.444,14.632 -35.909,38.5 -77.301,45.016 l -10.924,1.72 7.312,8.362 c 12.276,14.037 44.432,55.753 53.224,116.756 -61.003,-8.791 -102.719,-40.948 -116.757,-53.225 l -8.36,-7.312 -1.722,10.925 c -6.513,41.391 -30.382,65.856 -45.015,77.299 z "
                + "m 35.125,0 c 27.829,-25.798 34.256,-54.425 34.256,-54.425 40.625,34.242 103.986,41.96 135.664,43.666 -1.707,-31.679 -9.424,-95.039 -43.665,-135.666 0,0 28.625,-6.425 54.425,-34.255 25.799,27.83 54.424,34.255 54.424,34.255 -34.241,40.627 -41.958,103.987 -43.665,135.666 31.678,-1.706 95.039,-9.424 135.665,-43.666 0,0 6.425,28.627 34.255,54.425 -27.83,25.799 -34.255,54.425 -34.255,54.425 -40.626,-34.242 -103.987,-41.96 -135.665,-43.666 1.707,31.679 9.424,95.039 43.665,135.666 0,0 -28.625,6.425 -54.424,34.255 -25.8,-27.83 -54.425,-34.255 -54.425,-34.255 34.241,-40.627 41.958,-103.987 43.665,-135.666 -31.678,1.706 -95.039,9.424 -135.664,43.666 0,0 -6.427,-28.626 -34.256,-54.425 z";

        final Path2D pathCross = SimpleSvgTools.svgPathToPath2D(pathCrossSvg);

        final double r = 18;
        final double[] pos = {
            175.7545, 267, 236.5545, 164.576, 236.5545, 369.42499, 296.55551, 104.575, 296.55551, 429.42599, 398.97949, 43.774399, 398.97949, 490.22601, 501.4035, 104.575,
            501.4035, 429.42599, 561.40448, 164.576, 561.40448, 369.42499, 622.20551, 267};

        final Area aeraCross = new Area(pathCross);
        for (int i = 0; i < 12; i++) {
            aeraCross.add(new Area(new Ellipse2D.Double(pos[i * 2] - r, pos[i * 2 + 1] - r, r * 2, r * 2)));
        }
        return aeraCross;
    }
    
    public static Color alpha(Color c, double a) {
        return (c == null) ? null : new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.min(255, Math.max(0, (int) (255. * a))));
    }
    
    private final CalendarCalculator calendar;

    double hTop = 110; //125;
    double hHeader = 50; //58;

    public Color colorMonthBackground = new Color(250,230,230);//.lightGray; //Color.white;
 //   Color colorMonth = monthColors[i]; //Color.cyan; //alpha(Color.black, .5);
    Font fontMonth = new Font("Sunset Club Free Trial" /*"ParmaPetit"*/, Font.PLAIN, 100);
 //   Font fontMonth = new Font("Havana-Regular" /*"ParmaPetit"*/, Font.PLAIN, 120);

     
    int monthx = 20, monthy = (int) hTop - 0;

    float epLine = 2.f;
    Color colorLine = null; //Color.black;

    float epWeekLine = 1.f;
    Color colorWeekLine = null; //Color.gray;

    Color colorDayName = alpha(Color.black, .5);
    
    
       Font fontDayName = new Font("Zen Loop Regular", Font.PLAIN, 36);
//    Font fontDayName = new Font("ParmaPetit", Font.PLAIN, 36);

    Color colorWeekNumber = Color.lightGray;
    Font fontWeekNumber = fontDayName.deriveFont(Font.PLAIN, fontDayName.getSize2D()*(32.f/36.f));
//    Font fontWeekNumber = new Font("ParmaPetit", Font.PLAIN, 32);

    Color colorColumnDay = null; //Color.lightGray; //Color.black;
    float epColumnDay = 2.f;

    // Configuration dessin jour
    Color colorBackground = new Color(255, 255, 255, 192);
    Color colorFerie = mix(new Color(0xC07ACAFF, true), colorBackground, .25);//200, 200, 255, 150);
    Color colorWE = mix(new Color(0xC0ABE0E4, true), colorBackground, .25); //new Color(0x96ABE0E4, true); //200, 255, 200, 150);
  
    // Nuerao du jour
    Color colorDay = Color.black;
    Font fontDay = fontDayName.deriveFont(Font.BOLD, 26);
    int dxDay = 18;
    int dyDay = 18;

    // Heure soleil    
    Color colorSun = Color.gray;
 
    int dxSun = 40;
    int dySun = 14;

    // Vacances
    Color hollidayA = new Color(200, 100, 100, 64);
    Color hollidayB = new Color(100, 100, 200, 64);
    Color hollidayC = new Color(100, 200, 100, 64);
    Font fontHolliday = new Font("Arial", Font.PLAIN, 9);

    Font colorFont = fontDayName.deriveFont(Font.BOLD, fontWeekNumber.getSize2D()/2.f); //new Font("ParmaPetit", Font.PLAIN, 16);
    Font fontSun = new Font("Elementary_Gothic_Bookhand", Font.ITALIC, 10);  
   //  Font fontSun = new Font("Elementary_Gothic_Bookhand", Font.ITALIC, 10);  
    Color colorInfo = Color.black;

    boolean //
            withSun = true,
            withWeekNumbers = true,
            withFeries = true,
            withFeriesTxt = true,
            withHollidays = true,
            withMoon = true,
            withBackground = true;

    public CalendarDrawer2025() {
        super();
        CalendarGeneration.registerAllFonts();
        /*
        createFont("ParmaPetit-Normal");
        createFont("Elementary_Gothic_Bookhand");
        createFont("Peachy Mochi");
        createFont("Sunset Club Free Trial");
        createFont("Neon Future 2.0 Demo");
          */      

        this.calendar = new CalendarCalculator2025(1.5340268, 43.315025); // calendrette // 1.444209, 43.604652);  // Toulouse);
    }

    public static JComponent generateCalendarUI(final int year) {
        CalendarDrawer2025 textArea1 = new CalendarDrawer2025();
        return textArea1;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) PdfWriter.width + 20, (int) (PdfWriter.height + 20) * 12);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension((int) PdfWriter.width + 20, (int) (PdfWriter.height + 20) * 12);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension((int) PdfWriter.width + 100, (int) (PdfWriter.height + 20) * 12);
    }

    public void paintDay(Graphics2D g2, int day, int month, double x, double y, double w, double h, boolean isWeekEnd) {
    
        DayInfo di = calendar.getInfoAtDate(month + 1, day + 1);
        g2.setColor(/*(withFeries && di.ferie != null) ? colorFerie :*/ isWeekEnd ? colorWE : colorBackground);
        Shape dayShape =  new RoundRectangle2D.Double(x+2, y+1, w-5, h-3,24,24);
        g2.fill(dayShape);
        
        if (withFeries && di.ferie != null) {
            g2.setColor(colorFerie);
            g2.setStroke(new BasicStroke(4.f));
            g2.draw(dayShape);
        }
        Shape oldClip = g2.getClip();
        g2.setClip(dayShape);

        if (withSun) {
            //   DateTime[] sun = di..getSolarTimes(month+1, day+1);
            DateTime up = di.solarStart.getSecondOfMinute() < 30 ? di.solarStart : di.solarStart.plusMinutes(1);
            DateTime down = di.solarEnd.getSecondOfMinute() < 30 ? di.solarEnd : di.solarEnd.plusMinutes(1);
//            String txt =  String.format("%02d:%02d-%02d:%02d", up.getHourOfDay(),up.getMinuteOfHour(),down.getHourOfDay(),down.getMinuteOfHour());
            String txt1 = String.format("%02d:%02d", up.getHourOfDay(), up.getMinuteOfHour());
            String txt2 = String.format("%02d:%02d", down.getHourOfDay(), down.getMinuteOfHour());

            g2.setColor(colorSun);
            g2.setFont(fontSun);
            g2.drawString(txt1 + " → " + txt2, (int) x + dxSun, (int) y + dySun);
        }

        if (withHollidays) {
            g2.setFont(fontHolliday);
            int yv = (int) (y + h) - 8;
            if (di.inHoliday[2] >= 0) {
                g2.setColor(hollidayC);
                if (di.inHoliday[2] == 0) { // premier jour des vacances
                    g2.fillRect((int) x + 12+8, yv, (int) w - 13, (int) 6);
                    g2.setColor(new Color(hollidayC.getRed(), hollidayC.getGreen(), hollidayC.getBlue()));
                    g2.drawString("C", (int) x + 4+8, yv + 7);
                } else {
                    g2.fillRect((int) x + 1, yv, (int) w - 2, (int) 6);
                }
            }
            yv = (int) (y + h) - 16;
            if (di.inHoliday[1] >= 0) {
                g2.setColor(hollidayB);
                if (di.inHoliday[1] == 0) { // premier jour des vacances
                    g2.fillRect((int) x + 12, yv, (int) w - 13, (int) 6);
                    g2.setColor(new Color(hollidayB.getRed(), hollidayB.getGreen(), hollidayB.getBlue()));
                    g2.drawString("B", (int) x + 4, yv + 7);
                } else {
                    g2.fillRect((int) x + 1, yv, (int) w - 2, (int) 6);
                }
            }
            yv = (int) (y + h) - 24;
            if (di.inHoliday[0] >= 0) {
                g2.setColor(hollidayA);
                if (di.inHoliday[0] == 0) { // premier jour des vacances
                    g2.fillRect((int) x + 12, yv, (int) w - 13, (int) 6);
                    g2.setColor(new Color(hollidayA.getRed(), hollidayA.getGreen(), hollidayA.getBlue()));
                    g2.drawString("A", (int) x + 4, yv + 7);
                } else {
                    g2.fillRect((int) x + 1, yv, (int) w - 2, (int) 6);
                }
            }
        }

        double yy = y;
        // Texte Ferier
        if (withFeriesTxt) {
            if (di.ferie != null) {
                for (Info info : di.ferie) {
                    drawInfo(info, g2, x+8, yy, h);
                    yy -= 13;
                }
            }
            if (di.info != null) {
                for (Info info : di.info) {
                    drawInfo(info, g2, x+8, yy, h);
                    yy -= 13;
                }
            }
        }

        // numero du jour
        g2.setColor(colorDay);
        g2.setFont(fontDay);
        drawStringCenter(g2, "" + (day + 1), x + dxDay, y + dyDay);

        if (withMoon) {
            if (di.moon != null) {
                drawMoon(g2, di.moon, (int) (x + w) - 34 + 16, (int) (y + 18), 12);
            }
        }
        
        g2.setClip(oldClip);
        
        if (withFeries && di.ferie != null) {
            g2.setColor(new Color(0xC07ACAFF));
            g2.setStroke(new BasicStroke(2.f));
            dayShape =  new RoundRectangle2D.Double(x+3, y+2, w-7, h-5,24,24);
            g2.draw(dayShape);
        }

    }

    private void drawInfo(Info info, Graphics2D g2, double x, double y, double h) {
        if (info != null) {
            g2.setColor(colorInfo);
            g2.setFont(colorFont);
            if (info.icon != null) {
                g2.drawImage(info.icon, (int) x + 4, (int) (y + h) - 22, 20, 20, this);
                if (info.label != null) {
                    g2.drawString(info.label, (int) x + 26, (int) (y + h) - 6);
                }
            } else {
                g2.drawString(info.label, (int) x + 4, (int) (y + h) - 6);
            }
        }
    }

    public void paintMonth(Graphics2D g2, int month, double w, double h, double border) {
        
        paintMonthBackground(g2, w, h, border, month);

        g2.translate(border, border);
        
    //    g2.setClip(new Rectangle2D.Double(-border, -border, w + 2 * border, h + 2 * border));

        int nbDays = calendar.getDayInMonth(month);
        int decStart = calendar.getDecalage(month);
        int decEnd = (decStart + nbDays) % 7;
        int nbWeek = (decStart + nbDays - 1) / 7 + 1;

        double wWeekId = withWeekNumbers ? 0 : 0;
        double wDay = (w - wWeekId) / 7;
        double hWeek = (h - hTop - hHeader) / nbWeek;

        Color colorMonth = monthColors[month];

        colorWE = mix(colorMonth, Color.white, .95); //new Color(0x96ABE0E4, true); //200, 255, 200, 150);

        g2.setColor(colorMonth);
        g2.setFont(fontMonth);
        
        double qrsz = hTop - 30;
 
    Path2D pathTitle = new Path2D.Double();
    DrawTools.drawStringCenter(pathTitle, g2.getFontRenderContext(), g2.getFont(), monthNames[month], (w- qrsz - 20)/2, monthy+50/*45*//*-27*/, 0);
 
        drawNeon(g2, colorMonth, colorMonth, pathTitle);
           
        
        drawQRCode(g2, lstUrl[month], w - qrsz - 20, 30, qrsz, qrsz);
        g2.drawImage(CalendarCalculator.imgSpeaker, (int) (w - qrsz) - 70, 10 + (int) (qrsz / 2), 30, 30, null);

        double x, y;

        // Affichage des jours
        double x0 = wWeekId,
               y0 = hTop + hHeader;

        for (int d = 0; d < nbDays; d++) {
            x = (d + decStart) % 7;
            y = (d + decStart) / 7;
            paintDay(g2, d, month, x0 + x * wDay, y0 + y * hWeek, wDay, hWeek, x>4);
        }

        // Lignes autour des jours
        if (colorLine != null) {
            g2.setStroke(new BasicStroke(epLine));
            g2.setColor(colorLine);
            g2.draw(new Line2D.Double(0 - border, hTop, w + 2 * border, hTop));
            g2.draw(new Line2D.Double(0 - border, hTop + hHeader, w + 2 * border, hTop + hHeader));
        }
        
        y = hTop + hHeader + hWeek/2/* - 20*/;
        if (withWeekNumbers) {
            g2.setColor(colorWeekNumber);
            g2.setFont(fontWeekNumber);
            DateTime d0 = new DateTime(calendar.year, month + 1, 1, 0, 0);
            int weekId = /*month == 0 ? 0 :*/ d0.getWeekOfWeekyear();
            for (int week = 0; week < nbWeek; week++) {
                x = w - (wWeekId != 0 ? wWeekId / 2 : 20);

                drawStringCenter(g2, "" + (weekId + week), x, y);
                y += hWeek;
            }
        }
        // Lignes des semaines
        if (colorWeekLine != null) {
            g2.setColor(colorWeekLine);
            g2.setStroke(new BasicStroke(epWeekLine));
            y = hTop + hHeader + .5;
            for (int week = 0; week < nbWeek - 1; week++) {
                y += hWeek;
                g2.draw(new Line2D.Double(0 - border, y, w + 2 * border, y));
            }
        }
        // Colone par jours
        if (colorColumnDay != null) {
            y = hTop + hHeader + .5;
            x = wDay + wWeekId;
            g2.setColor(colorColumnDay);
            g2.setStroke(new BasicStroke(epColumnDay));

            if (withWeekNumbers && wWeekId > 0) {
                g2.draw(new Line2D.Double(wWeekId, decStart > 0 ? y : y + hWeek, wWeekId, h + border));
            }

            for (int d = 0; d < 6; d++) {
                g2.draw(new Line2D.Double(x, d >= decStart - 1 ? y : y + hWeek, x, decEnd == 0 || d < decEnd ? h + border : h - hWeek));
                x += wDay;
            }
        }
        
        // ecritures jours
        y = hTop + hHeader * .5 + 6;
        x = wWeekId + wDay * .5;

        g2.setFont(fontDayName);
        g2.setColor(colorDayName);

        for (int d = 0; d < 7; d++) {
            drawStringCenter(g2, dayNames[d], x, y + 2);
            x += wDay;
        }

        g2.translate(-border, -border);
    }

    public static void drawNeon(Graphics2D g2, Color colorLight,  Color colorFill, Shape pathTitle) {
        for (int i=0; i<10; i++) {
            g2.setColor(new Color(colorLight.getRed(), colorLight.getGreen(),colorLight.getBlue(),4 + i*4));
            g2.setStroke(new BasicStroke(20-2*i, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(pathTitle);
        }
        
        g2.setColor(colorFill);
        g2.fill(pathTitle);
        
        g2.setColor(new Color(0,0,0,24));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(pathTitle);
    }

    private static ByteMatrix generateMatrix(final String data) {
        try {
            QRCode qr = Encoder.encode(data, ErrorCorrectionLevel.M/*, qr*/);//new QRCode();
            ByteMatrix matrix = qr.getMatrix();
            return matrix;
        } catch (WriterException ex) {
            Logger.getLogger(CalendarDrawer2025.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void drawQRCode(Graphics2D g2, String data, double dx, double dy, double width, double height) {
        drawByteMatrix(g2, generateMatrix(data), dx, dy, width, height);
    }

    private void drawQRCode(Graphics2D g2, QRCode qr, double dx, double dy, double width, double height) {
        drawByteMatrix(g2, qr.getMatrix(), dx, dy, width, height);
    }

    private void drawByteMatrix(Graphics2D g2, ByteMatrix bitMatrix, double dx, double dy, double width, double height) {
        int matrixWidth = bitMatrix.getWidth();
        int matrixHeight = bitMatrix.getHeight();
        g2.setColor(Color.BLACK);
        double kx = width / matrixWidth;
        double ky = height / matrixHeight;
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixHeight; j++) {
                if (bitMatrix.get(i, j) == 1) {
                    g2.fill(new Rectangle2D.Double(dx + i * kx, dy + j * ky, kx, ky));
                }
            }
        }
    }
    
    private static int _byte(final double v) {
        return Math.min(Math.max(0,(int)v),255);
    }
    
    public static Color mix(final Color c0, final Color c1, final double k1) {
        if (c0 == null) {
            return c1;
        }
        if (c1 == null) {
            return c0;
        }
        final double k2 = k1 < 0 ? 0 : k1 > 1. ? 1. : k1,
                k0 = 1. - k2;
        return new Color(_byte(k0 * c0.getRed() + k2 * c1.getRed()),
                _byte(k0 * c0.getGreen() + k2 * c1.getGreen()),
                _byte(k0 * c0.getBlue() + k2 * c1.getBlue()),
                _byte(k0 * c0.getAlpha() + k2 * c1.getAlpha()));
    }
    
    private void paintMonthBackground(Graphics2D g2, double w, double h, double border, int month) {
        g2.setClip(new Rectangle2D.Double(0, 0, w + border * 2, h + border * 2));
        // entete month
        g2.setColor(new Color(230,230,230)); //mix(monthColors[month], Color.white,.85));
        g2.fill(new Rectangle2D.Double(0, 0, w + border * 2, h + border * 2));

      //  g2.translate(border, border);

     //   g2.setColor(monthColors[month]);
//        g2.setColor(mix(monthColors[month], Color.white,.6));
        g2.setColor(new Color(212,212,212));
        
        if (withBackground) {
            Random random = new Random(month);
            random.nextDouble();

            double[] circles = new double[15];
            int nbCircle = 0;
            for (int i = 0; i < 10; i++) {
                double r = 150 + random.nextDouble() * 400;
                double posX = random.nextDouble() * (w + 2 * r) - r;
                double posY = random.nextDouble() * (h + 2 * r) - r;
                boolean intersect = false;
                for (int n = 0; n < nbCircle; n++) {
                    if (Math.sqrt((circles[n * 3] - posX) * (circles[n * 3] - posX) + (circles[n * 3 + 1] - posY) * (circles[n * 3 + 1] - posY)) < circles[n * 3 + 2] + r + 20) {
                        intersect = true;
                        break;
                    }
                }
                if (!intersect) {
                    circles[nbCircle * 3] = posX;
                    circles[nbCircle * 3 + 1] = posY;
                    circles[nbCircle * 3 + 2] = r;
                    //g2.fillOval((int) (posX - r), (int) (posY - r), (int) r * 2, (int) r * 2);
                    AffineTransform tr = g2.getTransform();
                    g2.rotate(r, posX, posY);
                    AffineTransform af = findTransformToEnterShapeInRect(svgCross.getBounds2D(), new Rectangle2D.Double((int) (posX - r), (int) (posY - r), (int) (r * 2), (int) (r * 2)));
                    g2.fill(af.createTransformedShape(svgCross));
                    g2.setTransform(tr);
                    nbCircle++;
                    if (nbCircle == 3) {
                        break;
                    }
                }
            }
        }
    }

    // Trouve la transformation affine a appliquer au rectangle 1 pour le faire rentrer dans le rectangle 2 sans changer son aspect ratio
    public static AffineTransform findTransformToEnterShapeInRect(final Rectangle2D r1, final Rectangle2D r2) {
        final double k = Math.min(r2.getWidth() / r1.getWidth(), r2.getHeight() / r1.getHeight());
        final AffineTransform af = AffineTransform.getTranslateInstance(r2.getCenterX(), r2.getCenterY());
        af.scale(k, k);
        af.translate(-r1.getCenterX(), -r1.getCenterY());
        return af;
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

        double borderPx = 10;
        //   double border = bleedBorder;

        for (int month = 0; month < 12; month++) {
            paintMonthBackground(g2, PdfWriter.width, PdfWriter.height, 0/*bleedBorder*/, month);
            g2.translate(borderPx, borderPx);
            paintMonth(g2, month, PdfWriter.width - 2 * borderPx, PdfWriter.height - 2 * borderPx, 0);
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(2.f));
            g2.drawRect((int) 0, (int) 0, (int) (PdfWriter.width - 2 * borderPx), (int) (PdfWriter.height - 2 * borderPx));
            g2.setClip(null);
            g2.translate(-borderPx, -borderPx);
            g2.translate(0, PdfWriter.height + 20 + 2. * PdfWriter.bleedBorder);
        }
    }

    public static void drawStringRight(final Graphics g, final String txt, final double x, final double y) {
        if (txt == null || txt.isEmpty()) {
        }
        // Pas besoin de dessin
        final FontMetrics fm = g.getFontMetrics();
        final Rectangle2D rTxt = fm.getStringBounds(txt, g);
        // rq: on pourrait aussi le faire en faisant une transformation affine sur g
        g.drawString(txt, // Center text on rectangle
                (int) (x - rTxt.getWidth()),
                (int) (y + fm.getAscent() - .5 * rTxt.getHeight()));
    }

    public static Rectangle2D drawStringCenter(final Graphics g, final String txt, final double x, final double y) {
        if (txt == null || txt.isEmpty()) {
            return null;
        }
        // Pas besoin de dessin
        final FontMetrics fm = g.getFontMetrics();
        final Rectangle2D rTxt = fm.getStringBounds(txt, g);
        // rq: on pourrait aussi le faire en faisant une transformation affine sur g
        g.drawString(txt, // Center text on rectangle
                (int) (x - .5 * rTxt.getWidth()),
                (int) (y + fm.getAscent() - .5 * rTxt.getHeight()));
        rTxt.setRect(x - .5 * rTxt.getWidth(), y + fm.getAscent() - .5 * rTxt.getHeight(), rTxt.getWidth(), rTxt.getHeight());
        return rTxt;
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
            path.append(v.getOutline((float) (center ? x - .5 * v.getLogicalBounds().getWidth() : x), (float) y), false);
            y += dy;
        }
    }

    public static void appendGlyphVectorMultiline(final Font font, final FontRenderContext frc, Path2D path, String txt, double x, double y, boolean center) {
        final LineMetrics lm = font.getLineMetrics(txt, frc);
        appendGlyphVectorMultiline(path, createGlyphVectorMultiline(font, frc, txt), x, y, lm.getAscent(), center);
    }

    protected void drawCircle(final Graphics2D g2, double cx, double cy, double r) {
        g2.fill(new Ellipse2D.Double(cx - r, cy - r, r * 2, r * 2));
    }

    protected void drawMoon(final Graphics2D g2, int phase, double cx, double cy, double r) {

        if (phase == 0 || phase == 100) {
            g2.setColor(phase == 0 ? new Color(218, 218, 218) : new Color(255, 215, 130));
            drawCircle(g2, cx, cy, r);
        } else {
            g2.setColor(phase == 25 ? new Color(218, 218, 218) : new Color(255, 215, 130));
            g2.fill(new Arc2D.Double(cx - r, cy - r, r * 2, r * 2, 90, 180, Arc2D.PIE));
            g2.setColor(phase == 25 ? new Color(255, 215, 130) : new Color(218, 218, 218));
            g2.fill(new Arc2D.Double(cx - r, cy - r, r * 2, r * 2, 90, -180, Arc2D.PIE));
        }
        // Left
        g2.setColor(phase == 0 || phase == 25 ? new Color(194, 194, 194) : new Color(253, 189, 65));
        drawCircle(g2, cx - r * .3, cy - r * .45, r * .3);
        drawCircle(g2, cx - r * .35, cy + r * .4, r * .2);

        // Right
        g2.setColor(phase == 0 || phase == 75 ? new Color(194, 194, 194) : new Color(253, 189, 65));
        drawCircle(g2, cx + r * .4, cy + r * .4, r * .4);
        drawCircle(g2, cx + r * .25, cy - r * .4, r * .15);
    }

    /**
     * Remplace la fill color dans le svg de maniere ercursive
     *
     * @param toColor
     * @param node
     * @throws SVGException
     */
    private void setStroke(Color toColor, SVGElement node) throws SVGException {
//        if (node.hasAttribute("stroke", AnimationElement.AT_CSS)) {
//            StyleAttribute abs = node.getStyleAbsolute("stroke");
//            Color was = abs.getColorValue();
//            if (was.equals(fromColor)) {
//                abs.setStringValue(toColor);
//            }
//        }
        StyleAttribute abs = node.getPresAbsolute("fill");
        if (abs != null) {
            abs.setStringValue(getHexString(toColor));
        }
        for (int i = 0; i < node.getNumChildren(); ++i) {
            setStroke(toColor, node.getChild(i));
        }
    }

    private String getHexString(Color color) {
        return String.format("#%06x", (0xFFFFFF & color.getRGB()));
    }

}
