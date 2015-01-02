package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;

public class OpenBank extends Task<ClientContext> {

	public OpenBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return !ctx.bank.opened()
				&& ctx.backpack.select().id(ItemIds.LOG).count() == 0;
	}

	@Override
	public void execute() {
		if (!ctx.bank.inViewport()) {
			ctx.camera.turnTo(ctx.bank.nearest());
		}
		
		if (Random.nextInt(0, 10) > 2) {
			ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-5, 5));
		}
		
		ctx.movement.step(ctx.bank.nearest().tile());
		
		Condition.sleep(1000);
		Condition.wait(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				return ctx.players.local().animation() == -1;
			}
		}, 100, 40);
		
//		Grand exchange window is open
		if (ctx.widgets.component(105, 87).component(1).visible()) {
			ctx.widgets.component(105, 87).component(1).click();
		}
		
		Condition.wait(new Callable<Boolean>() {			
			public Boolean call() throws Exception {
				return !ctx.widgets.component(105, 87).component(1).visible();
			}
		}, 100, 40);		
		
		ctx.bank.open();
	}

}
