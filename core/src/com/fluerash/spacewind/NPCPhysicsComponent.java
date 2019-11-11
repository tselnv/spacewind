package com.fluerash.spacewind;

import com.fluerash.spacewind.maps.Map;

public class NPCPhysicsComponent extends PhysicsComponent {

    public NPCPhysicsComponent(){
        boundingBoxLocation = BoundingBoxLocation.CENTER;
        initBoundingBox(0.4f, 0.15f);
    }

    @Override
    public void update(Entity entity, Map map, float delta) {
        updateBoundingBoxPosition(nextPosition);

        if( state == Entity.State.IMMOBILE ) return;

        if (    !isCollisionWithMapLayer(entity, map) && !isCollisionWithMapEntities(entity, map) && state == Entity.State.WALKING){
            setNextPositionToCurrent(entity);
        } else {
            updateBoundingBoxPosition(currentPosition);
        }
        calculateNextPosition(delta);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void receiveMessage(MESSAGE messageType, Object... args) {

    }
}
