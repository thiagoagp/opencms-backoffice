// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;

import util.Direction;
import util.dice.Dice6;

public class Marine extends Piece {
	public final static int MAX_SUSTAINED_FIRE_BONUS = 1;
	public final static int MAX_MARINE_ACTION_POINTS = 4;

	public final static int SERGEANT = 2;
	public final static int FLAMER   = 1;
	public final static int STANDARD = 0;

	private String name_;
	private int type_;
	private boolean shoot_ = false;
	private boolean overwatch_ = false;
	private boolean guard_ = false;
	private boolean jammed_ = false;
	private CommandPoints cp_;

	private int carrying_ = 0;

	private int ammunition_ = 6;

	private Piece target_ = null;
	private int targetX_;
	private int targetY_;
	private int bonus_ = 0;

	Marine(String name, int type, CommandPoints cp) {
		super(Face.NORTH);
		name_ = name;
		type_ = type;
		cp_ = cp;
	}

	public String getName() {
		return name_;
	}

	public boolean getShoot() {
		return shoot_;
	}

	void setShoot(boolean s) {
		shoot_ = s;
	}

	public boolean getOverwatch() {
		return overwatch_;
	}

	void setOverwatch(boolean o) {
		overwatch_ = o;
		if(overwatch_)
			setGuard(false);
	}
	
	public boolean getGuard() {
		return guard_;
	}

	void setGuard(boolean g) {
		guard_ = g;
		if(guard_)
			setOverwatch(false);
	}

	public boolean getJammed() {
		return jammed_;
	}

	void clearJammed() {
		jammed_ = false;
	}

	public int getAmmunition() {
		return ammunition_;
	}

	public void useAmmunition() {
		util.Debug.assert2(ammunition_ >= 1, "Marine::useAmmunition out of ammo");
		--ammunition_;
	}

	public int getType() {
		return type_;
	}

	public int getCarrying() {
		return carrying_;
	}

	void setCarrying(int c) {
		carrying_ = c;
	}

	public boolean canUseActionPoints(int ap) {
		return (ap < (action_ + cp_.get()));
	}

	public boolean useActionPoints(int ap) {
		if (super.useActionPoints(ap))
			return true;
		else if (cp_.use(ap - getActionPoints()))
			return useActionPoints(getActionPoints()); // Should always be true
		else
			return false;
	}

	int getCloseCombatValue(Random r, int dir) {
		int v = Dice6.getDice().getDiceRoll();
		if (type_ == SERGEANT) {
			if(Face.isForward(dir, getFace()))
				++v;
		}
		return v;
	}

	int getShootValue(Random r, boolean jam, GameListener gl) {
		int v1 = Dice6.getDice().getDiceRoll();
		int v2 = Dice6.getDice().getDiceRoll();
		// util.Debug.message("Marine::getShootValue bonus " + bonus_);
		// if (jam && v1 >= 6 && v2 >= 6)
		if (jam && v1 == v2) {
			jammed_ = true;
			gl.pieceJams(this);
		}
		return Math.max(v1, v2) + bonus_;
	}

	void setTarget(Piece target) {
		if (target != null) {
			if (target.equals(target_) && targetX_ == this.getPosX() && targetY_ == this.getPosY()) {
				// Target is the same and Marine has not moved
				// util.Debug.message("Marine::setTarget bonus");
				if (bonus_ < MAX_SUSTAINED_FIRE_BONUS)
					++bonus_;
			}
			else {
				// util.Debug.message("Marine::setTarget " + target);
				target_ = target;
				targetX_ = this.getPosX();// target.getPosX();
				targetY_ = this.getPosY(); // target.getPosY();
				bonus_ = 0;
			}
		}
		else {
			target_ = null;
			bonus_ = 0;
		}
	}

	public void resetActionPoints() {
		action_ = MAX_MARINE_ACTION_POINTS;
	}
}
