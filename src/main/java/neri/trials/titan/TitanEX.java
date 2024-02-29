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
import gg.xp.xivsupport.events.state.XivState;
import gg.xp.xivsupport.models.XivCombatant;
import gg.xp.xivsupport.events.triggers.easytriggers.conditions.RefireFilter;

/**
 * Example trigger pack for a duty
 */
// @CalloutRepo indicates that the system should scan for fields defined as ModifiableCallout. The user is presented
// with a UI to enable/disable them, and change the callout text under the Plugins > Callouts tab.
// The name chosen here will show in the UI.
@CalloutRepo(name = "Titan EX", duty = KnownDuty.None)
// You should not chang the class name once you publish this, as it is used to determine the settings cdKey to store
// customizations to the callouts.
// FilteredEventHandler is an optional interface, giving you the 'enabled' option (see below).
public class TitanEX implements FilteredEventHandler {

	// Since we have @CalloutRepo
	private final ModifiableCallout<AbilityCastStart> landslide = ModifiableCallout.durationBasedCall("Landslide", "Dodge Lines");
	private final ModifiableCallout<AbilityCastStart> groundAoe = ModifiableCallout.durationBasedCall("Weight of the Land", "Move");
	private final ModifiableCallout<AbilityUsedEvent> tumult = new ModifiableCallout<>("Tumult", "AoE");
	private final ModifiableCallout<AbilityCastStart> geocrush = ModifiableCallout.durationBasedCall("Geocrush", "Go to edge");
	private final ModifiableCallout<AbilityUsedEvent> mountainBuster = new ModifiableCallout<>("Mountain Buster", "Tank Buster");
	private final ModifiableCallout<AbilityUsedEvent> rockThrow = new ModifiableCallout<>("Rock Throw", "Jail on {event.target}");
	private final ModifiableCallout<AbilityCastStart> upheaval = ModifiableCallout.durationBasedCall("Upheaval", "Big AoE");
	private final ModifiableCallout<BuffApplied> heart = new ModifiableCallout<>("Swap to Heart", "Attack Heart");
	private final ModifiableCallout<AbilityUsedEvent> earthenFury = new ModifiableCallout<>("Earthen Fury", "Big AoE");
	private final ModifiableCallout<TargetabilityUpdate> adds = new ModifiableCallout<>("Adds", "Attack Adds");
	private final ModifiableCallout<TargetabilityUpdate> bombs = new ModifiableCallout<>("Bomb Boulders", "Intercardinals, Dodge Lines");
	
	

	// This comes from FilteredEventHandler. In this case, we want to restrict this set of triggers to a specific
	// zone (Urth's Fount, in this case, Zone ID 394).
	@Override
	public boolean enabled(EventContext context) {
		return context.getStateInfo().get(XivState.class).zoneIs(296);
	}

	// This is an actual callout. You can specify as many as you want, but you have to follow the usual Java conventions
	// (e.g. they need to have unique names or it won't compile).
	// The @HandleEvents annotation is what tells the scanner that this is a method that should be called when we have
	// an event of the given type to handle.
	@HandleEvents(name = "Landslide")
	// The name should not be changed once published, as the is used for the settings keys (just like the class name).
	// The first argument is always EventContext, which gives you the ability to both query zone/player/etc info (as
	// can be seen in the `enabled` method above, as well as submit new events (in this case, a callout).
	// The second argument is the type of event to listen for. In this case, we want to know when something starts
	// casting an ability.
	public void landslide(EventContext context, AbilityCastStart event) {
		// landslide has ID 0xC49. As per usual Java conventions, numbers can be specified as base-10 or base-16. Note
		// that numbers are always signed in Java - so if something is in the 0x80000000 - 0xFFFFFFFF range, you need
		// to make sure you specify it as a long by putting L at the end of it (e.g. 0xE000000L).
		if (event.getAbility().getId() == 0x5BB) {
			// ModifiableCallout.getModified() returns a CalloutEvent with whatever user-specified modifications
			// applied (e.g. the text can be altered, you can pick TTS/Text/Both, or disable it entirely).
			// EventContext.accept(Event) - submit the new event to be processed immediately.
			context.accept(landslide.getModified(event));
		}
	}
	
	@HandleEvents(name = "groundAoe")
	public void groundAoe(EventContext context, AbilityCastStart event) {
		if (event.getAbility().getId() == 0x5BE) {
			context.accept(groundAoe.getModified(event));
		}
	}
	@HandleEvents(name = "tumult")
	public void tumult(EventContext context, AbilityUsedEvent event) {
		if (event.getAbility().getId() == 0x5C4) {
			context.accept(tumult.getModified(event));
		}
	}
	@HandleEvents(name = "geocrush")
	public void geocrush(EventContext context, AbilityCastStart event) {
		if (event.getAbility().getId() == 0x5C0) {
			context.accept(geocrush.getModified(event));
		}
	}
	@HandleEvents(name = "mountainBuster")
	public void mountainBuster(EventContext context, AbilityUsedEvent event) {
		if (event.getAbility().getId() == 0x5B8) {
			context.accept(mountainBuster.getModified(event));
		}
	}
	@HandleEvents(name = "rockThrow")
	public void rockThrow(EventContext context, AbilityUsedEvent event) {
		if (event.getAbility().getId() == 0x285) {
			context.accept(rockThrow.getModified(event));
		}
	}
	@HandleEvents(name = "upheaval")
	public void upheaval(EventContext context, AbilityCastStart event) {
		if (event.getAbility().getId() == 0x5BA) {
			context.accept(upheaval.getModified(event));
		}
	}
	@HandleEvents(name = "heart")
	public void heart(EventContext context, BuffApplied event) {
		if (event.getBuff().getId() == 0x148) {
			context.accept(heart.getModified(event));
		}
	}
	@HandleEvents(name = "earthenFury")
	public void earthenFury(EventContext context, AbilityUsedEvent event) {
		if (event.getAbility().getId() == 0x5C1) {
			context.accept(earthenFury.getModified(event));
		}
	}
	@HandleEvents(name = "adds")
	public void adds(EventContext context, TargetabilityUpdate event) {
		if (event.getTarget().getId() == 2290) {
			context.accept(adds.getModified(event));
		}
	}
	@HandleEvents(name = "bombs")
	public void bombs(EventContext context, TargetabilityUpdate event) {
		long targetId = event.getTarget().getId();
		System.out.println("Target ID: " + targetId); // Add this line for debugging
		if (targetId == 1504) {
			context.accept(bombs.getModified(event));
		}
	}
}
