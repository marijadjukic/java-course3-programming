package module6;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	PImage img;
	PImage ieImg;
	PImage homeImg;
	List<Marker> airportList;
	UnfoldingMap map;
	HashMap<String, Integer> countRoutes;
	
	public AirportMarker(Feature city, PImage img,PImage ieImg, List<Marker> airportList,
						 PImage homeImg, UnfoldingMap map,HashMap<String, Integer> countRoutes) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		this.img = img;
		this.ieImg = ieImg;
		this.airportList = airportList;
		this.homeImg = homeImg;
		this.map = map;
		this.countRoutes = countRoutes;
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		//pg.fill(11);
		//pg.ellipse(x, y, 5, 5);

		if(getName().substring(1,getName().length()-1).equals("Zagreb")) {
			pg.image(homeImg, x - 7, y - 7, 15, 15);
		}
		else if(getName().substring(1,getName().length()-1).equals("Dublin")){
			pg.image(ieImg, x - 7, y - 7, 15, 15);
		} else {
			pg.image(img,x-6.5f,y-6.5f,15,15);
		}
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		String title = getName().substring(1,getName().length()-1) + ", "
				+ getCity().substring(1,getCity().length()-1) + ", "
				+ getCountry().substring(1,getCountry().length()-1);
		String code = "Airport code: " + getCode().substring(1,getCode().length()-1);
		String numRoutes;
		if(countRoutes.get(this.getStringProperty("id"))!= null) {
			numRoutes = "Number of routes: " + countRoutes.get(this.getStringProperty("id"));
		} else {
			numRoutes = "Number of routes: 0";
		}

		pg.pushStyle();

		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-5-39, Math.max(pg.textWidth(title), pg.textWidth(code)) + 6, 55);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(title, x + 3, y - 5 - 33);
		pg.text(code, x + 3, y - 5 - 18);
		pg.text(numRoutes, x + 3, y - 5 - 3);

		pg.popStyle();
		// show routes

	}

	public String getName() {
		return (String) getProperty("name");
	}

	public String getCity() {
		return (String) getProperty("city");
	}

	public String getCountry() {
		return (String) getProperty("country");
	}

	public String getCode() {
		return (String) getProperty("code");
	}

}
