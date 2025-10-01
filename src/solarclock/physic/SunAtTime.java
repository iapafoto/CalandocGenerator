/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarclock.physic;

import org.joda.time.DateTime;


/**
 *
 * @author doudou
 */
public class SunAtTime extends AstreAtTime {
    public static double AU = 149597870.61; // Astronomical Unit = Mean distance of the earth from the sun - Km
    public static double scalingFactor = 1.00021;
    
    public SunAtTime(long timeUnixMs) {
        super(timeUnixMs);
    }
  
    public SunAtTime(double jd) {
        super(jd);
    }
    
    public static void calculateSunPositionFast(long unixTimeMs) {
        double julianDay = (2440587.5 + (unixTimeMs / 86400000.0));
        double tt  = julianDay-2451545.;
        double t0 = tt/36525.;    // Nombre de siecles depuis 2000

        final double t  = julianDay-2451545.,  // nombre de jours depuis le 01/01/2000 (1 January 2000 = 2451545 Julian Days)
                Ls = 0.779072+0.00273790931*t,
                Gs = 0.993126+0.00273777850*t,
        v =  0.39785*Math.sin(Ls) - 0.01000*Math.sin(Ls-Gs),
        u =  1.0000 - 0.03349*Math.cos(Gs), 
        w =  0.03211*Math.sin(Gs) - 0.04129*Math.sin(Ls*2.0);
        
        // Calculate declination and right ascention          
        double w1 = w / Math.sqrt(u-v*v);
        double v1 = v / Math.sqrt(u);
        // this should never happen
        if (Math.abs(w1) > 1.0) w1 = (w1<0.0) ? -1.0 : 1.0; 
        if (Math.abs(v1) > 1.0) v1 = (v1<0.0) ? -1.0 : 1.0; 
        
        double rightAscention = Ls + Math.atan(w1/Math.sqrt(1.-w1*w1));
        double declination = Math.atan(v1/Math.sqrt(1.-v*v));
        double distance = scalingFactor * Math.sqrt(u) * AU;
    }
              
    @Override
    public void calculatePlanetPosition() {
     //   calculatePlanetPosition2();
        
        final double t1 = (julianDay-2451545.)/36525. + 1.;  // Nombres de siecles juliens depuis 1900 = nombre de siecle depuis 2000 + 1 !
        final double u,v,w;

        v =  0.39785*Math.sin(C[7])                         
            -0.01000*Math.sin(C[7]-C[8])                    
            +0.00333*Math.sin(C[7]+C[8])
            -0.00021*Math.sin(C[7])*t1 
            +0.00004*Math.sin(C[7]+2.0*C[8]) 
            -0.00004*Math.cos(C[7])
            -0.00004*Math.sin(C[5]-C[7])
            +0.00003*Math.sin(C[7]-C[8])*t1;  

        u =  1.0000
            -0.03349*Math.cos(C[8])
            -0.00014*Math.cos(C[8]*2.0)
            +0.00008*Math.cos(C[8])*t1
            -0.00003*Math.sin(C[8]-C[19]);

        w =  0.03211*Math.sin(C[8])
            -0.04129*Math.sin(C[7]*2.0)
            +0.00104*Math.sin(2.0*C[7]-C[8])
            -0.00035*Math.sin(2.0*C[7]+C[8])-0.0001
            -0.00008*Math.sin(C[8])*t1
            -0.00008*Math.sin(C[5])
            +0.00007*Math.sin(2.0*C[8])
            +0.00005*Math.sin(2.0*C[7])*t1
            +0.00003*Math.sin(C[1]-C[7])
            -0.00002*Math.cos(C[8]-C[19])
            +0.00002*Math.sin(4.0*C[8]-8.0*C[16]+3.0*C[19])
            -0.00002*Math.sin(C[8]-C[13])
            -0.00002*Math.cos(2.0*C[8]-2.0*C[13]);
        
        // Calculate declination and right ascention
        coords(v,u,w,C[7]);
        distance = scalingFactor * Math.sqrt(u) * AU;

    }
     
//    @Override
    public void calculatePlanetPosition2() {
        double t,t0,t1, Ls,Ms,v,u,w,zs;

        t  = julianDay-2451545.;  // nombre de jours depuis le 01/01/2000 (1 January 2000 = 2451545 Julian Days)
        t0 = t/36525.;  // Nombre de siecles depuis 2000        
        t1 = t0 + 1.;   // Nombres de siecles juliens depuis 1900 = nombre de siecle depuis 2000 + 1 !
    
        Ls = 0.779072 + 0.00273790931 * t;  // mean longitude of sun
        Ms = 0.993126 + 0.0027377785 * t;    // mean anomaly of sun

        Ls = (Ls - Math.floor(Ls))*pi2;
        Ms = (Ms - Math.floor(Ms))*pi2;
        
        v = (0.39785 - 0.00021 * t1) * Math.sin(Ls)
             - 0.01 * Math.sin(Ls-Ms)
             + 0.00333 * Math.sin(Ls+Ms);
        u = 1. - 0.03349 * Math.cos(Ms)
             - 0.00014 * Math.cos(2.*Ls)
             + 0.00008 * Math.cos(Ls);
        w = -0.0001 
             - 0.04129 * Math.sin(2.*Ls)
             + (0.03211 - 0.00008 * t1)* Math.sin(Ms)
             + 0.00104 * Math.sin(2.*Ls-Ms)
             - 0.00035 * Math.sin(2.*Ls+Ms);
        
        zs = w / Math.sqrt(u-v*v);
        rightAscention = Ls + Math.atan(zs/Math.sqrt(1.-zs*zs));
        zs = v / Math.sqrt(u);
        declination = Math.atan(zs/Math.sqrt(1.-zs*zs));
        distance = scalingFactor * Math.sqrt(u) * AU;
        sin_dec = Math.sin(declination); // optim
        cos_dec = Math.cos(declination);
    }

}
