package module6;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PImage;

/** An applet that shows airports (and routes)
 * on a world map.
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet{

	UnfoldingMap map;
	private List<Marker> airportList;
	private List<Marker> airplaneCrashesList;
	List<Marker> routeList;
	private List<String> top100cities;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	PImage img;
	PImage ieImg;
	PImage homeImg;
	PImage warning;
	HashMap<String,Integer> countRoutes;

	public void setup() {
		// setting up PAppler
		size(800,600, OPENGL);

		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);

		img = loadImage("pink-airplane-md.png");
		ieImg = loadImage("green-airplane.png");
		homeImg = loadImage("unnamed.png");
		warning = loadImage("warning-icon.png");

		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		List<PointFeature> crashes = ParseFeed.parseAirplaneCrashes(this,"airplane_crashes.txt");

		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		airplaneCrashesList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		countRoutes = new HashMap<String, Integer>();

		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature,img,ieImg, airportList, homeImg, map, countRoutes);

			m.setRadius(10);
			airportList.add(m);

			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());

		}

		for(PointFeature feature : crashes){
			AirplaneCrashMarker acm = new AirplaneCrashMarker(feature,warning);
			acm.setRadius(10);
			airplaneCrashesList.add(acm);
		}

		map.addMarkers(airplaneCrashesList);

		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {

			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));

			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}

			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());

			//System.out.println(sl.getProperties());

			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
		}

		for (Marker route : routeList) {
			SimpleLinesMarker routeLine = (SimpleLinesMarker) route;
			String id = routeLine.getStringProperty("source");
			if (!countRoutes.containsKey(id)) {
				countRoutes.put(id, 1);
			} else {
				countRoutes.put(id, countRoutes.get(id) + 1);
			}
		}


		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		hideRoutes();

		top100cities = new ArrayList<String>();
		File f = new File("data/top100cities.txt");
		try {
			Scanner inputFile = new Scanner(f);
			while (inputFile.hasNext()) {
				top100cities.add(inputFile.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for(String city : top100cities){
			for(Marker m : airportList){
				String airport = m.getStringProperty("city");
				int length = airport.length();
				String airportCity = airport.substring(1,length-1);
				if(city.equals(airportCity)){
					map.addMarkers(m);
				}
			}
		}


	}

	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;

		}
		selectMarkerIfHover(airportList);
		selectMarkerIfHover(airplaneCrashesList);
		//loop();
	}

	// If there is a marker selected
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}

		for (Marker m : markers)
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}

	public void mouseClicked() {

		if (lastClicked != null) {
			unhideAirports();
			hideRoutes();
			lastClicked = null;
		}
		else if (lastClicked == null) {
			airportClicked();
		}
	}

	private void airportClicked(){
		if (lastClicked != null) return;
		for (Marker airport : airportList) {
			if (!airport.isHidden() && airport.isInside(map, mouseX, mouseY)) {
				lastClicked = (CommonMarker)airport;
				for (Marker mhide : airportList) {
					if (mhide != lastClicked) {
						mhide.setHidden(true);
					}
				}
				for (Marker route : routeList) {
					if(airport.getStringProperty("city").equals("\"Zagreb\"")){
						route.setColor(color(255,0,127));
					} else if(airport.getStringProperty("city").equals("\"Dublin\"")){
						route.setColor(color(0,204,0));
					} else {
						route.setColor(color(60,60,60));
					}
					//System.out.println(routeLine.getStringProperty("source"));
					//System.out.println("id " + airport.getStringProperty("id"));
					if (route.getStringProperty("source").equals(airport.getStringProperty("id"))) {
						route.setHidden(false);
					}
				}
				return;
			}
		}

	}

	private void unhideAirports(){
		for(Marker airport : airportList){
			airport.setHidden(false);
		}
	}

	private void hideRoutes(){
		for(Marker route : routeList){
			route.setHidden(true);
		}
	}


	private void addKey() {
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		stroke(255,0,0);
		rect(25,30,240,160);

		noStroke();
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("AIRPORT KEY", 90, 47);

		image(img,30,65,15,15);
		text("Airports of top 100 largest cities",50,70);

		image(homeImg,30,95,15,15);
		text("Zagreb Airport (hometown)",50,100);

		image(ieImg,30,125,15,15);
		text("Dublin Airport",50,130);

		stroke(color(51,0,25));
		line(28,157,47,157);
		noStroke();
		fill(0);
		text("route",50,155);

		image(warning,30,168,15,15);
		text("airplane crashes",50,175);

	}


	public void draw() {
		background(0);
		map.draw();
		addKey();

	}

	public static void main (String... args) {
		AirportMap am = new AirportMap();
		PApplet.runSketch(new String[]{"Airport Map"}, am);
	}

}
