package com.example.LegendsOfAndor.ReturnClasses;

import com.example.LegendsOfAndor.Fight;
import com.example.LegendsOfAndor.PublicEnums.FightResponses;

public class FightRC {
    private Fight fight;
    private FightResponses fightResponses;

    public FightRC() {}

    public FightRC(Fight fight, FightResponses fightResponses) {
        this.fight = fight;
        this.fightResponses = fightResponses;
    }

    public Fight getFight() {
        return fight;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
    }

    public FightResponses getFightResponses() {
        return fightResponses;
    }

    public void setFightResponses(FightResponses fightResponses) {
        this.fightResponses = fightResponses;
    }


}
