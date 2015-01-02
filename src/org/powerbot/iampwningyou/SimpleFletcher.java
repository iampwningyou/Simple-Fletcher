package org.powerbot.iampwningyou;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.iampwningyou.tasks.BankDepositEverything;
import org.powerbot.iampwningyou.tasks.BankWithdrawLogs;
import org.powerbot.iampwningyou.tasks.Fletch;
import org.powerbot.iampwningyou.tasks.OpenBank;
import org.powerbot.iampwningyou.tasks.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;

@Manifest(name="Simple Fletcher", description="Fletches Unfinished Bows.")
public class SimpleFletcher extends PollingScript<ClientContext> {

	private List <Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
	public static int logsInBank = 0;
	public static String task = "";
	
	public SimpleFletcher() {
		taskList.addAll(Arrays.asList(new BankWithdrawLogs(ctx), 
				new BankDepositEverything(ctx), 
				new Fletch(ctx),
				new OpenBank(ctx)));
	}

	@Override
	public void poll() {
		for (Task<ClientContext> task : taskList) {
			if (task.activate()) {
				task.execute();
			}
		}
	}
	
	public static void stop(ClientContext _ctx) {
		Condition.sleep(10000);
		_ctx.controller.stop();
	}

}
