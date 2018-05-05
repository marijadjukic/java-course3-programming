package module6;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class AirplaneCrashMarker extends CommonMarker {
    PImage warning;
    public AirplaneCrashMarker(Feature crash, PImage warning){
        super(((PointFeature)crash).getLocation(), crash.getProperties());
        this.warning = warning;
    }

    @Override
    public void drawMarker(PGraphics pg, float x, float y) {
        pg.image(warning,x-7,y-7,15,15);
    }

    @Override
    public void showTitle(PGraphics pg, float x, float y) {
        String date = getTime() + ", " + getDate();
        String operator = "Operators: " + getOperator();
        String fatalities = "Fatalities: " + getFatalities();

        pg.pushStyle();

        pg.fill(255, 255, 255);
        pg.textSize(12);
        pg.rectMode(PConstants.CORNER);
        pg.rect(x, y-5-39, Math.max(pg.textWidth(date), pg.textWidth(operator)) + 6, 55);
        pg.fill(0, 0, 0);
        pg.textAlign(PConstants.LEFT, PConstants.TOP);
        pg.text(date, x + 3, y - 5 - 33);
        pg.text(operator, x + 3, y - 5 - 18);
        pg.text(fatalities, x + 3, y - 5 - 3);

        pg.popStyle();
    }

    public String getDate() {
        return (String) getProperty("date");
    }

    public String getTime() {
        return (String) getProperty("time");
    }

    public String getFatalities() {
        return (String) getProperty("fatalities");
    }

    public String getOperator() {
        return (String) getProperty("operator");
    }
}
