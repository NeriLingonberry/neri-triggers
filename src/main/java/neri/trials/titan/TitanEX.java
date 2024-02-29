package neri.TitanEX;

import gg.xp.reevent.events.EventContext;
import gg.xp.reevent.scan.FilteredEventHandler;
import gg.xp.reevent.scan.HandleEvents;
import gg.xp.xivdata.data.duties.KnownDuty;
import gg.xp.xivsupport.callouts.CalloutRepo;
import gg.xp.xivsupport.callouts.ModifiableCallout;
import gg.xp.xivsupport.events.actlines.events.AbilityCastStart;
import gg.xp.xivsupport.events.actlines.events.TargetabilityUpdate;
import gg.xp.xivsupport.events.actlines.events.AbilityUsedEvent;
import gg.xp.xivsupport.events.actlines.events.BuffApplied;
import gg.xp.xivsupport.events.actlines.events.BuffRemoved;
import gg.xp.xivsupport.events.state.XivState;
import gg.xp.xivsupport.models.XivCombatant;
import gg.xp.xivsupport.events.triggers.util.RepeatSuppressor;

import java.time.Duration;

@CalloutRepo(name = "Titan EX", duty = KnownDuty.None)
public class TitanEX implements FilteredEventHandler {

    private final ModifiableCallout<AbilityCastStart> landslide = ModifiableCallout.durationBasedCall("Landslide", "Dodge Lines");
    private final ModifiableCallout<AbilityCastStart> groundAoe = ModifiableCallout.durationBasedCall("Weight of the Land", "Move");
    private final ModifiableCallout<AbilityUsedEvent> tumult = new ModifiableCallout<>("Tumult", "AoE");
    private final ModifiableCallout<AbilityCastStart> geocrush = ModifiableCallout.durationBasedCall("Geocrush", "Go to edge");
    private final ModifiableCallout<AbilityUsedEvent> mountainBuster = new ModifiableCallout<>("Mountain Buster", "Tank Buster");
    private final ModifiableCallout<AbilityUsedEvent> rockThrow = new ModifiableCallout<>("Rock Throw", "Jail on {event.target}");
    private final ModifiableCallout<AbilityCastStart> upheaval = ModifiableCallout.durationBasedCall("Upheaval", "Big AoE");
    private final ModifiableCallout<BuffApplied> heart = new ModifiableCallout<>("Swap to Heart", "Attack Heart");
    private final ModifiableCallout<BuffRemoved> addsSoon = new ModifiableCallout<>("Adds Warning", "Adds Soon");
    private final ModifiableCallout<AbilityUsedEvent> earthenFury = new ModifiableCallout<>("Earthen Fury", "Big AoE");
    private final ModifiableCallout<TargetabilityUpdate> adds = new ModifiableCallout<>("Adds", "Attack Adds");
    private final ModifiableCallout<TargetabilityUpdate> bombs = new ModifiableCallout<>("Bomb Boulders", "Dodge Boulders");

    @Override
    public boolean enabled(EventContext context) {
        return context.getStateInfo().get(XivState.class).zoneIs(296);
    }

    private final RepeatSuppressor noSpam = new RepeatSuppressor(Duration.ofMillis(5000));

    @HandleEvents
    public void handleEvent(EventContext context, Object event) {
        switch (event.getClass().getSimpleName()) {
            case "AbilityCastStart":
                AbilityCastStart castStart = (AbilityCastStart) event;
                switch (castStart.getAbility().getId()) {
                    case 0x5BB:
                        context.accept(landslide.getModified(castStart));
                        break;
                    case 0x5BE:
                        context.accept(groundAoe.getModified(castStart));
                        break;
                    case 0x5C0:
                        context.accept(geocrush.getModified(castStart));
                        break;
                    case 0x5BA:
                        context.accept(upheaval.getModified(castStart));
                        break;
                }
                break;
            case "AbilityUsedEvent":
                AbilityUsedEvent usedEvent = (AbilityUsedEvent) event;
                switch (usedEvent.getAbility().getId()) {
                    case 0x5B9:
                        if (noSpam.check(usedEvent)) {
                            context.accept(tumult.getModified(usedEvent));
                        }
                        break;
                    case 0x5B8:
                        context.accept(mountainBuster.getModified(usedEvent));
                        break;
                    case 0x285:
                        context.accept(rockThrow.getModified(usedEvent));
                        break;
                    case 0x5C1:
                        context.accept(earthenFury.getModified(usedEvent));
                        break;
                }
                break;
            case "BuffApplied":
                BuffApplied buffApplied = (BuffApplied) event;
                if (buffApplied.getBuff().getId() == 0x148 && noSpam.check(buffApplied)) {
                    context.accept(heart.getModified(buffApplied));
                }
                break;
            case "BuffRemoved":
                BuffRemoved buffRemoved = (BuffRemoved) event;
                if (buffRemoved.getBuff().getId() == 0x148 && noSpam.check(buffRemoved)) {
                    context.accept(addsSoon.getModified(buffRemoved));
                }
                break;
            case "TargetabilityUpdate":
                TargetabilityUpdate targetabilityUpdate = (TargetabilityUpdate) event;
                switch (targetabilityUpdate.getSource().getbNpcId()) {
                    case 2290:
                        if (noSpam.check(targetabilityUpdate)) {
                            context.accept(adds.getModified(targetabilityUpdate));
                        }
                        break;
                    case 1504:
                        if (noSpam.check(targetabilityUpdate)) {
                            context.accept(bombs.getModified(targetabilityUpdate));
                        }
                        break;
                }
                break;
        }
    }
}
