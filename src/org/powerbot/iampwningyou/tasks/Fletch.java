package org.powerbot.iampwningyou.tasks;

import java.util.concurrent.Callable;

import org.powerbot.iampwningyou.SimpleFletcher;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Item;

public class Fletch extends Task<ClientContext>{

	public Fletch(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Component fletchingProgress = ctx.widgets.component(1251, 9);
		return ctx.backpack.select().id(ItemIds.LOG).count() > 0
				&& ctx.players.local().animation() == -1
				&& !ctx.bank.opened()
				&& !fletchingProgress.visible();
	}

	@Override
	public void execute() {
		SimpleFletcher.task = "Fletching";
		
		Item log = ctx.backpack.select().id(ItemIds.LOG).shuffle().poll();
		log.interact("Craft");
		
		Condition.sleep(200);
		
		final Component craftWindow = ctx.widgets.component(1371, 0);
		if (!craftWindow.visible()) {
			final Component toolWindow = ctx.widgets.component(1179, 33);
			Condition.wait(new Callable<Boolean>() {
				
				public Boolean call() throws Exception {
					return toolWindow.visible();
				}
			}, Random.getDelay(), 10);

			final Component fletchOption = toolWindow.component(1);
			fletchOption.click();
		}
		
		Condition.wait(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				return craftWindow.visible();
			}
		}, Random.getDelay(), 10);
		
		Component shortBow = ctx.widgets.component(1371, 44).component(5);
		shortBow.click();
		
		Condition.sleep(Random.getDelay());
		
		Component fletchButton = ctx.widgets.component(1370, 38);
		fletchButton.click();
		
		Condition.sleep(Random.nextInt(1000, 2000));
	}

}
