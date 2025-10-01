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
public class CalendarCalculator2022 extends CalendarCalculator {
    
    public CalendarCalculator2022(final double longitude, final double latitude) {
        super(2022,longitude, latitude);
    }
    
    // Partie commune
    @Override
    protected List<Period> initHollidayAll() {
        final List<Period> lst = new ArrayList<>();
        // Noel
        lst.add(new Period(new DateTime(2021, 12, 18, 0, 0), new DateTime(2022, 1, 2, 23, 59)));
        // ete
        lst.add(new Period(new DateTime(2022, 7, 7, 0, 0), new DateTime(2022, 8, 31, 23, 59)));
        // Toussain
        lst.add(new Period(new DateTime(2022, 10, 22, 0, 0), new DateTime(2022, 11, 6, 23, 59)));
        // Noel
        lst.add(new Period(new DateTime(2022, 12, 17, 0, 0), new DateTime(2023, 1, 2, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_A() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(2022, 2, 12, 0, 0), new DateTime(2022, 2, 27, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(2022, 4, 16, 0, 0), new DateTime(2022, 5, 1, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_B() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(2022, 2, 5, 0, 0), new DateTime(2022, 2, 20, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(2022, 4, 9, 0, 0), new DateTime(2022, 4, 24, 23, 59)));
        return lst;
    }

    @Override
    protected List<Period> initHolliday_C() {
        final List<Period> lst = initHollidayAll();
        // Hivers
        lst.add(new Period(new DateTime(2022, 2, 19, 0, 0), new DateTime(2022, 3, 6, 23, 59)));
        // Printemps
        lst.add(new Period(new DateTime(2022, 4, 23, 0, 0), new DateTime(2022, 5, 8, 23, 59)));
        return lst;
    }

    @Override
    protected void initYearDependantInfo(int year) {
        // Saisons
        addInfo(year, 3, 20, "Prima");
        addInfo(year, 6, 21, "Estiu");
        addInfo(year, 9, 23, "Davalada");
        addInfo(year, 12, 21, "Ivèrn");
        
        // changement d heure
        addInfo(2022, 3, 27, "02:00 -> 03:00");
        addInfo(2022, 10, 30, "03:00 -> 02:00");

        // evenement astronomiques
        addInfo(year, 5, 16, "Eclipsi de Luna");
        addInfo(year, 8, 12, "Perséides");
        addInfo(year, 12, 14, "Géminides");

        addInfo(year, 1, 6, "Epifania");
        addInfo(year, 9, 1, "Dintrada", imgCartable); // rentree
       
        addInfo(year, 6, 4, "Fèsta de las maires"); // dernier dimanche de mai. sauf si = pentecote => premier dimanche de juin 
        addInfo(year, 6, 18, "Fèsta dels paires"); // peres - troisième dimanche de juin
        addInfo(year, 3, 5, "Fèsta de las mametas"); // grand meres - premier dimanche de mars.
        addInfo(year, 10, 1, "Fèsta dels papetas"); // grand - peres - premier dimanche du mois d'octobre

// Religieuses        
//        addInfo(year, 3, 2, "Cendres");        
//        addInfo(year, 3, 15, "Annonciation");            
//        addInfo(year, 4, 10, "Rameaux");
        addInfo(year, 5, 18, "Jeudi Saint");
//        addInfo(2022, 4, 17, "Pascas"); //Lundi de Pâques");
        addInfo(year, 5, 29, "Pentacosta");
//        addInfo(year, 11, 2, "Fête des morts");
    }
    
    @Override
    protected void initInfo(int year) {
        addInfo(year, 10, 31, "Martror", svgCitrouille.getImage());  //Halloween

        // fetes pour les proverbes        
        addInfo(year, 7, 29, "Santa Marta");
        addInfo(year, 6, 24, "Fèsta de la Tarasca");
        addInfo(year, 7, 14, "Volo Biòu");
     //   addInfo(year, 12, 6, "Sant Nicolau");
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
