package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.fluerash.spacewind.maps.Map;

public abstract class PhysicsComponent implements Component {
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    public static enum BoundingBoxLocation{
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
    }

    protected static final float VELOCITY_ABS = 2f;
    protected Rectangle boundingBox;
    protected Vector2 nextPosition;
    protected Vector2 currentPosition;
    protected Vector2 velocity;
    protected Entity.Direction currentDirection;
    protected Entity.State currentState;
    protected BoundingBoxLocation boundingBoxLocation;

    public abstract void update(Entity entity, Map map, float delta);

    public PhysicsComponent() {
        nextPosition = new Vector2(0,0);
        currentPosition = new Vector2(0,0);
        currentState = Entity.State.WALKING;
        currentDirection = Entity.Direction.DOWN;
        velocity = new Vector2(VELOCITY_ABS, VELOCITY_ABS);
        boundingBox = new Rectangle();
        boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
    }

    protected void updateBoundingBoxPosition(Vector2 position){
        float minX;
        float minY;

        if( Map.UNIT_SCALE > 0 ) {
            minX = position.x / Map.UNIT_SCALE;
            minY = position.y / Map.UNIT_SCALE;
        }else{
            minX = position.x;
            minY = position.y;
        }

        switch(boundingBoxLocation){
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, boundingBox.getWidth(), boundingBox.getHeight());
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH/2, minY + Entity.FRAME_HEIGHT/4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH/2, minY + Entity.FRAME_HEIGHT/2);
                break;
        }
        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }

    protected void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced){
        //Update the current bounding box
        float width;
        float height;

        float origWidth =  Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        if( widthReductionAmount > 0 && widthReductionAmount < 1){
            width = Entity.FRAME_WIDTH * widthReductionAmount;
        }else{
            width = Entity.FRAME_WIDTH;
        }

        if( heightReductionAmount > 0 && heightReductionAmount < 1){
            height = Entity.FRAME_HEIGHT * heightReductionAmount;
        }else{
            height = Entity.FRAME_HEIGHT;
        }

        if( width == 0 || height == 0){
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
        }

        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;

        if( Map.UNIT_SCALE > 0 ) {
            minX = nextPosition.x / Map.UNIT_SCALE;
            minY = nextPosition.y / Map.UNIT_SCALE;
        }else{
            minX = nextPosition.x;
            minY = nextPosition.y;
        }

        boundingBox.setWidth(width);
        boundingBox.setHeight(height);

        switch(boundingBoxLocation){
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + origWidth/2, minY + origHeight/4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + origWidth/2, minY + origHeight/2);
                break;
        }

        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }

    protected void calculateNextPosition(float deltaTime){
        if(currentState == Entity.State.WALKING) {
            if (currentDirection == null) return;

            float testX = currentPosition.x;
            float testY = currentPosition.y;

            velocity.scl(deltaTime);

            switch (currentDirection) {
                case LEFT:
                    testX -= velocity.x;
                    break;
                case RIGHT:
                    testX += velocity.x;
                    break;
                case UP:
                    testY += velocity.y;
                    break;
                case DOWN:
                    testY -= velocity.y;
                    break;
                default:
                    break;
            }

            nextPosition.x = testX;
            nextPosition.y = testY;

            //velocity
            velocity.set(VELOCITY_ABS, VELOCITY_ABS);
        }
    }

    protected void setNextPositionToCurrent(Entity entity){
        this.currentPosition.x = nextPosition.x;
        this.currentPosition.y = nextPosition.y;
        //Gdx.app.debug(TAG, "SETTING Current Position " + entity.getEntityConfig().getEntityID() + ": (" + _currentEntityPosition.x + "," + _currentEntityPosition.y + ")");

        //entity.sendMessage(MESSAGE.CURRENT_POSITION, currentPosition);
        entity.setPosition(currentPosition);
    }

    protected boolean isCollisionWithMapLayer(Entity entity, Map map){
//        if (boundingBox.x < 0 || boundingBox.y <0)
//            return true;
//        if ( boundingBox.x + boundingBox.width > map.getWidthInPixel() || boundingBox.y + boundingBox.height > map.getHeightInPixel())
//            return true;
//        return false;
//
//        MapLayer mapCollisionLayer =  map.getCollisionLayer();
//        if( mapCollisionLayer == null ){
//            return false;
//        }

        MapLayer mapCollisionLayer =  map.getCollisionLayer();
        if( mapCollisionLayer == null ){
            return false;
        }

        Rectangle rectangle;
        for(MapObject mapObject: mapCollisionLayer.getObjects()){
            if(mapObject instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) mapObject).getRectangle();
                if (boundingBox.overlaps(rectangle)){
                    entity.collisionWithMap();
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isCollisionWithMapEntities(Entity entity, Map map) {
        return false;
    }

    public void setCurrentState(Entity.State currentState) {
        this.currentState = currentState;
    }

    public void setDirection(Entity.Direction direction) {
        this.currentDirection = direction;
    }

    protected void sendDirection(Entity entity){
        entity.setDirection(currentDirection);
    }

    protected void sendState(Entity entity){
        entity.setState(currentState);
    }

    public Entity.Direction findPath(Vector2 gotoVector) {
        Vector2 delta = new Vector2( gotoVector.x - currentPosition.x, gotoVector.y - currentPosition.y);
        if (Math.abs(delta.x) > Math.abs(delta.y)){
            if(delta.x >0)
                return Entity.Direction.RIGHT;
            else
                return Entity.Direction.LEFT;
        } else {
            if (delta.y >0)
                return Entity.Direction.UP;
            else
                return Entity.Direction.DOWN;
        }
    }


}
