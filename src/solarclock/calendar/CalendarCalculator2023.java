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
public class CalendarCalculator2023 extends CalendarCalculator {
    
    public CalendarCalculator2023(final double longitude, final double latitude) {
        super(2023, longitude, latitude);
    }
    
    // Partie commune
    @Override
    protected List<Period> initHollidayAll() {// A METTRE A JOUR
        final List<Period> lst = new ArrayList<>();
        // Noel
        lst.add(new Period(new DateTime(2022, 12, 17, 0, 0), new DateTime(2023, 1, 2, 23, 59)));
        // Pont ascencion
        lst.add(new Period(new DateTime(2023, 5, 18, 0, 0), new DateTime(2023, 5, 21, 23, 59))); 
        // ete
        lst.add(new Period(new DateTime(2023, 7, 8, 0, 0), new DateTime(2023, 8, 31, 23, 59)));  // A METTRE A JOUR
        // Toussain
   //     lst.add(new Period(new DateTime(2023, 10, 21, 0, 0), new DateTime(2023, 11, 5, 23, 59))); // A METTRE A JOUR
        // Noel
   //     lst.add(new Period(new DateTime(2023, 12, 16, 0, 0), new DateTime(2024, 1, 2, 23, 59))); // A METTRE A JOUR
        return lst;
    }

    @Override
    protected List<Period> initHolliday_A() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(2023, 2, 4, 0, 0), new DateTime(2023, 2, 19, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(2023, 4, 8, 0, 0), new DateTime(2023, 4, 23, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_B() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(2023, 2, 11, 0, 0), new DateTime(2023, 2, 26, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(2023, 4, 15, 0, 0), new DateTime(2023, 5, 1, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_C() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(2023, 2, 18, 0, 0), new DateTime(2023, 3, 5, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(2023, 4, 22, 0, 0), new DateTime(2023, 5, 8, 23, 59)));
        return lst;
    }

    @Override
    protected void initYearDependantInfo(int year) {
        // Saisons
        addInfo(2023, 3, 20, "Prima");
        addInfo(2023, 6, 21, "Estiu");
        addInfo(2023, 9, 23, "Davalada");
        addInfo(2023, 12, 22, "Ivèrn");
        
        // changement d heure
        addInfo(2023, 3, 26, "02:00 -> 03:00"); 
        addInfo(2023, 10, 29, "03:00 -> 02:00");

        // evenement astronomiques
        addInfo(2023, 5, 13, "Cometa 39P Oterma");
        addInfo(2023, 8, 12, "Perséides (estela limpaira)");
        addInfo(2023, 5, 13, "Cometa 103P Hartley");
        addInfo(2023, 10, 13, "Géminides (estela limpaira)");
      
        addInfo(2023, 6, 4, "Fèsta de las maires"); // dernier dimanche de mai. sauf si = pentecote => premier dimanche de juin 
        addInfo(2023, 6, 18, "Fèsta dels paires"); // peres - troisième dimanche de juin
        addInfo(2023, 3, 5, "Fèsta de las mametas"); // grand meres - premier dimanche de mars.
        addInfo(2023, 10, 1, "Fèsta dels papetas"); // grand - peres - premier dimanche du mois d'octobre

        addInfo(2023, 1, 6, "Epifania");
//        addInfo(2023, 9, 4, "Dintrada", imgCartable); // rentree A METTRE A JOUR
        
        addInfo(2023, 7, 29, "Santa Marta");
        addInfo(2023, 6, 24, "Fèsta de la Tarasca");
        addInfo(2023, 7, 14, "Volo-Biòu");
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
