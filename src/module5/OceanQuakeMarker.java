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
	List<Marker> quakeMarkers;
	UnfoldingMap map;
	PointFeature quake;
	public OceanQuakeMarker(PointFeature quake, UnfoldingMap map, List<Marker> cityMarkers, List<Marker> quakeMarkers) {
		super(quake);
		this.quake = quake;
		this.map = map;
		this.cityMarkers = cityMarkers;
		this.quakeMarkers = quakeMarkers;
		// setting field in earthquake marker
		isOnLand = false;
	}

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		//draw a line between city marker (if clicked)
		// and oceanquake marker within threat circle
		if(clicked){
			for (Marker city: cityMarkers) {
				double dist = city.getDistanceTo(quake.getLocation());
				final CityMarker cityMarker = (CityMarker) city;
				final ScreenPosition sp = cityMarker.getScreenPosition(map);
				if(dist<=threatCircle()) {
					pg.line(sp.x - 200, sp.y - 50, x, y);
				}
			}
		}
	}
}
