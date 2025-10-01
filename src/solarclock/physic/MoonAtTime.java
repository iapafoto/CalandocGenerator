/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarclock.physic;

/**
 *
 * @author sebastien.durand
 * TODO: à completer avec http://articles.adsabs.harvard.edu/cgi-bin/nph-iarticle_query?1979ApJS...41..391V&defaultprint=YES&filetype=.pdf
 */
public class MoonAtTime extends AstreAtTime {
    
    public static double scalingFactor = 60.40974;
    public Double parallaxe = null;
    
    public MoonAtTime(long timeUnixMs) {
        super(timeUnixMs);
    }
    
    public MoonAtTime(double jd) {
        super(jd);
    }
    
    @Override
    public void calculatePlanetPosition() {
        final double t1 = (julianDay-2451545.)/36525. + 1.;  // Nombres de siecles juliens depuis 1900 = nombre de siecle depuis 2000 + 1 !
        final double u,v,w;

        v = 0.39558*Math.sin(C[3]+C[5])                        
           +0.08200*Math.sin(C[3])                              
           +0.03257*Math.sin(C[2]-C[3]-C[5])                 
           +0.01092*Math.sin(C[2]+C[3]+C[5])                    
           +0.00666*Math.sin(C[2]-C[3])                      
           -0.00644*Math.sin(C[2]+C[3]-2.0*C[4]+C[5])        
           -0.00331*Math.sin(C[3]-2.0*C[4]+C[5])             
           -0.00304*Math.sin(C[3]-2.0*C[4])                  
           -0.00240*Math.sin(C[2]-C[3]-2.0*C[4]-C[5])        
           +0.00226*Math.sin(C[2]+C[3])                      
           -0.00108*Math.sin(C[2]+C[3]-2.0*C[4])             
           -0.00079*Math.sin(C[3]-C[5])                      
           +0.00078*Math.sin(C[3]+2.0*C[4]+C[5])        
           +0.00066*Math.sin(C[3]+C[5]-C[8])                 
           -0.00062*Math.sin(C[3]+C[5]+C[8])                 
           -0.00050*Math.sin(C[2]-C[3]-2.0*C[4])             
           +0.00045*Math.sin(2.0*C[2]+C[3]+C[5])             
           -0.00031*Math.sin(2.0*C[2]+C[3]-2.0*C[4]+C[5])    
           -0.00027*Math.sin(C[2]+C[3]-2.0*C[4]+C[5])        
           -0.00024*Math.sin(C[3]-2.0*C[4]+C[5]+C[8])        
           -0.00021*Math.sin(C[3]+C[5])*t1                    
           +0.00018*Math.sin(C[3]-C[4]+C[5])                 
           +0.00016*Math.sin(C[3]+2.0*C[4])                  
           +0.00016*Math.sin(C[2]-C[3]-C[5]-C[8])            
           -0.00016*Math.sin(2.0*C[2]-C[3]-C[5])             
           -0.00015*Math.sin(C[3]-2.0*C[4]+C[8])             
           -0.00012*Math.sin(C[2]-C[3]-2.0*C[4]-C[5]+C[8])   
           -0.00011*Math.sin(C[2]-C[3]-C[5]+C[8]);

        u = 1.00000                                     
           -0.10828*Math.cos(C[2])                           
           -0.01880*Math.cos(C[2]-2.0*C[4])                  
           -0.01479*Math.cos(2.0*C[4])                       
           +0.00181*Math.cos(2.0*C[2]-2.0*C[4])              
           -0.00147*Math.cos(2.0*C[2])                       
           -0.00105*Math.cos(2.0*C[4]-C[8])                  
           -0.00075*Math.cos(C[2]-2.0*C[4]+C[8])             
           -0.00067*Math.cos(C[2]-C[8])                      
           +0.00057*Math.cos(C[4])                           
           +0.00055*Math.cos(C[1]+C[8])                      
           -0.00046*Math.cos(C[2]+2.0*C[4])                  
           +0.00041*Math.cos(C[2]-2.0*C[3])                  
           +0.00024*Math.cos(C[8])                           
           +0.00017*Math.cos(2.0*C[4]+C[8])                  
           +0.00013*Math.cos(C[2]-2.0*C[4]-C[8])             
           -0.00010*Math.cos(C[2]-4.0*C[4]);                   

        w = 0.10478*Math.sin(C[2])                           
           -0.04105*Math.sin(2.0*C[3]+2.0*C[5])              
           -0.02130*Math.sin(C[2]-2.0*C[4])                  
           -0.01779*Math.sin(2.0*C[3]+C[5])                  
           +0.01774*Math.sin(C[5])                           
           +0.00987*Math.sin(2.0*C[4])                       
           -0.00338*Math.sin(C[2]-2.0*C[3]-2.0*C[5])         
           -0.00309*Math.sin(C[8])                           
           -0.00190*Math.sin(2.0*C[3])                       
           -0.00144*Math.sin(C[2]+C[5])                      
           -0.00144*Math.sin(C[2]-2.0*C[3]-C[5])             
           -0.00113*Math.sin(C[2]+2.0*C[3]+2.0*C[5])         
           -0.00094*Math.sin(C[2]-2.0*C[4]+C[8])             
           -0.00092*Math.sin(2.0*C[2]-2.0*C[4])              
           +0.00071*Math.sin(2.0*C[4]-C[8])                  
           +0.00070*Math.sin(2.0*C[2])                       
           +0.00067*Math.sin(C[2]+2.0*C[3]-2.0*C[4]+2.0*C[5])
           +0.00066*Math.sin(2.0*C[3]-2.0*C[4]+C[5])         
           -0.00066*Math.sin(2.0*C[4]+C[5])                  
           +0.00061*Math.sin(C[2]-C[8])                      
           -0.00058*Math.sin(C[4])                           
           -0.00049*Math.sin(C[2]+2.0*C[3]+C[5])             
           -0.00049*Math.sin(C[2]-C[5])                      
           -0.00042*Math.sin(C[2]+C[8])                      
           +0.00034*Math.sin(2.0*C[3]-2.0*C[4]+2.0*C[5])     
           -0.00026*Math.sin(2.0*C[3]-2.0*C[4])              
           +0.00025*Math.sin(C[2]-2.0*C[3]-2.0*C[4]-2.0*C[5])
           +0.00024*Math.sin(C[2]-2.0*C[3])                  
           +0.00023*Math.sin(C[2]+2.0*C[3]-2.0*C[4]+C[5])    
           +0.00023*Math.sin(C[2]-2.0*C[4]-C[5])             
           +0.00019*Math.sin(C[2]+2.0*C[4])                  
           +0.00012*Math.sin(C[2]-2.0*C[4]-C[8])             
           +0.00011*Math.sin(C[2]-2.0*C[4]+C[5])             
           +0.00011*Math.sin(C[2]-2.0*C[3]-2.0*C[4]-C[5])    
           -0.00010*Math.sin(2.0*C[4]+C[8]);
        
        // Calculate declination and right ascention
        coords(v,u,w,C[1]);
        distance = scalingFactor * Math.sqrt(u)* EARTH_MEAN_RADIUS; // Pour la lune, on ne calcul pas en AU, mais en earth radii !
    } 
        
//    @Override
    /** Moins precis mais un peu plus rapide du coup */
    public void calculatePlanetPosition2() {
        double t,Lm,Ms,v,u,w,s,Mm,Fm,D,gm;

        t  = julianDay-2451545.;  // nombre de jours depuis le 01/01/2000 (1 January 2000 = 2451545 Julian Days)    

        /* Calcul de la position lunaire
        *********************************/ 
        Lm = 0.606434 + 0.03660110129 * t;  // Mean longitude
        Mm = 0.374897 + 0.03629164709 * t;  // Mean anomaly
        Fm = 0.259091 + 0.03674819520 * t;  // argument of latitude
        D  = 0.827362 + 0.03386319198 * t;  // mean elongation of the moon from the sun
        gm = 0.347343 - 0.00014709391 * t;  // longitude of the lunar ascending node
        Ms = 0.993126 + 0.00273777850 * t;  // mean anomaly of sun !?

        Lm = (Lm - Math.floor(Lm))* pi2;  // dans certaines doc on trouve des ceil pluto !
        Mm = (Mm - Math.floor(Mm))* pi2;
        Fm = (Fm - Math.floor(Fm))* pi2;
        D =  (D  - Math.floor(D)) * pi2;
        gm = (gm - Math.floor(gm))* pi2;
        Ms = (Ms - Math.floor(Ms))* pi2;

        v =   0.39558 * Math.sin(Fm+gm)
            + 0.08200 * Math.sin(Fm)
            + 0.03257 * Math.sin(Mm-Fm-gm)
            + 0.01092 * Math.sin(Mm+Fm+gm)
            + 0.00666 * Math.sin(Mm-Fm)
            - 0.00644 * Math.sin(Mm+Fm-2.*D+gm)
            - 0.00331 * Math.sin(Fm-2.*D+gm)
            - 0.00304 * Math.sin(Fm-2.*D)
            - 0.00240 * Math.sin(Mm-Fm-2.*D-gm)
            + 0.00226 * Math.sin(Mm+Fm)
            - 0.00108 * Math.sin(Mm+Fm-2.*D)
            - 0.00079 * Math.sin(Fm-gm)
            + 0.00078 * Math.sin(Fm+2.*D+gm);

        u =   1.
            - 0.10828 * Math.cos(Mm)
            - 0.01880 * Math.cos(Mm-2.*D)
            - 0.01479 * Math.cos(2.*D)
            + 0.00181 * Math.cos(2.*Mm-2.*D)
            - 0.00147 * Math.cos(2.*Mm)
            - 0.00105 * Math.cos(2.*D-Ms)
            - 0.00075 * Math.cos(Mm-2.*D+Ms);

        w =   0.10478 * Math.sin(Mm)
            - 0.04105 * Math.sin(2.*Fm+2.*gm)
            - 0.02130 * Math.sin(Mm-2.*D)
            - 0.01779 * Math.sin(2.*Fm+gm)
            + 0.01774 * Math.sin(gm)
            + 0.00987 * Math.sin(2.*D)
            - 0.00338 * Math.sin(Mm-2.*Fm-2.*gm)
            - 0.00309 * Math.sin(Ms)
            - 0.00190 * Math.sin(2.*Fm)
            - 0.00144 * Math.sin(Mm+gm)
            - 0.00144 * Math.sin(Mm-2.*Fm-gm)
            - 0.00113 * Math.sin(Mm+2.*Fm+2.*gm)
            - 0.00094 * Math.sin(Mm-2.*D+Ms)
            - 0.00092 * Math.sin(2.*Mm-2.*D);

        s = w / Math.sqrt(u-v*v);
        rightAscention = Lm + Math.atan(s/Math.sqrt(1.-s*s));
        s = v / Math.sqrt(u);
        declination = Math.atan(s/Math.sqrt(1.-s*s));
        distance = scalingFactor * Math.sqrt(u)* EARTH_MEAN_RADIUS;
        sin_dec = Math.sin(declination); // optim
        cos_dec = Math.cos(declination);
    }       


    /**
     * Calcul de la parallaxe lunaire
     * @param jd - Julian Day
     * @return la parallaxe en degres
     */
    public double getParallax() {
        if (parallaxe == null) {
            parallaxe = calculateParallax(julianDay);
        }
        return parallaxe;
    }

    // Retourne le parallaxe en degres
    public static double calculateParallax(double jd) {
        /* Declarations des variables
        ******************************/
        double t,m,mprime,d,f,e,parallaxe;

        /* Pre-calculs de variables intermediaires
        *******************************************/
        t = (jd-2451545.)/36525.; // Nombre de siecles depuis 2000

        d      = toRad*( ((((- 1./1.13065e8)*t + 1./545868.)  *t - 0.0016300) *t + 445267.1115168) *t + 297.8502042);
        m      = toRad*(                      (((1./2.449e7)  *t - 0.0001536) *t +  35999.0502909) *t + 357.5291092);
        mprime = toRad*( ((((- 1./1.4712e7) *t + 1./6.9699e4) *t + 0.0089970) *t + 477198.8676313) *t + 134.9634114);
        f      = toRad*( (((  (1./8.6331e8) *t - 1./3.526e7)  *t - 0.0034029) *t + 483202.0175273) *t +  93.2720993);

        e =  ((-7.4e-6) *t - 0.002516) *t  + 1;

        /* Calcul de la parallaxe
        **************************/
        parallaxe =     0.950724
                  +     0.051818 * Math.cos(mprime)
                  +     0.009531 * Math.cos(2. * d - mprime)
                  +     0.007843 * Math.cos(2. * d)
                  +     0.002824 * Math.cos(2. * mprime)
                  +     0.000857 * Math.cos(2. * d + mprime)
                  + e * 0.000533 * Math.cos(2. * d - m)
                  + e * 0.000401 * Math.cos(2. * d - m - mprime)
                  + e * 0.000320 * Math.cos(mprime - m)
                  -     0.000271 * Math.cos(d)
                  - e * 0.000264 * Math.cos(m + mprime)
                  -     0.000198 * Math.cos(2. * f - mprime)
                  +     0.000173 * Math.cos(3. * mprime)
                  +     0.000167 * Math.cos(4. * d - mprime)
                  - e * 0.000111 * Math.cos(m)
                  +     0.000103 * Math.cos(4. * d - 2. * mprime)
                  -     0.000084 * Math.cos(2. * mprime - 2. * d)
                  - e * 0.000083 * Math.cos(2. * d + m)
                  +     0.000079 * Math.cos(2. * d + 2. * mprime)
                  +     0.000072 * Math.cos(4. * d)
                  + e * 0.000064 * Math.cos(2. * d - m + mprime)
                  - e * 0.000063 * Math.cos(2. * d + m - mprime)
                  + e * 0.000041 * Math.cos(m + d)
                  + e * 0.000035 * Math.cos(2. * mprime - m)
                  -     0.000033 * Math.cos(3. * mprime - 2. * d)
                  -     0.000030 * Math.cos(mprime + d)
                  -     0.000029 * Math.cos(2. * f - 2. * d)
                  - e * 0.000029 * Math.cos(2. * mprime + m)
                  +e*e* 0.000026 * Math.cos(2. * d - 2. * m)
                  -     0.000023 * Math.cos(2. * f - 2. * d + mprime)
                  + e * 0.000019 * Math.cos(4. * d - m - mprime);

        return parallaxe;
    }

    public double getIncyall(final SunAtTime sun) { // Moon's age ? = nb day past new moon mais là c'est en radian !
        double zphi = Math.acos(sun.sin_dec*sin_dec + sun.cos_dec*cos_dec*Math.cos(sun.rightAscention-rightAscention));
        return Math.atan2(sun.distance * Math.sin(zphi), distance - sun.distance*Math.cos(zphi) );
    }

    public double getPhase(final SunAtTime sun) {
        return (1. + Math.cos(getIncyall(sun)))/2.;
    }

    
    public Vector3D getGeocentricPosition(final double jdate) {
        
        final double 
                atr = Math.PI / 648000.,
                djd = jdate - 2451545.,
                t = djd / 36525. + 1.;
        final double
                gm = Planet.r2r(0.374897 + 0.03629164709 * djd),
                gm2 = 2 * gm,
                gm3 = 3 * gm,
                fm = Planet.r2r(0.259091 + 0.0367481952 * djd),
                fm2 = 2 * fm,
                em = Planet.r2r(0.827362 + 0.03386319198 * djd),
                em2 = 2 * em,
                em4 = 4 * em,
                gs = Planet.r2r(0.993126 + 0.0027377785 * djd),
                lv = Planet.r2r(0.505498 + 0.00445046867 * djd),
                lm = Planet.r2r(0.606434 + 0.03660110129 * djd),
                ls = Planet.r2r(0.779072 + 0.00273790931 * djd),
                rm = Planet.r2r(0.347343 - 0.00014709391 * djd);

        // geocentric, ecliptic longitude of the moon (radians)

        double l = 22640 * Math.sin(gm) - 4586 * Math.sin(gm - em2) + 2370 * Math.sin(em2);
        l = l + 769 * Math.sin(gm2) - 668 * Math.sin(gs) - 412 * Math.sin(fm2);
        l = l - 212 * Math.sin(gm2 - em2) - 206 * Math.sin(gm - em2 + gs);
        l = l + 192 * Math.sin(gm + em2) + 165 * Math.sin(em2 - gs);
        l = l + 148 * Math.sin(gm - gs) - 125 * Math.sin(em) - 110 * Math.sin(gm + gs);
        l = l - 55 * Math.sin(fm2 - em2) - 45 * Math.sin(gm + fm2) + 40 * Math.sin(gm - fm2);
        l = l - 38 * Math.sin(gm - em4) + 36 * Math.sin(gm3) - 31 * Math.sin(gm2 - em4);
        l = l + 28 * Math.sin(gm - em2 - gs) - 24 * Math.sin(em2 + gs) + 19 * Math.sin(gm - em);
        l = l + 18 * Math.sin(em + gs) + 15 * Math.sin(gm + em2 - gs) + 14 * Math.sin(gm2 + em2);
        l = l + 14 * Math.sin(em4) - 13 * Math.sin(gm3 - em2) - 17 * Math.sin(rm);
        l = l - 11 * Math.sin(gm + 16 * ls - 18 * lv) + 10 * Math.sin(gm2 - gs) + 9 * Math.sin(gm - fm2 - em2);
        l = l + 9 * (Math.cos(gm + 16 * ls - 18 * lv) - Math.sin(gm2 - em2 + gs)) - 8 * Math.sin(gm + em);
        l = l + 8 * (Math.sin(2 * (em - gs)) - Math.sin(gm2 + gs)) - 7 * (Math.sin(2 * gs)  + Math.sin(gm - 2 * (em - gs)) - Math.sin(rm));
        l = l - 6 * (Math.sin(gm - fm2 + em2) + Math.sin(fm2 + em2)) - 4 * (Math.sin(gm - em4 + gs) - t * Math.cos(gm + 16 * ls - 18 * lv));
        l = l - 4 * (Math.sin(gm2 + fm2) - t * Math.sin(gm + 16 * ls - 18 * lv));
        l = l + 3 * (Math.sin(gm - 3 * em) - Math.sin(gm + em2 + gs) - Math.sin(gm2 - em4 + gs) + Math.sin(gm - 2 * gs) + Math.sin(gm - em2 - 2 * gs));
        l = l - 2 * (Math.sin(gm2 - em2 - gs) + Math.sin(fm2 - em2 + gs) - Math.sin(gm + em4));
        l = l + 2 * (Math.sin(4 * gm) + Math.sin(em4 - gs) + Math.sin(gm2 - em));

        double plon = lm + atr * l;

        // geocentric, ecliptic latitude of the moon (radians)

        double b = 18461 * Math.sin(fm) + 1010 * Math.sin(gm + fm) + 1000 * Math.sin(gm - fm);
        b = b - 624 * Math.sin(fm - em2) - 199 * Math.sin(gm - fm - em2) - 167 * Math.sin(gm + fm - em2);
        b = b + 117 * Math.sin(fm + em2) + 62 * Math.sin(gm2 + fm) + 33 * Math.sin(gm - fm + em2);
        b = b + 32 * Math.sin(gm2 - fm) - 30 * Math.sin(fm - em2 + gs) - 16 * Math.sin(gm2 - em2 + fm);
        b = b + 15 * Math.sin(gm + fm + em2) + 12 * Math.sin(fm - em2 - gs) - 9 * Math.sin(gm - fm - em2 + gs);
        b = b - 8 * (Math.sin(fm + rm) - Math.sin(fm + em2 - gs))  - 7 * Math.sin(gm + fm - em2 + gs);
        b = b + 7 * (Math.sin(gm + fm - gs) - Math.sin(gm + fm - em4));
        b = b - 6 * (Math.sin(fm + gs) + Math.sin(3 * fm) - Math.sin(gm - fm - gs));
        b = b - 5 * (Math.sin(fm + em) + Math.sin(gm + fm + gs) + Math.sin(gm - fm + gs) - Math.sin(fm - gs) - Math.sin(fm - em));
        b = b + 4 * (Math.sin(gm3 + fm) - Math.sin(fm - em4)) - 3 * (Math.sin(gm - fm - em4)  - Math.sin(gm - 3 * fm));
        b = b - 2 * (Math.sin(gm2 - fm - em4) + Math.sin(3 * fm - em2) - Math.sin(gm2 - fm + em2) - Math.sin(gm - fm + em2 - gs));

        double plat = atr * (b + 2 * (Math.sin(gm2 - fm - em2) + Math.sin(gm3 - fm)));

        // obliquity of the ecliptic (radians)

        double obliq = atr * (84428 - 47 * t + 9 * Math.cos(rm));

        // geocentric distance (kilometers)

        double r = 60.36298 - 3.27746 * Math.cos(gm) - .57994 * Math.cos(gm - em2);
        r = r - .46357 * Math.cos(em2) - .08904 * Math.cos(gm2) + .03865 * Math.cos(gm2 - em2);
        r = r - .03237 * Math.cos(em2 - gs) - .02688 * Math.cos(gm + em2) - .02358 * Math.cos(gm - em2 + gs);
        r = r - .0203 * Math.cos(gm - gs) + .01719 * Math.cos(em) + .01671 * Math.cos(gm + gs);
        r = r + .01247 * Math.cos(gm - fm2) + .00704 * Math.cos(gs) + .00529 * Math.cos(em2 + gs);
        r = r - .00524 * Math.cos(gm - em4) + .00398 * Math.cos(gm - em2 - gs) - .00366 * Math.cos(gm3);
        r = r - .00295 * Math.cos(gm2 - em4) - .00263 * Math.cos(em + gs) + .00249 * Math.cos(gm3 - em2);
        r = r - .00221 * Math.cos(gm + em2 - gs) + .00185 * Math.cos(fm2 - em2) - .00161 * Math.cos(2 * (em - gs));
        r = r + 0.00147 * Math.cos(gm + fm2 - em2) - 0.00142 * Math.cos(em4) + 0.00139 * Math.cos(gm2 - em2 + gs);

        double rmm = 6378.14 * (r - 0.00118 * Math.cos(gm - em4 + gs) - 0.00116 * Math.cos(gm2 + em2) - 0.0011 * Math.cos(gm2 - gs));

              // geocentric, equatorial right ascension and declination (radians)

        double a = Math.sin(plon) * Math.cos(obliq) - Math.tan(plat) * Math.sin(obliq);
        b = Math.cos(plon);

        double rasc = atan3(a, b);

        double decl = Math.asin(Math.sin(plat) * Math.cos(obliq) + Math.cos(plat) * Math.sin(obliq) * Math.sin(plon));

        // geocentric position vector of the moon (kilometers)
        return new Vector3D(
            rmm * Math.cos(rasc) * Math.cos(decl),
            rmm * Math.sin(rasc) * Math.cos(decl),
            rmm * Math.sin(decl));
    }

     /**
     * four quadrant inverse tangent
     * a = sine of angle
     * b = cosine of angle
     * output:  angle (radians; 0 =< c <= 2 * pi)
     */
    private double atan3(double a, double b) {
        double epsilon = 0.0000000001,
                pidiv2 = 0.5 * Math.PI;
        if (Math.abs(a) < epsilon)
           return (1 - sign(b)) * pidiv2;
        else {
           double c = (2 - sign(a)) * pidiv2;

            if (Math.abs(b) < epsilon) {
               return c;
            } else {
               return c + sign(a) * sign(b) * (Math.abs(Math.atan(a / b)) - pidiv2);
            }
        }
    }
    
    private int sign(double x) {
        return (x==0)?0:(x<0?-1:1);
    }
}
