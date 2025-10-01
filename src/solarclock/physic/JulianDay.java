package solarclock.physic;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author sebastien.durand
 */
public class JulianDay {
    final double julianDay;
    final double GMST;          // Greenwiwh mean sideral time
    
    public JulianDay(final double jd) {
        julianDay = jd;
        GMST = getGMST(julianDay);
    }
    
    public JulianDay(final long unixTimeMs) {
        this((double)JulianDay.convertToJulianDay(unixTimeMs));
    }
    
    static double convertToJulianDay(final long unixTimeMs) {
        return 2440587.5 + (unixTimeMs / 86400000.0);
    }
//    public JulianDay(final DateTime date) {
//        this(date.getMillis()); 
//    }
    
    public JulianDay(final int yr, final int mn, final int day, final int hr, final int min) {
        int im = (mn-14)/12;
        int ijulian = day - 32075 + 1461*(yr+4800+im)/4 + 367*(mn-2-im*12)/12 - 3*((yr+4900+im)/100)/4;
        julianDay = ijulian + hr/24. - 0.5 + min/24./60.;
        GMST = getGMST(julianDay); 
    }
        
    public double getGreenwichMeanSideralTime() {
        return GMST;
    }
    
    public double getJulianDay() {
        return julianDay;
    }
    
    public double getNbDaySince2000() {
        return julianDay-2451545.;
    }
    
    public double getNbCenturySince2000() {
        return (julianDay-2451545.)/36525.;
    }
    
    private static double getGMST(double jd) {
        double zt  = jd-2451545.;  // nombre de jours depuis le 01/01/2000 (1 January 2000 = 2451545 Julian Days)
        double zt0 = zt/36525.;    // Nombre de siecles depuis 2000
        return 280.46061837 + 360.98564736629*zt + zt0*zt0*(0.000387933 - zt0/38710000.); // Greenwich Mean Sidereal Time
    }

}
