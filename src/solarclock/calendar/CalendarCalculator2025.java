package solarclock.calendar;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * This class helps to generate Calendar for given Year.
 *
 * @author dsahu1
 *
 */
public class CalendarCalculator2025 extends CalendarCalculator {
    
    public CalendarCalculator2025(final double longitude, final double latitude) {
        super(2025, longitude, latitude);
    }
    
    // Partie commune
    @Override
    protected List<Period> initHollidayAll() {// A METTRE A JOUR
        final List<Period> lst = new ArrayList<>();
        // Noel
        lst.add(new Period(new DateTime(year-1, 12, 22, 0, 0), new DateTime(year, 1, 5, 23, 59))); 
        // Pont ascencion
        lst.add(new Period(new DateTime(year, 5, 29, 0, 0), new DateTime(year, 6, 1, 23, 59))); 
        // ete
        lst.add(new Period(new DateTime(year, 7, 5, 0, 0), new DateTime(year, 8, 31, 23, 59)));  
        // Toussain
        lst.add(new Period(new DateTime(year, 10, 18, 0, 0), new DateTime(year, 11, 2, 23, 59))); 
        // Noel
        lst.add(new Period(new DateTime(year, 12, 20, 0, 0), new DateTime(year+1, 1, 4, 23, 59))); 
        return lst;
    }

    @Override
    protected List<Period> initHolliday_A() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(year, 2, 22, 0, 0), new DateTime(year, 3, 9, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(year, 4, 19, 0, 0), new DateTime(year, 5, 4, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_B() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(year, 2, 8, 0, 0), new DateTime(year, 2, 23, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(year, 4, 5, 0, 0), new DateTime(year, 4, 21, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_C() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(year, 2, 15, 0, 0), new DateTime(year, 3,2, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(year, 4, 12, 0, 0), new DateTime(year, 4, 27, 23, 59)));
        return lst;
    }

    @Override
    protected void initYearDependantInfo(int year) {
        // Saisons
        addInfo(year, 3, 20, "Prima");
        addInfo(year, 6, 21, "Estiu");
        addInfo(year, 9, 22, "Davalada");
        addInfo(year, 12, 21, "Ivèrn");
        
        // changement d heure
        addInfo(year, 3, 30, "02:00 -> 03:00"); 
        addInfo(year, 10, 26, "03:00 -> 02:00");

        // evenement astronomiques
//        addInfo(year, 5, 13, "Cometa 39P Oterma");
//        addInfo(year, 8, 12, "Perséides (estela limpaira)");
//        addInfo(year, 5, 13, "Cometa 103P Hartley");
//        addInfo(year, 10, 13, "Géminides (estela limpaira)");
      
        addInfo(year, 5, 25, "Fèsta de las maires"); // dernier dimanche de mai. sauf si = pentecote => premier dimanche de juin 
        addInfo(year, 6, 15, "Fèsta dels paires"); // peres - troisième dimanche de juin
        addInfo(year, 3, 2, "Fèsta de las mametas"); // grand meres - premier dimanche de mars.
        addInfo(year, 10,5, "Fèsta dels papetas"); // grand - peres - premier dimanche du mois d'octobre

//        addInfo(year, 1, 6, "Epifania");
        addInfo(year, 9, 1, "Dintrada", imgCartable); 

    }
    
    @Override
    protected void initInfo(int year) {
        addInfo(year, 10, 31, "Martror", svgCitrouille.getImage());  //Halloween

        addInfo(year, 3, 17, "Sant Patric");
        addInfo(year, 2, 14, "Sant Valentin");

        addInfo(year, 2, 2, "Fèsta dels pescajons");
        addInfo(year, 3, 1, "Dimars gras");

        addInfo(year, 6, 21, "Fèsta de la musica");
        addInfo(year, 6, 23, "Fèsta de Sant Joan");

        // Depend des annees
        initYearDependantInfo(year);
    }
}
