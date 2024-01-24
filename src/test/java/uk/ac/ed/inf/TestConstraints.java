package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.DayOfWeek;

public class TestConstraints {
    public static NamedRegion[] getNoFlyZones() {
        return new NamedRegion[]{
                new NamedRegion("George Square Area", new LngLat[]{
                        new LngLat(-3.190578818321228, 55.94402412577528),
                        new LngLat(-3.1899887323379517, 55.94284650540911),
                        new LngLat(-3.187097311019897, 55.94328811724263),
                        new LngLat(-3.187682032585144, 55.944477740393744),
                        new LngLat(-3.190578818321228, 55.94402412577528)
                }),
                new NamedRegion("Dr Elsie Inglis Quadrangle", new LngLat[]{
                        new LngLat(-3.1907182931900024, 55.94519570234043),
                        new LngLat(-3.1906163692474365, 55.94498241796357),
                        new LngLat(-3.1900262832641597, 55.94507554227258),
                        new LngLat(-3.190133571624756, 55.94529783810495),
                        new LngLat(-3.1907182931900024, 55.94519570234043)
                }),
                new NamedRegion("Bristo Square Open Area", new LngLat[]{
                        new LngLat(-3.189543485641479, 55.94552313663306),
                        new LngLat(-3.189382553100586, 55.94553214854692),
                        new LngLat(-3.189259171485901, 55.94544803726933),
                        new LngLat(-3.1892001628875732, 55.94533688994374),
                        new LngLat(-3.189194798469543, 55.94519570234043),
                        new LngLat(-3.189135789871216, 55.94511759833873),
                        new LngLat(-3.188138008117676, 55.9452738061846),
                        new LngLat(-3.1885510683059692, 55.946105902745614),
                        new LngLat(-3.1895381212234497, 55.94555918427592),
                        new LngLat(-3.189543485641479, 55.94552313663306)
                }),
                new NamedRegion("Bayes Central Area", new LngLat[]{
                        new LngLat(-3.1876927614212036, 55.94520696732767),
                        new LngLat(-3.187555968761444, 55.9449621408666),
                        new LngLat(-3.186981976032257, 55.94505676722831),
                        new LngLat(-3.1872327625751495, 55.94536993377657),
                        new LngLat(-3.1874459981918335, 55.9453361389472),
                        new LngLat(-3.1873735785484314, 55.94519344934259),
                        new LngLat(-3.1875935196876526, 55.94515665035927),
                        new LngLat(-3.187624365091324, 55.94521973430925),
                        new LngLat(-3.1876927614212036, 55.94520696732767)
                }),
        };
    }

    public static NamedRegion getCentralArea() {
        return new NamedRegion(
                "central",
                new LngLat[]{new LngLat(-3.192473, 55.946233), new LngLat(-3.192473, 55.942617), new LngLat(-3.184319, 55.942617), new LngLat(-3.184319, 55.946233)}
        );
    }

    public static Restaurant[] getDefinedRestaurants() {
        return new Restaurant[]{
                new Restaurant(
                        "Civerinos Slice",
                        new LngLat(-3.1912869215011597,55.945535152517735),
                        new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[]{new Pizza("Margarita", 1000), new Pizza("Calzone", 1400)}
                ),
                new Restaurant(
                        "Sora Lella Vegan Restaurant",
                        new LngLat(-3.202541470527649, 55.943284737579376),
                        new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                        new Pizza[]{new Pizza("Meat Lover", 1400), new Pizza("Vegan Delight", 1100)}
                ),
                new Restaurant(
                        "Domino's Pizza - Edinburgh - Southside",
                        new LngLat(-3.1838572025299072, 55.94449876875712),
                        new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)}
                ),
                new Restaurant(
                        "Sodeberg Pavilion",
                        new LngLat(-3.1940174102783203, 55.94390696616939),
                        new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[]{new Pizza("Proper Pizza", 1400), new Pizza("Pineapple & Ham & Cheese", 900)}
                )
        };
    }
}
