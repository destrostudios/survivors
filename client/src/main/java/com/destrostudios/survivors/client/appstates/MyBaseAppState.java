package com.destrostudios.survivors.client.appstates;

import com.destrostudios.survivors.client.ClientApplication;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

public class MyBaseAppState extends AbstractAppState {

    protected ClientApplication mainApplication;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        mainApplication = (ClientApplication) application;
    }

    protected <T extends AppState> T getAppState(Class<T> appStateClass) {
        return mainApplication.getStateManager().getState(appStateClass);
    }
}