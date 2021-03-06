package com.ccc.util.swing.popup;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.RepaintManager;

//This class may fail to load if sun.awt.DisplayChangedListener does not exist.
class DisplayChangeHandler
	implements sun.awt.DisplayChangedListener, Runnable
{
	// We must keep a strong reference to the DisplayChangedListener,
	//  since SunDisplayChanger keeps only a WeakReference to it.
	private static	DisplayChangeHandler	displayChangeHack;
	static {
		try {
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();
			if ( displayChangeHack == null ) {
				displayChangeHack = new DisplayChangeHandler();
			}
			//Add ourselves as a listener to the GraphicsEnvironment if possible.
			env.getClass()
				.getMethod( "addDisplayChangedListener", new Class[] { sun.awt.DisplayChangedListener.class } )
				.invoke( env, new Object[] { displayChangeHack } );
		} catch (Throwable t) {
			System.out.println( "Could not create DisplayChangeHandler to plug mem leaks due to display changes: " + t );
		}
	}
	
	public void displayChanged()
	{
		EventQueue.invokeLater( this );
	}
	
	public void paletteChanged()
	{
		EventQueue.invokeLater( this );
	}
	
	public void run()
	{
		//Force the RepaintManager to clear out all of the VolatileImage back-buffers that it has cached.
		//	See Sun bug 6209673.
		RepaintManager rm = RepaintManager.currentManager(null);
		Dimension size = rm.getDoubleBufferMaximumSize();
		rm.setDoubleBufferMaximumSize(new Dimension(0, 0));
		rm.setDoubleBufferMaximumSize(size);
	}
}