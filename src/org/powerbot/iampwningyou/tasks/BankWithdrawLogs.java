package org.powerbot.iampwningyou.tasks;

import org.powerbot.iampwningyou.SimpleFletcher;
import org.powerbot.iampwningyou.resources.ids.ItemIds;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;

public class BankWithdrawLogs extends Task<ClientContext> {

	public BankWithdrawLogs(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened()
				&& ctx.backpack.select().count() == 0;
	}

	@Override
	public void execute() {
		int index = ctx.bank.indexOf(ItemIds.LOG);
//		-1 means not found in bank.
		if (index == -1) {
			SimpleFletcher.task = "Not enough ingredients to make more bows.";
			SimpleFletcher.stop(ctx);
		} else {
			SimpleFletcher.task = "Withdrawing logs.";
			ctx.bank.withdraw(ItemIds.LOG, Amount.ALL);
			
//			Used for ETC
			SimpleFletcher.logsInBank = ctx.bank.itemAt(index).stackSize();
		}
		
		Condition.sleep(Random.getDelay());
		
		ctx.bank.close();
	}

}
