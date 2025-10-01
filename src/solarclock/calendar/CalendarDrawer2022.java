package solarclock.calendar;

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
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
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

/**
 * This class helps to generate Calendar for given Year.
 *
 * @author dsahu1
 *
 */
public class CalendarDrawer2022 extends JPanel {

    public final static String //
            monthNames[] = {"Genièr", "Febrièr", "Març", "Abril", "Mai", "Junh", "Julhet", "Agost", "Setembre", "Octòbre", "Novembre", "Décembre"},
            dayNames[] = {"Diluns", "Dimars", "Dimècres", "Dijòus", "Divendres", "Dissabte", "Dimenge"};

    private final String[] lstProverbe = {
        "- Bona annada plan granada de plan autrasa companhada",
        "“Bonne année bien grainée et plein d’autres en suivant”",
        "Une année bien fructueuse et les autres toutes aussi juteuses.",
        "- La nèu de febrièr ten coma l’aiga dins un panièr",
        "“La neige de février tient comme l’eau dans un panier”",
        "S'il neige en février, courez en profiter.",
        "- Març marçal, un jorn de bon e l’autre mal",
        "“Mars martial : un jour bon et l’autre mauvais”",
        "En mars le temps varie, un jour soleil et l'autre pluie.",
        //	"- Quand tron a au mes de març, emplana barricas e casals",
        //	"“Le tonnerre du mois de mars, remplit barriques et jardins”",
        //	"En mars si l'orage gronde, tous les jardins s'inondent.",

        "- Avril fa la flor, e mai n'a l'onor",
        "“Avril fait la fleur et mai en a l’honneur”",
        "C'est en avril qu'on sème mais tout pousse en mai, patience.",
        "- Au mes de mai, cada casse met sans huèlhas",
        "“Au mois de mai, chaque chêne met ses feuilles”",
        "Au mois de mai tous les arbres sont parés.",
        "- Au mes de junh, la higa punt",
        "“Au mois de juin, la figue point”",
        "... mais il faudra attendre encore un peu pour la déguster.",
        "- Ploja de julhet,aiga de desquet",
        "“Pluie de juillet, pluie de panier”",
        "Ne comptez pas sur la pluie pour remplir le puit.",//"Une pluie en juillet ne pénètre guère dans le sol car elle tombe sous forme d'orages violents et est aussitôt évaporée par la chaleur du lendemain.",

        "-A la santa Domenica, te plangas pas se la calor te pica",
        "“A la Saint-Dominique, ne te plaint pas si la chaleur te pique”",
        "Le 8 août, il fait chaud, inutile de s'en plaindre.",
        "- Setembre s'empòrta los ponts o eisseca las fonts",
        "“Septembre emporte les ponts, ou tarit les fontaines”",
        "En Septembre la pluie, c'est soit tout, soit rien du tout", //"La pluviosité du mois de septembre peut être très différente d'une année à l'autre",

        "- Quand octobre perd sa fin, totsants son l’endoman maitin",
        "“Quand octobre prend fin, Toussaint est au matin”",
        "Si vous cherchez la Toussaint, regardez le mois prochain.", //c'est la page suivantequi est fêtée le premier jour du mois de novembre.",

        "- De Totsants a Sant Andrèu, vent o pluèja, fred o nèu",
        "“De la Toussaint jusqu'à la Saint André,\nvent ou pluie, froid ou neige”",
        "Au mois de novembre, restez au chaud dans votre chambre.",
        "- Per Santa Luça, un pè de puça, per Nadal un pè de gal e per l'an nòu un pè de buòu",
        "“Pour St Luce, un pied de puce,\npour Noël un pied de coq et pour l'an neuf un pied de bœuf”",
        "A partir de la St Luce, le crépuscule avance de plus en plus."
    };

    private final static Color[] monthColors = {
        new Color(225, 245, 246), // janvier
        new Color(246, 203, 109), // fev
        new Color(204, 176, 217), // mars
        new Color(173, 195, 120), // abril
        new Color(248, 178, 228), // mai
        new Color(231, 195, 101), // juin
        new Color(126, 185, 227), // juillet
        new Color(254, 221, 108), // aout
        new Color(210, 197, 178), // sept
        new Color(255, 214, 114), // oct
        new Color(209, 187, 150), // nov
        new Color(248, 42, 42) // dec
    };

    private final CalendarCalculator calendar;


    int daysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int decalage[] = {0, 3, 3, 6, 1, 4, 6, 2, 5, 0, 3, 5};

    boolean withSun = true,//
            withWeekNumbers = true,
            withFeries = true,
            withFeriesTxt = true,
            withHollidays = true,
            withMoon = true,
            withBackground = true;

    public CalendarDrawer2022(int year) {
        super();

        this.calendar = new CalendarCalculator2022(1.5340268, 43.315025); // calendrette // 1.444209, 43.604652);  // Toulouse);
        // A4  841.8898, 595.27563

        this.setFont(new Font("ParmaPetit", Font.PLAIN, 16));
    }

    public static JComponent generateCalendarUI(final int year) {
        CalendarDrawer2022 textArea1 = new CalendarDrawer2022(year);

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

    public void paintDay(Graphics2D g2, int day, int month, double x, double y, double w, double h) {

        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRect((int) x, (int) y, (int) w, (int) h + 1);

        DayInfo di = calendar.getInfoAtDate(month + 1, day + 1);

        // test
        //  getPhasesAndSolarTimes(date, 1.532, 43.312);
        if (withSun) {
            //   DateTime[] sun = di..getSolarTimes(month+1, day+1);
            DateTime up = di.solarStart.getSecondOfMinute() < 30 ? di.solarStart : di.solarStart.plusMinutes(1);
            DateTime down = di.solarEnd.getSecondOfMinute() < 30 ? di.solarEnd : di.solarEnd.plusMinutes(1);
//            String txt =  String.format("%02d:%02d-%02d:%02d", up.getHourOfDay(),up.getMinuteOfHour(),down.getHourOfDay(),down.getMinuteOfHour());
            String txt1 = String.format("%02d:%02d", up.getHourOfDay(), up.getMinuteOfHour());
            String txt2 = String.format("%02d:%02d", down.getHourOfDay(), down.getMinuteOfHour());

            g2.setColor(Color.gray);
            g2.setFont(new Font("Arial", Font.ITALIC, 10));
            g2.drawString(txt1, (int) x + 4, (int) (y + 42));
            g2.drawString(txt2, (int) x + 4, (int) (y + 55));
        }
        //   getSolarTimes(date, 1.5340268, 43.315025);  // cintegabelle

        if (withFeries) {
            if (di.ferie != null) {
                g2.setColor(new Color(100, 100, 255, 16));
                g2.fillRect((int) x, (int) y, (int) w, (int) h + 1);
            }
        }

        if (withHollidays) {
            int yv = (int) (y + h) - 8;
            if (di.inHoliday[2]== 0) {//isInPeriod(lstHolidays_C, date)) {
                g2.setColor(new Color(100, 200, 200, 92));
                g2.fillRect((int) x + 1, yv, (int) w - 2, (int) 6);
            }
            yv = (int) (y + h) - 16;
            if (di.inHoliday[1]== 0) {
                g2.setColor(new Color(100, 200, 200, 64));
                g2.fillRect((int) x + 1, yv, (int) w - 2, (int) 6);
            }
            yv = (int) (y + h) - 24;
            if (di.inHoliday[0]== 0) {
                g2.setColor(new Color(100, 200, 200, 32));
                g2.fillRect((int) x + 1, yv, (int) w - 2, (int) 6);
            }
        }

        // Texte Ferier
        if (withFeriesTxt) {
            if (di.ferie != null) {
                for(Info info : di.ferie) {
                    drawInfo(info, g2, x, y, h);
                }
            } else {
                for(Info info : di.ferie) {
                    drawInfo(info, g2, x, y, h);
                }
            }
        }

        // numero du jour
        g2.setColor(Color.black);
        g2.setFont(new Font("ParmaPetit", Font.PLAIN, 26));
        drawStringCenter(g2, "" + (day + 1), x + 18, y + 18);

        if (withMoon) {
            if (di.moon != null) {
                drawMoon(g2, di.moon, (int) (x + w) - 34 + 16, (int) (y + 19), 15);
            }
            if (month == 4 && day == 15) {
                // Cas particulier eclipse de lune !
                g2.setColor(new Color(128, 128, 128));
                drawCircle(g2, (int) (x + w) - 34 + 16, (int) (y + 19), 11);
            }
        }
    }

    private void drawInfo(Info info, Graphics2D g2, double x, double y, double h) {
        if (info != null) {
            g2.setColor(Color.black);
            g2.setFont(new Font("ParmaPetit", Font.PLAIN, 16));
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

        int nbDays = calendar.getDayInMonth(month);
        int decStart = calendar.getDecalage(month);
        int decEnd = (decStart + nbDays) % 7;
        int nbWeek = (decStart + nbDays - 1) / 7 + 1;

        // entete month
        g2.setColor(Color.white);
        g2.fill(new Rectangle2D.Double(0, 0, w + border * 2, h + border * 2));

        g2.translate(border, border);

        g2.setColor(monthColors[month]);
        if (withBackground) {
            Random random = new Random(month);
            random.nextDouble();

            double[] circles = new double[15];
            int nbCircle = 0;
            for (int i = 0; i < 10; i++) {
                double r = 50 + random.nextDouble() * 200;
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
                    g2.fillOval((int) (posX - r), (int) (posY - r), (int) r * 2, (int) r * 2);

                    nbCircle++;
                    if (nbCircle == 5) {
                        break;
                    }
                }
            }
        }

        double wWeekId = withWeekNumbers ? 0 : 0;
        double wDay = (w - wWeekId) / 7;
        double hTop = 120; //h / 6.5;
        double hHeader = 58; //.5 * hTop;
        double hWeek = (h - hTop - hHeader) / nbWeek;

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2.f));
        g2.setFont(this.getFont().deriveFont((float) 140));
        g2.drawString(monthNames[month], 20, (int) hTop - 20);

        final FontMetrics fm = g2.getFontMetrics();
        final Rectangle2D rTxt = fm.getStringBounds(monthNames[month], g2);
        //     final Rectangle2D rProv = fm.getStringBounds(this.lstProverbe[month*3+1], g2);
        Font font = new Font("Peachy Mochi", Font.PLAIN, 25);
        g2.setFont(font);
        g2.setColor(monthColors[month].darker().darker());
        String[] txtProv = lstProverbe[month * 3 + 1].split("\n");
        if (txtProv.length == 1) {
            drawStringRight(g2, txtProv[0], w - 10, (int) hTop - 70);
            g2.setColor(monthColors[month].darker());
            Font font2 = new Font("Peachy Mochi", Font.ITALIC, 20);
            g2.setFont(font2);
            drawStringRight(g2, this.lstProverbe[month * 3 + 2], w - 30, (int) hTop - 38);
        } else {
            drawStringRight(g2, txtProv[0], w - 20, (int) hTop - 85);
            Font font1 = new Font("Peachy Mochi", Font.PLAIN, 20);
            g2.setFont(font1);
            drawStringRight(g2, txtProv[1], w - 10, (int) hTop - 60);
            g2.setColor(monthColors[month].darker());
            Font font2 = new Font("Peachy Mochi", Font.ITALIC, 20);
            g2.setFont(font2);
            drawStringRight(g2, this.lstProverbe[month * 3 + 2], w - 30, (int) hTop - 25);
        }
        double x, y;

        // Affichage des jours
        double x0 = wWeekId,
                y0 = hTop + hHeader;

        for (int d = 0; d < nbDays; d++) {
            x = (d + decStart) % 7;
            y = (d + decStart) / 7;
            paintDay(g2, d, month, x0 + x * wDay, y0 + y * hWeek, wDay, hWeek);
        }

        // Lignes autour des jours
        g2.setColor(Color.black);
        g2.draw(new Line2D.Double(0 - border, hTop, w + 2 * border, hTop));
        g2.draw(new Line2D.Double(0 - border, hTop + hHeader, w + 2 * border, hTop + hHeader));

        y = hTop + hHeader + hWeek - 20;
        if (withWeekNumbers) {

            g2.setFont(new Font("ParmaPetit", Font.PLAIN, 32));
            DateTime d0 = new DateTime(calendar.year, month + 1, 1, 0, 0);
            int weekId = month == 0 ? 0 : d0.getWeekOfWeekyear();
            for (int week = 0; week < nbWeek; week++) {
                x = w - (wWeekId != 0 ? wWeekId / 2 : 20);
                g2.setColor(Color.lightGray);
                drawStringCenter(g2, "" + (weekId + week), x, y);
                y += hWeek;
            }
        }

        // Lignes des semaines
        g2.setColor(Color.gray);
        g2.setStroke(new BasicStroke(1.f));
        y = hTop + hHeader + .5;
        for (int week = 0; week < nbWeek - 1; week++) {
            y += hWeek;
            g2.draw(new Line2D.Double(0 - border, y, w + 2 * border, y));
        }

        // Colone par jours
        y = hTop + hHeader + .5;
        x = wDay + wWeekId;
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2.f));

        if (withWeekNumbers && wWeekId > 0) {
            g2.draw(new Line2D.Double(wWeekId, decStart > 0 ? y : y + hWeek, wWeekId, h + border));
        }

        for (int d = 0; d < 6; d++) {
            g2.draw(new Line2D.Double(x, d >= decStart - 1 ? y : y + hWeek, x, decEnd == 0 || d < decEnd ? h + border : h - hWeek));
            x += wDay;
        }

        // ecritures jours
        y = hTop + hHeader * .5;
        x = wWeekId + wDay * .5;
        g2.setFont(new Font("ParmaPetit", Font.PLAIN, 36));
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2.f));
        for (int d = 0; d < 7; d++) {
            drawStringCenter(g2, dayNames[d], x, y + 2);
            x += wDay;
        }

        g2.translate(-border, -border);
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

        for (int month = 0; month < 12; month++) {
            g2.setClip(new Rectangle2D.Double(0, 0, PdfWriter.width + PdfWriter.bleedBorder * 2, PdfWriter.height + PdfWriter.bleedBorder * 2));
            paintMonth(g2, month, PdfWriter.width, PdfWriter.height, PdfWriter.bleedBorder);
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

}
