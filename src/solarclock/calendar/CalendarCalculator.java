package solarclock.calendar;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import static solarclock.physic.moon.Jgdates.gDate;
import static solarclock.physic.moon.MoonPhase.isSameDay;
import static solarclock.physic.moon.MoonPhase.phasehunt5;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import solarclock.calendar.tools.MapList;
import solarclock.physic.SunAtTime;

/**
 * This class helps to generate Calendar for given Year.
 *
 * @author dsahu1
 *
 */
public abstract class CalendarCalculator {

    protected final static double MOON_PERIOD = 29.5310258398; //./((1./27.322)-(1./365.25));  // Formule de copernic
    protected final static int daysInMonth[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    protected final static int decalage[] = {0, 3, 3, 6, 1, 4, 6, 2, 5, 0, 3, 5};

    // Partie commune
    abstract protected List<Period> initHollidayAll();
    abstract protected List<Period> initHolliday_A();
    abstract protected List<Period> initHolliday_B();
    abstract protected List<Period> initHolliday_C();
    abstract protected void initYearDependantInfo(int year);

    
    /**
     * Ajout des info annuels
     * @param year 
     */
    protected void initInfo(int year) {
        addInfo(year, 10, 31, "Martror", svgCitrouille.getImage());  //Halloween

        // fetes pour les proverbes        
//        addInfo(year, 8, 8, "Santa Domenica");
//        addInfo(year, 11, 30, "Sant Andrèu");
//        addInfo(year, 12, 13, "Santa Luça");
        addInfo(year, 12, 6, "Sant Nicolau");
        addInfo(year, 3, 17, "Sant Patric");
        addInfo(year, 2, 14, "Sant Valentin");

        addInfo(year, 2, 2, "Fèsta dels pescajons");
        addInfo(year, 3, 1, "Dimars gras");

        addInfo(year, 6, 21, "Fèsta de la musica");
        addInfo(year, 6, 23, "Fèsta de Sant Joan");

        // Depend des annees
        initYearDependantInfo(year);
    }

    /**
     * Ajout des jours feriés annuels
     * @param year 
     */       
    protected void initFerie(int year) {
        // fixe    
        addFerie(year, 1, 1, "Cap d'an");//"Jorn de l'an"); //Jour de l’an");
        addFerie(year, 5, 1, "Fèsta de Trabalh");
        addFerie(year, 5, 8, "08-mai-45");
        addFerie(year, 7, 14, "Fèsta nacionala");
        addFerie(year, 8, 15, "Assompcion"); //Assomption");
        addFerie(year, 11, 1, "Totsants");
        addFerie(year, 11, 11, "Armistici");
        addFerie(year, 12, 25, "Nadal", getImage("noel"));
        
// variables        
        // Calcul des jours feries
        // https://www.mediaforma.com/excel-pratique-calculer-les-jours-feries-dune-annee/
        DateTime easter = getEaster(year);
        
        addFerie(easter, "Pascas", getImage("egg")); 
        addFerie(easter.plusDays(1), "Diluns de Pascas");
        addFerie(easter.plusDays(39), "Dijòus de Ascension");
        addFerie(easter.plusDays(50), "Diluns de Pentacosta");
    }
        
// -----------------------------------------------------------------------------
    protected static class Period {
        final DateTime start, end;

        protected Period(DateTime start, DateTime end) {
            this.start = start;
            this.end = end;
        }

        boolean isInPeriod(DateTime date) {
            return !(start.isAfter(date) || end.isBefore(date));
        }
        @Override
        public String toString() {
            return start.toString() + " => " + end.toString();
        }
    }
    
    public static class DayInfo {
        int[] inHoliday = new int[3];
        List<Info> ferie, info;
        DateTime solarStart, solarEnd;
        Integer moon;
    } 

    
    public DayInfo getInfoAtDate(final int month, final int day) {
        return getInfoAtDate(new DateTime(year, month, day, 0, 0));
    }
    
    public DayInfo getInfoAtDate(final DateTime d) {
        if (d.getMonthOfYear() == 8 && d.getDayOfMonth() == 27) {
            int test = 1;
        }
        DayInfo di = new DayInfo();
        di.inHoliday[0] = isInHoliday(d, 0);
        di.inHoliday[1] = isInHoliday(d, 1);
        di.inHoliday[2] = isInHoliday(d, 2);
        di.ferie = lstFeries.get(d);       
        di.info = lstInfo.get(d);   
        DateTime sun[] = getSolarTimes(d);
        di.solarStart = sun[0];
        di.solarEnd = sun[1];
        di.moon = getMoonAtDay(d);
        return di;
    }
    
    public static class Info {
        public String label;
        public Image icon;
        public Info(Image icon, String label) {
            this.label = label;
            this.icon = icon;
        }
    }
    
    
    // -------------------------------------------------------------------------
    
    protected final Map<DateTime, Integer> lstMoons = new LinkedHashMap<>();
    protected final MapList<DateTime, Info> lstFeries = new MapList<>(), lstInfo = new MapList<>();
    protected final List<Period>[] lstHolidays = new List[3];
    protected final int year;
    protected final double longitude, latitude;
        
    public CalendarCalculator(int year, final double longitude, final double latitude) {
        this.year = year;
        this.latitude = latitude;
        this.longitude = longitude;
        
        initFerie(year);
        initInfo(year);
        initMoons(year);

        lstHolidays[0] = initHolliday_A();
        lstHolidays[1] = initHolliday_B();
        lstHolidays[2] = initHolliday_C();
    }

    int DAY = 24*3600*1000;
    public int isInHoliday(DateTime date, int i) {
        final Period period = findPeriod(lstHolidays[i], date);
        if (period != null) {
            return (int)((date.getMillis() - period.start.getMillis())/(long)DAY);
        }
        return -1;
    }

    public Integer getMoonAtDay(DateTime day) {
        for (Entry<DateTime, Integer> e : lstMoons.entrySet()) {
            if (isSameDay(day, e.getKey().toDateTime(day.getZone()))) { // ! UTC
                return e.getValue();
            }
        }
        return null;
    }

    public boolean isBisextile() {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    public int getDayInMonth(int month) {
        return (month == 1 && isBisextile()) ? 29 : daysInMonth[month];
    }

    public int getDecalage(int month) {
        int f = 2 * (13) + (3 * (13 + 1) / 5) + (year - 1) + ((year - 1) / 4) - ((year - 1) / 100) + ((year - 1) / 400) + 2;
        return (f + decalage[month] + (isBisextile()&&month>=2 ? 1 : 0) - 1) % 7;
    }

    public DateTime[] getSolarTimes(final DateTime date) {
        long tstart = date.getMillis();
        long tmidi = tstart + 12l * 3600l * 1000l; // midi
        long tend = tstart + 24l * 3600l * 1000l; // soir

        long sunUp = doDychotomie(tstart, tmidi, latitude, longitude, 0);
        long sunDown = doDychotomie(tend, tmidi, latitude, longitude, 0);

        return new DateTime[]{
            new DateTime(sunUp, DateTimeZone.UTC).toDateTime(date.getZone()),
            new DateTime(sunDown, DateTimeZone.UTC).toDateTime(date.getZone())};
    }

    protected static long doDychotomie(long t0, long t1, double latitude, double longitude, double elevation) {
        long tmid = 0;
        for (int i = 0; i < 20; i++) {
            tmid = (t0 + t1) / 2;
            SunAtTime sun = new SunAtTime(tmid);
            double eMid = sun.getElevation(latitude, longitude);
            if (eMid > elevation) {
                t1 = tmid;
            } else {
                t0 = tmid;
            }
        }
        return tmid;
    }

    protected void addFerie(int year, int month, int day, String label) {
        addFerie(new DateTime(year, month, day, 0, 0), label, null);
    }   
    
    protected void addFerie(int year, int month, int day, String label, Image icon) {
        addFerie(new DateTime(year, month, day, 0, 0), label, icon);
    }   

    protected void addFerie(DateTime d, String label) {
        addFerie(d, label, null);        
    }

    protected void addFerie(DateTime d, String label, Image icon) {
        this.lstFeries.add(d, new Info(icon, label));        
    }
    
    protected void addInfo(int year, int month, int day, String label) {
        addInfo(new DateTime(year, month, day, 0, 0), label, null);
    }

    protected void addInfo(int year, int month, int day, String label, Image icon) {
        addInfo(new DateTime(year, month, day, 0, 0), label, icon);
    }   

    protected void addInfo(DateTime d, String label, Image icon) {
        this.lstInfo.add(d, new Info(icon, label));        
    }

    protected void addMoon(DateTime d, int phase) {
        this.lstMoons.put(d, phase);
    }
    
    protected void initMoons(int year) {
        DateTime date0 = new DateTime(year, 1, 1, 0, 0);
        double jd = 2440587.5 + (date0.getMillis() / 86400000.0);
        double[] phases = new double[5];

        double mem = -1;
        for (int i = 0; i < 240; i++) {
            phasehunt5(jd, phases);
            if (mem == phases[0]) {
                jd += 1;
                continue;
            } else if (mem > 0 && phases[0] - mem > 35) {
                // Bug Il a sauté un mois !
                addMoon(gDate(phases[0] - MOON_PERIOD), 0); // Approx
                addMoon(gDate(phases[1] - MOON_PERIOD), 25); // Approx
                addMoon(gDate(phases[2] - MOON_PERIOD), 100); // Approx
                addMoon(gDate(phases[3] - MOON_PERIOD), 75); // Approx
            }
            mem = phases[0];
            addMoon(gDate(phases[0]), 0);
            addMoon(gDate(phases[1]), 25);
            addMoon(gDate(phases[2]), 100);
            addMoon(gDate(phases[3]), 75);
            jd = phases[4];
        }
    }
    
    protected static Period findPeriod(final List<Period> lstPeriod, final DateTime date) {
        for (Period p : lstPeriod) {
            if (p.isInPeriod(date)) {
                return p;
            }
        }
        return null;
    }
    
    protected static boolean isInPeriod(final List<Period> lstPeriod, final DateTime date) {
        return findPeriod(lstPeriod, date) != null;
    }

    public static DateTime getEaster2(final int year) {
        int g = year % 19;
        int c = year / 100;
        int h = (c - (c / 4) - ((8 * c + 13) / 25) + 19 * g + 15) % 30;
        int i = h - (h / 28) * (1 - (h / 28) * (29 / (h + 1)) * ((21 - g) / 11));
        int day = i - ((year + year / 4 + i + 2 - c + (c / 4)) % 7) + 28;
        int month = 3;
        if (day > 31) {
            month++;
            day -= 31;
        }
        return new DateTime(year, month, day, 0, 0);
    }

    public static DateTime getEaster(final int year) {
        // Nombre de jours entre Pâques et le 21 mars.
        int alpha = year <= 1582 ? 0 : year <= 1699 ? 7 : year <= 1899 ? 8 : year <= 2199 ? 9 : 10;
        int beta = year <= 1582 ? 0 : year <= 1699 ? 4 : year <= 1799 ? 3 : year <= 1899 ? 2 : year <= 2099 ? 1 : year <= 2099 ? 0 : 6;
        int r1m = (19 * (year % 19) + 15 + alpha) % 30;
        int rm = r1m < 28 ? r1m : (r1m == 28 ? (year % 19 > 10 ? 27 : 28) : 28);
        int tm = (2 * (year % 4) + 4 * (year % 7) + 6 * rm + 6 - beta) % 7;
        //return new DateTime(year, rm + tm <= 9 ? 3 : 4, rm + tm <= 9 ? rm + tm + 22 : rm + tm - 9, 0, 0);      
        final DateTime d = new DateTime(year, 03, 22, 0, 0);
        return d.plusDays(rm + tm);
    }
    
    protected final static SVGIcon svgCitrouille = getSVGIcon("citrouille");
    protected final static BufferedImage imgCartable = getImage("cartable");  
    protected final static BufferedImage imgSpeaker = getImage("Speaker_Icon.svg");
      
    protected final static SVGUniverse SVG_UNIVERSE = SVGCache.getSVGUniverse();
    
    protected static BufferedImage getImage(String icon) {
        try {
            return ImageIO.read(CalendarCalculator.class.getClassLoader().getResource("solarclock/calendar/icons/" + icon + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(CalendarCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static SVGIcon getSVGIcon(final String svg) {
        SVGUniverse univ = SVGCache.getSVGUniverse();
        
        URL url = CalendarCalculator.class.getClassLoader().getResource("solarclock/calendar/icons/" + svg + ".svg");
        URI uri = univ.loadSVG(url);
        if (uri != null) {
            SVGIcon svgIcon = new SVGIcon();
            svgIcon.setSvgURI(uri);
            svgIcon.setAntiAlias(true);
            svgIcon.setClipToViewbox(true);
            svgIcon.setScaleToFit(true);
            return svgIcon;
        }
        return null;
    }
    
}
