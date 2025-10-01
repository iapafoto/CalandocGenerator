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
public class CalendarCalculator2024 extends CalendarCalculator {
    
    public CalendarCalculator2024(final double longitude, final double latitude) {
        super(2024, longitude, latitude);
    }
    
    // Partie commune
    @Override
    protected List<Period> initHollidayAll() {// A METTRE A JOUR
        final List<Period> lst = new ArrayList<>();
        // Noel
        lst.add(new Period(new DateTime(year-1, 12, 23, 0, 0), new DateTime(year, 1, 7, 23, 59)));
        // Pont ascencion
        lst.add(new Period(new DateTime(year, 5, 8, 0, 0), new DateTime(year, 5, 12, 23, 59))); 
        // ete
        lst.add(new Period(new DateTime(year, 7, 6, 0, 0), new DateTime(year, 9, 1, 23, 59)));  
        // Toussain
        lst.add(new Period(new DateTime(year, 10, 19, 0, 0), new DateTime(year, 11, 3, 23, 59))); 
        // Noel
        lst.add(new Period(new DateTime(year, 12, 21, 0, 0), new DateTime(year+1, 1, 5, 23, 59))); 
        return lst;
    }

    @Override
    protected List<Period> initHolliday_A() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(year, 2, 17, 0, 0), new DateTime(year, 3, 3, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(year, 4, 13, 0, 0), new DateTime(year, 4, 28, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_B() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(year, 2, 24, 0, 0), new DateTime(year, 3, 10, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(year, 4, 20, 0, 0), new DateTime(year, 5, 5, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_C() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(year, 2, 10, 0, 0), new DateTime(year, 2,25, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(year, 4, 6, 0, 0), new DateTime(year, 4, 21, 23, 59)));
        return lst;
    }

    @Override
    protected void initYearDependantInfo(int year) {
        // Saisons
        addInfo(year, 3, 20, "Prima");
        addInfo(year, 6, 20, "Estiu");
        addInfo(year, 9, 22, "Davalada");
        addInfo(year, 12, 21, "Ivèrn");
        
        // changement d heure
        addInfo(year, 3, 31, "02:00 -> 03:00"); 
        addInfo(year, 10, 27, "03:00 -> 02:00");

        // evenement astronomiques
//        addInfo(year, 5, 13, "Cometa 39P Oterma");
//        addInfo(year, 8, 12, "Perséides (estela limpaira)");
//        addInfo(year, 5, 13, "Cometa 103P Hartley");
//        addInfo(year, 10, 13, "Géminides (estela limpaira)");
      
        addInfo(year, 5, 26, "Fèsta de las maires"); // dernier dimanche de mai. sauf si = pentecote => premier dimanche de juin 
        addInfo(year, 6, 16, "Fèsta dels paires"); // peres - troisième dimanche de juin
        addInfo(year, 3, 3, "Fèsta de las mametas"); // grand meres - premier dimanche de mars.
        addInfo(year, 10,6, "Fèsta dels papetas"); // grand - peres - premier dimanche du mois d'octobre

//        addInfo(year, 1, 6, "Epifania");
        addInfo(year, 9, 2, "Dintrada", imgCartable); 

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
