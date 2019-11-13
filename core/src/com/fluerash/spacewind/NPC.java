package com.fluerash.spacewind;

public class NPC extends Entity {

    public NPC(MainGameScreen gameScreen, InputComponent inputComponent, PhysicsComponent physicsComponent, GraphicsComponent graphicsComponent) {
        super(gameScreen, inputComponent, physicsComponent, graphicsComponent);
    }

    public NPC(MainGameScreen gameScreen){
        super(gameScreen, new NPCInputComponent(), new NPCPhysicsComponent(), new NPCGraphicsComponent() );
    }

    public void init(){
        physicsComponent.setNextPositionToCurrent(this);
        physicsComponent.sendDirection(this);
        physicsComponent.sendState(this);
    }
}
