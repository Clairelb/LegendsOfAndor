package com.example.LegendsOfAndor.ReturnClasses;

import com.example.LegendsOfAndor.PublicEnums.ActivateFogResponses;
import com.example.LegendsOfAndor.PublicEnums.FogKind;

public class ActivateFogRC {
    private FogKind fogKind;
    private ActivateFogResponses activateFogResponses;

    public ActivateFogRC() {}

    public ActivateFogRC(FogKind fogKind, ActivateFogResponses activateFogResponses) {
        this.fogKind = fogKind;
        this.activateFogResponses = activateFogResponses;
    }

    public FogKind getFogKind() {
        return fogKind;
    }

    public void setFogKind(FogKind fogKind) {
        this.fogKind = fogKind;
    }

    public ActivateFogResponses getActivateFogResponses() {
        return activateFogResponses;
    }

    public void setActivateFogResponses(ActivateFogResponses activateFogResponses) {
        this.activateFogResponses = activateFogResponses;
    }
}
