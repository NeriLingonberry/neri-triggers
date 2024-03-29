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
import gg.xp.xivsupport.models.XivPlayerCharacter;
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
import gg.xp.xivsupport.models.CombatantType;
import gg.xp.xivsupport.events.actlines.events.HeadMarkerEvent;
import gg.xp.xivsupport.events.actlines.events.TetherEvent;

import java.time.Duration;

@CalloutRepo(name = "Thordan EX", duty = KnownDuty.None)
public class ThordanEX implements FilteredEventHandler {

    private final ModifiableCallout<AbilityCastStart> meteorain = ModifiableCallout.durationBasedCall("Meteorain", "Move");
    private final ModifiableCallout<AbilityCastStart> mercy = ModifiableCallout.durationBasedCall("Ascalon's Mercy", "Behind or Between Slashes");
    private final ModifiableCallout<AbilityCastStart> dragonsRage = ModifiableCallout.durationBasedCall("Dragon's Rage", "Stack");
    private final ModifiableCallout<AbilityCastStart> dragonsGaze = ModifiableCallout.durationBasedCall("Dragon's Gaze", "Look away");
    private final ModifiableCallout<AbilityCastStart> lightningStorm = ModifiableCallout.durationBasedCall("Lightning Storm", "Spread");
    private final ModifiableCallout<AbilityCastStart> quaga = ModifiableCallout.durationBasedCall("Ancient Quaga", "Raidwide");
    private final ModifiableCallout<AbilityCastStart> heavenlyHeel = ModifiableCallout.durationBasedCall("Heavenly Heel", "Tank Hit on {event.target}");
    private final ModifiableCallout<AbilityCastStart> heavensflame = ModifiableCallout.durationBasedCall("Heavensflame", "Spread");
    private final ModifiableCallout<AbilityCastStart> conviction = ModifiableCallout.durationBasedCall("Conviction", "Take tower");
    private final ModifiableCallout<AbilityCastStart> zephirinSpawn = ModifiableCallout.durationBasedCall("Sacred Cross", "Attack Zephirin");
    private final ModifiableCallout<AbilityCastStart> spiralThrust = ModifiableCallout.durationBasedCall("Spiral Thrust", "Dodge Dash");
    private final ModifiableCallout<AbilityCastStart> meteors = ModifiableCallout.durationBasedCall("Meteors", "Knockback");
    private final ModifiableCallout<AbilityCastStart> frostDebuff = ModifiableCallout.durationBasedCall("Hiemal Storm", "Drop Ice out");
    private final ModifiableCallout<AbilityCastStart> knockback = ModifiableCallout.durationBasedCall("Knockback", "Knockback soon");
    private final ModifiableCallout<AbilityCastStart> growingAoe = ModifiableCallout.durationBasedCall("Heavy Impact", "Dodge growing aoe");
    private final ModifiableCallout<AbilityCastStart> intercept = ModifiableCallout.durationBasedCall("Healer Mark 2", "Intercept Healer Mark");
    private final ModifiableCallout<HeadMarkerEvent> blueBall = new ModifiableCallout<>("Blue Balls", "Go far");
    private final ModifiableCallout<HeadMarkerEvent> spread2 = new ModifiableCallout<>("Spread 2", "Spread");
    private final ModifiableCallout<HeadMarkerEvent> comet = new ModifiableCallout<>("Comet", "Comet on YOU");
    private final ModifiableCallout<HeadMarkerEvent> healerHM = new ModifiableCallout<>("Healer Mark", "Healer Mark on YOU");
    private final ModifiableCallout<TetherEvent> tether = new ModifiableCallout<>("Tether", "Tether, Spread");
    private final ModifiableCallout<AbilityUsedEvent> attackAdds = new ModifiableCallout<>("Adds Spawn", "Attack Adds");
    private final ModifiableCallout<AbilityUsedEvent> goMid = new ModifiableCallout<>("Middle Reminder", "Go middle");
    private final ModifiableCallout<AbilityUsedEvent> ultimateEnd = new ModifiableCallout<>("Ultimate End", "Big raidwide");
    private final ModifiableCallout<TargetabilityUpdate> adds = new ModifiableCallout<>("Adds", "Attack Adds");

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
				if (noSpam.check(event)) {
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
			case 0x14AB:
				if (noSpamShort.check(event)) {
					call = heavensflame;
				} else {
					return;
				}
				break;
			case 0x149C:
				if (event.getTarget().getType() == CombatantType.NPC) {
					call = conviction;
				} else {
					return;
				}
				break;
			case 0x1490:
				if (event.getTarget().getType() == CombatantType.NPC) {
					call = zephirinSpawn;
				} else {
					return;
				}
				break;
			case 0x1491:
				if (event.getTarget().getType() == CombatantType.NPC) {
					call = zephirinSpawn;
				} else {
					return;
				}
				break;
			case 0x14A6:
				if (event.getTarget().getType() == CombatantType.NPC) {
					if (noSpamShort.check(event)) {
						call = spiralThrust;
					} else {
						return;
					}
				} else {
					return;
				}
				break;
			case 0x14B0:
				if (noSpamShort.check(event)) {
					call = meteors;
				} else {
					return;
				}
				break;
			case 0x14AE:
				if (noSpamShort.check(event)) {
					if (event.getTarget().isThePlayer()) {
						call = frostDebuff;
					} else {
						return;
					}
				} else {
					return;
				}
				break;
			case 0x1499:
				if (noSpamShort.check(event)) {
					call = knockback;
				} else {
					return;
				}
				break;
			case 0x14A0:
				if (noSpamShort.check(event)) {
					call = growingAoe;
				} else {
					return;
				}
				break;
			case 0x1497:
				if (noSpamShort.check(event)) {
					if (!event.getTarget().isThePlayer()) {
						call = intercept;
					} else {
						return;
					}
				} else {
					return;
				}
				break;
			case 0x1487:
				if (noSpamShort.check(event)) {
					call = heavenlyHeel;
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
			case 0x148D:
				if (noSpam.check(event)) {
					call = ultimateEnd;
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
			case 5205:
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
	
	@HandleEvents
	public void handleHeadMarkerEvent(EventContext context, HeadMarkerEvent event) {
		final ModifiableCallout<HeadMarkerEvent> call;
		if (event.getTarget().isThePlayer()) {
			if (event.getMarkerId() == 0xE || event.getMarkerId() == 0x1D) {
				if (noSpam.check(event)) {
					call = blueBall;
				} else {
					return;
				}
			} else if (event.getMarkerId() == 0xB) {
				call = comet;
			} else if (event.getMarkerId() == 0x10) {
				call = healerHM;
			} else {
				return;
			}
		} else {
			return;
		}
    context.accept(call.getModified(event));
	}
	
	@HandleEvents
	public void handleTetherEvent(EventContext context, TetherEvent event) {
		final ModifiableCallout<TetherEvent> call;
		if (event.getTarget().isThePlayer() && event.getId() == 0x5) {
			if (noSpam.check(event)) {
				call = tether;
			} else {
				return;
			}
		} else {
			return;
		}
    context.accept(call.getModified(event));
	}
}