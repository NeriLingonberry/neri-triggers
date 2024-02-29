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
	public void abilityCast(EventContext context, AbilityCastStart event) {
		int id = (int) event.getAbility().getId();
		final ModifiableCallout<AbilityCastStart> call;
		switch (id) {
			case 0x5BB:
				if (noSpam.check(event)) {
					call = landslide;
				} else {
					return;
				}
				break;
			case 0x5BE:
				if (noSpam.check(event)) {
					call = groundAoe;
				} else {
					return;
				}
				break;
			case 0x5C0:
				if (noSpam.check(event)) {
					call = geocrush;
				} else {
					return;
				}
				break;
			case 0x5BA:
				if (noSpam.check(event)) {
					call = upheaval;
				} else {
					return;
				}
				break;
			default:
				return;
		}
		context.accept(call.getModified(event));
	}

	@HandleEvents
	public void abilityUsed(EventContext context, AbilityUsedEvent event) {
		int id = (int) event.getAbility().getId();
		final ModifiableCallout<AbilityUsedEvent> call;
		switch (id) {
			case 0x5B9:
				if (noSpam.check(event)) {
					call = tumult;
				} else {
					return;
				}
				break;
			case 0x5B8:
				if (noSpam.check(event)) {
					call = mountainBuster;
				} else {
					return;
				}
				break;
			case 0x285:
				if (noSpam.check(event)) {
					call = rockThrow;
				} else {
					return;
				}
				break;
			case 0x5C1:
				if (noSpam.check(event)) {
					call = earthenFury;
				} else {
					return;
				}
				break;
			default:
				return;
		}
		context.accept(call.getModified(event));
	}

	@HandleEvents
	public void buffApplied(EventContext context, BuffApplied event) {
		int id = (int) event.getBuff().getId();
		final ModifiableCallout<BuffApplied> call;
		switch (id) {
			case 0x148:
				if (noSpam.check(event)) {
					call = heart;
				} else {
					return;
				}
				break;
			default:
				return;
		}
		context.accept(call.getModified(event));
	}

	@HandleEvents
	public void buffRemoved(EventContext context, BuffRemoved event) {
		int id = (int) event.getBuff().getId();
		final ModifiableCallout<BuffRemoved> call;
		switch (id) {
			case 0x148:
				if (noSpam.check(event)) {
					call = addsSoon;
				} else {
					return;
				}
				break;
			default:
				return;
		}
		context.accept(call.getModified(event));
	}

	@HandleEvents
	public void targetabilityUpdate(EventContext context, TargetabilityUpdate event) {
		int id = (int) event.getSource().getId();
		final ModifiableCallout<TargetabilityUpdate> call;
		switch (id) {
			case 0x148:
				if (noSpam.check(event)) {
					call = adds;
				} else {
					return;
				}
				break;
			default:
				return;
		}
		context.accept(call.getModified(event));
	}
}