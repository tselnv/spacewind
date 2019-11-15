package com.fluerash.spacewind.ai_test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.fluerash.spacewind.MainGameScreen;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PathFindingGame extends ApplicationAdapter {

    ShapeRenderer shapeRenderer;
    SpriteBatch batch;
    BitmapFont font;

    CityGraph cityGraph;
    GraphPath<City> cityPath;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        cityGraph = new CityGraph();

//        City startCity = new City(300, 250, "S");
//        City bCity = new City(300, 350, "B");
//        City aCity = new City(200, 350, "A");
//        City cCity = new City(400, 350, "C");
//        City dCity = new City(200, 250, "D");
//        City fCity = new City(100, 250, "F");
//        City eCity = new City(400, 250, "E");
//        City hCity = new City(300, 150, "H");
//        City gCity = new City(200, 150, "G");
//        City iCity = new City(200, 50, "I");
//        City jCity = new City(300, 50, "J");
//        City kCity = new City(400, 50, "K");
//        City goalCity = new City(400, 150, "Z");
//
//        cityGraph.addCity(startCity);
//        cityGraph.addCity(bCity);
//        cityGraph.addCity(aCity);
//        cityGraph.addCity(cCity);
//        cityGraph.addCity(dCity);
//        cityGraph.addCity(fCity);
//        cityGraph.addCity(eCity);
//        cityGraph.addCity(hCity);
//        cityGraph.addCity(gCity);
//        cityGraph.addCity(iCity);
//        cityGraph.addCity(jCity);
//        cityGraph.addCity(kCity);
//        cityGraph.addCity(goalCity);
//
//        cityGraph.connectCities(startCity, bCity);
//        cityGraph.connectCities(bCity, aCity);
//        cityGraph.connectCities(bCity, cCity);
//        cityGraph.connectCities(startCity, dCity);
//        cityGraph.connectCities(dCity, fCity);
//        cityGraph.connectCities(startCity, hCity);
//        cityGraph.connectCities(hCity, gCity);
//        cityGraph.connectCities(gCity, iCity);
//        cityGraph.connectCities(iCity, jCity);
//        cityGraph.connectCities(jCity, kCity);
//        cityGraph.connectCities(kCity, goalCity);
//        cityGraph.connectCities(startCity, eCity);
//        cityGraph.connectCities(eCity, goalCity);

        final int CITIES_NUM = 100;

        for(int i = 0; i< CITIES_NUM; i++){
            int x = MathUtils.random(10, 800-10);
            int y = MathUtils.random(10, 800-10);
            City city = new City(x, y, String.valueOf(i));
            cityGraph.addCity(city);

            if(i>0){
                cityGraph.connectCities(city, cityGraph.cities.get(i-1));
            }
        }

        for(int i =0; i< CITIES_NUM; i++){
            City cur = cityGraph.cities.get(i);
            List<City> sortedCities = Arrays.stream(cityGraph.cities.toArray(City.class)).sorted((o1, o2) -> cur.dist(o1) <= cur.dist(o2) ? -1 : 1 ).collect(Collectors.toList()) ;

            for(int j = 0; j< 2; j++){
                cityGraph.connectCities(cur, sortedCities.get(1));
            }
        }

        cityPath = cityGraph.findPath(cityGraph.cities.get(CITIES_NUM-1), cityGraph.cities.get(0));

        Iterable<City> iterable = () -> cityPath.iterator();
        Stream<City> stream = StreamSupport.stream(iterable.spliterator(), false);
        System.out.println(stream.map(c -> c.name + " ").collect(Collectors.joining()));
        //stream.forEach(c -> System.out.print(c.name + " "));

//        System.out.println();
//        for (City city : cityPath) {
//            System.out.print(city.name + " ");
//        }

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for(Street street: cityGraph.streets) {
            street.render(shapeRenderer);
        }

        for(City city: cityGraph.cities) {
            city.render(shapeRenderer,batch,font, false);
        }

        if(cityPath != null) {
            for (City city : cityPath) {
                city.render(shapeRenderer, batch, font, true);
            }
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}
