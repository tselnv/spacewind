package com.fluerash.spacewind;

import com.badlogic.gdx.Gdx;
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
    protected Entity.State state;
    protected BoundingBoxLocation boundingBoxLocation;

    public abstract void update(Entity entity, Map map, float delta);

    public PhysicsComponent() {
        nextPosition = new Vector2(0,0);
        currentPosition = new Vector2(0,0);
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
        if(state == Entity.State.WALKING) {
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

        //entity.sendMessage(MESSAGE.CURRENT_POSITION, currentPosition); //TODO
        entity.setPosition(currentPosition);
    }

    protected boolean isCollisionWithMapLayer(Entity entity, Map map){
        if (nextPosition.x < 0 && nextPosition.y <0)
            return true;
        return false;
    }

    protected boolean isCollisionWithMapEntities(Entity entity, Map map) {
        return false;
    }

    public void setState(Entity.State state) {
        this.state = state;
    }

    public void setDirection(Entity.Direction direction) {
        this.currentDirection = direction;
    }

    public void setCurrentPosition(Vector2 position) {
        this.currentPosition = position;
    }
}
