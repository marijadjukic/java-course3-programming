package module5;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	List<Marker> cityMarkers;
	UnfoldingMap map;

	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
		PApplet p = new PApplet();
		String cityFile = "city-data.json";
		List<Feature> cities = GeoJSONReader.loadData(p, cityFile);
		cityMarkers = new ArrayList<Marker>();
		map = new UnfoldingMap(p, 200, 50, 650, 600, new Microsoft.RoadProvider());
		for (Feature city: cities) {
			cityMarkers.add(new CityMarker(city));
		}
	}

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		//draw a line between city marker (if clicked)
		// and oceanquake marker within threat circle
		for (Marker city: cityMarkers) {
			Location loc1 = city.getLocation();
			Location loc2 = new Location(x,y);
			double dist = city.getDistanceTo(loc2);
			if(clicked){
				//if(dist<=threatCircle()){
					ScreenPosition sp1 = map.getScreenPosition(city.getLocation());
					ScreenPosition sp2 = new ScreenPosition(x,y);
					pg.line(sp1.x-200+(sp2.x-x), sp1.y-50+(sp2.y-y),sp2.x,sp2.y);
				//}
			}

		}

	}
}
