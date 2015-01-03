package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.rt6.ClientContext;

public class CloseBank extends Task<ClientContext> {

	public CloseBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened()
				&& ctx.backpack.select().id(ItemIds.LOG).count() > 0;
	}

	@Override
	public void execute() {
		ctx.bank.close();
	}


}
