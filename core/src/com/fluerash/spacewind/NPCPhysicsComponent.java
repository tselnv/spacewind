package com.fluerash.spacewind;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.fluerash.spacewind.maps.Map;

public class NPCPhysicsComponent extends PhysicsComponent {

    public NPCPhysicsComponent(){
        super();
        boundingBoxLocation = BoundingBoxLocation.CENTER;
        initBoundingBox(0.4f, 0.15f);
        currentPosition = new Vector2(MathUtils.random(5, 195),MathUtils.random(5, 195));
        //currentPosition = new Vector2(5,5);
        nextPosition = currentPosition.cpy();
    }

    @Override
    public void update(Entity entity, Map map, float delta) {
        updateBoundingBoxPosition(nextPosition);
        if(  currentState != Entity.State.IDLE && currentState != Entity.State.IMMOBILE ) {
            if (currentState == Entity.State.WALKING && !isCollisionWithMapLayer(entity, map)) {
                setNextPositionToCurrent(entity);
            }
            calculateNextPosition(delta);
        } else {
            updateBoundingBoxPosition(currentPosition);
        }
    }



    @Override
    public void dispose() {

    }

    @Override
    public void receiveMessage(MESSAGE messageType, Object... args) {

    }


}
