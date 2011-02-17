import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JKeyListener extends KeyAdapter {
	private JImageDisplay display;
	
	
	public JKeyListener(JImageDisplay jid)
	{
		System.out.println("initialized");
		display = jid;
	}
	
	public void keyPressed(KeyEvent e) {
		char pressedKey = e.getKeyChar();
		System.out.println(pressedKey);
		//this is only sample code, only for reference if you want to use
		if(pressedKey == 's')
		{
		}
		else if(pressedKey == 'c')
		{
			
		}
		else if(pressedKey == 'm')
		{
		}
		else if(pressedKey == 'w')
		{
		}
	
		//...... you can add more functions
	}
}
