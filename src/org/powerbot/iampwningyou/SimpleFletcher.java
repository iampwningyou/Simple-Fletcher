package org.powerbot.iampwningyou;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;
import org.powerbot.bot.rt6.client.Constants;
import org.powerbot.iampwningyou.tasks.BankDepositEverything;
import org.powerbot.iampwningyou.tasks.BankWithdrawLogs;
import org.powerbot.iampwningyou.tasks.CloseBank;
import org.powerbot.iampwningyou.tasks.Fletch;
import org.powerbot.iampwningyou.tasks.OpenBank;
import org.powerbot.iampwningyou.tasks.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Skills;

@Manifest(name="Simple Fletcher", description="Fletches Unfinished Bows.")
public class SimpleFletcher extends PollingScript<ClientContext> implements PaintListener {

	private List <Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
	public static int logsInBank = 0;
	private static int beginningLogCount = -1;
	public static String task = "";
	
	@SuppressWarnings("unchecked")
	public SimpleFletcher() {
		taskList.addAll(Arrays.asList(new BankWithdrawLogs(ctx), 
				new BankDepositEverything(ctx), 
				new Fletch(ctx),
				new OpenBank(ctx), 
				new CloseBank(ctx)));
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				createAndShowGUI(ctx);
			}
		});
	}
	
	@Override
	public void poll() {
		for (Task<ClientContext> task : taskList) {
			if (task.activate()) {
				task.execute();
				
				if (ctx.controller.isStopping()) {
					return;
				}
			}
		}
	}
	
	public static void stop(ClientContext _ctx) {
		Condition.sleep(10000);
		_ctx.controller.stop();
	}
	
	private static void createAndShowGUI(ClientContext _ctx) {
		JFrame frame = new JFrame("Simple Fletcher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComboBox<String> logTypes = new JComboBox<String>(new String[] {
				"Normal Wood",
				"Achey",
				"Oak",
				"Willow", 
				"Teak",
				"Maple",
				"Mahogany",
				"Yew",
				"Magic",
				"Blisterwood",
				"Elder"
		});
		frame.getContentPane().add(logTypes);
		
//		1371 51 0 = current category text
//		1371 62 has the category selection
		
//		1370 54 might give texture to match up later.
//		1370 56 gives title
		
//		Take a look at the child index. that will probably be most
//		useful.
		
//		Check the exp to see which selection is the best
//		Make it as dynamic as possible.
		
//		Component shortBow = ctx.widgets.component(1371, 44).component(5);
		frame.pack();
		frame.setVisible(true);
	}
	
//	An estimate of the height of a single character.
	private static final int STR_HEIGHT = 16;
//	An estimate of the width of a single character.
	private static final int STR_WIDTH = 6;
//	Will hold the strings to be displayed in the paint.
	private List <String> paintStrs = new ArrayList<String>();

	/*
	 * 	This paint is dynamic to the number of strings displayed and the
	 * 	length of the strings. 
	 */
	public void repaint(Graphics g) {
//		Calculating values for status
		double secondRuntime = ctx.controller.script().getTotalRuntime()/1000;
		double minuteRuntime = secondRuntime/60;
		
		int fletches = beginningLogCount - logsInBank;
		int fletchesPerMinute = (int) (fletches/minuteRuntime);
		
		int ETC = 0;
		if (fletchesPerMinute > 0) {
			ETC = logsInBank / fletchesPerMinute;
		}
		
		paintStrs.clear();
		
		paintStrs.add("iampwningyou's Simple Fletcher");
		
		paintStrs.add("Runtime: " + secondRuntime + "s");
		
		if (fletches > 0) {
			paintStrs.add("Fletches: " + fletches);
			paintStrs.add("Fletches/Min: " + fletchesPerMinute);
			paintStrs.add("ETC: " + ETC + "mins");
		} else {
			paintStrs.add("Waiting for first fletch...");
		}
		
		paintStrs.add("Current Task: " + task);
				
//		Calculates the longest strlen for bg width calc
		int longestStrLen = 0, strlen;
		for (String s : paintStrs) {
			strlen = s.length();
			if (strlen > longestStrLen) longestStrLen = strlen;
		}
		
//		Setting up the background.
		g.setColor(Color.BLACK);
		int height = ctx.game.dimensions().height - STR_HEIGHT*paintStrs.size();
		int width = longestStrLen * STR_WIDTH;
		g.drawRect(0, height, width, height);
		g.fillRect(0, height, width, height);
		
//		Drawing the text
		g.setColor(Color.WHITE);
		for (int i = 0; i < paintStrs.size(); i++) {
//			The i+1 is there because drawString's anchor is on the upper left
			int labelHeight = height + (i+1)*STR_HEIGHT; 
			g.drawString(paintStrs.get(i), 0, labelHeight);
		}
	}

	public static int getBeginningLogCount() {
		return beginningLogCount;
	}

//	Should only be set once.
	public static void setBeginningLogCount(int beginningLogCount) {
		if (SimpleFletcher.beginningLogCount == -1) {
			SimpleFletcher.beginningLogCount = beginningLogCount;
		}
	}

}
