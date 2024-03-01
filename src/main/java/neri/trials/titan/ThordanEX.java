package neri.ThordanEX;

import gg.xp.reevent.events.EventContext;
import gg.xp.reevent.scan.FilteredEventHandler;
import gg.xp.reevent.scan.HandleEvents;
import gg.xp.xivdata.data.*;
import gg.xp.xivdata.data.duties.*;
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
import gg.xp.xivsupport.events.triggers.seq.SequentialTrigger;
import gg.xp.xivsupport.events.triggers.seq.SequentialTriggerController;
import gg.xp.xivsupport.models.ArenaPos;
import gg.xp.xivsupport.models.ArenaSector;
import gg.xp.xivsupport.models.Position;
import gg.xp.xivsupport.events.state.XivState;
import gg.xp.reevent.events.BaseEvent;
import gg.xp.reevent.scan.AutoFeed;
import gg.xp.reevent.events.Event;
import gg.xp.reevent.events.SystemEvent;

import java.time.Duration;

@CalloutRepo(name = "Thordan EX", duty = KnownDuty.None)
public class ThordanEX implements FilteredEventHandler {

    private final ModifiableCallout<AbilityCastStart> meteorain = ModifiableCallout.durationBasedCall("Meteorain", "Move");
    private final ModifiableCallout<AbilityCastStart> mercy = ModifiableCallout.durationBasedCall("Ascalon's Mercy", "Behind or Between Slashes");
    private final ModifiableCallout<AbilityCastStart> dragonsRage = ModifiableCallout.durationBasedCall("Dragon's Rage", "Stack");
    private final ModifiableCallout<AbilityCastStart> dragonsGaze = ModifiableCallout.durationBasedCall("Dragon's Gaze", "Look away");
    private final ModifiableCallout<AbilityCastStart> lightningStorm = ModifiableCallout.durationBasedCall("Lightning Storm", "Spread");
    private final ModifiableCallout<AbilityCastStart> quaga = ModifiableCallout.durationBasedCall("Ancient Quaga", "Raidwide");
    private final ModifiableCallout<AbilityCastStart> heavenlyHeel = ModifiableCallout.durationBasedCall("Heavenly Heel", "Tank Hit");
    private final ModifiableCallout<AbilityCastStart> heavensflame = ModifiableCallout.durationBasedCall("Heavensflame", "Spread");
    private final ModifiableCallout<AbilityCastStart> conviction = ModifiableCallout.durationBasedCall("Conviction", "Take closest tower");
    private final ModifiableCallout<AbilityCastStart> zephirinSpawn = ModifiableCallout.durationBasedCall("Sacred Cross", "Attack Zephirin");
    private final ModifiableCallout<AbilityCastStart> spiralThrust = ModifiableCallout.durationBasedCall("Spiral Thrust", "Dashes");


	private final ModifiableCallout<AbilityCastStart> landslide = ModifiableCallout.durationBasedCall("Landslide", "Dodge Lines");
    private final ModifiableCallout<AbilityCastStart> groundAoe = ModifiableCallout.durationBasedCall("Weight of the Land", "Move");
    private final ModifiableCallout<AbilityUsedEvent> tumult = new ModifiableCallout<>("Tumult", "AoE");
    private final ModifiableCallout<AbilityCastStart> geocrush = ModifiableCallout.durationBasedCall("Geocrush", "Go to edge");
    private final ModifiableCallout<AbilityUsedEvent> mountainBuster = new ModifiableCallout<>("Mountain Buster", "Tank Buster");
    private final ModifiableCallout<AbilityUsedEvent> rockThrow = new ModifiableCallout<>("Rock Throw", "Jail on {event.target}");
	private final ModifiableCallout<AbilityUsedEvent> earthenFury = new ModifiableCallout<>("Earthen Fury", "Big AoE");
    private final ModifiableCallout<BuffApplied> heart = new ModifiableCallout<>("Swap to Heart", "Attack Heart");
    private final ModifiableCallout<BuffRemoved> addsSoon = new ModifiableCallout<>("Adds Warning", "Adds Soon");
    private final ModifiableCallout<TargetabilityUpdate> adds = new ModifiableCallout<>("Adds", "Attack Adds");
    private final ModifiableCallout<TargetabilityUpdate> bombs = new ModifiableCallout<>("Bomb Boulders", "Dodge Boulders");

    @Override
    public boolean enabled(EventContext context) {
        return context.getStateInfo().get(XivState.class).zoneIs(448);
    }

    private final RepeatSuppressor noSpam = new RepeatSuppressor(Duration.ofMillis(5000));
    private final RepeatSuppressor noSpamShort = new RepeatSuppressor(Duration.ofMillis(100));
	
	@HandleEvents
	public void abilityCast(EventContext context, AbilityCastStart event) {
		int id = (int) event.getAbility().getId();
		final ModifiableCallout<AbilityCastStart> call;
		switch (id) {
			case 0x1483:
				if (noSpamShort.check(event)) {
					call = meteorain;
				} else {
					return;
				}
				break;
			case 0x147F:
				if (noSpamShort.check(event)) {
					call = mercy;
				} else {
					return;
				}
				break;
			case 0x148B:
				if (noSpamShort.check(event)) {
					call = dragonsRage;
				} else {
					return;
				}
				break;
			case 0x1489:
				if (noSpamShort.check(event)) {
					call = dragonsGaze;
				} else {
					return;
				}
				break;
			case 0x1481:
				if (noSpamShort.check(event)) {
					call = lightningStorm;
				} else {
					return;
				}
				break;
			case 0x1485:
				if (noSpamShort.check(event)) {
					call = quaga;
				} else {
					return;
				}
				break;
			case 0x14AC:
				if (noSpamShort.check(event)) {
					call = heavensflame;
				} else {
					return;
				}
				break;
			case 0x149C:
				if (noSpamShort.check(event)) {
					call = conviction;
				} else {
					return;
				}
				break;
			case 0x1490:
				if (noSpamShort.check(event)) {
					call = zephirinSpawn;
				} else {
					return;
				}
				break;
			case 0x14A6:
				if (noSpam.check(event)) {
					call = spiralThrust;
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
				if (noSpamShort.check(event)) {
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
		int id = (int) event.getSource().getbNpcId();
		final ModifiableCallout<TargetabilityUpdate> call;
		switch (id) {
			case 1504:
				if (noSpam.check(event)) {
					call = bombs;
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