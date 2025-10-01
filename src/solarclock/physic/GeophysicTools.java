package solarclock.physic;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.geom.Point2D;


/**
 *
 * @author sebastien.durand
 */
public class GeophysicTools {
    
    public static final double  toRad = Math.PI/180.,
                                toDeg = 180./Math.PI,
                                pprterre = 6371.;
        
    public static double[] DeclinationRightAscention_to_AzimuthElevation(final double declination, final double rightAscention, final double latitude, final double longitude, final double jd) {  
            final double 
                zt  = jd-2451545.,  // nombre de jours depuis le 01/01/2000 (1 January 2000 = 2451545 Julian Days)
                zt0 = zt/36525.,    // Nombre de siecles depuis 2000
                GMST = 280.46061837 + 360.98564736629*zt + zt0*zt0*(0.000387933 - zt0/38710000.), // Greenwich Mean Sidereal Time          
                sin_dec = Math.sin(declination),
                cos_dec = Math.cos(declination),
                sin_lat = Math.sin(latitude),
                cos_lat = Math.cos(latitude),
                elevation = Math.asin(sin_lat * sin_dec + cos_lat * cos_dec * Math.cos(longitude + GMST*toRad - rightAscention)),
                azimuth = Math.acos(sin_dec - sin_lat*Math.sin(elevation) / cos_lat*Math.cos(elevation));
            return new double[] {azimuth, elevation};            
    }

    /** Calcul du jour julien
     * @param yr - Input Year (e.g. 1978)
     * @param mn - Input Month (1-12)
     * @param day - Input Day (1-31)
     * @param hr - Input Hour (0-24)
     * @param min - Input Min (0-59)
     * @return 
     */
    public static double toJulianDay(final int yr, final int mn, final int day, final int hr, final int min) {
        int im = (mn-14)/12;
        int ijulian = day - 32075 + 1461*(yr+4800+im)/4 + 367*(mn-2-im*12)/12 - 3*((yr+4900+im)/100)/4;
        return ijulian + hr/24. - 0.5 + min/24./60.;
    }
    
    public static double toJulianDay(final long unixTimeMs) {
        return 2440587.5 + (unixTimeMs / 86400000.0);
    }
    
//    public static double toJulianDay(final DateTime date) {
//        return toJulianDay(date.getMillis());
//    }

    /**
     * Pour determiner l'heure de coucher du soleil
     * Generalement, on se refere au crepuscule civil. 
     * Dans ce cas, le centre du disque solaire doit se trouver a 6° sous l'horizon. 
     * Pour obtenir ce qu'on appelle le crepuscule nautique, le Soleil doit se situer a 12° sous l'horizon. 
     * Pour le crepuscule astronomique, il faut 18°, mais parfois 15° suffisent aussi car a  ce moment la, il fait pratiquement deja  nuit.
     * @return Hauteur sur l'horizon du soleil pour une position et un instant donne
     */
    public static double getHSun(final long date, final Point2D coord) {       
        SunAtTime sun = new SunAtTime(GeophysicTools.toJulianDay(date)); 
        return sun.getElevation(coord.getY(), coord.getX());
    }
    
    
    /**
     * Calcul de la lumninositÃ© en un point du globe, Ã  une date donnÃ©e, incluant l'influence
     * du Soleil et de la Lune
     * @param date - timestamp en long
     * @param lat - latitude en degres
     * @param lon - longitude en degres
     * @return luminositée en lux
     */
    public static Double yallop(long date, double latitude, double longitude) {
        double jd = toJulianDay(date); 
        return yallop(jd, new SunAtTime(jd), new MoonAtTime(jd), latitude, longitude);
    }
    
    /**
     * 
     * @param jd
     * @param moon
     * @param parallaxe
     * @param latitude
     * @param longitude
     * @return La valeur de l'eclairage nocturne (en millilux)
     */
    public static Double yallop(final double jd, final SunAtTime sun, final MoonAtTime moon, final double latitude, final double longitude) {       
        // Recuperation du paralaxe        
        final double parallaxe = moon.getParallax();
        
        // Calcul de la hauteur sur l'horizon de la lune et du soleil
        final double hsol = sun.getElevation(latitude, longitude);
        double hlune = moon.getElevation(latitude, longitude);
        hlune -= parallaxe * Math.cos(toRad*hlune);

        // Calcul de l'eclairement selon Yallop
        final double m1, xm = hlune/90.;
        if (hlune >= 20.)                        m1 =    (((1.56 *xm) -   4.24) *xm +  4.06) *xm - 1.95;
        else if ((hlune < 20.) && (hlune >= 5.)) m1 =   (((59.06 *xm) -  42.58) *xm + 12.58) *xm - 2.58;
        else /*if (hlune < 5.)*/                 m1 = (((1321.29 *xm) - 252.95) *xm + 24.27) *xm - 2.79;

        final double incyall = toDeg * moon.getIncyall(sun);  // On passe en degres
        
        final double ml = m1 - 8.68e-3 * incyall -2.2e-9 * Math.pow(incyall,4) + 2. * Math.log10(parallaxe/0.951);
        final double xs = hsol/90.;
        final double tw = (((2797.93*xs) + 1447.42)*xs + 262.72)*xs + 13.84;
        return 1000.*(Math.pow(10.,tw)+Math.pow(10.,ml)+5.e-4);
    }
   
}
